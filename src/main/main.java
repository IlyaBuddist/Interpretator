package src.main;

import src.parser.Lexer;
import src.parser.Parser;
import src.parser.Token;
import src.parser.ast.Statement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;



public class main {
    public static void main(String[] args) throws IOException {
        final String input = new String( Files.readAllBytes(Paths.get("program.cpp")), "UTF-8");
        final List<Token> tokens = new Lexer(input).tokenize();
        for (Token token : tokens){
            System.out.println(token);
        }
        final Statement program = new Parser(tokens).parse();
        System.out.println(program.toString());
        program.execute();

    }
}