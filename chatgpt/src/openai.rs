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
) -> anyhow::Result<(String, f32)> {
    let mut n_trails = 0;
    let (response, elapsed) = loop {
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
    Ok((choice.message.content.unwrap(), elapsed))
}

pub fn construct_prompt<'a>(
    path_name: &'a str,
    method_name: &'a str,
    args: impl Iterator<Item = &'a str>,
    deprecation_comment: &'a str,
) -> Vec<ChatCompletionRequestMessage> {
    // vec![user(format!(
    //     r#"The method `{path_name}` is deprecated. The related deprecation comment in Javadoc is
    // {deprecation_comment}
    // Help me refactor the following code fragment:
    // <begin Java code>
    // {}
    // <end Java code>

    // Your answer will contain code only. Answer N/A if you think there is no solution."#,
    //     invoke_method(method_name, args)
    // ))]
    vec![
        system("You are an expert in Java."),
        user(format!("I have some Java method calls in my code base but they are deprecated. Refactor them according to \
        the given deprecation comments.\nLet's start with some examples.")),
        user("The method `java.awt.List.addItem(String)` is deprecated. The associated JavaDoc deprecation comment is \n replaced by  add(String) . \n.\nRefactor the following code:\n<begin Java code>\nthis.addItem(param0);\n<end Java code>\nAnswer N/A if there is no solution."),
        assistant("<begin Java code>\nthis.add(param0);\n<end Java code>"),
        // user("The method `java.util.Date.getHours()` is deprecated. \
        // The associated JavaDoc deprecation comment is \"As of JDK version 1.1, replaced by Calendar.get(Calendar.DAY_OF_HOUR).\".\n\
        // Refactor the following code:\n<begin Java code>\nthis.getHours;()\n<end Java code>\nAnswer N/A if there is no solution."),
        // assistant("<begin Java code>\nfinal Calendar calendar = Calendar.getInstance();\n\
        // calendar.setTime(this);\ncalendar.get(Calendar.HOUR_OF_DAY);\n<end Java code>"),
        user("The method `com.sun.jdi.VirtualMachine.canAddMethod()` is deprecated. \
        The associated JavaDoc deprecation comment is \"A JVM TI based JDWP back-end will never set this capability to true.\".\n\
        Refactor the following code:\n<begin Java code>\nthis.canAddMethod();\n<end Java code>\nAnswer N/A if there is no solution."),
        assistant("N/A"),
        user(format!("Now it's time for the real task.\nThe method `{path_name}` is deprecated. \
        The associated JavaDoc deprecation comment is {deprecation_comment}.\nRefactor the following code:\n\
        <begin Java code>\n{};\n<end Java code>\n\nAnswer N/A if there is no solution.", invoke_method(method_name, args)))
    ]
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
