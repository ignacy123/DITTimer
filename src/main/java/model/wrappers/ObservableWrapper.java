package model.wrappers;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import model.enums.AVG;
import model.enums.CubeType;
import model.logic.Solve;

import java.util.HashMap;
import java.util.Map;

//holds observable lists in one place so that all parts of app dont need to care about at least 9 lists
public class ObservableWrapper {
    private Map<CubeType, ObservableList<Solve>> listsOfSolves;
    private Map<Pair<CubeType, AVG>, ObservableList<AVGwrapper>> listsOfAvgs;
    public ObservableWrapper(){
        listsOfSolves = new HashMap<>();
        listsOfAvgs = new HashMap<>();
        listsOfSolves.put(CubeType.TWOBYTWO, FXCollections.observableArrayList());
        listsOfSolves.put(CubeType.THREEBYTHREE, FXCollections.observableArrayList());
        listsOfSolves.put(CubeType.FOURBYFOUR, FXCollections.observableArrayList());
        listsOfAvgs.put(new Pair<>(CubeType.TWOBYTWO, AVG.Ao5), FXCollections.observableArrayList());
        listsOfAvgs.put(new Pair<>(CubeType.TWOBYTWO, AVG.Ao12), FXCollections.observableArrayList());
        listsOfAvgs.put(new Pair<>(CubeType.THREEBYTHREE, AVG.Ao5), FXCollections.observableArrayList());
        listsOfAvgs.put(new Pair<>(CubeType.THREEBYTHREE, AVG.Ao12), FXCollections.observableArrayList());
        listsOfAvgs.put(new Pair<>(CubeType.FOURBYFOUR, AVG.Ao5), FXCollections.observableArrayList());
        listsOfAvgs.put(new Pair<>(CubeType.FOURBYFOUR, AVG.Ao12), FXCollections.observableArrayList());
    }

    public ObservableList<Solve> getListOfSolves(CubeType type){
        return listsOfSolves.get(type);
    }
    public ObservableList<AVGwrapper> getListAvg(CubeType type, AVG avg){
        return listsOfAvgs.get(new Pair<>(type, avg));
    }
}
