@echo off
echo Compilando o projeto...
call mvn clean install

if %errorlevel% neq 0 (
    echo Erro na compilação. Abortando.
    pause
    exit /b %errorlevel%
)
echo Iniciando servidor MCP...
cd target
java -jar servidor-mcp-1.0.0-SNAPSHOT-runner.jar
pause
