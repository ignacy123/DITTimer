package model.logic;

import javafx.util.Pair;

import java.util.Objects;

public class Corner {
    Pair<Integer, Integer> first;
    Pair<Integer, Integer> second;
    Pair<Integer, Integer> third;

    public Corner(Pair<Integer, Integer> first, Pair<Integer, Integer> second, Pair<Integer, Integer> third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public Pair<Integer, Integer> getFirst() {
        return first;
    }

    public Pair<Integer, Integer> getSecond() {
        return second;
    }

    public Pair<Integer, Integer> getThird() {
        return third;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Corner corner = (Corner) o;
        return Objects.equals(first, corner.first) &&
                Objects.equals(second, corner.second) &&
                Objects.equals(third, corner.third);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third);
    }
}
