package shadowbuddy.app;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import shadowbuddy.services.ShadowException;
import shadowbuddy.services.TaskList;
import shadowbuddy.storage.ShadowStorage;
import shadowbuddy.taskmodels.Deadline;
import shadowbuddy.taskmodels.Event;
import shadowbuddy.taskmodels.Task;
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

    public void run(String input) {
        if (input.equalsIgnoreCase("list")) {
            System.out.println("\nHere are the tasks in your list:\n" + this.taskList);
            return;
        }

        // Code reuse
        String[] inputData = input.split(" ");
        String requestType = inputData[0];
        String requestDetails = inputData.length > 1 ? input.substring(requestType.length() + 1) : "";

        try {
            // Code reuse
            switch (requestType.toLowerCase()) {
            case "mark":
                toggleTaskStatus(requestDetails, true);
                break;
            case "unmark":
                toggleTaskStatus(requestDetails, false);
                break;
            case "delete":
                handleDeleteTask(requestDetails);
                break;
            case "deadline":
                createDeadline(requestDetails);
                break;
            case "event":
                createEvent(requestDetails);
                break;
            case "todo":
                createTodo(requestDetails);
                break;
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

    // Code reuse
    private void toggleTaskStatus(String index, boolean flag) {
        int taskIndex = stringToIndex(index);

        if (flag) {
            this.taskList.markTask(taskIndex);
            System.out.println("\nNice! I've marked this task as done:\n  "
                    + this.taskList.getTask(taskIndex) + "\n");
        } else {
            this.taskList.unmarkTask(taskIndex);
            System.out.println("\nOK, I've marked this task as not done:\n  "
                    + this.taskList.getTask(taskIndex) + "\n");
        }
    }

    private void handleDeleteTask(String index) {
        int taskIndex = stringToIndex(index);
        Task deletedTask = this.taskList.deleteTask(taskIndex);
        System.out.println("\nNoted. I've removed this task:\n  " + deletedTask);
        System.out.println("Now you have " + this.taskList.length() + " tasks in the list.\n");
    }

    private void createDeadline(String details) throws ShadowException {
        if (details.isEmpty()) {
            throw new ShadowException("Invalid request! Please provide a description for your deadline.\n");
        }

        String[] deadlineDetails = details.split(" /by ");
        String formattedDueDate = formatTaskDateTime(deadlineDetails[1]);

        Task userDeadline = new Deadline(deadlineDetails[0], formattedDueDate);
        this.taskList.addTask(userDeadline);
        taskConfirmationMessage(userDeadline);
    }

    private void createEvent(String details) throws ShadowException {
        if (details.isEmpty()) {
            throw new ShadowException("Invalid request! Please provide a description for your event.\n");
        }

        String[] eventDetails = details.split(" /from ");
        String[] eventTimings = eventDetails[1].split(" /to ");
        String formattedStartDate = formatTaskDateTime(eventTimings[0]);
        String formattedEndDate = formatTaskDateTime(eventTimings[1]);

        Task userEvent = new Event(eventDetails[0], formattedStartDate, formattedEndDate);
        this.taskList.addTask(userEvent);
        taskConfirmationMessage(userEvent);
    }

    private void createTodo(String details) throws ShadowException {
        if (details.isEmpty()) {
            throw new ShadowException("Invalid request! Please provide a description for your todo.\n");
        }

        Task userTodo = new Todo(details);
        this.taskList.addTask(userTodo);
        taskConfirmationMessage(userTodo);
    }

    private String formatTaskDateTime(String timestamp) {
        DateTimeFormatter taskInputFormatter = DateTimeFormatter.ofPattern("d/M/yyyy HHmm"); // code reuse
        DateTimeFormatter taskOutputFormatter = DateTimeFormatter.ofPattern("MMM d yyyy HH:mm");
        LocalDateTime taskTimestamp = LocalDateTime.parse(timestamp, taskInputFormatter);
        return taskTimestamp.format(taskOutputFormatter);
    }

    // Code reuse
    private int stringToIndex(String index) {
        try {
            return Integer.parseInt(index);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    private void taskConfirmationMessage(Task task) {
        System.out.println("\nGot it. I've added this task:\n  " + task);
        System.out.println("Now you have " + this.taskList.length() + " tasks in the list.\n");
    }
}