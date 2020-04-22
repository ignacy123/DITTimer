package model.conn;

import model.enums.ClientRequestType;
import model.enums.CubeType;

import java.io.Serializable;
import java.sql.Time;

public class ClientRequest implements Serializable {
    private ClientRequestType type;
    private CubeType cubeType;
    private String userName;
    private Room room;
    private Time time;
    public ClientRequest(ClientRequestType type){
        this.type = type;
    }
    public ClientRequest(ClientRequestType type, Room room){
        this.type = type;
        this.room = room;
    }
    public ClientRequest(ClientRequestType type, Room room, Time time){
        this.type = type;
        this.room = room;
        this.time = time;
    }
    public ClientRequestType getType() {
        return type;
    }

    public CubeType getCubeType() {
        return cubeType;
    }

    public void setCubeType(CubeType cubeType) {
        this.cubeType = cubeType;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public Room getRoom() {
        return room;
    }
    public Time getTime() {
        return time;
    }
}