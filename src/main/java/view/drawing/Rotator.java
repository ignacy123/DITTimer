package view.drawing;

import model.logic.Move;

import java.util.ArrayList;

public interface Rotator {
    void executeMoves(ArrayList<Move> moves);
    void move(Move move);
}
