package model.conn;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import model.enums.ClientRequestType;
import model.enums.CubeType;
import model.enums.ServerResponseType;
import view.test.Client;

import java.io.EOFException;
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

    @Override
    public void createRoom(CubeType type, String name) {
        try {
            ClientRequest cr = new ClientRequest(ClientRequestType.CREATEROOM);
            cr.setCubeType(type);
            if(name.equals("")){
                name = "nobody";
            }
            cr.setUserName(name);
            outputStream.writeObject(cr);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                    switch(sr.getType()){
                        case SENDINGROOMS:
                            Platform.runLater(() -> {
                                client.sendRooms(sr.getRooms());
                            });
                            break;
                        case ROOMHASBEENCREATED:
                            Platform.runLater(() -> {
                                client.roomHasBeenCreated(sr.getRoom());
                            });
                    }
                } catch (EOFException e) {

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
