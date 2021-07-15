package eu.rfox.tinySelfEE;

import eu.rfox.tinySelfEE.parser.Parser;
import eu.rfox.tinySelfEE.parser.ParserException;
import eu.rfox.tinySelfEE.parser.ast.ASTItem;
import eu.rfox.tinySelfEE.parser.ast.ASTPrinter;
import eu.rfox.tinySelfEE.parser.ast.Root;
import eu.rfox.tinySelfEE.vm.Code;
import eu.rfox.tinySelfEE.vm.CodeContext;
import eu.rfox.tinySelfEE.vm.Interpreter;
import eu.rfox.tinySelfEE.vm.Process;

import java.io.IOException;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;


public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.err.println("Usage: tinySelfEE [script]");
            System.exit(1);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    private static void runPrompt() {
        System.err.println("Prompt is not implemented at the moment.");
        System.err.println("Usage: tinySelfEE [script]");
        System.exit(1);
    }

    private static Root parseSourceAndPrintErrors(String source_code) {
        String[] source_lines = source_code.split(System.getProperty("line.separator"));

        Parser parser = new Parser(source_code);
        ArrayList<ASTItem> ast = parser.parse();

        if (parser.hadErrors) {
            for (ParserException e: parser.exceptions) {
                e.prettify(source_lines, System.err);
            }
        }

        return new Root(ast);
    }

    private static void runFile(String file_path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(file_path));
        Root ast_root = parseSourceAndPrintErrors(new String(bytes, StandardCharsets.UTF_8));

        printRawAst(ast_root);

        CodeContext root_context = new CodeContext();
        ast_root.compile(root_context);

        Code root_code = root_context.compile();

        Interpreter vm = new Interpreter(new Process(root_code));
        vm.run();
    }

    private static void printRawAst(Root ast_root) {
        for (ASTItem item : ast_root.ast) {
            System.out.println(item.toString());
        }
    }

    private static void printAst(Root ast_root) {
        for (ASTItem item : ast_root.ast) {
            ASTPrinter printer = new ASTPrinter();
            System.out.println(printer.print(item));
        }
    }
}
