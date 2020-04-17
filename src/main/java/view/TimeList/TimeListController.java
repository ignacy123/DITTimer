package view.TimeList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.SS.StatisticServer;
import model.SS.StatisticServerImplementation;
import model.db.DatabaseService;
import model.db.DatabaseServiceImplementation;
import model.enums.AVG;
import model.enums.CubeType;
import model.enums.State;
import model.logic.ScrambleGenerator;
import model.logic.ScrambleGeneratorImplementation;
import model.logic.Solve;
import model.logic.SolveImplementation;
import model.wrappers.AVGwrapper;
import model.wrappers.ObservableWrapper;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static model.enums.AVG.Ao12;
import static model.enums.AVG.Ao5;


public class TimeListController extends Stage {
    @FXML
    private ListView listView;
    @FXML
    private ListView listView2;
    @FXML
    private ListView listView3;
    @FXML
    private Button deleteButton;
    @FXML
    private ChoiceBox<CubeType> choiceBox;
    private StatisticServer ss;
    private ObservableWrapper ow;
    private CubeType currentType;


    public void setSSAndOw(StatisticServer ss, ObservableWrapper ow){
        this.ss = ss;
        this.ow = ow;
        listView.setItems(ow.getListOfSolves(currentType));
        listView2.setItems(ow.getListAvg(currentType, Ao5));
        listView3.setItems(ow.getListAvg(currentType, Ao12));
    }

    @FXML
    void initialize() {
        choiceBox.setItems(FXCollections.observableArrayList(CubeType.TWOBYTWO, CubeType.THREEBYTHREE, CubeType.FOURBYFOUR));
        choiceBox.setValue(CubeType.THREEBYTHREE);
        choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                currentType = choiceBox.getItems().get((Integer) number2);
                ow.setCubeCurrType(currentType);
                listView.setItems(ow.getListOfSolves(currentType));
                listView2.setItems(ow.getListAvg(currentType, Ao5));
                listView3.setItems(ow.getListAvg(currentType, Ao12));
            }
        });
        currentType = CubeType.THREEBYTHREE;

        listView.setCellFactory(listView1 -> {
            TextFieldListCell<Solve> cell = new TextFieldListCell<>();
            cell.setConverter(new SolveConverter());
            return cell;
        });
        listView.scrollTo(Integer.MAX_VALUE);
        listView2.setCellFactory(listView1 -> {
            TextFieldListCell<AVGwrapper> cell = new TextFieldListCell<>();
            cell.setConverter(new AvgConverter());
            return cell;
        });
        listView2.scrollTo(Integer.MAX_VALUE);
        listView3.setCellFactory(listView1 -> {
            TextFieldListCell<AVGwrapper> cell = new TextFieldListCell<>();
            cell.setConverter(new AvgConverter());
            return cell;
        });
        listView3.scrollTo(Integer.MAX_VALUE);

    }


    public void handle(MouseEvent event) {
        if (!event.getButton().equals(MouseButton.PRIMARY)) {
            return;
        }

        if (event.getClickCount() != 2) {
            return;
        }
        Solve solve = (Solve) listView.getSelectionModel().getSelectedItem();
        String toStr = solve.toString();
        Dialog d = new Dialog();
        Window window = d.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(e -> window.hide());
        d.setTitle("Solve Info");
        d.setHeaderText("");
        d.setContentText(toStr);
        d.show();
    }

    public void handle2(MouseEvent event) {
        if (!event.getButton().equals(MouseButton.PRIMARY)) {
            return;
        }

        if (event.getClickCount() != 2) {
            return;
        }
        AVGwrapper avg = (AVGwrapper) listView2.getSelectionModel().getSelectedItem();
        if(avg.isNET()){
            event.consume();
            return;
        }
        Dialog d = new Dialog();
        Window window = d.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(e -> window.hide());
        d.setTitle("Average Info");
        d.setHeaderText("");
        SolveConverter converter = new SolveConverter();
        String str = "";
        for (int i = 4; i >= 0; i--) {
            str += 5 - i + ". " + converter.toString(ow.getListOfSolves(currentType).get(avg.getID() - i-1)) + '\n';
            str += ow.getListOfSolves(currentType).get(avg.getID() - i-1).getScramble() + "\n";
        }
        d.setContentText(str);
        d.show();
    }

    public void deleteSession(){
        ss.delete(currentType);
    }

    public void handle3(MouseEvent event) {
        if (!event.getButton().equals(MouseButton.PRIMARY)) {
            return;
        }

        if (event.getClickCount() != 2) {
            return;
        }
        AVGwrapper avg = (AVGwrapper) listView3.getSelectionModel().getSelectedItem();
        Dialog d = new Dialog();
        Window window = d.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(e -> window.hide());
        d.setTitle("Solve Info");
        d.setHeaderText("");
        SolveConverter converter = new SolveConverter();
        String str = "";
        if(avg.isNET()){
            event.consume();
            return;
        }
        for (int i = 11; i >= 0; i--) {
            str += 12 - i + ". " + converter.toString(ow.getListOfSolves(currentType).get(avg.getID() - i)) + '\n';
            str += ow.getListOfSolves(currentType).get(avg.getID() - i-1).getScramble() + "\n";
        }
        d.setContentText(str);
        d.show();
    }

    public TimeListController() {

    }

}
