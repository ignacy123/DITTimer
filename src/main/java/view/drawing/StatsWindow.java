package view.drawing;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import model.logic.Solve;
import model.logic.SolveImplementation;

import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.*;

public class StatsWindow implements Initializable {

    Integer count = Integer.MAX_VALUE;
    private ArrayList<Solve> morszczuk;

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
    void updateCount(ActionEvent event) {
        String newCount= countField.getText();
        if(!(newCount.matches("\\d+"))) return;
        count = Integer.parseInt(newCount);
        load(morszczuk);
    }

    @FXML
    private void load(Collection<Solve> inp) {
        chart.getData().clear();
        XYChart.Series<String, Integer> data = new XYChart.Series<>();
        int i = 0;
        for(Solve a: inp) {
            if(i >= count) break;
            data.getData().add(new XYChart.Data<>(a.getDate().toString(), (int) a.getTime().getTime()));
            i++;
        }
        chart.getData().add(data);
    }

    private void populate(Collection<Solve> inp) {
        for(int i = 0; i < 'z' - 'a'; i++) {
            Solve solve = new SolveImplementation();
            solve.setDate(new Date(2020+ i, Calendar.MARCH, i));
            solve.setTime(new Timestamp(1000));
            inp.add(solve);
        }
    }

    @FXML
    private void load() {
        load(morszczuk);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chart.setAnimated(false);
        morszczuk = new ArrayList<>();

        populate(morszczuk);
        load();
    }
}
