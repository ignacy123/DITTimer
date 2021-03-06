package model.logic;


import model.enums.Axis;
import model.enums.Direction;
import model.enums.Face;

import java.io.Serializable;
import java.util.Objects;

public class MoveImplementation implements Move, Serializable {
    private Axis axis;
    private Face face;
    private Direction direction;
    private int width;
    private boolean isTwoByTwo;

    public Axis getAxis() {
        return axis;
    }

    public Face getFace() {
        return face;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public int getWidth() {
        return width;
    }

    public MoveImplementation(Axis axis, Direction direction) {
        isTwoByTwo = true;
        this.axis = axis;
        switch (axis) {
            case X:
                face = Face.R;
                break;
            case Y:
                face = Face.U;
                break;
            case Z:
                face = Face.F;
                break;
        }
        this.direction = direction;
    }

    public MoveImplementation(Face face, Direction direction, int width) {
        isTwoByTwo = false;
        this.face = face;
        this.direction = direction;
        this.width = width;
        switch (face) {
            case B:
            case F:
                axis = Axis.Z;
                break;
            case R:
            case L:
                axis = Axis.X;
                break;
            case U:
            case D:
                axis = Axis.Y;
                break;
        }
    }

    @Override
    public String toString() {
        String string = "";
        if (isTwoByTwo) {
            string += face;
        } else {

            if (width > 2) {
                string += width;
            }
            string += face;
            if (width >= 2) {
                string += "w";
            }
        }
        if (direction == Direction.ANTICLOCKWISE) {
            string += "'";
        }
        if (direction == Direction.DOUBLE) {
            string += "2";
        }

        return string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoveImplementation that = (MoveImplementation) o;
        return width == that.width &&
                axis == that.axis &&
                face == that.face &&
                direction == that.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(axis, face, direction, width, isTwoByTwo);
    }
}
