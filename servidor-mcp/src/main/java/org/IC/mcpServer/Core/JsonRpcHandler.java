package org.IC.mcpServer.Core;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.IC.mcpServer.HTTP.Tools.LocalLLMTool;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class JsonRpcHandler {

    private final Map<String, Method> methods = new HashMap<>();
    private final Map<String, Object> instances = new HashMap<>();

    @Inject
    public JsonRpcHandler(LocalLLMTool llmTool) {
        try {
            Method method = LocalLLMTool.class.getMethod("responder", String.class);
            methods.put("responderPergunta", method);
            instances.put("responderPergunta", llmTool);
            System.out.println("→ Método registrado: responderPergunta");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao registrar ferramenta responderPergunta", e);
        }
    }

    public String handle(String json) {
        try {
            var mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            var node = mapper.readTree(json);

            String methodName = node.get("method").asText();
            var params = node.get("params");

            Method method = methods.get(methodName);
            if (method == null) throw new RuntimeException("Method not found: " + methodName);

            Object[] paramValues = mapper.treeToValue(params, Object[].class);
            Object result = method.invoke(instances.get(methodName), paramValues);

            var response = mapper.createObjectNode();
            response.put("jsonrpc", "2.0");
            response.set("result", mapper.valueToTree(result));
            response.set("id", node.get("id"));
            return mapper.writeValueAsString(response);
        } catch (Exception e) {
            var mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            var error = mapper.createObjectNode();
            error.put("jsonrpc", "2.0");
            error.putObject("error").put("code", -32603).put("message", e.getMessage());
            return error.toString();
        }
    }
}
