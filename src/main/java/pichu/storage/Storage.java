package pichu.storage;

import pichu.task.Task;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public final class Storage {
    private final Path dataPath;

    public Storage(String filePath) {
        this.dataPath = Paths.get(filePath);
        createFileIfNotExists();
    }

    private void createFileIfNotExists() {
        try {
            if (!Files.exists(dataPath)) {
                Files.createDirectories(dataPath.getParent());
                Files.createFile(dataPath);
            }
        } catch (IOException e) {
            System.err.println("Error creating storage file: " + e.getMessage());
        }
    }

    /**
     * Saves a task to the storage file.
     *
     * @param taskData The string representation of the task to be saved.
     *
     */
    public void saveTask(String taskData){
        try {
            Files.write(dataPath, (taskData + System.lineSeparator()).getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Error saving task: " + e.getMessage());
        }
    }

    /**
     * Loads all tasks from the storage file.
     *
     * @return List of task strings
     *
     */
    public List<String> loadTasks() {
        try {
            if (Files.exists(dataPath)) {
                return Files.readAllLines(dataPath);
            }
        } catch (IOException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    /**
     * Saves all tasks to storage file, overwriting existing content.
     *
     * @param tasks List of tasks to be saved
     *
     */
    public void saveAllTasks(List<Task> tasks) {
        try {
            List<String> taskStrings = new ArrayList<>();
            for (Task task : tasks) {
                taskStrings.add(task.toFileFormat());
            }
            Files.write(dataPath, taskStrings, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error saving all tasks: " + e.getMessage());
        }
    }
}
