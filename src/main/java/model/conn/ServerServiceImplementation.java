package model.conn;

import javafx.collections.ObservableList;
import model.enums.ClientRequestType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ServerServiceImplementation implements ServerService {
    ObjectInputStream inputStream;
    ObjectOutputStream outputStream;
    public void start(){
        try {
            Socket socket = new Socket("localhost", 8000);
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public ArrayList<Room> requestRooms() {
        ArrayList<Room> toReturn = null;
        try {
            outputStream.writeObject(new ClientRequest(ClientRequestType.REQUESTROOMS));
            toReturn = (ArrayList<Room>) inputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }
}
