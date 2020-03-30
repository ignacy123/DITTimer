package model;

import java.util.ArrayList;

public interface ScrambleGenerator {
    ArrayList<MoveImplementation> generate();
    void setCubeType(CubeType cubeType);
}
