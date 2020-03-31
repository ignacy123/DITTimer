package model.logic;

import model.enums.CubeType;

import java.util.ArrayList;

public interface ScrambleGenerator {
    ArrayList<Move> generate();
    void setCubeType(CubeType cubeType);
    String  scrambleToString(ArrayList<Move> list);
}
