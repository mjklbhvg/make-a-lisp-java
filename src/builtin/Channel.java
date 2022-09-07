package builtin;

import environment.MalEnvironment;
import exceptions.MalExecutionException;
import types.*;

public class Channel {

    public static MalCallable createChannel() {
        return new MalCallable() {
            @Override
            protected MalType execute(MalList args, MalEnvironment environment) throws MalExecutionException {
                if (args.size() > 1)
                    throw new MalExecutionException("create channel doesn't take any arguments");
                return new MalChannel();
            }
        };
    }

    public static MalCallable send() {
        return new MalCallable() {
            @Override
            protected MalType execute(MalList args, MalEnvironment environment) throws MalExecutionException {
                if (args.size() != 3)
                    throw new MalExecutionException("send takes 2 arguments");
                MalType c = args.get(1).eval(environment);
                if (!(c instanceof MalChannel channel))
                    throw new MalExecutionException("send needs a channel");
                boolean success = channel.add(args.get(2));
                return new MalBool(success);
            }
        };
    }

    public static MalCallable receive() {
        return new MalCallable() {
            @Override
            protected MalType execute(MalList args, MalEnvironment environment) throws MalExecutionException {
                if (args.size() != 2
                        || !(args.get(1) instanceof MalChannel channel))
                    throw new MalExecutionException("receive expects a single argument (channel)");
                return channel.take();
            }
        };
    }

    public static MalSpecial run() {
        return new MalSpecial() {
            @Override
            protected MalType execute(MalList args, MalEnvironment environment) throws MalExecutionException {
                if (args.size() < 2)
                    throw new MalExecutionException("run needs 1 argument");
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
