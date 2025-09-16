package shadowbuddy.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import shadowbuddy.services.ShadowException;

public class ShadowParserTest {
    @Test
    public void parse_validListCommand() throws ShadowException {
        ShadowCommand userCommand = ShadowParser.parse("list");
        assertEquals(ShadowCommand.CommandType.LIST, userCommand.commandType);
    }

    @Test
    public void parse_validMarkCommand() throws ShadowException {
        ShadowCommand userCommand = ShadowParser.parse("mark 3");
        assertEquals(ShadowCommand.CommandType.MARK, userCommand.commandType);
        assertEquals(3, userCommand.taskIndex);
    }

    @Test
    public void parse_validUnmarkCommand() throws ShadowException {
        ShadowCommand userCommand = ShadowParser.parse("unmark 4");
        assertEquals(ShadowCommand.CommandType.UNMARK, userCommand.commandType);
        assertEquals(4, userCommand.taskIndex);
    }

    @Test
    public void parse_validDeleteCommand() throws ShadowException {
        ShadowCommand userCommand = ShadowParser.parse("delete 2");
        assertEquals(ShadowCommand.CommandType.DELETE, userCommand.commandType);
        assertEquals(2, userCommand.taskIndex);
    }

    @Test
    public void parse_invalidTaskIndex_exceptionThrown() {
        try {
            assertEquals(ShadowCommand.CommandType.MARK, ShadowParser.parse("mark book").commandType);
            fail();
        } catch (ShadowException exception) {
            assertEquals("Invalid task index! Please provide a numeric index for your request!\n",
                    exception.getMessage());
        }

        try {
            assertEquals(ShadowCommand.CommandType.UNMARK, ShadowParser.parse("unmark book").commandType);
            fail();
        } catch (ShadowException exception) {
            assertEquals("Invalid task index! Please provide a numeric index for your request!\n",
                    exception.getMessage());
        }

        try {
            assertEquals(ShadowCommand.CommandType.DELETE, ShadowParser.parse("delete book").commandType);
            fail();
        } catch (ShadowException exception) {
            assertEquals("Invalid task index! Please provide a numeric index for your request!\n",
                    exception.getMessage());
        }
    }

    @Test
    public void parse_validFindCommand() throws ShadowException {
        ShadowCommand userCommand = ShadowParser.parse("find book");
        assertEquals(ShadowCommand.CommandType.FIND, userCommand.commandType);
        assertEquals("book", userCommand.taskDescription);
    }

    @Test
    public void parse_emptyFind_exceptionThrown() {
        try {
            assertEquals(ShadowCommand.CommandType.FIND, ShadowParser.parse("find").commandType);
            fail();
        } catch (ShadowException exception) {
            assertEquals("Invalid request! Please provide a keyword for your find.\n", exception.getMessage());
        }
    }

    @Test
    public void parse_findMultipleKeywords_exceptionThrown() {
        try {
            assertEquals(ShadowCommand.CommandType.FIND, ShadowParser.parse("find return book").commandType);
            fail();
        } catch (ShadowException exception) {
            assertEquals("Invalid request! Please provide only ONE keyword for your find.\n", exception.getMessage());
        }
    }

    @Test
    public void parse_validTodoCommand() throws ShadowException {
        ShadowCommand userCommand = ShadowParser.parse("todo return book");
        assertEquals(ShadowCommand.CommandType.TODO, userCommand.commandType);
        assertEquals("return book", userCommand.taskDescription);
    }

    @Test
    public void parse_emptyTodo_exceptionThrown() {
        try {
            assertEquals(ShadowCommand.CommandType.TODO, ShadowParser.parse("todo").commandType);
            fail();
        } catch (ShadowException exception) {
            assertEquals("Invalid request! Please provide a description for your todo.\n", exception.getMessage());
        }
    }

    @Test
    public void parse_validDeadlineCommand() throws ShadowException {
        ShadowCommand userCommand = ShadowParser.parse("deadline buy book /by 2/12/2025 1800");
        assertEquals(ShadowCommand.CommandType.DEADLINE, userCommand.commandType);
        assertEquals("buy book", userCommand.taskDescription);
        assertEquals("Dec 2 2025 18:00", userCommand.dueDate);
    }

    @Test
    public void parse_emptyDeadline_exceptionThrown() {
        try {
            assertEquals(ShadowCommand.CommandType.DEADLINE, ShadowParser.parse("deadline").commandType);
            fail();
        } catch (ShadowException exception) {
            assertEquals("Invalid request! Please provide a description for your deadline.\n", exception.getMessage());
        }
    }

    @Test
    public void parse_invalidDeadlineFormat_exceptionThrown() {
        try {
            assertEquals(ShadowCommand.CommandType.DEADLINE,
                    ShadowParser.parse("deadline buy book by 2/12/2025 1800").commandType);
            fail();
        } catch (ShadowException exception) {
            assertEquals("Invalid format! Please use: deadline DESCRIPTION /by d/M/yyyy HHmm.\n",
                    exception.getMessage());
        }
    }

    @Test
    public void parse_duplicateDeadlineMarkers_exceptionThrown() {
        try {
            assertEquals(ShadowCommand.CommandType.DEADLINE,
                    ShadowParser.parse("deadline buy book /by 2/12/2025 /by 1800").commandType);
            fail();
        } catch (ShadowException exception) {
            assertEquals("Duplicate '/by' found! Please use: deadline DESCRIPTION /by d/M/yyyy HHmm.\n",
                    exception.getMessage());
        }
    }

