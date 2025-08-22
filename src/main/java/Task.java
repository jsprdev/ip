package main.java;

public class Task {
    String name;
    boolean isCompleted = false;

    public Task(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @Override
    public String toString(){
        return (isCompleted ? "[X]" : "[ ]") + " " + name;
    }

    public String getType() {
        return "-";
    };

    public String getCompletion() {
        if (isCompleted) {
            return "X";
        }
        return " ";
    }

}
