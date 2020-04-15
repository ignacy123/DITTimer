package view.drawing;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private Button button;

    @FXML
    void openStats(ActionEvent event) throws Exception{
        Stage stats = new Stage();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("StatsWindow.fxml"));
        stats.setTitle("statystyki");
        stats.setResizable(false);
        stats.setScene(new Scene(root));
        stats.show();
    }
}
