package shadowbuddy.app;

import java.util.Scanner;

import shadowbuddy.taskmodels.Task;
import shadowbuddy.services.TaskList;

public class Shadow {
    protected static final TaskList taskList = new TaskList();

    public static void main(String[] args) {
        System.out.println("Hi, I'm Shadow, your personal assistant!");
        System.out.println("What can I help you with today?\n");

        Scanner inputScanner = new Scanner(System.in);
        while (inputScanner.hasNextLine()) {
            String userInput = inputScanner.nextLine();

            if (userInput.equalsIgnoreCase("bye")) {
                break;
            } else if (userInput.equalsIgnoreCase("list")) {
                System.out.println("\nHere are the tasks in your list:\n" + taskList);
            } else if (userInput.split(" ").length == 2) {
                if (userInput.split(" ")[0].equalsIgnoreCase("mark")) {
                    try {
                        int taskIndex = Integer.parseInt(userInput.split(" ")[1]);
                        taskList.markTask(taskIndex);
                        System.out.println("\nNice! I've marked this task as done:\n " + taskList.getTask(taskIndex));
                    } catch (NumberFormatException e) {
                        System.out.println("Require number after mark command!\n");
                    }
                } else if (userInput.split(" ")[0].equalsIgnoreCase("unmark")) {
                    try {
                        int taskIndex = Integer.parseInt(userInput.split(" ")[1]);
                        taskList.unmarkTask(taskIndex);
                        System.out.println("\nOK, I've marked this task as not done yet:\n " + taskList.getTask(taskIndex));
                    } catch (NumberFormatException e) {
                        System.out.println("Require number after markAsNotDone command!\n");
                    }
                } else {
                    Task userTask = new Task(userInput);
                    taskList.addTask(userTask);
                    System.out.println("\nadded: " + userInput + "\n");
                }
            } else {
                Task userTask = new Task(userInput);
                taskList.addTask(userTask);
                System.out.println("\nadded: " + userInput + "\n");
            }
        }
        System.out.println("\nGoodbye! I'll be here if you need anything else!");
        inputScanner.close();
    }
}