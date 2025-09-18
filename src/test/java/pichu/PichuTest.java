package pichu;  //same package as the class being tested

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import pichu.task.Task;
import pichu.parser.Parser;

public class PichuTest {

    // Task class tests - testing tag extraction and manipulation
    @Test
    public void testTaskTagExtraction_singleTag() {
        Task task = new Task("Buy groceries #urgent");
        assertEquals("Buy groceries", task.getName());
        assertTrue(task.hasTag("urgent"));
        assertEquals(1, task.getTags().size());
    }

    @Test
    public void testTaskTagExtraction_multipleTags() {
        Task task = new Task("Complete assignment #urgent #school #cs2103t");
        assertEquals("Complete assignment", task.getName());
        assertTrue(task.hasTag("urgent"));
        assertTrue(task.hasTag("school"));
        assertTrue(task.hasTag("cs2103t"));
        assertEquals(3, task.getTags().size());
    }

    @Test
    public void testTaskTagExtraction_noTags() {
        Task task = new Task("Simple task without tags");
        assertEquals("Simple task without tags", task.getName());
        assertEquals(0, task.getTags().size());
    }

    @Test
    public void testTaskTagExtraction_duplicateTags() {
        Task task = new Task("Task with #duplicate #duplicate tags");
        assertEquals("Task with tags", task.getName());
        assertTrue(task.hasTag("duplicate"));
        assertEquals(1, task.getTags().size()); // Should only store unique tags
    }

    @Test
    public void testTaskTagManipulation() {
        Task task = new Task("Test task #initial");

        // Test adding new tag
        task.addTag("new");
        assertTrue(task.hasTag("new"));
        assertTrue(task.hasTag("initial"));

        // Test adding duplicate tag (should not increase count)
        int initialSize = task.getTags().size();
        task.addTag("initial");
        assertEquals(initialSize, task.getTags().size());

        // Test removing tag
        task.removeTag("initial");
        assertFalse(task.hasTag("initial"));
        assertTrue(task.hasTag("new"));
    }

    @Test
    public void testTaskGetNameWithTags() {
        Task task = new Task("Study for exam #urgent #school");
        String nameWithTags = task.getNameWithTags();
        assertTrue(nameWithTags.contains("Study for exam"));
        assertTrue(nameWithTags.contains("#urgent"));
        assertTrue(nameWithTags.contains("#school"));
    }

    // Parser class tests - testing command type recognition
    @Test
    public void testParserGetCommandType_basicCommands() {
        assertEquals(Parser.CommandType.BYE, Parser.getCommandType("bye"));
        assertEquals(Parser.CommandType.LIST, Parser.getCommandType("list"));
        assertEquals(Parser.CommandType.TODO, Parser.getCommandType("todo buy milk"));
        assertEquals(Parser.CommandType.DEADLINE, Parser.getCommandType("deadline submit report /by 2023-12-01"));
        assertEquals(Parser.CommandType.EVENT, Parser.getCommandType("event team meeting /from 2pm /to 4pm"));
    }

    @Test
    public void testParserGetCommandType_caseInsensitive() {
        assertEquals(Parser.CommandType.BYE, Parser.getCommandType("BYE"));
        assertEquals(Parser.CommandType.LIST, Parser.getCommandType("LIST"));
        assertEquals(Parser.CommandType.TODO, Parser.getCommandType("TODO task"));
        assertEquals(Parser.CommandType.MARK, Parser.getCommandType("MARK 1"));
        assertEquals(Parser.CommandType.UNMARK, Parser.getCommandType("UNMARK 2"));
    }

    @Test
    public void testParserGetCommandType_withWhitespace() {
        assertEquals(Parser.CommandType.BYE, Parser.getCommandType("  bye  "));
        assertEquals(Parser.CommandType.LIST, Parser.getCommandType("\tlist\t"));
        assertEquals(Parser.CommandType.TODO, Parser.getCommandType("  todo   task description  "));
    }

    @Test
    public void testParserGetCommandType_invalidInputs() {
        assertEquals(Parser.CommandType.UNKNOWN, Parser.getCommandType(null));
        assertEquals(Parser.CommandType.UNKNOWN, Parser.getCommandType(""));
        assertEquals(Parser.CommandType.UNKNOWN, Parser.getCommandType("   "));
        assertEquals(Parser.CommandType.UNKNOWN, Parser.getCommandType("invalid command"));
        assertEquals(Parser.CommandType.UNKNOWN, Parser.getCommandType("randomtext"));
    }

    @Test
    public void testParserGetCommandType_partialMatches() {
        assertEquals(Parser.CommandType.UNKNOWN, Parser.getCommandType("by")); // not "bye"
        assertEquals(Parser.CommandType.UNKNOWN, Parser.getCommandType("lis")); // not "list"
    }

    // Parser index parsing tests
    @Test
    public void testParserParseIndex_validInputs() {
        assertEquals(1, Parser.parseIndex("mark 1"));
        assertEquals(5, Parser.parseIndex("unmark 5"));
        assertEquals(10, Parser.parseIndex("delete 10"));
    }

    @Test
    public void testParserParseIndex_invalidInputs() {
        // Test missing index
        assertThrows(NumberFormatException.class, () -> Parser.parseIndex("mark"));

        // Test non-numeric index
        assertThrows(NumberFormatException.class, () -> Parser.parseIndex("mark abc"));

        // Test empty string after command
        assertThrows(NumberFormatException.class, () -> Parser.parseIndex("mark "));

        // Test negative numbers (parseInt will work, returning -1)
        assertEquals(-1, Parser.parseIndex("mark -1"));

        // Test zero
        assertEquals(0, Parser.parseIndex("mark 0"));
    }
}
