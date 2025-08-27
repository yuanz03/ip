package shadowbuddy.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import shadowbuddy.taskmodels.Deadline;
import shadowbuddy.taskmodels.Event;
import shadowbuddy.taskmodels.Task;
import shadowbuddy.taskmodels.TaskList;
import shadowbuddy.taskmodels.Todo;

/**
 * Manages saving, updating, and loading the chatbot's task list from a file.
 * The ShadowStorage class wraps a file path and provides methods to create the database
 * file, print its contents, write a TaskList to it, and load tasks from it.
 */
public class ShadowStorage {
    /** File path to the task list database file */
    protected final String filePath;
    /** File instance representing the task list database file */
    protected final File databaseFile;

    /**
     * Initializes a ShadowStorage instance with the given file path.
     * Creates a ShadowStorage instance that initializes a File instance with the
     * specified file path and uses it to store, write, and retrieve tasks.
     */
    public ShadowStorage(String filePath) {
        this.filePath = filePath;
        this.databaseFile = new File(this.filePath);
    }

    /**
     * Creates the database file and its parent directories if they do not already exist.
     * If any of the parent directories are missing, they will be created.
     * If the database file did not already exist, a new database file will be created.
     * Otherwise, a message is printed to indicate that the existing task list will be displayed.
     *
     * @throws IOException If creating the database file fails.
     */
    public void createDatabase() throws IOException{
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

    /**
     * Prints the tasks stored in the database file to the screen.
     * A Scanner is used to read each line from the database file,
     * and print the tasks line by line to the screen.
     * A trailing blank line is printed after the file contents.
     *
     * @throws FileNotFoundException If the database file cannot be found or read.
     */
    public void printDatabase() throws FileNotFoundException {
        Scanner fileScanner = new Scanner(this.databaseFile);
        while (fileScanner.hasNextLine()) {
            System.out.println(fileScanner.nextLine());
        }
        System.out.println();
    }

    /**
     * Writes the given TaskList to the database file, overwriting any existing contents.
     * A FileWriter instance is created using the filePath field.
     * Each task in the TaskList is formatted with the private helper method formatTask,
     * and written to the database file on a separate line.
     *
     * @param taskList The TaskList whose tasks will be saved to the database file.
     * @throws IOException If the database file cannot be written to.
     */
    public void writeToDatabase(TaskList taskList) throws IOException {
        FileWriter taskWriter = new FileWriter(this.filePath);
        for (int i = 0; i < taskList.getLength(); i++) {
            Task task = taskList.getTask(i + 1);
            taskWriter.write(formatTask(task) + "\n");
        }
        taskWriter.close();
    }

    /**
     * Loads tasks from the database file into the given TaskList.
     * If the database file does not exist, the method will return early.
     * Each line from the database file is split at the "|" symbols, and each segment is
     * trimmed of whitespace. The resulting String data is then converted into a Task instance
     * using the private helper method createTask, and appended to the given task list.
     *
     * @param taskList The TaskList to populate with tasks read from the database file.
     * @throws FileNotFoundException If the database file cannot be found or read.
     */
    public void loadDatabase(TaskList taskList) throws FileNotFoundException {
        if (!this.databaseFile.exists()) {
            return;
        }

        Scanner fileScanner = new Scanner(this.databaseFile);
        while (fileScanner.hasNextLine()) {
            String[] taskDetails = fileScanner.nextLine().split("\\|"); // code reuse
            for (int i = 0; i < taskDetails.length; i++) {
                taskDetails[i] = taskDetails[i].trim();
            }

            Task currentTask = createTask(taskDetails);
            taskList.addTask(currentTask);
        }
    }

    /**
     * Returns a Task created from the given String array of database task data.
     * The first element of the task data array specifies the task type ("T", "D", or "E").
     * The second element represents the task's completion status ("1" for done, "0" otherwise).
     * The remaining elements contain the task description, and, if applicable, the dates and times.
     * Based on the task type, the appropriate Task instance is created.
     * If the task is completed, it is marked as done before being returned.
     *
     * @param taskDetails The String array of task information obtained from the database file.
     * @return A Task representing the given task data.
     */
    private Task createTask(String[] taskDetails) {
        String taskType = taskDetails[0];
        boolean isTaskDone = taskDetails[1].equals("1");
        String taskDescription = taskDetails[2];
        Task currentTask = new Task("");

        switch (taskType) {
        case "T":
            currentTask = new Todo(taskDescription);
            break;
        case "D":
            currentTask = new Deadline(taskDescription, taskDetails[3]);
            break;
        case "E":
            String[] eventTimings = taskDetails[3].split("-");
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

    /**
     * Returns a formatted String representation of the given Task.
     * The format of the returned String representation is:
     * "type | status | description | additional dates and timings (if present)."
     *
     * @param task The specified Task instance to be formatted for storage.
     * @return A single-line String representing the given Task.
     */
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
            return ""; // This code should never be reached
        }
    }
}