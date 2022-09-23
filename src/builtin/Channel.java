package builtin;

import environment.MalEnvironment;
import exceptions.MalException;
import types.*;

public class Channel {

    public static MalCallable createChannel() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment) {
                return new MalChannel();
            }
        };
    }

    public static MalCallable send() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment) throws MalException {
                boolean success = ((MalChannel) args.get(1)).add(args.get(2));
                return new MalBool(success);
            }
        };
    }

    public static MalCallable receive() {
        return new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment) throws MalException {
                return ((MalChannel) args.get(1)).take();
            }
        };
    }

    public static MalSpecial run() {
        return new MalSpecial() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment) {
                new Thread(() -> {
                    try {
                        args.get(1).eval(new MalEnvironment(environment));
                    } catch (MalException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
                return new MalNil();
            }
        };
    }
}
