package shadowbuddy.app;

import java.io.IOException;

import shadowbuddy.services.ShadowException;
import shadowbuddy.storage.ShadowStorage;

import javafx.application.Platform;

/**
 * Coordinates the Shadow chatbot's UI, controller, command parser and storage.
 * The Shadow class is composed of the ShadowUi, ShadowController, ShadowParser, and ShadowStorage components.
 * It also provides methods to integrate with the GUI, manage the database, and handle UI interactions.
 */
public class Shadow {
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
     * Returns a welcome message for users along with the existing tasks stored in the storage.
     * The storage sets up the task database (creating it if needed), and then outputs it.
     * The controller is responsible for loading the database into the TaskList.
     *
     * @return The combined greeting message and the contents of the task storage.
     */
    public String greetUsers() {
        String greeting = chatbotUi.greetUsers();
        try {
            taskStorage.createDatabase();
            chatbotController.loadDatabase();
            return greeting + "\n" + taskStorage.outputDatabase();
        } catch (IOException exception) {
            return exception.getMessage();
        }
    }

    /**
     * Processes user input and returns the Shadow chatbot's response.
     * If the user inputs "bye", a Thread is created that waits for one second before terminating the
     * JavaFX application. This short delay ensures the chatbot has time to display its goodbye message.
     * Each recognized command is handled and executed by the controller.
     * The resulting output is both displayed to the user and saved to storage.
     *
     * @param userInput The raw input string provided by the user for processing.
     * @return The chatbot's textual response, generated from executing the user's command.
     */
    public String getResponse(String userInput) {
        if (userInput.equalsIgnoreCase("bye")) {
            new Thread(() -> { // code reuse
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
                Platform.exit();
            }).start();

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
}
