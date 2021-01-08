package sendfile;

import com.jfoenix.controls.JFXButton;
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
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.logging.ErrorManager;


public class mainController implements Initializable {
    private ObservableList<FormatMessage> messageObservableList;
    Folder folder = null;
    Store store = null;
    @FXML
    private ListView<FormatMessage> listMessageViewParent;
    @FXML
    public JFXButton composeButton;
    public JFXButton btn_INBOX;
    public JFXButton btn_DRAFTS;
    public JFXButton attachment;
    public JFXButton btn_SENT;
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
    public JFXButton deleteMessage;
    @FXML
    public ImageView hamg;
    public Pane slidePane;
    public int percent;
    public int inc;

//    private boolean textIsHtml = false;

//    private boolean textIsHtml = false;


    @FXML
    public Label folderLabel;

    private boolean textIsHtml = false;
    Session createSession(String folderName) throws MessagingException {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");

        Session session = Session.getDefaultInstance(props, null);
        store = session.getStore("imaps");
        folder = store.getFolder(folderName);
        folder.open(Folder.READ_ONLY);
        return session;
    }

    public void initActions_inbox(){
        //Detecting mouse clicked
        listMessageViewParent.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
            }
        });
        listMessageViewParent.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends FormatMessage> ov, FormatMessage old_val, FormatMessage new_val) -> {
            if(!listMessageViewParent.getItems().isEmpty()){
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
                int index=listMessageViewParent.getSelectionModel().getSelectedIndex();
                deleteMessage.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            if(listMessageViewParent.getSelectionModel().getSelectedIndex()==(listMessageViewParent.getItems().size()-1)||listMessageViewParent.getSelectionModel().getSelectedIndex()==0){
                                fromMessageRecv.setText("");
                                subjectMessageRecv.setText("");
                                dateMessageRecv.setText("");
                                messageObservableList.remove(index);
                                deteleMessageView_inbox(index);
                                messageEngine.loadContent("");
                            }
                            else{
                                fromMessageRecv.setText(listMessageViewParent.getItems().get(index).getFrom());
                                subjectMessageRecv.setText(listMessageViewParent.getItems().get(index).getSubject());
                                dateMessageRecv.setText(listMessageViewParent.getItems().get(index).getDateCreated());
                                messageObservableList.remove(index);
                                deteleMessageView_inbox(index);
                                messageEngine.loadContent(listMessageViewParent.getItems().get(index-1).getBodyText());
                            }
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                    }
                });
                attachment.setDisable(true);
                if (listMessageViewParent.getSelectionModel().getSelectedItem().getAttachmentboo()) {
                    attachment.setDisable(false);
                    attachment.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            downloadAttachment(listMessageViewParent.getSelectionModel().getSelectedItem().getAt());
                        }
                    });
                }
            }
        });

    }
    public void initActions_draft(){
        //Detecting mouse clicked
        listMessageViewParent.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
            }
        });
        listMessageViewParent.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends FormatMessage> ov, FormatMessage old_val, FormatMessage new_val) -> {
            if(!listMessageViewParent.getItems().isEmpty()){
                subjectMessageRecv.setText(listMessageViewParent.getSelectionModel().getSelectedItem().getSubject());
                fromMessageRecv.setText(listMessageViewParent.getSelectionModel().getSelectedItem().getFrom());
                dateMessageRecv.setText(listMessageViewParent.getSelectionModel().getSelectedItem().getDateCreated());
                //attachmentRecv.setText(listMessageViewParent.getSelectionModel().getSelectedItem().getAttachment());
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
                int index=listMessageViewParent.getSelectionModel().getSelectedItem().getAt();
                deleteMessage.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            if(listMessageViewParent.getSelectionModel().getSelectedIndex()==(listMessageViewParent.getItems().size()-1)||listMessageViewParent.getSelectionModel().getSelectedIndex()==0){
                                fromMessageRecv.setText("");
                                subjectMessageRecv.setText("");
                                dateMessageRecv.setText("");
                                messageObservableList.remove(index);
                                deteleMessageView_draft(index);
                                messageEngine.loadContent("");
                            }
                            else{
                                fromMessageRecv.setText(listMessageViewParent.getItems().get(index).getFrom());
                                subjectMessageRecv.setText(listMessageViewParent.getItems().get(index).getSubject());
                                dateMessageRecv.setText(listMessageViewParent.getItems().get(index).getDateCreated());
                                messageObservableList.remove(index);
                                deteleMessageView_draft(index);
                                messageEngine.loadContent(listMessageViewParent.getItems().get(index-1).getBodyText());
                            }
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }
    public void initActions_sent(){
        //Detecting mouse clicked
        listMessageViewParent.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
            }
        });
        listMessageViewParent.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends FormatMessage> ov, FormatMessage old_val, FormatMessage new_val) -> {
            if(!listMessageViewParent.getItems().isEmpty()){
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
                int index=listMessageViewParent.getSelectionModel().getSelectedIndex();
                deleteMessage.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            if(listMessageViewParent.getSelectionModel().getSelectedIndex()==(listMessageViewParent.getItems().size()-1)||listMessageViewParent.getSelectionModel().getSelectedIndex()==0){
                                fromMessageRecv.setText("");
                                subjectMessageRecv.setText("");
                                dateMessageRecv.setText("");
                                messageObservableList.remove(index);
                                deteleMessageView_sent(index);
                                messageEngine.loadContent("");
                            }
                            else{
                                fromMessageRecv.setText(listMessageViewParent.getItems().get(index).getFrom());
                                subjectMessageRecv.setText(listMessageViewParent.getItems().get(index).getSubject());
                                dateMessageRecv.setText(listMessageViewParent.getItems().get(index).getDateCreated());
                                messageObservableList.remove(index);
                                deteleMessageView_sent(index);
                                messageEngine.loadContent(listMessageViewParent.getItems().get(index-1).getBodyText());
                            }
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }
    public static void processMultipart(Multipart mp)
            throws MessagingException {
        for (int i = 0; i < mp.getCount(); i++) {
            processPart(mp.getBodyPart(i));
        }
    }

    public static void processPart(Part p) {
        try {
            String fileName = p.getFileName();
            String disposition = p.getDisposition();
            String contentType = p.getContentType();
            if (contentType.toLowerCase().startsWith("multipart/")) {
                processMultipart((Multipart) p.getContent());
            } else if (fileName == null
                    && (Part.ATTACHMENT.equalsIgnoreCase(disposition)
                    || !contentType.equalsIgnoreCase("text/plain"))) {
                // pick a random file name.
                fileName = File.createTempFile("attachment", ".txt").getName();
            }
            if (fileName == null) { // likely inline
//                p.writeTo(System.out);
            } else {
                File f = new File(fileName);
                // find a file that does not yet exist
                for (int i = 1; f.exists(); i++) {
                    String newName = fileName + " " + i;
                    f = new File(newName);
                }
                try (
                        OutputStream out = new BufferedOutputStream(new FileOutputStream(f));
                        InputStream in = new BufferedInputStream(p.getInputStream())) {
                    // We can't just use p.writeTo() here because it doesn't
                    // decode the attachment. Instead we copy the input stream
                    // onto the output stream which does automatically decode
                    // Base-64, quoted printable, and a variety of other formats.
                    int b;
                    while ((b = in.read()) != -1) {out.write(b); }
                    out.flush();
                }
            }
        } catch (IOException | MessagingException ex) {
            ex.printStackTrace();
        }
    }

    public void downloadAttachment(int i) {
        try {
            Properties props = new Properties();
            props.setProperty("mail.store.protocol", "imaps");

            Session session = Session.getInstance(props, null);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com",MailConfig.APP_EMAIL, MailConfig.APP_PASSWORD);
            Folder folder = store.getFolder("INBOX");
            if (folder == null) {
                System.out.println("Folder  not found.");
                System.exit(1);
            }
            folder.open(Folder.READ_ONLY);
            // Get the messages from the server
            Message[] messages = folder.getMessages();
            System.out.println("------------ Message " + (i + 1)
                    + " ------------");
            // Print message headers
            @SuppressWarnings("unchecked")
            Enumeration<Header> headers = messages[i].getAllHeaders();
            while (headers.hasMoreElements()) {
                Header h = headers.nextElement();
                //  System.out.println(h.getName() + ": " + h.getValue());
            }
            System.out.println();
            // Enumerate parts
            Object body = messages[i].getContent();
            if (body instanceof Multipart) {
                processMultipart((Multipart) body);
            } else { // ordinary message
                processPart(messages[i]);
            }
            System.out.println();
            // Close the connection
            // but don't remove the messages from the server
            folder.close(false);
        } catch (MessagingException | IOException ex) {
            ex.printStackTrace();
        }
        // Since we may have brought up a GUI to authenticate,
        // we can't rely on returning from main() to exit
    }
    public void deteleMessageView_inbox(int index) throws MessagingException {
        try {
            Properties props = new Properties();
            props.setProperty("mail.store.protocol", "imaps");
            Session session = Session.getDefaultInstance(props, null);
            store = session.getStore("imaps");
            store.connect("imap.gmail.com", MailConfig.APP_EMAIL, MailConfig.APP_PASSWORD);
            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);
            // retrieve the messages from the folder in an array and print it
            Message[] messages = folder.getMessages();
            Message message = messages[index];
            message.setFlag(Flags.Flag.DELETED, true);
            // expunges the folder to remove messages which are marked deleted
            folder.close(true);
            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deteleMessageView_draft(int index) throws MessagingException {
        try {
            Properties props = new Properties();
            props.setProperty("mail.store.protocol", "imaps");
            Session session = Session.getDefaultInstance(props, null);
            store = session.getStore("imaps");
            store.connect("imap.gmail.com", MailConfig.APP_EMAIL, MailConfig.APP_PASSWORD);
            folder = store.getFolder("[Gmail]/Drafts");
            folder.open(Folder.READ_WRITE);
            // retrieve the messages from the folder in an array and print it
            Message[] messages = folder.getMessages();
            Message message = messages[index];
            message.setFlag(Flags.Flag.DELETED, true);
            // expunges the folder to remove messages which are marked deleted
            folder.close(true);
            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deteleMessageView_sent(int index) throws MessagingException {
        try {
            Properties props = new Properties();
            props.setProperty("mail.store.protocol", "imaps");
            Session session = Session.getDefaultInstance(props, null);
            store = session.getStore("imaps");
            store.connect("imap.gmail.com", MailConfig.APP_EMAIL, MailConfig.APP_PASSWORD);
            folder = store.getFolder("[Gmail]/Sent Mail");
            folder.open(Folder.READ_WRITE);
            // retrieve the messages from the folder in an array and print it
            Message[] messages = folder.getMessages();
            Message message = messages[index];
            message.setFlag(Flags.Flag.DELETED, true);
            // expunges the folder to remove messages which are marked deleted
            folder.close(true);
            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String getTextMess(Part p) throws
            MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String)p.getContent();
            textIsHtml = p.isMimeType("text/html");
            return s;
        }

        if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart)p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null)
                        text = getTextMess(bp);
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getTextMess(bp);
                    if (s != null)
                        return s;
                } else {
                    return getTextMess(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getTextMess(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }

        return null;
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
            for (int i= messages.length -1 ; i>=0 ;i--) {
                Message msg = messages[i];
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
                String attachFiles = "";
                boolean attachboo=false;
                if (contentType.contains("multipart")) {
                    Multipart multiPart = (Multipart) msg.getContent();
                    int numberOfParts = multiPart.getCount();
                    for (int partCount = 0; partCount < numberOfParts; partCount++) {
                        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                            attachboo = true;
                            InputStream is = part.getInputStream();
                            String fileName = part.getFileName();
                            fileName.substring(fileName.lastIndexOf("/")+1);
                            //System.out.println(fileName);
                            attachFiles += fileName + ", ";
                        } else {
//                            messageContent = part.getContent().toString();
                            messageContent = getTextMess(msg);
                        }
                    }
                    if (attachFiles.length() > 1) {
                        attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
//                        messageContent = getTextMess(msg);
                    }
                } else if (contentType.contains("text/plain") || contentType.contains("text/html")) {
                    Object content = msg.getContent();
                    if (content != null) {
                        messageContent = content.toString();
                    }
                }

                System.out.println(attachFiles);
                messageObservableList.add(new FormatMessage(from, subject, messageContent, date,attachFiles,i,attachboo));
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
            store.connect("imap.gmail.com", MailConfig.APP_EMAIL, MailConfig.APP_PASSWORD);
            folder = store.getFolder("[Gmail]/Sent Mail");
            folder.open(Folder.READ_ONLY);
            Message[] messages = folder.getMessages();
            messageObservableList = FXCollections.observableArrayList();
            for (int i= messages.length -1 ; i>=0 ;i--) {
                Message msg = messages[i];
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
                String attachFiles = "";
                boolean attachboo=false;
                if (contentType.contains("multipart")) {
                    Multipart multiPart = (Multipart) msg.getContent();
                    int numberOfParts = multiPart.getCount();
                    for (int partCount = 0; partCount < numberOfParts; partCount++) {
                        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                            attachboo = true;
                            InputStream is = part.getInputStream();
                            String fileName = part.getFileName();
                            fileName.substring(fileName.lastIndexOf("/")+1);
                            //System.out.println(fileName);
                            attachFiles += fileName + ", ";
                        } else {
//                            messageContent = part.getContent().toString();
                            messageContent = getTextMess(msg);
                        }
                    }
                    if (attachFiles.length() > 1) {
                        attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
//                        messageContent = getTextMess(msg);
                    }
                } else if (contentType.contains("text/plain") || contentType.contains("text/html")) {
                    Object content = msg.getContent();
                    if (content != null) {
                        messageContent = content.toString();
                    }
                }

                //     System.out.println(messageContent);
                messageObservableList.add(new FormatMessage(from, subject, messageContent, date,attachFiles,i,attachboo));
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
            store.connect("imap.gmail.com", MailConfig.APP_EMAIL, MailConfig.APP_PASSWORD);
            folder = store.getFolder("[Gmail]/Drafts");
            folder.open(Folder.READ_ONLY);
            Message[] messages = folder.getMessages();
            messageObservableList = FXCollections.observableArrayList();
            for (int i= messages.length -1 ; i>=0 ;i--) {
                Message msg = messages[i];
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
                String attachFiles = "";
                boolean attachboo=false;
                if (contentType.contains("multipart")) {
                    Multipart multiPart = (Multipart) msg.getContent();
                    int numberOfParts = multiPart.getCount();
                    for (int partCount = 0; partCount < numberOfParts; partCount++) {
                        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                            attachboo = true;
                            InputStream is = part.getInputStream();
                            String fileName = part.getFileName();
                            fileName.substring(fileName.lastIndexOf("/")+1);
                            //System.out.println(fileName);
                            attachFiles += fileName + ", ";
                        } else {
//                            messageContent = part.getContent().toString();
                            messageContent = getTextMess(msg);
                        }
                    }
                    if (attachFiles.length() > 1) {
                        attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
                    }
                } else if (contentType.contains("text/plain") || contentType.contains("text/html")) {
                    Object content = msg.getContent();
                    if (content != null) {
                        messageContent = content.toString();
                    }
                }

                //     System.out.println(messageContent);
                messageObservableList.add(new FormatMessage(from, subject, messageContent, date,attachFiles,i,attachboo));
            }
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    public void showInbox() throws NoSuchProviderException {
        setInboxMessageListView();
        folderLabel.setText("INBOX");
        listMessageViewParent.setItems(messageObservableList);
        listMessageViewParent.setCellFactory(listMessageView -> new messageListViewCell());
        initActions_inbox();
    }
    public void showSendMessage() throws NoSuchProviderException {
        setSentMessagesListView();
        folderLabel.setText("SENT MAILS");
        listMessageViewParent.setItems(messageObservableList);
        listMessageViewParent.setCellFactory(listMessageView -> new messageListViewCell());
        initActions_sent();

    }
    public void showDrafMessage() throws NoSuchProviderException {
        setDraftMessagesListView();
        folderLabel.setText("DRAFT MAILS");
        listMessageViewParent.setItems(messageObservableList);
        listMessageViewParent.setCellFactory(listMessageView -> new messageListViewCell());
        initActions_draft();
    }

    public void showComposeScreen() throws IOException {
        FXMLLoader fXMLLoader;
        Parent root = FXMLLoader.load(this.getClass().getResource("sendMessage.fxml"));
        showComponent.getChildren().clear();
        showComponent.getChildren().add(root);
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
