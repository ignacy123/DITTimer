package view.drawing;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.enums.CubeType;
import model.enums.Direction;
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

        DrawScramble dr = new DrawScramble();
        Rotator4 rotator = dr.getRotator();

        rotator.executeMoves(l);
        ArrayList<Move> toDo = new ArrayList<>();
        for(Move move: l) {
            Direction dir = move.getDirection();
            if(dir == Direction.CLOCKWISE) dir = Direction.ANTICLOCKWISE;
            else if(dir == Direction.ANTICLOCKWISE) dir = Direction.CLOCKWISE;
            toDo.add(0, new MoveImplementation(move.getFace(), dir, move.getWidth()));
        }
        rotator.executeMoves(toDo);
        dr.doMagic();
        //TimeUnit.SECONDS.sleep(3);


    }


    public static void main(String[] args) {
        launch(args);
    }
}
