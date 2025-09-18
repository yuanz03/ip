package shadowbuddy.app;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import shadowbuddy.services.Messages;
import shadowbuddy.services.ShadowException;

/**
 * Parses raw user input into ShadowCommand objects used by the controller.
 * The ShadowParser class interprets user commands defined in ShadowCommand, validates input,
 * and converts raw timestamps into a standardized format.
 */
public class ShadowParser {
    // Statement below adapted from a ChatGPT example on how to define a strict date format for user input
    private static final String INPUT_DATE_PATTERN = "d/M/yyyy HHmm";
    private static final String OUTPUT_DATE_PATTERN = "MMM d yyyy HH:mm";

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
        assert input != null : "user input should not be null";
        String userInput = input.trim();
        if (userInput.isEmpty()) {
            throw new ShadowException(Messages.PREFIX_EMPTY_COMMAND + Messages.MESSAGE_COMMANDS_GUIDE);
        }

        String[] inputDetails = userInput.split(" ");
        String requestType = inputDetails[0].toLowerCase();
        String requestDetails = inputDetails.length > 1 ? userInput.substring(requestType.length() + 1) : "";
        // Solution below inspired from a ChatGPT example on how to use a switch structure with return statements
        switch (requestType) {
        case "list":
            return new ShadowCommand(ShadowCommand.CommandType.LIST);
        case "mark":
            int markIndex = convertStringToIndex(requestDetails);
            return new ShadowCommand(ShadowCommand.CommandType.MARK, markIndex);
        case "unmark":
            int unmarkIndex = convertStringToIndex(requestDetails);
            return new ShadowCommand(ShadowCommand.CommandType.UNMARK, unmarkIndex);
        case "delete":
            int deleteIndex = convertStringToIndex(requestDetails);
            return new ShadowCommand(ShadowCommand.CommandType.DELETE, deleteIndex);
        case "find":
            validateSingleKeyword(inputDetails.length - 1, requestDetails);
            return new ShadowCommand(ShadowCommand.CommandType.FIND, requestDetails);
        case "todo":
            validateNonEmptyRequest(requestDetails, "todo");
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
     * @throws ShadowException If the requestDetails is syntactically invalid.
     */
    private static ShadowCommand parseDeadline(String requestDetails) throws ShadowException {
        assert requestDetails != null : "deadline requestDetails should not be null";
        validateNonEmptyRequest(requestDetails, "deadline");
        validateUniqueMarkerPresence(requestDetails, "/by", Messages.MESSAGE_DEADLINE_FORMAT);

        String[] deadlineDetails = requestDetails.split("/by");
        validateNonEmptyDate(deadlineDetails, 1, "due", Messages.MESSAGE_DEADLINE_FORMAT);

        try {
            String formattedDueDate = validateAndFormatDateRange(deadlineDetails[1].trim())[0];
            return new ShadowCommand(ShadowCommand.CommandType.DEADLINE, deadlineDetails[0].trim(), formattedDueDate);
        } catch (DateTimeParseException exception) {
            throw new ShadowException(Messages.MESSAGE_INVALID_DEADLINE_DATE + Messages.MESSAGE_DEADLINE_FORMAT);
        }
    }

    /**
     * Parses Event command details and returns the corresponding ShadowCommand instance.
     * Expects the command details to include a task description, the "/from" and "/to" keywords,
     * a start date in the format "d/M/yyyy HHmm", and an end date in the format "d/M/yyyy HHmm".
     *
     * @param requestDetails The trailing input after the event keyword.
     * @return A ShadowCommand instance representing the Event details.
     * @throws ShadowException If the requestDetails is syntactically invalid.
     */
    private static ShadowCommand parseEvent(String requestDetails) throws ShadowException {
        assert requestDetails != null : "event requestDetails should not be null";
        validateNonEmptyRequest(requestDetails, "event");
        validateUniqueMarkerPresence(requestDetails, "/from", Messages.MESSAGE_EVENT_FORMAT);
        validateUniqueMarkerPresence(requestDetails, "/to", Messages.MESSAGE_EVENT_FORMAT);

        String[] eventDetails = requestDetails.split("/from");
        String[] eventTimings = eventDetails[1].split("/to");
        validateNonEmptyDate(eventTimings, 0, "start", Messages.MESSAGE_EVENT_FORMAT);
        validateNonEmptyDate(eventTimings, 1, "end", Messages.MESSAGE_EVENT_FORMAT);

        try {
            String[] formattedDates = validateAndFormatDateRange(eventTimings[0].trim(), eventTimings[1].trim());
            return new ShadowCommand(ShadowCommand.CommandType.EVENT, eventDetails[0].trim(), formattedDates[0],
                    formattedDates[1]);
        } catch (DateTimeParseException exception) {
            throw new ShadowException(Messages.MESSAGE_INVALID_EVENT_DATE + Messages.MESSAGE_EVENT_FORMAT);
        }
    }

    /**
     * Validates that exactly one keyword has been supplied for the FIND command.
     *
     * @param keywordCount The number of keywords in the FIND command.
     * @param details The trailing input after the find keyword.
     * @throws ShadowException If the keyword is missing or if more than one keyword is provided.
     */
    private static void validateSingleKeyword(int keywordCount, String details) throws ShadowException {
        if (details.isEmpty()) {
            throw new ShadowException(Messages.PREFIX_UNKNOWN_COMMAND + Messages.MESSAGE_NO_KEYWORD);
        }

        if (keywordCount > 1) {
            throw new ShadowException(Messages.PREFIX_UNKNOWN_COMMAND + Messages.MESSAGE_MULTIPLE_KEYWORDS);
        }
    }

    /**
     * Validates that a non-empty description exists for TODO, DEADLINE, and EVENT commands.
     *
     * @param details The trailing input after the todo, deadline, or event keyword.
     * @param taskType The type of the task being validated.
     * @throws ShadowException If the task description is empty.
     */
    private static void validateNonEmptyRequest(String details, String taskType) throws ShadowException {
        if (details.isEmpty()) {
            throw new ShadowException(Messages.PREFIX_UNKNOWN_COMMAND
                    + String.format(Messages.MESSAGE_EMPTY_TASK_DESCRIPTION, taskType));
        }
    }

    /**
     * Validates that the given marker appears exactly once in the task details for DEADLINE and EVENT commands.
     *
     * @param details The trailing input after the deadline or event keyword.
     * @param marker The date marker of the task ("/by", "/from", "/to").
     * @param msg The error message to include in the exception thrown when the marker is missing or duplicated.
     * @throws ShadowException If the marker is not present or appears more than once.
     */
    private static void validateUniqueMarkerPresence(String details, String marker, String msg) throws ShadowException {
        if (!details.contains(marker)) {
            throw new ShadowException(String.format(Messages.MESSAGE_INVALID_MARKER_FORMAT, msg));
        }

        if (details.indexOf(marker) != details.lastIndexOf(marker)) {
            throw new ShadowException(String.format(Messages.MESSAGE_DUPLICATE_MARKERS, marker) + msg);
        }
    }

    /**
     * Validates that the appropriate date exists for DEADLINE and EVENT commands.
     *
     * @param data The parts of the task details separated by the date markers.
     * @param index The position in the array where the appropriate date should be found.
     * @param type The type of the date being validated.
     * @param msg The error message to include in the exception thrown when the date is missing.
     * @throws ShadowException If the expected date is missing.
     */
    private static void validateNonEmptyDate(String[] data, int index, String type, String msg) throws ShadowException {
        if (data.length <= index || data[index].trim().isEmpty()) {
            throw new ShadowException(String.format(Messages.MESSAGE_EMPTY_TASK_DATE, type) + msg);
        }
    }

    /**
     * Returns a String array containing the formatted timestamp(s) for the given input timestamp(s).
     * Array length is 1 for a single deadline due date, or 2 for an event start and end date.
     * If given two timestamps, validate that they form a valid chronological range.
     * This helper function converts the given timestamp using the DateTimeFormatter class.
     *
     * @param timestamps One or two timestamps in "d/M/yyyy HHmm" format.
     * @return An array of formatted timestamps in "MMM d yyyy HH:mm" format.
     * @throws ShadowException If two timestamps are supplied and the end date is before the start date.
     */
    // Solution below inspired from a ChatGPT example on how to use varargs and return a String array
    private static String[] validateAndFormatDateRange(String... timestamps) throws ShadowException {
        assert timestamps != null : "timestamps should not be null";
        DateTimeFormatter taskInputFormatter = DateTimeFormatter.ofPattern(INPUT_DATE_PATTERN);
        DateTimeFormatter taskOutputFormatter = DateTimeFormatter.ofPattern(OUTPUT_DATE_PATTERN);

        if (timestamps.length == 1) {
            LocalDateTime dueDate = LocalDateTime.parse(timestamps[0], taskInputFormatter);
            return new String[] { dueDate.format(taskOutputFormatter) };
        } else if (timestamps.length == 2) {
            LocalDateTime startDate = LocalDateTime.parse(timestamps[0], taskInputFormatter);
            LocalDateTime endDate = LocalDateTime.parse(timestamps[1], taskInputFormatter);
            if (endDate.isBefore(startDate)) {
                throw new ShadowException(Messages.PREFIX_UNKNOWN_COMMAND + Messages.MESSAGE_INVALID_DATE_RANGE);
            }
            return new String[] { startDate.format(taskOutputFormatter), endDate.format(taskOutputFormatter) };
        } else {
            throw new IllegalArgumentException(Messages.MESSAGE_INVALID_TIMESTAMP_ARGUMENT_COUNT);
        }
    }

    /**
     * Returns an integer corresponding to the given numeric String.
     * This helper function converts the given String into an integer used for TaskList indexing.
     *
     * @param index The String representing a numeric index.
     * @return The parsed integer index.
     * @throws ShadowException If the given String is not a valid integer representation.
     */
    private static int convertStringToIndex(String index) throws ShadowException {
        // Solution below inspired from a ChatGPT example on how to use parseInt to convert Strings to numbers
        try {
            return Integer.parseInt(index);
        } catch (NumberFormatException exception) {
            throw new ShadowException(Messages.PREFIX_UNKNOWN_COMMAND + Messages.MESSAGE_INVALID_TASK_INDEX);
        }
    }
}
