package view.drawing;

import javafx.scene.paint.Color;
import model.enums.Axis;
import model.enums.Direction;
import model.enums.Face;
import model.logic.Move;

import java.util.ArrayList;

public class Rotations {
    ArrayList<ArrayList<Color>> cube;

    public void rotateMatrix(ArrayList<ArrayList<Color>> matrix) { //anticlockwise
        ArrayList<ArrayList<Color>> toRet = new ArrayList<>();
        for(ArrayList<Color> i: matrix) {
            toRet.add((ArrayList<Color>) i.clone());
        }
        for(int i = 0; i < matrix.size()/2; i++) {
            for(int j = 0; j < matrix.size() - i - 1; j++) {
                Color tmp = matrix.get(i).get(j);
                matrix.get(i).set(j, matrix.get(matrix.size() - 1 - j).get(i));
                matrix.get(matrix.size() - 1 - j).set(i, matrix.get(matrix.size() - 1 - i).get(matrix.size() - 1 - j));
                matrix.get(matrix.size() - 1 - i).set(matrix.size() - 1 - j, matrix.get(j).get(matrix.size() - 1 - i));
                matrix.get(j).set(matrix.size() - 1 - i, tmp);
            }
        }
    }
    public void rotateLeftAC() {
        for(int i = 0; i < 3; i++) {
            rotateLeftCW();
        }
    }
    public void rotateLeftCW() { //Clockwise
        ArrayList<Color> te3 = new ArrayList<>();
        for(int i = 6; i < 9; i++) {
            te3.add(0, cube.get(i).get(3));
        }

        for(int i = 8; i >= 3; i--) {
            cube.get(i).set(3, cube.get(i-3).get(3));
        }
        for(int i = 0; i < 3; i++) {
            cube.get(i).set(3, cube.get(5-i).get(11));
        }
        for(int i = 3; i < 6; i++) {
            cube.get(i).set(11, te3.get(i-3));
        }
        ArrayList<ArrayList<Color>> leftSide = new ArrayList<>();
        for(int i = 0; i < 9; i++) {
            if(i % 3 == 0) leftSide.add(new ArrayList<>());
            leftSide.get((i - i%3)/3).add(cube.get((i - i%3)/3 + 3).get(i%3));
        }
        rotateMatrix(leftSide);
        for(int i = 0; i < 9; i++) {
            cube.get((i-i%3)/3 + 3).set(i%3, leftSide.get((i-i%3)/3).get(i%3));
        }
    }
    public void rotateRightCW() {
        for(int i = 0; i < 3; i++) {
            rotateRightAC();
        }
    }
    public void rotateRightAC() {//AntiClockWise
        ArrayList<Color> te3 = new ArrayList<>();
        for(int i = 6; i < 9; i++) {
            te3.add(0, cube.get(i).get(5));
        }
        for(int i = 8; i >= 3; i--) {
            cube.get(i).set(5, cube.get(i-3).get(5));
        }
        for(int i = 0; i < 3; i++) {
            cube.get(i).set(5, cube.get(5-i).get(9));
        }
        for(int i = 3; i < 6; i++) {
            cube.get(i).set(9, te3.get(i-3));
        }
        ArrayList<ArrayList<Color>> rightSide = new ArrayList<>();
        for(int i = 0; i < 9; i++) {
            if(i % 3 == 0) rightSide.add(new ArrayList<>());
            rightSide.get((i - i%3)/3).add(cube.get((i - i%3)/3 + 3).get(i%3 + 6));
        }
        for(int i = 0; i < 3; i++)
            rotateMatrix(rightSide);
        for(int i = 0; i < 9; i++) {
            cube.get((i-i%3)/3 + 3).set(i%3 + 6, rightSide.get((i-i%3)/3).get(i%3));
        }
    }
    public void rotateUpCW() {
        for(int i = 0; i < 3; i++) {
            rotateUpAC();
        }
    }
    public void rotateUpAC() {
        ArrayList<Color> te3 = new ArrayList<>();//at the very right side
        for(int i = 0; i < 3; i++) {
            te3.add(cube.get(3).get(9 + i));
        }
        for(int i = 11; i > 2; i--) {
            cube.get(3).set(i, cube.get(3).get(i-3));
        }
        for(int i = 0; i < 3; i++) {
            cube.get(3).set(i, te3.get(i));
        }
        ArrayList<ArrayList<Color>> upSide = new ArrayList<>();
        for(int i = 0; i < 9; i++) {
            if(i % 3 == 0) upSide.add(new ArrayList<>());
            upSide.get((i - i%3)/3).add(cube.get((i - i%3)/3).get(i%3 + 3));
        }
        for(int i = 0; i < 3; i++)
            rotateMatrix(upSide);
        for(int i = 0; i < 9; i++) {
            cube.get((i-i%3)/3).set(i%3 + 3, upSide.get((i-i%3)/3).get(i%3));
        }
    }
    public void rotateDownAC() {
        for(int i = 0; i < 3; i++) {
            rotateDownCW();
        }
    }
    public void rotateDownCW() {
        ArrayList<Color> te3 = new ArrayList<>();//at the very right side
        for(int i = 0; i < 3; i++) {
            te3.add(cube.get(5).get(9 + i));
        }
        for(int i = 11; i > 2; i--) {
            cube.get(5).set(i, cube.get(5).get(i-3));
        }
        for(int i = 0; i < 3; i++) {
            cube.get(5).set(i, te3.get(i));
        }
        ArrayList<ArrayList<Color>> downSide = new ArrayList<>();
        for(int i = 0; i < 9; i++) {
            if(i % 3 == 0) downSide.add(new ArrayList<>());
            downSide.get((i - i%3)/3).add(cube.get((i - i%3)/3 + 6).get(i%3 + 3));
        }
        rotateMatrix(downSide);
        for(int i = 0; i < 9; i++) {
            cube.get((i-i%3)/3 + 6).set(i%3 + 3, downSide.get((i-i%3)/3).get(i%3));
        }
    }
    public void rotateFrontAC() {
        for(int i = 0; i < 3; i++) {
            rotateFrontCW();
        }
    }
    public void rotateFrontCW() {
        ArrayList<Color> te3 = new ArrayList<>();//at the very right side
        for(int i = 0; i < 3; i++) {
            te3.add(cube.get(2).get(3 + i));
        }
        for(int i = 0; i < 3; i++) {
            cube.get(2).set(3 + i, cube.get(5-i).get(2));
        }
        for(int i = 0; i < 3; i++) {
            cube.get(5-i).set(2, cube.get(6).get(5-i));
        }
        for(int i = 0; i < 3; i++) {
            cube.get(6).set(3+i, cube.get(5-i).get(6));
        }
        for(int i = 0; i < 3; i++) {
            cube.get(3+i).set(6, te3.get(i));
        }
        ArrayList<ArrayList<Color>> frontSide = new ArrayList<>();
        for(int i = 0; i < 9; i++) {
            if(i % 3 == 0) frontSide.add(new ArrayList<>());
            frontSide.get((i - i%3)/3).add(cube.get((i - i%3)/3 + 3).get(i%3 + 3));
        }
        rotateMatrix(frontSide);
        for(int i = 0; i < 9; i++) {
            cube.get((i-i%3)/3 + 3).set(i%3 + 3, frontSide.get((i-i%3)/3).get(i%3));
        }
    }
    public void rotateBackAC() {
        for(int i = 0; i < 3; i++) {
            rotateBackCW();
        }
    }
    public void rotateBackCW() {
        ArrayList<Color> te3 = new ArrayList<>();//at the very right side
        for(int i = 0; i < 3; i++) {
            te3.add(cube.get(0).get(3 + i));
        }
        for(int i = 0; i < 3; i++) {
            cube.get(0).set(3+i, cube.get(3+i).get(8));
        }
        for(int i = 0; i < 3; i++) {
            cube.get(3 + i).set(8, cube.get(8).get(5-i));
        }
        for(int i = 0; i < 3; i++) {
            cube.get(8).set(3+i, cube.get(3+i).get(0));
        }
        for(int i = 0; i < 3; i++) {
            cube.get(5-i).set(0, te3.get(i));
        }
        ArrayList<ArrayList<Color>> backSide = new ArrayList<>();
        for(int i = 0; i < 9; i++) {
            if(i % 3 == 0) backSide.add(new ArrayList<>());
            backSide.get((i - i%3)/3).add(cube.get((i - i%3)/3 + 3).get(i%3 + 9));
        }
        rotateMatrix(backSide);
        for(int i = 0; i < 9; i++) {
            cube.get((i-i%3)/3 + 3).set(i%3 + 9, backSide.get((i-i%3)/3).get(i%3));
        }
    }
    public void move(Move move) {
        Face face = move.getFace();
        Direction dir = move.getDirection();
        if(face == Face.U) {
            if(dir == Direction.DOUBLE) {
                rotateUpCW();
                rotateUpCW();
            }
            else if(dir == Direction.CLOCKWISE) {
                rotateUpCW();
            }
            else
                rotateUpAC();
        }
        else if(face == Face.D) {
            if(dir == Direction.DOUBLE) {
                rotateDownCW();
                rotateDownCW();
            }
            else if(dir == Direction.CLOCKWISE) {
                rotateDownCW();
            }
            else
                rotateDownAC();
        }
        else if(face == Face.F) {
            if(dir == Direction.DOUBLE) {
                rotateFrontCW();
                rotateFrontCW();
            }
            else if(dir == Direction.CLOCKWISE) {
                rotateFrontCW();
            }
            else
                rotateFrontAC();
        }
        else if(face == Face.L) {
            if(dir == Direction.DOUBLE) {
                rotateLeftCW();
                rotateLeftCW();
            }
            else if(dir == Direction.CLOCKWISE) {
                rotateLeftCW();
            }
            else
                rotateLeftAC();
        }
        else if(face == Face.R) {
            if(dir == Direction.DOUBLE) {
                rotateRightCW();
                rotateRightCW();
            }
            else if(dir == Direction.CLOCKWISE) {
                rotateRightCW();
            }
            else
                rotateRightAC();
        }
        else if(face == Face.B) {
            if(dir == Direction.DOUBLE) {
                rotateBackCW();
                rotateBackCW();
            }
            else if(dir == Direction.CLOCKWISE) {
                rotateBackCW();
            }
            else
                rotateBackAC();
        }
    }
    public void executeMoves(ArrayList<Move> moves) {//control panel
        for(Move kijanka: moves) {
            move(kijanka);
        }
    }
    Rotations(ArrayList<ArrayList<Color>> inp) {
        this.cube = inp;
    }
}
