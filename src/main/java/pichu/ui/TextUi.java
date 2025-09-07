package pichu.ui;

import java.util.ArrayList;
import java.util.Scanner;

import pichu.task.Deadline;
import pichu.task.Event;
import pichu.task.Task;

/**
 * Handles interactions with the user.
 */
public class TextUi {
    private Scanner scanner;

    public TextUi() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Shows the welcome message to the user.
     */
    public void showWelcome() {
        String name = "Pichu";
        System.out.println("____________________________________________________________\n"
                + " Hello! I'm " + name + "\n"
                + " What can I do for you?\n"
                + "____________________________________________________________\n");
    }

    /**
     * Shows the goodbye message to the user.
     */
    public void showGoodbye() {
        System.out.println("""
                ____________________________________________________________
                 Bye. Hope to see you again soon!
                ___________________________________________________________""");
    }

    /**
     * Reads the next command from the user.
     *
     * @return the user's command as a String
     */
    public String readCommand() {
        try {
            if (scanner.hasNextLine()) {
                return scanner.nextLine();
            } else {
                // No more input available, signal to exit
                return "bye";
            }
        } catch (Exception e) {
            // If there's an issue reading input, signal to exit
            System.out.println("No input available. Exiting...");
            return "bye";
        }
    }

    /**
     * Shows an error message to the user.
     *
     * @param message the error message to display
     */
    public void showError(String message) {
        System.out.println("____________________________________________________________\n "
                + "OOPS!!! " + message
                + "\n___________________________________________________________");
    }

    /**
     * Shows a message when a task is marked as done.
     *
     * @param task the task that was marked as done
     */
    public void showTaskMarked(Task task) {
        System.out.println("""
                ____________________________________________________________
                Nice! I've marked this task as done:
                """
                + "[X] " + task.getName()
                + "\n___________________________________________________________");
    }

    /**
     * Shows a message when a task is unmarked.
     *
     * @param task the task that was unmarked
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("""
                ____________________________________________________________
                OK, I've marked this task as not done yet:
                """
                + "[ ] " + task.getName()
                + "\n___________________________________________________________");
    }

    /**
     * Shows a message when a task is added.
     *
     * @param task the task that was added
     * @param totalTasks the total number of tasks after addition
     */
    public void showTaskAdded(Task task, int totalTasks) {
        System.out.println("____________________________________________________________\n "
                + "Got it. I've added this task:\n "
                + "  [" + task.getType() + "][" + task.getCompletion() + "] " + task.getName()
                + getTaskTimeInfo(task) + "\n"
                + "Now you have " + totalTasks + " task(s) in the list."
                + "\n___________________________________________________________");
    }

    /**
     * Shows a message when a task is deleted.
     */
    public void showTaskDeleted() {
        System.out.println("""
                ____________________________________________________________
                Noted, I've removed this task!
                ___________________________________________________________""");
    }

    /**
     * Shows the list of tasks to the user.
     *
     * @param tasks the list of tasks to display
     */
    public void showTaskList(ArrayList<Task> tasks) {
        System.out.println("""
                ____________________________________________________________
                Here are the tasks in your list:
                """);
        for (int i = 0; i < tasks.size(); i++) {
            Task temp = tasks.get(i);
            System.out.println((i + 1) + "." + "[" + temp.getType() + "]" + temp.toString());
        }
        System.out.println("\n___________________________________________________________");
    }

    /**
     * Shows the search results for the find command.
     *
     * @param matchingTasks the list of tasks that match the search keyword
     */
    public void showFindResults(ArrayList<Task> matchingTasks) {
        System.out.println("""
                ____________________________________________________________
                Here are the matching tasks in your list:""");

        if (matchingTasks.isEmpty()) {
            System.out.println(" No matching tasks found.");
        } else {
            for (int i = 0; i < matchingTasks.size(); i++) {
                Task temp = matchingTasks.get(i);
                System.out.println(" " + (i + 1) + "." + "[" + temp.getType() + "]" + temp.toString());
            }
        }
        System.out.println("____________________________________________________________");
    }

    /**
     * Gets time information for a task if applicable.
     *
     * @param task the task to get time info for
     * @return formatted time information string
     */
    private String getTaskTimeInfo(Task task) {
        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return " (by: " + deadline.getFormattedDeadline() + ")";
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return " (from: " + event.getFormattedStart() + " to: " + event.getFormattedEnd() + ")";
        }
        return "";
    }
}
