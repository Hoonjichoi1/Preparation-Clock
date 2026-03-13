package model;

import static org.junit.Assert.assertEquals;

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
        task1 = new Task("makeup", TaskCategory.BEAUTY, 20, false);
        task2 = new Task("shower", TaskCategory.HYGIENE, 26, false);
        task3 = new Task("hair", TaskCategory.BEAUTY, 10, false);
        task4 = new Task("breakfast", TaskCategory.FOOD, 30, false);
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
