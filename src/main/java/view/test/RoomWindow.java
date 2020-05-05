package view.test;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import model.conn.ClientRequest;
import model.conn.Room;
import model.conn.ServerService;
import model.conn.User;
import model.enums.ClientRequestType;
import model.enums.CubeType;
import model.enums.State;
import model.logic.*;
import model.wrappers.AVGwrapper;
import model.wrappers.ObservableWrapper;
import view.TimeList.AvgConverter;
import view.TimeList.SolveConverter;

import java.sql.Time;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RoomWindow extends Application {

    //@FXML
    //private ListView<Time> timeList;
    @FXML
    private AnchorPane mainPane;

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
    private ListView<Solve> timeList0;

    @FXML
    private ListView<Solve> timeList1;

    @FXML
    private ListView<Solve> timeList2;

    @FXML
    private ListView<Solve> timeList3;

    @FXML
    private ListView<Solve> timeList4;

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
    private ListView<String> playerList;
    @FXML
    private Text Scramble;
    @FXML
    private Text timePassed;
    @FXML
    private Button nextScramble;
    @FXML
    ChoiceBox stateChoiceBox;
    @FXML
    Button leaveButton;

    Stage classStage = new Stage();
    ServerService jez;
    Room room;
    String name = "";
    ScrambleGenerator generator;
    boolean canRun = false;
    boolean isRunning = false;
    int mins = 0, secs = 0, millis = 0;
    Timeline timeline;
    Solve currentSolve;
    boolean hasBeenSent = false;

    void setName(String name) {
        this.name = name;
    }

    @FXML
    void initialize() {
        timeList0.setCellFactory(listView1 -> {
            TextFieldListCell<Solve> cell = new TextFieldListCell<>();
            cell.setConverter(new SolveConverter());
            return cell;
        });
        timeList0.scrollTo(Integer.MAX_VALUE);
        timeList1.setCellFactory(listView1 -> {
            TextFieldListCell<Solve> cell = new TextFieldListCell<>();
            cell.setConverter(new SolveConverter());
            return cell;
        });
        timeList1.scrollTo(Integer.MAX_VALUE);
        timeList2.setCellFactory(listView1 -> {
            TextFieldListCell<Solve> cell = new TextFieldListCell<>();
            cell.setConverter(new SolveConverter());
            return cell;
        });
        timeList2.scrollTo(Integer.MAX_VALUE);
        timeList3.setCellFactory(listView1 -> {
            TextFieldListCell<Solve> cell = new TextFieldListCell<>();
            cell.setConverter(new SolveConverter());
            return cell;
        });
        timeList3.scrollTo(Integer.MAX_VALUE);
        timeList4.setCellFactory(listView1 -> {
            TextFieldListCell<Solve> cell = new TextFieldListCell<>();
            cell.setConverter(new SolveConverter());
            return cell;
        });
        timeList4.scrollTo(Integer.MAX_VALUE);
        chat = FXCollections.observableArrayList();
        msgBox.setItems(chat);
        Scramble.setVisible(false);
        nextScramble.setVisible(false);
        generator = new ScrambleGeneratorImplementation(CubeType.THREEBYTHREE);

        EventHandler handler = new EventHandler<InputEvent>() {
            public void handle(InputEvent event) {
                if (!msgField.isFocused()) {
                    stopCounting();
                }
                event.consume();
            }

        };
        EventHandler handler2 = new EventHandler<InputEvent>() {
            public void handle(InputEvent event) {
                if (!msgField.isFocused()) {
                    startCounting();
                }
                event.consume();
            }

        };
        mainPane.setOnKeyPressed(handler);
        mainPane.setOnKeyReleased(handler2);
        mainPane.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.SPACE) {
                    stopCounting();
                    t.consume();
                }

            }
        });
        mainPane.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.SPACE) {
                    startCounting();
                    t.consume();
                }
                if (t.getCode() == KeyCode.ENTER) {
                    t.consume();
                }
            }
        });
        stateChoiceBox.setItems(FXCollections.observableArrayList(State.CORRECT, State.TWOSECPENALTY, State.DNF));
        stateChoiceBox.setValue(State.CORRECT);
    }

    void startCounting() {
        if (!canRun) {
            return;
        }
        if (isRunning) {
            return;
        }
        isRunning = true;
        timeline = new Timeline(new KeyFrame(Duration.millis(1), GO -> change(timePassed)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);
        timeline.play();

    }

    void stopCounting() {
        if (!isRunning) {
            return;
        }
        isRunning = false;
        timeline.stop();
        long value = 0;
        value += millis + secs * 1000 + mins * 60 * 1000 - 1;
        currentSolve = new SolveImplementation(new Date(), value, State.CORRECT, room.getType());
        currentSolve.setScramble(Scramble.getText());
        canRun = false;
        hasBeenSent = false;
    }

    void change(Text text) {
        if (millis == 1000) {
            secs++;
            millis = 0;
        }
        if (secs == 60) {
            mins++;
            secs = 0;
        }
        text.setText((((mins / 10) == 0) ? "0" : "") + mins + ":"
                + (((secs / 10) == 0) ? "0" : "") + secs + ":"
                + (((millis / 10) == 0) ? "00" : (((millis / 100) == 0) ? "0" : "")) + millis++);
    }

    @FXML
    void sendTime() {
        if (hasBeenSent) {
            return;
        }
        currentSolve.setState((State) stateChoiceBox.getSelectionModel().getSelectedItem());
        jez.sendTime(room, currentSolve);
        hasBeenSent = true;
    }

    public RoomWindow(ServerService conn, Room room) {
        jez = conn;
        this.room = room;
    }

    public void renderUsers(ArrayList<User> users) {
        ArrayList<String> names = new ArrayList<>();
        for (User xd : users) {
            names.add(xd.getName());
        }
        playerList.getItems().setAll(names);
    }

    private ListView<Solve> getList(int i) {
        try {
            return (ListView<Solve>) getClass().getDeclaredField("timeList" + i).get(this);
        } catch (Exception ignored) {
        }
        return null;
    }

    private Text getText(int i) {
        try {
            return (Text) getClass().getDeclaredField("user" + i).get(this);
        } catch (Exception ignored) {
        }
        return null;
    }

    private void hideLists() {
        for (int i = 0; i < 5; i++) {
            getList(i).setVisible(false);
            getText(i).setVisible(false);
        }
    }

    public void renderTimes(ConcurrentHashMap<User, ArrayList<Solve>> merlin) {
        hideLists();
        ArrayList<User> userList = new ArrayList<>(merlin.keySet());
        Collections.sort(userList, Comparator.comparing(User::getName));
        int i = 0;
        for (User user : userList) {
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
            jez.close();
        });
        stage.show();
    }

    @FXML
    public void sendMessage(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_PRESSED && event.getCode() == KeyCode.ENTER) {
            String msg = String.valueOf(msgField.getCharacters());
            if (msg.equals("")) {
                return;
            }
            System.out.println("sending message: " + msg);
            jez.sendChat(room, msg);
            msgField.clear();
            Scramble.requestFocus();
            event.consume();
        }
    }

    @FXML
    public void requestScramble() {
        System.out.println("i want a scramble");
        System.out.println(room);
        jez.requestScramble(room);
    }

    @FXML
    public void getMessage(String msg) {
        chat.addAll(msg);
        msgBox.scrollTo(Integer.MAX_VALUE);
    }

    @FXML
    public void getScramble(ArrayList<Move> scramble) {
        Scramble.setText(generator.scrambleToString(scramble));
        Scramble.setVisible(true);
        mins = secs = millis = 0;
        canRun = true;
    }

    @FXML
    public void getHostPermissions() {
        nextScramble.setVisible(true);
    }

    @FXML
    public void leaveRoom(){
        System.out.println("close");
        jez.leaveRoom(room);
        Client client = new Client();
        Stage stage = (Stage) Scramble.getScene().getWindow();
        stage.close();
        try {
            client.start(classStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
