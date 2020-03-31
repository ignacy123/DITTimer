package model.db;

import model.enums.CubeType;
import model.enums.State;
import model.logic.ScrambleGenerator;
import model.logic.ScrambleGeneratorImplementation;
import model.logic.Solve;
import model.logic.SolveImplementation;
import org.junit.Test;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DbTest {
    DatabaseService db = new DatabaseServiceImplementation();
    @Test
    public void initiateDb(){
        db.start();
    }
    @Test
    public void insertAndPullSingle3x3Solve(){
        db.dropDatabase();
        db.start();
        ScrambleGenerator scr = new ScrambleGeneratorImplementation(CubeType.THREEBYTHREE);
        String scramble = scr.scrambleToString(scr.generate());
        Date date = new Date();
        Solve solve = new SolveImplementation();
        solve.setType(CubeType.THREEBYTHREE);
        solve.setState(State.CORRECT);
        solve.setDate(date);
        solve.setTime(new Time(1000));
        solve.setComment("test");
        solve.setScramble(scramble);
        db.insert(solve);
        ArrayList<Solve> list = db.pullAndParseAllSolves(CubeType.THREEBYTHREE);
        assertEquals(1,list.size());
        Solve pulledSolve = list.get(0);
        assertEquals(pulledSolve.getID(), 1);
        assertEquals(pulledSolve.getTime(), new Time(1000));
        assertEquals(pulledSolve.getState(), State.CORRECT);
        assertEquals("test", pulledSolve.getComment().trim());
        assertEquals(pulledSolve.getScramble(), scramble);
        assertEquals(date.toString().trim(), pulledSolve.getDate().toString().trim());
    }
    @Test
    public void insertAndPullSingle4x4Solve(){
        db.dropDatabase();
        db.start();
        ScrambleGenerator scr = new ScrambleGeneratorImplementation(CubeType.FOURBYFOUR);
        String scramble = scr.scrambleToString(scr.generate());
        Date date = new Date();
        Solve solve = new SolveImplementation();
        solve.setType(CubeType.FOURBYFOUR);
        solve.setState(State.TWOSECPENALTY);
        solve.setDate(date);
        solve.setTime(new Time(1000));
        solve.setComment("test");
        solve.setScramble(scramble);
        db.insert(solve);
        ArrayList<Solve> list = db.pullAndParseAllSolves(CubeType.FOURBYFOUR);
        assertEquals(1,list.size());
        Solve pulledSolve = list.get(0);
        assertEquals(pulledSolve.getID(), 1);
        assertEquals(pulledSolve.getTime(), new Time(1000));
        assertEquals(pulledSolve.getState(), State.TWOSECPENALTY);
        assertEquals("test", pulledSolve.getComment().trim());
        assertEquals(pulledSolve.getScramble(), scramble);
        assertEquals(date.toString().trim(), pulledSolve.getDate().toString().trim());
    }
    @Test
    public void insertAndPullSingle2x2Solve(){
        db.dropDatabase();
        db.start();
        ScrambleGenerator scr = new ScrambleGeneratorImplementation(CubeType.TWOBYTWO);
        String scramble = scr.scrambleToString(scr.generate());
        Date date = new Date();
        Solve solve = new SolveImplementation();
        solve.setType(CubeType.TWOBYTWO);
        solve.setState(State.DNF);
        solve.setDate(date);
        solve.setTime(new Time(1000));
        solve.setComment("test");
        solve.setScramble(scramble);
        db.insert(solve);
        ArrayList<Solve> list = db.pullAndParseAllSolves(CubeType.TWOBYTWO);
        assertEquals(1,list.size());
        Solve pulledSolve = list.get(0);
        assertEquals(pulledSolve.getID(), 1);
        assertEquals(pulledSolve.getTime(), new Time(1000));
        assertEquals(pulledSolve.getState(), State.DNF);
        assertEquals("test", pulledSolve.getComment().trim());
        assertEquals(pulledSolve.getScramble(), scramble);
        assertEquals(date.toString().trim(), pulledSolve.getDate().toString().trim());
    }
    @Test
    public void increasesId(){
        db.dropDatabase();
        db.start();
        ScrambleGenerator scr = new ScrambleGeneratorImplementation(CubeType.TWOBYTWO);
        String scramble = scr.scrambleToString(scr.generate());
        Date date = new Date();
        Solve solve = new SolveImplementation();
        solve.setType(CubeType.TWOBYTWO);
        solve.setState(State.DNF);
        solve.setDate(date);
        solve.setTime(new Time(1000));
        solve.setComment("test");
        solve.setScramble(scramble);
        db.insert(solve);
        db.insert(solve);
        db.insert(solve);
        db.insert(solve);
        ArrayList<Solve> list1 = db.pullAndParseAllSolves(CubeType.TWOBYTWO);
        assertEquals(list1.size(), 4);
        assertEquals(list1.get(3).getID(), 4);
        solve.setType(CubeType.THREEBYTHREE);
        db.insert(solve);
        db.insert(solve);
        db.insert(solve);
        db.insert(solve);
        db.insert(solve);
        db.insert(solve);
        db.insert(solve);
        db.insert(solve);
        ArrayList<Solve> list2 = db.pullAndParseAllSolves(CubeType.THREEBYTHREE);
        assertEquals(list1.size(), 4);
        assertEquals(list1.get(3).getID(), 4);
        assertEquals(list2.size(), 8);
        assertEquals(list2.get(7).getID(), 8);
        solve.setType(CubeType.FOURBYFOUR);
        db.insert(solve);
        db.insert(solve);
        ArrayList<Solve> list3 = db.pullAndParseAllSolves(CubeType.FOURBYFOUR);
        assertEquals(list1.size(), 4);
        assertEquals(list1.get(3).getID(), 4);
        assertEquals(list2.size(), 8);
        assertEquals(list2.get(7).getID(), 8);
        assertEquals(list3.size(), 2);
        assertEquals(list3.get(1).getID(), 2);
    }
}
