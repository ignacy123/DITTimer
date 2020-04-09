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

        ScrambleGenerator sg = new ScrambleGeneratorImplementation(CubeType.FOURBYFOUR);
        ArrayList<Move> l = sg.generate();

        DrawScramble dr = new DrawScramble(CubeType.FOURBYFOUR);
        System.out.println(sg.scrambleToString(l));
        Rotator rotator = dr.getRotator();
        //Rw' U' D' B' R2
        //rotator.move(new MoveImplementation(Face.R, Direction.ANTICLOCKWISE, 2));
        //rotator.move(new MoveImplementation(Face.U, Direction.ANTICLOCKWISE, 2));
        //rotator.move(new MoveImplementation(Face.D, Direction.ANTICLOCKWISE, 1));
        //rotator.move(new MoveImplementation(Face.B, Direction.ANTICLOCKWISE, 1));

        //rotator.move(new MoveImplementation(Face.R, Direction.DOUBLE, 1));
        //rotator.move(new MoveImplementation(Face.D, Direction.CLOCKWISE, 2));

        rotator.executeMoves(l);

        dr.doMagic();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
