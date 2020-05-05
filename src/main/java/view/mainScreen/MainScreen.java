package view.mainScreen;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.SS.StatisticServer;
import model.enums.CubeType;
import model.enums.Running;
import model.wrappers.ObservableWrapper;
import view.ExportImport.ExportImportController;
import view.Metronome.MetronomeController;
import view.StateSetter.StateSetterController;
import view.TimeList.TimeListController;
import view.Timer.Controller;
import view.drawing.DrawScramble;
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
    @FXML
    Pane pane6;
    @FXML
    Pane pane7;
    private Text scramble;

    DrawScramble drawer;

    FXMLLoader listLoader = null;
    FXMLLoader statsLoader = null;
    FXMLLoader timerLoader = null;
    FXMLLoader metronomeLoader=null;
    FXMLLoader StateSetterLoader=null;
    FXMLLoader FileLoader=null;
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
        Pane child = (Pane) node4;
        scramble = (Text) child.getChildren().get(1);

        StateSetterLoader=new FXMLLoader(getClass().getClassLoader().getResource("StateSetter.fxml"));
        Node node5 = StateSetterLoader.load();
        pane5.getChildren().setAll(node5);
        FileLoader=new FXMLLoader(getClass().getClassLoader().getResource("FileService.fxml"));
        Node node7 = FileLoader.load();
        pane7.getChildren().setAll(node7);

        drawer=new DrawScramble();

        pane6.getChildren().add(drawer.getPane());
        //pane6.setMaxSize(600, 300);
        EventHandler handler = new EventHandler<InputEvent>() {
            public void handle (InputEvent event){
                Event.fireEvent(node4, event);
                event.consume();
            }
        };
        mainPane.setOnKeyPressed(handler);
        mainPane.setOnKeyReleased(handler);
    }

    public void setSSAndOw(StatisticServer ss, ObservableWrapper ow) throws IOException {
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
        controllerSet.init();
        ExportImportController FileController = FileLoader.getController();
        FileController.setSSAndOw(ss,ow);
        drawer.setOw(ow);
        ow.getRunning().addListener(new ListChangeListener<Running>() {
            @Override
            public void onChanged(Change<? extends Running> change) {
                if(ow.getRunning().get(0)==Running.YES){
                    pane.setVisible(false);
                    pane2.setVisible(false);
                    pane3.setVisible(false);
                    pane5.setVisible(false);
                    pane6.setVisible(false);
                    pane7.setVisible(false);
                    scramble.setVisible(false);

                }else{
                    pane.setVisible(true);
                    pane2.setVisible(true);
                    pane3.setVisible(true);
                    pane5.setVisible(true);
                    pane6.setVisible(true);
                    pane7.setVisible(true);
                    scramble.setVisible(true);
                }
            }
        });
    }
    public MainScreen() {

    }


}
