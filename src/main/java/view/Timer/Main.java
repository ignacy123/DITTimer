package view.Timer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    enum StartOrStop{
        START,STOP;
    }
    Timeline timeline;
    int mins = 0, secs = 0, millis = 0;
    Scene scene;
    StartOrStop whatToDo= StartOrStop.START;

    public static void main(String[] args) {
        launch(args);
    }
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
    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, k -> {
            if ( k.getCode() == KeyCode.SPACE){
                k.consume();
                KeyEvent k2 = new KeyEvent(null, null, null, KeyCode.K, false, false, false, false);
                k2.consume();
            }
        });


        Text text= new Text(10, 50, "00:00:000");
        Pane pane =new Pane();
        pane.getChildren().add(text);
        pane.getChildren().add(new Button("buttonwdwdwdwdwdwd"));
        pane.setPrefSize(500,400);
        Scene scene=new Scene(pane);


        scene.setOnKeyPressed(event -> {
            if(whatToDo==StartOrStop.START)
                text.setText("00:00:000");
            else{
                timeline.stop(); // tutaj zczytywanie do Solve
                mins=secs=millis=0;
                whatToDo=StartOrStop.STOP;
            }
        });

        scene.setOnKeyReleased(event -> {
            if(whatToDo== StartOrStop.STOP){
                whatToDo= StartOrStop.START;
            }else{
                timeline = new Timeline(new KeyFrame(Duration.millis(1), event1 -> change(text)));
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.setAutoReverse(false);
                timeline.play();
                whatToDo=StartOrStop.STOP;
            }
        });

        primaryStage.setTitle("StopWatch");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
