package view.StateSetter;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.SS.StatisticServer;
import model.enums.CubeType;
import model.enums.State;
import model.wrappers.ObservableWrapper;
import view.CommentWindow.CommentWindowController;

import java.io.IOException;
public class StateSetterController {
    private StatisticServer ss = null;
    private ObservableWrapper ow = null;
    CubeType type = CubeType.THREEBYTHREE;
    FXMLLoader CommentWindowLoader=null;
    CommentWindowController controller=null;
    Scene scene=null;
    Pane pane=null;
    Stage stage=null;
    @FXML
    Button DNFbutton;
    @FXML
    Button CorrectButton;
    @FXML
    Button TwoSecbutton;
    @FXML
    Button DELETEbutton;
    @FXML
    private Button CommentButton;

    @FXML
    void AddComment(ActionEvent event){
        stage.show();
    }
    @FXML
    void MakeLastDNF(ActionEvent event) {
        ss.ChangeStateLast(type, State.DNF);
    }
    @FXML
    void Correction(ActionEvent event) {
        ss.ChangeStateLast(type,State.CORRECT);
    }
    @FXML
    void MakeLastDelete(ActionEvent event) {
        ss.DeleteLast(type);
    }

    @FXML
    void MakeLastTwoSec(ActionEvent event) {
        ss.ChangeStateLast(type, State.TWOSECPENALTY);
    }
    public void setSSAndOw(StatisticServer ss, ObservableWrapper ow){
        this.ss = ss;
        this.ow = ow;
        ow.getCubeCurrType().addListener((ListChangeListener<CubeType>) change -> {
            type = ow.getCubeCurrType().get(0);
        });
    }
    public void init() throws IOException {
        CommentWindowLoader=new FXMLLoader(getClass().getClassLoader().getResource("CommentWindow.fxml"));
        pane= CommentWindowLoader.load();
        controller=CommentWindowLoader.getController();
        controller.setSSAndOw(ss,ow);
        scene=new Scene(pane);
        stage=new Stage();
        stage.setScene(scene);
        controller.setStage(stage);
    }
}
