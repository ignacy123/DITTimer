package model.conn;


import model.enums.CubeType;
import view.test.RoomWindow;

import java.sql.Time;
import java.util.ArrayList;

public interface ServerService {
    public void start();
    public ArrayList<Room> requestRooms();
    public void createRoom(CubeType cubeType, String name);
    public void joinRoom(Room room, String name);
    public void setWindow(RoomWindow wind);
    void getPlayers(Room room);

    void getTimes(Room room);
    void sendTime(Room room, Time time);
}
