package sendfile;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javax.mail.*;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;


public class mainController {
    private ObservableList<FormatMessage> messageObservableList;
    @FXML
    private ListView<FormatMessage> listMessageViewParent;
    @FXML
    public Button composeButton;
    public AnchorPane showCompose;
    Folder createFolder() throws MessagingException {
        Folder folder=null;
        Store store=null;
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        Session session = Session.getDefaultInstance(props, null);
        store = session.getStore("imaps");
        store.connect("imap.gmail.com", MailConfig.APP_EMAIL, MailConfig.APP_PASSWORD);
        folder = store.getFolder("INBOX");
        folder.open(Folder.READ_ONLY);
        return folder;
    }
    public mainController() throws NoSuchProviderException {
        try{
            Folder folder=createFolder();
            Message[] messages = folder.getMessages();
            messageObservableList = FXCollections.observableArrayList();
            for (int i = 0; i < messages.length; ++i){
                Message msg = messages[i];
                String from = "";
                if (msg.getReplyTo().length >= 1){
                    from = msg.getReplyTo()[0].toString();
                }
                else if (msg.getFrom().length >= 1){
                    from = msg.getFrom()[0].toString();
                }
                String subject = msg.getSubject();
                messageObservableList.add(new FormatMessage(from,subject));
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    public void showInbox(){
        listMessageViewParent.setItems(messageObservableList);
        listMessageViewParent.setCellFactory(listMessageView -> new messageListViewCell());
    }
    public void showComposeScreen() throws IOException {
        FXMLLoader fXMLLoader;
        Parent root = FXMLLoader.load(this.getClass().getResource("sendMessage.fxml"));
        showCompose.getChildren().add(root);
    }
}



