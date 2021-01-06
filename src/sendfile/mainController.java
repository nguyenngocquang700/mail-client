package sendfile;

import javafx.animation.TranslateTransition;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.logging.ErrorManager;


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
    //    public AnchorPane showCompose;
    public Label subjectMessageRecv;
    public Label fromMessageRecv;
    public Label dateMessageRecv;
    public StackPane showComponent;
    public WebView messageDisplay;
    public WebEngine messageEngine;
    public VBox slider;
    public Label hamburger;
    public Label hamburger1;
    public StackPane progress;
    public Label username;
    public Button logout;
    public Button info;

    @FXML
    public ImageView hamg;
    public Pane slidePane;
    public int percent;
    public int inc;

//    private boolean textIsHtml = false;

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

    public void initActions(){

        //Detecting mouse clicked
        listMessageViewParent.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
            }
        });
        listMessageViewParent.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends FormatMessage> ov, FormatMessage old_val, FormatMessage new_val) -> {
            subjectMessageRecv.setText(listMessageViewParent.getSelectionModel().getSelectedItem().getSubject());
            fromMessageRecv.setText(listMessageViewParent.getSelectionModel().getSelectedItem().getFrom());
            dateMessageRecv.setText(listMessageViewParent.getSelectionModel().getSelectedItem().getDateCreated());
            messageEngine = messageDisplay.getEngine();
            messageEngine.setJavaScriptEnabled(true);
            messageEngine.loadContent(listMessageViewParent.getSelectionModel().getSelectedItem().getBodyText());
            messageDisplay.getEngine().setCreatePopupHandler(
                    new Callback<PopupFeatures, WebEngine>() {
                        @Override
                        public WebEngine call(PopupFeatures config) {
                            // grab the last hyperlink that has :hover pseudoclass
                            Object o = messageDisplay
                                    .getEngine()
                                    .executeScript(
                                            "var list = document.querySelectorAll( ':hover' );"
                                                    + "for (i=list.length-1; i>-1; i--) "
                                                    + "{ if ( list.item(i).getAttribute('href') ) "
                                                    + "{ list.item(i).getAttribute('href'); break; } }");

                            // open in native browser
                            ErrorManager log = new ErrorManager();
                            try {
                                if (o != null) {
                                    Desktop.getDesktop().browse(
                                            new URI(o.toString()));
                                } else {
                                    System.out.println("No result from uri detector ");
                                }
                            } catch (IOException e) {
                                System.out.println("Unexpected error obtaining uri ");
                            } catch (URISyntaxException e) {
                                System.out.println("Could not interpret uri ");
                            }

                            // prevent from opening in webView
                            return null;
                        }
                    });
            showComponent.getChildren().clear();
            showComponent.getChildren().add(messageDisplay);
        });


    }

    public void setInboxMessageListView() throws NoSuchProviderException {
        try {
            Properties props = new Properties();
            props.setProperty("mail.store.protocol", "imaps");

            Session session = Session.getDefaultInstance(props, null);
            store = session.getStore("imaps");
            store.connect(MailConfig.hostRec_IMAP, MailConfig.APP_EMAIL, MailConfig.APP_PASSWORD);
            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);
            Message[] messages = folder.getMessages();
            messageObservableList = FXCollections.observableArrayList();
            for (Message msg : messages) {
                String from = "Unknow";
                if (msg.getReplyTo().length >= 1) {
                    from = msg.getReplyTo()[0].toString();
                } else if (msg.getFrom().length >= 1) {
                    from = msg.getFrom()[0].toString();
                }
                String subject = msg.getSubject();
                String date = msg.getReceivedDate().toString();
                String contentType = msg.getContentType();
                String messageContent = "";
                if (contentType.contains("multipart")) {
                    Multipart multipart = (Multipart) msg.getContent();
                    int numberOfParts = multipart.getCount();
                    for (int partCount = 0; partCount < numberOfParts; partCount++) {
                        MimeBodyPart part = (MimeBodyPart) multipart.getBodyPart(partCount);
                        messageContent = part.getContent().toString();
                    }
                } else if (contentType.contains("text/plain") || contentType.contains("text/html")) {
                    Object content = msg.getContent();
                    if (content != null) {
                        messageContent = content.toString();
                    }
                }
                messageObservableList.add(new FormatMessage(from, subject, messageContent, date));
            }

        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    public void setSentMessagesListView() throws NoSuchProviderException {
        try {
            Properties props = new Properties();
            props.setProperty("mail.store.protocol", "imaps");
            Session session = Session.getDefaultInstance(props, null);
            store = session.getStore("imaps");
            store.connect(MailConfig.hostRec_IMAP, MailConfig.APP_EMAIL, MailConfig.APP_PASSWORD);
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
                String date = msg.getSentDate().toString();
                String contentType = msg.getContentType();
                String messageContent = "";
                if (contentType.contains("multipart")){
                    Multipart multipart = (Multipart) msg.getContent();
                    int numberOfParts = multipart.getCount();
                    for (int partCount = 0; partCount < numberOfParts; partCount++){
                        MimeBodyPart part = (MimeBodyPart) multipart.getBodyPart(partCount);
                        messageContent = part.getContent().toString();
                    }
                } else if (contentType.contains("text/plain") || contentType.contains("text/html")){
                    Object content = msg.getContent();
                    if (content != null){
                        messageContent = content.toString();
                    }
                }
                messageObservableList.add(new FormatMessage(from, subject, messageContent, date));
            }
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }
    public void setDraftMessagesListView() throws NoSuchProviderException{
        try {
            Properties props = new Properties();
            props.setProperty("mail.store.protocol", "imaps");

            Session session = Session.getDefaultInstance(props, null);
            store = session.getStore("imaps");
            store.connect(MailConfig.hostRec_IMAP, MailConfig.APP_EMAIL, MailConfig.APP_PASSWORD);
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
                String date = msg.getSentDate().toString();
                String contentType = msg.getContentType();
                String messageContent = "";
                if (contentType.contains("multipart")){
                    Multipart multipart = (Multipart) msg.getContent();
                    int numberOfParts = multipart.getCount();
                    for (int partCount = 0; partCount < numberOfParts; partCount++){
                        MimeBodyPart part = (MimeBodyPart) multipart.getBodyPart(partCount);
                        messageContent = part.getContent().toString();
                    }
                } else if (contentType.contains("text/plain") || contentType.contains("text/html")){
                    Object content = msg.getContent();
                    if (content != null){
                        messageContent = content.toString();
                    }
                }
                messageObservableList.add(new FormatMessage(from, subject, messageContent, date));
            }
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    public void showInbox() throws NoSuchProviderException {
        setInboxMessageListView();
        folderLabel.setText("INBOX");
        btn_INBOX.setStyle("-fx-text-fill: #f0634f; -fx-background-color:  #232744;");
        listMessageViewParent.setItems(messageObservableList);
        listMessageViewParent.setCellFactory(listMessageView -> new messageListViewCell());
        initActions();

    }
    public void showSendMessage() throws NoSuchProviderException {
        setSentMessagesListView();
        folderLabel.setText("SENT MAILS");
        btn_SENT.setStyle("-fx-text-fill: #f0634f; -fx-background-color:  #232744;");
        btn_INBOX.setStyle("-fx-text-fill: #f0634f; -fx-background-color:  #232744;");
        listMessageViewParent.setItems(messageObservableList);
        listMessageViewParent.setCellFactory(listMessageView -> new messageListViewCell());
        initActions();
    }
    public void showDrafMessage() throws NoSuchProviderException {
        setDraftMessagesListView();
        folderLabel.setText("DRAFT MAILS");
        btn_DRAFTS.setStyle("-fx-text-fill: #f0634f; -fx-background-color:  #232744;");
        btn_INBOX.setStyle("-fx-text-fill: #f0634f; -fx-background-color:  #232744;");
        listMessageViewParent.setItems(messageObservableList);
        listMessageViewParent.setCellFactory(listMessageView -> new messageListViewCell());
        initActions();

    }

    public void showComposeScreen() throws IOException {
        FXMLLoader fXMLLoader;
        Parent root = FXMLLoader.load(this.getClass().getResource("sendMessage.fxml"));
        showComponent.getChildren().clear();
        showComponent.getChildren().add(root);
    }

//    public void showMessageScreen() throws IOException {
//        FXMLLoader fxmlLoader;
//        Parent root = FXMLLoader.load(this.getClass().getResource("showMessage.fxml"));
//        showComponent.getChildren().add(root);
//        initActions();
//    }

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

        logout.setOnAction((ActionEvent e)-> {
            try {
                Parent main = (Parent) FXMLLoader.load(getClass().getResource("Login.fxml"));
                Scene scene = new Scene(main);
                Stage mainStage = (Stage)((Node) e.getSource()).getScene().getWindow();
                mainStage.setScene(scene);
                mainStage.show();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
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
