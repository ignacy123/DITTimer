package model.conn;

import model.enums.CubeType;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;

public class Room implements Serializable {
    ArrayList<User> users = new ArrayList<>();
    ArrayList<Time> times = new ArrayList<>();

    User host;
    CubeType type;
    int id;
    Room(int id){
        this.id=id;
    }
    public void setHost(User user){
        host = user;
        users.add(host);
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

    public int getID() {
        return id;
    }
    public ArrayList<User> getUsers() {
        return users;
    }
    public ArrayList<Time> getTimes() {
        return times;
    }
    public void addTime(Time time) {
        times.add(time);
    }
}
