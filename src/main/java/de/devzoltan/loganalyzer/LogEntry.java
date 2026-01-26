package de.devzoltan.loganalyzer;

public final class LogEntry {
    private final String date;
    private final String time;
    private final String level;
    private final String source;
    private final String message;

    public LogEntry(String date, String time, String level, String source, String message) {
        this.date = date;
        this.time = time;
        this.level = level;
        this.source = source;
        this.message = message;
    }

    public String date() { return date; }
    public String time() { return time; }
    public String level() { return level; }
    public String source() { return source; }
    public String message() { return message; }
}
