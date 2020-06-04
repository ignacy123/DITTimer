package server;

import model.conn.*;
import model.enums.ClientRequestType;
import model.enums.CubeType;
import model.enums.ServerResponseType;
import model.logic.KeyFile;
import model.logic.Move;
import model.logic.ScrambleGenerator;
import model.logic.ScrambleGeneratorImplementation;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    static Map<String, File> myMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        RoomHolder holder;
        int counter = 1;
        holder = new RoomHolder();
        try {

            ServerSocket serverSocket = new ServerSocket(8000);
            System.out.println("Listening on port: " + 8000);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("A new wild client has appeared.");
                new ClientHandler(socket, counter++, holder, myMap).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class ClientHandler extends Thread {
        Map<String, File> myMap;
        private Socket socket;
        private User user;
        private RoomHolder holder;
        private ScrambleGenerator gen2 = new ScrambleGeneratorImplementation(CubeType.TWOBYTWO);
        private ScrambleGenerator gen3 = new ScrambleGeneratorImplementation(CubeType.THREEBYTHREE);
        private ScrambleGenerator gen4 = new ScrambleGeneratorImplementation(CubeType.FOURBYFOUR);

        ClientHandler(Socket socket, int id, RoomHolder holder, Map<String, File> map) {
            this.myMap = map;
            this.socket = socket;
            user = new User(id);
            //user.setSocket(socket);
            this.holder = holder;
        }

        @Override
        public void run() {
            try {
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                while (true) {
                    ClientRequest request = (ClientRequest) inputStream.readObject();
                    if (request == null) {
                        continue;
                    }

                    ServerResponse sr;
                    switch (request.getType()) {
                        case FILE:
                            String key;
                            int code = (int) (Math.random() * 10000);
                            key = Integer.toString(code);
                            myMap.put(key, request.getFile());
                            sr = new ServerResponse(ServerResponseType.FILERECEIVED);
                            KeyFile kf = new KeyFile(key);
                            sr.setKey(kf);
                            outputStream.writeObject(sr);
                            break;
                        case GIVEMEFILE:
                            if (request.getKey().getKey() != null) {
                                File toBeSent = myMap.get(request.getKey().getKey());
                                sr = new ServerResponse(ServerResponseType.FILE);
                                sr.setFile(toBeSent);
                                myMap.remove(request.getKey().getKey());
                                outputStream.writeObject(sr);
                            }
                            break;
                        case REQUESTROOMS:
                            sr = new ServerResponse(ServerResponseType.SENDINGROOMS);
                            sr.setRooms(holder.getAvailableRooms());
                            System.out.println("Sending rooms: " + holder.getAvailableRooms().size());
                            outputStream.writeObject(sr);
                            break;
                        case CREATEROOM:
                            System.out.println("Creating");
                            sr = new ServerResponse(ServerResponseType.ROOMHASBEENCREATED);
                            if (holder.hasFreeRoom()) {
                                Room room = holder.requestRoom(user);
                                sr.setRoom(room);
                                holder.joinRoom(user, room, outputStream);
                                user.setName(request.getUserName());
                                if (room != null) {
                                    room.setType(request.getCubeType());
                                    room.setHost(user);
                                    room.setPrivate(request.getPrivate());
                                    System.out.println(request.getPassword());
                                    if (request.getPassword() != null) {
                                        holder.setPassword(room, request.getPassword());
                                    }
                                }
                            }
                            outputStream.writeObject(sr);
                            break;
                        case GETUSERS:
                            sr = new ServerResponse(ServerResponseType.USERSCHANGED);
                            Room room = holder.getRoom(request.getRoom().getID());
                            if (room == null) {
                                System.out.println("Room not found ;c");//same
                                break;
                            }
                            System.out.println("Sending users");
                            sr.setUsers(room.getUsers());
                            outputStream.reset();
                            outputStream.writeObject(sr);
                            break;
                        case GETTIMES:
                        case SENDTIME:
                            System.out.println("Time received");
                            sr = new ServerResponse(ServerResponseType.TIMESCHANGED);
                            Room sienna = holder.getRoom(request.getRoom().getID());
                            if (sienna == null) {
                                System.out.println("Room not found ;c");//should handle properly
                                break;
                            }
                            if (request.getType() == ClientRequestType.SENDTIME)
                                sienna.addSolve(user, request.getSolve());
                            sr.setSolves(sienna.getSolves());
                            //for every user in room
                            for (ObjectOutputStream mike : holder.getStreams(sienna)) {
                                mike.reset();
                                mike.writeObject(sr);
                            }
                            user.setReadyForNext(true);
                            if (holder.isRoomReady(sienna)) {
                                room = holder.getRoom(request.getRoom().getID());
                                sr = new ServerResponse(ServerResponseType.SCRAMBLERECEIVED);
                                CubeType type = room.getType();
                                sr.setScramble(nextScramble(type));
                                for (ObjectOutputStream nycz : holder.getStreams(room)) {
                                    nycz.reset();
                                    nycz.writeObject(sr);
                                }
                                for (User user : room.getUsers()) {
                                    user.setReadyForNext(false);
                                }
                            }
                            break;
                        case LEAVEROOM:
                            System.out.println("leab");
                            Room verona = holder.getRoom(request.getRoom().getID());
                            //remove user from verona
                            holder.removeUser(user, verona);
                            ArrayList<ObjectOutputStream> streams = holder.getStreams(verona);
                            if (verona.getHost().equals(user)) {
                                System.out.println("host is leaving");
                                if (streams.size() > 0) {
                                    verona.setHost(verona.getUsers().get(0));
                                    streams.get(0).reset();
                                    streams.get(0).writeObject(new ServerResponse(ServerResponseType.HOSTGRANTED));
                                }
                            }
                            sr = new ServerResponse(ServerResponseType.USERSCHANGED);
                            sr.setUsers(verona.getUsers());
                            ServerResponse dt = new ServerResponse(ServerResponseType.TIMESCHANGED);
                            dt.setSolves(verona.getSolves());
                            for (ObjectOutputStream marcel : streams) {
                                System.out.println("sending update to");
                                marcel.reset();
                                marcel.writeObject(sr);
                                marcel.reset();
                                marcel.writeObject(dt);
                            }
                            //if no user in, then delete it
                            if (verona.getUsers().isEmpty()) {
                                holder.removeRoom(verona);
                            }
                            break;
                        case REQUESTSCRAMBLE:
                            room = holder.getRoom(request.getRoom().getID());
                            sr = new ServerResponse(ServerResponseType.SCRAMBLERECEIVED);
                            CubeType type = room.getType();
                            sr.setScramble(nextScramble(type));
                            for (ObjectOutputStream nycz : holder.getStreams(room)) {
                                nycz.reset();
                                nycz.writeObject(sr);
                            }
                            for (User user : room.getUsers()) {
                                user.setReadyForNext(false);
                            }
                            break;
                        case SENDCHAT:
                            String msg = user.getName() + ": " + request.getMsg();
                            Room room1 = holder.getRoom(request.getRoom().getID());
                            if (room1 == null) {
                                System.out.println("Room not found ;c");//should handle properly
                                break;
                            }
                            sr = new ServerResponse(ServerResponseType.CHATMSG);
                            sr.setMsg(msg);
                            for (ObjectOutputStream mike : holder.getStreams(room1)) {
                                mike.reset();
                                mike.writeObject(sr);
                            }
                            break;

                        case JOINROOM:
                            System.out.println("join");
                            sr = new ServerResponse(ServerResponseType.ROOMJOINED);
                            Room rome = holder.getRoom(request.getRoom().getID());
                            if (rome == null) {
                                System.out.println("Room not found ;c");//should handle properly
                                break;
                            }
                            if (!rome.isJoinable()) {
                                sr = new ServerResponse(ServerResponseType.ROOMFULL);
                                outputStream.writeObject(sr);
                                break;
                            }
                            if (rome.isPrivate()) {
                                String password = holder.getPassword(rome);
                                System.out.println("Expected password: " + password);
                                System.out.println("Actual password: " + request.getPassword());
                                if (!password.equals(request.getPassword())) {
                                    sr = new ServerResponse(ServerResponseType.WRONGPASSWORD);
                                    outputStream.writeObject(sr);
                                    break;
                                }
                            }
                            System.out.println(user.getName() + "asked to join " + rome.getHost().getName());
                            user.setName(request.getUserName());
                            holder.joinRoom(user, rome, outputStream);
                            sr.setSolves(rome.getSolves());
                            sr.setUsers(rome.getUsers());
                            sr.setRoom(rome);
                            outputStream.reset();
                            outputStream.writeObject(sr);
                            ServerResponse ans = new ServerResponse(ServerResponseType.USERSCHANGED);
                            ans.setUsers(rome.getUsers());
                            for (ObjectOutputStream mike : holder.getStreams(rome)) {
                                if (mike == outputStream) continue;
                                mike.reset();
                                mike.writeObject(ans);
                            }
                    }
                }
            } catch (Exception e) {
                //e.printStackTrace();
                //e.printStackTrace();
                //user disconnected, he might still be in some room?
                if (user.getRoom() != null) {
                    Room room = user.getRoom();
                    holder.removeUser(user, room);
                    ArrayList<ObjectOutputStream> streams = holder.getStreams(room);
                    if (room.getHost().equals(user)) {
                        System.out.println("host is leaving");
                        if (streams.size() > 0) {
                            room.setHost(room.getUsers().get(0));
                            try {
                                streams.get(0).reset();
                                streams.get(0).writeObject(new ServerResponse(ServerResponseType.HOSTGRANTED));
                            } catch (IOException ex) {

                            }
                        }
                    }
                    if (room.getUsers().isEmpty()) {
                        holder.removeRoom(room);
                    } else {
                        ServerResponse pol = new ServerResponse(ServerResponseType.USERSCHANGED);
                        pol.setUsers(room.getUsers());
                        ServerResponse rubio = new ServerResponse(ServerResponseType.TIMESCHANGED);
                        rubio.setSolves(room.getSolves());
                        //update for other users?
                        for (ObjectOutputStream oth : holder.getStreams(room)) {
                            try {
                                oth.reset();
                                oth.writeObject(pol);
                                oth.writeObject(rubio);
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        }
                    }
                }


            }
        }

        ArrayList<Move> nextScramble(CubeType type) {
            switch (type) {
                case TWOBYTWO:
                    return gen2.generate();
                case THREEBYTHREE:
                    return gen3.generate();
                case FOURBYFOUR:
                    return gen4.generate();
            }
            return null;
        }
    }
}