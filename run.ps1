param(
  [Parameter(Position=0)]
  [string]$LogFile = ".\sample.log",

  [Parameter(Position=1)]
  [int]$Top = 3,

  [Parameter(Position=2)]
  [ValidateSet("INFO","WARN","ERROR","")]
  [string]$Level = ""
)

$ErrorActionPreference = "Stop"

Remove-Item -Recurse -Force .\out -ErrorAction SilentlyContinue | Out-Null

$MainSources = Get-ChildItem -Path .\src\main\java -Recurse -Filter *.java
if (-not $MainSources) {
  throw "No Java sources found under .\src\main\java"
}

javac -d .\out $MainSources.FullName

$MainClass = "de.devzoltan.loganalyzer.Main"

if ($Level -ne "") {
  java -cp .\out $MainClass --level $Level --top $Top $LogFile
} else {
  java -cp .\out $MainClass --top $Top $LogFile
}
