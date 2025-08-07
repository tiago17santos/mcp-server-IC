package org.IC.mcpServer.HTTP.Tools;


import org.IC.mcpServer.HTTP.Services.AIService;
import org.IC.mcpServer.HTTP.Services.FileService;

public class SummarizeFileTool {
    private final FileService fileService;
    private final AIService aiService;

    public SummarizeFileTool() {
        this.fileService = new FileService();
        this.aiService = new AIService();
    }

    @Tool("arquivo.resumir")
    public String resumirArquivo(String caminho) {
        String conteudo = fileService.lerArquivo(caminho);
        if (conteudo == null || conteudo.isBlank()) return "Arquivo vazio ou não encontrado.";
        return aiService.resumirTexto(conteudo);
    }
}
