package model.conn;


import model.SS.StatisticServer;
import model.enums.CubeType;
import model.logic.KeyFile;
import model.logic.Solve;
import view.MoreOptions.MoreOptionsController;
import view.test.RoomWindow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
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
    void sendFile() throws IOException;
    void sendKey() throws IOException;
    void setKey(KeyFile kf);
    void setFile(File file);
    void setMOC(MoreOptionsController mocController);
    //void setSS(StatisticServer ss);
    ObjectInputStream getInputStream();
}
