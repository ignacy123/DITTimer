package model.SS;

import model.enums.CubeType;
import model.enums.State;
import model.logic.Solve;
import java.sql.Timestamp;
import java.util.ArrayList;

public interface StatisticServer {
    Timestamp GiveMeAverage(int WhatAverage, CubeType WhatModel) throws StatisticServerImplementation.DNF, StatisticServerImplementation.NotEnoughTimes ; // returns upToDate Average (5,12,100) or throw exception DNF
    //also creates average if can


    ArrayList<Timestamp> GiveMeTimes(CubeType WhatModel); // returns all times of specific Cube // for now only good times

    void insertSolve(Solve solve);
    Timestamp GiveMeMax(CubeType WhatModel); // return Max Time != DNF or throw exception NoTimesFound

    Timestamp GiveMeMin(CubeType WhatModel); // return Min Time != DNF or throw exception NoTimesFound

    void ChangeStateLast(CubeType WhatModel, State state);

    void DeleteLast(CubeType WhatModel);       // changes in DB and in SS



}
