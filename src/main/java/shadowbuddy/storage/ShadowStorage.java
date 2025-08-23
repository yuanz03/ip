package shadowbuddy.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

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

    public void writeToDatabase(String taskList) throws IOException {
        FileWriter taskWriter = new FileWriter(this.filePath, true);
        taskWriter.write(taskList);
        taskWriter.close();
    }

    public void printDatabase() throws FileNotFoundException {
        Scanner fileScanner = new Scanner(this.databaseFile);
        while (fileScanner.hasNextLine()) {
            System.out.println(fileScanner.nextLine());
        }
    }
}