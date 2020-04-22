package view.test;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.conn.Room;
import model.conn.ServerService;
import model.conn.ServerServiceImplementation;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Application {
    @FXML
    private Button createButton;
    @FXML
    private ListView roomsListView;


    @FXML
    void initialize(){
        ServerService conn = new ServerServiceImplementation();
        conn.start();
        ObservableList<Room> rooms = FXCollections.observableArrayList();
        rooms.addAll(conn.requestRooms());
        roomsListView.setItems(rooms);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("client.fxml"));
        Pane pane = loader.load();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }
}
