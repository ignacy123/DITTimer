package view.test;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.conn.ClientRequest;
import model.conn.Room;
import model.conn.ServerService;
import model.conn.User;
import model.enums.ClientRequestType;
import model.wrappers.ObservableWrapper;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;

public class RoomWindow extends Application {

    //@FXML
    //private ListView<Time> timeList;

    @FXML
    private TextField scrambleField;

    @FXML
    private Button addTime;

    @FXML
    private ListView msgBox;
    @FXML
    private TextField msgField;

    private ObservableList<String> chat;

    @FXML
    private HBox timesHolder;

    @FXML
    private ListView<Time> timeList0;

    @FXML
    private ListView<Time> timeList1;

    @FXML
    private ListView<Time> timeList2;

    @FXML
    private ListView<Time> timeList3;

    @FXML
    private ListView<Time> timeList4;

    @FXML
    private Text user0;

    @FXML
    private Text user1;

    @FXML
    private Text user2;

    @FXML
    private Text user3;

    @FXML
    private Text user4;
    @FXML
    void initialize(){
        //poiu.setVisible(true);
        chat = FXCollections.observableArrayList();
        msgBox.setItems(chat);
        chat.addAll("test");
    }
    @FXML

    private ListView<String> playerList;

    Stage classStage = new Stage();
    ServerService jez;
    Room room;
    boolean joining = false;
    String name="";
    void setName(String name) {
        this.name=name;
    }
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

    private ListView<Time> getList(int i) {
        switch(i) {
            case 0: return timeList0;
            case 1: return timeList1;
            case 2: return timeList2;
            case 3: return timeList3;
            case 4: return timeList4;
        }
        return null;
    }
    private Text getText(int i) {
        switch(i) {
            case 0: return user0;
            case 1: return user1;
            case 2: return user2;
            case 3: return user3;
            case 4: return user4;
        }
        return null;
    }
    private void hideLists() {
        for(int i = 0; i < 5; i++) {
            getList(i).setVisible(false);
            getText(i).setVisible(false);
        }
    }
    public void renderTimes(ConcurrentHashMap<User, ArrayList<Time>> merlin) {
        hideLists();
        ArrayList<User> userList = new ArrayList<>(merlin.keySet());
        Collections.sort(userList, Comparator.comparing(User::getName));
        int i = 0;
        for(User user: userList) {
            getList(i).getItems().setAll(merlin.get(user));
            getList(i).setVisible(true);
            getText(i).setText(user.getName());
            getText(i).setVisible(true);
            i++;
        }
    }

    @FXML
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Room.fxml"));
        loader.setController(this);
        Pane pane = loader.load();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        jez.getPlayers(room);
        jez.getTimes(room);
        stage.setOnCloseRequest(windowEvent -> {
            System.out.println("close");
            jez.leaveRoom(room);
        });
        stage.show();
    }

    @FXML
    public void sendMessage(KeyEvent event){
        if(event.getEventType()==KeyEvent.KEY_PRESSED && event.getCode()==KeyCode.ENTER){
            String msg = String.valueOf(msgField.getCharacters());
            if(msg.equals("")){
                return;
            }
            System.out.println("sending message: "+msg);
            jez.sendChat(room, msg);
            msgField.clear();
        }
    }

    @FXML
    public void getMessage(String msg){
        chat.addAll(msg);
    }
}
