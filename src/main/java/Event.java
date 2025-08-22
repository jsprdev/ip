package main.java;

public class Event extends Task{

    String start;
    String end;

    public Event(String name, String start, String end) {
        super(name);
        this.start = start;
        this.end = end;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    @Override
    public String getType() {
        return "E";
    };

    @Override
    public String toString() {
        return super.toString() + " (from: " + start + " to: " + end + ")";
    }

}
