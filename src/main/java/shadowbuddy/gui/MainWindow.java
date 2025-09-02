package shadowbuddy.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import shadowbuddy.app.Shadow;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    /** Shadow chatbot instance used to generate responses */
    private Shadow shadow;
    /** Avatar profile image displayed for the user */
    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/User.jpg"));
    /** Avatar profile image displayed for the chatbot */
    private final Image chatbotImage = new Image(this.getClass().getResourceAsStream("/images/Shadow.jpg"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the Shadow instance and displays the chatbot's greeting in a default dialog container.
     */
    public void setShadow(Shadow shadow) {
        this.shadow = shadow;
        dialogContainer.getChildren().addAll(
                DialogBox.getShadowDialog(shadow.greetUsers(), chatbotImage)
        );
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing
     * Shadow's reply and then appends them to the dialog container.
     * Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = shadow.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getShadowDialog(response, chatbotImage)
        );
        userInput.clear();
    }
}
