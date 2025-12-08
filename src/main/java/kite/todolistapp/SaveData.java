package kite.todolistapp;

import java.util.ArrayList;

public class SaveData {
    public ArrayList<Task> tasks;
    public ArrayList<String> categories;

    public SaveData(ArrayList<Task> tasks, ArrayList<String> categories) {
        this.tasks = tasks;
        this.categories = categories;
    }
}
