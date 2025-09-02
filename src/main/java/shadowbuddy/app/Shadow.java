package shadowbuddy.app;

import java.io.IOException;

import javafx.application.Platform;
import shadowbuddy.services.ShadowException;
import shadowbuddy.storage.ShadowStorage;

/**
 * Coordinates the Shadow chatbot's UI, controller, command parser and storage.
 * The Shadow class is composed of the ShadowUi, ShadowController, ShadowParser, and ShadowStorage components.
 */
public class Shadow {
    private final ShadowUi chatbotUi;
    private final ShadowController chatbotController;
    private final ShadowStorage taskStorage;

    /**
     * Initializes a Shadow instance with the given file path.
     * The constructor also initializes the storage, controller, and UI.
     * The storage sets up the task database (creating it if needed), and then prints it.
     * The controller then loads the database into the TaskList.
     *
     * @param filePath The file path to the task list database file.
     */
    public Shadow(String filePath) {
        taskStorage = new ShadowStorage(filePath);
        chatbotController = new ShadowController(taskStorage);
        chatbotUi = new ShadowUi();
    }

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
