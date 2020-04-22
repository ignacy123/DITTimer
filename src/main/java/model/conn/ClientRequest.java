package model.conn;

import model.enums.ClientRequestType;
import model.enums.CubeType;

import java.io.Serializable;

public class ClientRequest implements Serializable {
    private ClientRequestType type;
    private CubeType cubeType;
    private String userName;
    ClientRequest(ClientRequestType type){
        this.type = type;
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
}
