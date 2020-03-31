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



    void setTime(Time time);
    void setState(State state);
    void setDate(Date date);
    void setID(int id);
    void setType(CubeType type);

    void rejectSolution(); // changes stato to REJ
    void Penalty(); // modifies time of solution
}
