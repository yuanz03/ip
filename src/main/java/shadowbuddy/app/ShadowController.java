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

public class ShadowController {
    protected final TaskList taskList;
    protected final ShadowStorage storage;

    public ShadowController(ShadowStorage storage) {
        this.storage = storage;
        this.taskList = new TaskList();
    }

    public void loadDatabase() throws FileNotFoundException {
        this.storage.loadDatabase(this.taskList);
    }

    public ShadowCommand handleInput(String input) throws ShadowException {
        return ShadowParser.parse(input);
    }

    protected TaskList getTaskList() {
        return this.taskList;
    }

    public void executeCommand(ShadowCommand userCommand, ShadowUi ui) throws ShadowException {
        int taskIndex = userCommand.taskIndex;
        String taskDescription = userCommand.taskDescription;
        // Code reuse for switch structure
        switch (userCommand.commandType) {
        case LIST:
            ui.showTaskList(this.taskList);
            break;
        case MARK:
            validateTaskIndex(taskIndex, this.taskList.getLength());
            this.taskList.markTask(taskIndex);
            ui.showMarkConfirmationMessage(this.taskList.getTask(taskIndex));
            break;
        case UNMARK:
            validateTaskIndex(taskIndex, this.taskList.getLength());
            this.taskList.unmarkTask(taskIndex);
            ui.showUnmarkConfirmationMessage(this.taskList.getTask(taskIndex));
            break;
        case DELETE:
            validateTaskIndex(taskIndex, this.taskList.getLength());
            Task deletedTask = this.taskList.deleteTask(taskIndex);
            ui.showDeleteConfirmationMessage(deletedTask, this.taskList.getLength());
            break;
        case TODO:
            Task userTodo = new Todo(taskDescription);
            this.taskList.addTask(userTodo);
            ui.showTaskCreationMessage(userTodo, this.taskList.getLength());
            break;
        case DEADLINE:
            Task userDeadline = new Deadline(taskDescription, userCommand.dueDate);
            this.taskList.addTask(userDeadline);
            ui.showTaskCreationMessage(userDeadline, this.taskList.getLength());
            break;
        case EVENT:
            Task userEvent = new Event(taskDescription, userCommand.startDate, userCommand.endDate);
            this.taskList.addTask(userEvent);
            ui.showTaskCreationMessage(userEvent, this.taskList.getLength());
            break;
        case UNKNOWN:
            // Fallthrough
        default:
            throw new ShadowException("Unknown request! Try one of these commands: list, mark, unmark, todo, "
                    + "delete, event, or deadline, and I'll handle it for you.\n");
        }
    }

    private void validateTaskIndex(int taskIndex, int taskCount) throws ShadowException {
        if (taskCount == 0) {
            throw new ShadowException("ERROR! Your task list is empty!\n");
        } else if (taskIndex < 1 || taskIndex > taskCount) {
            throw new ShadowException("Invalid task index! Please enter a number between 1 and " + taskCount + ".\n");
        }
    }

    public void writeToDatabase() throws IOException{
        this.storage.writeToDatabase(this.taskList);
    }
}