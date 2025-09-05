package pichu.task;

/**
 * Represents a ToDo task.
 */
public class ToDo extends Task {
    public ToDo(String name) {
        super(name);
    }

    @Override
    public String getType() {
        return "T";
    }

    @Override
    public String toFileFormat() {
        return "T|" + (isCompleted() ? "1" : "0") + "|" + getName();
    }
}
