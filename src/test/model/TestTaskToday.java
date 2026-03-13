package model;

import static org.junit.Assert.assertEquals;

import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport
public class TestTaskToday {
    private Task task1;
    private Task task2;
    private Task task3;
    private Task task4;
    private TaskToday taskToday;

    @BeforeEach
    void runBefore() {
        task1 = new Task("makeup", 20);
        task2 = new Task("shower", 25);
        task3 = new Task("hair", 10);
        task4 = new Task("breakfast", 30);
        taskToday = new TaskToday();
    }

    // test addTask
    @Test
    void testAddTaskSingle() {
        taskToday.addTask(task1);

        assertEquals(task1, taskToday.getTasks().get(0));
    }

    @Test
    void testAddTaskMultiple() {
        taskToday.addTask(task1);
        taskToday.addTask(task2);
        taskToday.addTask(task3);
        taskToday.addTask(task4);

        assertEquals(4, taskToday.getTasks().size());
        assertEquals(task1, taskToday.getTasks().get(0));
        assertEquals(task2, taskToday.getTasks().get(1));
        assertEquals(task3, taskToday.getTasks().get(2));
        assertEquals(task4, taskToday.getTasks().get(3));
    }

    // test totalTime
    @Test
    void testTotalTimeEmptyTask() {
        assertEquals(0, taskToday.totalTime());
    }

    @Test
    void testTotalTimeSingleTask() {
        taskToday.addTask(task1);
        assertEquals(20, taskToday.totalTime());
    }

    @Test
    void testTotalTimeSingleCompletedTask() {
        taskToday.addTask(task1);
        assertEquals(20, taskToday.totalTime());
        taskToday.getTasks().get(0).markCompleted();
        assertEquals(0, taskToday.totalTime());
    }

    @Test
    void testTotalTimeMultipleTask() {
        taskToday.addTask(task1);
        taskToday.addTask(task2);
        taskToday.addTask(task3);
        taskToday.addTask(task4);
        assertEquals(85, taskToday.totalTime());
    }

    @Test
    void testTotalTimeAfterRemoveTask() {
        taskToday.addTask(task1);
        taskToday.addTask(task2);
        taskToday.addTask(task3);
        taskToday.addTask(task4);
        assertEquals(85, taskToday.totalTime());

        taskToday.removeTask("makeup");
        assertEquals(65, taskToday.totalTime());
    }

    // test estimateStartTime
    @Test
    void testEstimateStartTimeWithNoTask() {
        taskToday.addTask(task1);
        assertEquals(20, taskToday.totalTime());
        assertEquals(LocalTime.parse("10:00"), taskToday.estimateStartTime(LocalTime.parse("10:20")));
    }

    @Test
    void testEstimateStartTimeHourBorrow() {
        taskToday.addTask(task4);
        assertEquals(30, taskToday.totalTime());
        assertEquals(LocalTime.parse("09:50"), taskToday.estimateStartTime(LocalTime.parse("10:20")));
    }

    @Test
    void testEstimateStartTimeMinuteBorrow() {
        taskToday.addTask(task2);
        assertEquals(25, taskToday.totalTime());
        assertEquals(LocalTime.parse("10:05"), taskToday.estimateStartTime(LocalTime.parse("10:30")));
    }

    @Test
    void testEstimateStartTimeWrap() {
        taskToday.addTask(task2);
        assertEquals(25, taskToday.totalTime());
        assertEquals(LocalTime.parse("23:55"), taskToday.estimateStartTime(LocalTime.parse("00:20")));
    }

    @Test
    void testEstimateStartTime() {
        taskToday.addTask(task1);
        taskToday.addTask(task2);
        assertEquals(45, taskToday.totalTime());
        assertEquals(LocalTime.parse("09:45"), taskToday.estimateStartTime(LocalTime.parse("10:30")));
    }

    // test removeTask
    @Test
    void testRemoveTask() {
        taskToday.addTask(task1);
        taskToday.addTask(task2);
        assertEquals(2, taskToday.getTasks().size());
        assertEquals(task1, taskToday.getTasks().get(0));
        assertEquals(task2, taskToday.getTasks().get(1));

        taskToday.removeTask("makeup");
        assertEquals(1, taskToday.getTasks().size());
        assertEquals(null, taskToday.findTask("makeup"));
    }

    @Test
    void testRemoveTaskEmpty() {
        assertEquals(0, taskToday.getTasks().size());
        assertEquals(false, taskToday.removeTask("breakfast"));
    }

    @Test
    void testRemoveTaskFail() {
        taskToday.addTask(task1);
        taskToday.addTask(task2);
        assertEquals(2, taskToday.getTasks().size());

        assertEquals(false, taskToday.removeTask("breakfast"));
        assertEquals(2, taskToday.getTasks().size());
        assertEquals(null, taskToday.findTask("breakfast"));
    }

    // test findTask
    @Test
    void testFindTaskNotFound() {
        taskToday.addTask(task1);
        taskToday.addTask(task2);
        taskToday.addTask(task3);
        assertEquals(null, taskToday.findTask("breakfast"));
    }

    @Test
    void testFindTaskFirst() {
        taskToday.addTask(task1);
        taskToday.addTask(task2);
        taskToday.addTask(task3);
        taskToday.addTask(task4);
        assertEquals(task1, taskToday.findTask("makeup"));
    }

    @Test
    void testFindTaskLast() {
        taskToday.addTask(task1);
        taskToday.addTask(task2);
        taskToday.addTask(task3);
        taskToday.addTask(task4);
        assertEquals(task4, taskToday.findTask("breakfast"));
    }
}
