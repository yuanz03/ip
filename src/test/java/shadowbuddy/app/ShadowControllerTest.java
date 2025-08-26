package shadowbuddy.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import shadowbuddy.services.ShadowException;

public class ShadowControllerTest {
    private static final ShadowCommand LIST_COMMAND = new ShadowCommand(ShadowCommand.CommandType.LIST);
    private static final ShadowCommand MARK_COMMAND = new ShadowCommand(ShadowCommand.CommandType.MARK, 1);
    private static final ShadowCommand UNMARK_COMMAND = new ShadowCommand(ShadowCommand.CommandType.UNMARK, 1);
    private static final ShadowCommand DELETE_COMMAND = new ShadowCommand(ShadowCommand.CommandType.DELETE, 1);
    private static final ShadowCommand TODO_COMMAND = new ShadowCommand(ShadowCommand.CommandType.TODO,
            "borrow book");
    private static final ShadowCommand DEADLINE_COMMAND = new ShadowCommand(ShadowCommand.CommandType.DEADLINE,
            "return book", "Dec 2 2025 18:00");
    private static final ShadowCommand EVENT_COMMAND = new ShadowCommand(ShadowCommand.CommandType.EVENT,
            "project meeting", "Jul 4 2025 16:00", "Jul 5 2025 20:00");
    private static final ShadowCommand UNKNOWN_COMMAND = new ShadowCommand(ShadowCommand.CommandType.UNKNOWN);

    @Test
    public void execute_todoTaskCreation() throws ShadowException {
        ShadowUi dummyUi = new ShadowUi();
        ShadowController dummyController = new ShadowController(null);

        dummyController.executeCommand(TODO_COMMAND, dummyUi);
        assertEquals("[T][ ] borrow book", dummyController.getTaskList().getTask(1).toString());
    }

    @Test
    public void execute_deadlineTaskCreation() throws ShadowException {
        ShadowUi dummyUi = new ShadowUi();
        ShadowController dummyController = new ShadowController(null);

        dummyController.executeCommand(DEADLINE_COMMAND, dummyUi);
        assertEquals("[D][ ] return book (by: Dec 2 2025 18:00)",
                dummyController.getTaskList().getTask(1).toString());
    }

    @Test
    public void execute_eventTaskCreation() throws ShadowException {
        ShadowUi dummyUi = new ShadowUi();
        ShadowController dummyController = new ShadowController(null);

        dummyController.executeCommand(EVENT_COMMAND, dummyUi);
        assertEquals("[E][ ] project meeting (from: Jul 4 2025 16:00 to: Jul 5 2025 20:00)",
                dummyController.getTaskList().getTask(1).toString());
    }

    @Test
    public void execute_markTask() throws ShadowException {
        ShadowUi dummyUi = new ShadowUi();
        ShadowController dummyController = new ShadowController(null);

        dummyController.executeCommand(TODO_COMMAND, dummyUi);
        dummyController.executeCommand(MARK_COMMAND, dummyUi);
        assertEquals("[T][X] borrow book", dummyController.getTaskList().getTask(1).toString());
    }

    @Test
    public void execute_unmarkTask() throws ShadowException {
        ShadowUi dummyUi = new ShadowUi();
        ShadowController dummyController = new ShadowController(null);

        dummyController.executeCommand(TODO_COMMAND, dummyUi);
        dummyController.executeCommand(MARK_COMMAND, dummyUi);
        dummyController.executeCommand(UNMARK_COMMAND, dummyUi);
        assertEquals("[T][ ] borrow book", dummyController.getTaskList().getTask(1).toString());
    }

    @Test
    public void execute_deleteTask() throws ShadowException {
        ShadowUi dummyUi = new ShadowUi();
        ShadowController dummyController = new ShadowController(null);

        dummyController.executeCommand(TODO_COMMAND, dummyUi);
        dummyController.executeCommand(DELETE_COMMAND, dummyUi);
        assertEquals(0, dummyController.getTaskList().length());
    }
}