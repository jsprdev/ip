package pichu.core;

import java.util.ArrayList;
import java.util.List;

import pichu.parser.Parser;
import pichu.task.Task;

/**
 * Contains the task list and operations to add/delete tasks in the list.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Creates an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a TaskList with the given list of tasks.
     *
     * @param tasks the list of tasks to initialize with
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the list.
     *
     * @param task the task to add
     */
    public void addTask(Task task) {
        assert task != null : "Task to be added should not be null";
        tasks.add(task);
    }

    /**
     * Removes a task from the list at the specified index.
     *
     * @param index the 1-based index of the task to remove
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public void deleteTask(int index) throws IndexOutOfBoundsException {
        if (index < 1 || index > tasks.size()) {
            throw new IndexOutOfBoundsException("Invalid task index: " + index);
        }
        tasks.remove(index - 1);
    }

    /**
     * Gets a task at the specified index.
     *
     * @param index the 1-based index of the task
     * @return the task at the specified index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public Task getTask(int index) throws IndexOutOfBoundsException {
        if (index < 1 || index > tasks.size()) {
            throw new IndexOutOfBoundsException("Invalid task index: " + index);
        }
        return tasks.get(index - 1);
    }

    /**
     * Marks a task as completed.
     *
     * @param index the 1-based index of the task to mark
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public void markTask(int index) throws IndexOutOfBoundsException {
        if (index < 1 || index > tasks.size()) {
            throw new IndexOutOfBoundsException("Invalid task index: " + index);
        }
        tasks.get(index - 1).setCompleted(true);
    }

    /**
     * Marks a task as not completed.
     *
     * @param index the 1-based index of the task to unmark
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public void unmarkTask(int index) throws IndexOutOfBoundsException {
        if (index < 1 || index > tasks.size()) {
            throw new IndexOutOfBoundsException("Invalid task index: " + index);
        }
        tasks.get(index - 1).setCompleted(false);
    }

    /**
     * Gets the number of tasks in the list.
     *
     * @return the size of the task list
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Gets all tasks in the list.
     *
     * @return the ArrayList of tasks
     */
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks);
    }

    /**
     * Finds tasks that contain the specified keyword in their description.
     *
     * @param keyword the keyword to search for
     * @return a list of tasks that contain the keyword
     */
    public ArrayList<Task> findTasks(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getName().toLowerCase().contains(keyword.toLowerCase())) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }

    /**
     * Loads tasks from a list of task data strings.
     *
     * @param taskDataList the list of task data strings from storage
     */
    public void loadTasks(List<String> taskDataList) {
        tasks.clear();
        for (String taskData : taskDataList) {
            Task task = Parser.parseTaskFromString(taskData);
            if (task != null) {
                tasks.add(task);
            }
        }
    }
}
