package shadowbuddy.taskmodels;

/**
 * Models an Event task with a description, completion state, start date, and end date.
 * The Event class is a subclass of Task and inherits its functionality.
 */
public class Event extends Task {
    protected String startDate;
    protected String endDate;

    /**
     * Initializes an Event instance with the given description, start date, and end date.
     * The new Event instance is initially not done.
     *
     * @param description The description of the Event task.
     * @param startDate The start date associated with the Event task.
     * @param endDate The end date associated with the Event task.
     */
    public Event(String description, String startDate, String endDate) {
        super(description);
        assert startDate != null : "startDate should not be null";
        assert endDate != null : "endDate should not be null";
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    /**
     * Returns a String representation of this Event.
     *
     * @return A formatted String representation of this Event task.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.startDate + " to: " + this.endDate + ")";
    }
}
