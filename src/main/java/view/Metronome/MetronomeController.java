package view.Metronome;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.MalformedURLException;


public class MetronomeController {
    public MetronomeController() throws MalformedURLException {
    }

    enum POWER{
        ON,OFF;
    }
    long BMP;
    POWER state= POWER.ON;
    Media metronomeSound = new Media(new File("src/sample/metronome.wav").toURI().toURL().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(metronomeSound);
    @FXML
    private Button OFFbutton;
    @FXML
    private Button startButton;
    @FXML
    private ComboBox<Integer> bmpChooser;
    @FXML
    private Button SetBmp;
    @FXML
    void SetIT(ActionEvent event) {
        BMPline.setValue(bmpChooser.getValue());
    }
    ObservableList<Integer> list= FXCollections.observableArrayList(
      250,500,750,1000
    );
    @FXML
    private Slider BMPline;
    private static final double INIT_VALUE=0;
    @FXML
    void getValue(MouseEvent event) {
        if(BMPline.valueProperty()==null)
            bmpChooser.setValue(1000);
        System.out.println(BMPline.valueProperty());
    }
    @FXML
    void PowerON(ActionEvent event) throws InterruptedException {
        state= POWER.ON;
        Thread thread= new Thread( ()-> {
            while (state == POWER.ON) {
                mediaPlayer.play();
                try {
                    Thread.sleep((long) BMPline.getValue());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mediaPlayer.stop();
                if(state== POWER.OFF) break;
            }
        } );
        thread.start();
    }
    @FXML
    void PowerOFF(ActionEvent event) {
        if(state== POWER.ON)
            state= POWER.OFF;
    }
    @FXML
    void initialize() {
        bmpChooser.setItems(list);
    }
}