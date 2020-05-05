package model.conn;


import model.enums.CubeType;
import model.logic.Solve;
import view.test.RoomWindow;

import java.sql.Time;
import java.util.ArrayList;

public interface ServerService {
    void start();
    void close();
    ArrayList<Room> requestRooms();
    void createRoom(CubeType cubeType, String name, boolean isPrivate, String password);
    void joinRoom(Room room, String name, String password);
    void setWindow(RoomWindow wind);
    void getPlayers(Room room);
    void requestScramble(Room room);

    void getTimes(Room room);
    void sendTime(Room room, Solve solve);
    void sendChat(Room room, String msg);
    void leaveRoom(Room room);
}
