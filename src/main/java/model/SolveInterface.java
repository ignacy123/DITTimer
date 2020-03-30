package model;

import java.sql.Time;

public interface SolveInterface {
    Time extractTime();
    State extractState();
    String AddComment();
    void rejectSolution(); // changes stato to REJ
    void Penalty(); // modifies time of solution
}
