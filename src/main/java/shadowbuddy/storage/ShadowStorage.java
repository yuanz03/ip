package shadowbuddy.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import shadowbuddy.taskmodels.TaskList;
import shadowbuddy.taskmodels.Deadline;
import shadowbuddy.taskmodels.Event;
import shadowbuddy.taskmodels.Task;
import shadowbuddy.taskmodels.Todo;

public class ShadowStorage {
    protected final String filePath;
    protected final File databaseFile;

    public ShadowStorage(String filePath) {
        this.filePath = filePath;
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
        System.out.println();
    }

    public void writeToDatabase(TaskList taskList) throws IOException {
        FileWriter taskWriter = new FileWriter(this.filePath);
        for (int i = 0; i < taskList.getLength(); i++) {
            Task task = taskList.getTask(i + 1);
            taskWriter.write(formatTask(task) + "\n");
        }
        taskWriter.close();
    }

    public void loadDatabase(TaskList taskList) throws FileNotFoundException {
        if (!this.databaseFile.exists()) {
            return;
        }

        Scanner fileScanner = new Scanner(this.databaseFile);
        while (fileScanner.hasNextLine()) {
            String[] taskData = fileScanner.nextLine().split("\\|"); // code reuse
            for (int i = 0; i < taskData.length; i++) {
                taskData[i] = taskData[i].trim();
            }
            Task currentTask = getTask(taskData);
            taskList.addTask(currentTask);
        }
    }

    private Task getTask(String[] taskData) {
        String taskType = taskData[0];
        boolean isTaskDone = taskData[1].equals("1");
        String taskDescription = taskData[2];
        Task currentTask = new Task("");

        switch (taskType) {
        case "T":
            currentTask = new Todo(taskDescription);
            break;
        case "D":
            currentTask = new Deadline(taskDescription, taskData[3]);
            break;
        case "E":
            String[] eventTimings = taskData[3].split("-");
            currentTask = new Event(taskDescription, eventTimings[0], eventTimings[1]);
            break;
        default:
            break;
        }

        if (isTaskDone) {
            currentTask.markAsDone();
        }
        return currentTask;
    }

    private String formatTask(Task task) {
        String taskStatus = (task.getStatusIcon().equalsIgnoreCase("X")) ? "1" : "0";

        if (task instanceof Todo todo) { // code reuse
            return "T | " + taskStatus + " | " + todo.getDescription();
        } else if (task instanceof Deadline deadline) {
            return "D | " + taskStatus + " | " + deadline.getDescription() + " | " + deadline.getDueDate();
        } else if (task instanceof Event event) {
            return "E | " + taskStatus + " | " + event.getDescription() +  " | " + event.getStartDate()
                    + "-" + event.getEndDate();
        } else {
            return "";
        }
    }
}