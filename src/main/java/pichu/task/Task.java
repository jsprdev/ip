package pichu.task;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a task with a name, completion status, and tags.
 */
public class Task {
    protected static final String COMPLETED_STATUS_FILE = "1";
    protected static final String INCOMPLETE_STATUS_FILE = "0";
    protected static final String COMPLETED_SYMBOL = "X";
    protected static final String INCOMPLETE_SYMBOL = " ";
    private static final String TAG_SEPARATOR = ",";
    private static final Pattern TAG_PATTERN = Pattern.compile("#\\w+");

    private String name;
    private boolean isCompleted = false;
    private List<String> tags;

    public Task(String name) {
        this.name = name;
        this.tags = new ArrayList<>();
        extractTagsFromName();
    }

    /**
     * Extracts tags from the task name and stores them separately.
     * Tags are in the format #tagname and are removed from the display name.
     */
    private void extractTagsFromName() {
        Matcher matcher = TAG_PATTERN.matcher(name);
        StringBuilder nameWithoutTags = new StringBuilder(name);

        while (matcher.find()) {
            String tag = matcher.group().substring(1); // Remove the # symbol
            if (!tags.contains(tag)) {
                tags.add(tag);
            }
        }

        // Remove tags from the name for display purposes
        this.name = name.replaceAll("#\\w+", "").trim().replaceAll("\\s+", " ");
    }

    /**
     * Returns the name field of the task (without tags).
     *
     * @return name of the task.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the original name with tags included.
     *
     * @return name of the task with tags.
     */
    public String getNameWithTags() {
        if (tags.isEmpty()) {
            return name;
        }
        StringBuilder result = new StringBuilder(name);
        for (String tag : tags) {
            result.append(" #").append(tag);
        }
        return result.toString();
    }

    /**
     * Returns the list of tags associated with this task.
     *
     * @return list of tags.
     */
    public List<String> getTags() {
        return new ArrayList<>(tags);
    }

    /**
     * Adds a tag to the task.
     *
     * @param tag the tag to add (without # symbol).
     */
    public void addTag(String tag) {
        if (!tags.contains(tag) && !tag.isEmpty()) {
            tags.add(tag);
        }
    }

    /**
     * Removes a tag from the task.
     *
     * @param tag the tag to remove (without # symbol).
     */
    public void removeTag(String tag) {
        tags.remove(tag);
    }

    /**
     * Checks if the task has a specific tag.
     *
     * @param tag the tag to check for (without # symbol).
     * @return true if the task has the tag, false otherwise.
     */
    public boolean hasTag(String tag) {
        return tags.contains(tag);
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
        StringBuilder result = new StringBuilder();
        result.append(isCompleted ? "[X]" : "[ ]").append(" ").append(name);

        if (!tags.isEmpty()) {
            for (String tag : tags) {
                result.append(" #").append(tag);
            }
        }

        return result.toString();
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
        String tagsString = String.join(TAG_SEPARATOR, tags);
        return "T|" + completionStatus + "|" + name + "|" + tagsString;
    }
}
