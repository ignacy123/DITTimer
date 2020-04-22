package model.conn;


import java.util.ArrayList;

public interface ServerService {
    public void start();
    public ArrayList<Room> requestRooms();
}
