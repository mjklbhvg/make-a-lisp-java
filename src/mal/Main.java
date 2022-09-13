package mal;

public class Main {

    public static void main(String[] args) {
        Repl repl = new Repl();
        while (repl.rep());
        repl.exit();
    }
//(let* (p (+ 2 3) q (+ 2 p)) (+ p q))
}
