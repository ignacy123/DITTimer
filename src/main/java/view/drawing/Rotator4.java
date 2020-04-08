package view.drawing;

import javafx.scene.paint.Color;
import model.enums.Direction;
import model.enums.Face;
import model.logic.Move;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class Rotator4 {

    private void rotateMatrix(Color[][] matrix) {
        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 4 - 2*i - 1; j++) {
                Color tmp = matrix[i][i+j];
                matrix[i][i+j] = matrix[3 - i-j][i];
                matrix[3-i-j][i] = matrix[3 - i][3 - i-j];
                matrix[3-i][3-i-j] = matrix[i+j][3-i];
                matrix[i+j][3-i] = tmp;
            }
        }
    }
    private void rotat(int x, int y) {
        Color[][] rightSide = new Color[4][4];
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                rightSide[i][j] = cube[x+i][y+j];
            }
        }
        rotateMatrix(rightSide);
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                cube[x+i][y+j] = rightSide[i][j];
            }
        }
    }
    public void rotateRwAC() {
        for(int i = 0; i < 3; i++) {
            rotateRw();
        }
    }
    public void rotateRw() {
        rotateRight();
        ArrayList<Color> te4 = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            te4.add(cube[i][6]);
        }
        for(int i = 0; i < 8; i++) {
            cube[i][6] = cube[i+4][6];
        }
        for(int i = 0; i < 4; i++) {
            cube[i+8][6] = cube[7-i][13];
        }
        for(int i = 0; i < 4; i++) {
            cube[7-i][13] = te4.get(i);
        }
    }
    public void rotateRightAC() {
        for(int i = 0; i < 3; i++) {
            rotateRight();
        }
    }
    public void rotateRight() {
        ArrayList<Color> te4 = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            te4.add(cube[i][7]);
        }
        for(int i = 0; i < 8; i++) {
            cube[i][7] = cube[i+4][7];
        }
        for(int i = 0; i < 4; i++) {
            cube[i+8][7] = cube[7-i][12];
        }
        for(int i = 0; i < 4; i++) {
            cube[7-i][12] = te4.get(i);
        }
        rotat(4, 8);
    }
    public void rotateLw() {
        for(int i = 0; i < 3; i++) {
            rotateLwAC();
        }
    }
    public void rotateLwAC() {
        rotateLeftAC();
        ArrayList<Color> te4 = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            te4.add(cube[i][5]);
        }
        for(int i = 0; i < 8; i++) {
            cube[i][5] = cube[i+4][5];
        }
        for(int i = 0; i < 4; i++) {
            cube[11-i][5] = cube[4+i][14];//ans
        }
        for(int i = 0; i < 4; i++) {
            cube[7-i][14] = te4.get(i);
        }
    }
    public void rotateLeft() {
        for(int i = 0; i < 3; i++) {
            rotateLeftAC();
        }
    }
    public void rotateLeftAC() {
        ArrayList<Color> te4 = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            te4.add(cube[i][4]);
        }
        for(int i = 0; i < 8; i++) {
            cube[i][4] = cube[i+4][4];
        }
        for(int i = 0; i < 4; i++) {
            cube[i+8][4] = cube[7-i][15];
        }
        for(int i = 0; i < 4; i++) {
            cube[7-i][15] = te4.get(i);
        }
        for(int i = 0; i < 3; i++) rotat(4, 0);
    }
    public void rotateUwAC() {
        for(int i = 0; i < 3; i++) rotateUw();
    }
    public void rotateUw() {
        rotateUp();
        ArrayList<Color> te4 = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            te4.add(cube[5][i]);
        }
        for(int i = 0; i < 12; i++) {
            cube[5][i] = cube[5][i+4];
        }
        for(int i = 0; i < 4; i++) {
            cube[5][12+i] = te4.get(i);
        }
    }
    public void rotateUpAC() {
        for(int i = 0; i < 3; i++) {
            rotateUp();
        }
    }
    public void rotateUp() {
        ArrayList<Color> te4 = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            te4.add(cube[4][i]);
        }
        for(int i = 0; i < 12; i++) {
            cube[4][i] = cube[4][i+4];
        }
        for(int i = 0; i < 4; i++) {
            cube[4][12+i] = te4.get(i);
        }
        rotat(0, 4);
    }
    public void rotateDwAC() {
        for(int i = 0; i < 3; i++) {
            rotateDw();
        }
    }
    public void rotateDw() {
        rotateDown();
        ArrayList<Color> te4 = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            te4.add(cube[6][12+i]);
        }
        for(int i = 15; i >= 4; i--) {
            cube[6][i] = cube[6][i-4];
        }
        for(int i = 0; i < 4; i++) {
            cube[6][i] = te4.get(i);
        }
    }
    public void rotateDownAC() {
        for(int i = 0; i < 3; i++) {
            rotateDown();
        }
    }
    public void rotateDown() {
        ArrayList<Color> te4 = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            te4.add(cube[7][12+i]);
        }
        for(int i = 15; i >= 4; i--) {
            cube[7][i] = cube[7][i-4];
        }
        for(int i = 0; i < 4; i++) {
            cube[7][i] = te4.get(i);
        }
        rotat(8, 4);
    }
    public void rotateFrontAC() {
        for(int i = 0; i < 3; i++) {
            rotateFront();
        }
    }
    public void rotateFront() {
        ArrayList<Color> te4 = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            te4.add(cube[3][4+i]);
        }
        for(int i = 0; i < 4; i++) {
            cube[3][7-i] = cube[4+i][3];
        }
        for(int i = 0; i < 4; i++) {
            cube[4+i][3] = cube[8][4+i];
        }
        for(int i = 0; i < 4; i++) {
            cube[8][4+i] = cube[7-i][8];
        }
        for(int i = 0; i < 4; i++) {
            cube[4+i][8] =te4.get(i);
        }
        rotat(4, 4);
    }
    public void rotateFwAC() {
        for(int i = 0; i < 3; i++) {
            rotateFw();
        }
    }
    public void rotateFw() {
        rotateFront();
        ArrayList<Color> te4 = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            te4.add(cube[2][4+i]);
        }
        for(int i = 0; i < 4; i++) {
            cube[2][7-i] = cube[4+i][2];
        }
        for(int i = 0; i < 4; i++) {
            cube[4+i][2] = cube[9][4+i];
        }
        for(int i = 0; i < 4; i++) {
            cube[9][4+i] = cube[7-i][9];
        }
        for(int i = 0; i< 4; i++) {
            cube[4+i][9] = te4.get(i);
        }
    }
    public void rotateBackAC() {
        for(int i = 0; i < 3; i++) {
            rotateBack();
        }
    }
    public void rotateBack() {
        ArrayList<Color> te4 = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            te4.add(cube[0][4+i]);
        }
        for(int i = 0; i < 4; i++) {
            cube[0][4+i] = cube[4+i][11];
        }
        for(int i = 0; i < 4; i++) {
            cube[4+i][11] = cube[11][7-i];
        }
        for(int i = 0; i < 4; i++) {
            cube[11][7-i] = cube[7-i][0];
        }
        for(int i = 0 ; i< 4; i++) {
            cube[7 -i][0] = te4.get(i);
        }
        rotat(4, 12);
    }
    public void rotateBwAC() {
        for(int i = 0; i < 3; i++) {
            rotateBw();
        }
    }
    public void rotateBw() {
        rotateBack();
        ArrayList<Color> te4 = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            te4.add(cube[1][4+i]);
        }
        for(int i = 0; i < 4; i++) {
            cube[1][4+i] = cube[4+i][10];
        }
        for(int i = 0; i < 4; i++) {
            cube[4+i][10] = cube[10][7-i];
        }
        for(int i = 0; i < 4; i++) {
            cube[10][7-i] = cube[7-i][1];
        }
        for(int i = 0 ; i< 4; i++) {
            cube[7 -i][1] = te4.get(i);
        }
    }
    public void move(Move move) {
        Face face = move.getFace();
        Direction dir = move.getDirection();
        int width = move.getWidth();
        int hw = 1;
        if(dir == Direction.DOUBLE) hw = 2;
        if(dir == Direction.ANTICLOCKWISE) hw = 3;
        if(face == Face.U) {
            for(int i = 0; i < hw; i++) {
                if(width == 1)
                rotateUp();
                else
                    rotateUw();
            }
        }
        else if(face == Face.D) {
            for(int i = 0; i < hw; i++) {
                if(width == 1)
                    rotateDown();
                else
                    rotateDw();
            }
        }
        else if(face == Face.F) {
            for(int i = 0; i < hw; i++) {
                if(width == 1)
                    rotateFront();
                else
                    rotateFw();
            }
        }
        else if(face == Face.L) {
            for(int i = 0; i < hw; i++) {
                if(width == 1)
                    rotateLeft();
                else
                    rotateLw();
            }
        }
        else if(face == Face.R) {
            for(int i = 0; i < hw; i++) {
                if(width == 1)
                    rotateRight();
                else
                    rotateRw();
            }
        }
        else if(face == Face.B) {
            for(int i = 0; i < hw; i++) {
                if(width == 1)
                    rotateBack();
                else
                    rotateBw();
            }
        }
    }
    public void executeMoves(ArrayList<Move> moves) {
        for(Move kijanka: moves) {
            move(kijanka);
        }
    }
    Color[][] cube;
    Rotator4(Color[][] cube) {
        this.cube = cube;
    }
}
