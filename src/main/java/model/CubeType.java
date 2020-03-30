package model;

public enum CubeType{
    TWOBYTWO, THREEBYTHREE, FOURBYFOUR;
}

enum Axis {
    X, Y, Z;
}
enum Face{
    U, D, F, B, R, L;
}
enum Direction{
    CLOCKWISE, ANTICLOCKWISE, DOUBLE;
}
enum State{
    NONE,TWOSECPENALTY,CORRECT,DNF,REJ;
}
enum Color{
    WHITE, YELLOW, RED, ORANGE, GREEN, BLUE;
}