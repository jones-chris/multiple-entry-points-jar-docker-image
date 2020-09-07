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
    }

}
