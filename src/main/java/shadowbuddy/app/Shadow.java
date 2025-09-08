package shadowbuddy.app;

import java.io.IOException;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.util.Duration;
import shadowbuddy.services.ShadowException;
import shadowbuddy.storage.ShadowStorage;

/**
 * Coordinates the Shadow chatbot's UI, controller, command parser and storage.
 * The Shadow class is composed of the ShadowUi, ShadowController, ShadowParser, and ShadowStorage components.
 * It also provides methods to integrate with the GUI, manage the database, and handle UI interactions.
 */
public class Shadow {
    private static final int GOODBYE_DELAY = 1000;
    private final ShadowUi chatbotUi;
    private final ShadowController chatbotController;
    private final ShadowStorage taskStorage;

    /**
     * Initializes a Shadow instance with the given file path.
     * The constructor also initializes the storage, controller, and UI.
     *
     * @param filePath The file path to the task list database file.
     */
    public Shadow(String filePath) {
        taskStorage = new ShadowStorage(filePath);
        chatbotController = new ShadowController(taskStorage);
        chatbotUi = new ShadowUi();
    }

    /**
     * Starts a chatbot session by greeting the user and displaying the existing tasks stored in the storage.
     * Delegates database setup and retrieval of tasks to a helper method.
     *
     * @return The greeting message combined with the output of the database.
     */
    public String startShadowSession() {
        String greeting = chatbotUi.greetUser();
        try {
            String databaseOutput = prepareDatabaseOutput();
            return greeting + "\n" + databaseOutput;
        } catch (IOException exception) {
            return "Oops! Something went wrong when starting the chatbot session: " + exception.getMessage();
        }
    }

    /**
     * Processes user input and returns the Shadow chatbot's response.
     * If the user inputs "bye", the chatbot schedules a short delay before terminating the
     * JavaFX application. This ensures the chatbot has time to display its goodbye message.
     * Each recognized command is handled and executed by the controller.
     * The resulting output is both displayed to the user and saved to storage.
     *
     * @param userInput The raw input string provided by the user for processing.
     * @return The chatbot's textual response, generated from executing the user's command.
     */
    public String getResponse(String userInput) {
        if (userInput.equalsIgnoreCase("bye")) { // code reuse (initial was thread)
            PauseTransition exitDelay = new PauseTransition(Duration.millis(GOODBYE_DELAY));
            exitDelay.setOnFinished(event -> Platform.exit());
            exitDelay.play();
            return chatbotUi.sayGoodbye();
        }

        try {
            ShadowCommand userCommand = chatbotController.handleInput(userInput);
            String chatbotOutput = chatbotController.executeCommand(userCommand, chatbotUi);
            chatbotController.writeToDatabase();
            return chatbotOutput;
        } catch (ShadowException | IOException exception) {
            return exception.getMessage();
        }
    }

    /**
     * Prepares and returns the tasks stored in the database.
     * This helper function sets up the task database (creating it if needed),
     * loads it into the TaskList, and then outputs it.
     *
     * @return The confirmation message combined with the existing tasks in the database.
     * @throws IOException If the database file cannot be created, found or read.
     */
    private String prepareDatabaseOutput() throws IOException {
        String confirmationMessage = taskStorage.createDatabase();
        chatbotController.loadDatabase();
        return confirmationMessage + "\n" + taskStorage.outputDatabase();
    }
}
