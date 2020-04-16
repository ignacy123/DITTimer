package view.drawing;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
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
import javafx.scene.control.ListView;
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
        String newCount = countField.getText();
        if (!(newCount.matches("\\d+"))) return;
        count = Integer.parseInt(newCount);
        load();
    }

    @FXML
    private void load() {
        chart.getData().clear();
        XYChart.Series<String, Integer> data = new XYChart.Series<>();
        yAxis.setLabel("time");
        xAxis.setLabel("id");
        //size - how much-1 (max with 0) up to size -1
        for (int j = Math.max(morszczuk.getListOfSolves(type).size() - count, 0), i = 0; j < morszczuk.getListOfSolves(type).size(); j++, i++) {
            Solve a = morszczuk.getListOfSolves(type).get(j);
            data.getData().add(new XYChart.Data<>(Integer.toString(i + 1), (int) a.getTime().getTime()));
        }
        data.setName("all");
        //data.getData().addAll(morszczuk.getListOfSolves(type).subList(2,3));
        XYChart.Series<String, Integer> data2 = new XYChart.Series<>();
        for (int j = Math.max(morszczuk.getListAvg(type, AVG.Ao5).size() - count, 0), i = 0; j < morszczuk.getListAvg(type, AVG.Ao5).size(); j++, i++) {
            AVGwrapper avg = morszczuk.getListAvg(type, AVG.Ao5).get(j);
            data2.getData().add(new XYChart.Data<>(Integer.toString(i + 1), (int) avg.getAVG().getTime()));
        }
        data2.setName("Ao5");
        XYChart.Series<String, Integer> data3 = new XYChart.Series<>();
        for (int j = Math.max(morszczuk.getListAvg(type, AVG.Ao12).size() - count, 0), i = 0; j < morszczuk.getListAvg(type, AVG.Ao12).size(); j++, i++) {
            AVGwrapper avg = morszczuk.getListAvg(type, AVG.Ao12).get(j);
            data3.getData().add(new XYChart.Data<>(Integer.toString(i + 1), (int) avg.getAVG().getTime()));
        }
        data3.setName("Ao12");
        chart.getData().addAll(data, data2, data3);
    }

    public void setOw(ObservableWrapper ow) {
        morszczuk = ow;
        //create listeners on obs lists
        ArrayList<CubeType> types = new ArrayList<CubeType>();
        types.add(CubeType.THREEBYTHREE);
        types.add(CubeType.FOURBYFOUR);
        types.add(CubeType.TWOBYTWO);
        morszczuk.getCubeCurrType().addListener(new ListChangeListener<CubeType>() {
            @Override
            public void onChanged(Change<? extends CubeType> change) {
                type = morszczuk.getCubeCurrType().get(0);
                load();
            }
        });
        for (CubeType type : types) {
            morszczuk.getListOfSolves(type).addListener(new ListChangeListener<Solve>() {
                @Override
                public void onChanged(Change<? extends Solve> change) {
                    load();
                }
            });
        }
        for (CubeType type : types) {
            morszczuk.getListAvg(type, AVG.Ao12).addListener(new ListChangeListener<AVGwrapper>() {
                @Override
                public void onChanged(Change<? extends AVGwrapper> change) {
                    load();
                }
            });
        }
        for (CubeType type : types) {
            morszczuk.getListAvg(type, AVG.Ao5).addListener(new ListChangeListener<AVGwrapper>() {
                @Override
                public void onChanged(Change<? extends AVGwrapper> change) {
                    load();
                }
            });
        }
        load();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chart.setAnimated(false);
        chart.setCreateSymbols(false);
    }
}
