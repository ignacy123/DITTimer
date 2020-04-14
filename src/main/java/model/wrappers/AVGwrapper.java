package model.wrappers;

import java.sql.Timestamp;

public class AVGwrapper {

    boolean isDNF;
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
    }
}