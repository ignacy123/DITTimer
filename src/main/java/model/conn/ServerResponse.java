package model.conn;

import model.enums.ServerResponseType;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;

public class ServerResponse implements Serializable {
    private ServerResponseType type;
    private ArrayList<Room> rooms;
    private Room room;
    private ArrayList<Time> times;
    private ArrayList<User> users;

    public ServerResponse(ServerResponseType type){
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

    public void setTimes(ArrayList<Time> times) {
        this.times=times;
    }
    public ArrayList<Time> getTimes() {
        return times;
    }
    public void setUsers(ArrayList<User> users) {
        this.users=users;
    }
    public ArrayList<User> getUsers() {
        return users;
    }
}

