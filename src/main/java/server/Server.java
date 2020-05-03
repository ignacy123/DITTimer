package server;

import model.conn.*;
import model.enums.ClientRequestType;
import model.enums.ServerResponseType;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
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
                new ClientHandler(socket, counter++, holder).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class ClientHandler extends Thread {
        private Socket socket;
        private User user;
        private RoomHolder holder;

        ClientHandler(Socket socket, int id, RoomHolder holder) {
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
                    if(request==null){
                        continue;
                    }

                    ServerResponse sr;
                    switch (request.getType()){
                        case REQUESTROOMS:
                            sr = new ServerResponse(ServerResponseType.SENDINGROOMS);
                            sr.setRooms(holder.getAvailableRooms());
                            System.out.println("Sending rooms: "+holder.getAvailableRooms().size());
                            outputStream.writeObject(sr);
                            break;
                        case CREATEROOM:
                            System.out.println("Creating");
                            sr = new ServerResponse(ServerResponseType.ROOMHASBEENCREATED);
                            if(holder.hasFreeRoom()){
                                Room room = holder.requestRoom(user);
                                sr.setRoom(room);
                                holder.joinRoom(user, room, outputStream);
                                user.setName(request.getUserName());
                                if(room!=null){
                                    room.setType(request.getCubeType());
                                    //room.setHost(user);
                                    room.setPrivate(request.getPrivate());
                                    System.out.println(request.getPassword());
                                    if(request.getPassword()!=null){
                                        holder.setPassword(room, request.getPassword());
                                    }
                                }
                            }
                            outputStream.writeObject(sr);
                            break;
                        case GETUSERS:
                            sr = new ServerResponse(ServerResponseType.USERSCHANGED);
                            Room room = holder.getRoom(request.getRoom().getID());
                            if(room == null) {
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
                            sr = new ServerResponse(ServerResponseType.TIMESCHANGED);
                            Room sienna = holder.getRoom(request.getRoom().getID());
                            if(sienna == null) {
                                System.out.println("Room not found ;c");//should handle properly
                                break;
                            }
                            if(request.getType() == ClientRequestType.SENDTIME)
                            sienna.addTime(user, request.getTime());
                            sr.setTimes(sienna.getTimes());
                            //for every user in room
                            for(ObjectOutputStream mike: holder.getStreams(sienna)) {
                                mike.reset();
                                mike.writeObject(sr);
                            }
                            break;
                        case LEAVEROOM:
                            Room verona = holder.getRoom(request.getRoom().getID());
                            //remove user from verona
                            holder.removeUser(user, verona);
                            sr=new ServerResponse(ServerResponseType.USERSCHANGED);
                            sr.setUsers(verona.getUsers());
                            ServerResponse dt = new ServerResponse(ServerResponseType.TIMESCHANGED);
                            dt.setTimes(verona.getTimes());
                            for(ObjectOutputStream marcel: holder.getStreams(verona)) {
                                System.out.println("sending update to");
                                marcel.reset();
                                marcel.writeObject(sr);
                                marcel.reset();
                                marcel.writeObject(dt);
                            }
                            //if no user in, then delete it
                            if(verona.getUsers().isEmpty()) {
                                holder.removeRoom(verona);
                            }
                            break;
                        case SENDCHAT:
                            String msg = user.getName()+": "+request.getMsg();
                            Room room1 = holder.getRoom(request.getRoom().getID());
                            if(room1 == null) {
                                System.out.println("Room not found ;c");//should handle properly
                                break;
                            }
                            sr = new ServerResponse(ServerResponseType.CHATMSG);
                            sr.setMsg(msg);
                            for(ObjectOutputStream mike: holder.getStreams(room1)) {
                                mike.reset();
                                mike.writeObject(sr);
                            }
                            break;

                        case JOINROOM:
                            System.out.println("join");
                            sr = new ServerResponse(ServerResponseType.ROOMJOINED);
                            Room rome = holder.getRoom(request.getRoom().getID());
                            if(rome == null) {
                                System.out.println("Room not found ;c");//should handle properly
                                break;
                            }
                            if(!rome.isJoinable()){
                                sr = new ServerResponse(ServerResponseType.ROOMFULL);
                                outputStream.writeObject(sr);
                                break;
                            }
                            if(rome.isPrivate()){
                                String password = holder.getPassword(rome);
                                System.out.println("Expected password: "+password);
                                System.out.println("Actual password: "+request.getPassword());
                                if(!password.equals(request.getPassword())){
                                    sr = new ServerResponse(ServerResponseType.WRONGPASSWORD);
                                    outputStream.writeObject(sr);
                                    break;
                                }
                            }
                            System.out.println(user.getName()+"asked to join "+rome.getHost().getName());
                            user.setName(request.getUserName());
                            holder.joinRoom(user, rome, outputStream);
                            sr.setTimes(rome.getTimes());
                            sr.setUsers(rome.getUsers());
                            sr.setRoom(rome);
                            outputStream.reset();
                            outputStream.writeObject(sr);
                            ServerResponse ans = new ServerResponse(ServerResponseType.USERSCHANGED);
                            ans.setUsers(rome.getUsers());
                            for(ObjectOutputStream mike: holder.getStreams(rome)) {
                                if(mike == outputStream) continue;
                                mike.reset();
                                mike.writeObject(ans);
                            }
                    }
                }
            } catch (EOFException e) {
                //reached EOF
            } catch(Exception e){
                //user disconnected, he might still be in some room?
                if(user.getRoom()!=null) {
                    Room room = user.getRoom();
                    user.getRoom().removeUser(user);
                    if(room.getUsers().isEmpty()) {
                        holder.removeRoom(room);
                    }
                    ServerResponse pol = new ServerResponse(ServerResponseType.USERSCHANGED);
                    pol.setUsers(room.getUsers());
                    ServerResponse rubio = new ServerResponse(ServerResponseType.TIMESCHANGED);
                    rubio.setTimes(room.getTimes());
                    //update for other users?
                    for(ObjectOutputStream oth: holder.getStreams(room)) {
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
}
