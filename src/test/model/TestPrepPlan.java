package model;

import static org.junit.Assert.assertEquals;

import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport
public class TestPrepPlan {
    private Task task1;
    private Task task2;
    private Task task3;
    private Task task4;
    private PrepPlan prepPlan;
    private TaskToday taskToday;

    @BeforeEach
    void runBefore() {
        task1 = new Task("makeup", TaskCategory.BEAUTY, 20, false);
        task2 = new Task("shower", TaskCategory.HYGIENE, 26, false);
        task3 = new Task("hair", TaskCategory.BEAUTY, 10, false);
        task4 = new Task("breakfast", TaskCategory.FOOD, 30, false);
        taskToday = new TaskToday();
        prepPlan = new PrepPlan(taskToday);
    }

    // test totalTime
    @Test
    void testTotalTimeEmptyTask() {
        assertEquals(0, prepPlan.totalTime());
    }

    @Test
    void testTotalTimeSingleTask() {
        taskToday.addTask(task1);
        assertEquals(20, prepPlan.totalTime());
    }

    @Test
    void testTotalTimeSingleCompletedTask() {
        taskToday.addTask(task1);
        assertEquals(20, prepPlan.totalTime());
        taskToday.getTasks().get(0).markCompleted();
        assertEquals(0, prepPlan.totalTime());
    }

    @Test
    void testTotalTimeMultipleTask() {
        taskToday.addTask(task1);
        taskToday.addTask(task2);
        taskToday.addTask(task3);
        taskToday.addTask(task4);
        assertEquals(86, prepPlan.totalTime());
    }

    @Test
    void testTotalTimeAfterRemoveTask() {
        taskToday.addTask(task1);
        taskToday.addTask(task2);
        taskToday.addTask(task3);
        taskToday.addTask(task4);
        assertEquals(86, prepPlan.totalTime());

        taskToday.removeTask("makeup");
        assertEquals(66, prepPlan.totalTime());
    }

    @Test
    void testTotalTimeWithCompletedTask() {
        taskToday.addTask(task1);
        taskToday.addTask(task2);
        taskToday.addTask(task3);
        taskToday.addTask(task4);
        assertEquals(86, prepPlan.totalTime());

        task1.markCompleted();
        assertEquals(66, prepPlan.totalTime());
    }

    // test estimateStartTime
    @Test
    void testEstimateStartTimeWithNoTask() {
        taskToday.addTask(task1);
        assertEquals(20, prepPlan.totalTime());
        assertEquals(LocalTime.parse("10:00"), prepPlan.estimateStartTime(LocalTime.parse("10:20")));
    }

    @Test
    void testEstimateStartTimeHourBorrow() {
        taskToday.addTask(task4);
        assertEquals(30, prepPlan.totalTime());
        assertEquals(LocalTime.parse("09:50"), prepPlan.estimateStartTime(LocalTime.parse("10:20")));
    }

    @Test
    void testEstimateStartTimeMinuteBorrow() {
        taskToday.addTask(task2);
        assertEquals(26, prepPlan.totalTime());
        assertEquals(LocalTime.parse("10:04"), prepPlan.estimateStartTime(LocalTime.parse("10:30")));
    }

    @Test
    void testEstimateStartTimeWrap() {
        taskToday.addTask(task2);
        assertEquals(26, prepPlan.totalTime());
        assertEquals(LocalTime.parse("23:54"), prepPlan.estimateStartTime(LocalTime.parse("00:20")));
    }

    @Test
    void testEstimateStartTime() {
        taskToday.addTask(task1);
        taskToday.addTask(task2);
        assertEquals(46, prepPlan.totalTime());
        assertEquals(LocalTime.parse("09:44"), prepPlan.estimateStartTime(LocalTime.parse("10:30")));
    }

    @Test
    void testEstimateRoundUp() {
        taskToday.addTask(task2);
        prepPlan.setPace(Pace.RELAXED); // 26 * 1.15 = 29.9
        assertEquals(LocalTime.parse("10:00"), prepPlan.estimateStartTime(LocalTime.parse("10:30")));
    }

    @Test
    void testEstimateRoundDown() {
        taskToday.addTask(task2);
        prepPlan.setPace(Pace.TIGHT); // 25 * 0.9 = 23.4
        assertEquals(LocalTime.parse("10:07"), prepPlan.estimateStartTime(LocalTime.parse("10:30")));
    }

}
