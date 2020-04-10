package view.TimeList;

import javafx.util.StringConverter;
import model.logic.Solve;

import java.time.format.DateTimeFormatter;

public class SolveConverter extends StringConverter<Solve> {

    @Override
    public String toString(Solve solve) {
        //return solve.getTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("mm:ss.SSS"));
        return "jak daniel zmienie date to bedzie zmieniona";
    }

    @Override
    public Solve fromString(String s) {
        return null;
    }
}
