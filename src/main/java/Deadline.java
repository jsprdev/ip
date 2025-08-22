package main.java;

public class Deadline extends Task{

    String date;
    String byTime;

    public Deadline(String name, String byTime) {
        super(name);
        this.byTime = byTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String getType() {
        return "D";
    };

    @Override
    public String toString() {
        return super.toString() + " (by: " + byTime + ")";
    }


}
