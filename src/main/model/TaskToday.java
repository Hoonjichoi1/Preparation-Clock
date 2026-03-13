package model;

import java.time.LocalTime;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import persistence.Writable;

// Represents today's preparation plan consisting of multiple prep tasks.
public class TaskToday implements Writable {
    private ArrayList<Task> tasks;

    // EFFECTS : initialize TaskToday with empty task list
    public TaskToday() {
        tasks = new ArrayList<Task>();
    }

    // REQUIRES : no duplicated name (case sensitive)
    // EFFECTS : add a task to tasks list
    public void addTask(Task task) {
        tasks.add(task);
    }

    // EFFECTS : return the list of tasks user has set in the list
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    // EFFECTS : return the total amount of time of incompleted tasks
    public int totalTime() {
        int total = 0;
        for (Task t : tasks) {
            if (!t.isCompleted()) {
                total += t.getTime();
            }
        }
        return total;
    }

    // REQUIRES : departTime must be "HH:MM" form
    // EFFECTS : return the time when we need to start getting ready for departing
    // at time
    public LocalTime estimateStartTime(LocalTime departTime) {
        return departTime.minusMinutes(totalTime());
    }

    // MODIFIES : tasks
    // EFFECTS : remove the task that matches with the given name from the tasks
    // if it finds and removes the task, return true
    // if it can't find the task in the list or if the list is empty, return false
    // (case sensitive)
    public boolean removeTask(String name) {
        Task removable = findTask(name);
        if (removable == null) {
            return false;
        } else {
            tasks.remove(removable);
            return true;
        }
    }

    // REQUIRES : tasks.size() > 0
    // EFFECTS : find the given name of the task and return it
    // if can't find it, return null (case sensitive)
    public Task findTask(String name) {
        for (Task t : tasks) {
            if (t.getName().equals(name)) {
                return t;
            }
        }
        return null;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("tasks", tasksToJson());
        json.put("total time", totalTime());
        return json;
    }

    // EFFECTS: returns tasks in this tasktoday as a JSON array
    private JSONArray tasksToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Task t : tasks) {
            jsonArray.put(t.toJson());
        }

        return jsonArray;
    }
}
