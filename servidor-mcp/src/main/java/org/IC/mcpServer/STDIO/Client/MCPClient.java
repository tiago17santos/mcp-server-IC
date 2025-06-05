package org.IC.mcpServer.STDIO.Client;

import java.io.*;

public class MCPClient {
    public static void main(String[] args) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("java", "-jar", "target/servidor-mcp-1.0.0-SNAPSHOT-runner.jar");

        Process process = pb.start();

        // Envia JSON para a entrada padrão do processo
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        writer.write("{\"jsonrpc\":\"2.0\",\"method\":\"math.somar\",\"params\":[10, 5],\"id\":\"1\"}");
        writer.newLine(); // importante!
        writer.flush();

        // Lê a resposta do processo
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        System.out.println("Resposta do servidor:");
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            if (line.contains("result") || line.contains("error")) break;
        }

        // Opcional: encerra o processo se desejar
        process.destroy();
    }
}

