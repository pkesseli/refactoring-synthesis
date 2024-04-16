package uk.ac.ox.cs.refactoring.synthesis.neural;

import de.kherud.llama.InferenceParameters;
import de.kherud.llama.LlamaModel;
import de.kherud.llama.ModelParameters;

public class LocalCodeLLaMa2 implements CodeEngine {
  @Override
  public String query(String prompt) throws Exception {
    String homeDir = System.getProperty("user.home");
    ModelParameters modelParams = new ModelParameters()
      .setNCtx(4096)
      .setModelFilePath(homeDir + "/models/codellama-7b-instruct.Q5_K_M.gguf");
    // String instructed = String.format("[INST] Write code to solve the following coding problem that obeys the constraints and passes the example test cases. Please wrap your code answer using ```:\n%s\n[/INST]", prompt);
    InferenceParameters inferParams = new InferenceParameters(prompt)
      .setTemperature((float) 0.2);
    try (LlamaModel model = new LlamaModel(modelParams)) {
      String response = model.complete(inferParams);
      return response;
    }
  }

  @Override
  public String query(Prompt prompt) throws Exception {
    return query(String.format("[INST]\n%s[/INST]", prompt.toString()));
  }
  
}