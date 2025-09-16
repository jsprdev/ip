package pichu;

import java.util.ArrayList;
import java.util.List;

import pichu.core.TaskList;
import pichu.parser.Parser;
import pichu.storage.Storage;
import pichu.task.Deadline;
import pichu.task.Event;
import pichu.task.Task;
import pichu.task.Todo;

/**
 * Main class for the Pichu chatbot application.
 */

// AI USAGE! for A-AiAssisted
// Used GitHub Copilot to assist in writing methods that handle user commands and adding personality for A-Personality
public class Pichu {
    private static final String DEFAULT_FILE_PATH = "data/tasks.txt";
    private static final String EMPTY_COMMAND_MESSAGE = "SEWY... Please enter a command!";
    private static final String UNKNOWN_COMMAND_MESSAGE = "SEWY... I'm sorry, I don't know what that means! :-(";
    private static final String GOODBYE_MESSAGE = "SUIII! Bye. Hope to see you again soon!";
    private static final String EMPTY_TASK_LIST_MESSAGE = "SEWY... Your task list is empty!";
    private static final String TASK_LIST_HEADER = "SUIII! Here are the tasks in your list:\n";
    private static final String MATCHING_TASKS_HEADER = "SUIII! Here are the matching tasks in your list:\n";
    private static final String NO_MATCHES_MESSAGE = "SEWY... No matching tasks found.";
    private static final String INVALID_NUMBER_MESSAGE = "SEWY... Invalid task number format.";
    private static final String INDEX_OUT_OF_RANGE_MESSAGE = "SEWY... Task number is out of range.";
    private static final String ERROR_PREFIX = "SEWY... ";

    private final Storage storage;
    private final TaskList taskList;

    /**
     * Constructor for Pichu chatbot.
     * @param filePath the file path for task storage
     */
    public Pichu(String filePath) {
        storage = new Storage(filePath);
        taskList = new TaskList();

        // Load existing tasks
        List<String> savedTasks = storage.loadTasks();
        taskList.loadTasks(savedTasks);
    }

    // Overloaded constructor
    public Pichu() {
        this(DEFAULT_FILE_PATH);
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        try {
            String fullCommand = input.trim();
            if (fullCommand.isEmpty()) {
                return EMPTY_COMMAND_MESSAGE;
            }

            Parser.CommandType commandType = Parser.getCommandType(fullCommand);
            return executeCommand(commandType, fullCommand);
        } catch (Exception e) {
            return ERROR_PREFIX + e.getMessage();
        }
    }

    private String executeCommand(Parser.CommandType commandType, String fullCommand) {
        switch (commandType) {
        case BYE:
            return GOODBYE_MESSAGE;
        case LIST:
            return formatTaskList(taskList.getTasks());
        case MARK:
            return handleMarkCommand(fullCommand);
        case UNMARK:
            return handleUnmarkCommand(fullCommand);
        case TODO:
            return handleTodoCommand(fullCommand);
        case DEADLINE:
            return handleDeadlineCommand(fullCommand);
        case EVENT:
            return handleEventCommand(fullCommand);
        case DELETE:
            return handleDeleteCommand(fullCommand);
        case FIND:
            return handleFindCommand(fullCommand);
        case TAG:
            return handleTagCommand(fullCommand);
        default:
            return UNKNOWN_COMMAND_MESSAGE;
        }
    }

    private String formatTaskList(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            return EMPTY_TASK_LIST_MESSAGE;
        }

