package model;

import java.sql.Time;

public class SolveImplementation implements Solve {
   // SimpleDateFormat ft;
  //  Date dateOfSolution; date to be added
    Time timeOfSolution;
    State whatState;
 //   String comment;

    public SolveImplementation(){
        timeOfSolution=null;
        whatState=State.NONE;
    }
    public SolveImplementation(long time, State s){
        timeOfSolution=new Time(time);
        whatState=s;
    }

    @Override
    public Time extractTime(SolveImplementation solution) throws NullPointerException{
        if(solution==null) throw new NullPointerException();
        return timeOfSolution;
    }

    @Override
    public State extractState(SolveImplementation solution) throws NullPointerException{
        if(solution==null) throw new NullPointerException();
        return whatState;
    }

    @Override
    public String AddComment() {
        return null;
    }

    @Override
    public void rejectSolution() {
        whatState=State.REJ;
    }

    @Override
    public void Penalty() {
    }
}
