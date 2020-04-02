package view.Metronome;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Metronome extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        //FXMLLoader loader= new FXMLLoader();
        //loader.setLocation(this.getClass().getResource("view/Metronome/metronome.fxml"));
        Pane pane = FXMLLoader.load(getClass().getClassLoader().getResource("metronome.fxml"));;

        Scene scene=new Scene(pane);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
