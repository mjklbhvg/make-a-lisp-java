package mal;

import exceptions.MalParserException;
import types.*;

import java.util.HashMap;
import java.util.Map;

public class Parser {
    private Reader r;

    private static HashMap<String, String> readerMacros = new HashMap<>(Map.of(
            "@", "deref",
            "'", "quote",
            "`", "quasiquote",
            "~", "unquote",
            "~@", "splice-unquote"
    ));

    public Parser (Reader r) {
        this.r = r;
    }

    public MalList getAST() throws MalParserException {
        MalList ast = new MalList();
        ast.setMainAST(true);
        while (r.hasNext()) {
            ast.add(readForm());
        }
        return ast;
    }

    private MalType readForm() throws MalParserException {
         if (r.peekAt("("))
             return readContainer(")", new MalList());
         if (r.peekAt("["))
             return readContainer("]", new MalVector());
         if (r.peekAt("{"))
             return readContainer("}", new MalTable());
         return readAtom();
    }

    private MalType readContainer(String end, MalContainer container) throws MalParserException {
        while (r.hasNext()) {
            if (r.peekAt(end))
                return container.checkComplete();
            container.store(readForm());
        }
        throw new MalParserException("missing closing character '" + end + "'");
    }

    private MalType readAtom() throws MalParserException {
        if (!r.hasNext())
            throw new MalParserException("unexpected end of input");
        String tok = r.next();

        // MalNumber
        //noinspection CatchMayIgnoreException
        try {
            return new MalNumber(Double.parseDouble(tok));
        } catch (NumberFormatException e){}

        // Expand Reader Macros
        if (readerMacros.containsKey(tok)) {
            MalList expanded = new MalList();
            expanded.add(new MalSymbol(readerMacros.get(tok)));
            expanded.add(readForm());
            return expanded;
        }

        // MalNil
        if (tok.equals(Keywords.NIL))
            return new MalNil();
        // MalBool
        if (tok.equals(Keywords.TRUE))
            return new MalBool(true);
        if (tok.equals(Keywords.FALSE))
            return new MalBool(false);
        // MalKeyword
        if (tok.startsWith(":")) {
            if (tok.length() < 2)
                throw new MalParserException("zero length keyword");
            return new MalKeyword(tok.substring(1));
        }
        // MalString
        if (tok.startsWith("\"")) {
            if (!tok.endsWith("\""))
                throw new MalParserException("unbalanced string");
            return new MalString(MalString.unescape(tok));
        }

        // MalSymbol
        return new MalSymbol(tok);
    }
}
