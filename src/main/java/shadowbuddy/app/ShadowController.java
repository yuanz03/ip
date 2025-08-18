package shadowbuddy.app;

import shadowbuddy.services.TaskList;
import shadowbuddy.taskmodels.Deadline;
import shadowbuddy.taskmodels.Task;

public class ShadowController {
    protected static final TaskList taskList = new TaskList();

    public void run(String input) {
        if (input.equalsIgnoreCase("list")) {
            System.out.println("\nHere are the tasks in your list:\n" + taskList);
            return;
        }

        String[] inputData = input.split(" ");
        String requestType = inputData[0];
        String requestDetails = input.substring(requestType.length() + 1);

        switch (requestType.toLowerCase()) {
        case "mark":
            handleMarkingTask(requestDetails);
            break;
        case "unmark":
            handleUnmarkingTask(requestDetails);
            break;
        case "deadline":
            handleDeadline(requestDetails);
            break;
        default:
            Task userTask = new Task(input);
            taskList.addTask(userTask);
            System.out.println("\nGot it. I've added this task:\n " + userTask);
            System.out.println("Now you have " + taskList.length() + " tasks in the list.\n");
            break;
        }
    }

    private static void handleMarkingTask(String index) {
        try {
            int taskIndex = Integer.parseInt(index);
            taskList.markTask(taskIndex);
            System.out.println("\nNice! I've marked this task as done:\n  " + taskList.getTask(taskIndex) + "\n");
        } catch (NumberFormatException exception) {
            System.out.println("Require number after mark command!\n");
        }
    }

    private static void handleUnmarkingTask(String index) {
        try {
            int taskIndex = Integer.parseInt(index);
            taskList.unmarkTask(taskIndex);
            System.out.println("\nOK, I've marked this task as not done:\n  " + taskList.getTask(taskIndex) + "\n");
        } catch (NumberFormatException exception) {
            System.out.println("Require number after unmark command!\n");
        }
    }

    private static void handleDeadline(String details) {
        String[] detailsData = details.split(" /by ");
        Deadline userDeadline = new Deadline(detailsData[0], detailsData[1]);
        taskList.addTask(userDeadline);
        System.out.println("\nGot it. I've added this task:\n  " + userDeadline);
        System.out.println("Now you have " + taskList.length() + " tasks in the list.\n");
    }
}