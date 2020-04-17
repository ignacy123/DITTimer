package model.SS;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.db.DatabaseServiceImplementation;
import model.enums.AVG;
import model.enums.CubeType;
import model.enums.State;
import model.logic.Solve;
import model.logic.SolveImplementation;
import model.wrappers.AVGwrapper;
import model.wrappers.ObservableWrapper;


import java.util.*;
import java.sql.Timestamp;

public class StatisticServerImplementation implements StatisticServer {
    DatabaseServiceImplementation myDataBase;

    ObservableList<Solve> TwoByTwo;
    ObservableList<Solve> TreeByTree;
    ObservableList<Solve> FourByFour;
    ObservableList<AVGwrapper> A5Two;
    ObservableList<AVGwrapper> A12Two;
    ObservableList<AVGwrapper> A5Tree;
    ObservableList<AVGwrapper> A12Tree;
    ObservableList<AVGwrapper> A5Four;
    ObservableList<AVGwrapper> A12Four; // history

    Date CurrentDate;

    public StatisticServerImplementation(ObservableWrapper OW) {
        myDataBase = new DatabaseServiceImplementation();
        myDataBase.start();
        CurrentDate = new Date();
        TwoByTwo = OW.getListOfSolves(CubeType.TWOBYTWO);
        TreeByTree = OW.getListOfSolves(CubeType.THREEBYTHREE);
        FourByFour = OW.getListOfSolves(CubeType.FOURBYFOUR); //taking lists of solves from outside
        TwoByTwo.addAll(myDataBase.pullAndParseAllSolves(CubeType.TWOBYTWO));
        TreeByTree.addAll(myDataBase.pullAndParseAllSolves(CubeType.THREEBYTHREE));
        FourByFour.addAll(myDataBase.pullAndParseAllSolves(CubeType.FOURBYFOUR)); //provides times to those lists from db
        A5Two = OW.getListAvg(CubeType.TWOBYTWO, AVG.Ao5);
        A12Two = OW.getListAvg(CubeType.TWOBYTWO, AVG.Ao12);
        A5Tree = OW.getListAvg(CubeType.THREEBYTHREE, AVG.Ao5);
        A12Tree = OW.getListAvg(CubeType.THREEBYTHREE, AVG.Ao12);
        A5Four = OW.getListAvg(CubeType.FOURBYFOUR, AVG.Ao5);
        A12Four = OW.getListAvg(CubeType.FOURBYFOUR, AVG.Ao12); //taking lists of averages from outside
        initializeHistory(A5Two, TwoByTwo, 5);
        initializeHistory(A12Two, TwoByTwo, 12);
        initializeHistory(A5Tree, TreeByTree, 5);
        initializeHistory(A12Tree, TreeByTree, 12);
        initializeHistory(A5Four, FourByFour, 5);
        initializeHistory(A12Four, FourByFour, 12); // initialize those lists with data from db
    }
    /* private void initializeHistory(ObservableList<AVGwrapper> ToFill, ObservableList<Solve> source, int k){
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
     } first version */

    private void initializeHistory(ObservableList<AVGwrapper> ToFill, ObservableList<Solve> source, int k) {
        int helper;
        int DNFcounter;
        long value;
        int i;
        if (source.size() < k) {
            for (int j = 0; j < source.size(); j++) {
                ToFill.add(new AVGwrapper(j + 1, new Timestamp(0), false));
                ToFill.get(ToFill.size() - 1).setNET();
            }
        } else {
            for (int j = k - 1; j > 0; j--) {
                ToFill.add(new AVGwrapper(j + 1, new Timestamp(0), false));
                ToFill.get(ToFill.size() - 1).setNET();
            }
            for (i = k - 1; i < source.size(); i++) {
                helper = k;
                value = 0;
                DNFcounter = 0;
                for (int j = i; helper > 0; j--, helper--) {
                    if (source.get(j).getState() == State.DNF) {
                        DNFcounter++;
                    } else value += source.get(j).getTime().getTime();
                }
                value = value / k;
                if (DNFcounter < 2)
                    ToFill.add(new AVGwrapper(i + 1, new Timestamp(value), false));
                else ToFill.add(new AVGwrapper(i + 1, new Timestamp(value), true));
            }
        }
    }

    @Override
    public Timestamp GiveMeAverage(int WhatAverage, CubeType WhatModel) {
        Timestamp average = null;
        boolean somethingWrong = false;
        ObservableList<AVGwrapper> temp;
        if (WhatAverage == 5 && WhatModel == CubeType.TWOBYTWO) temp = A5Two;
        else if (WhatAverage == 12 && WhatModel == CubeType.TWOBYTWO) temp = A12Two;
        else if (WhatAverage == 5 && WhatModel == CubeType.THREEBYTHREE) temp = A5Tree;
        else if (WhatAverage == 12 && WhatModel == CubeType.THREEBYTHREE) temp = A12Tree;
        else if (WhatAverage == 5 && WhatModel == CubeType.FOURBYFOUR) temp = A5Four;
        else temp = A12Four;
        ObservableList<Solve> source;
        if (WhatModel == CubeType.TWOBYTWO) {
            source = TwoByTwo;
        } else if (WhatModel == CubeType.THREEBYTHREE) {
            source = TreeByTree;
        } else {
            source = FourByFour;
        }

        try {
            average = CreateAverage(WhatAverage, WhatModel);
        } catch (NotEnoughTimes notEnoughTimes) {
            temp.add(new AVGwrapper(source.size(), new Timestamp(0), false));
            temp.get(temp.size() - 1).setNET();
            somethingWrong = true;
        } catch (DNF dnf) {
            temp.add(new AVGwrapper(source.size(), new Timestamp(0), true));
            somethingWrong = true;
        }
        if (!somethingWrong)
            temp.add(new AVGwrapper(source.size(), average, false));
        return average;
    }

