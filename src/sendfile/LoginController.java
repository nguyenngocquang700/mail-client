package sendfile;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.mail.*;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class LoginController {
    public static String username;
    public static String password;
    @FXML
    public TextField user;
    public PasswordField pass;
    public JFXButton Login;
    public Button exit;
    public ImageView imgLoader;

    public boolean check(String user, String password)
    {
        try {
            // create properties field
            String host = "imap.gmail.com";
            Properties props = new Properties();
            props.setProperty("mail.imap.ssl.enable", "true");
            // set any other needed mail.imap.* properties here
            Session session = Session.getInstance(props);
            Store store = session.getStore("imap");
            store.connect(host, user, password);
            if(store.isConnected()==true){
                return true;
            }
            else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
    @FXML
    public void Login() throws IOException {


        Login.setOnAction(Event -> {
            username=user.getText();
            password=pass.getText();
            if(!username.isEmpty()&&!password.isEmpty()){
                if(check(username,password)==true){
                    try {
//                    Image i = new Image(new File("sendfile/img/Spinner-1s-200px.gif").toURI().toString());
//                    imgLoader.setImage(i);
                        user.clear();
                        pass.clear();
                        Parent main = (Parent) FXMLLoader.load(getClass().getResource("Main.fxml"));
                        Scene scene = new Scene(main);
                        Stage mainStage = (Stage)((Node) Event.getSource()).getScene().getWindow();
                        mainStage.setScene(scene);
                        mainStage.setTitle("JavaMail");
                        mainStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter Email and Password CORRECT");
                }

            }
            else {

                showAlert(Alert.AlertType.ERROR, "Form Error!", "Email and Password IS NULL");
            }

        });
        exit.setOnAction((ActionEvent event) -> {
            System.exit(0);
        });
    }

    public static void infoBox(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

    private static void showAlert(Alert.AlertType alertType, String title, String message){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}