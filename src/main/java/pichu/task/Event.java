package pichu.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents an Event task with start and end times.
 */
public class Event extends Task {

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String originalStartInput;
    private String originalEndInput;

    /**
     * Constructor for Event task.
     * @param name the task description
     * @param startInput the start date/time
     * @param endInput the end date/time
     */
    public Event(String name, String startInput, String endInput) {
        super(name);
        this.originalStartInput = startInput;
        this.originalEndInput = endInput;
        this.startDateTime = parseDateTime(startInput);
        this.endDateTime = parseDateTime(endInput);
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
     * Returns the startDateTime field of the task.
     *
     * @return start date and time of the event.
     *
     */
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     * Returns the endDateTime field of the task.
     *
     * @return end date and time of the event.
     *
     */
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     * Returns the formatted start date and time of the event.
     *
     * @return formatted start date and time string.
     *
     */
    public String getFormattedStart() {
        if (startDateTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a");
            return startDateTime.format(formatter);
        }
        return originalStartInput; // Fallback to original input if parsing failed
    }

    /**
     * Returns the formatted end date and time of the event.
     *
     * @return formatted end date and time string.
     *
     */
    public String getFormattedEnd() {
        if (endDateTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a");
            return endDateTime.format(formatter);
        }
        return originalEndInput; // Fallback to original input if parsing failed
    }

    @Override
    public String getType() {
        return "E";
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + getFormattedStart() + " to: " + getFormattedEnd() + ")";
    }

    @Override
    public String toFileFormat() {
        String completionStatus = isCompleted() ? COMPLETED_STATUS_FILE : INCOMPLETE_STATUS_FILE;
        String tagsString = String.join(",", getTags());
        return "E|" + completionStatus + "|" + getName() + "|" + originalStartInput + "|" + originalEndInput + "|" + tagsString;
    }
}
