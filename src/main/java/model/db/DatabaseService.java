package model.db;

import model.enums.CubeType;
import model.logic.Solve;

import java.util.ArrayList;

public interface DatabaseService {
    void start();

    ArrayList<Solve> pullAndParseAllSolves(CubeType cubeType);


    void deleteLast(CubeType cubeType);
    void updateLast(Solve solve);

    void insert(Solve solve);

    //only for tests, should be removed before release;
    void dropDatabase();

    void clearTable(CubeType cubeType);
}
