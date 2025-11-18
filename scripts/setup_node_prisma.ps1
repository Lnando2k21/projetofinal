<#
Script para instalar dependências Node, gerar Prisma Client e aplicar migrations.
Uso: execute a partir do PowerShell:
  .\setup_node_prisma.ps1
#>

$projectDir = Join-Path $PSScriptRoot '..\conecta-bairro-api'
Write-Host "Entrando em: $projectDir"
Set-Location $projectDir

if (-not (Get-Command npm -ErrorAction SilentlyContinue)) {
    Write-Error "npm não encontrado. Instale Node.js e npm."; exit 1
}

Write-Host "Instalando dependências..."
npm install
if ($LASTEXITCODE -ne 0) { Write-Error "npm install falhou."; exit $LASTEXITCODE }

Write-Host "Gerando Prisma Client..."
npx prisma generate --schema=prisma/schema.prisma
if ($LASTEXITCODE -ne 0) { Write-Error "prisma generate falhou."; exit $LASTEXITCODE }

Write-Host "Aplicando migrations (modo dev) — isto pode abrir prompts interativos."
npx prisma migrate dev --name init --schema=prisma/schema.prisma
if ($LASTEXITCODE -ne 0) { Write-Error "prisma migrate falhou."; exit $LASTEXITCODE }

Write-Host "Setup Node/Prisma concluído. Inicie a API com: npm run dev"
