package pichu.task;

/**
 * Represents a Todo task.
 */
public class Todo extends Task {
    public Todo(String name) {
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
