package model.logic;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Objects;

public class OrientationLessCorner {
    Color first;
    Color second;
    Color third;

    public OrientationLessCorner(Color first, Color second, Color third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public Color getFirst() {
        return first;
    }

    public Color getSecond() {
        return second;
    }

    public Color getThird() {
        return third;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrientationLessCorner that = (OrientationLessCorner) o;
        if(that.first.equals(first) && that.second.equals(second) && that.third.equals(third)){
            return true;
        }
        if(that.first.equals(second) && that.second.equals(third) && that.third.equals(first)){
            return true;
        }
        if(that.first.equals(third) && that.second.equals(first) && that.third.equals(second)){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        ArrayList<String> a = new ArrayList<>();
        a.add(first.toString());
        a.add(second.toString());
        a.add(third.toString());
        a.sort(String::compareTo);
        return Objects.hash(a);
    }
}
