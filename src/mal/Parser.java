package mal;

import exceptions.MalParserException;
import types.*;

public class Parser {
    private Reader r;

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

        // Dereference atoms with '@'
        if (tok.equals("@")) {
            MalList derefForm = new MalList();
            derefForm.add(new MalSymbol("deref"));
            derefForm.add(readForm());
            return derefForm;
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
