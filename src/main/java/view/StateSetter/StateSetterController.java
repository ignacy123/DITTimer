package view.StateSetter;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import model.SS.StatisticServer;
import model.enums.AVG;
import model.enums.CubeType;
import model.enums.State;
import model.logic.Solve;
import model.wrappers.AVGwrapper;
import model.wrappers.ObservableWrapper;

import java.util.ArrayList;

public class StateSetterController {
    private StatisticServer ss = null;
    private ObservableWrapper ow = null;
    CubeType type = CubeType.THREEBYTHREE;

    @FXML
    Button DNFbutton;
    @FXML
    Button CorrectButton;
    @FXML
    Button TwoSecbutton;

    @FXML
    Button DELETEbutton;

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
    public void setSSAndOw(StatisticServer ss, ObservableWrapper ow) {
        this.ss = ss;
        this.ow = ow;
        ow.getCubeCurrType().addListener((ListChangeListener<CubeType>) change -> {
            type = ow.getCubeCurrType().get(0);
        });

    }
}
