package model.conn;

import model.enums.ClientRequestType;

import java.io.Serializable;

public class ClientRequest implements Serializable {
    private ClientRequestType type;
    ClientRequest(ClientRequestType type){
        this.type = type;
    }

    public ClientRequestType getType() {
        return type;
    }
}
