package eu.rfox;

import eu.rfox.parser.Parser;
import eu.rfox.parser.ast.ASTItem;
import eu.rfox.tokenizer.TokenizerException;

import java.io.IOException;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

public class Main {
    private static boolean hadError;

    public static void main(String[] args) throws IOException, TokenizerException {
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

    private static void runFile(String file_path) throws IOException, TokenizerException {
        byte[] bytes = Files.readAllBytes(Paths.get(file_path));
        ArrayList<ASTItem> ast = Parser.parse(new String(bytes, StandardCharsets.UTF_8));

        for (ASTItem item: ast) {
            System.out.println(item.toString());
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
