package model.logic;


import model.enums.Axis;
import model.enums.CubeType;
import model.enums.Direction;
import model.enums.Face;
import model.logic.Move;
import model.logic.MoveImplementation;
import model.logic.ScrambleGenerator;
import model.logic.ScrambleGeneratorImplementation;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class ScrambleToStringTest {
    @Test
    public void check(){
        ArrayList<Move> list = new ArrayList<>();
        list.add(new MoveImplementation(Axis.X, Direction.CLOCKWISE));
        list.add(new MoveImplementation(Axis.X, Direction.ANTICLOCKWISE));
        ScrambleGenerator scr = new ScrambleGeneratorImplementation(CubeType.TWOBYTWO);
        assertEquals(scr.scrambleToString(list), "R R'");
    }
    @Test
    public void check2(){
        ArrayList<Move> list = new ArrayList<>();
        list.add(new MoveImplementation(Axis.X, Direction.CLOCKWISE));
        list.add(new MoveImplementation(Axis.X, Direction.ANTICLOCKWISE));
        list.add(new MoveImplementation(Axis.Y, Direction.CLOCKWISE));
        list.add(new MoveImplementation(Axis.Y, Direction.ANTICLOCKWISE));
        list.add(new MoveImplementation(Axis.Z, Direction.CLOCKWISE));
        list.add(new MoveImplementation(Axis.Z, Direction.DOUBLE));
        ScrambleGenerator scr = new ScrambleGeneratorImplementation(CubeType.TWOBYTWO);
        assertEquals(scr.scrambleToString(list), "R R' U U' F F2");
    }
    @Test
    public void check3(){
        ArrayList<Move> list = new ArrayList<>();
        list.add(new MoveImplementation(Face.F, Direction.CLOCKWISE, 4));
        list.add(new MoveImplementation(Face.F, Direction.ANTICLOCKWISE, 5));
        list.add(new MoveImplementation(Face.L, Direction.DOUBLE, 2));
        list.add(new MoveImplementation(Face.B, Direction.CLOCKWISE, 3));
        list.add(new MoveImplementation(Face.D, Direction.DOUBLE, 7));
        list.add(new MoveImplementation(Face.U, Direction.ANTICLOCKWISE, 1));
        ScrambleGenerator scr = new ScrambleGeneratorImplementation(CubeType.TWOBYTWO);
        assertEquals(scr.scrambleToString(list), "4Fw 5Fw' Lw2 3Bw 7Dw2 U'");
    }
}
