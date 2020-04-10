package view.TimeList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class TimeList extends Application{


    public void start(Stage primaryStage) throws Exception {
        Pane pane = FXMLLoader.load(getClass().getClassLoader().getResource("TimeList.fxml"));

        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }

}
