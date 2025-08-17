import java.util.Scanner;

public class Shadow {
    public static void main(String[] args) {
        Scanner inputScanner = new Scanner(System.in);
        System.out.println("Hi, I'm Shadow, your personal assistant!\nWhat can I help you with today?\n");

        String userInput = inputScanner.next();
        while (!(userInput.equalsIgnoreCase("bye"))) {
            System.out.println("\n" + userInput + "\n");
            userInput = inputScanner.next();
        }
        System.out.println("\nGoodbye! I'll be here if you need anything else!");
    }
}