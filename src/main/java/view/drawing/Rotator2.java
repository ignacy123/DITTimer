package view.drawing;

import javafx.scene.paint.Color;
import model.enums.Direction;
import model.enums.Face;
import model.logic.Move;

import java.util.ArrayList;

public class Rotator2 implements Rotator {
    Color[][] cube;
    void rotat(int x, int y) {
        Color tmp = cube[x][y];
        cube[x][y] = cube[x+1][y];
        cube[x+1][y] = cube[x+1][y+1];
        cube[x+1][y+1] = cube[x][y+1];
        cube[x][y+1] = tmp;
    }
    void rotateL() {
        ArrayList<Color> te2 = new ArrayList<>();
        te2.add(cube[4][2]);
        te2.add(cube[5][2]);
        for(int i = 5; i >= 2; i--) {
            cube[i][2] = cube[i-2][2];
        }
        cube[0][2] = cube[3][7];
        cube[1][2] = cube[2][7];

        cube[3][7] = te2.get(0);
        cube[2][7] = te2.get(1);
        rotat(2, 0);
    }
    void rotateR() {
        ArrayList<Color> te2 = new ArrayList<>();
        te2.add(cube[0][3]);
        te2.add(cube[1][3]);
        for(int i = 0; i < 4; i++) {
            cube[i][3] = cube[i+2][3];
        }
        cube[4][3] = cube[3][6];
        cube[5][3] = cube[2][6];

        cube[3][6] = te2.get(0);
        cube[2][6] = te2.get(1);
        rotat(2, 4);
    }
    void rotateU() {
        ArrayList<Color> te2 = new ArrayList<>();
        te2.add(cube[2][0]);
        te2.add(cube[2][1]);
        for(int i = 0; i < 6; i++) {
            cube[2][i] = cube[2][i+2];
        }
        cube[2][7] = te2.get(1);
        cube[2][6] = te2.get(0);
        rotat(0, 2);
    }
    void rotateD() {
        ArrayList<Color> te2 = new ArrayList<>();
        te2.add(cube[3][7]);
        te2.add(cube[3][6]);
        for(int i = 7; i >= 2; i--) {
            cube[3][i] = cube[3][i-2];
        }
        cube[3][0] = te2.get(1);
        cube[3][1] = te2.get(0);
        rotat(4, 2);
    }
    void rotateF() {
        ArrayList<Color> te2 = new ArrayList<>();
        te2.add(cube[1][2]);
        te2.add(cube[1][3]);

        cube[1][2] = cube[3][1];
        cube[1][3] = cube[2][1];

        cube[2][1] = cube[4][2];
        cube[3][1] = cube[4][3];

        cube[4][2] = cube[3][4];
        cube[4][3] = cube[2][4];

        cube[3][4] = te2.get(1);
        cube[2][4] = te2.get(0);
        rotat(2, 2);
    }
    void rotateB() {
        ArrayList<Color> te2 = new ArrayList<>();
        te2.add(cube[0][2]);
        te2.add(cube[0][3]);

        cube[0][2] = cube[2][5];
        cube[0][3] = cube[3][5];

        cube[2][5] = cube[5][3];
        cube[3][5] = cube[5][2];

        cube[5][3] = cube[3][0];
        cube[5][2] = cube[2][0];

        cube[3][0] = te2.get(0);
        cube[2][0] = te2.get(1);

        rotat(2, 6);
    }
    Rotator2(Color[][] cube) {
        this.cube = cube;
    }
    @Override
    public void executeMoves(ArrayList<Move> moves) {
        for(Move move: moves) {
            move(move);
        }
    }

    @Override
    public void move(Move move) {
        Face face = move.getFace();
        Direction dir = move.getDirection();
        int hw = 1;
        if(dir == Direction.DOUBLE) hw = 2;
        if(dir == Direction.ANTICLOCKWISE) hw = 3;
        for(int i = 0; i < hw; i++) {
            if(face == Face.F) {
                rotateF();
            }
            else if(face == Face.U) {
                rotateU();
            }
            else if(face == Face.B) {
                rotateB();
            }
            else if(face == Face.R) {
                rotateR();
            }
            else if(face == Face.D) {
                rotateD();
            }
            else if(face == Face.L) {
                rotateL();
            }
        }
    }
}
