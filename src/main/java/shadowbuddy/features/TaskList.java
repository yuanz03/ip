package shadowbuddy.features;

import java.util.ArrayList;

public class TaskList {
    protected final ArrayList<Task> storage;

    public TaskList() {
        this.storage = new ArrayList<>(100);
    }

    public void addTask(Task task) {
        storage.add(task);
    }

    public void markTask(int index) {
        storage.get(index - 1).markAsDone();
    }

    public void unmarkTask(int index) {
       storage.get(index - 1).markAsNotDone();
    }

    public Task getTask(int index) {
        return storage.get(index - 1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < storage.size(); i++) {
            sb.append(i + 1).append(". ").append(storage.get(i)).append("\n");
        }
        return sb.toString();
    }
}