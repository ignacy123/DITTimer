package model.conn;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import model.enums.ClientRequestType;
import model.enums.CubeType;
import model.enums.ServerResponseType;
import view.test.Client;
import view.test.RoomWindow;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Time;
import java.util.ArrayList;

public class ServerServiceImplementation implements ServerService {
    ObjectInputStream inputStream;
    ObjectOutputStream outputStream;
    private Client client;
    private ServerResponseHandler responseHandler;
    public ServerServiceImplementation(Client client){
        this.client = client;
    }
    public void start(){
        try {
            Socket socket = new Socket("localhost", 8000);
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            responseHandler = new ServerResponseHandler(inputStream, client);
            responseHandler.start();
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
    public void createRoom(CubeType type, String name, boolean isPrivate, String password) {
        try {
            ClientRequest cr = new ClientRequest(ClientRequestType.CREATEROOM);
            cr.setCubeType(type);
            if(name.equals("")){
                name = "nobody";
            }
            cr.setUserName(name);
            cr.setPrivate(isPrivate);
            cr.setPassword(password);
            outputStream.writeObject(cr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void joinRoom(Room room, String name, String password) {
        ClientRequest cr = new ClientRequest(ClientRequestType.JOINROOM, room);
        System.out.println("sending"+name);
        if(name.equals("")){
            name = "nobody";
        }
        cr.setUserName(name);
        cr.setPassword(password);
        try {
            outputStream.writeObject(cr);
            System.out.println("writing join ");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setWindow(RoomWindow wind) {
        responseHandler.setWindow(wind);
    }

    @Override
    public void getPlayers(Room room) {
        try {
            outputStream.writeObject(new ClientRequest(ClientRequestType.GETUSERS, room));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void getTimes(Room room) {
        try {
            outputStream.writeObject(new ClientRequest(ClientRequestType.GETTIMES, room));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendTime(Room room, Time time) {
        try {
            outputStream.writeObject(new ClientRequest(ClientRequestType.SENDTIME, room, time));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ServerResponseHandler extends Thread{
        private RoomWindow window;
        ObjectInputStream inputStream;
        Client client;
        ServerResponseHandler(ObjectInputStream inputStream, Client client){
            this.inputStream = inputStream;
            this.client = client;
        }
        public void setWindow(RoomWindow wind) {
            this.window = wind;
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
                            break;
                        case USERSCHANGED:
                            if(window == null) break;
                            System.out.println("user upd");
                            Platform.runLater(()-> {
                                window.renderUsers(sr.getUsers());
                            });
                            break;
                        case TIMESCHANGED:
                            if(window == null) break;
                            System.out.println("TImes upd");
                            Platform.runLater(()-> {
                                window.renderTimes(sr.getTimes());
                            });
                            break;
                        case ROOMJOINED:
                            //if(window == null) break;
                            client.roomAccessHasBeenGranted(sr.getRoom());
                            //Platform.runLater(()-> {
                            //    window.renderTimes(sr.getTimes());
                            //});
                            //Platform.runLater(()-> {
                             //   window.renderUsers(sr.getUsers());
                            //});
                            break;
                        case WRONGPASSWORD:
                            Platform.runLater(()-> {
                                client.wrongPassword();
                            });
                            break;
                        case ROOMFULL:
                            Platform.runLater(()-> {
                                client.roomFull();
                            });
                            break;

                    }
                } catch (EOFException e) {

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
