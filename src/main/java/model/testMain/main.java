package model.testMain;


import model.logic.Solve;
import model.logic.SolveImplementation;
import model.db.DatabaseService;
import model.db.DatabaseServiceImplementation;
import model.enums.CubeType;
import model.enums.State;

import java.sql.Time;
import java.util.Date;


public class main {
    public static void main(String args[]) {
        DatabaseService db = new DatabaseServiceImplementation();
        db.start();
        Solve solve = new SolveImplementation();
        solve.setType(CubeType.THREEBYTHREE);
        solve.setState(State.CORRECT);
        solve.setDate(new Date());
        solve.setTime(new Time(1000));
    }
}