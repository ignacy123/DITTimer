package model.logic;

import model.enums.CubeType;
import model.enums.State;

import java.sql.Time;
import java.util.Date;

public interface Solve {
    Time getTime();
    State getState();
    Date getDate();
    int getID();
    CubeType getType();
    String getScramble();
    String getComment();



    void setTime(Time time);
    void setState(State state);
    void setDate(Date date);
    void setID(int id);
    void setType(CubeType type);
    void setScramble(String scr);
    void setComment(String com);

    void rejectSolution(); // changes stato to REJ
    void Penalty(); // modifies time of solution
}
