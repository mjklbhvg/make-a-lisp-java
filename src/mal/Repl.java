package mal;

import environment.MalEnvironment;
import exceptions.MalException;
import exceptions.MalParserException;
import types.MalList;
import types.MalType;

import java.io.*;

public class Repl {

    private BufferedReader reader;

    private MalEnvironment replEnv;
    private String prompt;

    public Repl() {
        reader = new BufferedReader(
                new InputStreamReader(System.in)
        );
        replEnv = MalEnvironment.getBaseEnvironment();
        try {
            replEnv.set("*ARGV*", new MalList());
        } catch (MalException e) {
            throw new RuntimeException(e);
        }
        prompt = System.getProperty("user.name") + "> ";
    }

    public void exit() {
        try {
            reader.close();
        } catch (IOException e) {
            System.err.println("Error closing reader: " + e);
        }
    }

    public boolean rep() {
        System.out.print(prompt);
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
            System.out.println("" + e);
            return true;
        }

        MalEnvironment backupEnvironment;
        try {
            backupEnvironment = (MalEnvironment) replEnv.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        try {
            MalType result = ast.eval(replEnv);
            System.out.println("=> " + result.toString());
        } catch (MalException e) {
            System.out.println("Uncaught Exception: "+e.getValue());
            replEnv = backupEnvironment;
            System.out.println("Restored environment (⌐■_■)");
        }
        return true;
    }

}
