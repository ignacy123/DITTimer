package model.SS;

import javafx.collections.ObservableList;
import model.db.DatabaseServiceImplementation;
import model.enums.AVG;
import model.enums.CubeType;
import model.enums.State;
import model.logic.Solve;
import model.wrappers.AVGwrapper;
import model.wrappers.ObservableWrapper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class StatisticServerImplementation implements StatisticServer {
    DatabaseServiceImplementation myDataBase;

    ObservableList<Solve> TwoByTwo;
    ObservableList<Solve> TreeByTree;
    ObservableList<Solve> FourByFour;

    ObservableList<AVGwrapper> A5Two;
    ObservableList<AVGwrapper> A12Two;
    ObservableList<AVGwrapper> A50Two;
    ObservableList<AVGwrapper> A100Two;

    ObservableList<AVGwrapper> A5Tree;
    ObservableList<AVGwrapper> A12Tree;
    ObservableList<AVGwrapper> A50Tree;
    ObservableList<AVGwrapper> A100Tree;

    ObservableList<AVGwrapper> A5Four;
    ObservableList<AVGwrapper> A12Four; // history
    ObservableList<AVGwrapper> A50Four;
    ObservableList<AVGwrapper> A100Four;

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
        A50Two = OW.getListAvg(CubeType.TWOBYTWO, AVG.Ao50);
        A100Two = OW.getListAvg(CubeType.TWOBYTWO, AVG.Ao100);

        A5Tree = OW.getListAvg(CubeType.THREEBYTHREE, AVG.Ao5);
        A12Tree = OW.getListAvg(CubeType.THREEBYTHREE, AVG.Ao12);
        A50Tree = OW.getListAvg(CubeType.THREEBYTHREE, AVG.Ao50);
        A100Tree = OW.getListAvg(CubeType.THREEBYTHREE, AVG.Ao100);

        A5Four = OW.getListAvg(CubeType.FOURBYFOUR, AVG.Ao5);
        A12Four = OW.getListAvg(CubeType.FOURBYFOUR, AVG.Ao12); //taking lists of averages from outside
        A50Four = OW.getListAvg(CubeType.FOURBYFOUR, AVG.Ao50);
        A100Four = OW.getListAvg(CubeType.FOURBYFOUR, AVG.Ao100);

        initializeHistory(A5Two, TwoByTwo, 5);
        initializeHistory(A12Two, TwoByTwo, 12);
        initializeHistory(A50Two, TwoByTwo, 50);
        initializeHistory(A100Two, TwoByTwo, 100);

        initializeHistory(A5Tree, TreeByTree, 5);
        initializeHistory(A12Tree, TreeByTree, 12);
        initializeHistory(A50Tree, TreeByTree, 50);
        initializeHistory(A100Tree, TreeByTree, 100);

        initializeHistory(A5Four, FourByFour, 5);
        initializeHistory(A12Four, FourByFour, 12); // initialize those lists with data from db
        initializeHistory(A50Four, FourByFour, 50);
        initializeHistory(A100Four, FourByFour, 100);
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
    private ObservableList<AVGwrapper> returnAVGlist(AVG WhatAverage, CubeType WhatModel){
        ObservableList<AVGwrapper> temp;
        if (WhatModel == CubeType.TWOBYTWO) {
            if (WhatAverage == AVG.Ao5)
                temp = A5Two;
            else if(WhatAverage == AVG.Ao12)
                temp= A12Two;
            else  if(WhatAverage == AVG.Ao50)
                temp=A50Two;
            else temp=A100Two;

        } else if (WhatModel == CubeType.THREEBYTHREE) {
            if (WhatAverage == AVG.Ao5)
                temp = A5Tree;
            else if(WhatAverage == AVG.Ao12)
                temp= A12Tree;
            else  if(WhatAverage == AVG.Ao50)
                temp=A50Tree;
            else temp=A100Tree;
        } else {
            if (WhatAverage == AVG.Ao5)
                temp = A5Four;
            else if(WhatAverage == AVG.Ao12)
                temp= A12Four;
            else  if(WhatAverage == AVG.Ao50)
                temp=A50Four;
            else temp=A100Four;
        }
        return temp;
    }
    private ObservableList<Solve> returnSOLVElist(CubeType WhatModel){
        if (WhatModel == CubeType.TWOBYTWO) {
            return TwoByTwo;
        } else if (WhatModel == CubeType.THREEBYTHREE) {
            return TreeByTree;
        } else {
          return FourByFour;
        }
    }
    @Override
    public Timestamp GiveMeAverage(AVG WhatAverage, CubeType WhatModel) {
        Timestamp average = null;
        boolean somethingWrong = false;
        ObservableList<AVGwrapper> temp=returnAVGlist(WhatAverage,WhatModel);
        ObservableList<Solve> source=returnSOLVElist(WhatModel);

        try {
            average = CreateAverage(WhatAverage, WhatModel);
        } catch (NotEnoughTimes notEnoughTimes) {
            temp.add(new AVGwrapper(-1, new Timestamp(0), false));
            temp.get(temp.size() - 1).setNET();
            somethingWrong = true;
        } catch (DNF dnf) {
            temp.add(new AVGwrapper(-1, new Timestamp(0), true));
            somethingWrong = true;
        }
        if (!somethingWrong){
            System.out.println("nothing");
            temp.add(new AVGwrapper(source.size(), average, false));
            System.out.println(temp.get(temp.size()-1));
        }

        return average;
    }
    private int giveMeSize(AVG WhatAverage){
        if(WhatAverage.equals(AVG.Ao5)) return 5;
        else if(WhatAverage.equals(AVG.Ao12)){
            return 12;
        }
        else if(WhatAverage.equals(AVG.Ao50)){
            return 50;
        }
        else return 100;
    }
    @Override
    public Timestamp CreateAverage(AVG WhatAverage, CubeType WhatModel) throws DNF, NotEnoughTimes {
        ObservableList<Solve> temp=returnSOLVElist(WhatModel);
        int IntAVG=giveMeSize(WhatAverage);;
        long best = 999999999;
        long worst=0;
        if (temp.size() < IntAVG) {
            System.out.println(IntAVG);
            throw new NotEnoughTimes();
        }
        else {
            int helper = IntAVG;
            long  value = 0;
            int amountOfDNF = 0;
            for (int i = temp.size() - 1; helper > 0; i--, helper--) {
                if (temp.get(i).getState() == State.DNF) {
                    amountOfDNF++;
                    if (amountOfDNF == 2) break;
                } else {
                    value += temp.get(i).getTime().getTime(); // unfortunate
                   // if(temp.get(i).getTime().getTime()<best) best=temp.get(i).getTime().getTime();
                    //if(temp.get(i).getTime().getTime()>worst) worst=temp.get(i).getTime().getTime();
                }
            }
            if (amountOfDNF >= 2) throw new DNF();
            else {
                //value=value-best-worst;
                return new Timestamp(value / (IntAVG));
            }
        }
    }

    @Override
    public ArrayList<Timestamp> GiveMeTimes(CubeType WhatModel) {
        ObservableList<Solve> temp=returnSOLVElist(WhatModel);
        ArrayList<Timestamp> times = new ArrayList<>();
        for (Solve a : temp) {
            if (a.getState() == State.DNF || a.getState() == State.REJ) continue;
            times.add(a.getTime());
        }
        return times;
    }

    @Override
    public Timestamp GiveMeMax(CubeType WhatModel) {
        ObservableList<Solve> temp=returnSOLVElist(WhatModel);
        Timestamp max = new Timestamp(0);
        for (Solve a : temp) {
            if (a.getTime().getTime() > max.getTime() && a.getState()!=State.DNF) // unfortunate
                max = a.getTime();
        }
        return max;
    }

    @Override
    public Timestamp GiveMeMin(CubeType WhatModel) {
        ObservableList<Solve> temp=returnSOLVElist(WhatModel);
        Timestamp min = new Timestamp(999999999);
        for (Solve a : temp) {
            if (a.getTime().getTime() < min.getTime() && a.getState()!=State.DNF) // unfortunate
                min = a.getTime();
        }
        return min;
    }

    @Override
    public AVGwrapper GiveMeMaxAVG(CubeType WhatModel, AVG whatAvg) {
        ObservableList<AVGwrapper> temp=returnAVGlist(whatAvg,WhatModel);
        AVGwrapper max = new AVGwrapper(-1,new Timestamp(0), false);
        for (AVGwrapper a : temp) {
            if (a.getAVG().getTime() > max.getAVG().getTime() && !a.isDNF()) // unfortunate
                max = a;
        }
        return max;
    }


    @Override
    public AVGwrapper GiveMeMinAVG(CubeType WhatModel, AVG whatAvg) {
        ObservableList<AVGwrapper> temp=returnAVGlist(whatAvg,WhatModel);
        AVGwrapper min = new AVGwrapper(-1,new Timestamp(999999999), false);
        for (AVGwrapper a : temp) {
            if (a.getAVG().getTime() < min.getAVG().getTime() && !a.isDNF() && !a.isNET()) // unfortunate
                min = a;
        }
        return min;
    }

    @Override
    public void ChangeStateLast(CubeType WhatModel, State state) {
        ObservableList<Solve> temp=returnSOLVElist(WhatModel);
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
                A50Two.remove(A50Two.size()-1);
                A100Two.remove(A100Two.size()-1);
                myDataBase.deleteLast(WhatModel);
            }
        } else if (WhatModel == CubeType.THREEBYTHREE) {
            temp = TreeByTree;
            if(!temp.isEmpty()) {
                temp.remove(temp.size()-1);
                A5Tree.remove(A5Tree.size() - 1);
                A12Tree.remove(A12Tree.size() - 1);
                A50Tree.remove(A50Tree.size()-1);
                A100Tree.remove(A100Tree.size()-1);
                myDataBase.deleteLast(WhatModel);
            }
        } else {
            temp = FourByFour;
            if(!temp.isEmpty()) {
                temp.remove(temp.size()-1);
                A5Four.remove(A5Four.size() - 1);
                A12Four.remove(A12Four.size() - 1);
                A50Four.remove(A50Four.size()-1);
                A100Four.remove(A100Four.size()-1);
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
            A50Two.clear();
            A100Two.clear();
        }else if(type==CubeType.THREEBYTHREE){
            TreeByTree.clear();
            A5Tree.clear();
            A12Tree.clear();
            A50Tree.clear();
            A100Tree.clear();
        }else{
            FourByFour.clear();
            A5Four.clear();
            A12Four.clear();
            A50Four.clear();
            A100Four.clear();
        }

    }

    @Override
    public void addComment(CubeType WhatModel, String string) {
        if (WhatModel == CubeType.TWOBYTWO) {
           TwoByTwo.get(TwoByTwo.size()-1).setComment(string);
            myDataBase.updateLast(TwoByTwo.get(TwoByTwo.size()-1));
        } else if (WhatModel == CubeType.THREEBYTHREE) {
            TreeByTree.get(TreeByTree.size()-1).setComment(string);
            myDataBase.updateLast(TreeByTree.get(TreeByTree.size()-1));
        } else {
            FourByFour.get(FourByFour.size()-1).setComment(string);
            myDataBase.updateLast(FourByFour.get(FourByFour.size()-1));
        }

    }

    @Override
    public void insertSolve(Solve solve){

        if (solve.getType() == CubeType.TWOBYTWO) {
            TwoByTwo.add(solve);
            GiveMeAverage(AVG.Ao5,CubeType.TWOBYTWO);
            GiveMeAverage(AVG.Ao12,CubeType.TWOBYTWO);
            GiveMeAverage(AVG.Ao50,CubeType.TWOBYTWO);
            GiveMeAverage(AVG.Ao100,CubeType.TWOBYTWO);
        } else if (solve.getType() == CubeType.THREEBYTHREE) {
            TreeByTree.add(solve);
            GiveMeAverage(AVG.Ao5,CubeType.THREEBYTHREE);
            GiveMeAverage(AVG.Ao12,CubeType.THREEBYTHREE);
            GiveMeAverage(AVG.Ao50,CubeType.THREEBYTHREE);
            GiveMeAverage(AVG.Ao100,CubeType.THREEBYTHREE);
        } else {
            FourByFour.add(solve);
            GiveMeAverage(AVG.Ao5,CubeType.FOURBYFOUR);
            GiveMeAverage(AVG.Ao12,CubeType.FOURBYFOUR);
            GiveMeAverage(AVG.Ao50,CubeType.FOURBYFOUR);
            GiveMeAverage(AVG.Ao100,CubeType.FOURBYFOUR);
        }
        myDataBase.insert(solve);
    }

    public static class DNF extends Exception {
    }

    public static class NotEnoughTimes extends Exception {
    }
}
