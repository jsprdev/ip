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
        String completionStatus = isCompleted() ? COMPLETED_STATUS_FILE : INCOMPLETE_STATUS_FILE;
        String tagsString = String.join(",", getTags());
        return "T|" + completionStatus + "|" + getName() + "|" + tagsString;
    }
}
