package shadowbuddy.app;

import org.junit.jupiter.api.Test;
import shadowbuddy.services.ShadowException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShadowParserTest {
    @Test
    public void parse_validListCommand() throws ShadowException {
        String userInput = "list";
        ShadowCommand userCommand = ShadowParser.parse(userInput);
        assertEquals(ShadowCommand.CommandType.LIST, userCommand.commandType);
    }

    @Test
    public void parse_validMarkCommand() throws ShadowException {
        String userInput = "mark 3";
        ShadowCommand userCommand = ShadowParser.parse(userInput);

        assertEquals(ShadowCommand.CommandType.MARK, userCommand.commandType);
        assertEquals(3, userCommand.taskIndex);
    }

    @Test
    public void parse_validUnmarkCommand() throws ShadowException {
        String userInput = "unmark 4";
        ShadowCommand userCommand = ShadowParser.parse(userInput);

        assertEquals(ShadowCommand.CommandType.UNMARK, userCommand.commandType);
        assertEquals(4, userCommand.taskIndex);
    }

    @Test
    public void parse_validDeleteCommand() throws ShadowException {
        String userInput = "delete 2";
        ShadowCommand userCommand = ShadowParser.parse(userInput);

        assertEquals(ShadowCommand.CommandType.DELETE, userCommand.commandType);
        assertEquals(2, userCommand.taskIndex);
    }

    @Test
    public void parse_validTodoCommand() throws ShadowException {
        String userInput = "todo return book";
        ShadowCommand userCommand = ShadowParser.parse(userInput);

        assertEquals(ShadowCommand.CommandType.TODO, userCommand.commandType);
        assertEquals("return book", userCommand.taskDescription);
    }

    @Test
    public void parse_emptyTodo_exceptionThrown() {
        String userInput = "todo";
        try {
            ShadowCommand userCommand = ShadowParser.parse(userInput);
            assertEquals(ShadowCommand.CommandType.TODO, userCommand.commandType);
        } catch (ShadowException exception) {
            assertEquals("Invalid request! Please provide a description for your todo.\n",
                    exception.getMessage());
        }
    }

    @Test
    public void parse_validDeadlineCommand() throws ShadowException {
        String userInput = "deadline buy book /by 2/12/2025 1800";
        ShadowCommand userCommand = ShadowParser.parse(userInput);

        assertEquals(ShadowCommand.CommandType.DEADLINE, userCommand.commandType);
        assertEquals("buy book", userCommand.taskDescription);
        assertEquals("Dec 2 2025 18:00", userCommand.dueDate);
    }

    @Test
    public void parse_emptyDeadline_exceptionThrown() {
        String userInput = "deadline";
        try {
            ShadowCommand userCommand = ShadowParser.parse(userInput);
            assertEquals(ShadowCommand.CommandType.DEADLINE, userCommand.commandType);
        } catch (ShadowException exception) {
            assertEquals("Invalid request! Please provide a description for your deadline.\n",
                    exception.getMessage());
        }
    }

    @Test
    public void parse_validEventCommand() throws ShadowException {
        String userInput = "event project meeting /from 4/7/2025 1600 /to 5/7/2025 2100";
        ShadowCommand userCommand = ShadowParser.parse(userInput);

        assertEquals(ShadowCommand.CommandType.EVENT, userCommand.commandType);
        assertEquals("project meeting", userCommand.taskDescription);
        assertEquals("Jul 4 2025 16:00", userCommand.startDate);
        assertEquals("Jul 5 2025 21:00", userCommand.endDate);
    }

    @Test
    public void parse_emptyEvent_exceptionThrown() {
        String userInput = "event";
        try {
            ShadowCommand userCommand = ShadowParser.parse(userInput);
            assertEquals(ShadowCommand.CommandType.EVENT, userCommand.commandType);
        } catch (ShadowException exception) {
            assertEquals("Invalid request! Please provide a description for your event.\n",
                    exception.getMessage());
        }
    }

    @Test
    public void parse_invalidCommandType() throws ShadowException {
        String userInput = "plan";
        ShadowCommand userCommand = ShadowParser.parse(userInput);
        assertEquals(ShadowCommand.CommandType.UNKNOWN, userCommand.commandType);
    }
}