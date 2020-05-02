package model.conn;

import model.enums.ServerResponseType;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ServerResponse implements Serializable {
    private ServerResponseType type;
    private ArrayList<Room> rooms;
    private Room room;
    private ConcurrentHashMap<User, ArrayList<Time>> times;
    private ArrayList<User> users;
    private String msg;

    public ServerResponse(ServerResponseType type) {
        this.type = type;
    }

    public ServerResponseType getType() {
        return type;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setTimes(ConcurrentHashMap<User, ArrayList<Time>> times) {
        this.times = times;
    }

    public ConcurrentHashMap<User, ArrayList<Time>> getTimes() {
        return times;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}

