package server;

import model.conn.*;
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
                            sr = new ServerResponse(ServerResponseType.ROOMHASBEENCREATED);
                            if(holder.hasFreeRoom()){
                                Room room = holder.requestRoom(user);
                                sr.setRoom(room);
                                user.setName(request.getUserName());
                                if(room!=null){
                                    room.setType(request.getCubeType());
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
                            outputStream.writeObject(sr);
                            break;
                        case GETTIMES:
                            sr = new ServerResponse(ServerResponseType.TIMESCHANGED);
                            Room rome = holder.getRoom(request.getRoom().getID());
                            if(rome == null) {
                                System.out.println("Room not found ;c");//should handle properly
                                break;
                            }
                            System.out.println("Sending times");
                            sr.setTimes(rome.getTimes());
                            outputStream.writeObject(sr);
                            break;
                        case SENDTIME:
                            sr = new ServerResponse(ServerResponseType.TIMESCHANGED);
                            Room sienna = holder.getRoom(request.getRoom().getID());
                            if(sienna == null) {
                                System.out.println("Room not found ;c");//should handle properly
                                break;
                            }
                            System.out.println("Time"+request.getTime());
                            sienna.addTime(request.getTime());
                            sr.setTimes(sienna.getTimes());
                            outputStream.writeObject(sr);
                            break;
                    }
                }
            } catch (EOFException e) {
                //reached EOF
            } catch(Exception e){
                e.printStackTrace();

            }
        }
    }
}
