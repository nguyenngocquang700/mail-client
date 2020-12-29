package sendfile;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class messageListViewCell extends ListCell<FormatMessage> {
    @FXML
    private Label fromLb;
    @FXML
    private Label subjectLb;
    @FXML
    private Label dateLb;
    @FXML
    private GridPane gridPane;
    @FXML
    private Label contentMailLb;

    private FXMLLoader mLLoader;
    @Override
    protected void updateItem(FormatMessage FormatMessage, boolean empty){
        super.updateItem(FormatMessage, empty);
        if (empty || FormatMessage == null){
            setText(null);
            setGraphic(null);
        }
        else {
            if (mLLoader == null){
                mLLoader = new FXMLLoader(getClass().getResource("ListCell.fxml"));
                mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            fromLb.setText(String.valueOf(FormatMessage.getFrom()));
            subjectLb.setText(String.valueOf(FormatMessage.getSubject()));
            setText(null);
            setGraphic(gridPane);
        }
    }
}
