package shadowbuddy.gui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents a dialog box consisting of an ImageView to represent the speaker's face
 * and a label containing text from the speaker.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    /**
     * Initializes a DialogBox instance with the given message text and avatar image.
     *
     * @param message The content of the chatbot or user response.
     * @param avatar The profile image representing the chatbot or user.
     */
    private DialogBox(String message, Image avatar) {
        try {
            FXMLLoader loader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(message);
        displayPicture.setImage(avatar);
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
        dialog.getStyleClass().add("reply-label");
    }

    /**
     * Creates a DialogBox instance that represents the user.
     *
     * @param message The content of the user response.
     * @param avatar The profile image representing the user.
     * @return A DialogBox configured for the user displayed on the right.
     */
    public static DialogBox getUserDialog(String message, Image avatar) {
        return new DialogBox(message, avatar);
    }

    /**
     * Creates a DialogBox instance that represents the chatbot.
     *
     * @param message The content of the chatbot response.
     * @param avatar The profile image representing the chatbot.
     * @return A DialogBox configured for the chatbot displayed on the left.
     */
    public static DialogBox getShadowDialog(String message, Image avatar) {
        var shadowDialog = new DialogBox(message, avatar);
        shadowDialog.flip();
        return shadowDialog;
    }
}
