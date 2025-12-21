param(
    [string]$Board = (Join-Path $PSScriptRoot 'samples\board-invalid.csv'),
    [int]$Mode = 27
)

$ErrorActionPreference = 'Stop'

$root = $PSScriptRoot
$srcDir = Join-Path $root 'src'
$outDir = Join-Path $root 'out'

if (-not (Test-Path $srcDir)) {
    Write-Error "Source folder not found: $srcDir"
}

# Create output directory
if (-not (Test-Path $outDir)) { New-Item -ItemType Directory -Path $outDir | Out-Null }

# Compile
$sources = Get-ChildItem -Path $srcDir -Filter *.java | ForEach-Object { $_.FullName }
if ($sources.Count -eq 0) { Write-Error "No .java files found in $srcDir" }

Write-Host "Compiling sources to $outDir ..."
& javac -d $outDir @sources

# Run
if (-not (Test-Path $Board)) {
    Write-Error "Board file not found: $Board"
}

Write-Host "Running: mode=$Mode board=$Board" -ForegroundColor Cyan
& java -cp $outDir Main $Board $Mode
