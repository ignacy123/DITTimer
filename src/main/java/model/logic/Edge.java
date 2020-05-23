package model.logic;

import javafx.util.Pair;

public class Edge {
    Pair<Integer, Integer> first;
    Pair<Integer, Integer> second;
    public Edge(Pair a, Pair b){
        first = a;
        second = b;
    }

    public Pair<Integer, Integer> getFirst() {
        return first;
    }

    public Pair<Integer, Integer> getSecond() {
        return second;
    }
}
