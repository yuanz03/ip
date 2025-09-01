package shadowbuddy.taskmodels;

/**
 * Models a Deadline task with a description, completion state, and due date.
 * The Deadline class is a subclass of Task and inherits its functionality.
 */
public class Deadline extends Task {
    /** Due date of the Deadline */
    protected String dueDate;

    /**
     * Initializes a Deadline instance with the given description and due date.
     * The new Deadline instance is initially not done.
     *
     * @param description The description of the Deadline task.
     * @param dueDate The due date associated with the Deadline task.
     */
    public Deadline(String description, String dueDate) {
        super(description);
        this.dueDate = dueDate;
    }

    public String getDueDate() {
        return this.dueDate;
    }

    /**
     * Returns a String representation of this Deadline.
     *
     * @return A formatted String representation of this Deadline task.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.dueDate + ")";
    }
}
