package model.logic;

import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Objects;

public class OrientationLessEdge {
    Color first;
    Color second;

    public OrientationLessEdge(Color first, Color second) {
        this.first = first;
        this.second = second;
    }

    public Color getFirst() {
        return first;
    }

    public Color getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrientationLessEdge that = (OrientationLessEdge) o;
        if(that.first.equals(first) && that.second.equals(second)){
            return true;
        }
        if(that.first.equals(second) && that.second.equals(first)){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        ArrayList<String> a = new ArrayList<>();
        a.add(first.toString());
        a.add(second.toString());
        a.sort(String::compareTo);
        return Objects.hash(a);
    }
}
