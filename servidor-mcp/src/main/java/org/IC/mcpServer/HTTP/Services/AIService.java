package org.IC.mcpServer.HTTP.Services;

import jakarta.enterprise.context.ApplicationScoped;
import org.IC.mcpServer.HTTP.Tools.LocalLLMTool;

@ApplicationScoped
public class AIService {
    private final LocalLLMTool llmTool = new LocalLLMTool();

    public String responderPergunta(String prompt) {
        return llmTool.responder(prompt);
    }

    public String resumirTexto(String texto) {
        String prompt = "Resuma o seguinte texto em até 10 linhas:\n\n" + texto;
        return responderPergunta(prompt);
    }
}
