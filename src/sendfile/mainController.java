package sendfile;

import com.sun.prism.Image;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.MotionBlur;
import javafx.scene.effect.Shadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javax.mail.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javafx.scene.paint.Color;
import sendfile.sendMessageController;

import static javafx.scene.paint.Color.GRAY;

public class mainController {

    @FXML
    private Button btn_COMPOSE;
    @FXML
    private Button btn_INBOX;
    @FXML
    private Button btn_SENT;
    @FXML
    private Button btn_DRAFTS;

    @FXML
    private VBox messageListViewParent;
    @FXML
    public ImageView imgCompose;
    @FXML
    public Button composeButton;
    public AnchorPane showCompose;
    @FXML
    public VBox manageMessage;
    @FXML
    public ScrollPane manageScrollPane;

    public void showComposeScreen() throws IOException {
        FXMLLoader fXMLLoader;
        Parent root = FXMLLoader.load(this.getClass().getResource("sendMessage.fxml"));
        showCompose.getChildren().add(root);
    }
}
