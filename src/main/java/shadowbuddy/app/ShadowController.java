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

public class ShadowController {
    protected final TaskList taskList;
    protected final ShadowStorage storage;
    protected ShadowCommand userCommand;

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
            this.taskList.markTask(taskIndex);
            ui.markConfirmationMessage(this.taskList.getTask(taskIndex));
            break;
        case UNMARK:
            this.taskList.unmarkTask(taskIndex);
            ui.unmarkConfirmationMessage(this.taskList.getTask(taskIndex));
            break;
        case DELETE:
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

    public void writeToDatabase() throws IOException{
        this.storage.writeToDatabase(this.taskList);
    }
}