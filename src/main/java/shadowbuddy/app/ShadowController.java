package shadowbuddy.app;

import java.io.FileNotFoundException;
import java.io.IOException;

import shadowbuddy.services.ShadowException;
import shadowbuddy.storage.ShadowStorage;
import shadowbuddy.taskmodels.Deadline;
import shadowbuddy.taskmodels.Event;
import shadowbuddy.taskmodels.Task;
import shadowbuddy.taskmodels.TaskList;
import shadowbuddy.taskmodels.Todo;

/**
 * Manages command parsing, task list mutations and storage persistence for Shadow chatbot.
 * The ShadowController class receives parsed ShadowCommand objects, modifies the TaskList,
 * delegates UI messages to ShadowUi, and uses ShadowStorage to save and load tasks.
 */
public class ShadowController {
    protected TaskList taskList;
    protected final ShadowStorage storage;

    /**
     * Initializes a ShadowController instance with the given ShadowStorage instance.
     * The constructor initializes an empty TaskList and references the given ShadowStorage to save and load tasks.
     *
     * @param storage The ShadowStorage instance used for loading and saving tasks.
     */
    public ShadowController(ShadowStorage storage) {
        assert storage != null : "storage should not be null";
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
     * Writes the controller's current TaskList to the internal storage.
     * Relies on ShadowStorage to save the internal TaskList contents to the database file.
     *
     * @throws IOException If the database file cannot be written to.
     */
    public void writeToDatabase() throws IOException {
        this.storage.writeToDatabase(this.taskList);
    }

    /**
     * Returns a ShadowCommand instance parsed from the given raw input String by ShadowParser.
     *
     * @param input The raw user input String to parse.
     * @return The ShadowCommand instance parsed by delegating to ShadowParser.
     * @throws ShadowException If the user input is syntactically invalid.
     */
    public ShadowCommand handleInput(String input) throws ShadowException {
        assert input != null : "user input should not be null";
        return ShadowParser.parse(input);
    }

    /**
     * Executes the given ShadowCommand instance, modifies the internal TaskList, and triggers UI updates.
     * Handles all supported user command types defined in ShadowCommand, updates the internal TaskList,
     * and outputs confirmation messages through the given ShadowUi instance.
     *
     * @param userCommand The parsed ShadowCommand instance to execute.
     * @param ui The ShadowUi instance used to display confirmation messages and TaskList output.
     * @return A String representing the UI confirmation message.
     * @throws ShadowException If the command type is unknown or the task index is invalid.
     */
    public String executeCommand(ShadowCommand userCommand, ShadowUi ui) throws ShadowException {
        assert userCommand != null : "userCommand should not be null";
        assert ui != null : "ui should not be null";
        String taskDescription = userCommand.taskDescription.trim();
        // Solution below inspired from a ChatGPT example on how to use a switch structure to toggle between commands
        switch (userCommand.commandType) {
        case LIST:
            return ui.showTaskList(this.taskList);
        case MARK:
            int markIndex = userCommand.taskIndex;
            validateTaskIndex(markIndex, this.taskList.getLength());
            this.taskList.markTask(markIndex);
            return ui.showMarkConfirmationMessage(this.taskList.getTask(markIndex));
        case UNMARK:
            int unmarkIndex = userCommand.taskIndex;
            validateTaskIndex(unmarkIndex, this.taskList.getLength());
            this.taskList.unmarkTask(unmarkIndex);
            return ui.showUnmarkConfirmationMessage(this.taskList.getTask(unmarkIndex));
        case DELETE:
            int deleteIndex = userCommand.taskIndex;
            validateTaskIndex(deleteIndex, this.taskList.getLength());
            Task deletedTask = this.taskList.deleteTask(deleteIndex);
            return ui.showDeleteConfirmationMessage(deletedTask, this.taskList.getLength());
        case FIND:
            TaskList matchingTasks = this.taskList.getMatchingTasks(this.taskList, taskDescription);
            return ui.showMatchingTasks(matchingTasks);
        case TODO:
            validateTaskDescription(taskDescription);
            Task todo = new Todo(taskDescription);
            this.taskList.addTask(todo);
            return ui.showTaskCreationMessage(todo, this.taskList.getLength());
        case DEADLINE:
            validateTaskDescription(taskDescription);
            Task deadline = new Deadline(taskDescription, userCommand.dueDate);
            this.taskList.addTask(deadline);
            return ui.showTaskCreationMessage(deadline, this.taskList.getLength());
        case EVENT:
            validateTaskDescription(taskDescription);
            Task event = new Event(taskDescription, userCommand.startDate, userCommand.endDate);
            this.taskList.addTask(event);
            return ui.showTaskCreationMessage(event, this.taskList.getLength());
        case UNKNOWN:
            // Fallthrough
        default:
            throw new ShadowException("Unknown request! Try one of these commands: list, mark, unmark, todo, "
                    + "delete, event, or deadline, and I'll handle it for you.\n");
        }
    }

    /**
     * Returns the controller's internal TaskList.
     * This protected getter is solely provided to facilitate unit testing of the ShadowController class.
     */
    protected TaskList getTaskList() {
        return this.taskList;
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
        assert taskCount >= 0 : "taskCount should not be negative";
        if (taskCount == 0) {
            throw new ShadowException("ERROR! Your task list is empty!\n");
        } else if (taskIndex < 1 || taskIndex > taskCount) {
            throw new ShadowException("Invalid task index! Please enter a number between 1 and " + taskCount + ".\n");
        }
    }

    /**
     * Validates that the given task description is unique within the TaskList.
     * This helper function validates task descriptions used for todo, deadline, and event commands.
     *
     * @param taskDescription The description of the task to validate.
     * @throws ShadowException If duplicate task descriptions are detected.
     */
    private void validateTaskDescription(String taskDescription) throws ShadowException {
        if (this.taskList.containsDuplicate(taskDescription)) {
            throw new ShadowException("Invalid request! A task with this description already exists!\n");
        }
    }
}
