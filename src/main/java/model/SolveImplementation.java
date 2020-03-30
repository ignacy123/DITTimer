package model;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SolveImplementation implements Solve {

  //  SimpleDateFormat ft;
    Date dateOfSolution;
    Time timeOfSolution;
    State whatState;
    CubeType whatCube;
    int ID;
    public SolveImplementation(){
        dateOfSolution=null;
        timeOfSolution=null;
        whatState=State.NONE;
        whatCube=CubeType.THREEBYTHREE;
    }
    public SolveImplementation(Date date,long time, State state,CubeType type){
        timeOfSolution=new Time(time);
        whatState=state;
        dateOfSolution=new Date();
        dateOfSolution=date;
        whatCube=type;
    }

    @Override
    public Time getTime() {
        return timeOfSolution;
    }

    @Override
    public State getState() {
        return whatState;
    }

    @Override
    public Date getDate() {
        return dateOfSolution;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public CubeType getType() {
        return whatCube;
    }

    @Override
    public void setTime(Time time) {
        this.timeOfSolution=time;
    }

    @Override
    public void setState(State state) {
        whatState=state;
    }

    @Override
    public void setDate(Date date) {
        this.dateOfSolution=date;
    }

    @Override
    public void setID(int id) {
        this.ID=id;
    }

    @Override
    public void setType(CubeType type) {
        this.whatCube=type;
    }


    @Override
    public void rejectSolution() {
        whatState=State.REJ;
    }
    @Override
    public void Penalty() {
    }
}
