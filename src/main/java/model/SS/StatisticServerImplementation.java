package model.SS;


import model.db.DatabaseServiceImplementation;
import model.enums.CubeType;
import model.enums.State;
import model.logic.Solve;
import model.logic.SolveImplementation;

import java.util.*;
import java.sql.Time;

public class StatisticServerImplementation implements StatisticServer{

    DatabaseServiceImplementation myDataBase;
   // HashMap<String,Time> Averages;
    ArrayList<Solve> TwoByTwo=new ArrayList<>();
    ArrayList<Solve> TreeByTree=new ArrayList<>();
    ArrayList<Solve> FourByFour=new ArrayList<>();
    Date CurrentDate;
    public StatisticServerImplementation(DatabaseServiceImplementation db){
        myDataBase=db;
        CurrentDate=new Date();
        TwoByTwo.addAll(myDataBase.pullAndParseAllSolves(CubeType.TWOBYTWO));
        TreeByTree.addAll(myDataBase.pullAndParseAllSolves(CubeType.THREEBYTHREE));
        FourByFour.addAll(myDataBase.pullAndParseAllSolves(CubeType.FOURBYFOUR));
      //  Averages=new HashMap<>();

    }
    @Override
    public Time GiveMeAverage(int WhatAverage, CubeType WhatModel) throws DNF, NotEnoughTimes {
        Time average;
            try {
                average=CreateAverage(WhatAverage, WhatModel);
            } catch (NotEnoughTimes notEnoughTimes) {
                throw new NotEnoughTimes();
            } catch (DNF dnf) {
                throw new DNF();
            }
            return average;
    }
    private Time CreateAverage(int WhatAverage,CubeType WhatModel) throws DNF, NotEnoughTimes {
        ArrayList<Solve> temp;

        if (WhatModel == CubeType.TWOBYTWO) {
            temp = TwoByTwo;
        } else if (WhatModel == CubeType.THREEBYTHREE) {
            temp = TreeByTree;
        } else {
            temp = FourByFour;
        }
        if (temp.size() < WhatAverage) throw new NotEnoughTimes();
        else {
            int helper = WhatAverage;
            long value = 0;
            int amountOfDNF = 0;
            for (int i = temp.size() - 1; helper > 0; i--, helper--) {
                if (temp.get(i).getState() == State.DNF) {
                    amountOfDNF++;
                    if (amountOfDNF == 2) break;
                } else value += temp.get(i).getTime().getTime(); // unfortunate
            }
            if (amountOfDNF >= 2) throw new DNF();
            else {
               return new Time(value / WhatAverage);
            }
        }
    }

    @Override
    public ArrayList<Time> GiveMeTimes(CubeType WhatModel) {
        ArrayList<Solve> temp;
        if (WhatModel == CubeType.TWOBYTWO) {
           temp=TwoByTwo;
        } else if (WhatModel == CubeType.THREEBYTHREE) {
           temp=TreeByTree;
        } else {
            temp=FourByFour;
        }
        ArrayList<Time> times =new ArrayList<>();
        for(Solve a: temp){
            if(a.getState()==State.DNF || a.getState()==State.REJ) continue;
            times.add(a.getTime());
        }
        return times;
    }

    @Override
    public Time GiveMeMax(CubeType WhatModel) {
        ArrayList<Solve> temp;
        Time max=new Time(0);
        if (WhatModel == CubeType.TWOBYTWO) {
            temp=TwoByTwo;
        } else if (WhatModel == CubeType.THREEBYTHREE) {
            temp=TreeByTree;
        } else {
            temp=FourByFour;
        }
        for(Solve a : temp){
            if(a.getTime().getTime()>max.getTime()) // unfortunate
                max=a.getTime();
        }
        return max;
    }

    @Override
    public Time GiveMeMin(CubeType WhatModel) {
        ArrayList<Solve> temp;
        Time min=new Time(999999999);
        if (WhatModel == CubeType.TWOBYTWO) {
            temp=TwoByTwo;
        } else if (WhatModel == CubeType.THREEBYTHREE) {
            temp=TreeByTree;
        } else {
            temp=FourByFour;
        }
        for(Solve a : temp){
            if(a.getTime().getTime()<min.getTime()) // unfortunate
                min=a.getTime();
        }
        return min;
    }

    @Override
    public void ChangeStateLast(CubeType WhatModel, State state) {
        ArrayList<Solve> temp;
        if (WhatModel == CubeType.TWOBYTWO) {
            temp=TwoByTwo;
        } else if (WhatModel == CubeType.THREEBYTHREE) {
            temp=TreeByTree;
        } else {
            temp=FourByFour;
        }
        temp.get(temp.size()-1).setState(state);
        myDataBase.updateLast(temp.get(temp.size()-1));
    }

    @Override
    public void DeleteLast(CubeType WhatModel) {
        ArrayList<Solve> temp;
        if (WhatModel == CubeType.TWOBYTWO) {
            temp=TwoByTwo;
        } else if (WhatModel == CubeType.THREEBYTHREE) {
            temp=TreeByTree;
        } else {
            temp=FourByFour;
        }
        temp.remove(temp.size()-1);
        myDataBase.deleteLast(WhatModel);
    }

    @Override
    public Solve insertAndPackToSolve(Time timeOfSolution, CubeType WhatModel) {
        Solve solve=new SolveImplementation();
        solve.setDate(CurrentDate);
        solve.setTime(timeOfSolution);
        solve.setType(WhatModel);
        if (WhatModel == CubeType.TWOBYTWO) {
            TwoByTwo.add(solve);
        } else if (WhatModel == CubeType.THREEBYTHREE) {
            TreeByTree.add(solve);
        } else {
            FourByFour.add(solve);
        }
        // state
        //comment
        // scramble
      //  myDataBase.insert(solve);
        return solve;
    }

   /* private void Refresh(HashSet<Integer> set, Time timeOfSolution, CubeType WhatModel){
        String model;
        ArrayList<Solve> temp;
        if (WhatModel == CubeType.TWOBYTWO) {
            model = "TWOBYTWO";
            temp=TwoByTwo;
        } else if (WhatModel == CubeType.THREEBYTHREE) {
            model = "THREEBYTHREE";
            temp=TreeByTree;
        } else {
            model = "FOURBYFOUR";
            temp=FourByFour;
        }
        for(Integer a : set){
            System.out.println("doing refresh for " + a);
            System.out.println(Averages.get(model + a).getTime());
            System.out.println(temp.get());
            System.out.println(Averages.get(model + a).getTime()-temp.get(temp.size()-a).getTime().getTime()+timeOfSolution.getTime()/a);

           // Averages.replace(model+a,new Time(Averages.get(model + a).getTime()-temp.get(temp.size()-a).getTime().getTime()+timeOfSolution.getTime()/a));
        }
    }*/

    public static class DNF extends Exception{
    }
    public static class NotEnoughTimes extends Exception{
    }
}
