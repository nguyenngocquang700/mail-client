//package sendfile;
//
//import sendfile.MailConfig;
//
//import javax.mail.*;
//import java.io.IOException;
//import java.util.Properties;
//
//public class CheckingMails {
//    public static void main(String[] args){
//        try {
//            Properties properties = new Properties();
//            properties.put("mail.pop3.host", MailConfig.hostRec_POP);
//            properties.put("mail.pop3.port", "995");
//            properties.put("mail.pop3.starttls.enable", "true");
//            Session emailSession = Session.getDefaultInstance(properties);
//
//            Store store = emailSession.getStore("pop3s");
//            store.connect(MailConfig.hostRec_POP, MailConfig.APP_EMAIL, MailConfig.APP_PASSWORD);
//
//            Folder emailFolder = store.getFolder("INBOX");
//            emailFolder.open(Folder.READ_ONLY);
//
//            Message[] messages = emailFolder.getMessages();
//            System.out.println("messages.length---" + messages.length);
//            for (int i = 0, n = messages.length; i < n ;i++){
//                Message message = messages[i];
//                System.out.println("--------------------------------");
//                System.out.println("Email Number" + (i+1));
//                System.out.println("Subject: " + message.getSubject());
//                System.out.println("From: " + message.getFrom()[0]);
//                System.out.println("Text: " + message.getContent().toString());
//            }
//
//            emailFolder.close();
//            store.close();
//            System.out.println("Out of range");
//        } catch (NoSuchProviderException e) {
//            e.printStackTrace();
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}

package sendfile;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.logging.Logger;

import javafx.scene.control.CheckBox;
import sendfile.MailConfig;

import static sendfile.MailConfig.hostRec_IMAP;

public class CheckingMails {
    public CheckingMails() {}
    public static void receive()throws MessagingException, IOException{
        Folder folder = null;
        Store store = null;
        try{
            Properties properties = new Properties();
            properties.setProperty("mail.store.protocol", "imaps");
            Session session = Session.getDefaultInstance(properties, null);
            store = session.getStore("imaps");
            store.connect("imap.gmail.com", MailConfig.APP_EMAIL, MailConfig.APP_PASSWORD);
            folder = store.getFolder("[Gmail]/Sent Mail");
            folder.open(Folder.READ_WRITE);
            Message[] message = folder.getMessages();
            System.out.println("No of messages: " + folder.getMessageCount());
            System.out.println("No of Unread Message: " + folder.getUnreadMessageCount());
            for (int i = 0; i < message.length; ++i){
                System.out.println("MESSAGE #" + (i+1) + ": ");
                Message msg = message[i];
                String from = "unknow";
                if (msg.getReplyTo().length >= 1){
                    from = msg.getReplyTo()[0].toString();
                }
                else if(msg.getFrom().length >= 1){
                    from = msg.getFrom()[0].toString();
                }
                String subject = msg.getSubject();
                System.out.println("Saving... " + subject + from);
                System.out.println("Content " + msg.getContent().toString());
                String filename = "E:\\temp" + subject;

            }
        }
        finally {
            if (folder != null){
                folder.close(true);
            }
            if (store != null){
                store.close();
            }
        }
    }
    public static void saveParts(Object content, String filename) throws IOException, MessagingException{
        OutputStream out = null;
        InputStream in = null;
        try {
            if (content instanceof Multipart){
                Multipart multi = (Multipart)content;
                int parts = multi.getCount();
                for (int j = 0; j < parts ; j++){
                    MimeBodyPart part = (MimeBodyPart)multi.getBodyPart(j);
                    if (part.getContent() instanceof Multipart){
                        saveParts(part.getContent(), filename);
                    }
                    else {
                        String extension = "";
                        if (part.isMimeType("text/html")){
                            extension = "html";
                        }
                        else{
                            if (part.isMimeType("text/plain")){
                                extension = "txt";
                            }
                            else{
                                extension = part.getDataHandler().getName();
                            }
                            filename = filename + "." + extension;
                            System.out.println("... " + filename);
                            File file;
                            out = new FileOutputStream(new File(filename));
                            in = part.getInputStream();
                            int k;
                            while((k = in.read()) != -1){
                                out.write(k);
                            }
                        }
                    }
                }
            }
        }
        finally {
            if (in != null){
                in.close();
            }
            if (out != null){
                out.flush();
                out.close();
            }
        }
    }
    public static void main(String args[]) throws  Exception{
        CheckingMails.receive();
    }
}