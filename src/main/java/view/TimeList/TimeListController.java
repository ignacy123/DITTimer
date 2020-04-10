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
import model.logic.Solve;
import model.logic.SolveImplementation;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


public class TimeListController extends Stage {
    @FXML
    private ListView listView;
    @FXML
    private Button button;
    private ObservableList<Solve> list = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        listView.setItems(list);
        listView.setCellFactory(listView1 -> {
            TextFieldListCell<Solve> cell = new TextFieldListCell<>();
            cell.setConverter(new SolveConverter());
            return cell;
        });

    }

    @FXML
    void buttonPressed() {
        Random random = new Random();
        Solve solve = new SolveImplementation();
        //solve.setTime(new Timestamp(random.nextInt(100000)));
        solve.setScramble("R U F2");
        solve.setComment("test");
        solve.setDate(new Date());
        list.add(solve);
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
