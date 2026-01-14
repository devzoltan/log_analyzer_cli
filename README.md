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

---

## Voraussetzungen

- **Java 25 (LTS)** installiert
- Windows PowerShell (PowerShell 7 empfohlen)
- Maven ist **nicht nötig**, wenn der **Maven Wrapper** genutzt wird

---

## Projektstruktur
```text
log_analyzer_cli/
  src/
    main/
      java/
        Main.java
        LogEntry.java
        LogParser.java
    test/
      java/
        LogParserTest.java
  sample.log
  run.ps1
  pom.xml
  mvnw
  mvnw.cmd
  .mvn/
    wrapper/
  .gitignore
  LICENSE
  README.md
```

---

## Log-Format (Erwartung)

Minimaler Aufbau pro Zeile:
```text
YYYY-MM-DD HH:MM:SS LEVEL Source - Message
```

Beispiel:
```text
2026-01-14 10:00:01 INFO AuthService - Login ok (user=devzoltan)
```

Wenn eine Zeile **nicht** diesem Muster entspricht, wird sie als `UNKNOWN` gezählt.

---

## Quick Start (empfohlen)

Im Projekt-Root:
```powershell
.\run.ps1
```

Das Script:

- kompiliert die Java-Dateien nach `.\out`
- startet das Programm mit `.\sample.log`

---

## Script-Nutzung (run.ps1)
```powershell
.\run.ps1
.\run.ps1 .\sample.log 2
.\run.ps1 .\sample.log 5 ERROR
```

Parameter:

1. **LogFile** (Default: `.\sample.log`)
2. **Top** (Default: `3`)
3. **Level** (Default: kein Filter; Werte: `INFO|WARN|ERROR`)

---

## Direkte CLI-Nutzung (ohne Script)

### Build (manuell)

Wichtig: Vor dem Start muss zuerst kompiliert werden (sonst ist `.\out` leer).
```powershell
Remove-Item -Recurse -Force .\out -ErrorAction SilentlyContinue
javac -d .\out (Get-ChildItem -Recurse -Filter *.java).FullName
```

### Run
```powershell
java -cp .\out Main .\sample.log
```

### CLI-Argumente
```powershell
java -cp .\out Main [--top N] [--level INFO|WARN|ERROR] <logfile>
```

Beispiele:
```powershell
java -cp .\out Main --top 2 .\sample.log
java -cp .\out Main --level ERROR .\sample.log
java -cp .\out Main --level INFO --top 5 .\sample.log
```

---

## Tests (JUnit 5)

### Empfohlen: Maven Wrapper (keine lokale Maven-Installation nötig)

**Windows (PowerShell):**
```powershell
.\mvnw.cmd test
```

**Linux/macOS:**
```bash
./mvnw test
```

### Alternative: Mit installiertem Maven
```powershell
mvn test
```

---

## Exit-Codes

- `1` Ungültige Argumente (z. B. falscher Parameter)
- `2` Datei nicht gefunden
- `3` IO-Fehler beim Lesen der Datei

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

## Hinweise zur Repo-Hygiene

- `out/` (manuelles Build-Verzeichnis) und `target/` (Maven Output) gehören **nicht** ins Git
- dafür existiert `.gitignore`

---

## Lizenz

Dieses Projekt steht unter der [MIT-Lizenz](LICENSE).  
Copyright (c) 2026 Zoltan Lung (devzoltan)
