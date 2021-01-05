package sendfile;

import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextFlow;

import java.util.List;

public class showMessageController {
    @FXML
    public TextFlow showMessage;
    @FXML 
    public Label subjectMessageRecv;
    public Label fromMessageRecv;
    public Label dateMessageRecv;
    @FXML
    private ListView<FormatMessage> listMessageViewParent;
    public void showMessage(){

            //Detecting mouse clicked
            listMessageViewParent.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent arg0) {
                    //Check wich list index is selected then set txtContent value for that index
//                if (listMessageViewParent.getSelectionModel().getSelectedIndex() == 0) {
//                    System.out.println("");
//                }
                }
            });
            listMessageViewParent.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends FormatMessage> ov, FormatMessage old_val, FormatMessage new_val) -> {
                subjectMessageRecv.setText(listMessageViewParent.getSelectionModel().getSelectedItem().getSubject());
//                fromMessageRecv.setText(listMessageViewParent.getSelectionModel().getSelectedItems().);
//                dateMessageRecv.setText(listMessageViewParent.getSelectionModel().getSelectedItem().getDateCreated());
//            showCompose.getChildren().add(showMessageRecv);
            });


    }
}
