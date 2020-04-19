package view.Timer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.scene.input.InputEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.SS.StatisticServer;
import model.enums.AVG;
import model.enums.CubeType;
import model.enums.Running;
import model.enums.State;
import model.logic.*;
import model.wrappers.AVGwrapper;
import model.wrappers.ObservableWrapper;


import java.util.ArrayList;
import java.util.Date;


public class Controller {
    @FXML
    Pane mainPane;
    private StatisticServer ss = null;
    private ObservableWrapper ow = null;
    CubeType type = CubeType.THREEBYTHREE;
    private ScrambleGenerator Generator=new ScrambleGeneratorImplementation(type);

    enum StartOrStop {
        START, STOP;
    }

    StartOrStop whatToDo = StartOrStop.START;
    int mins = 0, secs = 0, millis = 0;
    Timeline timeline = new Timeline();
    void change(Text text) {
        if (millis == 1000) {
            secs++;
            millis = 0;
        }
        if (secs == 60) {
            mins++;
            secs = 0;
        }
        text.setText((((mins / 10) == 0) ? "0" : "") + mins + ":"
                + (((secs / 10) == 0) ? "0" : "") + secs + ":"
                + (((millis / 10) == 0) ? "00" : (((millis / 100) == 0) ? "0" : "")) + millis++);
    }
    void reset(){
        mins=secs=millis=0;
       // timePassed.setText("00:00:000");
        whatToDo=StartOrStop.START;
        ow.setRunning(Running.NO);
        Generator=new ScrambleGeneratorImplementation(type);
        ArrayList<Move> scramble = Generator.generate();
        Scramble.setText(Generator.scrambleToString(scramble));
        ow.setCurrentScramble(scramble);
    }
    @FXML
    private Text timePassed;
    @FXML
    private Text Scramble;
    @FXML
    void ResetAndGetReady() {
        if(whatToDo== StartOrStop.START)
            timePassed.setText("00:00:000");
        else{
            timeline.stop(); // tutaj zczytywanie do Solve
            ow.setRunning(Running.NO);
            long value=0;
            if(ss!=null){
                value+=millis+secs*1000+mins*60*1000-1;
                Solve solve=new SolveImplementation(new Date(),value, State.CORRECT, type);
                solve.setScramble(Scramble.getText());
                ss.insertSolve(solve);
            }
            ArrayList<Move> scramble = Generator.generate();
            Scramble.setText(Generator.scrambleToString(scramble));
            ow.setCurrentScramble(scramble);
            mins=secs=millis=0;
            whatToDo= StartOrStop.STOP;
        }
    }

    @FXML
    public void StartTimer() {
        if (whatToDo == StartOrStop.STOP) {
            whatToDo = StartOrStop.START;
        } else {
            timeline = new Timeline(new KeyFrame(Duration.millis(1), GO -> change(timePassed)));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.setAutoReverse(false);
            timeline.play();
            ow.setRunning(Running.YES);
            whatToDo = StartOrStop.STOP;
        }
    }

    @FXML
    void initialize() {
        assert timePassed != null : "fx:id=\"timePassed\" was not injected: check your FXML file 'timersample.fxml'.";
        EventHandler handler = new EventHandler<InputEvent>() {
            public void handle(InputEvent event) {
                ResetAndGetReady();
                event.consume();
            }

        };
        EventHandler handler2 = new EventHandler<InputEvent>() {
            public void handle(InputEvent event) {
                StartTimer();
                event.consume();
            }

        };
        mainPane.setOnKeyPressed(handler);
        mainPane.setOnKeyReleased(handler2);
    }

    public void setSSAndOw(StatisticServer ss, ObservableWrapper ow) {
        this.ss = ss;
        this.ow = ow;
        ow.getCubeCurrType().addListener((ListChangeListener<CubeType>) change -> {
            type = ow.getCubeCurrType().get(0);
            reset();
        });
        ArrayList<Move> scramble = Generator.generate();
        Scramble.setText(Generator.scrambleToString(scramble));
        ow.setCurrentScramble(scramble);
    }
}
