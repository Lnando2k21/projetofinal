<#
Script para compilar e executar o backend Java (Spring Boot).
Requisitos: Maven e JDK 21 instalados e disponíveis no PATH ou via JAVA_HOME.
Uso:
  .\run_java.ps1
#>

$projectDir = Join-Path $PSScriptRoot '..\conecta-bairro-java'
Write-Host "Entrando em: $projectDir"
Set-Location $projectDir

Write-Host "Executando: mvn -U clean package"
mvn -U clean package
if ($LASTEXITCODE -ne 0) { Write-Error "Maven build falhou."; exit $LASTEXITCODE }

Write-Host "Iniciando aplicativo Spring Boot (spring-boot:run)"
mvn spring-boot:run
