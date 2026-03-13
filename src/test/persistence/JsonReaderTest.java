package persistence;

import org.junit.jupiter.api.Test;

import ca.ubc.cs.ExcludeFromJacocoGeneratedReport;
import model.TaskCategory;
import model.TaskToday;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExcludeFromJacocoGeneratedReport
class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            TaskToday tt = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyTaskToday() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyTaskToday.json");
        try {
            TaskToday tt = reader.read();
            assertEquals(0, tt.getTasks().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralTaskToday() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralTaskToday.json");
        try {
            TaskToday tt = reader.read();
            assertEquals(3, tt.getTasks().size());
            checkTask("shower", TaskCategory.HYGIENE, 10, false, true, tt.getTasks().get(0));
            checkTask("breakfast", TaskCategory.FOOD, 15, false, false, tt.getTasks().get(1));
            checkTask("makeup", TaskCategory.BEAUTY, 20, false, false, tt.getTasks().get(2));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}