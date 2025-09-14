package shadowbuddy.app;

import shadowbuddy.taskmodels.Task;
import shadowbuddy.taskmodels.TaskList;

/**
 * Provides simple messages for user interaction with the chatbot.
 * The ShadowUi class encapsulates all messages displayed to the user, such as greetings, farewells,
 * and task-related confirmations, providing a single platform for handling communication.
 */
public class ShadowUi {
    public String greetUser() {
        return "Hi, I'm Shadow, your personal assistant!\nWhat can I help you with today?\n";
    }

    public String sayGoodbye() {
        return "Goodbye! I'll be here if you need anything else!";
    }

    public String showTaskList(TaskList taskList) {
        return "Here are the tasks in your list:\n" + taskList;
    }

    /**
     * Displays all matching tasks from the given TaskList that match the specified keyword.
     *
     * @param filteredTaskList The TaskList whose matching tasks will be displayed for the user.
     */
    public String showMatchingTasks(TaskList filteredTaskList) {
        return "Here are the matching tasks in your list:\n" + filteredTaskList;
    }

    /**
     * Returns a confirmation message that the given task has been marked as done.
     * The String representation of the completed task is also displayed.
     *
     * @param task The Task that was marked as done.
     */
    public String showMarkConfirmationMessage(Task task) {
        return "Nice! I've marked this task as done:\n  " + task + "\n";
    }

    /**
     * Returns a confirmation message that the given task has been marked as not done.
     * The String representation of the incomplete task is also displayed.
     *
     * @param task The Task that was marked as not done.
     */
    public String showUnmarkConfirmationMessage(Task task) {
        return "OK, I've marked this task as not done:\n  " + task + "\n";
    }

    /**
     * Returns a confirmation message that the given task has been deleted.
     * The String representation of the deleted task and the remaining task count are also displayed.
     *
     * @param task The Task that was deleted.
     * @param taskCount The number of tasks remaining in the task list.
     */
    public String showDeleteConfirmationMessage(Task task, int taskCount) {
        return "Noted. I've removed this task:\n  " + task + "\nNow you have " + taskCount + " tasks in the list.\n";
    }

    /**
     * Returns a confirmation message that the given task has been created.
     * The String representation of the created task and the new task count are also displayed.
     *
     * @param task The Task that was created.
     * @param taskCount The updated number of tasks in the task list.
     */
    public String showTaskCreationMessage(Task task, int taskCount) {
        return "Got it. I've added this task:\n  " + task + "\nNow you have " + taskCount + " tasks in the list.\n";
    }
}
