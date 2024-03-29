package sendfile;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.awt.*;


import java.io.*;

import java.util.*;
import java.util.List;


public class sendMessageController {
    @FXML
    public AnchorPane anchorPaneStage;
    public TextField receiverText;
    public TextField ccText;
    public TextField bccText;
    public TextField subjectText;
    public TextArea messageText;

    public Button sendButton;
    //    public List<ImageView> imgAttach;
    public String  addressReceiver;
    public String addressCC;
    public String addressBCC;
    public String messageInfomation;
    public String subjectInformation;
    public String Username_ss = MailConfig.APP_EMAIL;
    public String Password_ss = MailConfig.APP_PASSWORD;
    public List<File> files=new ArrayList<>();

    public HBox showFiles;
    public MenuButton format;
    private Desktop desktop = Desktop.getDesktop();


    private void showAlertConfirm() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("CONFIRM");

        // Header Text: null
        alert.setHeaderText(null);
        alert.setContentText("SEND SUCCESS");
        alert.showAndWait();
    }
    private void showAlertWarning(MessagingException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        // Header Text: null
        alert.setHeaderText(null);
        alert.setTitle("NO SEND SUCCESS");
        Label label = new Label("Stack Trace:");

        String stackTrace = this.getStackTrace(e);
        TextArea textArea = new TextArea();
        textArea.setText(stackTrace);
        VBox dialogPaneContent = new VBox();
        dialogPaneContent.getChildren().addAll(label, textArea);

        // Sét đặt nội dung cho Dialog Pane
        alert.getDialogPane().setContent(dialogPaneContent);
        alert.showAndWait();
    }

    private String getStackTrace(MessagingException e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String s = sw.toString();
        return s;
    }

    @FXML
    public void getInformation(){
        addressReceiver=receiverText.getText();
        addressCC=ccText.getText();
        addressBCC=bccText.getText();
        messageInfomation=messageText.getText();
        subjectInformation=subjectText.getText();
        if(receiverText!=null){
            sendButton.setDisable(false);
        }

    }
    Session createSession(){
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", MailConfig.HostSend);
        props.put("mail.smtp.socketFactory.port", MailConfig.SSL_PORT);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.port", MailConfig.SSL_PORT);

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Username_ss, Password_ss);
            }
        });
        return session;
    }

    public void chooseImage(){
        Stage stage=(Stage)anchorPaneStage.getScene().getWindow();
        FileChooser fc=new FileChooser();

        FileChooser.ExtensionFilter imageFilter=new FileChooser.ExtensionFilter("Image","*.jpg","*png");
        fc.getExtensionFilters().add(imageFilter);
        fc.setTitle("Choose a Image");
//        files=fc.showOpenMultipleDialog(stage);
        files.addAll(fc.showOpenMultipleDialog(stage));
        if(files!=null){
            for(int i=0;i<files.size();i++) {
//                System.out.println("file: "+files.get(i).getName());
                Image image = new Image(files.get(i).toURI().toString());
                ImageView imgAttach=new ImageView();
                imgAttach.setImage(image);
                imgAttach.setFitWidth(43);
                imgAttach.setFitHeight(43);
                showFiles.getChildren().add(imgAttach);
            }
        }
    }

    public void chooseFile(){
        Stage stage=(Stage)anchorPaneStage.getScene().getWindow();
        FileChooser fc=new FileChooser();
        FileChooser.ExtensionFilter fileFilter=new FileChooser.ExtensionFilter("File","*.txt","*.docx","*.pdf");
        fc.getExtensionFilters().add(fileFilter);
        fc.setTitle("Choose a File");
        files.addAll(fc.showOpenMultipleDialog(stage));
        if(files!=null){
            for(int i=0;i<files.size();i++) {
                Label label=new Label(files.get(i).getName());
                label.setStyle(String.valueOf(Color.GRAY));
                label.setTextFill(Color.BLACK);
                showFiles.getChildren().add(label);
            }
        }
    }

    private void printLog(TextArea textArea, List<File> files) {
        if (files == null || files.isEmpty()) {
            return;
        }
        for (File file : files) {
            textArea.appendText(file.getAbsolutePath() + "\n");
        }
    }

    private void openFile(File file) {
        try {
            this.desktop.open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendMessage() {
        try {
            getInformation();
            Session session = createSession();
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(MailConfig.APP_EMAIL));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(addressReceiver));
            message.setRecipients(Message.RecipientType.BCC,InternetAddress.parse(addressBCC));
            message.setRecipients(Message.RecipientType.CC,InternetAddress.parse(addressCC));
            // Set Subject: header field
            message.setSubject(subjectInformation);

            if(files==null&& files.size()==0) {
                message.setText(messageInfomation);
            }else{

//                    System.out.println("file: "+files.get(i).getName());
                Multipart multipart = new MimeMultipart();
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(messageInfomation);
                multipart.addBodyPart(messageBodyPart);
//                    String filename = files.get(i).getPath().toString();
                for(File file: files){
                    messageBodyPart=new MimeBodyPart();
                    DataSource source = new FileDataSource(file);
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(file.getName());
                    multipart.addBodyPart(messageBodyPart);
                }
                message.setContent(multipart);
            }

            message.saveChanges();

            try{
                Transport.send(message,message.getAllRecipients());
            }
            catch (Exception e){
                System.out.println(e);
            }
            showAlertConfirm();

        }
        catch (MessagingException e){
            showAlertWarning(e);
        }

    }

}
