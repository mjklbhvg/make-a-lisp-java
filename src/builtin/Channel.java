package builtin;

import environment.MalEnvironment;
import exceptions.MalExecutionException;
import types.*;

public class Channel {

    public static MalCallable createChannel() {
        return new MalCallable(new Class[]{}, null, false) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) throws MalExecutionException {
                if (args.size() > 1)
                    throw new MalExecutionException("create channel doesn't take any arguments");
                return new MalChannel();
            }
        };
    }

    public static MalCallable send() {
        return new MalCallable(new Class[]{MalChannel.class, MalType.class}, null, false) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) {
                boolean success = ((MalChannel) args.get(1)).add(args.get(2));
                return new MalBool(success);
            }
        };
    }

    public static MalCallable receive() {
        return new MalCallable(new Class[]{MalChannel.class}, null, false) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) {
                return ((MalChannel) args.get(1)).take();
            }
        };
    }

    public static MalSpecial run() {
        return new MalSpecial(new Class[]{MalType.class}, null, false) {
            @Override
            public MalType executeChecked(MalList args, MalEnvironment environment) {
                new Thread(() -> {
                    try {
                        args.get(1).eval(new MalEnvironment(environment));
                    } catch (MalExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
                return new MalNil();
            }
        };
    }
}
