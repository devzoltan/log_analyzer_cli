package de.devzoltan.loganalyzer;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class LogParserTest {

    @Test
    void parse_validLine_returnsEntry() {
        String line = "2026-01-14 10:00:01 INFO AuthService - Login ok (user=devzoltan)";
        Optional<LogEntry> opt = LogParser.parse(line);

        assertTrue(opt.isPresent());
        LogEntry e = opt.get();

        assertEquals("2026-01-14", e.date());
        assertEquals("10:00:01", e.time());
        assertEquals("INFO", e.level());
        assertEquals("AuthService", e.source());
        assertTrue(e.message().startsWith("Login ok"));
    }

    @Test
    void parse_emptyLine_returnsEmpty() {
        assertTrue(LogParser.parse("   ").isEmpty());
    }

    @Test
    void parse_missingDelimiter_returnsEmpty() {
        String line = "2026-01-14 10:00:01 INFO AuthService Login ok";
        assertTrue(LogParser.parse(line).isEmpty());
    }

    @Test
    void parse_allowsFlexibleWhitespaceAroundDash() {
        String line = "2026-01-14 10:00:01 INFO AuthService    -     Login ok";
        Optional<LogEntry> opt = LogParser.parse(line);
        assertTrue(opt.isPresent());
        assertEquals("AuthService", opt.get().source());
    }
}
