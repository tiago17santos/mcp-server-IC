package org.IC.mcpServer.HTTP.Services;

import jakarta.enterprise.context.ApplicationScoped;
import org.IC.mcpServer.HTTP.Tools.FileTool;

@ApplicationScoped
public class FileService {
    private final FileTool fileTool = new FileTool();

    public String lerArquivo(String caminho) {
        return fileTool.ler(caminho);
    }

}
