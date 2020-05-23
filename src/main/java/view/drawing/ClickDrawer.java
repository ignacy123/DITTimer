package view.drawing;

import javafx.application.Application;

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
import javafx.stage.Stage;
import model.enums.CubeType;

import javax.swing.event.ChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class ClickDrawer extends Application {

    Color[][] cube;
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
        //aaaaa
        return true;
    }

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane layout = new BorderPane();
        stage.setScene(new Scene(layout, 1000, 800));
        GridPane rubics = draw();
        this.rubics = rubics;
        layout.setCenter(rubics);
        curCol = new ChoiceBox<>();
        ArrayList<String> colorNames = new ArrayList<>(Arrays.asList("Blue", "Green", "Red", "Orange", "White", "Yellow"));
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
