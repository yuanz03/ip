package shadowbuddy.taskmodels;

/**
 * Models a Todo task with a description and completion state.
 * The Todo class is a subclass of Task and inherits its functionality.
 */
public class Todo extends Task {
    /**
     * Initializes a Todo instance with the given description.
     * The new Todo instance is initially not done.
     *
     * @param description The description of the Todo task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns a String representation of this Todo.
     *
     * @return A formatted String representation of this Todo task.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}