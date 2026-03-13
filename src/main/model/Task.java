package model;

import org.json.JSONObject;

import persistence.Writable;

// Represents a single preparation task with a name and base time.
public class Task implements Writable {
    private String name;
    private int time;
    private TaskCategory category;
    private boolean completed;
    private boolean optional;

    // REQUIRES : time > 0
    // EFFECTS: initialize Task object with given name, time and incompleted status
    public Task(String name, TaskCategory category, int time, Boolean optional) {
        this.name = name;
        this.category = category;
        this.time = time;
        this.optional = optional;
        completed = false;

    }

    public String getName() {
        return name;
    }

    public int getTime() {
        return time;
    }

    public TaskCategory getCategory() {
        return category;
    }

    public boolean isOptional() {
        return optional;
    }

    // MODIFIES : this
    // EFFECTS : change the optional status to true
    public void markOptional() {
        optional = true;
    }

    public boolean isCompleted() {
        return completed;
    }

    // MODIFIES : this
    // EFFECTS : change the complete status to true
    public void markCompleted() {
        completed = true;
    }

    // MODIFIES : this
    // EFFECTS : change the complete status to false
    public void markIncompleted() {
        completed = false;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("category", category);
        json.put("time", time);
        json.put("optional", optional);
        json.put("completed", completed);
        return json;
    }
}
