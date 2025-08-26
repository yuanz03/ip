package shadowbuddy.app;

public class ShadowCommand {
    public enum CommandType { LIST, MARK, UNMARK, DELETE, FIND, TODO, DEADLINE, EVENT, UNKNOWN } // code reuse

    protected final CommandType commandType;
    protected final String taskDescription;
    protected final String dueDate;
    protected final String startDate;
    protected final String endDate;
    protected final int taskIndex;

    public ShadowCommand(CommandType commandType) {
        this.commandType = commandType;
        this.taskDescription = "";
        this.dueDate = "";
        this.startDate = "";
        this.endDate = "";
        this.taskIndex = -1;
    }

    public ShadowCommand(CommandType commandType, int taskIndex) {
        this.commandType = commandType;
        this.taskDescription = "";
        this.dueDate = "";
        this.startDate = "";
        this.endDate = "";
        this.taskIndex = taskIndex;
    }

    public ShadowCommand(CommandType commandType, String taskDescription) {
        this.commandType = commandType;
        this.taskDescription = taskDescription;
        this.dueDate = "";
        this.startDate = "";
        this.endDate = "";
        this.taskIndex = -1;
    }

    public ShadowCommand(CommandType commandType, String taskDescription, String dueDate) {
        this.commandType = commandType;
        this.taskDescription = taskDescription;
        this.dueDate = dueDate;
        this.startDate = "";
        this.endDate = "";
        this.taskIndex = -1;
    }

    public ShadowCommand(CommandType commandType, String taskDescription, String startDate, String endDate) {
        this.commandType = commandType;
        this.taskDescription = taskDescription;
        this.dueDate = "";
        this.startDate = startDate;
        this.endDate = endDate;
        this.taskIndex = -1;
    }
}