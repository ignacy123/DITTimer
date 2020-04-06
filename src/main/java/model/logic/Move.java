package model.logic;

import model.enums.Axis;
import model.enums.Direction;
import model.enums.Face;

public interface Move {
    Axis getAxis();
    Face getFace();
    Direction getDirection();
    int getWidth();
}
