package model.conn;

import model.enums.ClientRequestType;
import model.enums.CubeType;
import model.logic.KeyFile;
import model.logic.Solve;

import java.io.File;
import java.io.Serializable;
import java.sql.Time;

public class ClientRequest implements Serializable {
    private ClientRequestType type;
    private File myfile;
    private KeyFile keyFile;
    private CubeType cubeType;
    private String userName;
    private Room room;
    private boolean isPrivate;
    private String password;
    private String msg;
    private Solve solve;
    public ClientRequest(ClientRequestType type){
        this.type = type;
    }
    public ClientRequest(ClientRequestType type, Room room){
        this.type = type;
        this.room = room;
    }
    public ClientRequest(File file){
        this.type = ClientRequestType.FILE;
        myfile=file;
    }
    public void setKey(KeyFile kf) {
        this.keyFile=kf;
    }
    public KeyFile getKey() {
        return this.keyFile;
    }

    public ClientRequestType getType() {
        return type;
    }
    public void setFile(File file) {
        this.myfile=file;
    }
    public File getFile(){
        return myfile;
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

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }
    public boolean getPrivate(){
        return isPrivate;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Solve getSolve() {
        return solve;
    }

    public void setSolve(Solve solve) {
        this.solve = solve;
    }
}
