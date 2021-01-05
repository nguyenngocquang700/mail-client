package sendfile;

import java.util.Date;

public class FormatMessage {
//    private String to;
//    private String toEmailId;
    private String from;
//    private String fromEmailId;
    private String subject;
    private Date dateCreated;
    private char contentMail;

    public FormatMessage(String from, String subject, Date dateCreated) {
        this.from = from;
        this.subject = subject;
//        this.contentMail =  contentMail;
        this.dateCreated = dateCreated;
    }
//    private String bodyText = null;

//    public String getTo(){
//        return to;
//    }
//
//    public String getToEmailId(){
//        return toEmailId;
//    }

    public String getFrom(){
        return from;
    }

//    public String getFromEmailId(){
//        return fromEmailId;
//    }

    public String getSubject(){
        return subject;
    }

    public char getContentMail(){ return contentMail;}

//    public String getBodyText(){
//        return bodyText;
//    }

    public Date getDateCreated(){ return dateCreated; }

    public void setFrom(String to){
        this.from = from;
    }

    public void setSubject(String subject){
        this.subject = subject;
    }

//    public void setContentMail(char contentMail) { this.contentMail = contentMail; }


    public void setDateCreated(Date dateCreated) { this.dateCreated = dateCreated; }
}
