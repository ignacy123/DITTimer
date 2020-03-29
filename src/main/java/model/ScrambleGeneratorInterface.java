package model;

import java.util.ArrayList;

public interface ScrambleGeneratorInterface {
    ArrayList<Move> generate();
    void setCubeType(CubeType cubeType);
}