        StringBuilder sb = new StringBuilder(TASK_LIST_HEADER);
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            sb.append(formatTaskForDisplay(task, i + 1)).append("\n");
        }
        return sb.toString().trim();
    }

    private String formatTaskForDisplay(Task task, int displayIndex) {
        return displayIndex + ".[" + task.getType() + "]" + task;
    }

    private String handleIndexBasedCommand(String fullCommand, IndexBasedOperation operation) {
        try {
            int index = Parser.parseIndex(fullCommand);
            return operation.execute(index);
        } catch (NumberFormatException e) {
            return INVALID_NUMBER_MESSAGE;
        } catch (IndexOutOfBoundsException e) {
            return INDEX_OUT_OF_RANGE_MESSAGE;
        }
    }

    @FunctionalInterface
    private interface IndexBasedOperation {
        String execute(int index) throws IndexOutOfBoundsException;
    }

    private String handleMarkCommand(String fullCommand) {
        return handleIndexBasedCommand(fullCommand, index -> {
            taskList.markTask(index);
            Task task = taskList.getTask(index);
            storage.saveAllTasks(taskList.getTasks());
            return "SUIIIII! I've marked this task as done:\n[X] " + task.getName();
        });
    }

    private String handleUnmarkCommand(String fullCommand) {
        return handleIndexBasedCommand(fullCommand, index -> {
            taskList.unmarkTask(index);
            Task task = taskList.getTask(index);
            storage.saveAllTasks(taskList.getTasks());
            return "SEWY...,I've marked this task as not done yet:\n[ ] " + task.getName();
        });
    }

    private String handleTodoCommand(String fullCommand) {
        try {
            String description = Parser.parseTodoDescription(fullCommand);
            Task newTask = new Todo(description);
            taskList.addTask(newTask);
            storage.saveTask(newTask.toFileFormat());
            return formatTaskAddedMessage(newTask);
        } catch (IllegalArgumentException e) {
            return ERROR_PREFIX + e.getMessage();
        }
    }

    private String handleDeadlineCommand(String fullCommand) {
        try {
            String[] parsed = Parser.parseDeadlineCommand(fullCommand);
            Task newTask = new Deadline(parsed[0], parsed[1]);
            taskList.addTask(newTask);
            storage.saveTask(newTask.toFileFormat());
            return formatTaskAddedMessage(newTask);
        } catch (IllegalArgumentException e) {
            return ERROR_PREFIX + e.getMessage();
        }
    }

    private String handleEventCommand(String fullCommand) {
        try {
            String[] parsed = Parser.parseEventCommand(fullCommand);
            Task newTask = new Event(parsed[0], parsed[1], parsed[2]);
            taskList.addTask(newTask);
            storage.saveTask(newTask.toFileFormat());
            return formatTaskAddedMessage(newTask);
        } catch (IllegalArgumentException e) {
            return ERROR_PREFIX + e.getMessage();
        }
    }

    private String handleDeleteCommand(String fullCommand) {
        return handleIndexBasedCommand(fullCommand, index -> {
            Task taskToDelete = taskList.getTask(index);
            taskList.deleteTask(index);
            storage.saveAllTasks(taskList.getTasks());
            return "SUIIII. I've removed this task:\n  [" + taskToDelete.getType() + "][" + taskToDelete.getCompletion() + "] " + taskToDelete.getName() + "\nNow you have " + taskList.size() + " task(s) in the list.";
        });
    }

    private String handleTagCommand(String fullCommand) {
        try {
            String tag = Parser.parseTagKeyword(fullCommand);
            ArrayList<Task> foundTasks = taskList.findTasksByTag(tag);
            return formatTagResults(foundTasks, tag);
        } catch (IllegalArgumentException e) {
            return ERROR_PREFIX + e.getMessage();
        }
    }

    private String formatTagResults(ArrayList<Task> foundTasks, String tag) {
        if (foundTasks.isEmpty()) {
            return "SEWY...No tasks found with tag #" + tag + ".";
        }

        StringBuilder sb = new StringBuilder("Here are the tasks with tag #" + tag + ":\n");
        for (int i = 0; i < foundTasks.size(); i++) {
            Task task = foundTasks.get(i);
            sb.append(formatTaskForDisplay(task, i + 1)).append("\n");
        }
        return sb.toString().trim();
    }

    private String handleFindCommand(String fullCommand) {
        try {
            String keyword = Parser.parseFindKeyword(fullCommand);
            ArrayList<Task> foundTasks = taskList.findTasks(keyword);
            return formatFindResults(foundTasks);
        } catch (IllegalArgumentException e) {
            return ERROR_PREFIX + e.getMessage();
        }
    }

    private String formatTaskAddedMessage(Task newTask) {
        return "GOLAZO!!!! I've added this task:\n  [" + newTask.getType() + "]["
            + newTask.getCompletion() + "] " + newTask.getName()
            + getTaskTimeInfo(newTask) + "\nNow you have " + taskList.size()
            + " task(s) in the list.";
    }

    private String formatFindResults(ArrayList<Task> foundTasks) {
        if (foundTasks.isEmpty()) {
            return NO_MATCHES_MESSAGE;
        }

        StringBuilder sb = new StringBuilder(MATCHING_TASKS_HEADER);
        for (int i = 0; i < foundTasks.size(); i++) {
            Task task = foundTasks.get(i);
            sb.append(formatTaskForDisplay(task, i + 1)).append("\n");
        }
        return sb.toString().trim();
    }

    private String getTaskTimeInfo(Task task) {
        if (task instanceof Deadline) {
            return " (by: " + ((Deadline) task).getDeadline() + ")";
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return " (from: " + event.getStartDateTime() + " to: " + event.getEndDateTime() + ")";
        }
        return "";
    }

//    public static void main(String[] args) {
//    }
}
