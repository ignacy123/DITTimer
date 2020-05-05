package model.conn;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.logic.Solve;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RoomHolder {
    ConcurrentMap<Room, String> passwords;
    ConcurrentMap<Integer, Room> rooms;
    ConcurrentMap<Room, ArrayList<ObjectOutputStream>> streamHolder;
    private ConcurrentMap<User, ObjectOutputStream> users;
    int roomCounter;
    int id;
    private int scrambleCounter;
    public RoomHolder(){
        passwords = new ConcurrentHashMap<>();
        rooms = new ConcurrentHashMap<>();
        streamHolder = new ConcurrentHashMap<>();
        users=new ConcurrentHashMap<>();
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
            streamHolder.put(rooms.get(roomCounter), new ArrayList<>());
            user.setInRoom(true);
            user.setRoom(rooms.get(roomCounter));
            return user.getRoom();
        }
        return null;
    }
    public void removeUser(User user, Room room) {
        streamHolder.get(room).remove(users.get(user));
        room.removeUser(user);
        users.remove(user);
        user.setRoom(null);
    }
    public void removeRoom(Room room) {
        streamHolder.remove(room);
        //give new roomCounters
        ConcurrentMap<Integer, Room> newRooms = new ConcurrentHashMap<>();
        roomCounter=0;
        for(int i = 1; i <= rooms.size(); i++) {
            if(rooms.get(i)==room) continue;
            newRooms.put(++roomCounter, rooms.get(i));
        }
        rooms=newRooms;
        passwords.remove(room);
    }
    public void joinRoom(User user, Room room, ObjectOutputStream oos) {
        room.addUser(user);
        streamHolder.get(room).add(oos);
        users.put(user, oos);
        user.setRoom(room);
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
    public ArrayList<ObjectOutputStream> getStreams(Room room) {
        return streamHolder.get(room);
    }
    public Room getRoom(int id) {
        for(Room room: rooms.values()) {
            if(room.getID() == id) return room;
        }
        return null;
    }

    public boolean isRoomReady(Room room){
        int amountOfUsers = room.getUsers().size();
        int amountWithZero = 0;
        for(User user: room.getUsers()){
            if(room.getSolves().get(user).size()==0){
                amountWithZero++;
            }
        }
        if(amountOfUsers==amountWithZero){
            return false;
        }
        for(User user: room.getUsers()){
            if(!user.isReadyForNext()){
                return false;
            }
        }
        return true;
    }
    public void setPassword(Room room, String password){
        passwords.put(room, password);
    }
    public String getPassword(Room room){
        return passwords.get(room);
    }
}
