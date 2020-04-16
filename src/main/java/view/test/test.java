package view.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.SS.StatisticServer;
import model.SS.StatisticServerImplementation;
import model.wrappers.ObservableWrapper;
import view.mainScreen.MainScreen;

public class  test extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("timer.fxml"));
        ObservableWrapper ow = new ObservableWrapper();
        StatisticServer ss = new StatisticServerImplementation(ow);
        Pane pane = loader.load();
        MainScreen controller = loader.getController();
        controller.setSSAndOw(ss, ow);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }
}
