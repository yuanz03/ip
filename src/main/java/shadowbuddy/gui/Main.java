package shadowbuddy.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import shadowbuddy.app.Shadow;

/**
 * Initializes the core application state and loads the main window UI.
 */
public class Main extends Application {
    /** Shadow instance initialized with a predefined storage file path */
    private final Shadow shadow = new Shadow("./data/database.txt");

    /**
     * Starts the JavaFX application by loading the main FXML and injecting the Shadow object into its controller.
     *
     * @param stage The primary stage for this application, onto which the application scene can be set.
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Shadow");
            stage.setMinHeight(220);
            stage.setMinWidth(417);
            loader.<MainWindow>getController().setShadow(shadow);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
