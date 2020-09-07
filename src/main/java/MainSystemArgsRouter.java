import java.util.Optional;

public class MainSystemArgsRouter {

    public static void main(String[] args) {
        System.out.println("In mainSystemArgsRouter");

        Optional.ofNullable(System.getProperty("entryPointMethod")).ifPresentOrElse(
                (arg) -> {
                    if (arg.equals("anotherEntryPoint")) {
                        AnotherEntryPoint.main(args);
                    } else {
                        throw new IllegalArgumentException(
                                String.format("Did not recognize %s", arg)
                        );
                    }
                },
                () -> {
                    Main.main(args);
                }
        );

//        if (entryPointMethod == null) {
//            Main.main(args);
//        }
//
//        if (entryPointMethod.equals("anotherEntryPoint")) {
//            AnotherEntryPoint.main(args);
//        } else {
//            throw new IllegalArgumentException(
//                    String.format("Did not recognize %s", args[0])
//            );
//        }
//
//        // If no args, then execute the default Main.main method.
//        if (args.length == 0) {
//            Main.main(args);
//        } else if (args[0].equals("anotherEntryPoint")) {
//            AnotherEntryPoint.main(args);
//        } else {
//            throw new IllegalArgumentException(
//                    String.format("Did not recognize %s", args[0])
//            );
//        }
    }

}
