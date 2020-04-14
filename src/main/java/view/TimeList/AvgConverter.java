package view.TimeList;

import javafx.util.StringConverter;
import model.SS.StatisticServerImplementation;
import model.wrappers.AVGwrapper;

import java.time.format.DateTimeFormatter;

public class AvgConverter extends StringConverter<AVGwrapper> {

    @Override
    public String toString(AVGwrapper avGwrapper) {
        String str = "";
        if (avGwrapper.isDNF()) {
            str = "DNF";
        } else if (avGwrapper.isNET()) {
            str = "-";
        } else {
            str = avGwrapper.getAVG().toLocalDateTime().format(DateTimeFormatter.ofPattern("mm:ss.SSS"));
        }
        return str;
    }

    @Override
    public AVGwrapper fromString(String s) {
        return null;
    }
}
