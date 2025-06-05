package org.IC.mcpServer.HTTP.Tools;

public class MathTool {
    @Tool("math.somar")
    public int somar(int a, int b) {
        return a + b;
    }

    @Tool("math.subtrair")
    public int subtrair(int a, int b) {
        return a - b;
    }
}
