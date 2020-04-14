package view.TimeList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.SS.StatisticServer;
import model.SS.StatisticServerImplementation;
import model.db.DatabaseService;
import model.db.DatabaseServiceImplementation;
import model.enums.CubeType;
import model.logic.Solve;
import model.logic.SolveImplementation;
import model.wrappers.AVGwrapper;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


public class TimeListController extends Stage {
    @FXML
    private ListView listView;
    @FXML
    private ListView listView2;
    @FXML
    private ListView listView3;
    @FXML
    private Button button;
    private ObservableList<Solve> list1 = FXCollections.observableArrayList();
    private ObservableList<Solve> list2 = FXCollections.observableArrayList();
    private ObservableList<Solve> list3 = FXCollections.observableArrayList();
    private ObservableList<AVGwrapper> list4 = FXCollections.observableArrayList();
    private ObservableList<AVGwrapper> list5 = FXCollections.observableArrayList();
    private ObservableList<AVGwrapper> list6 = FXCollections.observableArrayList();
    private ObservableList<AVGwrapper> list7 = FXCollections.observableArrayList();
    private ObservableList<AVGwrapper> list8 = FXCollections.observableArrayList();
    private ObservableList<AVGwrapper> list9 = FXCollections.observableArrayList();
    StatisticServer ss;

    @FXML
    void initialize() {
        DatabaseService db = new DatabaseServiceImplementation();
        db.start();
        ss = new StatisticServerImplementation((DatabaseServiceImplementation)db, list1, list2, list3, list4, list5, list6, list7, list8, list9);
        listView.setItems(list2);
        listView2.setItems(list6);
        listView3.setItems(list7);
        listView.setCellFactory(listView1 -> {
            TextFieldListCell<Solve> cell = new TextFieldListCell<>();
            cell.setConverter(new SolveConverter());
            return cell;
        });
        listView2.setCellFactory(listView1 -> {
            TextFieldListCell<AVGwrapper> cell = new TextFieldListCell<>();
            cell.setConverter(new AvgConverter());
            return cell;
        });
        listView3.setCellFactory(listView1 -> {
            TextFieldListCell<AVGwrapper> cell = new TextFieldListCell<>();
            cell.setConverter(new AvgConverter());
            return cell;
        });

    }

    @FXML
    void buttonPressed() {
        Random random = new Random();
        Solve solve = new SolveImplementation();
        solve.setTime(new Timestamp(random.nextInt(100000)));
        solve.setScramble("R U F2");
        solve.setComment("test");
        solve.setDate(new Date());
        try {
            ss.insertAndPackToSolve(new Timestamp(random.nextInt(100000)), CubeType.THREEBYTHREE);
        } catch (StatisticServerImplementation.DNF dnf) {
            dnf.printStackTrace();
        } catch (StatisticServerImplementation.NotEnoughTimes notEnoughTimes) {
            notEnoughTimes.printStackTrace();
        }
        System.out.println("button pressed");
    }

    public void handle(MouseEvent event) {

        if (!event.getButton().equals(MouseButton.PRIMARY)) {
            return;
        }

        if (event.getClickCount() != 2) {
            return;
        }
        Solve solve = (Solve) listView.getSelectionModel().getSelectedItem();
        String toStr = "";
        //toStr+="Time: "+ solve.getTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("mm:ss.SSS"));
        toStr+="\nScramble: "+solve.getScramble();
        toStr+="\nDate: "+solve.getDate();
        toStr+="\nComment: "+solve.getComment();
        Dialog d = new Dialog();
        Window window = d.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(e -> window.hide());
        d.setTitle("Solve Info");
        d.setHeaderText("");
        d.setContentText(toStr);
        d.show();
    }

    public TimeListController() {
    }

}
