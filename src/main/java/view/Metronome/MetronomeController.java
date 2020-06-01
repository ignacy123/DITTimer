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
import model.SS.StatisticServer;
import model.enums.Running;
import model.wrappers.ObservableWrapper;

import java.io.File;
import java.net.MalformedURLException;


public class MetronomeController {
    public MetronomeController() throws MalformedURLException {}
    private StatisticServer ss = null;
    private ObservableWrapper ow = null;
    Thread thread;
    boolean wasDone=false;
    enum POWER{ON,OFF}
    POWER state= POWER.OFF;
    Media metronomeSound = new Media(new File("src/main/resources/metronome.wav").toURI().toURL().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(metronomeSound);
    @FXML
    public Button OFFbutton;
    @FXML
    private Button startButton;
    @FXML
    private ComboBox<Integer> bmpChooser;
    @FXML
    private Button SetBmp;
    @FXML
    void SetIT(ActionEvent event) {
        if(bmpChooser.getValue()!=null) BMPline.setValue(bmpChooser.getValue());
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
    }
    @FXML
    synchronized void PowerON(ActionEvent event) {
        state = POWER.ON;
        if (!wasDone) {
            thread = new Thread(() -> {
                while (state == POWER.ON) {
                    if (ow.getRunning().get(0) == Running.YES)
                        mediaPlayer.play();
                    try {
                        Thread.sleep((long) BMPline.getValue());
                    } catch (InterruptedException e) {
                        wasDone = false;
                        break;
                    }
                    mediaPlayer.stop();
                    if (state == POWER.OFF) break;
                }
            });
            wasDone=true;
            thread.start();
        }
    }
    @FXML
    void PowerOFF(ActionEvent event) {
        if(state== POWER.ON){
            state= POWER.OFF;
            thread.interrupt();
        }

    }
    @FXML
    void initialize() {
        bmpChooser.setItems(list);
    }

    public void setSSAndOw(StatisticServer ss, ObservableWrapper ow) {
        this.ss = ss;
        this.ow = ow;
    }
}
