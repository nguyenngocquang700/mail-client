package sendfile;

public class FormatMessage {
//    private String to;
//    private String toEmailId;
    private String from;
//    private String fromEmailId;
    private String subject;
    private String dateCreated;
    private String contentMail;

    public FormatMessage(String from, String subject) {
        this.from = from;
        this.subject = subject;
//        this.contentMail =  contentMail;
//        this.dateCreated = dateCreated;
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

    public String getContentMail(){ return contentMail;}

//    public String getBodyText(){
//        return bodyText;
//    }

//    public String getDateCreated(){ return dateCreated; }

    public void setFrom(String to){
        this.from = from;
    }

    public void setSubject(String subject){
        this.subject = subject;
    }

    public void setContentMail(String contentMail) { this.contentMail = contentMail; }


//    public void setDateCreated(String dateCreated) { this.dateCreated = dateCreated; }
}
