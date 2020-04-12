package model.SS;


import javafx.collections.ObservableList;
import model.db.DatabaseServiceImplementation;
import model.enums.CubeType;
import model.enums.State;
import model.logic.Solve;
import model.logic.SolveImplementation;

import java.util.*;
import java.sql.Timestamp;

public class StatisticServerImplementation implements StatisticServer{
    class AVGwrapper{
        boolean isDNF;
        int ID;
        Timestamp average;
        AVGwrapper(int i,Timestamp avg, boolean temp){
            ID=i;
            average=avg;
            isDNF=temp;
        }
    }
    DatabaseServiceImplementation myDataBase;
    /*
    ArrayList<Solve> TwoByTwo=new ArrayList<>();
    ArrayList<Solve> TreeByTree=new ArrayList<>();
    ArrayList<Solve> FourByFour=new ArrayList<>();
    */
    ObservableList<Solve> TwoByTwo;
    ObservableList<Solve> TreeByTree;
    ObservableList<Solve> FourByFour;
    ArrayList<AVGwrapper> A5Two=new ArrayList<>();
    ArrayList<AVGwrapper> A12Two=new ArrayList<>();
    ArrayList<AVGwrapper> A5Tree=new ArrayList<>();
    ArrayList<AVGwrapper> A12Tree=new ArrayList<>();
    ArrayList<AVGwrapper> A5Four=new ArrayList<>();
    ArrayList<AVGwrapper> A12Four=new ArrayList<>(); // history

    Date CurrentDate;
    public StatisticServerImplementation(DatabaseServiceImplementation db,ObservableList<Solve> TWO,ObservableList<Solve> TREE,ObservableList<Solve> FOUR){
        myDataBase=db;
        CurrentDate=new Date();
        TwoByTwo=TWO;
        TreeByTree=TREE;
        FourByFour=FOUR; //taking lists from outside
        TwoByTwo.addAll(myDataBase.pullAndParseAllSolves(CubeType.TWOBYTWO));
        TreeByTree.addAll(myDataBase.pullAndParseAllSolves(CubeType.THREEBYTHREE));
        FourByFour.addAll(myDataBase.pullAndParseAllSolves(CubeType.FOURBYFOUR)); //provides times to those lists
        initializeHistory(A5Two,TwoByTwo,5);
        initializeHistory(A12Two,TwoByTwo,12);
        initializeHistory(A5Tree,TreeByTree,5);
        initializeHistory(A12Tree,TreeByTree,12);
        initializeHistory(A5Four,FourByFour,5);
        initializeHistory(A12Four,FourByFour,12);
    }
    private void initializeHistory(ArrayList<AVGwrapper> ToFill, ObservableList<Solve> source, int k){
        if(source.size()<k) return;
        long value=0;
        int ID=0;
        int i;
        for(i=0;i<k;i++) value+=source.get(i).getTime().getTime();
        value=value/k;
        ToFill.add(new AVGwrapper(ID,new Timestamp(value),false));
        ID++;
        i++;
        while (i<source.size()) {
            value = value * k;
            if (source.get(i).getState() == State.DNF)
                value += 0;
            else value += source.get(i).getTime().getTime();
            if (source.get(i-k).getState() == State.DNF)
                value -= 0;
            else value -= source.get(i-k).getTime().getTime();
            value = value / k;
            ToFill.add(new AVGwrapper(ID, new Timestamp(value), false));
            ID++;
            i++;
        }
    } // serve DNF to be add;
    @Override
    public Timestamp GiveMeAverage(int WhatAverage, CubeType WhatModel) throws DNF, NotEnoughTimes {
        Timestamp average;
        ArrayList<AVGwrapper>temp;
        if(WhatAverage==5 && WhatModel==CubeType.TWOBYTWO) temp=A5Two;
        else if(WhatAverage==12 && WhatModel==CubeType.TWOBYTWO) temp=A12Two;
        else if(WhatAverage==5 && WhatModel==CubeType.THREEBYTHREE) temp=A5Tree;
        else if(WhatAverage==12 && WhatModel==CubeType.THREEBYTHREE) temp=A12Tree;
        else if(WhatAverage==5 && WhatModel==CubeType.FOURBYFOUR) temp=A5Four;
        else temp=A12Four;

            try {
                average=CreateAverage(WhatAverage, WhatModel);
            } catch (NotEnoughTimes notEnoughTimes) {
                throw new NotEnoughTimes();
            } catch (DNF dnf) {
                temp.add(new AVGwrapper(temp.size(),new Timestamp(0),true));
                throw new DNF();
            }
            temp.add(new AVGwrapper(temp.size(),average,false));
            return average;
    }
    private Timestamp CreateAverage(int WhatAverage,CubeType WhatModel) throws DNF, NotEnoughTimes {
        ObservableList<Solve> temp;
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
               return new Timestamp(value / WhatAverage);
            }
        }
    }

    @Override
    public ArrayList<Timestamp> GiveMeTimes(CubeType WhatModel) {
        ObservableList<Solve> temp;
        if (WhatModel == CubeType.TWOBYTWO) {
           temp=TwoByTwo;
        } else if (WhatModel == CubeType.THREEBYTHREE) {
           temp=TreeByTree;
        } else {
            temp=FourByFour;
        }
        ArrayList<Timestamp> times =new ArrayList<>();
        for(Solve a: temp){
            if(a.getState()==State.DNF || a.getState()==State.REJ) continue;
            times.add(a.getTime());
        }
        return times;
    }

    @Override
    public void insertSolve(Solve solve) {
        if (solve.getType()==CubeType.THREEBYTHREE){
            TreeByTree.add(solve);
        } else if (solve.getType()==CubeType.TWOBYTWO) {
           TwoByTwo.add(solve);
        } else {
            FourByFour.add(solve);
        }
    }

    @Override
    public Timestamp GiveMeMax(CubeType WhatModel) {
        ObservableList<Solve> temp;
        Timestamp max=new Timestamp(0);
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
    public Timestamp GiveMeMin(CubeType WhatModel) {
        ObservableList<Solve> temp;
        Timestamp min=new Timestamp(999999999);
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
        ObservableList<Solve> temp;
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
        ObservableList<Solve> temp;
        if (WhatModel == CubeType.TWOBYTWO) {
            temp=TwoByTwo;
        } else if (WhatModel == CubeType.THREEBYTHREE) {
            temp=TreeByTree;
        } else {
            temp=FourByFour;
        }
        temp.remove(temp.size()-1);
        //////////////////////////////////////////////
        myDataBase.deleteLast(WhatModel);
    }

    @Override
    public void insertAndPackToSolve(Timestamp timeOfSolution, CubeType WhatModel) {
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
        myDataBase.insert(solve);
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
