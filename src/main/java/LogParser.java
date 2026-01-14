import java.util.Optional;

public final class LogParser {

    private LogParser() {
    }

    // Expected format (minimal):
    // YYYY-MM-DD HH:MM:SS LEVEL  Source - Message...
    public static Optional<LogEntry> parse(String line) {
        if (line == null) return Optional.empty();

        String trimmed = line.trim();
        if (trimmed.isEmpty()) return Optional.empty();

        // date time level rest...
        String[] parts = trimmed.split("\\s+", 4);
        if (parts.length < 4) return Optional.empty();

        String date = parts[0];
        String time = parts[1];
        String level = parts[2].toUpperCase();
        String rest = parts[3];

        // Robust separator: whitespace + '-' + whitespace (multiple spaces/tabs ok)
        String[] restParts = rest.split("\\s+-\\s+", 2);
        if (restParts.length < 2) return Optional.empty();

        String source = restParts[0].trim();
        String message = restParts[1].trim();

        if (source.isEmpty() || message.isEmpty()) return Optional.empty();

        return Optional.of(new LogEntry(date, time, level, source, message));
    }
}
