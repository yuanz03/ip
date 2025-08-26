package shadowbuddy.app;

import shadowbuddy.taskmodels.TaskList;
import shadowbuddy.taskmodels.Task;

public class ShadowUi {

    public void greetUsers() {
        System.out.println("Hi, I'm Shadow, your personal assistant!");
        System.out.println("What can I help you with today?\n");
    }

    public void sayGoodbye() {
        System.out.println("\nGoodbye! I'll be here if you need anything else!");
    }

    public void showTaskList(TaskList taskList) {
        System.out.println("\nHere are the tasks in your list:\n" + taskList);
    }

    public void showMatchingTasks(TaskList taskList) {
        System.out.println("\nHere are the matching tasks in your list:\n" + taskList);
    }

    public void markConfirmationMessage(Task task) {
        System.out.println("\nNice! I've marked this task as done:\n  " + task + "\n");
    }

    public void unmarkConfirmationMessage(Task task) {
        System.out.println("\nOK, I've marked this task as not done:\n  " + task + "\n");
    }

    public void deleteConfirmationMessage(Task task, int taskCount) {
        System.out.println("\nNoted. I've removed this task:\n  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.\n");
    }
    public void taskCreationMessage(Task task, int taskCount) {
        System.out.println("\nGot it. I've added this task:\n  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.\n");
    }
}