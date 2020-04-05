package model.db;

import model.enums.CubeType;
import model.enums.State;
import model.logic.*;
import org.junit.Test;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DbTest {
    DatabaseService db = new DatabaseServiceImplementation();

    @Test
    public void initiateDb() {
        db.start();
    }

    @Test
    public void insertAndPullSingle3x3Solve() {
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
        assertEquals(1, list.size());
        Solve pulledSolve = list.get(0);
        assertEquals(pulledSolve.getID(), 1);
        assertEquals(pulledSolve.getTime(), new Time(1000));
        assertEquals(pulledSolve.getState(), State.CORRECT);
        assertEquals("test", pulledSolve.getComment().trim());
        assertEquals(pulledSolve.getScramble(), scramble);
        assertEquals(date.toString().trim(), pulledSolve.getDate().toString().trim());
    }

    @Test
    public void insertAndPullSingle4x4Solve() {
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
        assertEquals(1, list.size());
        Solve pulledSolve = list.get(0);
        assertEquals(pulledSolve.getID(), 1);
        assertEquals(pulledSolve.getTime(), new Time(1000));
        assertEquals(pulledSolve.getState(), State.TWOSECPENALTY);
        assertEquals("test", pulledSolve.getComment().trim());
        assertEquals(pulledSolve.getScramble(), scramble);
        assertEquals(date.toString().trim(), pulledSolve.getDate().toString().trim());
    }

    @Test
    public void insertAndPullSingle2x2Solve() {
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
        assertEquals(1, list.size());
        Solve pulledSolve = list.get(0);
        assertEquals(pulledSolve.getID(), 1);
        assertEquals(pulledSolve.getTime(), new Time(1000));
        assertEquals(pulledSolve.getState(), State.DNF);
        assertEquals("test", pulledSolve.getComment().trim());
        assertEquals(pulledSolve.getScramble(), scramble);
        assertEquals(date.toString().trim(), pulledSolve.getDate().toString().trim());
    }

    @Test
    public void increasesId() {
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

    @Test
    public void deleteTwoByTwoTest() {
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
        db.deleteLast(CubeType.TWOBYTWO);
        assertEquals(3, db.pullAndParseAllSolves(CubeType.TWOBYTWO).size());
        assertEquals(3, db.pullAndParseAllSolves(CubeType.TWOBYTWO).get(2).getID());
    }

    @Test
    public void deleteThreeByThreeTest() {
        db.dropDatabase();
        db.start();
        ScrambleGenerator scr = new ScrambleGeneratorImplementation(CubeType.THREEBYTHREE);
        String scramble = scr.scrambleToString(scr.generate());
        Date date = new Date();
        Solve solve = new SolveImplementation();
        solve.setType(CubeType.THREEBYTHREE);
        solve.setState(State.CORRECT);
        solve.setDate(date);
        solve.setTime(new Time(10000));
        solve.setComment("test2");
        solve.setScramble(scramble);

        solve.setType(CubeType.THREEBYTHREE);
        db.insert(solve);
        db.insert(solve);
        db.insert(solve);
        db.insert(solve);
        db.insert(solve);
        db.insert(solve);
        db.insert(solve);
        db.insert(solve);
        db.deleteLast(CubeType.THREEBYTHREE);
        assertEquals(7, db.pullAndParseAllSolves(CubeType.THREEBYTHREE).size());
        //checks if the very last solve was deleted
        assertEquals(7, db.pullAndParseAllSolves(CubeType.THREEBYTHREE).get(6).getID());
    }

    @Test
    public void deleteFourByFourTest() {
        db.dropDatabase();
        db.start();
        ScrambleGenerator scr = new ScrambleGeneratorImplementation(CubeType.FOURBYFOUR);
        String scramble = scr.scrambleToString(scr.generate());
        Date date = new Date();
        Solve solve = new SolveImplementation();
        solve.setType(CubeType.FOURBYFOUR);
        solve.setState(State.TWOSECPENALTY);
        solve.setDate(date);
        solve.setTime(new Time(100000));
        solve.setComment("test2");
        solve.setScramble(scramble);
        solve.setType(CubeType.FOURBYFOUR);
        db.insert(solve);
        db.insert(solve);
        db.insert(solve);
        db.insert(solve);
        db.insert(solve);
        db.deleteLast(CubeType.FOURBYFOUR);
        assertEquals(4, db.pullAndParseAllSolves(CubeType.FOURBYFOUR).size());
        //checks if the very last solve was deleted
        assertEquals(4, db.pullAndParseAllSolves(CubeType.FOURBYFOUR).get(3).getID());
    }

    @Test
    public void deleteFromEmpty() {
        db.dropDatabase();
        db.start();
        db.deleteLast(CubeType.THREEBYTHREE);
        assertEquals(0, db.pullAndParseAllSolves(CubeType.THREEBYTHREE).size());

    }

    @Test
    public void updateLast() {
        db.dropDatabase();
        db.start();
        Date date = new Date();
        Date date2 = new Date();
        ScrambleGenerator scr = new ScrambleGeneratorImplementation(CubeType.TWOBYTWO);
        ArrayList<Move> scr1 = scr.generate();
        ArrayList<Move> scr2 = scr.generate();
        date2.setTime(1000);
        Solve solve1 = new SolveImplementation();
        Solve solve2 = new SolveImplementation();
        solve1.setType(CubeType.TWOBYTWO);
        solve2.setType(CubeType.TWOBYTWO);
        solve1.setTime(new Time(1000));
        solve2.setTime(new Time(10000));
        solve1.setDate(date);
        solve2.setDate(date2);
        solve1.setComment("test1");
        solve2.setComment("test2");
        solve1.setScramble(scr1.toString());
        solve2.setScramble(scr2.toString());
        solve1.setState(State.DNF);
        solve2.setState(State.CORRECT);
        db.insert(solve1);
        db.updateLast(solve2);
        Solve solve3 = db.pullAndParseAllSolves(CubeType.TWOBYTWO).get(0);
        assertEquals(1, solve3.getID());
        assertEquals(new Time(10000), solve3.getTime());
        assertEquals(date2, solve3.getDate());
        assertEquals(scr2.toString(), solve3.getScramble());
        assertEquals(CubeType.TWOBYTWO, solve3.getType());
        assertEquals("test2", solve3.getComment());
        assertEquals(State.CORRECT, solve3.getState());

        solve1.setType(CubeType.THREEBYTHREE);
        solve2.setType(CubeType.THREEBYTHREE);
        db.insert(solve1);
        db.updateLast(solve2);
        solve3 = db.pullAndParseAllSolves(CubeType.THREEBYTHREE).get(0);
        assertEquals(1, solve3.getID());
        assertEquals(new Time(10000), solve3.getTime());
        assertEquals(date2, solve3.getDate());
        assertEquals(scr2.toString(), solve3.getScramble());
        assertEquals(CubeType.THREEBYTHREE, solve3.getType());
        assertEquals("test2", solve3.getComment());
        assertEquals(State.CORRECT, solve3.getState());

        solve1.setType(CubeType.FOURBYFOUR);
        solve2.setType(CubeType.FOURBYFOUR);
        db.insert(solve1);
        db.updateLast(solve2);
        solve3 = db.pullAndParseAllSolves(CubeType.FOURBYFOUR).get(0);
        assertEquals(1, solve3.getID());
        assertEquals(new Time(10000), solve3.getTime());
        assertEquals(date2, solve3.getDate());
        assertEquals(scr2.toString(), solve3.getScramble());
        assertEquals(CubeType.FOURBYFOUR, solve3.getType());
        assertEquals("test2", solve3.getComment());
        assertEquals(State.CORRECT, solve3.getState());


    }
}
