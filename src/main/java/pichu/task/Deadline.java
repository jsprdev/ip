package pichu.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a Deadline task with a due date.
 */
public class Deadline extends Task {

    private LocalDateTime deadline;
    private String originalInput;

    /**
     * Constructor for Deadline task.
     * @param name the task description
     * @param dateTimeInput the deadline date/time
     */
    public Deadline(String name, String dateTimeInput) {
        super(name);
        this.originalInput = dateTimeInput;
        this.deadline = parseDateTime(dateTimeInput);
    }

    private LocalDateTime parseDateTime(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }

        // Try different date formats
        String[] formats = {
                "yyyy-MM-dd HHmm", // 2019-12-02 1800
                "yyyy-MM-dd", // 2019-12-02
                "d/M/yyyy HHmm", // 2/12/2019 1800
                "d/M/yyyy", // 2/12/2019
                "dd/MM/yyyy HHmm", // 02/12/2019 1800
                "dd/MM/yyyy" // 02/12/2019
        };

        for (String format : formats) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                return LocalDateTime.parse(input.trim(), formatter);
            } catch (DateTimeParseException e) {
                // Continue to next format
            }
        }

        // If no format matches, return null and keep original string
        return null;
    }

    /**
     * Returns the deadline field of the task.
     *
     * @return deadline of the task.
     *
     */
    public LocalDateTime getDeadline() {
        return deadline;
    }

    /**
     * Returns the deadline in a user-friendly formatted string.
     * If parsing failed, returns the original input string.
     *
     * @return formatted deadline string or original input if parsing failed.
     *
     */
    public String getFormattedDeadline() {
        if (deadline != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a");
            return deadline.format(formatter);
        }
        return originalInput; // Fallback to original input if parsing failed
    }

    @Override
    public String getType() {
        return "D";
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + getFormattedDeadline() + ")";
    }

    @Override
    public String toFileFormat() {
        return "D|" + (isCompleted() ? "1" : "0") + "|" + getName() + "|" + originalInput;
    }
}
