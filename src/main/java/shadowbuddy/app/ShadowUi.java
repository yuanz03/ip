package shadowbuddy.app;

import shadowbuddy.services.ShadowException;
import shadowbuddy.services.TaskList;
import shadowbuddy.storage.ShadowStorage;
import shadowbuddy.taskmodels.Deadline;
import shadowbuddy.taskmodels.Event;
import shadowbuddy.taskmodels.Task;
import shadowbuddy.taskmodels.Todo;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ShadowUi {
    protected final TaskList taskList;
    protected final ShadowStorage storage;
    protected ShadowCommand userCommand;

    public ShadowUi(ShadowStorage storage) {
        this.storage = storage;
        this.taskList = new TaskList();
    }

    public void loadDatabase() throws FileNotFoundException {
        this.storage.loadDatabase(this.taskList);
    }

    public void greetUsers() {
        System.out.println("Hi, I'm Shadow, your personal assistant!");
        System.out.println("What can I help you with today?\n");
    }

    public void sayGoodbye() {
        System.out.println("\nGoodbye! I'll be here if you need anything else!");
    }

    public void handleInput(String input) {
        try {
            userCommand = ShadowParser.parse(input);
        } catch (ShadowException exception) {
            System.out.println("\n" + exception.getMessage());
        }

        try {
            int taskIndex = userCommand.taskIndex;
            String taskDescription = userCommand.taskDescription;
            // Code reuse for switch structure
            switch (userCommand.commandType) {
            case LIST:
                System.out.println("\nHere are the tasks in your list:\n" + this.taskList);
                break;
            case MARK:
                this.taskList.markTask(taskIndex);
                System.out.println("\nNice! I've marked this task as done:\n  "
                        + this.taskList.getTask(taskIndex) + "\n");
                break;
            case UNMARK:
                this.taskList.unmarkTask(taskIndex);
                System.out.println("\nOK, I've marked this task as not done:\n  "
                        + this.taskList.getTask(taskIndex) + "\n");
                break;
            case DELETE:
                Task deletedTask = this.taskList.deleteTask(taskIndex);
                System.out.println("\nNoted. I've removed this task:\n  " + deletedTask);
                System.out.println("Now you have " + this.taskList.length() + " tasks in the list.\n");
                break;
            case TODO:
                Task userTodo = new Todo(taskDescription);
                this.taskList.addTask(userTodo);
                taskConfirmationMessage(userTodo);
                break;
            case DEADLINE:
                Task userDeadline = new Deadline(taskDescription, userCommand.dueDate);
                this.taskList.addTask(userDeadline);
                taskConfirmationMessage(userDeadline);
                break;
            case EVENT:
                Task userEvent = new Event(taskDescription, userCommand.startDate, userCommand.endDate);
                this.taskList.addTask(userEvent);
                taskConfirmationMessage(userEvent);
                break;
            case UNKNOWN:
                // Fallthrough
            default:
                throw new ShadowException("Unknown request! Try one of these commands: list, mark, unmark, todo, "
                        + "delete, event, or deadline, and I'll handle it for you.\n");
            }
        } catch (ShadowException exception) {
            System.out.println("\n" + exception.getMessage());
        }

        try {
            this.storage.writeToDatabase(this.taskList);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void taskConfirmationMessage(Task task) {
        System.out.println("\nGot it. I've added this task:\n  " + task);
        System.out.println("Now you have " + this.taskList.length() + " tasks in the list.\n");
    }
}