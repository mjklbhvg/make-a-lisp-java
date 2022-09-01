import exceptions.ParserException;
import types.MalList;

import java.io.*;

public class Repl {

    private BufferedReader reader;

    public Repl() {
        reader = new BufferedReader(
                new InputStreamReader(System.in)
        );
    }

    public void exit() {
        try {
            reader.close();
        } catch (IOException e) {
            System.err.println("Error closing reader: " + e);
        }
    }

    public boolean rep() {
        System.out.print("user> ");
        String line;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (line == null)
            return false;
        Reader tokenizer = new Reader(line);
        MalList ast;
        try {
            ast = new Parser(tokenizer).getAST();
        } catch (ParserException e) {
            System.out.println("\nParsing input failed: " + e);
            return true;
        }
        // i can now pass the reader to the evaluator, so it can throw beatiful malexceptions
        System.out.println(ast.toString());
        return true;
    }

}
