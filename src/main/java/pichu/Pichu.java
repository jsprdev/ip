package pichu;

import pichu.storage.Storage;
import pichu.core.TaskList;
import pichu.ui.Ui;
import pichu.parser.Parser;
import pichu.task.Task;
import pichu.task.ToDo;
import pichu.task.Deadline;
import pichu.task.Event;

import java.util.List;

public class Pichu {
    private Storage storage;
    private TaskList taskList;
    private Ui ui;

    public Pichu(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        taskList = new TaskList();

        // Load existing tasks
        List<String> savedTasks = storage.loadTasks();
        taskList.loadTasks(savedTasks);
    }

    public void run() {
        ui.showWelcome();


        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                Parser.CommandType commandType = Parser.getCommandType(fullCommand);

                switch (commandType) {
                    case BYE:
                        isExit = true;
                        ui.showGoodbye();
                        break;

                    case LIST:
                        ui.showTaskList(taskList.getTasks());
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

                    default:
                        ui.showError("I'm sorry, I don't know what that means! :-(");
                        break;
                }
            } catch (Exception e) {
                ui.showError(e.getMessage());
            }
        }
    }

    private void handleMarkCommand(String fullCommand) {
        try {
            int index = Parser.parseIndex(fullCommand);
            taskList.markTask(index);
            Task task = taskList.getTask(index);
            ui.showTaskMarked(task);
            storage.saveAllTasks(taskList.getTasks());
        } catch (NumberFormatException e) {
            ui.showError("Invalid task number format.");
        } catch (IndexOutOfBoundsException e) {
            ui.showError("Task number is out of range.");
        }
    }

    private void handleUnmarkCommand(String fullCommand) {
        try {
            int index = Parser.parseIndex(fullCommand);
            taskList.unmarkTask(index);
            Task task = taskList.getTask(index);
            ui.showTaskUnmarked(task);
            storage.saveAllTasks(taskList.getTasks());
        } catch (NumberFormatException e) {
            ui.showError("Invalid task number format.");
        } catch (IndexOutOfBoundsException e) {
            ui.showError("Task number is out of range.");
        }
    }

    private void handleTodoCommand(String fullCommand) {
        try {
            String description = Parser.parseTodoDescription(fullCommand);
            Task newTask = new ToDo(description);
            taskList.addTask(newTask);
            ui.showTaskAdded(newTask, taskList.size());
            storage.saveTask(newTask.toFileFormat());
        } catch (IllegalArgumentException e) {
            ui.showError(e.getMessage());
        }
    }

    private void handleDeadlineCommand(String fullCommand) {
        try {
            String[] parsed = Parser.parseDeadlineCommand(fullCommand);
            String description = parsed[0];
            String deadline = parsed[1];

            Task newTask = new Deadline(description, deadline);
            taskList.addTask(newTask);
            ui.showTaskAdded(newTask, taskList.size());
            storage.saveTask(newTask.toFileFormat());
        } catch (IllegalArgumentException e) {
            ui.showError(e.getMessage());
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
            ui.showTaskAdded(newTask, taskList.size());
            storage.saveTask(newTask.toFileFormat());
        } catch (IllegalArgumentException e) {
            ui.showError(e.getMessage());
        }
    }

    private void handleDeleteCommand(String fullCommand) {
        try {
            int index = Parser.parseIndex(fullCommand);
            taskList.deleteTask(index);
            ui.showTaskDeleted();
            storage.saveAllTasks(taskList.getTasks());
        } catch (NumberFormatException e) {
            ui.showError("Invalid task number format.");
        } catch (IndexOutOfBoundsException e) {
            ui.showError("Task number is out of range.");
        }
    }

    public static void main(String[] args) {
        new Pichu("data/tasks.txt").run();
    }
}
