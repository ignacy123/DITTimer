package model.conn;

import java.io.Serializable;
import java.util.ArrayList;

public class Room implements Serializable {
    ArrayList<User> users;
    User host;
    private int id;
    Room(int id){
        this.id = id;
    }
    public void setHost(User user){
        host = user;
    }
    public boolean isJoinable(){
        return true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
