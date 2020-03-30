package model;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Solve implements SolveInterface {
   // SimpleDateFormat ft;
  //  Date dateOfSolution; date to be added
    Time timeOfSolution;
    State whatState;
 //   String comment;

    public Solve(){
        timeOfSolution=null;
        whatState=State.NONE;
    }
    public Solve(long time,State s){
        timeOfSolution=new Time(time);
        whatState=s;
    }

    @Override
    public Time extraxtTime(Solve solution) throws NullPointerException{
        if(solution==null) throw new NullPointerException();
        return timeOfSolution;
    }

    @Override
    public State extractState(Solve solution) throws NullPointerException{
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
