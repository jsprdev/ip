package pichu.task;

public class Task {
    String name;
    boolean isCompleted = false;

    public Task(String name) {
        this.name = name;
    }

    /**
     * Returns the name field of the task.
     *
     * @return name of the task.
     *
     */
    public String getName() {
        return name;
    }


    /**
     * Returns the isCompleted field of the task.
     *
     * @return true if the task is completed, false otherwise.
     *
     */
    public boolean isCompleted() {
        return isCompleted;
    }

    /**
     * Sets the name field of the task.
     *
     *
     * @param name the name to set for task.
     *
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the isCompleted field of the task.
     *
     * @param completed the status of completion of the task.
     *
     */
    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @Override
    public String toString(){
        return (isCompleted ? "[X]" : "[ ]") + " " + name;
    }


    /**
     * Returns the type of the task in terms of a single letter.
     *
     * @return a single-letter string representing the type of the task.
     *
     */
    public String getType() {
        return "-";
    };

    /**
     * Returns the type of the task in terms of a single letter.
     *
     * @return a single-letter string representing the type of the task.
     *
     */
    public String getCompletion() {
        if (isCompleted) {
            return "X";
        }
        return " ";
    }


    /**
     * Returns the file formatted string of the task.
     *
     * @return a string representing the file-formatted task.
     *
     */
    public String toFileFormat() {
        return "T|" + (isCompleted ? "1" : "0") + "|" + name;
    }

}
