package sendfile;


import java.util.Properties;
import java.io.IOException;
import java.util.Properties;
import javax.mail.*;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class Action {
    public static void main(String[] args){
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

        // compose message
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(MailConfig.APP_EMAIL));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(MailConfig.RECEIVE_EMAIL));
            message.setSubject("Sending email with attachment");

            BodyPart messageBodyPart1 = new MimeBodyPart();
            messageBodyPart1.setText("Content file with attachment");

            MimeBodyPart messageBodyPart2 = new MimeBodyPart();

            String filename = "C:\\Users\\nguye\\Downloads\\LimitTime.exe";
            DataSource source = new FileDataSource(filename);
            messageBodyPart2.setDataHandler(new DataHandler(source));
            messageBodyPart2.setFileName(filename);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart1);
            multipart.addBodyPart(messageBodyPart2);

            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Message sent successfully");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
