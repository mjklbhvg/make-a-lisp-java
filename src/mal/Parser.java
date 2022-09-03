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
         if (r.peekAt("(")) {
             return readList();
         }
         return readAtom();
    }

    private MalList readList() throws MalParserException {
        MalList list = new MalList();
        while (r.hasNext()) {
            if (r.peekAt(")")) {
                return list;
            }
            list.add(readForm());
        }
        throw new MalParserException("unbalanced parenthesis");
    }

    private MalType readAtom() throws MalParserException {
        if (!r.hasNext())
            throw new MalParserException("unexpected end of input");
        String tok = r.next();

        // MalNumber
        try {
            return new MalNumber(Double.parseDouble(tok));
        } catch (NumberFormatException e){}

        // MalNil
        if (tok.equals(Keywords.NIL))
            return new MalNil();
        // MalBool
        if (tok.equals(Keywords.TRUE))
            return new MalBool(true);
        if (tok.equals(Keywords.FALSE))
            return new MalBool(false);
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
