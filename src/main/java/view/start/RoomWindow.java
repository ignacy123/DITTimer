package view.start;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.conn.Room;
import model.conn.ServerService;
import model.conn.User;
import model.enums.CubeType;
import model.enums.State;
import model.logic.*;
import model.wrappers.AVGwrapper;
import view.TimeList.AvgConverter;
import view.TimeList.SolveConverter;
import view.drawing.DrawScramble;

import java.sql.Timestamp;
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
    @FXML
    Text ao5Text0;
    @FXML
    Text ao5Text1;
    @FXML
    Text ao5Text2;
    @FXML
    Text ao5Text3;
    @FXML
    Text ao5Text4;
    @FXML
    Text ao12Text0;
    @FXML
    Text ao12Text1;
    @FXML
    Text ao12Text2;
    @FXML
    Text ao12Text3;
    @FXML
    Text ao12Text4;
    @FXML
    Pane DrawerPane;
    DrawScramble drawer=null;


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
    ArrayList<Text> avgs5;
    ArrayList<Text> avgs12;

    Stage clientStage;

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
        avgs5 = new ArrayList<>();
        avgs5.add(ao5Text0);
        avgs5.add(ao5Text1);
        avgs5.add(ao5Text2);
        avgs5.add(ao5Text3);
        avgs5.add(ao5Text4);
        avgs12 = new ArrayList<>();
        avgs12.add(ao12Text0);
        avgs12.add(ao12Text1);
        avgs12.add(ao12Text2);
        avgs12.add(ao12Text3);
        avgs12.add(ao12Text4);

        drawer=new DrawScramble();
        DrawerPane.getChildren().add(drawer.getPane());
        DrawerPane.setVisible(false);

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
        if(currentSolve==null){
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
            //getList(i).setVisible(false);
            getText(i).setVisible(false);
            avgs5.get(i).setVisible(false);
            avgs12.get(i).setVisible(false);
        }
    }

    public void renderTimes(ConcurrentHashMap<User, ArrayList<Solve>> merlin) {
        hideLists();
        noBestTime();
        ArrayList<User> userList = new ArrayList<>(merlin.keySet());
        Collections.sort(userList, Comparator.comparing(User::getName));
        AVGwrapper bestao5 = null;
        AVGwrapper bestao12 = null;
        int bestao5Ind = 0;
        int bestao12Ind = 0;
        int i = 0;
        AvgConverter converter = new AvgConverter();
        for (User user : userList) {
            getList(i).getItems().setAll(merlin.get(user));
            getList(i).setVisible(true);
            getList(i).scrollTo(Integer.MAX_VALUE);
            getText(i).setText(user.getName());
            getText(i).setVisible(true);
            avgs5.get(i).setVisible(true);
            avgs12.get(i).setVisible(true);
            AVGwrapper avg = getAvg(merlin.get(user), 5);
            AVGwrapper avg2 = getAvg(merlin.get(user), 12);
            avgs5.get(i).setText(converter.toString(avg));
            avgs12.get(i).setText(converter.toString(avg2));
            if(!avg.isDNF() && !avg.isNET()){
                if(bestao5==null){
                    bestao5 = avg;
                    bestao5Ind = i;
                }else{
                    if(bestao5.getAVG().getTime()>avg.getAVG().getTime()){
                        bestao5 = avg;
                        bestao5Ind = i;
                    }
                }
            }
            if(!avg2.isDNF() && !avg2.isNET()){
                if(bestao12==null){
                    bestao12 = avg;
                    bestao12Ind = i;
                }else{
                    if(bestao12.getAVG().getTime()>avg2.getAVG().getTime()){
                        bestao12 = avg;
                        bestao12Ind = i;
                    }
                }
            }
            noBestTime();
            if(bestao5!=null){
                avgs5.get(bestao5Ind).setFill(Color.GREEN);
            }
            if(bestao12!=null){
                avgs12.get(bestao12Ind).setFill(Color.GREEN);
            }
            i++;
        }
        for(int j = userList.size(); j < 5; j++) {
            getList(i).getItems().clear();
        }
    }
    private void noBestTime() {
        for(Text text: avgs5){
            text.setFill(Color.BLACK);
        }
        for(Text text: avgs12){
            text.setFill(Color.BLACK);
        }
    }

    private AVGwrapper getAvg(ArrayList<Solve> solves, int amount) {
        AVGwrapper toRet = new AVGwrapper();
        if (solves.size() < amount - 1) {
            toRet.setNET();
            return toRet;
        }
        if (solves.size() == amount - 1) {
            for (int i = 0; i < amount - 1; i++) {
                if (solves.get(i).getState().equals(State.DNF)) {
                    toRet.setDNF();
                    return toRet;
                }
            }
            long best = -1;
            long avg = 0;
            for (int i = 0; i < amount - 1; i++) {
                if (solves.get(i).getTime().getTime() < best) {
                    best = solves.get(i).getTime().getTime();
                }
                avg += solves.get(i).getTime().getTime();
            }
            avg -= best;
            avg = avg / (amount - 2);
            toRet.setAverage(new Timestamp(avg));
            return toRet;
        }
        //size is at least 5 now
        int dnfCounter = 0;
        for (int i = solves.size() - amount; i < solves.size(); i++) {
            if (solves.get(i).getState().equals(State.DNF)) {
                dnfCounter++;
            }
        }
        if (dnfCounter >= 2) {
            toRet.setDNF();
            return toRet;
        }
        if (dnfCounter == 1) {
            long best = 999999999;
            long avg = 0;
            for (int i = solves.size() - amount; i < solves.size(); i++) {
                if (solves.get(i).getState().equals(State.DNF)) {
                    continue;
                }
                if (solves.get(i).getTime().getTime() < best) {
                    best = solves.get(i).getTime().getTime();
                }
                avg += solves.get(i).getTime().getTime();
            }
            avg -= best;
            avg = avg / (amount - 2);
            toRet.setAverage(new Timestamp(avg));
            return toRet;
        }

        long worst = -1;
        long best = 99999999;
        long avg = 0;
        for (int i = solves.size() - amount; i < solves.size(); i++) {
            if (solves.get(i).getState().equals(State.DNF)) {
                continue;
            }
            if (solves.get(i).getTime().getTime() < best) {
                best = solves.get(i).getTime().getTime();
            }
            if (solves.get(i).getTime().getTime() > worst) {
                worst = solves.get(i).getTime().getTime();
            }
            avg += solves.get(i).getTime().getTime();
        }
        avg -= best;
        avg -= worst;
        avg = avg / (amount - 2);
        toRet.setAverage(new Timestamp(avg));
        return toRet;

    }

    @FXML
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Room.fxml"));
        loader.setController(this);
        Pane pane = loader.load();
        Scene scene = new Scene(pane);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("css0.css").toExternalForm());
        stage.setScene(scene);
        jez.getPlayers(room);
        jez.getTimes(room);
        stage.setOnCloseRequest(windowEvent -> {
            System.out.println("close");
            jez.leaveRoom(room);
            //jez.close();
            clientStage.show();
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
        DrawerPane.setVisible(true);
        drawer.setTypePernament(room.getType());
        Scramble.setText(generator.scrambleToString(scramble));
        Scramble.setVisible(true);
        drawer.setScramble(scramble);
        mins = secs = millis = 0;
        canRun = true;
    }

    @FXML
    public void getHostPermissions() {
        nextScramble.setVisible(true);
    }

    @FXML
    public void leaveRoom() {
        System.out.println("close");
        jez.leaveRoom(room);
        //Client client = new Client();
        Stage stage = (Stage) Scramble.getScene().getWindow();

        try {
            System.out.println("open xd");
            clientStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        stage.close();
    }

}
