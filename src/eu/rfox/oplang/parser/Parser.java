package eu.rfox.oplang.parser;

import eu.rfox.oplang.tokenizer.Token;
import eu.rfox.oplang.parser.ast.ASTItem;
import eu.rfox.oplang.tokenizer.Tokenizer;
import eu.rfox.oplang.tokenizer.TokenizerException;

import java.util.ArrayList;

public class Parser {
    public static ArrayList<ASTItem> parse(String input_string) throws TokenizerException {
        ArrayList<ASTItem> ast = new ArrayList<>();

        Tokenizer tokenizer = new Tokenizer(input_string);
        ArrayList<Token> tokens = tokenizer.tokenize();
        for (Token token : tokens) {
            System.out.println(token.toString());
        }


        return ast;
    }
}
