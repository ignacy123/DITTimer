package model.logic;

import java.io.Serializable;

public class KeyFile implements Serializable {
    String myKey;
    public KeyFile(String key){
        myKey=key;
    }
    public String getKey(){
        return myKey;
    }
}
