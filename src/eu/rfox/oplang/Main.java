package eu.rfox.oplang;

import eu.rfox.oplang.parser.Parser;
import eu.rfox.oplang.parser.ParserException;
import eu.rfox.oplang.parser.ast.ASTItem;
import eu.rfox.oplang.tokenizer.TokenizerException;
import eu.rfox.oplang.tokenizer.UnexpectedTokenException;
import eu.rfox.oplang.tokenizer.UnterminatedStringException;

import java.io.IOException;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

public class Main {
    private static boolean hadError;

    public static void main(String[] args) throws IOException, TokenizerException, ParserException {
        if (args.length > 1) {
            System.err.println("Usage: oplang [script]");
            System.exit(1);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    private static void runPrompt() {
        System.err.println("Invalid arguments.");
        System.exit(1);
    }

    private static void runFile(String file_path) throws IOException, TokenizerException, ParserException {
        byte[] bytes = Files.readAllBytes(Paths.get(file_path));
        String source_code = new String(bytes, StandardCharsets.UTF_8);

        try {
            Parser parser = new Parser(source_code);
            ArrayList<ASTItem> ast = parser.parse();

            for (ASTItem item : ast) {
                System.out.println(item.toString());
            }
        } catch (UnexpectedTokenException e) {
            System.err.println("Unexpected token `" + e.token.content + "` on line " + e.token.line + ";");
            String[] lines = source_code.split(System.getProperty("line.separator"));
            System.err.println(lines[e.token.line - 1]);

            for (int i = 0; i < e.token.start; i++) {
                System.err.print("-");
            }
            System.err.println("^");
        } catch (UnterminatedStringException e) {
            System.err.println("Unterminated string on line " + e.token.line + ":\n" + e.token.content);
        } catch (TokenizerException e) {
            System.err.println("Invalid token: " + e.token.toString());
        }
    }

    static void error(int line, String message) {
        reportError(line, "", message);
    }

    private static void reportError(int line, String where, String message) {
        System.err.println("[Line " + line + "] error " + where + ": " + message);
        hadError = true;
    }
}
