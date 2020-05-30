package view.MoreOptions;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.SS.StatisticServer;
import model.conn.ServerService;
import model.conn.ServerServiceImplementation;
import model.enums.CubeType;
import model.logic.ArraySerializable;
import model.logic.KeyFile;
import model.wrappers.ObservableWrapper;
import view.drawing.ClickDrawer;
import view.drawing.CubeTraining;
import view.drawing.WeirdCube;
import view.test.Client;

import java.io.*;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class MoreOptionsController {
    private StatisticServer ss = null;
    private ObservableWrapper ow = null;
    ServerService conn;
    Stage toHide = null;
    Stage me = null;
    CubeType type = CubeType.THREEBYTHREE;
    Stage stage = new Stage();
    FileChooser fileChooser = new FileChooser();
    Stage StageFromOutside = null;
    Client client = null;
    @FXML
    private Button ImportButton;
    @FXML
    private Button PlayButton;
    @FXML
    private Button ExportButton;

    @FXML
    private Button ImportServerButton;
    @FXML
    private Button GoOnlineButton;
    @FXML
    private Button ExportServerButton;
    @FXML
    private Button stateButton;
    @FXML
    private Button wvButton;
    @FXML
    private Button Exit;

    @FXML
    void exit(ActionEvent event) {
        System.out.println("exit");
    }

    @FXML
    void ExportLocal(ActionEvent event) throws IOException {
        File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null) ss.exportToFile(selectedFile);
    }

    @FXML
    void Online(ActionEvent event) throws Exception {
        Client client = new Client();
        client.start(new Stage());
        toHide.close();
        me.close();
    }

    @FXML
    void Play(ActionEvent event) throws Exception {
        WeirdCube game = new WeirdCube();
        game.start(new Stage());
    }

    @FXML
    void stateChecker() throws Exception {
        ClickDrawer drawer = new ClickDrawer();
        drawer.start(new Stage());
    }
    @FXML
    void wvPractice() throws Exception {
        CubeTraining wv = new CubeTraining();
        wv.start(new Stage());
    }
    @FXML
    void ImportLocal(ActionEvent event) {
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) ss.importFromFile(selectedFile);
    }

    @FXML
    void ImportServer(ActionEvent event) throws IOException {
        String key = null;
        Dialog<String> KeyDialog = new Dialog<>();
        KeyDialog.setTitle("Key");
        KeyDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField pwd = new TextField();
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Please insert key: "), pwd);
        KeyDialog.getDialogPane().setContent(content);
        KeyDialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return pwd.getText();
            }
            return null;
        });
        Optional<String> pass = KeyDialog.showAndWait();
        if (pass.isPresent()) {
            key = pass.get();
        }
        KeyFile toSend = new KeyFile(key);
        if (conn == null) {
            conn = new ServerServiceImplementation(ss);
            conn.start();
        }
        conn.setKey(toSend);
        conn.sendKey();
    }

    public void GiveStage(Stage stage, Stage stage2) {
        toHide = stage;
        me = stage2;
    }

    @FXML
    void ExportServer(ActionEvent event) throws IOException {
        File automatic = new File("src/main/resources/Generated");
        ss.exportToFile(automatic);
        if (conn == null) {
            conn = new ServerServiceImplementation(ss);
            conn.start();
        }
        conn.setFile(automatic);
        conn.setMOC(this);
        conn.sendFile();
    }

    public void ReceivedCodePrinter(KeyFile code) {
        Stage stage = new Stage();
        stage.setTitle("This is your code!");
        StackPane root = new StackPane();
        Text text = new Text(code.getKey());
        text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        root.getChildren().add(text);
        Scene scene = new Scene(root, 200, 200);
        stage.setScene(scene);
        stage.show();
        conn.close();
        conn = null;
    }

    public void setSSAndOw(StatisticServer ss, ObservableWrapper ow) {
        this.ss = ss;
        this.ow = ow;
        ow.getCubeCurrType().addListener((ListChangeListener<CubeType>) change -> {
            type = ow.getCubeCurrType().get(0);
        });
    }

    public void setStage(Stage stage) {
        this.StageFromOutside = stage;
        stage.setOnCloseRequest(windowEvent -> {
                    if (conn != null) {
                        conn.close();
                    }
                }
        );
    }
}