package view.mainScreen;

import javafx.collections.ListChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.SS.StatisticServer;
import model.enums.CubeType;
import model.enums.Running;
import model.wrappers.ObservableWrapper;
import view.Metronome.MetronomeController;
import view.StateSetter.StateSetterController;
import view.TimeList.TimeListController;
import view.Timer.Controller;
import view.drawing.StatsWindow;

import java.io.IOException;

public class MainScreen extends Stage {
    @FXML
    Pane mainPane;
    @FXML
    Pane pane;
    @FXML
    Pane pane2;
    @FXML
    Pane pane3;
    @FXML
    Pane pane4;
    @FXML
    Pane pane5;

    FXMLLoader listLoader = null;
    FXMLLoader statsLoader = null;
    FXMLLoader timerLoader = null;
    FXMLLoader metronomeLoader=null;
    FXMLLoader StateSetterLoader=null;
    @FXML
    void initialize() throws IOException {
        listLoader = new FXMLLoader(getClass().getClassLoader().getResource("TimeList.fxml"));
        Node node = listLoader.load();
        pane.getChildren().setAll(node);

        statsLoader = new FXMLLoader(getClass().getClassLoader().getResource("StatsWindow.fxml"));
        Node node2 = statsLoader.load();
        pane2.getChildren().setAll(node2);

        metronomeLoader=new FXMLLoader(getClass().getClassLoader().getResource("metronome.fxml"));
        Node node3 = metronomeLoader.load();
        pane3.getChildren().setAll(node3);

        timerLoader = new FXMLLoader(getClass().getClassLoader().getResource("timersample.fxml"));
        Node node4 = timerLoader.load();
        pane4.getChildren().setAll(node4);

        StateSetterLoader=new FXMLLoader(getClass().getClassLoader().getResource("StateSetter.fxml"));
        Node node5 = StateSetterLoader.load();
        pane5.getChildren().setAll(node5);

        EventHandler handler = new EventHandler<InputEvent>() {
            public void handle (InputEvent event){
                Event.fireEvent(node4, event);
                event.consume();
            }
        };
        mainPane.setOnKeyPressed(handler);
        mainPane.setOnKeyReleased(handler);
    }

    public void setSSAndOw(StatisticServer ss, ObservableWrapper ow){
        TimeListController controller = listLoader.getController();
        controller.setSSAndOw(ss, ow);
        StatsWindow controller2 = statsLoader.getController();
        controller2.setOw(ow);
        Controller controller3 = timerLoader.getController();
        controller3.setSSAndOw(ss, ow);
        MetronomeController controllerMet = metronomeLoader.getController();
        controllerMet.setSSAndOw(ss,ow);
        StateSetterController controllerSet = StateSetterLoader.getController();
        controllerSet.setSSAndOw(ss,ow);

        ow.getRunning().addListener(new ListChangeListener<Running>() {
            @Override
            public void onChanged(Change<? extends Running> change) {
                if(ow.getRunning().get(0)==Running.YES){
                    pane.setVisible(false);
                    pane2.setVisible(false);
                    pane3.setVisible(false);
                    pane5.setVisible(false);
                }else{
                    pane.setVisible(true);
                    pane2.setVisible(true);
                    pane3.setVisible(true);
                    pane5.setVisible(true);
                }
            }
        });
    }
    public MainScreen() {

    }


}
