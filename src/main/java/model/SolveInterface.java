package model;

import java.sql.Time;

public interface SolveInterface {
    Time extraxtTime(Solve solution);
    State extractState(Solve solution);
    String AddComment();
    void rejectSolution(); // changes stato to REJ
    void Penalty(); // modifies time of solution
}
