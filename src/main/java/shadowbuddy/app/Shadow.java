package shadowbuddy.app;

import java.io.IOException;

import shadowbuddy.services.ShadowException;
import shadowbuddy.storage.ShadowStorage;

/**
 * Coordinates the Shadow chatbot's UI, controller, command parser and storage.
 * The Shadow class is composed of the ShadowUi, ShadowController, ShadowParser, and ShadowStorage components.
 */
public class Shadow {
    private final ShadowUi chatbotUi;
    private final ShadowController chatbotController;

    /**
     * Initializes a Shadow instance with the given file path.
     * The constructor also initializes the storage, controller, and UI.
     * The storage sets up the task database (creating it if needed), and then prints it.
     * The controller then loads the database into the TaskList.
     *
     * @param filePath The file path to the task list database file.
     */
    public Shadow(String filePath) {
        ShadowStorage taskStorage = new ShadowStorage(filePath);
        chatbotController = new ShadowController(taskStorage);
        chatbotUi = new ShadowUi();
        try {
            taskStorage.createDatabase();
            taskStorage.printDatabase();
            chatbotController.loadDatabase();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    /**
     * Executes the main loop which handles user input and commands.
     * This method greets the user and reads user input until it processes "bye".
     * Each recognized command is executed by the controller and the output is written to storage.
     */
    public String getResponse(String userInput) {
        if (userInput.equalsIgnoreCase("bye")) {
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
