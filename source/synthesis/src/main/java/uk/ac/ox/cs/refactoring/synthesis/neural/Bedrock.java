package uk.ac.ox.cs.refactoring.synthesis.neural;

import java.util.ArrayList;
import java.util.Arrays;

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
    String enclosedPrompt = "Human: " + prompt + "\n\nAssistant:\n<code>";

    BedrockRuntimeClient client = BedrockRuntimeClient.builder()
        .region(Region.US_EAST_1)
        .credentialsProvider(ProfileCredentialsProvider.create())
        .build();

    String payload = new JSONObject()
        .put("prompt", enclosedPrompt)
        .put("max_tokens_to_sample", 10000)
        .put("temperature", 0.2)
        .put("stop_sequences", new ArrayList<>(Arrays.asList("\n\nHuman:", "</code>")))
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

  public static String invokeClaude3(String prompt) {
    String modelId = "anthropic.claude-3-sonnet-20240229-v1:0";

    BedrockRuntimeClient client = BedrockRuntimeClient.builder()
        .region(Region.US_EAST_1)
        .credentialsProvider(ProfileCredentialsProvider.create())
        .build();

    // Prepare the JSON payload for the Messages API request
    var payload = new JSONObject()
        .put("anthropic_version", "bedrock-2023-05-31")
        .put("max_tokens", 10000)
        .put("temperature", 0.2)
        .put("stop_sequences", new ArrayList<>(Arrays.asList("</code>")))
        .append("messages", new JSONObject()
            .put("role", "user")
            .append("content", new JSONObject()
                .put("type", "text")
                .put("text", prompt)
            ));

    var request = InvokeModelRequest.builder()
        .body(SdkBytes.fromUtf8String(payload.toString()))
        .modelId(modelId)
        .contentType("application/json")
        .accept("application/json")
        .build();
    
    InvokeModelResponse response = client.invokeModel(request);

    JSONObject responseBody = new JSONObject(response.body().asUtf8String());

    String generatedText = responseBody.getJSONArray("content").getJSONObject(0).getString("text");
    if (responseBody.getString("stop_reason").equals("stop_sequence")) {
      generatedText += responseBody.getString("stop_sequence");
    }

    return generatedText;
  }
}
