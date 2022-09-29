package mal;

import environment.MalEnvironment;
import exceptions.MalException;
import exceptions.MalParserException;
import types.MalList;

import java.io.*;

public class Repl {

    private BufferedReader reader;
    private MalEnvironment replEnv;
    private int iteration = 1;
    private String username;

    private enum Status {OK, SKIP, EXIT}

    public Repl() {
        reader = new BufferedReader(
                new InputStreamReader(System.in)
        );
        replEnv = MalEnvironment.getBaseEnvironment();
        replEnv.set("*ARGV*", new MalList());
        username = System.getProperty("user.name");
    }

    public void start() {
        System.out.println("Mal[Java]");
        Status s;
        while ((s = rep()) != Status.EXIT) {
            if (s == Status.OK)
                iteration++;
        }
        try {
            reader.close();
        } catch (IOException e) {
            System.err.println("Error closing reader: " + e);
        }
    }

    public Status rep() {
        System.out.print("\033[35;1m["+iteration+"] " + username + "> \033[m");
        String line;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (line == null)
            return Status.EXIT;

        Reader tokenizer = new Reader(line);
        if (tokenizer.getTokens().size() == 0)
            return Status.SKIP;

        MalList ast;
        try {
            ast = new Parser(tokenizer).getAST();
        } catch (MalParserException e) {
            System.out.println("" + e);
            return Status.SKIP;
        }

        MalEnvironment backupEnvironment;
        try {
            backupEnvironment = (MalEnvironment) replEnv.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        try {
            String result = ast.eval(replEnv).prettyPrint();
            // Strip the parens of the initial "mainAST" list
            System.out.println("=> " + result.substring(1, result.length() - 1));
        } catch (MalException e) {
            System.out.println("Uncaught Exception: "+e.getValue());
            replEnv = backupEnvironment;
            System.out.println("Restored environment (⌐■_■)");
            return Status.SKIP;
        }
        return Status.OK;
    }

}
