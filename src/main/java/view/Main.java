package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(new StackPane(), 300, 275));
        primaryStage.show();
        DrawScramble.doMagic();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
