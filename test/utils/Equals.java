package utils;

import model.Task;
import org.junit.jupiter.api.Assertions;

public class Equals {
    public static void assertEqualsTask(Task expected, Task actual, String message) {
        Assertions.assertEquals(expected.getSummary(), actual.getSummary(), message + " - Summary");
        Assertions.assertEquals(expected.getStatus(), actual.getStatus(), message + " - Status");
        Assertions.assertEquals(expected.getDescription(), actual.getDescription(), message + " - Description");
        Assertions.assertEquals(expected.getId(), actual.getId(), message + " - Id");

    }
}
