package pichu.task;

/**
 * Represents a task with a name and completion status.
 */
public class Task {
    protected static final String COMPLETED_STATUS_FILE = "1";
    protected static final String INCOMPLETE_STATUS_FILE = "0";
    protected static final String COMPLETED_SYMBOL = "X";
    protected static final String INCOMPLETE_SYMBOL = " ";

    private String name;
    private boolean isCompleted = false;

    public Task(String name) {
        this.name = name;
    }

    /**
     * Returns the name field of the task.
     *
     * @return name of the task.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the isCompleted field of the task.
     *
     * @return true if the task is completed, false otherwise.
     */
    public boolean isCompleted() {
        return isCompleted;
    }

    /**
     * Sets the isCompleted field of the task.
     *
     * @param completed the status of completion of the task.
     */
    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @Override
    public String toString() {
        return (isCompleted ? "[X]" : "[ ]") + " " + name;
    }

    /**
     * Returns the type of the task in terms of a single letter.
     *
     * @return a single-letter string representing the type of the task.
     */
    public String getType() {
        return "-";
    }

    /**
     * Returns the completion status symbol.
     *
     * @return a single-letter string representing the completion status.
     */
    public String getCompletion() {
        return isCompleted ? COMPLETED_SYMBOL : INCOMPLETE_SYMBOL;
    }

    /**
     * Returns the file formatted string of the task.
     *
     * @return a string representing the file-formatted task.
     */
    public String toFileFormat() {
        String completionStatus = isCompleted ? COMPLETED_STATUS_FILE : INCOMPLETE_STATUS_FILE;
        return "T|" + completionStatus + "|" + name;
    }
}
