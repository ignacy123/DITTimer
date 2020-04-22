package model.conn;

import javafx.collections.ObservableList;
import model.enums.ClientRequestType;
import model.enums.ServerResponseType;
import view.test.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ServerServiceImplementation implements ServerService {
    ObjectInputStream inputStream;
    ObjectOutputStream outputStream;
    private Client client;
    
    public ServerServiceImplementation(Client client){
        this.client = client;
    }
    public void start(){
        try {
            Socket socket = new Socket("localhost", 8000);
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            new ServerResponseHandler(inputStream, client).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public ArrayList<Room> requestRooms() {
        ArrayList<Room> toReturn = null;
        try {
            outputStream.writeObject(new ClientRequest(ClientRequestType.REQUESTROOMS));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }
    
    static class ServerResponseHandler extends Thread{
        ObjectInputStream inputStream;
        Client client;
        ServerResponseHandler(ObjectInputStream inputStream, Client client){
            this.inputStream = inputStream;
            this.client = client;
        }

        @Override
        public void run() {
            while(true){
                try {
                    ServerResponse sr = (ServerResponse) inputStream.readObject();
                    if(sr==null){
                        continue;
                    }
                    if(sr.getType()== ServerResponseType.SENDINGROOMS){
                        client.sendRooms(sr.getRooms());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
