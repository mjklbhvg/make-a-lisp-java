package mal;

import environment.MalEnvironment;
import exceptions.MalException;
import exceptions.MalParserException;
import types.MalKeyword;
import types.MalList;
import types.MalString;
import types.MalType;

import java.io.IOException;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {

        if (args.length == 0) {
            Repl repl = new Repl();
            System.out.println("Mal[Java]");
            while (repl.rep()) ;
            repl.exit();
            return;
        }

        String filename = args[0];
        MalList ast;
        try {
             ast = new Parser(new Reader(Path.of(filename))).getAST();
        } catch (MalParserException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MalList arguments = new MalList();
        for (int i = 1; i < args.length; i++)
            arguments.add(new MalString(args[i]));
        MalEnvironment env = MalEnvironment.getBaseEnvironment();
        try {
            env.set("*ARGV*", arguments);
        } catch (MalException e) {
            throw new RuntimeException(e);
        }
        try {
            ast.eval(env);
        } catch (MalException e) {
            System.err.println(e.getValue());
            throw new RuntimeException(e);
        }
    }
}
