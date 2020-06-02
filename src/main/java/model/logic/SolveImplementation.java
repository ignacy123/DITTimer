package model.logic;

import model.enums.CubeType;
import model.enums.State;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class SolveImplementation implements Solve, Serializable {
    Date dateOfSolution;
    Timestamp timeOfSolution;
    State whatState;
    CubeType whatCube;
    String comment;
    String scramble;
    int ID;
    public SolveImplementation(){
        dateOfSolution=new Date();
        whatState=State.NONE;
        whatCube=CubeType.THREEBYTHREE;
        scramble="none";
    }
    public SolveImplementation(Date date,long time, State state,CubeType type){
        timeOfSolution=new Timestamp(time);
        whatState=state;
        dateOfSolution=new Date();
        dateOfSolution=date;
        whatCube=type;
    }
    @Override
    public String toString(){
        String toStr = "";
        toStr+="Time: "+ this.getTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("mm:ss.SSS"));
        toStr+="\nScramble: "+this.getScramble();
        toStr+="\nDate: "+this.getDate();
        toStr+="\nComment: "+this.getComment();
        toStr+="\nState: "+this.getState();
        return toStr;
    }
    @Override
    public Timestamp getTime() {
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
    public String getScramble() {
        return scramble;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void setTime(Timestamp time) {
        this.timeOfSolution=time;
    }

    @Override
    public void setState(State state) {
        whatState=state;
        if(state==State.TWOSECPENALTY)
            timeOfSolution.setTime(timeOfSolution.getTime()+2000);
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
    public void setScramble(String scr) {
        scramble=scr;
    }

    @Override
    public void setComment(String com) {
        comment=com;
    }


    @Override
    public void rejectSolution() {
        whatState=State.REJ;
    }
}
