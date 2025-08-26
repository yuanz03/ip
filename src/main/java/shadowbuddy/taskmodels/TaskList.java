package shadowbuddy.taskmodels;

import java.util.ArrayList;

/**
 * Manages an ordered collection of Task instances.
 * The TaskList class provides operations to add, remove, and toggle the completion status of tasks.
 * All methods use 1-based indexing to match the TaskList numbering.
 */
public class TaskList {
    protected final ArrayList<Task> storage;

    /**
     * Initializes an empty TaskList instance with a pre-allocated capacity of 100 elements.
     */
    public TaskList() {
        this.storage = new ArrayList<>(100);
    }

    /**
     * Appends the given task to the end of the TaskList.
     *
     * @param task The Task to add to the TaskList.
     */
    public void addTask(Task task) {
        this.storage.add(task);
    }

    /**
     * Removes the task at the given 1-based index and returns it.
     *
     * @param index The 1-based index of the Task to remove.
     * @return The Task that was removed from the TaskList.
     */
    public Task deleteTask(int index) {
        return this.storage.remove(index - 1);
    }

    /**
     * Marks the task at the given 1-based index as done.
     *
     * @param index The 1-based index of the Task to mark.
     */
    public void markTask(int index) {
        this.storage.get(index - 1).markAsDone();
    }

    /**
     * Marks the task at the given 1-based index as not done.
     *
     * @param index The 1-based index of the Task to unmark.
     */
    public void unmarkTask(int index) {
       this.storage.get(index - 1).markAsNotDone();
    }

    public Task getTask(int index) {
        return this.storage.get(index - 1);
    }

    public int length() {
        return this.storage.size();
    }

    /**
     * Returns this TaskList as a numbered, formatted String.
     * Each task is rendered on a separate line, prefixed with a 1-based index.
     *
     * @return The String representation of the TaskList.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.storage.size(); i++) { // Code reuse
            sb.append(i + 1).append(". ").append(this.storage.get(i)).append("\n");
        }
        return sb.toString();
    }
}