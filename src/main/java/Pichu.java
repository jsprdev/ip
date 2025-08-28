import main.java.*;

import java.util.ArrayList;
import java.util.List;
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
        Storage storage = new Storage("data/tasks.txt");


        // Loading tasks from ./data/tasks.txt
        List<String> savedTasks = storage.loadTasks();
        for (String taskData : savedTasks) {

            Task task = parseTaskFromString(taskData);
            if (task != null) {
                cache.add(task);
            }
        }

        while (!saidBye) {
            String command = input.nextLine();

            if (command.toLowerCase().startsWith("mark")) {
                int index = Integer.parseInt(command.substring(5));
                cache.get(index - 1).setCompleted(true);
                storage.saveAllTasks(cache);
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
                storage.saveAllTasks(cache);
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
                storage.saveTask(cache.get(cache.size() - 1).toFileFormat());

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
                storage.saveTask(temp.toFileFormat());

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
                storage.saveTask(temp.toFileFormat());

                System.out.println("____________________________________________________________\n " +
                        "added:\n " +
                        "[" + temp.getType() + "]" + "[" + temp.getCompletion() + "] " + description +
                        " (from: " + start + " to: " + end + ") \n" +
                        "Now you have " + cache.size() + " task(s) in the list." +
                        "\n___________________________________________________________");
                continue;
            } else if (command.toLowerCase().startsWith("delete")) {
                int index = Integer.parseInt(command.substring(7));
                cache.remove(index - 1);
                storage.saveAllTasks(cache);

                System.out.println("""
                    ____________________________________________________________
                    Noted, I've removed this task!
                    ___________________________________________________________""");
                continue;
            }

            enum MainWord {
                bye,
                list
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

    private static Task parseTaskFromString(String taskData) {
        if (taskData == null || taskData.trim().isEmpty()) {
            return null;
        }

        String[] parts = taskData.split("\\|");
        if (parts.length < 3) {
            return null;
        }

        String type = parts[0];
        boolean isCompleted = parts[1].equals("1");
        String description = parts[2];

        Task task = null;

        switch (type) {
            case "T":
                task = new ToDo(description);
                break;
            case "D":
                if (parts.length >= 4) {
                    task = new Deadline(description, parts[3]);
                }
                break;
            case "E":
                if (parts.length >= 5) {
                    task = new Event(description, parts[3], parts[4]);
                }
                break;
        }

        if (task != null) {
            task.setCompleted(isCompleted);
        }

        return task;
    }
}
