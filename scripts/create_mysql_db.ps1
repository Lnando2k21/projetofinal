<#
Criar banco MySQL e usuário para o projeto Conecta Bairro.
Uso: execute este script no PowerShell. Ele pedirá a senha do root de forma segura.
Exemplo: .\create_mysql_db.ps1
#>

param(
    [string]$RootUser = 'root',
    [string]$DbName = 'conecta_bairro',
    [string]$AppUser = 'conecta_user',
    [string]$AppPassword = 'conecta_pass'
)

if (-not (Get-Command mysql -ErrorAction SilentlyContinue)) {
    Write-Error "O cliente 'mysql' não foi encontrado no PATH. Instale MySQL ou adicione o cliente ao PATH."; exit 1
}

$secure = Read-Host -AsSecureString "Senha do usuário $RootUser (entrada escondida)"
$bstr = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($secure)
$rootPwd = [Runtime.InteropServices.Marshal]::PtrToStringAuto($bstr)
[Runtime.InteropServices.Marshal]::ZeroFreeBSTR($bstr)

Write-Host "Criando banco '$DbName' e usuário '$AppUser' (host=localhost) ..."

$sql = @"
CREATE DATABASE IF NOT EXISTS `$DbName` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS '$AppUser'@'localhost' IDENTIFIED BY '$AppPassword';
GRANT ALL PRIVILEGES ON `$DbName`.* TO '$AppUser'@'localhost';
FLUSH PRIVILEGES;
"@

try {
    $tempFile = [System.IO.Path]::GetTempFileName()
    Set-Content -Path $tempFile -Value $sql -Encoding UTF8
    & mysql -u $RootUser -p$rootPwd < $tempFile
    Remove-Item $tempFile -Force
    Write-Host "Banco e usuário criados (ou já existiam)."
} catch {
    Write-Error "Erro ao executar comandos MySQL: $_"
    exit 1
}
