use std::time::Instant;

use anyhow::Context;
use async_openai::{
    config::OpenAIConfig,
    types::{
        ChatCompletionRequestAssistantMessage, ChatCompletionRequestMessage,
        ChatCompletionRequestSystemMessage, ChatCompletionRequestUserMessage,
        ChatCompletionRequestUserMessageContent, CreateChatCompletionRequestArgs, Role,
    },
    Client,
};

const MODEL: &str = "gpt-3.5-turbo-0301";

pub async fn send_request(
    client: &Client<OpenAIConfig>,
    msgs: Vec<ChatCompletionRequestMessage>,
) -> anyhow::Result<String> {
    let mut n_trails = 0;
    let (response, _) = loop {
        let mut request = CreateChatCompletionRequestArgs::default();
        request
            .model(MODEL)
            .messages(msgs.clone())
            .temperature(0f32);
        let request = request.build().unwrap();
        let now = Instant::now();
        let response = client.chat().create(request).await;
        let elapsed = now.elapsed().as_secs_f32();
        match response {
            Ok(response) => break (response, elapsed),
            Err(openai_error) => {
                n_trails += 1;
                if n_trails >= 9 {
                    return (Err(anyhow::anyhow!(openai_error))).context("failed after 10 tries");
                }
            }
        }
    };

    let choice = response.choices.into_iter().next().unwrap();
    Ok(choice.message.content.unwrap())
}

pub fn construct_prompt<'a>(
    path_name: &'a str,
    method_name: &'a str,
    args: impl Iterator<Item = &'a str>,
    deprecation_comment: &'a str,
) -> Vec<ChatCompletionRequestMessage> {
    vec![user(format!(
        r#"The method `{path_name}` is deprecated. The related deprecation comment in Javadoc is 
{deprecation_comment}
Help me refactor the following code fragment:
<begin Java code>
{}
<end Java code>
        
Your answer will contain code only. Answer N/A if you think there is no solution."#,
        invoke_method(method_name, args)
    ))]
}

pub fn invoke_method<'a>(method_name: &'a str, args: impl Iterator<Item = &'a str>) -> String {
    format!(
        "this.{method_name}({})",
        args.enumerate()
            .map(|(index, _)| format!("param{index}"))
            .collect::<Vec<_>>()
            .join(", ")
    )
}

fn system<S>(s: S) -> ChatCompletionRequestMessage
where
    S: ToString,
{
    ChatCompletionRequestMessage::System(ChatCompletionRequestSystemMessage {
        content: Some(s.to_string()),
        role: Role::System,
        name: None,
    })
}

fn user<S>(s: S) -> ChatCompletionRequestMessage
where
    S: ToString,
{
    ChatCompletionRequestMessage::User(ChatCompletionRequestUserMessage {
        content: Some(ChatCompletionRequestUserMessageContent::Text(s.to_string())),
        role: Role::User,
        name: None,
    })
}

fn assistant<S>(s: S) -> ChatCompletionRequestMessage
where
    S: ToString,
{
    ChatCompletionRequestMessage::Assistant(ChatCompletionRequestAssistantMessage {
        content: Some(s.to_string()),
        role: Role::Assistant,
        tool_calls: None,
        function_call: None,
        name: None,
    })
}
