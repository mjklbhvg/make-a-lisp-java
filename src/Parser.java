import exceptions.ParserException;
import types.MalList;
import types.MalNumber;
import types.MalSymbol;
import types.MalType;

public class Parser {
    private Reader r;

    public Parser (Reader r) {
        this.r = r;
    }

    public MalList getAST() throws ParserException {
        MalList ast = new MalList();
        ast.setMainAST(true);
        while (r.hasNext()) {
            ast.add(readForm());
        }
        return ast;
    }

    private MalType readForm() throws ParserException {
         if (r.peekAt("(")) {
             return readList();
         }
         return readAtom();
    }

    private MalList readList() throws ParserException {
        MalList list = new MalList();
        while (r.hasNext()) {
            if (r.peekAt(")")) {
                return list;
            }
            list.add(readForm());
        }
        throw new ParserException("unexpected end of input");
    }

    private MalType readAtom() throws ParserException {
        if (!r.hasNext())
            throw new ParserException("unexpected end of input");
        String tok = r.next();
        MalType type;
        try {
            type = new MalNumber(Double.parseDouble(tok));
        } catch (NumberFormatException e) {
            type = new MalSymbol(tok);
        }
        return type;
    }
}
