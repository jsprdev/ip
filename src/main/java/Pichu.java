import main.java.Deadline;
import main.java.Event;
import main.java.Task;
import main.java.ToDo;

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
            } else if (command.toLowerCase().startsWith("todo")) {
                if (command.length() <= 4 || command.substring(5).isEmpty()) {
                    System.out.println("""
                            ____________________________________________________________
                             OOPS!!! The description of a todo cannot be empty.
                            ___________________________________________________________""");
                }
                cache.add(new ToDo(command));

                System.out.println("____________________________________________________________\n " +
                        "added: " + command +
                        "Now you have " + cache.size() + " in the list." +
                        "\n___________________________________________________________");
                continue;
            } else if (command.toLowerCase().startsWith("deadline")) {


                String[] parts = command.split(" ", 2);

                if (parts.length == 1) {
                    System.out.println("""
                            ____________________________________________________________
                             OOPS!!! The description of a deadline cannot be empty.
                            ___________________________________________________________""");
                }

                String[] descriptionAndTime = parts[1].split("/by ", 2);
                String description = descriptionAndTime[0].trim();
                String byTime = descriptionAndTime.length > 1 ? descriptionAndTime[1].trim() : "";

                Deadline temp = new Deadline(description, byTime);
                cache.add(temp);

                System.out.println("____________________________________________________________\n " +
                        "added:\n " +
                        "[" + temp.getType() + "]" + "[" + temp.getCompletion() + "] " + description + " (by: " + byTime + ") \n" +
                        "Now you have " + cache.size() + " task(s) in the list." +
                        "\n___________________________________________________________");
                continue;
            } else if (command.toLowerCase().startsWith("event")) {

                String[] parts = command.split(" ", 2);

                if (parts.length == 1) {
                    System.out.println("""
                            ____________________________________________________________
                             OOPS!!! The description of a event cannot be empty.
                            ___________________________________________________________""");
                }

                String[] descriptionAndTime = parts[1].split("/from ", 2);
                String description = descriptionAndTime[0].trim();
                String time = descriptionAndTime.length > 1 ? descriptionAndTime[1].trim() : "";

                String[] startAndEnd = time.split("/to ", 2);
                String start = startAndEnd[0].trim();
                String end = startAndEnd.length > 1 ? startAndEnd[1].trim() : "";

                Event temp = new Event(description, start, end);
                cache.add(temp);

                System.out.println("____________________________________________________________\n " +
                        "added:\n " +
                        "[" + temp.getType() + "]" + "[" + temp.getCompletion() + "] " + description +
                        " (from: " + start + " to: " + end + ") \n" +
                        "Now you have " + cache.size() + " task(s) in the list." +
                        "\n___________________________________________________________");
                continue;
            } else if (command.toLowerCase().startsWith("delete")) {
                int index = Integer.parseInt(command.substring(7));
                cache.remove(index);

                System.out.println("""
                    ____________________________________________________________
                    Noted, I've removed this task!
                    ___________________________________________________________""");
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
                        Task temp = cache.get(i);
                        System.out.println((i + 1) + "." + "[" + temp.getType() + "]" + temp.toString());
                    }
                    System.out.println("\n___________________________________________________________");
                    break;


                default:
                    cache.add(new Task(command));
                    System.out.println("____________________________________________________________\n " +
                            "OOPS!!! I'm sorry, I dont know what that means! :-(" +
                            "\n___________________________________________________________");
                    break;
            }
        }
    }
}
