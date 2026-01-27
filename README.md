````md
# Java – Log Analyzer CLI

Ein kleines, bewusst simples Java-CLI-Tool zum Einlesen einer Logdatei und zum Ausgeben von Statistiken.
Ziel: **GitHub-/Praktikum-tauglich**, nachvollziehbar, reproduzierbar ausführbar (inkl. Tests).

---

## Features

- Liest eine Logdatei **zeilenweise**
- Zählt:
  - Gesamtanzahl Zeilen
  - Level: `INFO`, `WARN`, `ERROR`, `UNKNOWN`
- Optionaler Filter: nur ein bestimmtes Log-Level berücksichtigen (`--level`)
- Gibt **Top-N Sources** aus (`--top`)
- Robustes Verhalten: nicht parsebare Zeilen werden als `UNKNOWN` gezählt
- Hilfe: `-h` / `--help`

---

## Voraussetzungen

- **JDK 17** installiert
- Windows PowerShell (PowerShell 7 empfohlen)
- Maven ist **nicht nötig**, wenn der **Maven Wrapper** genutzt wird (`mvnw` / `mvnw.cmd`)

---

## Projektstruktur

```text
log_analyzer_cli/
  src/
    main/java/de/devzoltan/loganalyzer/
      Main.java
      LogEntry.java
      LogParser.java
    test/java/de/devzoltan/loganalyzer/
      LogParserTest.java
  sample.log
  run.ps1
  pom.xml
````

---

## Quick Start (empfohlen)

### Option A: PowerShell (`run.ps1`)

Im Projekt-Root:

```powershell
.\run.ps1
```

Falls PowerShell das Script wegen **ExecutionPolicy** blockiert:

```powershell
Unblock-File .\run.ps1
.\run.ps1
```

Oder einmalig (ohne Policy-Änderung):

```powershell
powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\run.ps1
```

Beispiele:

```powershell
.\run.ps1 .\sample.log 3
.\run.ps1 .\sample.log 5 ERROR
```

### Option B: Standard (JAR, plattformunabhängig)

```powershell
.\mvnw.cmd -B -DskipTests package
java -jar .\target\log-analyzer-cli-1.0.0.jar .\sample.log --top 3
```

---

## Standard Build & Run (Maven Wrapper)

### Tests

```powershell
.\mvnw.cmd -B test
```

### Paket bauen (JAR)

```powershell
.\mvnw.cmd -B -DskipTests package
```

### Run (JAR)

```powershell
java -jar .\target\log-analyzer-cli-1.0.0.jar .\sample.log
```

Mit Optionen:

```powershell
java -jar .\target\log-analyzer-cli-1.0.0.jar .\sample.log --top 2
java -jar .\target\log-analyzer-cli-1.0.0.jar .\sample.log --level ERROR
java -jar .\target\log-analyzer-cli-1.0.0.jar .\sample.log --level INFO --top 5
```

---

## Build (manuell, ohne Maven)

Wichtig: Vor dem Start muss zuerst kompiliert werden (sonst ist `.\out` leer).

```powershell
Remove-Item -Recurse -Force .\out -ErrorAction SilentlyContinue
$MainSources = Get-ChildItem -Path .\src\main\java -Recurse -Filter *.java
javac -d .\out $MainSources.FullName
```

### Run (Classpath)

```powershell
java -cp .\out de.devzoltan.loganalyzer.Main .\sample.log
```

### CLI-Argumente

```powershell
java -cp .\out de.devzoltan.loganalyzer.Main [--top N] [--level INFO|WARN|ERROR] <logfile>
```

---

## Beispielausgabe

```text
File   : .\sample.log
Lines  : 11
INFO   : 5
WARN   : 3
ERROR  : 3
UNKNOWN: 0

Top Sources (top 3):
- AuthService: 4
- OrderService: 4
- PaymentService: 3
```

Mit Filter:

```text
File   : .\sample.log
Lines  : 11
Filter : level=ERROR
INFO   : 0
WARN   : 0
ERROR  : 3
UNKNOWN: 0

Top Sources (top 5):
- AuthService: 1
- OrderService: 1
- PaymentService: 1
```

---

## Screenshots (für GitHub)

Empfohlene Screenshots (Windows):

1. Quick Start:

```powershell
.\run.ps1
```

2. Filter & Top:

```powershell
.\run.ps1 .\sample.log 5 ERROR
```

3. Tests grün:

```powershell
.\mvnw.cmd -B test
```

Speichere sie z. B. unter:

```text
docs/screenshots/
  01-run-default.png
  02-run-filter-error.png
  03-mvn-test.png
```

---

## Hinweise zur Repo-Hygiene

* `out/` (manuelles Build-Verzeichnis) und `target/` (Maven Output) gehören **nicht** ins Git
* dafür existiert `.gitignore`

---

## Lizenz

Dieses Projekt steht unter der [MIT-Lizenz](LICENSE).
Copyright (c) 2026 Zoltan Lung (devzoltan)

```
::contentReference[oaicite:0]{index=0}
```
