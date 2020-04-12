package model.logic;

import model.enums.CubeType;
import model.enums.State;

import java.sql.Timestamp;
import java.util.Date;

public interface Solve {
    Timestamp getTime();
    State getState();
    Date getDate();
    int getID();
    CubeType getType();
    String getScramble();
    String getComment();



    void setTime(Timestamp time);
    void setState(State state);
    void setDate(Date date);
    void setID(int id);
    void setType(CubeType type);
    void setScramble(String scr);
    void setComment(String com);
    String toString();

    void rejectSolution(); // changes stato to REJ
    void Penalty(); // modifies time of solution
}
