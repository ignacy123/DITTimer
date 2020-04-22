package model.conn;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RoomHolder {
    ConcurrentMap<Integer, Room> rooms;
    int roomCounter;
    int id;
    public RoomHolder(){
        rooms = new ConcurrentHashMap<>();
        roomCounter = 0;
        id=0;
    }
    public boolean hasFreeRoom(){
        if(roomCounter<=20){
            return true;
        }
        return false;
    }
    public Room requestRoom(User user){
        if(!user.isInRoom()){
            roomCounter++;
            rooms.put(roomCounter, new Room(id++));
            rooms.get(roomCounter).setHost(user);
            user.setInRoom(true);
            user.setRoom(rooms.get(roomCounter));
            return user.getRoom();
        }
        return null;
    }
    public ArrayList<Room> getAvailableRooms(){
        ArrayList<Room> toReturn = new ArrayList<>();
        for(Map.Entry entry: rooms.entrySet()){
            if(((Room)entry.getValue()).isJoinable()){
                toReturn.add((Room) entry.getValue());
            }
        }
        return toReturn;
    }
    public Room getRoom(int id) {
        for(Room room: rooms.values()) {
            if(room.getID() == id) return room;
        }
        return null;
    }
}
