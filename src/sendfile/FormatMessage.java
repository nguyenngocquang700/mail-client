package sendfile;

import java.util.Date;

public class FormatMessage {
//    private String to;
//    private String toEmailId;
    private String from;
//    private String fromEmailId;
    private String subject;
    private String dateCreated;
    private String bodyText = null;
    private boolean attachmentboo;
    private char contentMail;
    private String attachment;
    private int at;
    public FormatMessage(String from, String subject, String bodyText, String dateCreated, String attachment, int at,boolean attachmentboo) {
        this.from = from;
        this.subject = subject;
//        this.contentMail =  contentMail;
        this.dateCreated = dateCreated;
        this.bodyText = bodyText;
        this.attachment = attachment;
        this.at = at;
        this.attachmentboo = attachmentboo;
    }

//    private String bodyText = null;

//    public String getTo(){
//        return to;
//    }
//
//    public String getToEmailId(){
//        return toEmailId;
//    }
    public boolean getAttachmentboo() { return attachmentboo;}
    public String getAttachment() { return attachment;}
    public int getAt() { return at;}
    public String getFrom(){
        return from;
    }

//    public String getFromEmailId(){
//        return fromEmailId;
//    }

    public String getSubject(){
        return subject;
    }

//    public char getContentMail(){ return contentMail;}

    public String getBodyText() { return  bodyText;}


//    public String getBodyText(){
//        return bodyText;
//    }

    public String getDateCreated(){ return (String)dateCreated; }

    public void setFrom(String to){
        this.from = from;
    }

    public void setSubject(String subject){
        this.subject = subject;
    }

//    public void setContentMail(char contentMail) { this.contentMail = contentMail; }

    public void setBodyText(String text){
        bodyText = text;
    }

    public void setDateCreated(String dateCreated) { this.dateCreated = dateCreated; }
}
