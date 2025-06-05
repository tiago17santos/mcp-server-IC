package org.IC.mcpServer.STDIO.Tools;

public class MathTools implements Tools {

    @Override
    public Object execute(String methodName, Object[] args) {
        return switch (methodName) {
            case "somar" -> somar((int) args[0], (int) args[1]);
            case "subtrair" -> subtrair((int) args[0], (int) args[1]);
            default -> throw new IllegalArgumentException("Método desconhecido: " + methodName);
        };
    }

    public int somar(int a, int b) {
        return a + b;
    }

    public int subtrair(int a, int b) {
        return a - b;
    }

}

