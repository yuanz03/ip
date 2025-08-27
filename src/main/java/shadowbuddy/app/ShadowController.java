package shadowbuddy.app;

import shadowbuddy.services.ShadowException;
import shadowbuddy.storage.ShadowStorage;
import shadowbuddy.taskmodels.Deadline;
import shadowbuddy.taskmodels.Event;
import shadowbuddy.taskmodels.Task;
import shadowbuddy.taskmodels.TaskList;
import shadowbuddy.taskmodels.Todo;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Manages command parsing, task list mutations and storage persistence for Shadow chatbot.
 * The ShadowController class receives parsed ShadowCommand objects, modifies the TaskList,
 * delegates UI messages to ShadowUi, and uses ShadowStorage to save and load tasks.
 */
public class ShadowController {
    protected final TaskList taskList;
    protected final ShadowStorage storage;

    /**
     * Initializes a ShadowController instance with the given ShadowStorage instance.
     * The constructor initializes an empty TaskList and references the given ShadowStorage to save and load tasks.
     *
     * @param storage The ShadowStorage instance used for loading and saving tasks.
     */
    public ShadowController(ShadowStorage storage) {
        this.storage = storage;
        this.taskList = new TaskList();
    }

    /**
     * Loads tasks from the internal storage into the controller's task list.
     * Relies on ShadowStorage to populate the internal TaskList with saved tasks.
     *
     * @throws FileNotFoundException If the database file cannot be found or read.
     */
    public void loadDatabase() throws FileNotFoundException {
        this.storage.loadDatabase(this.taskList);
    }

    /**
     * Returns a ShadowCommand instance parsed from the given raw input String by ShadowParser.
     *
     * @param input The raw user input String to parse.
     * @return The ShadowCommand instance parsed by delegating to ShadowParser.
     * @throws ShadowException If the user input is syntactically invalid.
     */
    public ShadowCommand handleInput(String input) throws ShadowException {
        return ShadowParser.parse(input);
    }

    /**
     * Returns the controller's internal TaskList.
     * This protected getter is solely provided to facilitate unit testing of the ShadowController class.
     */
    protected TaskList getTaskList() {
        return this.taskList;
    }

    /**
     * Executes the given ShadowCommand instance, modifies the internal TaskList, and triggers UI updates.
     * Handles all supported user command types defined in ShadowCommand, updates the internal TaskList,
     * and outputs confirmation messages through the given ShadowUi instance.
     *
     * @param userCommand The parsed ShadowCommand instance to execute.
     * @param ui The ShadowUi instance used to display confirmation messages and TaskList output.
     * @throws ShadowException If the command type is unknown or the task index is invalid.
     */
    public void executeCommand(ShadowCommand userCommand, ShadowUi ui) throws ShadowException {
        int taskIndex = userCommand.taskIndex;
        String taskDescription = userCommand.taskDescription;
        // Code reuse for switch structure
        switch (userCommand.commandType) {
        case LIST:
            ui.showTaskList(this.taskList);
            break;
        case MARK:
            validateTaskIndex(taskIndex, this.taskList.length());
            this.taskList.markTask(taskIndex);
            ui.markConfirmationMessage(this.taskList.getTask(taskIndex));
            break;
        case UNMARK:
            validateTaskIndex(taskIndex, this.taskList.length());
            this.taskList.unmarkTask(taskIndex);
            ui.unmarkConfirmationMessage(this.taskList.getTask(taskIndex));
            break;
        case DELETE:
            validateTaskIndex(taskIndex, this.taskList.length());
            Task deletedTask = this.taskList.deleteTask(taskIndex);
            ui.deleteConfirmationMessage(deletedTask, this.taskList.length());
            break;
        case TODO:
            Task userTodo = new Todo(taskDescription);
            this.taskList.addTask(userTodo);
            ui.taskCreationMessage(userTodo, this.taskList.length());
            break;
        case DEADLINE:
            Task userDeadline = new Deadline(taskDescription, userCommand.dueDate);
            this.taskList.addTask(userDeadline);
            ui.taskCreationMessage(userDeadline, this.taskList.length());
            break;
        case EVENT:
            Task userEvent = new Event(taskDescription, userCommand.startDate, userCommand.endDate);
            this.taskList.addTask(userEvent);
            ui.taskCreationMessage(userEvent, this.taskList.length());
            break;
        case UNKNOWN:
            // Fallthrough
        default:
            throw new ShadowException("Unknown request! Try one of these commands: list, mark, unmark, todo, "
                    + "delete, event, or deadline, and I'll handle it for you.\n");
        }
    }

    /**
     * Validates that the given 1-based task index is within the current bounds of the TaskList.
     * This helper function validates task indices used for mark, unmark, and delete commands.
     *
     * @param taskIndex The 1-based index of the task to validate.
     * @param taskCount The current number of tasks in the TaskList.
     * @throws ShadowException If the TaskList is empty or the index is out of range.
     */
    private void validateTaskIndex(int taskIndex, int taskCount) throws ShadowException {
        if (taskCount == 0) {
            throw new ShadowException("ERROR! Your task list is empty!\n");
        } else if (taskIndex < 1 || taskIndex > taskCount) {
            throw new ShadowException("Invalid task index! Please enter a number between 1 and " + taskCount + ".\n");
        }
    }

    /**
     * Writes the controller's current TaskList to the internal storage.
     * Relies on ShadowStorage to save the internal TaskList contents to the database file.
     *
     * @throws IOException If the database file cannot be written to.
     */
    public void writeToDatabase() throws IOException{
        this.storage.writeToDatabase(this.taskList);
    }
}