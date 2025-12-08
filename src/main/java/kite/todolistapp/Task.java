package kite.todolistapp;

import java.time.LocalDate;

public class Task {
    private String taskName;
    private LocalDate dueDate;
    private String dueTime;
    private String category;
    private String status;

    public Task(String taskName, LocalDate dueDate, String dueTime, String category, String status) {
        this.taskName = taskName;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.category = category;
        this.status = status;
    }

    public String getTaskName() {
        return taskName;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    public LocalDate getDueDate() {
        return dueDate;
    }
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    public String getDueTime() {
        return dueTime;
    }
    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "[" + status + "] " + "[" + category + "] " + taskName + " - " + "Due: " + dueDate + " " + dueTime;
    }
}
