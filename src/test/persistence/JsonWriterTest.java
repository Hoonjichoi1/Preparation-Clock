package persistence;

import model.Task;
import model.TaskToday;
import org.junit.jupiter.api.Test;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExcludeFromJacocoGeneratedReport
class JsonWriterTest extends JsonTest {
    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.

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
            assertEquals(0, tt.totalTime());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralTaskToday() {
        try {
            TaskToday tt = new TaskToday();
            tt.addTask(new Task("breakfast", 15));
            tt.addTask(new Task("shower", 10));
            tt.addTask(new Task("makeup", 20));
            tt.getTasks().get(1).markCompleted();

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralTaskToday.json");
            writer.open();
            writer.write(tt);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralTaskToday.json");
            tt = reader.read();
            assertEquals(3, tt.getTasks().size());
            assertEquals(35, tt.totalTime());
            checkTask("breakfast", 15, false, tt.getTasks().get(0));
            checkTask("shower", 10, true, tt.getTasks().get(1));
            checkTask("makeup", 20, false, tt.getTasks().get(2));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}