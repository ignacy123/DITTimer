package model.conn;

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
    private int scrambleCounter;
    private int getFreeId() {
        for(int i = 0; i < roomCounter; i++) {
            if(!rooms.containsKey(i)) return i;
        }
        return roomCounter+1;
    }
    public RoomHolder(){
        passwords = new ConcurrentHashMap<>();
        rooms = new ConcurrentHashMap<>();
        streamHolder = new ConcurrentHashMap<>();
        users=new ConcurrentHashMap<>();
        roomCounter = 0;
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
            int id = getFreeId();
            rooms.put(id, new Room(id));
            rooms.get(id).setHost(user);
            streamHolder.put(rooms.get(id), new ArrayList<>());
            user.setInRoom(true);
            user.setRoom(rooms.get(id));
            return user.getRoom();
        }
        return null;
    }
    public void removeUser(User user, Room room) {
        streamHolder.get(room).remove(users.get(user));
        room.removeUser(user);
        users.remove(user);
        user.setRoom(null);
        user.setInRoom(false);
    }
    public void removeRoom(Room room) {
        streamHolder.remove(room);
        roomCounter--;
        rooms.remove(room.getID());
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
