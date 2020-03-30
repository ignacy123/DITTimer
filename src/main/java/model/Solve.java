package model;

import java.sql.Time;

public interface Solve {
    Time extractTime(SolveImplementation solution);
    State extractState(SolveImplementation solution);
    String AddComment();
    void rejectSolution(); // changes stato to REJ
    void Penalty(); // modifies time of solution
}
