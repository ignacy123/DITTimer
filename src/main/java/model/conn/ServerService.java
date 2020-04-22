package model.conn;


import model.enums.CubeType;

import java.util.ArrayList;

public interface ServerService {
    public void start();
    public ArrayList<Room> requestRooms();
    public void createRoom(CubeType cubeType, String name);
}
