package model.wrappers;

import java.sql.Timestamp;

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
    public boolean isNET(){
        return isNotEnoughTimes;
    }
    public void TwoSecPenalty(int k){
        long value=average.getTime();
        value=value*k;
        value+=2000;
        average.setTime(value/k);
    }
    public AVGwrapper(int i, Timestamp avg, boolean temp) {
        ID = i;
        average = avg;
        isDNF = temp;
        isNotEnoughTimes=false;
    }


}