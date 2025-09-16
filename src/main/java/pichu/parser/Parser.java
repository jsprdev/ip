package pichu.parser;

import pichu.task.Deadline;
import pichu.task.Event;
import pichu.task.Task;
import pichu.task.Todo;

/**
 * Deals with making sense of the user command.
 */
public class Parser {
    private static final String BYE_COMMAND = "bye";
    private static final String LIST_COMMAND = "list";
    private static final String MARK_COMMAND = "mark ";
    private static final String UNMARK_COMMAND = "unmark ";
    private static final String TODO_COMMAND = "todo ";
    private static final String DEADLINE_COMMAND = "deadline ";
    private static final String EVENT_COMMAND = "event ";
    private static final String DELETE_COMMAND = "delete ";
    private static final String FIND_COMMAND = "find ";

    private static final int TODO_PREFIX_LENGTH = 5;
    private static final int FIND_PREFIX_LENGTH = 5;
    private static final int MIN_COMMAND_PARTS = 2;

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
        if (isInvalidInput(input)) {
            return CommandType.UNKNOWN;
        }

        String command = input.toLowerCase().trim();
        return mapCommandToType(command);
    }

    private static boolean isInvalidInput(String input) {
        return input == null || input.trim().isEmpty();
    }

    private static CommandType mapCommandToType(String command) {
        if (command.equals(BYE_COMMAND)) {
            return CommandType.BYE;
        } else if (command.equals(LIST_COMMAND)) {
            return CommandType.LIST;
        } else if (command.startsWith(MARK_COMMAND)) {
            return CommandType.MARK;
        } else if (command.startsWith(UNMARK_COMMAND)) {
            return CommandType.UNMARK;
        } else if (command.startsWith(TODO_COMMAND)) {
            return CommandType.TODO;
        } else if (command.startsWith(DEADLINE_COMMAND)) {
            return CommandType.DEADLINE;
        } else if (command.startsWith(EVENT_COMMAND)) {
            return CommandType.EVENT;
        } else if (command.startsWith(DELETE_COMMAND)) {
            return CommandType.DELETE;
        } else if (command.startsWith(FIND_COMMAND)) {
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
        String[] parts = input.split(" ", MIN_COMMAND_PARTS);
        if (parts.length < MIN_COMMAND_PARTS) {
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
        if (input.length() <= TODO_PREFIX_LENGTH - 1 || input.substring(TODO_PREFIX_LENGTH).trim().isEmpty()) {
            throw new IllegalArgumentException("The description of a todo cannot be empty.");
        }
        return input.substring(TODO_PREFIX_LENGTH).trim();
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
        if (input.length() <= FIND_PREFIX_LENGTH - 1 || input.substring(FIND_PREFIX_LENGTH).trim().isEmpty()) {
            throw new IllegalArgumentException("The search keyword cannot be empty.");
        }
        return input.substring(FIND_PREFIX_LENGTH).trim();
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
            task = new Todo(description);
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
