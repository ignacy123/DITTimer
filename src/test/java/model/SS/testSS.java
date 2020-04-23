package model.SS;


import model.db.DatabaseServiceImplementation;
import model.enums.CubeType;
import model.enums.State;
import model.logic.Solve;
import model.logic.SolveImplementation;
import model.wrappers.ObservableWrapper;
import org.junit.Before;
import org.junit.Test;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class testSS {
    DatabaseServiceImplementation db;
    ObservableWrapper ow=null;
    StatisticServer SS;
    @Before
    public void intit(){
        db = new DatabaseServiceImplementation();
        ow=new ObservableWrapper();
        SS=new StatisticServerImplementation(ow);

    }

    @Test
    public void justComputingAVG() throws StatisticServerImplementation.NotEnoughTimes, StatisticServerImplementation.DNF {
        Random random=new Random();
        db.dropDatabase();
        db.start();
        System.out.println("TEST1 BEGINS=====================================");
        Timestamp[] times=new Timestamp[20];
        Solve[] solves=new SolveImplementation[20];
        long value=0;
        for(int i=0;i<20;i++){
            times[i]=new Timestamp(random.nextInt(9999999));
            solves[i]=new SolveImplementation();
            solves[i].setTime(times[i]);
            value+=times[i].getTime();
        }  // generates Times

        for(int i=0;i<20;i++){
            SS.insertSolve(solves[i]);
        } // inserting to SS

        Time average=new Time(value/20);
        assertEquals(SS.GiveMeAverage(20,CubeType.THREEBYTHREE).getTime(),average.getTime());
        // generated 20-average correctly from 20 times
        System.out.println("Computed 20-average correctly");
        try{
            SS.GiveMeAverage(100,CubeType.THREEBYTHREE);
        } catch (StatisticServerImplementation.DNF dnf) {
            dnf.printStackTrace();
        } catch (StatisticServerImplementation.NotEnoughTimes notEnoughTimes) {
            System.out.println("Not enough times for "+ 100 + " average");
        }
    }

    @Test
    public void AutomaticRefresh() throws StatisticServerImplementation.NotEnoughTimes, StatisticServerImplementation.DNF {
        Random random=new Random();
        db.dropDatabase();
        db.start();
        System.out.println("TEST2 BEGINS=====================================");
        Timestamp[] times=new Timestamp[5];
        Solve[] solves=new SolveImplementation[5];
        long value=0;

        for(int i=0;i<3;i++){
            times[i]=new Timestamp(random.nextInt(99999));
            solves[i]=new SolveImplementation();
            solves[i].setTime(times[i]);
            value+=times[i].getTime();
        }  // generates Times

        for(int i=0;i<3;i++){
            SS.insertSolve(solves[i]);
        } // inserting to SS
        for(int i=0;i<3;i++){
           System.out.println(times[i]);
        }
        Time average=new Time(value/3);
        assertEquals(SS.GiveMeAverage(3,CubeType.THREEBYTHREE).getTime(),average.getTime());
        System.out.println("Computed static: " + average);
        System.out.println("Computed by SS: " + SS.GiveMeAverage(3,CubeType.THREEBYTHREE));
        System.out.println("Lets add one time, see if average refreshes");
        Timestamp addtime=new Timestamp(random.nextInt(99999));
        Solve addedsolve=new SolveImplementation();
        addedsolve.setTime(addtime);
        System.out.println("Added " + addtime);
        SS.insertSolve(addedsolve);
        System.out.println("Computed static: " + new Time((times[1].getTime()+times[2].getTime()+addtime.getTime())/3));
        System.out.println("Computed by SS: " + SS.GiveMeAverage(3,CubeType.THREEBYTHREE));
    }

    @Test
    public void DNF_and_DEL(){
        db.dropDatabase();
        db.start();
        SS.delete(CubeType.THREEBYTHREE);
        Random random=new Random();
        Timestamp[] times=new Timestamp[5];
        Solve[] solves=new SolveImplementation[5];
        long value=0;
        for(int i=0;i<5;i++){
            times[i]=new Timestamp(random.nextInt(99999));
            solves[i]=new SolveImplementation();
            solves[i].setTime(times[i]);
            SS.insertSolve(solves[i]);
            value+=times[i].getTime();
        }  // generates Times
        solves[2].setState(State.DNF);
        value=value-times[2].getTime();
        try {
            assertEquals(value/5,SS.GiveMeAverage(5,CubeType.THREEBYTHREE).getTime());
        } catch (StatisticServerImplementation.NotEnoughTimes | StatisticServerImplementation.DNF notEnoughTimes) {
            notEnoughTimes.printStackTrace();
        }
        System.out.println("SS computed correctly average when one of times was set to DNF");


        value+=times[2].getTime();
        solves[2].setState(State.CORRECT);

        System.out.println("Deleting last");
        SS.DeleteLast(CubeType.THREEBYTHREE);
        try {
            SS.CreateAverage(10,CubeType.THREEBYTHREE);
        } catch (StatisticServerImplementation.NotEnoughTimes notEnoughTimes) {
            System.out.println("NET as expected");
        } catch (StatisticServerImplementation.DNF dnf) {
            dnf.printStackTrace();
        }

        SS.insertSolve(solves[4]);
        System.out.println("Back to normal");
        try {
            assertEquals(value/5,SS.GiveMeAverage(5,CubeType.THREEBYTHREE).getTime());
        } catch (StatisticServerImplementation.NotEnoughTimes | StatisticServerImplementation.DNF notEnoughTimes) {
            System.out.println("Not enough times was thrown as expected");
        }
    }

    @Test
    public void MAXandMIN(){
        db.dropDatabase();
        db.start();
        SS.delete(CubeType.THREEBYTHREE);
        Random random = new Random();
        Timestamp[] times = new Timestamp[5];
        Solve[] solves=new SolveImplementation[5];
        long value = 0;
        for (int i = 0; i < 5; i++) {
            times[i] = new Timestamp(random.nextInt(999999));
            solves[i]=new SolveImplementation();
            solves[i].setTime(times[i]);
            value += times[i].getTime();
        }  // generates Times
        for (int i = 0; i < 5; i++)
            SS.insertSolve(solves[i]);
        for (int i = 0; i < 5; i++)
            System.out.println(times[i]);
        System.out.println("MAX");
        System.out.println(SS.GiveMeMax( CubeType.THREEBYTHREE));
        System.out.println("MIN");
        System.out.println(SS.GiveMeMin(CubeType.THREEBYTHREE));
    }

    @Test
    public void TimesToArray() throws StatisticServerImplementation.NotEnoughTimes, StatisticServerImplementation.DNF {
        db.dropDatabase();
        db.start();
        SS.delete(CubeType.THREEBYTHREE);
        ArrayList<Timestamp> timesStatic=new ArrayList<>();
        Random random = new Random();
        Timestamp[] times = new Timestamp[5];
        Solve[] solves=new SolveImplementation[5];
        for (int i = 0; i < 5; i++) {
            times[i] = new Timestamp(random.nextInt(999999));
            solves[i]=new SolveImplementation();
            solves[i].setTime(times[i]);
            timesStatic.add(times[i]);
        }  // generates Times
        for (int i = 0; i < 5; i++)
            SS.insertSolve(solves[i]);
        assertEquals(timesStatic,SS.GiveMeTimes(CubeType.THREEBYTHREE));
    }

}