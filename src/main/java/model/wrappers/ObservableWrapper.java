package model.wrappers;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import model.enums.AVG;
import model.enums.CubeType;
import model.enums.Running;
import model.logic.Move;
import model.logic.Solve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//holds observable lists in one place so that all parts of app dont need to care about at least 9 lists
public class ObservableWrapper {
    private Map<CubeType, ObservableList<Solve>> listsOfSolves;
    private Map<Pair<CubeType, AVG>, ObservableList<AVGwrapper>> listsOfAvgs;
    private ObservableList<CubeType> currCubeType;
    private ObservableList<Running> isRunning;
    private ObservableList<Move> currentScramble;
    public ObservableWrapper(){
        currentScramble = FXCollections.observableArrayList();
        isRunning = FXCollections.observableArrayList();
        isRunning.addAll(Running.NO);
        currCubeType = FXCollections.observableArrayList();
        currCubeType.add(0, CubeType.THREEBYTHREE);
        listsOfSolves = new HashMap<>();
        listsOfAvgs = new HashMap<>();
        listsOfSolves.put(CubeType.TWOBYTWO, FXCollections.observableArrayList());
        listsOfSolves.put(CubeType.THREEBYTHREE, FXCollections.observableArrayList());
        listsOfSolves.put(CubeType.FOURBYFOUR, FXCollections.observableArrayList());
        listsOfAvgs.put(new Pair<>(CubeType.TWOBYTWO, AVG.Ao5), FXCollections.observableArrayList());
        listsOfAvgs.put(new Pair<>(CubeType.TWOBYTWO, AVG.Ao12), FXCollections.observableArrayList());
        listsOfAvgs.put(new Pair<>(CubeType.TWOBYTWO, AVG.Ao50), FXCollections.observableArrayList());
        listsOfAvgs.put(new Pair<>(CubeType.TWOBYTWO, AVG.Ao100), FXCollections.observableArrayList());
        listsOfAvgs.put(new Pair<>(CubeType.THREEBYTHREE, AVG.Ao5), FXCollections.observableArrayList());
        listsOfAvgs.put(new Pair<>(CubeType.THREEBYTHREE, AVG.Ao12), FXCollections.observableArrayList());
        listsOfAvgs.put(new Pair<>(CubeType.THREEBYTHREE, AVG.Ao50), FXCollections.observableArrayList());
        listsOfAvgs.put(new Pair<>(CubeType.THREEBYTHREE, AVG.Ao100), FXCollections.observableArrayList());
        listsOfAvgs.put(new Pair<>(CubeType.FOURBYFOUR, AVG.Ao5), FXCollections.observableArrayList());
        listsOfAvgs.put(new Pair<>(CubeType.FOURBYFOUR, AVG.Ao12), FXCollections.observableArrayList());
        listsOfAvgs.put(new Pair<>(CubeType.FOURBYFOUR, AVG.Ao50), FXCollections.observableArrayList());
        listsOfAvgs.put(new Pair<>(CubeType.FOURBYFOUR, AVG.Ao100), FXCollections.observableArrayList());
    }

    public ObservableList<Solve> getListOfSolves(CubeType type){
        return listsOfSolves.get(type);
    }
    public ObservableList<AVGwrapper> getListAvg(CubeType type, AVG avg){
        return listsOfAvgs.get(new Pair<>(type, avg));
    }
    public void setCubeCurrType(CubeType type){
        currCubeType.set(0, type);
    }
    public void setRunning(Running running){
        isRunning.set(0, running);
    }
    public ObservableList<CubeType> getCubeCurrType(){
        return currCubeType;
    }
    public ObservableList<Running> getRunning(){
        return isRunning;
    }
    public void setCurrentScramble(ArrayList<Move> newScramble){
        currentScramble.clear();
        currentScramble.addAll(newScramble);
    }
    public ObservableList<Move> getCurrentScramble() {
        return currentScramble;
    }
}
