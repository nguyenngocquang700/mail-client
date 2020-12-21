package sendfile;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.control.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class newMessageController extends Application{
    public static void main(String[] args) {
        launch(args);
    }
    @FXML
    private TextField to;
    public TextField cc;
    public TextField bcc;
    public TextField subject;
    public TextField message;
    public Button btn_send;

    public void start(Stage stage) throws Exception{
//        FXMLLoader loader = new FXMLLoader();
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", MailConfig.HostSend);
        props.put("mail.smtp.socketFactory.port", MailConfig.SSL_PORT);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.port", MailConfig.SSL_PORT);

        // get Session
        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MailConfig.APP_EMAIL, MailConfig.APP_PASSWORD);
            }
        });

        try {
            MimeMessage messagePacket = new MimeMessage(session);
            String getTo = to.getText();
            String getSubject = subject.getText();
            String getMessage = message.getText();
            messagePacket.setFrom(new InternetAddress(MailConfig.APP_EMAIL));
            messagePacket.addRecipient(Message.RecipientType.TO,new InternetAddress(getTo));
            messagePacket.setSubject(getSubject);

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(getMessage);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            messagePacket.setContent(multipart);
            btn_send.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
                        Transport.send(messagePacket);
                        System.out.println("Message sent successfully");
                    } catch (MessagingException e) {
                        System.out.println("Cannot send mail");
                        e.printStackTrace();
                    }
                }
            });

            stage.setTitle("New Mail");
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


}
