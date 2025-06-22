package org.IC.mcpServer.Core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.IC.mcpServer.HTTP.Tools.Tool;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ApplicationScoped
public class JsonRpcHandler {

    private static final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, Method> methods = new HashMap<>();
    private final Map<String, Object> instances = new HashMap<>();

    @Inject
    public JsonRpcHandler() {
        Reflections reflections = new Reflections("org.IC.mcpServer.HTTP.Tools", Scanners.MethodsAnnotated);

        Set<Method> annotatedMethods = reflections.get(Scanners.MethodsAnnotated.with(Tool.class).as(Method.class));

        for (Method method : annotatedMethods) {
            Tool tool = method.getAnnotation(Tool.class);
            String name = tool.value();
            try {
                Object instance = method.getDeclaringClass().getDeclaredConstructor().newInstance();
                methods.put(name, method);
                instances.put(name, instance);
                System.out.println("✓ Registrado: " + name + " -> " + method.getDeclaringClass().getSimpleName());
            } catch (Exception e) {
                System.out.println("✗ Falha ao registrar " + name + ": " + e.getMessage());
            }
        }
    }

    public String handle(String json) {
        try {
            JsonNode node = mapper.readTree(json);
            if (!node.has("method")) throw new RuntimeException("Campo 'method' ausente no JSON");

            String methodName = node.get("method").asText();
            JsonNode params = node.get("params");

            Method method = methods.get(methodName);
            if (method == null) throw new RuntimeException("Method not found: " + methodName);

            Object result = method.invoke(instances.get(methodName),
                    (Object[]) mapper.treeToValue(params, Object[].class));

            ObjectNode response = mapper.createObjectNode();
            response.put("jsonrpc", "2.0");
            response.set("result", mapper.valueToTree(result));
            response.set("id", node.get("id"));
            return mapper.writeValueAsString(response);
        } catch (Exception e) {
            ObjectNode error = mapper.createObjectNode();
            error.put("jsonrpc", "2.0");
            error.putObject("error").put("code", -32603).put("message", e.getMessage());
            return error.toString();
        }
    }
}
