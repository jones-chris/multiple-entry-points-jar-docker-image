public class MainArgsRouter {

    public static void main(String[] args) {
        System.out.println("In mainRouter");

        // If no args, then execute the default Main.main method.
        if (args.length == 0) {
            Main.main(args);
        } else if (args[0].equals("anotherEntryPoint")) {
            AnotherEntryPoint.main(args);
        } else {
            throw new IllegalArgumentException(
                    String.format("Did not recognize %s", args[0])
            );
        }
    }

}
