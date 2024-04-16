package uk.ac.ox.cs.refactoring.synthesis.neural;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

public class GPT4 implements CodeEngine {
  private static final int TIMEOUT = 300;
  public final OpenAiService service = new OpenAiService(System.getenv("OPENAI_API_KEY"), Duration.ofSeconds(TIMEOUT));
  private static final String MODEL = "gpt-4-turbo-preview";

  public List<String> queryN(ChatMessage message, int beamSize) {
    ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
      .builder()
      .model(MODEL)
      .messages(Collections.singletonList(message))
      .n(beamSize)
      .maxTokens(4096)
      .build();

    return service.createChatCompletion(chatCompletionRequest)
      .getChoices()
      .stream()
      .map(response -> response.getMessage().getContent())
      .collect(Collectors.toList());
  }

	@Override
  public String query(String prompt) {
    ChatMessage message = new ChatMessage(ChatMessageRole.USER.value(), prompt);
    return query(message);
  }

  public String query(ChatMessage message) {
    return queryN(message, 1).get(0);
  }
}