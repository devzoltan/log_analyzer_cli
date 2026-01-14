import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        CliOptions opt = CliOptions.parse(args);
        if (!opt.ok) {
            printUsage();
            System.exit(1);
        }

        Path logPath = Path.of(opt.logFile);

        if (!Files.exists(logPath)) {
            System.out.println("Error: file not found: " + logPath);
            System.exit(2);
        }

        long lineCount = 0;
        long infoCount = 0;
        long warnCount = 0;
        long errorCount = 0;
        long unknownCount = 0;

        Map<String, Long> sourceCounts = new HashMap<>();
        List<String> unknownSamples = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(logPath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineCount++;

                if (line.trim().isEmpty()) {
                    continue;
                }

                Optional<LogEntry> entryOpt = LogParser.parse(line);
                if (entryOpt.isEmpty()) {
                    unknownCount++;
                    if (unknownSamples.size() < 5) {
                        unknownSamples.add(line);
                    }
                    continue;
                }

                LogEntry entry = entryOpt.get();

                // Level filter
                if (opt.levelFilter != null && !entry.level().equals(opt.levelFilter)) {
                    continue;
                }

                // level stat (only for included entries)
                switch (entry.level()) {
                    case "INFO" -> infoCount++;
                    case "WARN" -> warnCount++;
                    case "ERROR" -> errorCount++;
                    default -> unknownCount++;
                }

                sourceCounts.merge(entry.source(), 1L, Long::sum);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            System.exit(3);
        }

        System.out.println("File   : " + logPath);
        System.out.println("Lines  : " + lineCount);
        if (opt.levelFilter != null) {
            System.out.println("Filter : level=" + opt.levelFilter);
        }
        System.out.println("INFO   : " + infoCount);
        System.out.println("WARN   : " + warnCount);
        System.out.println("ERROR  : " + errorCount);
        System.out.println("UNKNOWN: " + unknownCount);

        System.out.println();
        System.out.println("Top Sources (top " + opt.topN + "):");
        sourceCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(opt.topN)
                .forEach(e -> System.out.println("- " + e.getKey() + ": " + e.getValue()));

        if (unknownCount > 0) {
            System.out.println();
            System.out.println("UNKNOWN samples (max 5):");
            for (String u : unknownSamples) {
                System.out.println("> " + u);
            }
        }
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  java Main [--top N] [--level INFO|WARN|ERROR] <logfile>");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  java Main sample.log");
        System.out.println("  java Main --top 2 sample.log");
        System.out.println("  java Main --level ERROR sample.log");
        System.out.println("  java Main --level INFO --top 5 sample.log");
    }

    private static final class CliOptions {
        final boolean ok;
        final int topN;
        final String levelFilter; // "INFO" | "WARN" | "ERROR" | null
        final String logFile;

        private CliOptions(boolean ok, int topN, String levelFilter, String logFile) {
            this.ok = ok;
            this.topN = topN;
            this.levelFilter = levelFilter;
            this.logFile = logFile;
        }

        static CliOptions parse(String[] args) {
            int topN = 3;
            String level = null;
            String logFile = null;

            for (int i = 0; i < args.length; i++) {
                String a = args[i];

                if ("--top".equals(a)) {
                    if (i + 1 >= args.length) return new CliOptions(false, 0, null, null);
                    String v = args[++i];
                    try {
                        topN = Integer.parseInt(v);
                        if (topN <= 0) return new CliOptions(false, 0, null, null);
                    } catch (NumberFormatException e) {
                        return new CliOptions(false, 0, null, null);
                    }
                } else if ("--level".equals(a)) {
                    if (i + 1 >= args.length) return new CliOptions(false, 0, null, null);
                    String v = args[++i].toUpperCase();
                    if (!v.equals("INFO") && !v.equals("WARN") && !v.equals("ERROR")) {
                        return new CliOptions(false, 0, null, null);
                    }
                    level = v;
                } else if (a.startsWith("--")) {
                    return new CliOptions(false, 0, null, null);
                } else {
                    // first positional argument = logfile
                    if (logFile != null) return new CliOptions(false, 0, null, null);
                    logFile = a;
                }
            }

            if (logFile == null) return new CliOptions(false, 0, null, null);
            return new CliOptions(true, topN, level, logFile);
        }
    }
}
