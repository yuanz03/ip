import java.util.Scanner;

public class Shadow {
    private static final Database database = new Database();

    public static void main(String[] args) {
        System.out.println("Hi, I'm Shadow, your personal assistant!");
        System.out.println("What can I help you with today?\n");

        Scanner inputScanner = new Scanner(System.in);
        while (inputScanner.hasNextLine()) {
            String userInput = inputScanner.nextLine();

            if (userInput.equalsIgnoreCase("bye")) {
                break;
            } else if (userInput.equalsIgnoreCase("list")) {
                System.out.println("\n" + database);
            } else {
                database.addItem(userInput);
                System.out.println("\nadded: " + userInput + "\n");
            }
        }
        System.out.println("\nGoodbye! I'll be here if you need anything else!");
    }
}