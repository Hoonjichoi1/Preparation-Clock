package model;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport
public class TestTask {
    private Task task;

    @BeforeEach 
    void setup() {
        task = new Task("makeup", 30);
    }

    @Test
    void testConstructor() {
        assertEquals("makeup", task.getName());
        assertEquals(30, task.getTime());
    }

    @Test
    void testMarkCompleted() {
        assertEquals(false, task.isCompleted());
        task.markCompleted();
        assertEquals(true, task.isCompleted());
    }

    @Test
    void testMarkCompletedAlready() {
        task.markCompleted();
        assertEquals(true, task.isCompleted());
        task.markCompleted();
        assertEquals(true, task.isCompleted());
    }

    @Test
    void testMarkIncompleted() {
        task.markCompleted();
        assertEquals(true, task.isCompleted());
        task.markIncompleted();
        assertEquals(false, task.isCompleted());
    }

    @Test
    void testMarkIncompletedAlready() {
        assertEquals(false, task.isCompleted());
        task.markIncompleted();
        assertEquals(false, task.isCompleted());
    }
}
