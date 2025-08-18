package shadowbuddy.app;

import shadowbuddy.services.TaskList;
import shadowbuddy.taskmodels.Deadline;
import shadowbuddy.taskmodels.Event;
import shadowbuddy.taskmodels.Task;
import shadowbuddy.taskmodels.Todo;

public class ShadowController {
    protected static final TaskList taskList = new TaskList();

    public void run(String input) {
        if (input.equalsIgnoreCase("list")) {
            System.out.println("\nHere are the tasks in your list:\n" + taskList);
            return;
        }

        // Code reuse
        String[] inputData = input.split(" ");
        String requestType = inputData[0];
        String requestDetails = input.substring(requestType.length() + 1);

        // Code reuse
        switch (requestType.toLowerCase()) {
        case "mark":
            toggleTaskStatus(requestDetails, true);
            break;
        case "unmark":
            toggleTaskStatus(requestDetails, false);
            break;
        case "deadline":
            createDeadline(requestDetails);
            break;
        case "event":
            createEvent(requestDetails);
            break;
        default:
            createTodo(requestDetails);
            break;
        }
    }

    // Code reuse
    private static void toggleTaskStatus(String index, boolean flag) {
        int taskIndex = stringToIndex(index);

        if (flag) {
            taskList.markTask(taskIndex);
            System.out.println("\nNice! I've marked this task as done:\n  " + taskList.getTask(taskIndex) + "\n");
        } else {
            taskList.unmarkTask(taskIndex);
            System.out.println("\nOK, I've marked this task as not done:\n  " + taskList.getTask(taskIndex) + "\n");
        }
    }

    private static void createDeadline(String details) {
        String[] deadlineDetails = details.split(" /by ");
        Task userDeadline = new Deadline(deadlineDetails[0], deadlineDetails[1]);
        taskList.addTask(userDeadline);
        taskConfirmationMessage(userDeadline);
    }

    private static void createEvent(String details) {
        String[] eventDetails = details.split(" /from ");
        String[] eventTimings = eventDetails[1].split(" /to ");
        Task userEvent = new Event(eventDetails[0], eventTimings[0], eventTimings[1]);
        taskList.addTask(userEvent);
        taskConfirmationMessage(userEvent);
    }

    private static void createTodo(String details) {
        Task userTodo = new Todo(details);
        taskList.addTask(userTodo);
        taskConfirmationMessage(userTodo);
    }

    // Code reuse
    private static int stringToIndex(String index) {
        try {
            return Integer.parseInt(index);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    private static void taskConfirmationMessage(Task task) {
        System.out.println("\nGot it. I've added this task:\n  " + task);
        System.out.println("Now you have " + taskList.length() + " tasks in the list.\n");
    }
}