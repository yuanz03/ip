package shadowbuddy.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Shadow {
    public static void main(String[] args) {
        System.out.println("Hi, I'm Shadow, your personal assistant!");
        System.out.println("What can I help you with today?\n");

        Scanner userInputScanner = new Scanner(System.in);
        String filePath = "./shadowbuddy/app/database.txt";

        try {
            printFileContents(filePath);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        ShadowController chatbotController = new ShadowController();

        while (userInputScanner.hasNextLine()) {
            String userInput = userInputScanner.nextLine();

            if (userInput.equalsIgnoreCase("bye")) {
                break;
            }
            chatbotController.run(userInput);
        }

        System.out.println("\nGoodbye! I'll be here if you need anything else!");
        userInputScanner.close();
    }

    private static void printFileContents(String filePath) throws FileNotFoundException {
        File f = new File(filePath);
        Scanner fileScanner = new Scanner(f);
        while (fileScanner.hasNextLine()) {
            System.out.println(fileScanner.nextLine());
        }
    }
}