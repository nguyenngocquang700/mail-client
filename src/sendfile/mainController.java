package sendfile;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;


public class mainController implements Initializable {
    private ObservableList<FormatMessage> messageObservableList;
    Folder folder = null;
    Store store = null;
    @FXML
    private ListView<FormatMessage> listMessageViewParent;
    @FXML
    public Button composeButton;

    public Button btn_INBOX;
    public Button btn_SENT;
    public Button btn_DRAFTS;
    public AnchorPane showCompose;
    public VBox slider;
    public Label hamburger;
    public Label hamburger1;
    public StackPane progress;
    public Label username;
    public Button info;

    @FXML
    public ImageView hamg;
    public Pane slidePane;
    public int percent;
    public int inc;

//    private boolean textIsHtml = false;


    @FXML
    public Label folderLabel;

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
                Date date = msg.getReceivedDate();
                messageObservableList.add(new FormatMessage(from, subject, date));
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
            for (Message msg : messages) {
                String from = "Unknown";
                if (msg.getReplyTo().length >= 1) {
                    from = msg.getReplyTo()[0].toString();
                } else if (msg.getFrom().length >= 1) {
                    from = msg.getFrom()[0].toString();
                }
                String subject = msg.getSubject();
                Date date = msg.getSentDate();
                messageObservableList.add(new FormatMessage(from, subject, date));
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
            for (Message msg : messages) {
                String from = "Unknown";
                if (msg.getReplyTo().length >= 1) {
                    from = msg.getReplyTo()[0].toString();
                } else if (msg.getFrom().length >= 1) {
                    from = msg.getFrom()[0].toString();
                }
                String subject = msg.getSubject();
                Date date = msg.getSentDate();
                messageObservableList.add(new FormatMessage(from, subject, date));
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    public void showInbox() throws NoSuchProviderException {
        setInboxMessageListView();
        folderLabel.setText("INBOX");
        btn_INBOX.setStyle("-fx-text-fill: #f0634f; -fx-background-color:  #232744;");
        listMessageViewParent.setItems(messageObservableList);
        listMessageViewParent.setCellFactory(listMessageView -> new messageListViewCell());


    }
    public void showSendMessage() throws NoSuchProviderException {
        setSentMessagesListView();
        folderLabel.setText("SENT MAILS");
        btn_SENT.setStyle("-fx-text-fill: #f0634f; -fx-background-color:  #232744;");
        listMessageViewParent.setItems(messageObservableList);
        listMessageViewParent.setCellFactory(listMessageView -> new messageListViewCell());
    }
    public void showDrafMessage() throws NoSuchProviderException {
        setDraftMessagesListView();
        folderLabel.setText("DRAFT MAILS");
        btn_DRAFTS.setStyle("-fx-text-fill: #f0634f; -fx-background-color:  #232744;");
        listMessageViewParent.setItems(messageObservableList);
        listMessageViewParent.setCellFactory(listMessageView -> new messageListViewCell());
    }
    public void showComposeScreen() throws IOException {
        FXMLLoader fXMLLoader;
        Parent root = FXMLLoader.load(this.getClass().getResource("sendMessage.fxml"));
        showCompose.getChildren().add(root);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username.setText(MailConfig.APP_EMAIL);
        slider.setTranslateX(0);
        hamburger.setOnMouseClicked(mouseEvent -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);
            slide.setToX(0);
            slide.play();
            slider.setTranslateX(-197);
            slide.setOnFinished((ActionEvent e)-> {
                hamburger.setVisible(false);
                hamburger1.setVisible(true);

            });
        });
        hamburger1.setOnMouseClicked(mouseEvent -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);
            slide.setToX(-197);
            slide.play();
            slider.setTranslateX(0);
            slide.setOnFinished((ActionEvent e)-> {
                hamburger.setVisible(true);
                hamburger1.setVisible(false);
            });
        });
        info.setOnAction((ActionEvent e)-> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About");
            alert.setHeaderText(null);
            alert.setContentText("""
                Mail client with JavaMail API
                Developed by: Nguyen Ngoc Quang 
                              Tran Thi Thanh Tam 
                              Le Thi My Phung 
                              Huynh My Dung
                Copyright © 2021 Alright Reserved""");
            alert.showAndWait();
        });
    }
}


