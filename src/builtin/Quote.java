package builtin;

import environment.MalEnvironment;
import exceptions.MalException;
import mal.TCO;
import types.*;

public class Quote {
    public static MalSpecial quote() {
        return new MalSpecial() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment) throws MalException {
                return args.get(1);
            }
        };
    }

    public static MalSpecial quasiquote() {
      return new MalSpecial() {
          @Override
          public MalType execute(MalList args, MalEnvironment environment) throws MalException, TCO {
              MalType result = qq_internal(args.get(1));
              throw new TCO(result, environment);
          }
      };
    }

    private static MalType qq_internal(MalType ast) throws MalException {
        if (ast instanceof MalVector list) {
            boolean isList = list instanceof MalList;

            if (list.isEmpty()) {
                if (isList)
                    return new MalList();
                else
                    return new MalVector();
            }

            if (isList
                    && list.get(0) instanceof MalSymbol sym
                    && sym.value().equals("unquote")) {
                if (list.size() != 2)
                    throw new MalException(new MalString("wrong argument count for unquote (expected 1)"));
                return list.get(1);
            }

            MalList resultList = new MalList();
            for (int i = list.size() - 1; i >= 0; i--) {
                MalType elt = list.get(i);
                if (elt instanceof MalList eltList) {
                    if (!eltList.isEmpty() && eltList.get(0) instanceof MalSymbol sym && sym.value()
                            .equals("splice-unquote")) {
                        if (eltList.size() != 2)
                            throw new MalException(new MalString("wrong argument count for splice-unquote (expected 1)"));
                        MalList tmp = new MalList();
                        tmp.add(new MalSymbol("concat"));
                        tmp.add(eltList.get(1));
                        tmp.add(resultList);
                        resultList = tmp;
                        continue;
                    }
                }
                MalList tmp = new MalList();
                tmp.add(new MalSymbol("cons"));
                tmp.add(qq_internal(elt));
                tmp.add(resultList);
                resultList = tmp;
            }
            if (!isList) {
                MalList resultAsVec = new MalList();
                resultAsVec.add(new MalSymbol("vec"));
                resultAsVec.add(resultList);
                return resultAsVec;
            }
            return resultList;
        } else if (ast instanceof MalSymbol || ast instanceof MalTable) {
            MalList resultList = new MalList();
            resultList.add(new MalSymbol("quote"));
            resultList.add(ast);
            return resultList;
        }
        return ast;
    }
}
