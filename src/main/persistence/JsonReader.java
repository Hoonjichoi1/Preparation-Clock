package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import model.Task;
import model.TaskToday;

import org.json.*;

// Represents a reader that reads TaskToday from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads TaskToday from file and returns it;
    // throws IOException if an error occurs reading data from file
    public TaskToday read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseTaskToday(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }
        return contentBuilder.toString();
    }

    // EFFECTS: parses TaskToday from JSON object and returns it
    private TaskToday parseTaskToday(JSONObject jsonObject) {
        TaskToday tt = new TaskToday();
        addTasks(tt, jsonObject);
        return tt;
    }

    // MODIFIES: TaskToday
    // EFFECTS: parses tasks from JSON object and adds them to TaskToday
    private void addTasks(TaskToday tt, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("tasks");
        for (Object json : jsonArray) {
            JSONObject nextTask = (JSONObject) json;
            // consider the json as JSONObject (e.g. {"name":"study","time":60})
            // since json is just object, not JSONObject
            addTask(tt, nextTask);
        }
    }

    // MODIFIES: TaskToday
    // EFFECTS: parses task from JSON object and adds it to TaskToday
    private void addTask(TaskToday tt, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        int time = jsonObject.getInt("time");
        boolean status = jsonObject.getBoolean("status");
        Task task = new Task(name, time);
        if (status) {
            task.markCompleted();
        } else {
            task.markIncompleted();
        }
        
        tt.addTask(task);
    }
}