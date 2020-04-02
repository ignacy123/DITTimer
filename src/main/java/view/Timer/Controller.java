package view.Timer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller {
    enum StartOrStop{
        START,STOP;
    }
    StartOrStop whatToDo= StartOrStop.START;
    int mins = 0, secs = 0, millis = 0;
    Timeline timeline=new Timeline();
    void change(Text text) {
        if(millis == 1000) {
            secs++;
            millis = 0;
        }
        if(secs == 60) {
            mins++;
            secs = 0;
        }
        text.setText((((mins/10) == 0) ? "0" : "") + mins + ":"
                + (((secs/10) == 0) ? "0" : "") + secs + ":"
                + (((millis/10) == 0) ? "00" : (((millis/100) == 0) ? "0" : "")) + millis++);
    }
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button LH1;

    @FXML
    private Button RH1;

    @FXML
    private Text timePassed;

    @FXML
    void ResetAndGetReady(KeyEvent event) {
        if(whatToDo== StartOrStop.START)
             timePassed.setText("00:00:000");
        else{
            timeline.stop(); // tutaj zczytywanie do Solve




            mins=secs=millis=0;
            whatToDo= StartOrStop.STOP;
        }
    }

    @FXML
    void StartTimer(KeyEvent event) {
        if(whatToDo== StartOrStop.STOP){
            whatToDo= StartOrStop.START;
        }else{
            timeline = new Timeline(new KeyFrame(Duration.millis(1), GO -> change(timePassed)));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.setAutoReverse(false);
            timeline.play();
            whatToDo= StartOrStop.STOP;
        }
    }

    @FXML
    void initialize() {
        assert LH1 != null : "fx:id=\"LH1\" was not injected: check your FXML file 'timersample.fxml'.";
        assert RH1 != null : "fx:id=\"RH1\" was not injected: check your FXML file 'timersample.fxml'.";
        assert timePassed != null : "fx:id=\"timePassed\" was not injected: check your FXML file 'timersample.fxml'.";

    }
}
