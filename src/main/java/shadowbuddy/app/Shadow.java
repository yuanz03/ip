package shadowbuddy.app;

import java.io.IOException;
import java.util.Scanner;

import shadowbuddy.storage.ShadowStorage;

public class Shadow {
    private final ShadowUi chatbotUi;

    public Shadow(String filePath) {
        ShadowStorage taskStorage = new ShadowStorage(filePath);
        chatbotUi = new ShadowUi(taskStorage);
        try {
            taskStorage.createDatabase();
            taskStorage.printDatabase();
            chatbotUi.loadDatabase();
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
            chatbotUi.handleInput(userInput);
        }

        chatbotUi.sayGoodbye();
        inputScanner.close();
    }

    public static void main(String[] args) {
        new Shadow("./data/database.txt").run();
    }
}