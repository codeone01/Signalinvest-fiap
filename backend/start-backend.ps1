$ErrorActionPreference = 'Stop'
$envFile = Join-Path $PSScriptRoot '.env.local'

if (-not (Test-Path -LiteralPath $envFile)) {
    throw "Arquivo backend/.env.local não encontrado. Copie .env.local.example, preencha os valores Oracle e tente novamente."
}

foreach ($linha in Get-Content -LiteralPath $envFile) {
    $texto = $linha.Trim()
    if ([string]::IsNullOrWhiteSpace($texto) -or $texto.StartsWith('#')) { continue }
    $separador = $texto.IndexOf('=')
    if ($separador -le 0) { continue }
    $nome = $texto.Substring(0, $separador).Trim()
    $valor = $texto.Substring($separador + 1).Trim()
    if ($valor.Length -ge 2 -and (($valor.StartsWith("'") -and $valor.EndsWith("'")) -or ($valor.StartsWith('"') -and $valor.EndsWith('"')))) {
        $valor = $valor.Substring(1, $valor.Length - 2)
    }
    Set-Item -Path "Env:$nome" -Value $valor
}

if ([string]::IsNullOrWhiteSpace($env:FRONTEND_ORIGEM)) {
    $env:FRONTEND_ORIGEM = 'http://localhost:5173'
}

Write-Host 'Variáveis Oracle carregadas somente nesta sessão.' -ForegroundColor Green
mvn spring-boot:run
