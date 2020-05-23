package view.drawing;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.enums.CubeType;

import javax.swing.event.ChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class ClickDrawer extends Application {

    Color[][] cube;
    ArrayList<Edge> edges;
    GridPane rubics;
    ChoiceBox<String> curCol;
    public GridPane draw() {
        int hw = 3;
        GridPane gridPane = new GridPane();
        gridPane.setVgap(15);
        gridPane.setHgap(15);
        gridPane.setAlignment(Pos.CENTER);

        ArrayList<GridPane> lay = new ArrayList<>();
        for(int i = 0; i < 12; i++) {
            GridPane curr = new GridPane();
            if(i % 4 == 1 || (i- i%4)/ 4 == 1) {
                curr.setStyle("-fx-background-color: black ; -fx-vgap: 1; -fx-hgap: 1; -fx-padding: 1;");
                for(int j = 0; j < hw*hw; j++) {
                    Rectangle field = new Rectangle(40, 40);
                    ArrayList<Integer> ids=new ArrayList<>();
                    ids.add(i); ids.add(j);
                    field.setUserData(ids);
                    field.setFill(cube[((j - j%hw)/hw + (i - i%4)/4 * hw)][j%hw + (i % 4)*hw]);
                    curr.add(field, j%hw, (j - j%hw)/hw);
                    field.setOnMouseClicked(mouseEvent -> {
                        try {
                            Color newCol = (Color) Color.class.getField(curCol.getValue().toUpperCase()).get(null);
                            field.setFill(newCol);
                            ArrayList<Integer> xd = ((ArrayList<Integer>)field.getUserData());
                            cube[xd.get(0)][xd.get(1)]=newCol;
                            //cube[i][j]=newCol;
                        } catch (IllegalAccessException | NoSuchFieldException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
            lay.add(curr);
        }
        for(int i = 0; i < 12; i++) {
            gridPane.add(lay.get(i), i % 4, (i- i%4)/ 4);
        }
        return gridPane;
    }

    public ClickDrawer() {
        cube = new Color[3*3][4*3];
        for(int i = 0; i < 3*3; i++) {
            for(int j = 0; j < 4*3; j++) {
                cube[i][j] = Color.LIGHTSKYBLUE;
            }
        }
    }

    boolean validate() {
        int oriented = 0;
        int test;
        test = findEdge(Color.WHITE, Color.BLUE);
        if(test==-1){
            System.out.println("couldnt find edge: +Color.WHITE+Color.BLUE");
            return false;
        }
        oriented+=test;
        test = findEdge(Color.WHITE, Color.GREEN);
        if(test==-1){
            System.out.println("couldnt find edge: +Color.WHITE+Color.GREEN");
            return false;
        }
        oriented+=test;
        test = findEdge(Color.WHITE, Color.RED);
        if(test==-1){
            System.out.println("couldnt find edge: +Color.WHITE+Color.RED");
            return false;
        }
        oriented+=test;
        test = findEdge(Color.WHITE, Color.ORANGE);
        if(test==-1){
            System.out.println("couldnt find edge: +Color.WHITE+Color.ORANGE");
            return false;
        }
        test = findEdge(Color.YELLOW, Color.BLUE);
        if(test==-1){
            System.out.println("couldnt find edge: +Color.YELLOW+Color.BLUE");
            return false;
        }
        oriented+=test;
        test = findEdge(Color.YELLOW, Color.GREEN);
        if(test==-1){
            System.out.println("couldnt find edge: +Color.YELLOW+Color.GREEN");
            return false;
        }
        oriented+=test;
        test = findEdge(Color.YELLOW, Color.RED);
        if(test==-1){
            System.out.println("couldnt find edge: +Color.YELLOW+Color.RED");
            return false;
        }
        oriented+=test;
        test = findEdge(Color.YELLOW, Color.ORANGE);
        if(test==-1){
            System.out.println("couldnt find edge: +Color.YELLOW+Color.ORANGE");
            return false;
        }
        test = findEdge(Color.RED, Color.BLUE);
        if(test==-1){
            System.out.println("couldnt find edge: +Color.RED+Color.BLUE");
            return false;
        }
        oriented+=test;
        test = findEdge(Color.RED, Color.GREEN);
        if(test==-1){
            System.out.println("couldnt find edge: +Color.RED+Color.GREEN");
            return false;
        }
        test = findEdge(Color.ORANGE, Color.BLUE);
        if(test==-1){
            System.out.println("couldnt find edge: +Color.ORANGE+Color.BLUE");
            return false;
        }
        oriented+=test;
        test = findEdge(Color.ORANGE, Color.GREEN);
        if(test==-1){
            System.out.println("couldnt find edge: +Color.ORANGE+Color.GREEN");
            return false;
        }
        if(oriented%2==0){
            return true;
        }
        System.out.println("Edge misoriented");
        return false;
    }

    int findEdge(Color one, Color two){
        for(Edge e: edges){
            Color firstColor = cube[e.getFirst().getKey()][e.getFirst().getValue()];
            Color secondColor = cube[e.getSecond().getKey()][e.getSecond().getValue()];
            if(firstColor.equals(one) && secondColor.equals(two)){
                if(one.equals(Color.WHITE) || one.equals(Color.YELLOW)){
                    if(e.getFirst().getKey()<=2 || e.getFirst().getKey()>=6){
                        return 1;
                    }
                    if(e.getFirst().getKey()==4 && e.getFirst().getValue()>=3 && e.getFirst().getValue()<=5){
                        return 1;
                    }
                    if(e.getFirst().getKey()==4 && e.getFirst().getValue()>=9 && e.getFirst().getValue()<=11){
                        return 1;
                    }
                    return 0;
                }
                if(two.equals(Color.WHITE) || two.equals(Color.YELLOW)){
                    if(e.getSecond().getKey()<=2 || e.getSecond().getKey()>=6){
                        return 1;
                    }
                    if(e.getSecond().getKey()==4 && e.getSecond().getValue()>=3 && e.getSecond().getValue()<=5){
                        return 1;
                    }
                    if(e.getSecond().getKey()==4 && e.getSecond().getValue()>=9 && e.getSecond().getValue()<=11){
                        return 1;
                    }
                    return 0;
                }
                //doesnt have yellow or white, must have blue or green
                if(one.equals(Color.GREEN) || one.equals(Color.BLUE)){
                    if(e.getFirst().getKey()<=2 || e.getFirst().getKey()>=6){
                        return 1;
                    }
                    if(e.getFirst().getKey()==4 && e.getFirst().getValue()>=3 && e.getFirst().getValue()<=5){
                        return 1;
                    }
                    if(e.getFirst().getKey()==4 && e.getFirst().getValue()>=9 && e.getFirst().getValue()<=11){
                        return 1;
                    }
                    return 0;
                }
                if(two.equals(Color.GREEN) || two.equals(Color.BLUE)){
                    if(e.getSecond().getKey()<=2 || e.getSecond().getKey()>=6){
                        return 1;
                    }
                    if(e.getSecond().getKey()==4 && e.getSecond().getValue()>=3 && e.getSecond().getValue()<=5){
                        return 1;
                    }
                    if(e.getSecond().getKey()==4 && e.getSecond().getValue()>=9 && e.getSecond().getValue()<=11){
                        return 1;
                    }
                    return 0;
                }
                return 0;
            }
        }
        return -1;
    }
    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(true);
        BorderPane layout = new BorderPane();
        stage.setScene(new Scene(layout, 1000, 800));
        GridPane rubics = draw();
        this.rubics = rubics;
        layout.setCenter(rubics);
        curCol = new ChoiceBox<>();
        ArrayList<String> colorNames = new ArrayList<>(Arrays.asList("Blue", "Green", "Red", "Orange", "White", "Yellow"));
        edges = new ArrayList<>();
        edges.add(new Edge(new Pair(1, 3), new Pair(3, 1)));
        edges.add(new Edge(new Pair(0, 4), new Pair(3, 10)));
        edges.add(new Edge(new Pair(1, 5), new Pair(3, 7)));
        edges.add(new Edge(new Pair(2, 4), new Pair(3, 4)));
        edges.add(new Edge(new Pair(4, 0), new Pair(4, 11)));
        edges.add(new Edge(new Pair(5, 1), new Pair(7, 3)));
        edges.add(new Edge(new Pair(4, 2), new Pair(4, 3)));
        edges.add(new Edge(new Pair(5, 4), new Pair(6, 4)));
        edges.add(new Edge(new Pair(4, 5), new Pair(4, 6)));
        edges.add(new Edge(new Pair(5, 7), new Pair(7, 5)));
        edges.add(new Edge(new Pair(4, 8), new Pair(4, 9)));
        edges.add(new Edge(new Pair(5, 10), new Pair(8, 4)));
        colorNames.sort(Comparator.comparing(String::toString));
        curCol.getItems().addAll(colorNames);
        curCol.setValue(colorNames.get(0));
        layout.setTop(curCol);
        Button val = new Button();
        val.setOnMouseClicked(mouseEvent -> {
            System.out.println(validate());
        });
        val.setText("aaaaaa");
        layout.setRight(val);
        stage.show();
    }
}
