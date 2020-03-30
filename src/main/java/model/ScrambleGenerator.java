package model;

import java.util.ArrayList;

public interface ScrambleGenerator {
    ArrayList<Move> generate();
    void setCubeType(CubeType cubeType);
}
