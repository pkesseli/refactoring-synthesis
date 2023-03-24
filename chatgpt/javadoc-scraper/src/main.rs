use clap::Parser;
use std::path::PathBuf;

#[derive(Parser, Debug)]
struct Args {
    #[arg(long)]
    javadoc: bool,
    #[arg(long, default_value_t = String::from("queries"))]
    output_dir: String,
}

fn main() {
    let args = Args::parse();

    let response = reqwest::blocking::get(
        "https://docs.oracle.com/en/java/javase/15/docs/api/deprecated-list.html",
    )
    .unwrap()
    .text()
    .unwrap();

    let document = scraper::Html::parse_document(&response);

    let method_selector =
        scraper::Selector::parse("div#method table.summary-table tbody tr").unwrap();

    let deprecated_methods = document.select(&method_selector);

    let name_selector = scraper::Selector::parse("a").unwrap();
    let comment_selector = scraper::Selector::parse(".deprecation-comment").unwrap();

    let queries_dir = PathBuf::from(args.output_dir);
    if !queries_dir.is_dir() {
        std::fs::create_dir_all(&queries_dir).unwrap();
    }

    for (element, idx) in deprecated_methods.zip(0..) {
        let method_name = element.select(&name_selector).next().unwrap().inner_html();
        let deprecation_comment = element
            .select(&comment_selector)
            .next()
            .map(|element| {
                let mut comment = "".to_owned();
                for text in element.text() {
                    comment += text;
                }
                comment
            })
            .unwrap_or_else(|| "".to_owned());

        let mut query = format!("The method {method_name} is deprecated.\n");
        if args.javadoc {
            query += &format!(
                " The related deprecation comment in Javadoc is \"{deprecation_comment}\"\n"
            );
        }
        // query += "Show me a refactoring example.";
        query += "Give me an executable refactoring example in the following format:\n";
        query += "<<< Before refactoring: <code here>\n";
        query += ">>> After refactoring: <code here>\n";

        let query_dir = queries_dir.join(&idx.to_string());
        if !query_dir.is_dir() {
            std::fs::create_dir_all(&query_dir).unwrap();
        }
        std::fs::write(query_dir.join("query.txt"), query).unwrap();
    }
}
