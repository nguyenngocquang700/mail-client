package sendfile;

import com.sun.prism.Image;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.*;
import javax.mail.*;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

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

    public static void receiveListMessages()throws MessagingException, IOException{
        Store store = null;
        Folder folder = null;
        try{
            Properties props = new Properties();
            props.setProperty("mail.store.protocol", "imaps");
            Session session = Session.getDefaultInstance(props, null);
            store = session.getStore("imaps");
            store.connect("imap.gmail.com", MailConfig.APP_EMAIL, MailConfig.APP_PASSWORD);
            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);
            Message[] message = folder.getMessages();
            ListView<String> listMessages = new ListView<String>();
            ObservableList<String> data = FXCollections.observableArrayList();
            for (Message value : message) {
                data.addAll(value.toString());
            }
//            messageListViewParent.getChildren().addAll(listMessages);
            VBox.setVgrow(listMessages, Priority.ALWAYS);

            listMessages.setItems(data);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
