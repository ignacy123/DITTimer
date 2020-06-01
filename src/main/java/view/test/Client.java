package view.test;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.conn.Room;
import model.conn.ServerService;
import model.conn.ServerServiceImplementation;
import model.enums.CubeType;
import model.logic.AES;
import java.util.ArrayList;
import java.util.Optional;

public class Client extends Application {
    @FXML
    private Button createButton;
    @FXML
    private Button refreshButton;
    @FXML
    private ListView roomsListView;
    @FXML
    private ChoiceBox cubeTypeChoiceBox;
    @FXML
    private TextField nameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private CheckBox passwordCheckBox;

    private ObservableList<Room> rooms;
    ServerService conn;
    private RoomWindow roomWindow;


    @FXML
    void initialize(){
        System.out.println("init");
        passwordCheckBox.selectedProperty().addListener(observable -> {
            if(passwordCheckBox.isSelected()){
                passwordField.setVisible(true);
            }else{
                passwordField.setVisible(false);
            }
        });
        conn = new ServerServiceImplementation(this, null);
        conn.start();
        rooms = FXCollections.observableArrayList();
        cubeTypeChoiceBox.setItems(FXCollections.observableArrayList(CubeType.TWOBYTWO, CubeType.THREEBYTHREE, CubeType.FOURBYFOUR));
        cubeTypeChoiceBox.setValue(CubeType.THREEBYTHREE);
        roomsListView.setItems(rooms);
        roomsListView.setCellFactory(listView -> {
            TextFieldListCell<Room> cell = new TextFieldListCell<>();
            cell.setConverter(new RoomConverter());
            return cell;
        });
        refreshRooms();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("client.fxml"));
        loader.setController(this);
        Pane pane = loader.load();
        Scene scene = new Scene(pane);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("css0.css").toExternalForm());
        stage.setScene(scene);
        stage.setOnCloseRequest((windowEvent) -> {
            System.out.println("Closing client");
            if(conn!=null){
                conn.close();
            }
        });
        stage.show();
    }
    @FXML
    void joinRoom(MouseEvent event) {
        System.out.println("p");
        if (!event.getButton().equals(MouseButton.PRIMARY)) {
            return;
        }
        if (event.getClickCount() != 2) {
            return;
        }

        Room room = (Room)roomsListView.getSelectionModel().getSelectedItem();
        String password = null;
        if(room==null){
            return;
        }
        if(room.isPrivate()){
            Dialog<String> passwordDialog = new Dialog<>();
            passwordDialog.setTitle("Password");
            passwordDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            PasswordField pwd = new PasswordField();
            HBox content = new HBox();
            content.setAlignment(Pos.CENTER_LEFT);
            content.setSpacing(10);
            content.getChildren().addAll(new Label("Please insert password: "), pwd);
            passwordDialog.getDialogPane().setContent(content);
            passwordDialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    return pwd.getText();
                }
                return null;
            });
            Optional<String> pass = passwordDialog.showAndWait();
            if(pass.isPresent()){
                password = pass.get();
            }
        }
        String encryptedPass=null;
        if(password!=null){
            encryptedPass=AES.encrypt(password, "59929") ;
        }
        conn.joinRoom(room, String.valueOf(nameField.getCharacters()), encryptedPass);
    }
    @FXML
    public void sendRooms(ArrayList<Room> rooms){
        this.rooms.clear();
        this.rooms.addAll(rooms);
    }
    @FXML
    public void createRoom(){
        System.out.println("xd");
        String password = null;
        if(passwordCheckBox.isSelected()){
            password = String.valueOf(passwordField.getCharacters());
        }
        String encryptedPass=null;
        if(password!=null){
            encryptedPass=AES.encrypt(password, "59929") ;
        }
        conn.createRoom((CubeType)cubeTypeChoiceBox.getValue(), String.valueOf(nameField.getCharacters()), passwordCheckBox.isSelected(), encryptedPass);
    }

    @FXML
    public void refreshRooms(){
        conn.requestRooms();
    }

    @FXML
    public void roomHasBeenCreated(Room room) {
        if(room==null){
            System.out.println("Room couldn't be created.");
            return;
        }
        System.out.println("I requested a room and it has been created ="+room+ ". A new view should start now.");
        RoomWindow roomWindow = new RoomWindow(conn, room);

        conn.setWindow(roomWindow);
        try {
            Stage stage = (Stage) roomsListView.getScene().getWindow();
            roomWindow.clientStage=stage;
            roomWindow.classStage.initOwner(stage);
            stage.hide();
            //stage.close();
            roomWindow.start(roomWindow.classStage);
            roomWindow.getHostPermissions();
        } catch (Exception e) {
            e.printStackTrace();
        }
        refreshRooms();
    }
    @FXML
    public void roomAccessHasBeenGranted(Room room){
        System.out.println("I've requested an access to a room and server has granted it to me. A new view should start now. Room: "+room);
        Platform.runLater(() -> {
            RoomWindow roomWindow = new RoomWindow(conn, room);
            conn.setWindow(roomWindow);
            roomWindow.setName(String.valueOf(nameField.getCharacters()));
            try {
                Stage stage = (Stage) roomsListView.getScene().getWindow();
                roomWindow.classStage.initOwner(stage);
                roomWindow.clientStage=stage;
                stage.hide();//hide this window
                roomWindow.start(roomWindow.classStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    @FXML
    public void wrongPassword(){
        System.out.println("I've inserted a wrong password and server is letting me know about that.");
    }
    @FXML
    public void roomFull(){
        System.out.println("I've tried to join a full room and server is letting me know about that.");
    }
}
