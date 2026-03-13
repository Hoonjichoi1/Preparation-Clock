package persistence;

import model.Task;
import model.TaskCategory;
import model.TaskToday;
import org.junit.jupiter.api.Test;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExcludeFromJacocoGeneratedReport
class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyTaskToday() {
        try {
            TaskToday tt = new TaskToday();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyTaskToday.json");
            writer.open();
            writer.write(tt);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyTaskToday.json");
            tt = reader.read();
            assertEquals(0, tt.getTasks().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralTaskToday() {
        try {
            TaskToday tt = new TaskToday();
            tt.addTask(new Task("breakfast", TaskCategory.FOOD, 15, false));
            tt.addTask(new Task("shower", TaskCategory.HYGIENE, 10, false));
            tt.addTask(new Task("makeup", TaskCategory.BEAUTY, 20, false));
            tt.getTasks().get(1).markCompleted();

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralTaskToday.json");
            writer.open();
            writer.write(tt);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralTaskToday.json");
            tt = reader.read();
            assertEquals(3, tt.getTasks().size());
            checkTask("breakfast", TaskCategory.FOOD, 15, false, false, tt.getTasks().get(0));
            checkTask("shower", TaskCategory.HYGIENE, 10, false, true, tt.getTasks().get(1));
            checkTask("makeup", TaskCategory.BEAUTY, 20, false, false, tt.getTasks().get(2));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}