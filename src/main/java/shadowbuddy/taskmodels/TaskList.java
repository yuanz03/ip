package shadowbuddy.taskmodels;

import java.util.ArrayList;

public class TaskList {
    protected final ArrayList<Task> storage;

    public TaskList() {
        this.storage = new ArrayList<>(100);
    }

    public void addTask(Task task) {
        this.storage.add(task);
    }

    public Task deleteTask(int index) {
        return this.storage.remove(index - 1);
    }

    public void markTask(int index) {
        this.storage.get(index - 1).markAsDone();
    }

    public void unmarkTask(int index) {
       this.storage.get(index - 1).markAsNotDone();
    }

    public Task getTask(int index) {
        return this.storage.get(index - 1);
    }

    public int length() {
        return this.storage.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.storage.size(); i++) { // Code reuse
            sb.append(i + 1).append(". ").append(this.storage.get(i)).append("\n");
        }
        return sb.toString();
    }
}