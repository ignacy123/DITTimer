package model.conn;

import model.enums.CubeType;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Room implements Serializable {
    ArrayList<User> users = new ArrayList<>();
    ConcurrentHashMap<User, ArrayList<Time>> times = new ConcurrentHashMap<>();
    User host;
    CubeType type;
    int id;
    boolean isPrivate;
    Room(int id){
        this.id=id;
    }
    public void setHost(User user){
        host = user;
    }
    public void addUser(User user) {
        users.add(user);
        times.put(user, new ArrayList<>());
        user.setInRoom(true);
    }
    public void removeUser(User user) {
        users.remove(user);
        times.remove(user);
        user.setInRoom(false);
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
    public ConcurrentHashMap<User, ArrayList<Time>> getTimes() {
        return times;
    }
    public void addTime(User user, Time time) {
        times.get(user).add(time);
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }
    public boolean isPrivate() {
        return isPrivate;
    }
}
