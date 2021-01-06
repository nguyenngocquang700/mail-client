package sendfile;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
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
            return store.isConnected();
//            else return false;
//            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    @FXML
    public void Login() throws IOException {
        username = user.getText();
        if (username.isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter your Gmail account");
            return;
        }
        password = pass.getText();
        if (password.isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter your password account");
            return;
        }
        boolean flag = check(username,password);
        Login.setOnAction(Event -> {
            if (!flag){
//                infoBox("Please enter correct Email and Password", null, "Failed");
                showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter correct Email and Password");
            }
            else {
//            FXMLLoader fXMLLoader;
//            Parent root = FXMLLoader.load(this.getClass().getResource("Main.fxml"));
                try {
//                    Image i = new Image(new File("sendfile/img/Spinner-1s-200px.gif").toURI().toString());
//                    imgLoader.setImage(i);
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
            username = null;
            password = null;
        });
        exit.setOnAction(actionEvent -> Platform.exit());
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
