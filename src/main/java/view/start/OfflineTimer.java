package view.start;

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

public class OfflineTimer extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("timer.fxml"));
        ObservableWrapper ow = new ObservableWrapper();
        StatisticServer ss = new StatisticServerImplementation(ow);
        Pane pane = loader.load();
        MainScreen controller = loader.getController();
        Scene scene = new Scene(pane);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("css0.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
        controller.giveStage(stage); // to close if go online
        stage.addEventFilter(KeyEvent.KEY_PRESSED, k -> {
            if (k.getCode() == KeyCode.SPACE) {
                k.consume();
                KeyEvent k2 = new KeyEvent(KeyEvent.KEY_PRESSED, "k", null, KeyCode.K, false, false, false, false);
                KeyEvent.fireEvent(pane, k2);
            }
        });

        stage.addEventFilter(KeyEvent.KEY_RELEASED, k -> {
            if (k.getCode() == KeyCode.SPACE) {
                k.consume();
                KeyEvent k2 = new KeyEvent(KeyEvent.KEY_RELEASED, "k", null, KeyCode.K, false, false, false, false);
                KeyEvent.fireEvent(pane, k2);
            }
        });
        controller.setSSAndOw(ss, ow);
        stage.setOnCloseRequest(event -> {
            controller.KillMetronome();
        });
    }
}
