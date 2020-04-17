package model.wrappers;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class AVGwrapper {

    boolean isDNF;
    boolean isNotEnoughTimes;
    int ID;
    Timestamp average;

    public Timestamp getAVG(){
        return average;
    }
    public boolean isDNF(){
        return isDNF;
    }
    public void setDNF(){
        isDNF=true;
    }

    public int getID(){
        return ID;
    }
    public void setNET(){
        isNotEnoughTimes=true;
    }
    public boolean getState(){
        return isDNF;
    }
    public boolean isNET(){
        return isNotEnoughTimes;
    }
    public void TwoSecPenalty(int k){
        System.out.println(this.getAVG());
        long value=average.getTime();
        value=value*k;
        value+=2000;
        average.setTime(value/k);
        System.out.println(this.getAVG());
    }
    public AVGwrapper(int i, Timestamp avg, boolean temp) {
        ID = i;
        average = avg;
        isDNF = temp;
        isNotEnoughTimes=false;
    }
    public String toString(){
        String toStr = "";
        toStr+="Time: "+ this.getAVG().toLocalDateTime().format(DateTimeFormatter.ofPattern("mm:ss.SSS"));
        toStr+="\nState: "+this.getState();
        return toStr;
    }

}