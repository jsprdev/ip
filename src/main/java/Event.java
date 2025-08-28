package main.java;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Event extends Task{

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String originalStartInput;
    private String originalEndInput;

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
            "yyyy-MM-dd HHmm",     // 2019-12-02 1800
            "yyyy-MM-dd",          // 2019-12-02
            "d/M/yyyy HHmm",       // 2/12/2019 1800
            "d/M/yyyy",            // 2/12/2019
            "dd/MM/yyyy HHmm",     // 02/12/2019 1800
            "dd/MM/yyyy"           // 02/12/2019
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

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public String getFormattedStart() {
        if (startDateTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a");
            return startDateTime.format(formatter);
        }
        return originalStartInput; // Fallback to original input if parsing failed
    }

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
        return "E|" + (isCompleted ? "1" : "0") + "|" + name + "|" + originalStartInput + "|" + originalEndInput;
    }
}
