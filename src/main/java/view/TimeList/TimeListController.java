package view.TimeList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.SS.StatisticServer;
import model.enums.CubeType;
import model.logic.Solve;
import model.wrappers.AVGwrapper;
import model.wrappers.ObservableWrapper;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

import static model.enums.AVG.*;


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
    @FXML
    private Label bestSingle;
    @FXML
    private Label bestAo5;
    @FXML
    private Label bestAo12;
    @FXML
    private Label count;
    @FXML
    private Label bestAo100;
    @FXML
    private Label bestAo50;


    public void setSSAndOw(StatisticServer ss, ObservableWrapper ow){
        this.ss = ss;
        this.ow = ow;
        currentType = ow.getCubeCurrType().get(0);
        addListeners(CubeType.TWOBYTWO);
        addListeners(CubeType.THREEBYTHREE);
        addListeners(CubeType.FOURBYFOUR);
        listView.setItems(ow.getListOfSolves(currentType));
        listView2.setItems(ow.getListAvg(currentType, Ao5));
        listView3.setItems(ow.getListAvg(currentType, Ao12));
        setBestAndCount();
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
                listView.scrollTo(Integer.MAX_VALUE);
                listView2.scrollTo(Integer.MAX_VALUE);
                listView3.scrollTo(Integer.MAX_VALUE);
                setBestAndCount();
            }
        });
        currentType = CubeType.THREEBYTHREE;

        listView.setFocusTraversable(false);
        listView.setCellFactory(listView1 -> {
            TextFieldListCell<Solve> cell = new TextFieldListCell<>();
            cell.setConverter(new SolveConverter());
            return cell;
        });
        listView.scrollTo(Integer.MAX_VALUE);

        listView2.setFocusTraversable(false);
        listView2.setCellFactory(listView1 -> {
            TextFieldListCell<AVGwrapper> cell = new TextFieldListCell<>();
            cell.setConverter(new AvgConverter());
            return cell;
        });
        listView2.scrollTo(Integer.MAX_VALUE);

        listView3.setFocusTraversable(false);
        listView3.setCellFactory(listView1 -> {
            TextFieldListCell<AVGwrapper> cell = new TextFieldListCell<>();
            cell.setConverter(new AvgConverter());
            return cell;
        });
        listView.scrollTo(Integer.MAX_VALUE);
        listView2.scrollTo(Integer.MAX_VALUE);
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
        d.setResizable(true);
        Window window = d.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(e -> window.hide());
        Stage stage = (Stage) window;
        if(currentType==CubeType.FOURBYFOUR){
            stage.setMinHeight(200);
        }else if(currentType==CubeType.THREEBYTHREE){
            stage.setMinHeight(170);
        }
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
        if(avg!=null && avg.isNET()){
            event.consume();
            return;
        }
        Dialog d = new Dialog();
        d.setResizable(true);
        Window window = d.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(e -> window.hide());
        Stage stage = (Stage) window;
        if(currentType==CubeType.FOURBYFOUR){
            stage.setMinHeight(400);
        }else if(currentType==CubeType.THREEBYTHREE){
            stage.setMinHeight(250);

        }else{
            stage.setMinHeight(200);
        }
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
        d.setResizable(true);
        Window window = d.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(e -> window.hide());
        Stage stage = (Stage) window;
        if(currentType==CubeType.FOURBYFOUR){
            stage.setMinHeight(730);
            stage.setMinWidth(600);
        }else if(currentType==CubeType.THREEBYTHREE){
            stage.setMinHeight(500);
            stage.setMinWidth(400);

        }else{
            stage.setMinHeight(400);
            stage.setMinWidth(250);

        }
        d.setTitle("Solve Info");
        d.setHeaderText("");
        SolveConverter converter = new SolveConverter();
        String str = "";
        if(avg.isNET()){
            event.consume();
            return;
        }
        for (int i = 11; i >= 0; i--) {
            str += 12 - i + ". " + converter.toString(ow.getListOfSolves(currentType).get(avg.getID() - i-1)) + '\n';
            str += ow.getListOfSolves(currentType).get(avg.getID() - i-1).getScramble() + "\n";
        }
        d.setContentText(str);
        d.show();
    }

    public void setBestAndCount(){
        if(ss.GiveMeMin(currentType).equals(new Timestamp(999999999))){
            bestSingle.setText("-");
        }else{
            bestSingle.setText(ss.GiveMeMin(currentType).toLocalDateTime().format(DateTimeFormatter.ofPattern("mm:ss.SSS")));
        }
        if(ss.GiveMeMinAVG(currentType, Ao5).getID()==-1){
            bestAo5.setText("-");
        }else{
            bestAo5.setText(ss.GiveMeMinAVG(currentType, Ao5).getAVG().toLocalDateTime().format(DateTimeFormatter.ofPattern("mm:ss.SSS")));
        }
        if(ss.GiveMeMinAVG(currentType, Ao12).getID()==-1){
            bestAo12.setText("-");
        }else{
            bestAo12.setText(ss.GiveMeMinAVG(currentType, Ao12).getAVG().toLocalDateTime().format(DateTimeFormatter.ofPattern("mm:ss.SSS")));
        }
        if(ss.GiveMeMinAVG(currentType, Ao50).getID()==-1){
            bestAo50.setText("-");
        }else{
            bestAo50.setText(ss.GiveMeMinAVG(currentType, Ao50).getAVG().toLocalDateTime().format(DateTimeFormatter.ofPattern("mm:ss.SSS")));
        }
        if(ss.GiveMeMinAVG(currentType, Ao100).getID()==-1){
            bestAo100.setText("-");
        }else{
            bestAo100.setText(ss.GiveMeMinAVG(currentType, Ao100).getAVG().toLocalDateTime().format(DateTimeFormatter.ofPattern("mm:ss.SSS")));
        }
        count.setText(String.valueOf(ow.getListOfSolves(currentType).size()));
    }

    private void addListeners(CubeType cubeType){

        ow.getListOfSolves(cubeType).addListener(new ListChangeListener<Solve>() {
            @Override
            public void onChanged(Change<? extends Solve> change) {
                listView.scrollTo(Integer.MAX_VALUE);
                listView2.scrollTo(Integer.MAX_VALUE);
                listView3.scrollTo(Integer.MAX_VALUE);
                setBestAndCount();
            }
        });
        ow.getListAvg(cubeType, Ao5).addListener(new ListChangeListener<AVGwrapper>() {
            @Override
            public void onChanged(Change<? extends AVGwrapper> change) {
                listView.scrollTo(Integer.MAX_VALUE);
                listView2.scrollTo(Integer.MAX_VALUE);
                listView3.scrollTo(Integer.MAX_VALUE);
                setBestAndCount();
            }
        });
        ow.getListAvg(cubeType, Ao12).addListener(new ListChangeListener<AVGwrapper>() {
            @Override
            public void onChanged(Change<? extends AVGwrapper> change) {
                listView.scrollTo(Integer.MAX_VALUE);
                listView2.scrollTo(Integer.MAX_VALUE);
                listView3.scrollTo(Integer.MAX_VALUE);
                setBestAndCount();
            }
        });
    }


    public TimeListController() {

    }

}
