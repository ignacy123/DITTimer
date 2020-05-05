package model.conn;

import model.enums.CubeType;
import model.logic.Solve;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Room implements Serializable {
    ArrayList<User> users = new ArrayList<>();
    ConcurrentHashMap<User, ArrayList<Solve>> solves = new ConcurrentHashMap<>();
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
        solves.put(user, new ArrayList<>());
        user.setInRoom(true);
    }
    public void removeUser(User user) {
        users.remove(user);
        solves.remove(user);
        user.setInRoom(false);
    }
    public User getHost() {
        return host;
    }

    public boolean isJoinable(){
        if(users.size()>=5){
            return false;
        }
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
    public ConcurrentHashMap<User, ArrayList<Solve>> getSolves() {
        return solves;
    }
    public void addSolve(User user, Solve solve) {
        solves.get(user).add(solve);
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }
    public boolean isPrivate() {
        return isPrivate;
    }
}
