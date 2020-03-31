package model.db;

import model.enums.CubeType;
import model.logic.Solve;

import java.util.ArrayList;

public interface DatabaseService {
    void start();
    ArrayList<Solve> pullAndParseAllSolves(CubeType cubeType);
    ArrayList<Solve> pullAndParseSolves(CubeType cubeType, int size);
    void insertIntoThreeByThree();
    void insert(Solve solve);
    //only for tests, should be removed before release;
    void dropDatabase();
}
