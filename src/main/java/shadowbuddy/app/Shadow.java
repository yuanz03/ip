package shadowbuddy.app;

import java.io.IOException;
import java.util.Scanner;

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
    public void run() {
        chatbotUi.greetUsers();

        Scanner inputScanner = new Scanner(System.in);
        while (inputScanner.hasNextLine()) {
            String userInput = inputScanner.nextLine();

            if (userInput.equalsIgnoreCase("bye")) {
                break;
            }

            try {
                ShadowCommand userCommand = chatbotController.handleInput(userInput);
                chatbotController.executeCommand(userCommand, chatbotUi);
                chatbotController.writeToDatabase();
            } catch (ShadowException | IOException exception) {
                System.out.println(exception.getMessage());
            }
        }

        chatbotUi.sayGoodbye();
        inputScanner.close();
    }

    /**
     * Provides the main entry point of the Shadow chatbot application, using a predefined storage file path.
     */
    public static void main(String[] args) {
        new Shadow("./data/database.txt").run();
    }

    public String getResponse(String input) {
        return "Duke heard: " + input;
    }
}
