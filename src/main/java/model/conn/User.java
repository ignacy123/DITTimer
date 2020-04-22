package model.conn;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private boolean inRoom = false;
    private Room room;
    private String name;
    public User(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setInRoom(boolean inRoom) {
        this.inRoom = inRoom;
    }
    public boolean isInRoom(){
        return inRoom;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
