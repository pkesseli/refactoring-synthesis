package uk.ac.ox.cs.refactoring.synthesis.neural;

import java.time.Duration;
import java.util.Collections;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

/** Base engine interfacing OpenAi API */
public class QueryEngine {
  private static final int TIMEOUT = 300;
  public final OpenAiService service = new OpenAiService(System.getenv("OPENAI_API_KEY"), Duration.ofSeconds(TIMEOUT));
  public static final String MODEL = "gpt-4-turbo-preview";

  public String query(String prompt) {
    ChatMessage message = new ChatMessage(ChatMessageRole.USER.value(), prompt);
    return query(message);
  }

  public String query(ChatMessage message) {
    ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
      .builder()
      .model(MODEL)
      .messages(Collections.singletonList(message))
      .maxTokens(4096)
      .build();

    ChatMessage responseMessage = service.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage();

    return responseMessage.getContent();
  }
}
