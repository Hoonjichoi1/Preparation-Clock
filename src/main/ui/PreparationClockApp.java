package ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;
import persistence.JsonReader;
import persistence.JsonWriter;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;
import model.Task;
import model.TaskCategory;
import model.TaskToday;
import model.Pace;
import model.PrepPlan;

// Preparation Clock application
@ExcludeFromJacocoGeneratedReport
public class PreparationClockApp {
    private static final String JSON_STORE = "./data/TaskToday.json";
    private PrepPlan prepPlan;
    private TaskToday tasks;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    public PreparationClockApp() throws FileNotFoundException {
        input = new Scanner(System.in);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runPreparationClockApp();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    public void runPreparationClockApp() {
        boolean keepGoing = true;
        String command = null;
        input = new Scanner(System.in);

        init();

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nGoodbye!");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("a")) {
            doAddTask();
        } else if (command.equals("m")) {
            doMarkTask();
        } else if (command.equals("r")) {
            doRemoveTask();
        } else if (command.equals("v")) {
            doViewTasks();
        } else if (command.equals("t")) {
            doShowStartTime();
        } else if (command.equals("s")) {
            saveTaskToday();
        } else if (command.equals("l")) {
            loadTaskToday();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes tasks
    private void init() {
        tasks = new TaskToday();
        prepPlan = new PrepPlan(tasks);
        input = new Scanner(System.in);
        input.useDelimiter("\r?\n|\r");
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\ta -> Add Task");
        System.out.println("\tm -> Mark a Completed Task");
        System.out.println("\tt -> Estimate Start Time");
        System.out.println("\tr -> Remove Task");
        System.out.println("\tv -> View Tasks");
        System.out.println("\ts -> Save Task List to File");
        System.out.println("\tl -> Load Task List from File");
        System.out.println("\tq -> Quit");
    }

    // MODIFIES: this
    // EFFECTS : conducts a task addition
    private void doAddTask() {
        System.out.print("Task name: ");
        String name = input.next();

        for (TaskCategory c : TaskCategory.values()) {
            System.out.println("- " + c);
        }
        System.out.print("(not case-sensitive)");
        System.out.print("Catergory: ");
        String categoryStr = input.next().toUpperCase();
        TaskCategory category = TaskCategory.valueOf(categoryStr);

        System.out.print("Minutes: ");
        int time = input.nextInt();

        System.out.print("Is it optional task? (true/false)");
        boolean optional = input.nextBoolean();

        Task task = new Task(name, category, time, optional);
        tasks.addTask(task);

        System.out.println("Added.");
    }

    // MODIFIES: this
    // EFFECTS : conducts a task status change as complete
    private void doMarkTask() {
        System.out.print("Task name: ");
        String name = input.next();

        Task t = tasks.findTask(name);
        if (t != null) {
            tasks.findTask(name).markCompleted();
            System.out.println("Marked.");
        } else {
            System.out.println("Not found");
            System.out.println("Remaining total time is " + prepPlan.totalTime());
        }
    }

    // MODIFIES: this
    // EFFECTS : conducts a task remove
    private void doRemoveTask() {
        System.out.print("Task name: ");
        String name = input.next();

        if (tasks.removeTask(name)) {
            System.out.println("Removed.");
            System.out.println("Remaining total time is " + prepPlan.totalTime());
        } else {
            System.out.println("Not found.");
            System.out.println("Remaining total time is " + prepPlan.totalTime());
        }

    }

    // MODIFIES: this
    // EFFECTS : conducts a the list of task view
    private void doViewTasks() {
        List<Task> viewTasks = tasks.getTasks();

        System.out.println("Today's Tasks");
        for (Task t : viewTasks) {
            System.out.println(t.getCategory() + " - " + t.getName() + " : " + t.getTime() + " minutes"
                    + " / completed : " + t.isCompleted());
        }

    }

    // MODIFIES: this
    // EFFECTS : estimate the starting time to prepare
    private void doShowStartTime() {
        System.out.print("Enter your departure time (HH:MM) : ");
        String time = input.next();
        LocalTime dep = LocalTime.parse(time);

        System.out.println("Total time for tasks : " + prepPlan.totalTime());

        System.out.print("Select the pace today");
        System.out.print("(not case-sensitive)");
        for (Pace p : Pace.values()) {
            System.out.println("- " + p);
        }
        String paceStr = input.next().toUpperCase();
        Pace pace = Pace.valueOf(paceStr);
        prepPlan.setPace(pace);

        LocalTime start = prepPlan.estimateStartTime(dep);
        System.out.println("You need to start prepareing at: " + start);

    }

    // EFFECTS: saves the workroom to file
    protected void saveTaskToday() {
        try {
            jsonWriter.open();
            jsonWriter.write(tasks);
            jsonWriter.close();
            System.out.println("Saved to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads workroom from file
    protected void loadTaskToday() {
        try {
            tasks = jsonReader.read();
            prepPlan = new PrepPlan(tasks);
            System.out.println("Loaded from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

}