    private Timestamp CreateAverage(int WhatAverage, CubeType WhatModel) throws DNF, NotEnoughTimes {
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
            temp = TwoByTwo;
        } else if (WhatModel == CubeType.THREEBYTHREE) {
            temp = TreeByTree;
        } else {
            temp = FourByFour;
        }
        ArrayList<Timestamp> times = new ArrayList<>();
        for (Solve a : temp) {
            if (a.getState() == State.DNF || a.getState() == State.REJ) continue;
            times.add(a.getTime());
        }
        return times;
    }

    @Override
    public Timestamp GiveMeMax(CubeType WhatModel) {
        ObservableList<Solve> temp;
        Timestamp max = new Timestamp(0);
        if (WhatModel == CubeType.TWOBYTWO) {
            temp = TwoByTwo;
        } else if (WhatModel == CubeType.THREEBYTHREE) {
            temp = TreeByTree;
        } else {
            temp = FourByFour;
        }
        for (Solve a : temp) {
            if (a.getTime().getTime() > max.getTime() && a.getState()!=State.DNF) // unfortunate
                max = a.getTime();
        }
        return max;
    }

    @Override
    public Timestamp GiveMeMin(CubeType WhatModel) {
        ObservableList<Solve> temp;
        Timestamp min = new Timestamp(999999999);
        if (WhatModel == CubeType.TWOBYTWO) {
            temp = TwoByTwo;
        } else if (WhatModel == CubeType.THREEBYTHREE) {
            temp = TreeByTree;
        } else {
            temp = FourByFour;
        }
        for (Solve a : temp) {
            if (a.getTime().getTime() < min.getTime() && a.getState()!=State.DNF) // unfortunate
                min = a.getTime();
        }
        return min;
    }

    @Override
    public AVGwrapper GiveMeMaxAVG(CubeType WhatModel, AVG whatAvg) {
        ObservableList<AVGwrapper> temp;
        AVGwrapper max = new AVGwrapper(-1,new Timestamp(0), false);
        if (WhatModel == CubeType.TWOBYTWO) {
            if (whatAvg == AVG.Ao5)
                temp = A5Two;
            else temp = A12Two;
        } else if (WhatModel == CubeType.THREEBYTHREE) {
            if (whatAvg == AVG.Ao5)
                temp = A5Tree;
            else temp = A12Tree;
        } else {
            if (whatAvg == AVG.Ao5)
                temp = A5Four;
            else temp = A12Four;
        }
        for (AVGwrapper a : temp) {
            if (a.getAVG().getTime() > max.getAVG().getTime() && !a.isDNF()) // unfortunate
                max = a;
        }
        return max;
    }


    @Override
    public AVGwrapper GiveMeMinAVG(CubeType WhatModel, AVG whatAvg) {
        ObservableList<AVGwrapper> temp;
        AVGwrapper min = new AVGwrapper(-1,new Timestamp(999999999), false);
        if (WhatModel == CubeType.TWOBYTWO) {
            if (whatAvg == AVG.Ao5)
                temp = A5Two;
            else temp = A12Two;
        } else if (WhatModel == CubeType.THREEBYTHREE) {
            if (whatAvg == AVG.Ao5)
                temp = A5Tree;
            else temp = A12Tree;
        } else {
            if (whatAvg == AVG.Ao5)
                temp = A5Four;
            else temp = A12Four;
        }
        for (AVGwrapper a : temp) {
            if (a.getAVG().getTime() < min.getAVG().getTime() && !a.isDNF()) // unfortunate
                min = a;
        }
        return min;
    }

    @Override
    public void ChangeStateLast(CubeType WhatModel, State state) {
        ObservableList<Solve> temp;
        if (WhatModel == CubeType.TWOBYTWO) {
            temp = TwoByTwo;
            if(temp.isEmpty()) return;
            if(state==State.TWOSECPENALTY){
                Solve solve=temp.get(temp.size()-1);
                solve.getTime().setTime(solve.getTime().getTime()+2000);
                solve.setState(State.TWOSECPENALTY);
                DeleteLast(WhatModel);
                insertSolve(solve);
            }else if(state==State.DNF) {
                Solve solve=temp.get(temp.size()-1);
                solve.setState(State.DNF);
                DeleteLast(WhatModel);
                insertSolve(solve);
            }else {
                Solve solve=temp.get(temp.size()-1);
                DeleteLast(WhatModel);
                if(solve.getState()==State.DNF){
                    solve.setState(State.CORRECT);
                    insertSolve(solve);
                } else if(solve.getState()==State.TWOSECPENALTY){
                    solve.setState(State.CORRECT);
                    solve.getTime().setTime(solve.getTime().getTime()-2000);
                    insertSolve(solve);
                } else{
                    insertSolve(solve);
                }
            }
        } else if (WhatModel == CubeType.THREEBYTHREE) {
            temp = TreeByTree;
            if(temp.isEmpty()) return;
            if(state==State.TWOSECPENALTY){
                Solve solve=temp.get(temp.size()-1);
                solve.getTime().setTime(solve.getTime().getTime()+2000);
                solve.setState(State.TWOSECPENALTY);
                DeleteLast(WhatModel);
                insertSolve(solve);
            }else if(state==State.DNF) {
                Solve solve=temp.get(temp.size()-1);
                solve.setState(State.DNF);
                DeleteLast(WhatModel);
                insertSolve(solve);
            }else {
                Solve solve=temp.get(temp.size()-1);
                DeleteLast(WhatModel);
                if(solve.getState()==State.DNF){
                    solve.setState(State.CORRECT);
                    insertSolve(solve);
                } else if(solve.getState()==State.TWOSECPENALTY){
                    solve.setState(State.CORRECT);
                    solve.getTime().setTime(solve.getTime().getTime()-2000);
                    insertSolve(solve);
                }
                else {
                    insertSolve(solve);
                }
            }
        } else {
            temp = FourByFour;
            if(temp.isEmpty()) return;
            if(state==State.TWOSECPENALTY){
                Solve solve=temp.get(temp.size()-1);
                solve.getTime().setTime(solve.getTime().getTime()+2000);
                solve.setState(State.TWOSECPENALTY);
                DeleteLast(WhatModel);
                insertSolve(solve);
            }else if(state==State.DNF) {
                Solve solve=temp.get(temp.size()-1);
                solve.setState(State.DNF);
                DeleteLast(WhatModel);
                insertSolve(solve);
            }else {
                Solve solve=temp.get(temp.size()-1);
                DeleteLast(WhatModel);
                if(solve.getState()==State.DNF){
                    solve.setState(State.CORRECT);
                    insertSolve(solve);
                }
                else if(solve.getState()==State.TWOSECPENALTY){
                    solve.setState(State.CORRECT);
                    solve.getTime().setTime(solve.getTime().getTime()-2000);
                    insertSolve(solve);
                } else{
                    insertSolve(solve);
                }
            }
        }
        myDataBase.updateLast(temp.get(temp.size() - 1));
    }

    @Override
    public void DeleteLast(CubeType WhatModel) {
        ObservableList<Solve> temp;

        if (WhatModel == CubeType.TWOBYTWO) {
            temp = TwoByTwo;
            if(!temp.isEmpty()){
                temp.remove(temp.size()-1);
                A5Two.remove(A5Two.size()-1);
                A12Two.remove(A12Two.size()-1);
                myDataBase.deleteLast(WhatModel);
            }
        } else if (WhatModel == CubeType.THREEBYTHREE) {
            temp = TreeByTree;
            if(!temp.isEmpty()) {
                temp.remove(temp.size()-1);
                A5Tree.remove(A5Tree.size() - 1);
                A12Tree.remove(A12Tree.size() - 1);
                myDataBase.deleteLast(WhatModel);
            }
        } else {
            temp = FourByFour;
            if(!temp.isEmpty()) {
                temp.remove(temp.size()-1);
                A5Four.remove(A5Four.size() - 1);
                A12Four.remove(A12Four.size() - 1);
                myDataBase.deleteLast(WhatModel);
            }
        }


    }

    @Override
    public void delete(CubeType type) {
        myDataBase.clearTable(type);
        if(type==CubeType.TWOBYTWO){
            TwoByTwo.clear();
            A5Two.clear();
            A12Two.clear();
        }else if(type==CubeType.THREEBYTHREE){
            TreeByTree.clear();
            A5Tree.clear();
            A12Tree.clear();
        }else{
            FourByFour.clear();
            A5Four.clear();
            A12Four.clear();
        }

    }

    @Override
    public void insertSolve(Solve solve){

        if (solve.getType() == CubeType.TWOBYTWO) {
            TwoByTwo.add(solve);
            GiveMeAverage(5,CubeType.TWOBYTWO);
            GiveMeAverage(12,CubeType.TWOBYTWO);
        } else if (solve.getType() == CubeType.THREEBYTHREE) {
            TreeByTree.add(solve);
            GiveMeAverage(5,CubeType.THREEBYTHREE);
            GiveMeAverage(12,CubeType.THREEBYTHREE);
        } else {
            FourByFour.add(solve);
            GiveMeAverage(5,CubeType.FOURBYFOUR);
            GiveMeAverage(12,CubeType.FOURBYFOUR);
        }
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

    public static class DNF extends Exception {
    }

    public static class NotEnoughTimes extends Exception {
    }
}
