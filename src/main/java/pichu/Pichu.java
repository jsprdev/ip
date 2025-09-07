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

    /**
     * Main run method to start the chatbot.
     */
    public void run() {
        textUi.showWelcome();


        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = textUi.readCommand();
                Parser.CommandType commandType = Parser.getCommandType(fullCommand);

                switch (commandType) {
                case BYE:
                    isExit = true;
                    textUi.showGoodbye();
                    break;

                case LIST:
                    textUi.showTaskList(taskList.getTasks());
                    break;

                case MARK:
                    handleMarkCommand(fullCommand);
                    break;

                case UNMARK:
                    handleUnmarkCommand(fullCommand);
                    break;

                case TODO:
                    handleTodoCommand(fullCommand);
                    break;

                case DEADLINE:
                    handleDeadlineCommand(fullCommand);
                    break;

                case EVENT:
                    handleEventCommand(fullCommand);
                    break;

                case DELETE:
                    handleDeleteCommand(fullCommand);
                    break;

                case FIND:
                    handleFindCommand(fullCommand);
                    break;

                default:
                    textUi.showError("I'm sorry, I don't know what that means! :-(");
                    break;
                }
            } catch (Exception e) {
                textUi.showError(e.getMessage());
            }
        }
    }

    private void handleMarkCommand(String fullCommand) {
        try {
            int index = Parser.parseIndex(fullCommand);
            taskList.markTask(index);
            Task task = taskList.getTask(index);
            textUi.showTaskMarked(task);
            storage.saveAllTasks(taskList.getTasks());
        } catch (NumberFormatException e) {
            textUi.showError("Invalid task number format.");
        } catch (IndexOutOfBoundsException e) {
            textUi.showError("Task number is out of range.");
        }
    }

    private void handleUnmarkCommand(String fullCommand) {
        try {
            int index = Parser.parseIndex(fullCommand);
            taskList.unmarkTask(index);
            Task task = taskList.getTask(index);
            textUi.showTaskUnmarked(task);
            storage.saveAllTasks(taskList.getTasks());
        } catch (NumberFormatException e) {
            textUi.showError("Invalid task number format.");
        } catch (IndexOutOfBoundsException e) {
            textUi.showError("Task number is out of range.");
        }
    }

    private void handleTodoCommand(String fullCommand) {
        try {
            String description = Parser.parseTodoDescription(fullCommand);
            Task newTask = new Todo(description);
            taskList.addTask(newTask);
            textUi.showTaskAdded(newTask, taskList.size());
            storage.saveTask(newTask.toFileFormat());
        } catch (IllegalArgumentException e) {
            textUi.showError(e.getMessage());
        }
    }

    private void handleDeadlineCommand(String fullCommand) {
        try {
            String[] parsed = Parser.parseDeadlineCommand(fullCommand);
            String description = parsed[0];
            String deadline = parsed[1];

            Task newTask = new Deadline(description, deadline);
            taskList.addTask(newTask);
            textUi.showTaskAdded(newTask, taskList.size());
            storage.saveTask(newTask.toFileFormat());
        } catch (IllegalArgumentException e) {
            textUi.showError(e.getMessage());
        }
    }

    private void handleEventCommand(String fullCommand) {
        try {
            String[] parsed = Parser.parseEventCommand(fullCommand);
            String description = parsed[0];
            String start = parsed[1];
            String end = parsed[2];

            Task newTask = new Event(description, start, end);
            taskList.addTask(newTask);
            textUi.showTaskAdded(newTask, taskList.size());
            storage.saveTask(newTask.toFileFormat());
        } catch (IllegalArgumentException e) {
            textUi.showError(e.getMessage());
        }
    }

    private void handleDeleteCommand(String fullCommand) {
        try {
            int index = Parser.parseIndex(fullCommand);
            taskList.deleteTask(index);
            textUi.showTaskDeleted();
            storage.saveAllTasks(taskList.getTasks());
        } catch (NumberFormatException e) {
            textUi.showError("Invalid task number format.");
        } catch (IndexOutOfBoundsException e) {
            textUi.showError("Task number is out of range.");
        }
    }

    private void handleFindCommand(String fullCommand) {
        try {
            String keyword = Parser.parseFindKeyword(fullCommand);
            ArrayList<Task> foundTasks = taskList.findTasks(keyword);
            textUi.showFindResults(foundTasks);
        } catch (IllegalArgumentException e) {
            textUi.showError(e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Pichu("data/tasks.txt").run();
    }
}
