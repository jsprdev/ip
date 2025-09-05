package pichu.parser;

import pichu.task.Deadline;
import pichu.task.Event;
import pichu.task.Task;
import pichu.task.ToDo;

/**
 * Deals with making sense of the user command.
 */
public class Parser {

    /**
     * Enum representing different command types.
     */
    public enum CommandType {
        BYE, LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, FIND, UNKNOWN
    }

    /**
     * Parses the user input to determine the command type.
     *
     * @param input the user's input command
     * @return the CommandType enum representing the command
     */
    public static CommandType getCommandType(String input) {
        if (input == null || input.trim().isEmpty()) {
            return CommandType.UNKNOWN;
        }

        String command = input.toLowerCase().trim();

        if (command.equals("bye")) {
            return CommandType.BYE;
        } else if (command.equals("list")) {
            return CommandType.LIST;
        } else if (command.startsWith("mark ")) {
            return CommandType.MARK;
        } else if (command.startsWith("unmark ")) {
            return CommandType.UNMARK;
        } else if (command.startsWith("todo ")) {
            return CommandType.TODO;
        } else if (command.startsWith("deadline ")) {
            return CommandType.DEADLINE;
        } else if (command.startsWith("event ")) {
            return CommandType.EVENT;
        } else if (command.startsWith("delete ")) {
            return CommandType.DELETE;
        } else if (command.startsWith("find ")) {
            return CommandType.FIND;
        } else {
            return CommandType.UNKNOWN;
        }
    }

    /**
     * Parses the index from mark/unmark/delete commands.
     *
     * @param input the user's input command
     * @return the index (1-based) specified by the user
     * @throws NumberFormatException if the index is not a valid number
     */
    public static int parseIndex(String input) throws NumberFormatException {
        String[] parts = input.split(" ", 2);
        if (parts.length < 2) {
            throw new NumberFormatException("No index provided");
        }
        return Integer.parseInt(parts[1].trim());
    }

    /**
     * Parses a todo command to extract the description.
     *
     * @param input the user's input command
     * @return the todo description
     * @throws IllegalArgumentException if the description is empty
     */
    public static String parseTodoDescription(String input) throws IllegalArgumentException {
        if (input.length() <= 4 || input.substring(5).trim().isEmpty()) {
            throw new IllegalArgumentException("The description of a todo cannot be empty.");
        }
        return input.substring(5).trim();
    }

    /**
     * Parses a deadline command to extract description and deadline.
     *
     * @param input the user's input command
     * @return an array where [0] is description and [1] is deadline
     * @throws IllegalArgumentException if the description is empty
     */
    public static String[] parseDeadlineCommand(String input) throws IllegalArgumentException {
        String[] parts = input.split(" ", 2);

        if (parts.length == 1 || parts[1].trim().isEmpty()) {
            throw new IllegalArgumentException("The description of a deadline cannot be empty.");
        }

        String[] descriptionAndTime = parts[1].split("/by ", 2);
        String description = descriptionAndTime[0].trim();
        String byTime = descriptionAndTime.length > 1 ? descriptionAndTime[1].trim() : "";

        if (description.isEmpty()) {
            throw new IllegalArgumentException("The description of a deadline cannot be empty.");
        }

        return new String[]{description, byTime};
    }

    /**
     * Parses an event command to extract description, start time, and end time.
     *
     * @param input the user's input command
     * @return an array where [0] is description, [1] is start time, [2] is end time
     * @throws IllegalArgumentException if the description is empty
     */
    public static String[] parseEventCommand(String input) throws IllegalArgumentException {
        String[] parts = input.split(" ", 2);

        if (parts.length == 1 || parts[1].trim().isEmpty()) {
            throw new IllegalArgumentException("The description of an event cannot be empty.");
        }

        String[] descriptionAndTime = parts[1].split("/from ", 2);
        String description = descriptionAndTime[0].trim();
        String time = descriptionAndTime.length > 1 ? descriptionAndTime[1].trim() : "";

        String[] startAndEnd = time.split("/to ", 2);
        String start = startAndEnd[0].trim();
        String end = startAndEnd.length > 1 ? startAndEnd[1].trim() : "";

        if (description.isEmpty()) {
            throw new IllegalArgumentException("The description of an event cannot be empty.");
        }

        return new String[]{description, start, end};
    }

    /**
     * Parses a find command to extract the search keyword.
     *
     * @param input the user's input command
     * @return the search keyword
     * @throws IllegalArgumentException if the keyword is empty
     */
    public static String parseFindKeyword(String input) throws IllegalArgumentException {
        if (input.length() <= 4 || input.substring(5).trim().isEmpty()) {
            throw new IllegalArgumentException("The search keyword cannot be empty.");
        }
        return input.substring(5).trim();
    }

    /**
     * Parses a task from its string representation (for loading from file).
     *
     * @param taskData the string representation of the task
     * @return the Task object, or null if parsing fails
     */
    public static Task parseTaskFromString(String taskData) {
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
        default:
            break;
        }

        if (task != null) {
            task.setCompleted(isCompleted);
        }

        return task;
    }
}
