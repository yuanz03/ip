package shadowbuddy.app;

import shadowbuddy.services.ShadowException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Parses raw user input into ShadowCommand objects used by the controller.
 * The ShadowParser class interprets user commands defined in ShadowCommand, validates input,
 * and converts raw timestamps into a standardized format.
 */
public class ShadowParser {
    /**
     * Parses raw user input String into a ShadowCommand instance.
     * The user input is analyzed to identify the command type, before returning the corresponding
     * ShadowCommand instance. Parsing of deadline and event commands is delegated to specialized parsers.
     *
     * @param input The raw user input String to parse.
     * @return A ShadowCommand instance representing the parsed input.
     * @throws ShadowException If the user input is syntactically invalid.
     */
    public static ShadowCommand parse(String input) throws ShadowException {
        // Code reuse
        String[] inputData = input.split(" ");
        String requestType = inputData[0].toLowerCase();
        String requestDetails = inputData.length > 1 ? input.substring(requestType.length() + 1) : "";
        // Code reuse for switch structure and return statement
        switch (requestType) {
        case "list":
            return new ShadowCommand(ShadowCommand.CommandType.LIST);
        case "mark":
            int markIndex = stringToIndex(requestDetails);
            return new ShadowCommand(ShadowCommand.CommandType.MARK, markIndex);
        case "unmark":
            int unmarkIndex = stringToIndex(requestDetails);
            return new ShadowCommand(ShadowCommand.CommandType.UNMARK, unmarkIndex);
        case "delete":
            int deleteIndex = stringToIndex(requestDetails);
            return new ShadowCommand(ShadowCommand.CommandType.DELETE, deleteIndex);
        case "find":
            if (requestDetails.isEmpty()) {
                throw new ShadowException("Invalid request! Please provide a keyword for your find.\n");
            } else if (inputData.length > 2) {
                throw new ShadowException("Invalid request! Please provide only ONE keyword for your find.\n");
            }
            return new ShadowCommand(ShadowCommand.CommandType.FIND, requestDetails);
        case "todo":
            if (requestDetails.isEmpty()) {
                throw new ShadowException("Invalid request! Please provide a description for your todo.\n");
            }
            return new ShadowCommand(ShadowCommand.CommandType.TODO, requestDetails);
        case "deadline":
            return parseDeadline(requestDetails);
        case "event":
            return parseEvent(requestDetails);
        default:
            return new ShadowCommand(ShadowCommand.CommandType.UNKNOWN);
        }
    }

    /**
     * Parses Deadline command details and returns the corresponding ShadowCommand instance.
     * Expects the command details to include a task description, the "/by" keyword,
     * and a due date in the format "d/M/yyyy HHmm".
     *
     * @param requestDetails The trailing input after the deadline keyword.
     * @return A ShadowCommand instance representing the Deadline details.
     * @throws ShadowException If "/by", description, or due date is missing, or the date format is invalid.
     */
    private static ShadowCommand parseDeadline(String requestDetails) throws ShadowException {
        if (requestDetails.isEmpty()) {
            throw new ShadowException("Invalid request! Please provide a description for your deadline.\n");
        } else if (!requestDetails.contains("/by")) {
            throw new ShadowException("Invalid format! Please use: deadline DESCRIPTION /by d/M/yyyy HHmm.\n");
        }

        String[] deadlineDetails = requestDetails.split("/by");
        if (deadlineDetails.length < 2 || deadlineDetails[1].trim().isEmpty()) {
            throw new ShadowException("Missing due date! Please use: deadline DESCRIPTION /by d/M/yyyy HHmm.\n");
        }

        try {
            String formattedDueDate = formatTaskDateTime(deadlineDetails[1].trim());
            return new ShadowCommand(ShadowCommand.CommandType.DEADLINE, deadlineDetails[0].trim(), formattedDueDate);
        } catch (DateTimeParseException exception) {
            throw new ShadowException("Invalid due date! Please use: deadline DESCRIPTION /by d/M/yyyy HHmm.\n");
        }
    }

    /**
     * Parses Event command details and returns the corresponding ShadowCommand instance.
     * Expects the command details to include a task description, the "/from" and "/to" keywords,
     * a start date in the format "d/M/yyyy HHmm", and an end date in the format "d/M/yyyy HHmm".
     *
     * @param requestDetails The trailing input after the event keyword.
     * @return A ShadowCommand instance representing the Event details.
     * @throws ShadowException If "/from", "/to", description, or dates are missing, or the date format is invalid.
     */
    private static ShadowCommand parseEvent(String requestDetails) throws ShadowException {
        if (requestDetails.isEmpty()) {
            throw new ShadowException("Invalid request! Please provide a description for your event.\n");
        } else if (!requestDetails.contains("/from") || !requestDetails.contains("/to")) {
            throw new ShadowException("Invalid format! "
                    + "Please use: event DESCRIPTION /from d/M/yyyy HHmm /to d/M/yyyy HHmm.\n");
        }

        String[] eventDetails = requestDetails.split("/from");
        String[] eventTimings = eventDetails[1].split("/to");
        if (eventTimings[0].trim().isEmpty()) {
            throw new ShadowException("Missing start date! "
                    + "Please use: event DESCRIPTION /from d/M/yyyy HHmm /to d/M/yyyy HHmm.\n");
        } else if (eventTimings.length < 2 || eventTimings[1].trim().isEmpty()) {
            throw new ShadowException("Missing end date! "
                    + "Please use: event DESCRIPTION /from d/M/yyyy HHmm /to d/M/yyyy HHmm.\n");
        }

        try {
            String formattedStartDate = formatTaskDateTime(eventTimings[0].trim());
            String formattedEndDate = formatTaskDateTime(eventTimings[1].trim());
            return new ShadowCommand(ShadowCommand.CommandType.EVENT, eventDetails[0].trim(),
                    formattedStartDate, formattedEndDate);
        } catch (DateTimeParseException exception) {
            throw new ShadowException("Invalid start or end date! "
                    + "Please use: event DESCRIPTION /from d/M/yyyy HHmm /to d/M/yyyy HHmm.\n");
        }
    }

    /**
     * Returns a String representing the given task timestamp, formatted as "MMM d yyyy HH:mm".
     * This helper function converts the given timestamp using the DateTimeFormatter class.
     *
     * @param timestamp The raw task timestamp in "d/M/yyyy HHmm" format.
     * @return A formatted String representation of the given task timestamp.
     */
    private static String formatTaskDateTime(String timestamp) {
        DateTimeFormatter taskInputFormatter = DateTimeFormatter.ofPattern("d/M/yyyy HHmm"); // code reuse
        DateTimeFormatter taskOutputFormatter = DateTimeFormatter.ofPattern("MMM d yyyy HH:mm");
        LocalDateTime taskTimestamp = LocalDateTime.parse(timestamp, taskInputFormatter);
        return taskTimestamp.format(taskOutputFormatter);
    }

    /**
     * Returns an integer corresponding to the given numeric String.
     * This helper function converts the given String into an integer used for TaskList indexing.
     *
     * @param index The String representing a numeric index.
     * @return The parsed integer index, or -1 if parsing fails.
     */
    private static int stringToIndex(String index) { // Code reuse
        try {
            return Integer.parseInt(index);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }
}