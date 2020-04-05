package model.testMain;


import model.logic.*;
import model.db.DatabaseService;
import model.db.DatabaseServiceImplementation;
import model.enums.CubeType;
import model.enums.State;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;


public class main {
    public static void main(String args[]) {
        DatabaseService db = new DatabaseServiceImplementation();
        ScrambleGenerator scr = new ScrambleGeneratorImplementation(CubeType.THREEBYTHREE);
        db.dropDatabase();
        db.start();
        Solve solve = new SolveImplementation();
        solve.setType(CubeType.THREEBYTHREE);
        solve.setState(State.CORRECT);
        solve.setDate(new Date());
        solve.setTime(new Time(1000));
        solve.setComment("");
        solve.setScramble(scr.scrambleToString(scr.generate()));
        db.insert(solve);
        ArrayList<Solve> list = db.pullAndParseAllSolves(CubeType.THREEBYTHREE);
        System.out.println(list.get(0).getID());
        System.out.println(list.get(0).getTime());
        System.out.println(list.get(0).getType());
        System.out.println(list.get(0).getDate());
        System.out.println(list.get(0).getState());
        System.out.println(list.get(0).getComment());
        System.out.println(list.get(0).getScramble());
        solve.setComment("gywd");
        solve.setTime(new Time(10000));
        System.out.println(db.pullAndParseAllSolves(CubeType.THREEBYTHREE).get(0).getTime());
        db.updateLast(solve);
        System.out.println(db.pullAndParseAllSolves(CubeType.THREEBYTHREE).get(0).getTime());
        System.out.println(db.pullAndParseAllSolves(CubeType.THREEBYTHREE).get(0).getComment());
    }
}