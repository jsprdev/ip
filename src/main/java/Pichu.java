import java.util.Scanner;

public class Pichu {
    public static void main(String[] args) {
        String name = "Pichu";

        System.out.println("____________________________________________________________\n" +
                " Hello! I'm " + name + "\n" +
                " What can I do for you?\n" +
                "____________________________________________________________\n"
                );

        Scanner input = new Scanner(System.in);
        while (true) {
            String command = input.nextLine();
            if (command.equals("bye")) {
                System.out.println("____________________________________________________________\n" +
                        " Bye. Hope to see you again soon!\n" +
                        "___________________________________________________________");
                break;
            }
            System.out.println("____________________________________________________________\n " +
                    command +
                    "\n___________________________________________________________");
        }

    }
}
