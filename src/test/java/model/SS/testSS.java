package model.SS;

import model.db.DatabaseService;
import model.db.DatabaseServiceImplementation;
import model.enums.Axis;
import model.enums.CubeType;
import model.enums.Direction;
import model.logic.Move;
import model.logic.MoveImplementation;
import model.logic.ScrambleGenerator;
import model.logic.ScrambleGeneratorImplementation;
import org.junit.Test;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

public class testSS {

    DatabaseServiceImplementation db = new DatabaseServiceImplementation();

    @Test
    public void check() throws StatisticServerImplementation.NotEnoughTimes, StatisticServerImplementation.DNF {
        Random random=new Random();
        db.dropDatabase();
        db.start();
        StatisticServer SS = new StatisticServerImplementation(db);
        System.out.println("TEST1 BEGINS=====================================");
        Time[] times=new Time[20];
        long value=0;
        for(int i=0;i<20;i++){
            times[i]=new Time(random.nextInt(9999999));
            value+=times[i].getTime();
        }  // generates Times

        for(int i=0;i<20;i++){
            SS.insertAndPackToSolve(times[i],CubeType.THREEBYTHREE);
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
    public void check2() throws StatisticServerImplementation.NotEnoughTimes, StatisticServerImplementation.DNF {
        Random random=new Random();
        db.dropDatabase();
        db.start();
        StatisticServer SS = new StatisticServerImplementation(db);
        System.out.println("TEST2 BEGINS=====================================");
        Time[] times=new Time[5];
        long value=0;

        for(int i=0;i<3;i++){
            times[i]=new Time(random.nextInt(99999));

            value+=times[i].getTime();
        }  // generates Times

        for(int i=0;i<3;i++){
            SS.insertAndPackToSolve(times[i],CubeType.THREEBYTHREE);
        } // inserting to SS
        for(int i=0;i<3;i++){
           System.out.println(times[i]);
        }
        Time average=new Time(value/3);
        assertEquals(SS.GiveMeAverage(3,CubeType.THREEBYTHREE).getTime(),average.getTime());
        System.out.println("Computed static: " + average);
        System.out.println("Computed by SS: " + SS.GiveMeAverage(3,CubeType.THREEBYTHREE));
        System.out.println("Lets add one time, see if average refreshes");
        Time addtime=new Time(random.nextInt(99999));
        System.out.println("Added " + addtime);
        SS.insertAndPackToSolve(addtime,CubeType.THREEBYTHREE);
        System.out.println("Computed static: " + new Time((times[1].getTime()+times[2].getTime()+addtime.getTime())/3));
        System.out.println("Computed by SS: " + SS.GiveMeAverage(3,CubeType.THREEBYTHREE));
    }
    @Test
    public void check3() throws StatisticServerImplementation.NotEnoughTimes, StatisticServerImplementation.DNF {
       
    }
}