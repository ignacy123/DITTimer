package model.conn;

import model.enums.ServerResponseType;

import java.io.Serializable;
import java.util.ArrayList;

public class ServerResponse implements Serializable {
    private ServerResponseType type;
    private ArrayList<Room> rooms;
    private Room room;
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
}
