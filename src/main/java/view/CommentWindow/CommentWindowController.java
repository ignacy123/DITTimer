package view.CommentWindow;


import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.SS.StatisticServer;
import model.enums.CubeType;
import model.wrappers.ObservableWrapper;



public class CommentWindowController {
    Stage stage=null;
    private StatisticServer ss = null;
    private ObservableWrapper ow = null;
    CubeType type = CubeType.THREEBYTHREE;
    @FXML
    private TextField myTextField;

    @FXML
    private Button AddButton;

    @FXML
    void AddTheComment(ActionEvent event) {
       // myTextField.setMaxLength(6);
        ss.addComment(type,myTextField.getText());
        stage.close();
    }

    public void setStage(Stage stage){
        this.stage=stage;
    }
    public void setSSAndOw(StatisticServer ss, ObservableWrapper ow) {
        this.ss = ss;
        this.ow = ow;
        ow.getCubeCurrType().addListener((ListChangeListener<CubeType>) change -> {
            type = ow.getCubeCurrType().get(0);
        });
    }

}
