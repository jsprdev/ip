import main.java.Task;

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

        ArrayList<Task> cache = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        boolean saidBye = false;

        while (!saidBye) {
            String command = input.nextLine();

            if (command.toLowerCase().startsWith("mark")) {

                int index = Integer.parseInt(command.substring(5));

                cache.get(index - 1).setCompleted(true);

                System.out.println("""
                    ____________________________________________________________
                    Nice! I've marked this task as done:
                    """ +
                        "[X] " + cache.get(index - 1).getName() +
                        "\n___________________________________________________________");
                continue;
            } else if (command.toLowerCase().startsWith("unmark")) {
                int index = Integer.parseInt(command.substring(7));
                cache.get(index - 1).setCompleted(false);

                System.out.println("""
                    ____________________________________________________________
                    OK, I've marked this taks as not done yet:
                    """ +
                        "[ ] " + cache.get(index - 1).getName() +
                        "\n___________________________________________________________");
                continue;
            }

            switch (command) {
                case "bye":
                    System.out.println("""
                            ____________________________________________________________
                             Bye. Hope to see you again soon!
                            ___________________________________________________________""");
                    saidBye = true;
                    break;

                case "list":
                    System.out.println("""
                            ____________________________________________________________
                            Here are the tasks in your list:
                            """);
                    for (int i = 0; i < cache.size(); i++) {
                        System.out.println((i + 1) + ". " + cache.get(i).toString());
                    }
                    System.out.println("\n___________________________________________________________");
                    break;


                default:
                    cache.add(new Task(command));
                    System.out.println("____________________________________________________________\n " +
                            "added: " + command +
                            "\n___________________________________________________________");
                    break;
            }
        }
    }
}
