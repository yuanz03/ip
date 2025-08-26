package shadowbuddy.app;

import java.io.IOException;
import java.util.Scanner;

import shadowbuddy.services.ShadowException;
import shadowbuddy.storage.ShadowStorage;

public class Shadow {
    private final ShadowUi chatbotUi;
    private final ShadowController chatbotController;

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

    public static void main(String[] args) {
        new Shadow("./data/database.txt").run();
    }
}