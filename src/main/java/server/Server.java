package server;

import model.conn.ClientRequest;
import model.conn.RoomHolder;
import model.conn.User;

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
            holder.requestRoom(new User(1));
        }

        @Override
        public void run() {
            try {
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                outputStream.writeObject(holder.getAvailableRooms());
                while (true) {
                    ClientRequest request = (ClientRequest) inputStream.readObject();
                    if(request==null){
                        continue;
                    }
                    System.out.println(request.getType());

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
