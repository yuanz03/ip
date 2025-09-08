package shadowbuddy.taskmodels;

/**
 * Represents a generic task with a description and completion state.
 * The Task class serves as the base class for the different task types (Todo, Deadline, Event),
 * providing common state handling and String representation.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Initializes a Task instance with the given description and default not-done state.
     */
    public Task(String description) {
        assert description != null : "description should not be null";
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns a String representation of the task's completion status.
     *
     * @return "X" if the task is completed, otherwise a blank space.
     */
    public String getStatusIcon() {
        return (this.isDone ? "X" : " ");
    }

    public String getDescription() {
        return this.description;
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns a String representation of this task.
     *
     * @return A String representation of the task's status icon and description.
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + this.description;
    }
}
