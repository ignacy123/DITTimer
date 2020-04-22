package view.test;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.conn.ClientRequest;
import model.conn.Room;
import model.conn.ServerService;
import model.conn.User;
import model.enums.ClientRequestType;
import model.wrappers.ObservableWrapper;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;

public class RoomWindow extends Application {

    @FXML
    private ListView<Time> timeList;

    @FXML
    private TextField scrambleField;

    @FXML
    private Button addTime;


    @FXML
    private ListView<String> playerList;
    Stage classStage = new Stage();
    ServerService jez;
    Room room;
    @FXML
    void testTime(ActionEvent event) {
        jez.sendTime(room, new Time(12, 12, 12));
    }
    public RoomWindow(ServerService conn, Room room) {
        jez=conn;
        this.room=room;
    }
    public void renderUsers(ArrayList<User> users) {
        ArrayList<String> names = new ArrayList<>();
        for(User xd: users) {
            names.add(xd.getName());
        }
        playerList.getItems().setAll(names);
}
    public void renderTimes(ArrayList<Time> times) {
        timeList.getItems().setAll(times);
    }
    @FXML
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Room.fxml"));
        loader.setController(this);
        Pane pane = loader.load();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        //fill things with data
        jez.getPlayers(room);
        jez.getTimes(room);
        stage.show();
    }
}
