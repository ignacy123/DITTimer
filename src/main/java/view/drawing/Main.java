package view.drawing;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.enums.CubeType;
import model.enums.Direction;
import model.enums.Face;
import model.logic.Move;
import model.logic.MoveImplementation;
import model.logic.ScrambleGenerator;
import model.logic.ScrambleGeneratorImplementation;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        ScrambleGenerator sg = new ScrambleGeneratorImplementation(CubeType.THREEBYTHREE);
        ArrayList<Move> l = sg.generate();

        ClickDrawer dr = new ClickDrawer();
        System.out.println(sg.scrambleToString(l));
        //Rotator rotator = dr.getRotator();
        //Rw' U' D' B' R2

        dr.doMagic();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
