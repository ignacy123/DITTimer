package model.logic;

import java.io.Serializable;


public class ArraySerializable implements Serializable {
    byte[] myArray;
    public ArraySerializable(byte[] array){
        this.myArray=array;
    }
    public byte[] getArray(){
        return myArray;
    }
}
