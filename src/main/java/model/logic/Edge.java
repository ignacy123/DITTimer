package model.logic;

import javafx.util.Pair;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(first, edge.first) &&
                Objects.equals(second, edge.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
