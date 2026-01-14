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
javac -d .\out (Get-ChildItem -Recurse -Filter *.java).FullName

if ($Level -ne "") {
  java -cp .\out Main --level $Level --top $Top $LogFile
} else {
  java -cp .\out Main --top $Top $LogFile
}
