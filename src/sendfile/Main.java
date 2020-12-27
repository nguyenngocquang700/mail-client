package sendfile;

import com.jfoenix.controls.JFXHamburger;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        try {
//            JFXHamburger show = new JFXHamburger();
//            show.setPadding(new Insets(10, 5, 10, 5));
            Parent root = FXMLLoader.load(this.getClass().getResource("Main.fxml"));
            primaryStage.setTitle("JavaMail");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
