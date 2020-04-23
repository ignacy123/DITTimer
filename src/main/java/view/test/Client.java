package view.test;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.conn.Room;
import model.conn.ServerService;
import model.conn.ServerServiceImplementation;
import model.enums.CubeType;
import model.logic.Solve;

import java.util.ArrayList;
import java.util.Arrays;

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
    private Button joinTo;

    private ObservableList<Room> rooms;
    ServerService conn;
    @FXML
    void initialize(){
        conn = new ServerServiceImplementation(this);
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
        Pane pane = loader.load();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void joinRoom(ActionEvent event) {
        Room sel = (Room)roomsListView.getSelectionModel().getSelectedItem();
        RoomWindow roomWindow = new RoomWindow(conn, sel);
        conn.setWindow(roomWindow);
        roomWindow.joining=true;
        roomWindow.setName(String.valueOf(nameField.getCharacters()));
        try {
            roomWindow.start(roomWindow.classStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void sendRooms(ArrayList<Room> rooms){
        this.rooms.clear();
        this.rooms.addAll(rooms);
    }
    @FXML
    public void createRoom(){
        conn.createRoom((CubeType)cubeTypeChoiceBox.getValue(), String.valueOf(nameField.getCharacters()));
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
            roomWindow.start(roomWindow.classStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        refreshRooms();
    }
}
