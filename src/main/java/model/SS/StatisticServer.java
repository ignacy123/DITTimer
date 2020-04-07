package model.SS;

import model.enums.CubeType;
import model.enums.State;
import model.logic.Solve;

import java.sql.Time;
import java.util.ArrayList;

public interface StatisticServer {
    Time GiveMeAverage(int WhatAverage, CubeType WhatModel) throws StatisticServerImplementation.DNF, StatisticServerImplementation.NotEnoughTimes ; // returns upToDate Average (5,12,100) or throw exception DNF
    //also creates average if can


    ArrayList<Time> GiveMeTimes(CubeType WhatModel); // returns all times of specific Cube // for now only good times


    Time GiveMeMax(CubeType WhatModel); // return Max Time != DNF or throw exception NoTimesFound

    Time GiveMeMin(CubeType WhatModel); // return Min Time != DNF or throw exception NoTimesFound

    void ChangeStateLast(CubeType WhatModel, State state);

    void DeleteLast(CubeType WhatModel);       // changes in DB and in SS

    Solve insertAndPackToSolve(Time timeOfsolution, CubeType whatCube); // take Time from Timer returns Solve; // missing scramble and state

}
