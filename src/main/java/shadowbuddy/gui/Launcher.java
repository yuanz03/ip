package shadowbuddy.gui;

import javafx.application.Application;

/**
 * A launcher class to workaround classpath issues.
 */
public class Launcher {
    /**
     * Provides the main entry point of the Shadow chatbot application.
     */
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}
