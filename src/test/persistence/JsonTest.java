package persistence;

import model.Task;
import model.TaskCategory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport
public class JsonTest {
    protected void checkTask(String name, TaskCategory category, int time, boolean optional, boolean completed, Task task) {
        assertEquals(name, task.getName());
        assertEquals(category, task.getCategory());
        assertEquals(time, task.getTime());
        assertEquals(optional, task.isOptional());
        assertEquals(completed, task.isCompleted());
    }
}