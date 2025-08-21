import java.util.ArrayList;
import java.util.Scanner;

public class Pichu {
    public static void main(String[] args) {
        String name = "Pichu";

        System.out.println("____________________________________________________________\n" +
                " Hello! I'm " + name + "\n" +
                " What can I do for you?\n" +
                "____________________________________________________________\n"
                );

        ArrayList<String> cache = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        boolean saidBye = false;
        while (!saidBye) {


            String command = input.nextLine();

            switch (command) {
                case "bye":
                    System.out.println("""
                            ____________________________________________________________
                             Bye. Hope to see you again soon!
                            ___________________________________________________________""");
                    saidBye = true;
                    break;

                case "list":
                    System.out.println("____________________________________________________________\n ");
                    for (int i = 0; i < cache.size(); i++) {
                        System.out.println((i + 1) + ". " + cache.get(i));
                    }
                    System.out.println("\n___________________________________________________________");
                    break;

                default:
                    cache.add(command);
                    System.out.println("____________________________________________________________\n " +
                            "added: " + command +
                            "\n___________________________________________________________");
                    break;
            }


        }

    }
}
