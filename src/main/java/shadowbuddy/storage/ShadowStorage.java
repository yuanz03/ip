package shadowbuddy.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import shadowbuddy.services.TaskList;
import shadowbuddy.taskmodels.Deadline;
import shadowbuddy.taskmodels.Event;
import shadowbuddy.taskmodels.Task;
import shadowbuddy.taskmodels.Todo;

public class ShadowStorage {
    protected final String filePath;
    protected final File databaseFile;

    public ShadowStorage() {
        this.filePath = "shadowbuddy/storage/database.txt";
        this.databaseFile = new File(this.filePath);
    }

    public void createDatabase() throws IOException {
        File parentFile = this.databaseFile.getParentFile();

        if (parentFile != null && !parentFile.exists()) { // code reuse
            //noinspection ResultOfMethodCallIgnored
            parentFile.mkdirs();
            System.out.println("The relevant folder did not exist! It has now been created for you.\n");
        }

        if (this.databaseFile.createNewFile()) {
            System.out.println("No task list found! A new one has been created for you.\n");
        } else {
            System.out.println("Here is your current task list: ");
        }
    }

    public void printDatabase() throws FileNotFoundException {
        Scanner fileScanner = new Scanner(this.databaseFile);
        while (fileScanner.hasNextLine()) {
            System.out.println(fileScanner.nextLine());
        }
    }

    public void writeToDatabase(TaskList taskList) throws IOException {
        FileWriter taskWriter = new FileWriter(this.filePath);
        for (int i = 0; i < taskList.length(); i++) {
            Task task = taskList.getTask(i + 1);
            taskWriter.write(formatTask(task) + "\n");
        }
        taskWriter.close();
    }

    private String formatTask(Task task) {
        String taskStatus = (task.getStatusIcon().equalsIgnoreCase("X")) ? "1" : "0";

        if (task instanceof Todo todo) { // code reuse
            return "T | " + taskStatus + " | " + todo.getDescription();
        } else if (task instanceof Deadline deadline) {
            return "D | " + taskStatus + " | " + deadline.getDescription() + " | " + deadline.getDueDate();
        } else if (task instanceof Event event) {
            return "E | " + taskStatus + " | " + event.getDescription() +  " | " + event.getStartTime()
                    + "-" + event.getEndTime();
        } else {
            return "";
        }
    }
}