package view.ExportImport;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.SS.StatisticServer;
import model.enums.CubeType;
import model.wrappers.ObservableWrapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExportImportController {
    private StatisticServer ss = null;
    private ObservableWrapper ow = null;
    CubeType type = CubeType.THREEBYTHREE;
    Stage stage=new Stage();
    FileChooser fileChooser = new FileChooser();
    File selectedFile;
    @FXML
    private Button ImportButton;
    @FXML
    private Button ExportButton;
    @FXML
    void ImportFromFile(ActionEvent event) {
        File selectedFile = fileChooser.showOpenDialog(stage);
        ss.importFromFile(selectedFile);
    }
    @FXML
    void ExportToFile(ActionEvent event) throws IOException {
        File selectedFile = fileChooser.showOpenDialog(stage);
        ss.exportToFile(selectedFile);
    }

    public void setSSAndOw(StatisticServer ss, ObservableWrapper ow){
        this.ss = ss;
        this.ow = ow;
        ow.getCubeCurrType().addListener((ListChangeListener<CubeType>) change -> {
            type = ow.getCubeCurrType().get(0);
        });
    }
}
