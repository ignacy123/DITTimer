package model.conn;

import model.enums.CubeType;

import java.io.Serializable;
import java.util.ArrayList;

public class Room implements Serializable {
    ArrayList<User> users;
    User host;
    CubeType type;

    Room(){

    }
    public void setHost(User user){
        host = user;
    }

    public User getHost() {
        return host;
    }

    public boolean isJoinable(){
        return true;
    }

    public void setType(CubeType type) {
        this.type = type;
    }

    public CubeType getType() {
        return type;
    }
}
