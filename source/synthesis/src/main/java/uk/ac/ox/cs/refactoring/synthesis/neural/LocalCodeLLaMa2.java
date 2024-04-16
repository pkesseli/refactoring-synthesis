package uk.ac.ox.cs.refactoring.synthesis.neural;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.NoSuchElementException;

public class LocalCodeLLaMa2 implements CodeEngine {
  private final URL host;

  public LocalCodeLLaMa2() throws IOException {
    host = new URL("http://localhost:8080/completion");
  }


  @Override
  public String query(String prompt) throws Exception {
    HttpURLConnection connection = (HttpURLConnection) host.openConnection();
    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", "application/json");

		connection.setDoOutput(true);
		OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
		writer.write("{\"prompt\": \"Show me how to solve n-queens puzzle in Java. \",\"n_predict\": 256}");
		writer.flush();
		writer.close();
		connection.getOutputStream().close();

    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
      BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

      return response.toString();
    } else {
      throw new NoSuchElementException();
    }
  }

  @Override
  public List<String> queryN(String prompt, int beamSize) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'queryN'");
  }
  
}
