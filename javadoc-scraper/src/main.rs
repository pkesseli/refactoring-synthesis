mod openai;

use std::path::PathBuf;

use async_openai::{config::OpenAIConfig, Client};
use clap::Parser;

#[derive(Parser)]
struct Cli {
    #[arg(short, long)]
    output: PathBuf,
}

#[tokio::main]
async fn main() -> anyhow::Result<()> {
    let cli = Cli::parse();
    // download the target HTML document
    let response =
        reqwest::get("https://docs.oracle.com/en/java/javase/15/docs/api/deprecated-list.html")
            .await?;
    let html_content = response.text().await?;
    let document = scraper::Html::parse_document(&html_content);
    let method_selector =
        scraper::Selector::parse(".deprecated-summary#method").expect("wrong css selector");
    let deprecated_methods = document.select(&method_selector);
    let item_selector =
        scraper::Selector::parse(".col-deprecated-item-name").expect("wrong css selector");
    let comment_selector =
        scraper::Selector::parse(".deprecation-comment").expect("wrong css selector");

    let deprecated_methods = deprecated_methods
        .flat_map(|deprecated_methods| {
            deprecated_methods
                .select(&item_selector)
                .zip(deprecated_methods.select(&comment_selector))
                .map(|(item, comment)| {
                    item.text()
                        .next()
                        .map(|item| (item, comment.text().collect::<Vec<_>>().join(" ")))
                        .ok_or(anyhow::anyhow!("failed to extract items"))
                })
        })
        .collect::<anyhow::Result<Vec<_>>>()?;

    let config = OpenAIConfig::new().with_org_id("org-VrmlZ45kWPFIWoRet1H083yR");

    let client = Client::with_config(config);

    let responses = futures::future::join_all(deprecated_methods.iter().map(
        |(path_name, deprecation_comment)| {
            let (_, method_name, args) = decompose_path(path_name);
            let prompt =
                openai::construct_prompt(path_name, method_name, args, deprecation_comment);
            async {
                (
                    serde_json::to_value(&prompt),
                    openai::send_request(&client, prompt).await,
                    // anyhow::Result::<String, anyhow::Error>::Ok("".to_owned())
                )
            }
        },
    ))
    .await;

    let mut classes = serde_json::Map::new();

    for ((path_name, _), (prompt, response)) in deprecated_methods.into_iter().zip(responses) {
        match response {
            Ok(response) => {
                let path_name = path_name.replace("​", "");
                let response = response.replace("​", "");
                let prompt = remove_strange_char(prompt?);
                let (class_name, method_name, args) = decompose_path(&path_name);
                let value = serde_json::json!({
                    "prompt": prompt,
                    "response": response.clone(),
                    "before": openai::invoke_method(method_name, args),
                    "after": extract_code(response),
                });
                match classes.entry(class_name) {
                    serde_json::map::Entry::Vacant(vacant) => {
                        let mut methods = serde_json::Map::new();
                        methods.insert(
                            method_name.to_string(),
                            serde_json::Value::Array(vec![value]),
                        );
                        let value = serde_json::Value::Object(methods);
                        vacant.insert(value);
                    }
                    serde_json::map::Entry::Occupied(mut occupied) => {
                        let serde_json::Value::Object(methods) = occupied.get_mut() else {
                            unreachable!()
                        };
                        match methods.entry(method_name) {
                            serde_json::map::Entry::Vacant(vacant) => {
                                vacant.insert(serde_json::Value::Array(vec![value]));
                            }
                            serde_json::map::Entry::Occupied(mut occupied) => {
                                let serde_json::Value::Array(contents) = occupied.get_mut() else {
                                    unreachable!()
                                };
                                contents.push(value);
                            }
                        }
                    }
                }
            }
            Err(err) => eprintln!("failed to query {path_name} with {err}"),
        }
    }

    let summary = serde_json::Value::Object(classes);

    std::fs::write(cli.output, summary.to_string())?;

    Ok(())
}

fn remove_strange_char(value: serde_json::Value) -> serde_json::Value {
    match value {
        serde_json::Value::Null | serde_json::Value::Bool(_) | serde_json::Value::Number(_) => {
            value
        }
        serde_json::Value::String(literal) => serde_json::Value::String(literal.replace("​", "")),
        serde_json::Value::Array(arr) => serde_json::Value::Array(
            arr.into_iter()
                .map(|value| remove_strange_char(value))
                .collect(),
        ),
        serde_json::Value::Object(map) => serde_json::Value::Object(
            map.into_iter()
                .map(|(key, value)| (key.replace("​", ""), remove_strange_char(value)))
                .collect(),
        ),
    }
}

fn extract_code(response: String) -> Option<String> {
    if response.contains("N/A") {
        None
    } else if response.contains("<begin Java code>") {
        const BEGIN: &str = "<begin Java code>";
        const END: &str = "<end Java code>";
        let begin_pos = response.find(BEGIN)? + BEGIN.len();
        let end_pos = response.find(END)?;

        Some(response[begin_pos..end_pos].to_owned())
    } else {
        Some(response)
    }
}

fn decompose_path(path: &str) -> (&str, &str, impl Iterator<Item = &str>) {
    let pos = if let Some(variadic_pos) = path.find("...") {
        path[..variadic_pos].rfind('.').unwrap()
    } else if let Some(inner_class_pos) = path.find("Position.Bias") {
        path[..inner_class_pos].rfind('.').unwrap()
    } else if let Some(inner_class_pos) = path.find("HTML.Tag") {
        path[..inner_class_pos].rfind('.').unwrap()
    } else {
        path.rfind('.').unwrap()
    };
    let class_name = &path[..pos];
    let method_invoke = &path[pos + 1..];

    let left_paren = method_invoke.find('(').unwrap();
    let right_paren = method_invoke.find(')').unwrap();

    let method_name = &method_invoke[..left_paren];
    let args = &method_invoke[left_paren + 1..right_paren];

    if args.is_empty() {
        return (class_name, method_name, either::Left(std::iter::empty()));
    }

    return (class_name, method_name, either::Right(args.split(", ")));
}
