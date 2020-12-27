package sendfile;

import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import javax.mail.*;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class mainController implements Initializable {
    private ObservableList<FormatMessage> messageObservableList;
    @FXML
    private ListView<FormatMessage> listMessageViewParent;

    public mainController() throws NoSuchProviderException {
        Folder folder = null;
        Store store = null;
        try{
            Properties props = new Properties();
            props.setProperty("mail.store.protocol", "imaps");

            Session session = Session.getDefaultInstance(props, null);
            store = session.getStore("imaps");
            store.connect("imap.gmail.com", MailConfig.APP_EMAIL, MailConfig.APP_PASSWORD);
            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);
            Message[] messages = folder.getMessages();
            messageObservableList = FXCollections.observableArrayList();
            for (int i = 0; i < messages.length; ++i){
                Message msg = messages[i];
                String from = "Unknow";
                if (msg.getReplyTo().length >= 1){
                    from = msg.getReplyTo()[0].toString();
                }
                else if (msg.getFrom().length >= 1){
                    from = msg.getFrom()[0].toString();
                }
                String subject = msg.getSubject();
//                String content = msg.getContent().toString();
                messageObservableList.add(new FormatMessage(from,subject));
            }

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb){
        listMessageViewParent.setItems(messageObservableList);
        listMessageViewParent.setCellFactory(listMessageView -> new messageListViewCell());
    }

//    public String dumpMessage(final Message message) throws IOException, MessagingException{
//        final Object content = message.getContent();
//        if(String.class.isInstance(content)){
//            if (message.isMimeType("text/plain")) return (String)content;
//            else if(message.isMimeType("text/html")) return GenericTools
//        }
    }



