package org.IC.mcpServer.HTTP.Tools;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileTool {

    private static final Tika tika = new Tika();
    private static final Path BASE_DIR = Paths.get("C:/Users/tiago/Desktop/JAVA/IC/3 SEMESTRE/filesystem").normalize();

    private boolean foraDoDiretorioPermitido(Path caminho) {
        try {
            return !caminho.toRealPath().startsWith(BASE_DIR);
        } catch (IOException e) {
            return true;
        }
    }

    @Tool("arquivo.ler")
    public String ler(String diretorio){

        try{
            Path path = Paths.get(diretorio).normalize();

            if (foraDoDiretorioPermitido(path)) return "Acesso negado: fora do diretório permitido.";

            if (!Files.exists(path) || !Files.isRegularFile(path)) return "Arquivo não encontrado: " + diretorio;

            // Limita tamanho para evitar retornos muito grandes
            long tamanho = Files.size(path);
            if (tamanho > 100_000) return "Arquivo muito grande para leitura (" + tamanho + " bytes).";


            String tipo = tika.detect(path);
            return switch (tipo) {
                case "application/pdf", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                     "text/plain" -> tika.parseToString(path);
                default -> "Tipo de arquivo não suportado: " + tipo;
            };

        }catch (IOException e) {
            return "Erro ao ler o arquivo: " + e.getMessage();
        } catch (TikaException e) {
            throw new RuntimeException(e);
        }
    }

    @Tool("arquivo.listar")
    public String listar(String diretorio, String extensao) {
        try {
            Path dir = Paths.get(diretorio).normalize();
            if (foraDoDiretorioPermitido(dir)) return "Acesso negado: fora do diretório permitido.";

            if (!Files.exists(dir) || !Files.isDirectory(dir)) return "Diretório não encontrado: " + diretorio;


            List<String> arquivos = Files.list(dir)
                    .filter(p -> extensao == null || p.getFileName().toString().endsWith(extensao))
                    .map(p -> p.getFileName().toString())
                    .collect(Collectors.toList());

            return arquivos.isEmpty()
                    ? "Nenhum arquivo com extensão " + extensao + " encontrado."
                    : String.join("\n", arquivos);

        } catch (IOException e) {
            return "Erro ao listar arquivos: " + e.getMessage();
        }
    }

    @Tool("arquivo.buscar")
    public String buscar(String diretorio, String termo) {
        try {
            Path path = Paths.get(diretorio).normalize();
            if (foraDoDiretorioPermitido(path)) return "Acesso negado: fora do diretório permitido.";

            if (!Files.exists(path) || !Files.isRegularFile(path)) return "Arquivo não encontrado: " + diretorio;

            List<String> linhas = Files.readAllLines(path);
            return linhas.stream()
                    .filter(linha -> linha.contains(termo))
                    .collect(Collectors.joining("\n"));

        } catch (IOException e) {
            return "Erro ao buscar no arquivo: " + e.getMessage();
        }
    }
}
