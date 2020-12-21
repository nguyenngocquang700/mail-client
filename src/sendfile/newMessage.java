package sendfile;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class newMessage {
    public static void main(String[] args){ launch(args);}

    private static void launch(String[] args) {
    }

    public void start(Stage primaryStage){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("newMessage.fxml"));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
