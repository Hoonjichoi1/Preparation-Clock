package model;

import java.time.LocalTime;

// Represents today's preparation plan consisting of multiple prep tasks.
public class PrepPlan {
    private TaskToday tasks;
    private Pace pace;

    // EFFECTS : initialize TaskToday with empty task list
    public PrepPlan(TaskToday tasks) {
        this.tasks = tasks;
        this.pace = Pace.NORMAL;
    }

    public void setPace(Pace pace) {
        this.pace = pace;
    }

    // EFFECTS : return the total amount of time of incompleted tasks
    public int totalTime() {
        int total = 0;
        for (Task t : tasks.getTasks()) {
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
        double paceTime = 1.0;
        if (pace == Pace.TIGHT) {
            paceTime =  0.9;
        } else if (pace == Pace.RELAXED) {
            paceTime =  1.15;
        }
        int actualTime = (int) Math.round(totalTime()*paceTime);
        return departTime.minusMinutes(actualTime);
    }

}
