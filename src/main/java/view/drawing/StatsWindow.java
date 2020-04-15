package view.drawing;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import model.SS.StatisticServer;
import model.SS.StatisticServerImplementation;
import model.enums.AVG;
import model.enums.CubeType;
import model.logic.Solve;
import model.logic.SolveImplementation;
import model.wrappers.AVGwrapper;
import model.wrappers.ObservableWrapper;

import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.*;

public class StatsWindow implements Initializable {

    Integer count = 30;
    ObservableWrapper morszczuk;
    CubeType type = CubeType.THREEBYTHREE;

    @FXML
    private LineChart<String, Integer> chart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private TextField countField;

    @FXML
    private Button updateButton;

    @FXML
    private ChoiceBox<String> cubeBox;

    @FXML
    void updateCount(ActionEvent event) {
        String newCount= countField.getText();
        if(!(newCount.matches("\\d+"))) return;
        count = Integer.parseInt(newCount);
        load();
    }

    @FXML
    private void load() {
        chart.getData().clear();
        XYChart.Series<String, Integer> data = new XYChart.Series<>();
        yAxis.setLabel("time");
        xAxis.setLabel("id");
        int i = 0;
        for(Solve a: morszczuk.getListOfSolves(type)) {
            if(i >= count) break;
            data.getData().add(new XYChart.Data<>(Integer.toString(i+1), (int) a.getTime().getTime()));
            i++;
        }
        data.setName("all");

        i=0;
        XYChart.Series<String, Integer> data2 = new XYChart.Series<>();
        for(AVGwrapper avg:morszczuk.getListAvg(type, AVG.Ao5)) {
            if(i >= count) break;
            data2.getData().add(new XYChart.Data<>(Integer.toString(i+1),(int)avg.getAVG().getTime()));
            i++;
        }
        data2.setName("Ao5");
        i=0;
        XYChart.Series<String, Integer> data3 = new XYChart.Series<>();
        for(AVGwrapper avg:morszczuk.getListAvg(type, AVG.Ao12)) {
            if (i >= count) break;
            data3.getData().add(new XYChart.Data<>(Integer.toString(i + 1), (int) avg.getAVG().getTime()));
            i++;
        }
        data3.setName("Ao12");
        chart.getData().addAll(data, data2, data3);
    }

    public void setOw(ObservableWrapper ow){
        morszczuk = ow;
        load();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chart.setAnimated(false);
        chart.setCreateSymbols(false);
        cubeBox.getItems().addAll("TWOBYTWO", "THREEBYTHREE", "FOURBYFOUR");
        cubeBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                try {
                    type = (CubeType) CubeType.class.getDeclaredField(t1).get(null);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                if(morszczuk!=null){
                    load();
                }
            }
        });
        cubeBox.setValue("THREEBYTHREE");
    }
}
