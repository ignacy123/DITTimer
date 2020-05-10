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

public class StringToScrambleTest {
    @Test
    public void simple(){
        ScrambleGenerator sg = new ScrambleGeneratorImplementation(CubeType.THREEBYTHREE);
        ArrayList<Move> test = sg.fromString("R");
        assertEquals(test.size(), 1);
        assertEquals(test.get(0), new MoveImplementation(Face.R, Direction.CLOCKWISE, 1));
    }
    @Test
    public void oneMoveLongButVariousTypes(){
        ScrambleGenerator sg = new ScrambleGeneratorImplementation(CubeType.THREEBYTHREE);
        ArrayList<Move> test = sg.fromString("Rw");
        assertEquals(test.size(), 1);
        assertEquals(new MoveImplementation(Face.R, Direction.CLOCKWISE, 2), test.get(0));
        test = sg.fromString("3Fw'");
        assertEquals(test.size(), 1);
        assertEquals(new MoveImplementation(Face.F, Direction.ANTICLOCKWISE, 3), test.get(0));
        test = sg.fromString("4Dw2");
        assertEquals(test.size(), 1);
        assertEquals(new MoveImplementation(Face.D, Direction.DOUBLE, 4), test.get(0));
        test = sg.fromString("B'");
        assertEquals(test.size(), 1);
        assertEquals(new MoveImplementation(Face.B, Direction.ANTICLOCKWISE, 1), test.get(0));
    }
    @Test
    public void combined(){
        ScrambleGenerator sg = new ScrambleGeneratorImplementation(CubeType.THREEBYTHREE);
        ArrayList<Move> test = sg.fromString("Rw 3Dw' F F2 5Lw' Bw2 L' 3Uw");
        assertEquals(new MoveImplementation(Face.R, Direction.CLOCKWISE, 2), test.get(0));
        assertEquals(new MoveImplementation(Face.D, Direction.ANTICLOCKWISE, 3), test.get(1));
        assertEquals(new MoveImplementation(Face.F, Direction.CLOCKWISE, 1), test.get(2));
        assertEquals(new MoveImplementation(Face.F, Direction.DOUBLE, 1), test.get(3));
        assertEquals(new MoveImplementation(Face.L, Direction.ANTICLOCKWISE, 5), test.get(4));
        assertEquals(new MoveImplementation(Face.B, Direction.DOUBLE, 2), test.get(5));
        assertEquals(new MoveImplementation(Face.L, Direction.ANTICLOCKWISE, 1), test.get(6));
        assertEquals(new MoveImplementation(Face.U, Direction.CLOCKWISE, 3), test.get(7));
    }
    @Test
    public void longForThreeByThree(){
        ScrambleGenerator sg = new ScrambleGeneratorImplementation(CubeType.THREEBYTHREE);
        ArrayList<Move> test = sg.fromString("R2 D2 F2 R' D2 L F2 R2 B2 U2 B2 R' B' U' F' L R2 D' L' B' U'");
        assertEquals(new MoveImplementation(Face.R, Direction.DOUBLE, 1), test.get(0));
        assertEquals(new MoveImplementation(Face.D, Direction.DOUBLE, 1), test.get(1));
        assertEquals(new MoveImplementation(Face.F, Direction.DOUBLE, 1), test.get(2));
        assertEquals(new MoveImplementation(Face.R, Direction.ANTICLOCKWISE, 1), test.get(3));
        assertEquals(new MoveImplementation(Face.D, Direction.DOUBLE, 1), test.get(4));
        assertEquals(new MoveImplementation(Face.L, Direction.CLOCKWISE, 1), test.get(5));
        assertEquals(new MoveImplementation(Face.F, Direction.DOUBLE, 1), test.get(6));
        assertEquals(new MoveImplementation(Face.R, Direction.DOUBLE, 1), test.get(7));
        assertEquals(new MoveImplementation(Face.B, Direction.DOUBLE, 1), test.get(8));
        assertEquals(new MoveImplementation(Face.U, Direction.DOUBLE, 1), test.get(9));
        assertEquals(new MoveImplementation(Face.B, Direction.DOUBLE, 1), test.get(10));
        assertEquals(new MoveImplementation(Face.R, Direction.ANTICLOCKWISE, 1), test.get(11));
        assertEquals(new MoveImplementation(Face.B, Direction.ANTICLOCKWISE, 1), test.get(12));
        assertEquals(new MoveImplementation(Face.U, Direction.ANTICLOCKWISE, 1), test.get(13));
        assertEquals(new MoveImplementation(Face.F, Direction.ANTICLOCKWISE, 1), test.get(14));
        assertEquals(new MoveImplementation(Face.L, Direction.CLOCKWISE, 1), test.get(15));
        assertEquals(new MoveImplementation(Face.R, Direction.DOUBLE, 1), test.get(16));
        assertEquals(new MoveImplementation(Face.D, Direction.ANTICLOCKWISE, 1), test.get(17));
        assertEquals(new MoveImplementation(Face.L, Direction.ANTICLOCKWISE, 1), test.get(18));
        assertEquals(new MoveImplementation(Face.B, Direction.ANTICLOCKWISE, 1), test.get(19));
        assertEquals(new MoveImplementation(Face.U, Direction.ANTICLOCKWISE, 1), test.get(20));

    }
}
