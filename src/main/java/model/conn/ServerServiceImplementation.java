package model.conn;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.SS.StatisticServer;
import model.enums.ClientRequestType;
import model.enums.CubeType;
import model.logic.KeyFile;
import model.logic.Solve;
import view.MoreOptions.MoreOptionsController;
import view.start.Client;
import view.start.RoomWindow;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class ServerServiceImplementation implements ServerService {
    ObjectInputStream inputStream;
    ObjectOutputStream outputStream;
    private Client client;
    private File myFile;
    private StatisticServer ss;
    private KeyFile keyFile;
    private ServerResponseHandler responseHandler;
    private MoreOptionsController MOCcontroller;
    Socket socket;
    Random random;

    FileInputStream fis = null;
    BufferedInputStream bis = null;

    public ServerServiceImplementation(Client client, StatisticServer ss) {
        this.client = client;
        random = new Random();
        this.ss = ss;
    }

    public ServerServiceImplementation(StatisticServer ss) {
        this.ss = ss;
    }

    public void start() {
        try {
//            socket = new Socket("13.79.145.215", 8000);
            socket = new Socket("localhost", 8000);
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            responseHandler = new ServerResponseHandler(inputStream, client, ss);
            responseHandler.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    @Override
    public void close() {
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendKey() throws IOException {
        ClientRequest c = new ClientRequest(ClientRequestType.GIVEMEFILE);
        c.setKey(keyFile);
        outputStream.writeObject(c);
    }

    @Override
    public void setKey(KeyFile kf) {
        this.keyFile = kf;
    }

    @Override
    public void setFile(File file) {
        this.myFile = file;
    }

    @Override
    public void setMOC(MoreOptionsController mocController) {
        MOCcontroller = mocController;
        responseHandler.setMoc(MOCcontroller);
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
            if (name.equals("")) {
                name = "anonymous" + random.nextInt(100000);
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
        System.out.println("sending" + name);
        if (name.equals("")) {
            name = "anonymous" + random.nextInt(100000);
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
    public void requestScramble(Room room) {
        ClientRequest cr = new ClientRequest(ClientRequestType.REQUESTSCRAMBLE);
        cr.setRoom(room);
        try {
            outputStream.writeObject(cr);
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
    public void sendTime(Room room, Solve solve) {
        try {
            ClientRequest cr = new ClientRequest(ClientRequestType.SENDTIME);
            cr.setSolve(solve);
            cr.setRoom(room);
            outputStream.writeObject(cr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendChat(Room room, String msg) {
        try {
            ClientRequest cr = new ClientRequest(ClientRequestType.SENDCHAT);
            cr.setRoom(room);
            cr.setMsg(msg);
            outputStream.writeObject(cr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void leaveRoom(Room room) {
        ClientRequest cr = new ClientRequest(ClientRequestType.LEAVEROOM);
        cr.setRoom(room);
        try {
            outputStream.writeObject(cr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendFile() { // catch in future
        ClientRequest toSend = new ClientRequest(ClientRequestType.FILE);   // zmien podejscie daj mu setFile
        toSend.setFile(myFile);
        try {
            outputStream.writeObject(toSend);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ServerResponseHandler extends Thread {
        private RoomWindow window;
        private MoreOptionsController MOCcontroller;
        StatisticServer ss;
        ObjectInputStream inputStream;
        Client client;

        ServerResponseHandler(ObjectInputStream inputStream, Client client, StatisticServer ss) {
            this.inputStream = inputStream;
            this.client = client;
            this.ss = ss;
        }

        public void setWindow(RoomWindow wind) {
            this.window = wind;
        }

        public void setMoc(MoreOptionsController controller) {
            MOCcontroller = controller;
        }
        @Override
        public void run() {
            try {
                while (true) {
                    try {
                        ServerResponse sr = (ServerResponse) inputStream.readObject();
                        if (sr == null) {
                            continue;
                        }
                        switch (sr.getType()) {
                            case FILERECEIVED:
                                Platform.runLater(() -> {
                                    MOCcontroller.ReceivedCodePrinter(sr.getKey());
                                });
                                break;
                            case FILE:
                                File file = sr.getFile();
                                Platform.runLater(() -> {
                                    if (file == null) {
                                        Stage stage = new Stage();
                                        stage.setTitle("Wrong key");
                                        StackPane root = new StackPane();
                                        Text text = new Text("Wrong key");
                                        text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
                                        root.getChildren().add(text);
                                        Scene scene = new Scene(root, 200, 150);
                                        stage.setScene(scene);
                                        stage.show();
                                    } else {
                                        ss.importFromFile(file);
                                        file.delete();
                                    }
                                });
                                break;
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
                                if (window == null) break;
                                System.out.println("user upd");
                                Platform.runLater(() -> {
                                    window.renderUsers(sr.getUsers());
                                });
                                break;
                            case TIMESCHANGED:
                                if (window == null) break;
                                System.out.println("TImes upd");
                                Platform.runLater(() -> {
                                    window.renderTimes(sr.getSolves());
                                });
                                break;
                            case ROOMJOINED:
                                client.roomAccessHasBeenGranted(sr.getRoom());
                                break;
                            case WRONGPASSWORD:
                                Platform.runLater(() -> {
                                    client.wrongPassword();
                                });
                                break;
                            case ROOMFULL:
                                Platform.runLater(() -> {
                                    client.roomFull();
                                });
                                break;
                            case CHATMSG:
                                if (window == null) break;
                                System.out.println("chat msg");
                                Platform.runLater(() -> {
                                    window.getMessage(sr.getMsg());
                                });
                                break;
                            case SCRAMBLERECEIVED:
                                if (window == null) break;
                                System.out.println("Scramble received, ay");
                                Platform.runLater(() -> {
                                    window.getScramble(sr.getScramble());
                                });
                                break;
                            case HOSTGRANTED:
                                if (window == null) break;
                                System.out.println("I'm da host");
                                Platform.runLater(() -> {
                                    window.getHostPermissions();
                                });
                                break;
                        }
                    } catch (EOFException e) {
                    }
                }
            } catch (Exception e) {
                //disconnected from server, oopsie
                //e.printStackTrace();
            }
        }
    }
}
