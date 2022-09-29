package builtin;

import environment.MalEnvironment;
import exceptions.MalException;
import types.*;

public class Channel {

    public static MalCallable createChannel = new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) {
                return new MalChannel();
            }
        };

    public static MalCallable send = new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                boolean success = ((MalChannel) args.get(1)).add(args.get(2));
                return new MalBool(success);
            }
        };

    public static MalCallable receive = new MalCallable() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) throws MalException {
                return ((MalChannel) args.get(1)).take();
            }
        };

    public static MalSpecial run = new MalSpecial() {
            @Override
            public MalType execute(MalList args, MalEnvironment environment, MalType caller) {
                new Thread(() -> {
                    try {
                        args.get(1).eval(new MalEnvironment(environment));
                    } catch (MalException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
                return MalNil.NIL;
            }
        };
}
