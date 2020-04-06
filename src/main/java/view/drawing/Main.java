package view.drawing;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.enums.CubeType;
import model.logic.Move;
import model.logic.ScrambleGenerator;
import model.logic.ScrambleGeneratorImplementation;

import java.util.ArrayList;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        //primaryStage.setTitle("Hello World");
        //primaryStage.setScene(new Scene(new StackPane(), 300, 275));
        //primaryStage.show();

        ScrambleGenerator sg = new ScrambleGeneratorImplementation(CubeType.THREEBYTHREE);
        ArrayList<Move> l = sg.generate();
        DrawScramble dr = new DrawScramble(l);
        System.out.println(sg.scrambleToString(l));
        dr.doMagic();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
