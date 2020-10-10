package eu.rfox;

import eu.rfox.parser.Parser;
import eu.rfox.parser.ast.ASTItem;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
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

    private static void runFile(String file_path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(file_path));
        ArrayList<ASTItem> ast = Parser.parse(new String(bytes, Charset.forName("utf-8")));
    }
}
