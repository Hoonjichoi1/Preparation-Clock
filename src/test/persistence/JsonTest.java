package persistence;

import model.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport
public class JsonTest {
    protected void checkTask(String name, int time, boolean status, Task task) {
        assertEquals(name, task.getName());
        assertEquals(time, task.getTime());
        assertEquals(status, task.isCompleted());
    }
}