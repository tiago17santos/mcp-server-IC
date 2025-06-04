package org.IC.mcpServer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import org.IC.mcpServer.Tools.MathTools;
import org.IC.mcpServer.Tools.Tool;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

@Command(name = "mcpserver", mixinStandardHelpOptions = true)
public class MCPServer implements Callable<Integer> {

    @Inject
    ObjectMapper objectMapper;

    Map<String, Tool> tools = new HashMap<>();

    public MCPServer() {
        tools.put("math", new MathTools());
        // futuros: tools.put("file", new FileTools());
    }


    public Integer call() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;

        while ((line = reader.readLine()) != null) {
            try {
                JsonNode input = objectMapper.readTree(line);

                String method = input.get("method").asText(); // ex: "math.somar"
                String id = input.get("id").asText();

                String[] parts = method.split("\\.");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Formato de método inválido. Use: ferramenta.metodo");
                }

                String toolName = parts[0];
                String methodName = parts[1];

                Tool tool = tools.get(toolName);
                if (tool == null) {
                    throw new IllegalArgumentException("Ferramenta desconhecida: " + toolName);
                }

                JsonNode params = input.get("params");
                Object[] args = objectMapper.convertValue(params, Object[].class);
                Object result = tool.execute(methodName, args);

                ObjectNode responseNode = objectMapper.createObjectNode();
                responseNode.put("jsonrpc", "2.0");
                responseNode.set("result", objectMapper.valueToTree(result));
                responseNode.put("id", id);

                System.out.println(objectMapper.writeValueAsString(responseNode));

            } catch (Exception e) {
                ObjectNode errorNode = objectMapper.createObjectNode();
                errorNode.put("jsonrpc", "2.0");
                ObjectNode error = objectMapper.createObjectNode();
                error.put("code", -32601);
                error.put("message", e.getMessage());
                errorNode.set("error", error);
                errorNode.put("id", "null");

                System.out.println(objectMapper.writeValueAsString(errorNode));
            }
        }

        return 0;
    }

    public static void main(String[] args) {
        new CommandLine(new MCPServer()).execute(args);
    }
}