    @Test
    public void parse_missingDeadlineDueDate_exceptionThrown() {
        try {
            assertEquals(ShadowCommand.CommandType.DEADLINE,
                    ShadowParser.parse("deadline buy book /by").commandType);
            fail();
        } catch (ShadowException exception) {
            assertEquals("Missing due date! Please use: deadline DESCRIPTION /by d/M/yyyy HHmm.\n",
                    exception.getMessage());
        }
    }

    @Test
    public void parse_invalidDeadlineDueDate_exceptionThrown() {
        try {
            assertEquals(ShadowCommand.CommandType.DEADLINE,
                    ShadowParser.parse("deadline buy book /by 2/12/2025 6pm").commandType);
            fail();
        } catch (ShadowException exception) {
            assertEquals("Invalid due date! Please use: deadline DESCRIPTION /by d/M/yyyy HHmm.\n",
                    exception.getMessage());
        }
    }

    @Test
    public void parse_validEventCommand() throws ShadowException {
        ShadowCommand userCommand = ShadowParser.parse("event meeting /from 4/7/2025 1600 /to 5/7/2025 2100");
        assertEquals(ShadowCommand.CommandType.EVENT, userCommand.commandType);
        assertEquals("meeting", userCommand.taskDescription);
        assertEquals("Jul 4 2025 16:00", userCommand.startDate);
        assertEquals("Jul 5 2025 21:00", userCommand.endDate);
    }

    @Test
    public void parse_emptyEvent_exceptionThrown() {
        try {
            assertEquals(ShadowCommand.CommandType.EVENT, ShadowParser.parse("event").commandType);
            fail();
        } catch (ShadowException exception) {
            assertEquals("Invalid request! Please provide a description for your event.\n",
                    exception.getMessage());
        }
    }

    @Test
    public void parse_invalidEventFormat_exceptionThrown() {
        try {
            assertEquals(ShadowCommand.CommandType.EVENT,
                    ShadowParser.parse("event meeting from 4/7/2025 1600 to 5/7/2025 2100").commandType);
            fail();
        } catch (ShadowException exception) {
            assertEquals("Invalid format! "
                    + "Please use: event DESCRIPTION /from d/M/yyyy HHmm /to d/M/yyyy HHmm.\n", exception.getMessage());
        }
    }

    @Test
    public void parse_duplicateEventMarkers_exceptionThrown() {
        try {
            assertEquals(ShadowCommand.CommandType.EVENT,
                    ShadowParser.parse("event meeting /from 4/7/2025 /from 1600 /to 5/7/2025 2100").commandType);
            fail();
        } catch (ShadowException exception) {
            assertEquals("Duplicate '/from' found! "
                    + "Please use: event DESCRIPTION /from d/M/yyyy HHmm /to d/M/yyyy HHmm.\n", exception.getMessage());
        }

        try {
            assertEquals(ShadowCommand.CommandType.EVENT,
                    ShadowParser.parse("event meeting /from 4/7/2025 1600 /to 5/7/2025 /to 2100").commandType);
            fail();
        } catch (ShadowException exception) {
            assertEquals("Duplicate '/to' found! "
                    + "Please use: event DESCRIPTION /from d/M/yyyy HHmm /to d/M/yyyy HHmm.\n", exception.getMessage());
        }
    }

    @Test
    public void parse_missingEventStartDate_exceptionThrown() {
        try {
            assertEquals(ShadowCommand.CommandType.EVENT,
                    ShadowParser.parse("event meeting /from /to 5/7/2025 2100").commandType);
            fail();
        } catch (ShadowException exception) {
            assertEquals("Missing start date! "
                    + "Please use: event DESCRIPTION /from d/M/yyyy HHmm /to d/M/yyyy HHmm.\n", exception.getMessage());
        }
    }

    @Test
    public void parse_missingEventEndDate_exceptionThrown() {
        try {
            assertEquals(ShadowCommand.CommandType.EVENT,
                    ShadowParser.parse("event meeting /from 4/7/2025 1600 /to").commandType);
            fail();
        } catch (ShadowException exception) {
            assertEquals("Missing end date! "
                    + "Please use: event DESCRIPTION /from d/M/yyyy HHmm /to d/M/yyyy HHmm.\n", exception.getMessage());
        }
    }

    @Test
    public void parse_invalidChronologicalOrder_exceptionThrown() {
        try {
            assertEquals(ShadowCommand.CommandType.EVENT,
                    ShadowParser.parse("event meeting /from 4/7/2025 2100 /to 4/7/2025 1600").commandType);
            fail();
        } catch (ShadowException exception) {
            assertEquals("Invalid event dates! Start date must be before end date!\n", exception.getMessage());
        }
    }

    @Test
    public void parse_invalidEventDates_exceptionThrown() {
        try {
            assertEquals(ShadowCommand.CommandType.EVENT,
                    ShadowParser.parse("event meeting /from 4/7/2025 4pm /to 7 July 2025 2100").commandType);
            fail();
        } catch (ShadowException exception) {
            assertEquals("Invalid start or end date! "
                    + "Please use: event DESCRIPTION /from d/M/yyyy HHmm /to d/M/yyyy HHmm.\n", exception.getMessage());
        }
    }

    @Test
    public void parse_emptyCommand_exceptionThrown() {
        try {
            assertEquals(ShadowCommand.CommandType.UNKNOWN, ShadowParser.parse(" ").commandType);
            fail();
        } catch (ShadowException exception) {
            assertEquals("Empty request! Try one of these commands: list, mark, unmark, todo, "
                    + "delete, event, or deadline, and I'll handle it for you.\n", exception.getMessage());
        }
    }

    @Test
    public void parse_invalidCommandType() throws ShadowException {
        ShadowCommand userCommand = ShadowParser.parse("plan");
        assertEquals(ShadowCommand.CommandType.UNKNOWN, userCommand.commandType);
    }
}
