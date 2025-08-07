package org.IC.mcpServer.HTTP.Tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LocalLLMTool {

    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    @Tool("responderPergunta")
    public String responder(String pergunta) {
        try {
            ObjectNode body = mapper.createObjectNode();
            body.put("model", "mistral");
            body.put("prompt", pergunta);
            body.put("stream", false);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(OLLAMA_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectNode json = (ObjectNode) mapper.readTree(response.body());
            return json.has("response") ? json.get("response").asText() : "Resposta inválida do LLM";

        } catch (IOException | InterruptedException e) {
            return "Erro ao chamar LLM local: " + e.getMessage();
        }
    }
}
