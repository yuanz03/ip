package shadowbuddy.app;

import shadowbuddy.services.TaskList;
import shadowbuddy.taskmodels.Task;

public class ShadowController {
    protected static final TaskList taskList = new TaskList();

    public void run(String input) {
        if (input.equalsIgnoreCase("list")) {
            System.out.println("\nHere are the tasks in your list:\n" + taskList);
            return;
        }

        String[] inputData = input.split(" ");
        String inputCommand = inputData[0];
        String inputIndex = inputData.length == 2 ? inputData[1] : " ";

        switch (inputCommand.toLowerCase()) {
        case "mark":
            handleMarkingTask(inputIndex);
            break;
        case "unmark":
            handleUnmarkingTask(inputIndex);
            break;
        default:
            Task userTask = new Task(input);
            taskList.addTask(userTask);
            System.out.println("\nadded: " + input + "\n");
            break;
        }
    }

    private static void handleMarkingTask(String index) {
        try {
            int taskIndex = Integer.parseInt(index);
            taskList.markTask(taskIndex);
            System.out.println("\nNice! I've marked this task as done:\n " + taskList.getTask(taskIndex) + "\n");
        } catch (NumberFormatException exception) {
            System.out.println("Require number after mark command!\n");
        }
    }

    private static void handleUnmarkingTask(String index) {
        try {
            int taskIndex = Integer.parseInt(index);
            taskList.unmarkTask(taskIndex);
            System.out.println("\nOK, I've marked this task as not done yet:\n " + taskList.getTask(taskIndex) + "\n");
        } catch (NumberFormatException exception) {
            System.out.println("Require number after unmark command!\n");
        }
    }
}