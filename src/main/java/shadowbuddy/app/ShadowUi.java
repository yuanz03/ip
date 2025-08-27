package shadowbuddy.app;

import shadowbuddy.taskmodels.TaskList;
import shadowbuddy.taskmodels.Task;

/**
 * Provides simple console messages for user interaction with the chatbot.
 * The ShadowUi class encapsulates all messages displayed to the user, such as greetings, farewells,
 * and task-related confirmations, providing a single platform for handling communication.
 */
public class ShadowUi {
    /**
     * Prints a friendly greeting and prompts the user for an input.
     */
    public void greetUsers() {
        System.out.println("Hi, I'm Shadow, your personal assistant!");
        System.out.println("What can I help you with today?\n");
    }

    /**
     * Prints a friendly goodbye message to the user.
     */
    public void sayGoodbye() {
        System.out.println("\nGoodbye! I'll be here if you need anything else!");
    }

    /**
     * Displays all tasks in the given TaskList to the user.
     *
     * @param taskList The TaskList whose tasks will be printed for the user.
     */
    public void showTaskList(TaskList taskList) {
        System.out.println("\nHere are the tasks in your list:\n" + taskList);
    }

    /**
     * Displays all matching tasks from the given TaskList that match the specified keyword.
     *
     * @param taskList The TaskList whose matching tasks will be printed for the user.
     */
    public void showMatchingTasks(TaskList taskList) {
        System.out.println("\nHere are the matching tasks in your list:\n" + taskList);
    }

    /**
     * Prints a confirmation message that the given task has been marked as done.
     * The String representation of the completed task is also displayed.
     *
     * @param task The Task that was marked as done.
     */
    public void markConfirmationMessage(Task task) {
        System.out.println("\nNice! I've marked this task as done:\n  " + task + "\n");
    }

    /**
     * Prints a confirmation message that the given task has been marked as not done.
     * The String representation of the incomplete task is also displayed.
     *
     * @param task The Task that was marked as not done.
     */
    public void unmarkConfirmationMessage(Task task) {
        System.out.println("\nOK, I've marked this task as not done:\n  " + task + "\n");
    }

    /**
     * Prints a confirmation message that the given task has been deleted.
     * The String representation of the deleted task and the remaining task count are also displayed.
     *
     * @param task The Task that was deleted.
     * @param taskCount The number of tasks remaining in the task list.
     */
    public void deleteConfirmationMessage(Task task, int taskCount) {
        System.out.println("\nNoted. I've removed this task:\n  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.\n");
    }

    /**
     * Prints a confirmation message that the given task has been created.
     * The String representation of the created task and the new task count are also displayed.
     *
     * @param task The Task that was created.
     * @param taskCount The updated number of tasks in the task list.
     */
    public void taskCreationMessage(Task task, int taskCount) {
        System.out.println("\nGot it. I've added this task:\n  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.\n");
    }
}