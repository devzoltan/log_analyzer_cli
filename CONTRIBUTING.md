# Contributing

Danke für dein Interesse. Dieses Repo ist bewusst klein gehalten.

## Lokales Setup

### Voraussetzungen
- JDK 17
- Git
- Optional: PowerShell 7

### Build & Tests
```bash
./mvnw test
```

Windows (PowerShell):
```powershell
.\mvnw.cmd test
```

## Commit-Konvention (kurz)

Beispiele:
- `fix: handle empty lines`
- `feat: add --help option`
- `docs: update README`

## Pull Requests
- Kleine, fokussierte PRs
- Tests müssen grün sein
- Keine Secrets/Keys im Repo
