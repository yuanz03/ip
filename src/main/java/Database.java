import java.util.ArrayList;

public class Database {
    private final ArrayList<String> storage;

    public Database() {
        this.storage = new ArrayList<>(100);
    }

    public void addItem(String item) {
        storage.add(item);
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