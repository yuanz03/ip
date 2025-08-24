package shadowbuddy.app;

import shadowbuddy.services.ShadowException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
            int markIndex = stringToIndex(requestDetails);
            return new ShadowCommand(ShadowCommand.CommandType.MARK, markIndex);
        case "unmark":
            int unmarkIndex = stringToIndex(requestDetails);
            return new ShadowCommand(ShadowCommand.CommandType.UNMARK, unmarkIndex);
        case "delete":
            int deleteIndex = stringToIndex(requestDetails);
            return new ShadowCommand(ShadowCommand.CommandType.DELETE, deleteIndex);
        case "todo":
            if (requestDetails.isEmpty()) {
                throw new ShadowException("Invalid request! Please provide a description for your todo.\n");
            }
            return new ShadowCommand(ShadowCommand.CommandType.TODO, requestDetails);
        case "deadline":
            if (requestDetails.isEmpty()) {
                throw new ShadowException("Invalid request! Please provide a description for your deadline.\n");
            }

            String[] deadlineDetails = requestDetails.split(" /by ");
            String formattedDueDate = formatTaskDateTime(deadlineDetails[1]);
            return new ShadowCommand(ShadowCommand.CommandType.DEADLINE, deadlineDetails[0], formattedDueDate);
        case "event":
            if (requestDetails.isEmpty()) {
                throw new ShadowException("Invalid request! Please provide a description for your event.\n");
            }

            String[] eventDetails = requestDetails.split(" /from ");
            String[] eventTimings = eventDetails[1].split(" /to ");
            String formattedStartDate = formatTaskDateTime(eventTimings[0]);
            String formattedEndDate = formatTaskDateTime(eventTimings[1]);
            return new ShadowCommand(ShadowCommand.CommandType.EVENT, eventDetails[0], formattedStartDate,
                    formattedEndDate);
        default:
            return new ShadowCommand(ShadowCommand.CommandType.UNKNOWN);
        }
    }

    private static String formatTaskDateTime(String timestamp) {
        DateTimeFormatter taskInputFormatter = DateTimeFormatter.ofPattern("d/M/yyyy HHmm"); // code reuse
        DateTimeFormatter taskOutputFormatter = DateTimeFormatter.ofPattern("MMM d yyyy HH:mm");
        LocalDateTime taskTimestamp = LocalDateTime.parse(timestamp, taskInputFormatter);
        return taskTimestamp.format(taskOutputFormatter);
    }

    // Code reuse
    private static int stringToIndex(String index) {
        try {
            return Integer.parseInt(index);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }
}