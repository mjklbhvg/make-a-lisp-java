package mal;

import environment.MalEnvironment;
import exceptions.MalExecutionException;
import exceptions.MalParserException;
import types.MalList;

import java.io.*;

public class Repl {

    private BufferedReader reader;
    private MalEnvironment replEnvironment;

    public Repl() {
        reader = new BufferedReader(
                new InputStreamReader(System.in)
        );
        replEnvironment = MalEnvironment.getBaseEnvironment();
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

        // If there is no input or just a comment (removed by the Reader),
        // we can just continue to avoid printing an empty result prompt at
        // the repl
        if (tokenizer.getTokens().size() == 0)
            return true;

        MalList ast;
        try {
            ast = new Parser(tokenizer).getAST();
        } catch (MalParserException e) {
            System.out.println(e);
            return true;
        }

        MalEnvironment backupEnvironment = (MalEnvironment) replEnvironment.clone();
        try {
            System.out.println("=> " +
                    ast.eval(replEnvironment)
                            .toString()
            );
        } catch (MalExecutionException e) {
            System.out.println(e);
            replEnvironment = backupEnvironment;
            System.out.println("Restored environment (⌐■_■)");
        }
        return true;
    }

}
