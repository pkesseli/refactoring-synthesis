package uk.ac.ox.cs.refactoring.synthesis.neural;

import java.util.List;

import org.json.JSONObject;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

public class Bedrock {


  // snippet-start:[bedrock-runtime.java2.invoke_claude.main]
  /**
   * Invokes the Anthropic Claude 2 model to run an inference based on the
   * provided input.
   *
   * @param prompt The prompt for Claude to complete.
   * @return The generated response.
   */
  public static String invokeClaude(String prompt) {
    /*
      * The different model providers have individual request and response formats.
      * For the format, ranges, and default values for Anthropic Claude, refer to:
      * https://docs.aws.amazon.com/bedrock/latest/userguide/model-parameters-claude.html
      */

    String claudeModelId = "anthropic.claude-v2";

    // Claude requires you to enclose the prompt as follows:
    String enclosedPrompt = "Human: " + prompt + "\n\nAssistant:";

    BedrockRuntimeClient client = BedrockRuntimeClient.builder()
        .region(Region.US_EAST_1)
        .credentialsProvider(ProfileCredentialsProvider.create())
        .build();

    String payload = new JSONObject()
        .put("prompt", enclosedPrompt)
        .put("max_tokens_to_sample", 10000)
        .put("temperature", 0.2)
        .put("stop_sequences", List.of("\n\nHuman:"))
        .toString();

    InvokeModelRequest request = InvokeModelRequest.builder()
        .body(SdkBytes.fromUtf8String(payload))
        .modelId(claudeModelId)
        .contentType("application/json")
        .accept("application/json")
        .build();

    InvokeModelResponse response = client.invokeModel(request);

    JSONObject responseBody = new JSONObject(response.body().asUtf8String());

    String generatedText = responseBody.getString("completion");

    return generatedText;
  }
}
