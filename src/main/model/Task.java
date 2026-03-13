package model;

import org.json.JSONObject;

import persistence.Writable;

// Represents a single preparation task with a name and base time.
public class Task implements Writable {
    private String name;
    private int time;
    private boolean status;

    // REQUIRES : time > 0
    // EFFECTS: initialize Task object with given name, time and incompleted status
    public Task(String name, int time) {
        this.name = name;
        this.time = time;
        status = false;
    }

    public String getName() {
        return name;
    }

    public int getTime() {
        return time;
    }

    public boolean isCompleted() {
        return status;
    }

    // MODIFIES : this
    // EFFECTS : change the complete status to true
    public void markCompleted() {
        status = true;
    }

    // MODIFIES : this
    // EFFECTS : change the complete status to false
    public void markIncompleted() {
        status = false;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("time", time);
        json.put("status", status);
        return json;
    }
}
