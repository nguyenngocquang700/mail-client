package sendfile;

public class FormatMessage {
    private String from;
    private  String subject;
    private  String dateCreated;
    private  String contentMail;

    public FormatMessage() {
    }
    public FormatMessage(String from, String subject) {
        this.from = from;
        this.subject = subject;
    }
    public String getFrom(){
        return from;
    }
    public String getSubject(){
        return subject;
    }
    public String getContentMail(){ return contentMail;}
    public void setFrom(String to){
        this.from = from;
    }
    public void setSubject(String subject){
        this.subject = subject;
    }
    public void setContentMail(String contentMail) { this.contentMail = contentMail; }
}
