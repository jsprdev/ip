package pichu;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import pichu.core.TaskList;
import pichu.parser.Parser;
import pichu.storage.Storage;
import pichu.task.Deadline;
import pichu.task.Event;
import pichu.task.Task;
import pichu.task.Todo;
import pichu.ui.TextUi;

/**
 * Main class for the Pichu chatbot application.
 */
public class Pichu {
    private Storage storage;
    private TaskList taskList;
    private TextUi textUi;


    private static final String DEFAULT_FILE_PATH = "data/tasks.txt";

    /**
     * Constructor for Pichu chatbot.
     * @param filePath the file path for task storage
     */
    public Pichu(String filePath) {
        textUi = new TextUi();
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
                return "Please enter a command!";
            }

            Parser.CommandType commandType = Parser.getCommandType(fullCommand);

            switch (commandType) {
            case BYE:
                return "Bye. Hope to see you again soon!";

            case LIST:
                return formatTaskList(taskList.getTasks());

            case MARK:
                return handleMarkCommandGui(fullCommand);

            case UNMARK:
                return handleUnmarkCommandGui(fullCommand);

            case TODO:
                return handleTodoCommandGui(fullCommand);

            case DEADLINE:
                return handleDeadlineCommandGui(fullCommand);

            case EVENT:
                return handleEventCommandGui(fullCommand);

            case DELETE:
                return handleDeleteCommandGui(fullCommand);

            case FIND:
                return handleFindCommandGui(fullCommand);

            default:
                return "I'm sorry, I don't know what that means! :-(";
            }
        } catch (Exception e) {
            return "OOPS!!! " + e.getMessage();
        }
    }

    private String formatTaskList(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            return "Your task list is empty!";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            Task temp = tasks.get(i);
            sb.append((i + 1)).append(".").append("[").append(temp.getType()).append("]").append(temp.toString()).append("\n");
        }
        return sb.toString().trim();
    }

    private String handleMarkCommandGui(String fullCommand) {
        try {
            int index = Parser.parseIndex(fullCommand);
            taskList.markTask(index);
            Task task = taskList.getTask(index);
            storage.saveAllTasks(taskList.getTasks());
            return "Nice! I've marked this task as done:\n[X] " + task.getName();
        } catch (NumberFormatException e) {
            return "OOPS!!! Invalid task number format.";
        } catch (IndexOutOfBoundsException e) {
            return "OOPS!!! Task number is out of range.";
        }
    }

    private String handleUnmarkCommandGui(String fullCommand) {
        try {
            int index = Parser.parseIndex(fullCommand);
            taskList.unmarkTask(index);
            Task task = taskList.getTask(index);
            storage.saveAllTasks(taskList.getTasks());
            return "OK, I've marked this task as not done yet:\n[ ] " + task.getName();
        } catch (NumberFormatException e) {
            return "OOPS!!! Invalid task number format.";
        } catch (IndexOutOfBoundsException e) {
            return "OOPS!!! Task number is out of range.";
        }
    }

    private String handleTodoCommandGui(String fullCommand) {
        try {
            String description = Parser.parseTodoDescription(fullCommand);
            Task newTask = new Todo(description);
            taskList.addTask(newTask);
            storage.saveTask(newTask.toFileFormat());
            return "Got it. I've added this task:\n  [" + newTask.getType() + "][" + newTask.getCompletion() + "] " + newTask.getName() + "\nNow you have " + taskList.size() + " task(s) in the list.";
        } catch (IllegalArgumentException e) {
            return "OOPS!!! " + e.getMessage();
        }
    }

    private String handleDeadlineCommandGui(String fullCommand) {
        try {
            String[] parsed = Parser.parseDeadlineCommand(fullCommand);
            String description = parsed[0];
            String deadline = parsed[1];

            Task newTask = new Deadline(description, deadline);
            taskList.addTask(newTask);
            storage.saveTask(newTask.toFileFormat());
            return "Got it. I've added this task:\n  [" + newTask.getType() + "][" + newTask.getCompletion() + "] " + newTask.getName() + getTaskTimeInfoGui(newTask) + "\nNow you have " + taskList.size() + " task(s) in the list.";
        } catch (IllegalArgumentException e) {
            return "OOPS!!! " + e.getMessage();
        }
    }

    private String handleEventCommandGui(String fullCommand) {
        try {
            String[] parsed = Parser.parseEventCommand(fullCommand);
            String description = parsed[0];
            String start = parsed[1];
            String end = parsed[2];

            Task newTask = new Event(description, start, end);
            taskList.addTask(newTask);
            storage.saveTask(newTask.toFileFormat());
            return "Got it. I've added this task:\n  [" + newTask.getType() + "][" + newTask.getCompletion() + "] " + newTask.getName() + getTaskTimeInfoGui(newTask) + "\nNow you have " + taskList.size() + " task(s) in the list.";
        } catch (IllegalArgumentException e) {
            return "OOPS!!! " + e.getMessage();
        }
    }

    private String handleDeleteCommandGui(String fullCommand) {
        try {
            int index = Parser.parseIndex(fullCommand);
            Task taskToDelete = taskList.getTask(index);
            taskList.deleteTask(index);
            storage.saveAllTasks(taskList.getTasks());
            return "Noted. I've removed this task:\n  [" + taskToDelete.getType() + "][" + taskToDelete.getCompletion() + "] " + taskToDelete.getName() + "\nNow you have " + taskList.size() + " task(s) in the list.";
        } catch (NumberFormatException e) {
            return "OOPS!!! Invalid task number format.";
        } catch (IndexOutOfBoundsException e) {
            return "OOPS!!! Task number is out of range.";
        }
    }

    private String handleFindCommandGui(String fullCommand) {
        try {
            String keyword = Parser.parseFindKeyword(fullCommand);
            ArrayList<Task> foundTasks = taskList.findTasks(keyword);
            if (foundTasks.isEmpty()) {
                return "No matching tasks found.";
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Here are the matching tasks in your list:\n");
            for (int i = 0; i < foundTasks.size(); i++) {
                Task temp = foundTasks.get(i);
                sb.append((i + 1)).append(".").append("[").append(temp.getType()).append("]").append(temp.toString()).append("\n");
            }
            return sb.toString().trim();
        } catch (IllegalArgumentException e) {
            return "OOPS!!! " + e.getMessage();
        }
    }

    private String getTaskTimeInfoGui(Task task) {
        if (task instanceof Deadline) {
            return " (by: " + ((Deadline) task).getDeadline() + ")";
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return " (from: " + event.getStartDateTime() + " to: " + event.getEndDateTime() + ")";
        }
        return "";
    }



// DEPRECATED: Old text-based UI

//    /**
//     * Main run method to start the chatbot.
//     */
//    public void run() {
//        textUi.showWelcome();
//
//
//        boolean isExit = false;
//        while (!isExit) {
//            try {
//                String fullCommand = textUi.readCommand();
//                Parser.CommandType commandType = Parser.getCommandType(fullCommand);
//
//                switch (commandType) {
//                case BYE:
//                    isExit = true;
//                    textUi.showGoodbye();
//                    break;
//
//                case LIST:
//                    textUi.showTaskList(taskList.getTasks());
//                    break;
//
//                case MARK:
//                    handleMarkCommand(fullCommand);
//                    break;
//
//                case UNMARK:
//                    handleUnmarkCommand(fullCommand);
//                    break;
//
//                case TODO:
//                    handleTodoCommand(fullCommand);
//                    break;
//
//                case DEADLINE:
//                    handleDeadlineCommand(fullCommand);
//                    break;
//
//                case EVENT:
//                    handleEventCommand(fullCommand);
//                    break;
//
//                case DELETE:
//                    handleDeleteCommand(fullCommand);
//                    break;
//
//                case FIND:
//                    handleFindCommand(fullCommand);
//                    break;
//
//                default:
//                    textUi.showError("I'm sorry, I don't know what that means! :-(");
//                    break;
//                }
//            } catch (Exception e) {
//                textUi.showError(e.getMessage());
//            }
//        }
//    }
//
//    private void handleMarkCommand(String fullCommand) {
//        try {
//            int index = Parser.parseIndex(fullCommand);
//            taskList.markTask(index);
//            Task task = taskList.getTask(index);
//            textUi.showTaskMarked(task);
//            storage.saveAllTasks(taskList.getTasks());
//        } catch (NumberFormatException e) {
//            textUi.showError("Invalid task number format.");
//        } catch (IndexOutOfBoundsException e) {
//            textUi.showError("Task number is out of range.");
//        }
//    }
//
//    private void handleUnmarkCommand(String fullCommand) {
//        try {
//            int index = Parser.parseIndex(fullCommand);
//            taskList.unmarkTask(index);
//            Task task = taskList.getTask(index);
//            textUi.showTaskUnmarked(task);
//            storage.saveAllTasks(taskList.getTasks());
//        } catch (NumberFormatException e) {
//            textUi.showError("Invalid task number format.");
//        } catch (IndexOutOfBoundsException e) {
//            textUi.showError("Task number is out of range.");
//        }
//    }
//
//    private void handleTodoCommand(String fullCommand) {
//        try {
//            String description = Parser.parseTodoDescription(fullCommand);
//            Task newTask = new Todo(description);
//            taskList.addTask(newTask);
//            textUi.showTaskAdded(newTask, taskList.size());
//            storage.saveTask(newTask.toFileFormat());
//        } catch (IllegalArgumentException e) {
//            textUi.showError(e.getMessage());
//        }
//    }
//
//    private void handleDeadlineCommand(String fullCommand) {
//        try {
//            String[] parsed = Parser.parseDeadlineCommand(fullCommand);
//            String description = parsed[0];
//            String deadline = parsed[1];
//
//            Task newTask = new Deadline(description, deadline);
//            taskList.addTask(newTask);
//            textUi.showTaskAdded(newTask, taskList.size());
//            storage.saveTask(newTask.toFileFormat());
//        } catch (IllegalArgumentException e) {
//            textUi.showError(e.getMessage());
//        }
//    }
//
//    private void handleEventCommand(String fullCommand) {
//        try {
//            String[] parsed = Parser.parseEventCommand(fullCommand);
//            String description = parsed[0];
//            String start = parsed[1];
//            String end = parsed[2];
//
//            Task newTask = new Event(description, start, end);
//            taskList.addTask(newTask);
//            textUi.showTaskAdded(newTask, taskList.size());
//            storage.saveTask(newTask.toFileFormat());
//        } catch (IllegalArgumentException e) {
//            textUi.showError(e.getMessage());
//        }
//    }
//
//    private void handleDeleteCommand(String fullCommand) {
//        try {
//            int index = Parser.parseIndex(fullCommand);
//            taskList.deleteTask(index);
//            textUi.showTaskDeleted();
//            storage.saveAllTasks(taskList.getTasks());
//        } catch (NumberFormatException e) {
//            textUi.showError("Invalid task number format.");
//        } catch (IndexOutOfBoundsException e) {
//            textUi.showError("Task number is out of range.");
//        }
//    }
//
//    private void handleFindCommand(String fullCommand) {
//        try {
//            String keyword = Parser.parseFindKeyword(fullCommand);
//            ArrayList<Task> foundTasks = taskList.findTasks(keyword);
//            textUi.showFindResults(foundTasks);
//        } catch (IllegalArgumentException e) {
//            textUi.showError(e.getMessage());
//        }
//    }


    public static void main(String[] args) {
//        new Pichu("data/tasks.txt").run();
    }
}
