package model.conn;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    public User(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
