package view.drawing;

import javafx.scene.paint.Color;
import model.enums.Direction;
import model.enums.Face;
import model.logic.Move;

import java.util.ArrayList;

public class Rotator3 implements Rotator{
    Color[][] cube;

    private void rotateMatrix(Color[][] matrix) {
        for(int i = 0; i < 1; i++) {
            for(int j = 0; j < 3 - 2*i - 1; j++) {
                Color tmp = matrix[i][i+j];
                matrix[i][i+j] = matrix[2 - i-j][i];
                matrix[2-i-j][i] = matrix[2 - i][2 - i-j];
                matrix[2-i][2-i-j] = matrix[i+j][2-i];
                matrix[i+j][2-i] = tmp;
            }
        }
    }
    private void rotat(int x, int y) {
        Color[][] rightSide = new Color[3][3];
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                rightSide[i][j] = cube[x+i][y+j];
            }
        }
        rotateMatrix(rightSide);
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                cube[x+i][y+j] = rightSide[i][j];
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
            te3.add(0, cube[i][3]);
        }

        for(int i = 8; i >= 3; i--) {
            cube[i][3]= cube[i-3][3];
        }
        for(int i = 0; i < 3; i++) {
            cube[i][3]= cube[5-i][11];
        }
        for(int i = 3; i < 6; i++) {
            cube[i][11] = te3.get(i-3);
        }
        rotat(3, 0);
    }
    public void rotateRightCW() {
        for(int i = 0; i < 3; i++) {
            rotateRightAC();
        }
    }
    public void rotateRightAC() {//AntiClockWise
        ArrayList<Color> te3 = new ArrayList<>();
        for(int i = 6; i < 9; i++) {
            te3.add(0, cube[i][5]);
        }
        for(int i = 8; i >= 3; i--) {
            cube[i][5] = cube[i-3][5];
        }
        for(int i = 0; i < 3; i++) {
            cube[i][5] = cube[5-i][9];
        }
        for(int i = 3; i < 6; i++) {
            cube[i][9] = te3.get(i-3);
        }
        for(int i = 0; i < 3; i++) {
            rotat(3, 6);
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
            te3.add(cube[3][9 + i]);
        }
        for(int i = 11; i > 2; i--) {
            cube[3][i] = cube[3][i-3];
        }
        for(int i = 0; i < 3; i++) {
            cube[3][i] = te3.get(i);
        }
        for(int i = 0; i < 3; i++)
            rotat(0, 3);
    }
    public void rotateDownAC() {
        for(int i = 0; i < 3; i++) {
            rotateDownCW();
        }
    }
    public void rotateDownCW() {
        ArrayList<Color> te3 = new ArrayList<>();//at the very right side
        for(int i = 0; i < 3; i++) {
            te3.add(cube[5][9 + i]);
        }
        for(int i = 11; i > 2; i--) {
            cube[5][i] = cube[5][i-3];
        }
        for(int i = 0; i < 3; i++) {
            cube[5][i] = te3.get(i);
        }
        rotat(6, 3);
    }
    public void rotateFrontAC() {
        for(int i = 0; i < 3; i++) {
            rotateFrontCW();
        }
    }
    public void rotateFrontCW() {
        ArrayList<Color> te3 = new ArrayList<>();//at the very right side
        for(int i = 0; i < 3; i++) {
            te3.add(cube[2][3 + i]);
        }
        for(int i = 0; i < 3; i++) {
            cube[2][3 + i] = cube[5-i][2];
        }
        for(int i = 0; i < 3; i++) {
            cube[5-i][2] = cube[6][5-i];
        }
        for(int i = 0; i < 3; i++) {
            cube[6][3+i] = cube[5-i][6];
        }
        for(int i = 0; i < 3; i++) {
            cube[3+i][6] = te3.get(i);
        }
        rotat(3, 3);
    }
    public void rotateBackAC() {
        for(int i = 0; i < 3; i++) {
            rotateBackCW();
        }
    }
    public void rotateBackCW() {
        ArrayList<Color> te3 = new ArrayList<>();//at the very right side
        for(int i = 0; i < 3; i++) {
            te3.add(cube[0][3 + i]);
        }
        for(int i = 0; i < 3; i++) {
            cube[0][3+i] = cube[3+i][8];
        }
        for(int i = 0; i < 3; i++) {
            cube[3 + i][8] = cube[8][5-i];
        }
        for(int i = 0; i < 3; i++) {
            cube[8][3+i] = cube[3+i][0];
        }
        for(int i = 0; i < 3; i++) {
            cube[5-i][0] = te3.get(i);
        }
        rotat(3, 9);
    }
    @Override
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
    @Override
    public void executeMoves(ArrayList<Move> moves) {//control panel
        for(Move kijanka: moves) {
            move(kijanka);
        }
    }
    Rotator3(Color[][] inp) {
        this.cube = inp;
    }
}
