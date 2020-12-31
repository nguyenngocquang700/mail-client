package sendfile;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import javax.mail.*;
import java.io.IOException;
import java.util.Properties;

public class mainController  {//implements Initializable
    private ObservableList<FormatMessage> messageObservableList;
    Folder folder = null;
    Store store = null;
    @FXML
    private ListView<FormatMessage> listMessageViewParent;
    @FXML
    public Button composeButton;
    public AnchorPane showCompose;



    Session createSession(String folderName) throws MessagingException {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");

        Session session = Session.getDefaultInstance(props, null);
        store = session.getStore("imaps");
        folder = store.getFolder(folderName);
        folder.open(Folder.READ_ONLY);
        return session;
    }

    public void setInboxMessageListView() throws NoSuchProviderException {
        try {
            Properties props = new Properties();
            props.setProperty("mail.store.protocol", "imaps");

            Session session = Session.getDefaultInstance(props, null);
            store = session.getStore("imaps");
            store.connect("imap.gmail.com", MailConfig.APP_EMAIL, MailConfig.APP_PASSWORD);
            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);
            Message[] messages = folder.getMessages();
            messageObservableList = FXCollections.observableArrayList();
            for (int i = 0; i < messages.length; ++i) {
                Message msg = messages[i];
                String from = "Unknow";
                if (msg.getReplyTo().length >= 1) {
                    from = msg.getReplyTo()[0].toString();
                } else if (msg.getFrom().length >= 1) {
                    from = msg.getFrom()[0].toString();
                }
                String subject = msg.getSubject();
                messageObservableList.add(new FormatMessage(from, subject));
            }

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    public void setSentMessagesListView() throws NoSuchProviderException {
        try {
            Properties props = new Properties();
            props.setProperty("mail.store.protocol", "imaps");
            Session session = Session.getDefaultInstance(props, null);
            store = session.getStore("imaps");
            store.connect("imap.gmail.com", MailConfig.APP_EMAIL, MailConfig.APP_PASSWORD);
            folder = store.getFolder("[Gmail]/Sent Mail");
            folder.open(Folder.READ_ONLY);
            Message[] messages = folder.getMessages();
            messageObservableList = FXCollections.observableArrayList();
            for (int i = 0; i < messages.length; ++i) {
                Message msg = messages[i];
                String from = "Unknown";
                if (msg.getReplyTo().length >= 1) {
                    from = msg.getReplyTo()[0].toString();
                } else if (msg.getFrom().length >= 1) {
                    from = msg.getFrom()[0].toString();
                }
                String subject = msg.getSubject();
                messageObservableList.add(new FormatMessage(from, subject));
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    public void setDraftMessagesListView() throws NoSuchProviderException{
        try {
            Properties props = new Properties();
            props.setProperty("mail.store.protocol", "imaps");

            Session session = Session.getDefaultInstance(props, null);
            store = session.getStore("imaps");
            store.connect("imap.gmail.com", MailConfig.APP_EMAIL, MailConfig.APP_PASSWORD);
            folder = store.getFolder("[Gmail]/Drafts");
            folder.open(Folder.READ_ONLY);
            Message[] messages = folder.getMessages();
            messageObservableList = FXCollections.observableArrayList();
            for (int i = 0; i < messages.length; ++i) {
                Message msg = messages[i];
                String from = "Unknown";
                if (msg.getReplyTo().length >= 1) {
                    from = msg.getReplyTo()[0].toString();
                } else if (msg.getFrom().length >= 1) {
                    from = msg.getFrom()[0].toString();
                }
                String subject = msg.getSubject();
                messageObservableList.add(new FormatMessage(from, subject));
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    public void showInbox() throws NoSuchProviderException {
        setInboxMessageListView();
        listMessageViewParent.setItems(messageObservableList);
        listMessageViewParent.setCellFactory(listMessageView -> new messageListViewCell());
    }
    public void showSendMessage() throws NoSuchProviderException {
        setSentMessagesListView();
        listMessageViewParent.setItems(messageObservableList);
        listMessageViewParent.setCellFactory(listMessageView -> new messageListViewCell());
    }
    public void showDrafMessage() throws NoSuchProviderException {
        setDraftMessagesListView();
        listMessageViewParent.setItems(messageObservableList);
        listMessageViewParent.setCellFactory(listMessageView -> new messageListViewCell());
    }
    public void showComposeScreen() throws IOException {
        FXMLLoader fXMLLoader;
        Parent root = FXMLLoader.load(this.getClass().getResource("sendMessage.fxml"));
        showCompose.getChildren().add(root);
    }
}


