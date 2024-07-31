package com.extrc.view.console;

import static org.jline.builtins.Completers.TreeCompleter.node;

import org.jline.reader.Completer;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.ParsedLine;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.jline.utils.InfoCmp.Capability;
import org.jline.builtins.Completers.TreeCompleter;
import org.jline.builtins.Completers.FileNameCompleter;
import org.jline.reader.LineReader.Option;

public class ConsoleApp {

  public static void generalHelp() {
    System.out.println("Usage: [command] [options]");
    System.out.println();
    System.out.println("Commands:");
    System.out.println("  help [command]                 Display help for a specific command");
    System.out.println("  exit                           Exit the application");
    System.out.println("  quit                           Exit the application");
    System.out.println("  load-kb [options]              Load the knowledge base with specified options");
    System.out.println("  query [options] [formula]      Check if a formula is entailed by the knowledge base");
    System.out.println();
    System.out.println("Type 'help [command]' for detailed information on a specific command.");
  }

  public static void helpLoadKb() {
    System.out.println("Usage: load-kb [options]");
    System.out.println();
    System.out.println("Options:");
    System.out.println("  --string                       Load knowledge from a comma-separated string");
    System.out.println("                                 Example: load-kb --string \"p=>b,b~>f,p~>!f\"");
    System.out.println("  --file                         Load knowledge from a file (each formula on a new line)");
    System.out.println("                                 Example: load-kb --file /path/to/file.txt");
  }

  public static void helpQuery() {
    System.out.println("Usage: query [options] [formula]");
    System.out.println();
    System.out.println("Options:");
    System.out.println("  --all                          Use all defeasible reasoners");
    System.out.println("  --rational                     Use the rational closure defeasible reasoner");
    System.out.println("  --lexical                      Use the lexicographic closure defeasible reasoner");
    System.out.println("                                 Example: query --lexical \"p~>f\"");
  }

  public static void helpExplain() {
    System.out.println("Usage: explain [options] [formula]");
    System.out.println();
    System.out.println("Options:");
    System.out.println("  --all                          Use all defeasible reasoners");
    System.out.println("  --rational                     Use the rational closure defeasible reasoner");
    System.out.println("  --lexical                      Use the lexicographic closure defeasible reasoner");
    System.out.println("                                 Example: explain --lexical \"p~>f\"");
  }

  public static Completer getCompleter() {
    return new TreeCompleter(
        node("help",
            node("load-kb"),
            node("query")),
        node("load-kb",
            node("--string"),
            node("--file", node(new FileNameCompleter()))),
        node("show-kb"),
        node("query",
            node("--all"),
            node("--rational"),
            node("--lexical")),
        node("explain",
            node("--all"),
            node("--rational"),
            node("--lexical")),
        node("exit"),
        node("quit"));
  }

  public static void run() {
    try (Terminal terminal = TerminalBuilder.builder().build()) {
      ConsoleAppHandler appHandler = new ConsoleAppHandler(terminal);

      Completer completer = getCompleter();

      LineReader reader = LineReaderBuilder.builder()
          .terminal(terminal)
          .completer(completer)
          .build();

      reader.option(Option.USE_FORWARD_SLASH, true);

      String prompt = new AttributedStringBuilder()
          .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN).bold())
          .append("extrc").style(AttributedStyle.DEFAULT)
          .append("$ ").toAnsi();

      while (true) {
        String line = null;
        try {
          line = reader.readLine(prompt);
        } catch (UserInterruptException e) {
          // Ignore
        } catch (EndOfFileException e) {
          return;
        }
        if (line == null) {
          continue;
        }

        line = line.trim();
        terminal.flush();
        if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) {
          break;
        }

        ParsedLine pl = reader.getParser().parse(line, 0);
        String command = pl.word();

        switch (command) {
          case "help" -> {
            if (pl.words().size() > 1) {
              String subCommand = pl.words().get(1);
              switch (subCommand) {
                case "load-kb" -> helpLoadKb();
                case "query" -> helpQuery();
                default -> System.out.println("No help for command: " + subCommand);
              }
            } else {
              generalHelp();
            }
          }
          case "load-kb" -> {
            if (pl.words().size() > 1) {
              String option = pl.words().get(1);
              switch (option) {
                case "--string" -> {
                  if (pl.words().size() > 2) {
                    appHandler.loadKb(pl.words().get(2));
                  } else {
                    System.out.println("Error: Missing knowledge base string.");
                  }
                }
                case "--file" -> {
                  if (pl.words().size() > 2) {
                    appHandler.loadKbFromFile(pl.words().get(2));
                  } else {
                    System.out.println("Error: Missing file path.");
                  }
                }
                default -> appHandler.loadKb(pl.words().get(1));
              }
            } else {
              System.out.println("Error: Missing options for load-kb.");
            }
          }
          case "show-kb" -> {
            appHandler.showKb();
          }
          case "query" -> {
            if (pl.words().size() > 1) {
              String option = pl.words().get(1);
              String formula = pl.words().size() > 2 ? pl.words().get(2) : null;
              switch (option) {
                case "--all" -> appHandler.queryAll(formula);
                case "--rational" -> appHandler.queryRationalReasoner(formula);
                case "--lexical" -> appHandler.queryLexicalReasoner(formula);
                default -> appHandler.queryAll(pl.words().get(1));
              }
            } else {
              System.out.println("Error: Missing options or formula for query.");
            }
          }
          case "explain" -> {
            if (pl.words().size() > 1) {
              String option = pl.words().get(1);
              String formula = pl.words().size() > 2 ? pl.words().get(2) : null;
              switch (option) {
                case "--all" -> appHandler.explainAll(formula);
                case "--rational" -> appHandler.explainRationalClosure(formula);
                case "--lexical" -> appHandler.explainLexicalClosure(formula);
                default -> appHandler.explainAll(pl.words().get(1));
              }
            } else {
              System.out.println("Error: Missing options or formula for query.");
            }
          }
          case "clear" -> {
            terminal.puts(Capability.clear_screen);
            terminal.flush();
          }
          default -> System.out.println("Unknown command: " + command);
        }
      }

    } catch (Exception e) {
      System.err.println("An error occurred: " + e.getMessage());
    }
  }
}
