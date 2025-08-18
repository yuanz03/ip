package shadowbuddy.app;

import java.util.Scanner;

public class Shadow {
    public static void main(String[] args) {
        System.out.println("Hi, I'm Shadow, your personal assistant!");
        System.out.println("What can I help you with today?\n");

        Scanner inputScanner = new Scanner(System.in);
        ShadowController chatbotController = new ShadowController();

        while (inputScanner.hasNextLine()) {
            String userInput = inputScanner.nextLine();

            if (userInput.equalsIgnoreCase("bye")) {
                break;
            }
            chatbotController.run(userInput);
        }

        System.out.println("\nGoodbye! I'll be here if you need anything else!");
        inputScanner.close();
    }
}