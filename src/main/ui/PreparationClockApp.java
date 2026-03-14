package ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
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
            System.out.println("Selection not valid");
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
        System.out.println("--------------------");
        String name = validTaskName();
        TaskCategory category = validCategory();
        int time = validMinutes();
        boolean optional = validOptional();

        Task task = new Task(name, category, time, optional);
        tasks.addTask(task);
        System.out.println("Added");
        System.out.println("--------------------");
    }

    // MODIFIES: this
    // EFFECTS : conducts a task status change as complete
    private void doMarkTask() {
        System.out.println("--------------------");
        System.out.println("Task name: ");
        String name = input.next();

        Task t = tasks.findTask(name);
        if (t != null) {
            tasks.findTask(name).markCompleted();
            System.out.println("Marked.");
        } else {
            System.out.println("Not found");
            System.out.println("Remaining total time is " + prepPlan.totalTime());
        }
        System.out.println("--------------------");
    }

    // MODIFIES: this
    // EFFECTS : conducts a task remove
    private void doRemoveTask() {
        System.out.println("--------------------");
        System.out.println("Task name: ");
        String name = input.next();

        if (tasks.removeTask(name)) {
            System.out.println("Removed.");
            System.out.println("Remaining total time is " + prepPlan.totalTime());
        } else {
            System.out.println("Not found.");
            System.out.println("Remaining total time is " + prepPlan.totalTime());
        }
        System.out.println("--------------------");

    }

    // MODIFIES: this
    // EFFECTS : conducts a the list of task view
    private void doViewTasks() {
        System.out.println("--------------------");
        List<Task> viewTasks = tasks.getTasks();

        System.out.println("Today's Tasks");
        for (Task t : viewTasks) {
            System.out.println(t.getCategory() + " - " + t.getName() + " : " + t.getTime() + " minutes"
                    + " / completed : " + t.isCompleted());
        }
        System.out.println("--------------------");
    }

    // MODIFIES: this
    // EFFECTS : estimate the starting time to prepare
    private void doShowStartTime() {
        System.out.println("--------------------");
        LocalTime dep = validDepartTime();

        System.out.println("Total time for tasks : " + prepPlan.totalTime());
        validPace();
        LocalTime start = prepPlan.estimateStartTime(dep);
        System.out.println("You need to start prepareing at: " + start);
        System.out.println("--------------------");
    }

    // EFFECTS: saves the workroom to file
    protected void saveTaskToday() {
        System.out.println("--------------------");
        try {
            jsonWriter.open();
            jsonWriter.write(tasks);
            jsonWriter.close();
            System.out.println("Saved to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
        System.out.println("--------------------");
    }

    // MODIFIES: this
    // EFFECTS: loads workroom from file
    protected void loadTaskToday() {
        System.out.println("--------------------");
        try {
            tasks = jsonReader.read();
            prepPlan = new PrepPlan(tasks);
            System.out.println("Loaded from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
        System.out.println("--------------------");
    }

    // helpers
    private String validTaskName() {
        String name = "";
        while (true) {
            System.out.print("Task name: ");
            name = input.next().trim();
            if (name.isEmpty()) {
                System.out.println("Nothing entered. Try again");
            } else if (tasks.findTask(name) == null) {
                return name;
            } else {
                System.out.println("Duplicate name exists. Try again");
            }
        }

    }

    private TaskCategory validCategory() {
        TaskCategory category = null;
        while (true) {
            System.out.println("Category(case-insensitive): ");
            for (TaskCategory c : TaskCategory.values()) {
                System.out.println("- " + c);
            }
            String categoryStr = input.next().toUpperCase();

            try {
                category = TaskCategory.valueOf(categoryStr);
                return category;
            } catch (IllegalArgumentException e) {
                System.out.println("Wrong category entered. Try again");
            }
        }

    }

    private int validMinutes() {
        int time = 0;
        while (true) {
            System.out.print("Minutes: ");
            try {
                time = input.nextInt();
                if (time <= 0) {
                    System.out.println("Minutes must be positive. Try again");
                    continue;
                }
                return time;
            } catch (IllegalArgumentException e) {
                System.out.println("Wrong input for minutes. Try again");
                input.next(); // clean up
            }
        }

    }

    private boolean validOptional() {
        boolean optional = false;
        while (true) {
            System.out.print("Is it optional task?(true/false):");
            try {
                optional = input.nextBoolean();
                return optional;
            } catch (InputMismatchException e) {
                System.out.println("Wrong input for optional selection. Try again");
                input.next(); // clean up the wrong token
            }
        }

    }

    private LocalTime validDepartTime() {
        String time = "";
        LocalTime dep = null;

        while (true) {
            System.out.print("Enter your departure time (HH:MM) : ");
            time = input.next();
            try {
                dep = LocalTime.parse(time);
                return dep;
            } catch (DateTimeParseException e) {
                System.out.println("Wrong format of time has entered. It must be HH:MM");
            }
        }
    }

    private void validPace() {
        while (true) {
            System.out.println("Select the pace today");
            System.out.println("(case-insensitive)");

            for (Pace p : Pace.values()) {
                System.out.println("- " + p);
            }
            String paceStr = input.next().toUpperCase();

            try {
                Pace pace = Pace.valueOf(paceStr);
                prepPlan.setPace(pace);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Wrong pace entered");
            }

        }
    }

}
