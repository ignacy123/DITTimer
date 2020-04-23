package model.SS;

import model.enums.AVG;
import model.enums.CubeType;
import model.enums.State;
import model.logic.Solve;
import model.wrappers.AVGwrapper;

import java.sql.Timestamp;
import java.util.ArrayList;

public interface StatisticServer {
    Timestamp GiveMeAverage(int WhatAverage, CubeType WhatModel) throws StatisticServerImplementation.DNF, StatisticServerImplementation.NotEnoughTimes ; // returns upToDate Average (5,12,100) or throw exception DNF
    //also creates average if can


    ArrayList<Timestamp> GiveMeTimes(CubeType WhatModel); // returns all times of specific Cube // for now only good times

    void insertSolve(Solve solve);
    Timestamp GiveMeMax(CubeType WhatModel); // return Max Time != DNF or throw exception NoTimesFound

    Timestamp GiveMeMin(CubeType WhatModel); // return Min Time != DNF or throw exception NoTimesFound
    AVGwrapper GiveMeMaxAVG(CubeType Whatmodel, AVG whatAvg);
    AVGwrapper GiveMeMinAVG(CubeType Whatmodel, AVG whatAvg);
    void ChangeStateLast(CubeType WhatModel, State state);

    void DeleteLast(CubeType WhatModel);       // changes in DB and in SS
    void delete(CubeType type);  // cleans everything including db
    void addComment(CubeType WhatModel, String string);
    Timestamp CreateAverage(int WhatAverage, CubeType WhatModel) throws StatisticServerImplementation.DNF, StatisticServerImplementation.NotEnoughTimes;

}
