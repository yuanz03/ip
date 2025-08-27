package shadowbuddy.app;

import shadowbuddy.services.ShadowException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ShadowParser {
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
            int markIndex = convertStringToIndex(requestDetails);
            return new ShadowCommand(ShadowCommand.CommandType.MARK, markIndex);
        case "unmark":
            int unmarkIndex = convertStringToIndex(requestDetails);
            return new ShadowCommand(ShadowCommand.CommandType.UNMARK, unmarkIndex);
        case "delete":
            int deleteIndex = convertStringToIndex(requestDetails);
            return new ShadowCommand(ShadowCommand.CommandType.DELETE, deleteIndex);
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

    private static String formatTaskDateTime(String timestamp) {
        DateTimeFormatter taskInputFormatter = DateTimeFormatter.ofPattern("d/M/yyyy HHmm"); // code reuse
        DateTimeFormatter taskOutputFormatter = DateTimeFormatter.ofPattern("MMM d yyyy HH:mm");
        LocalDateTime taskTimestamp = LocalDateTime.parse(timestamp, taskInputFormatter);
        return taskTimestamp.format(taskOutputFormatter);
    }

    // Code reuse
    private static int convertStringToIndex(String index) {
        try {
            return Integer.parseInt(index);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }
}