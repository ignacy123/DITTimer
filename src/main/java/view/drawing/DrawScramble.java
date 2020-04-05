package view.drawing;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.enums.CubeType;
import model.logic.Move;
import model.logic.MoveImplementation;

import java.util.ArrayList;

public class DrawScramble extends Stage {
    ArrayList<ArrayList<Color>> cube;
    CubeType type = CubeType.THREEBYTHREE;

    ArrayList<Move> moves;
    DrawScramble(ArrayList<Move> moves) {
        this.moves = moves;
    }
    private GridPane draw(int hw) {
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
                    Color toFill = cube.get((j - j%hw)/hw + (i - i%4)/4 * hw).get(j%hw + (i % 4)*hw);
                    field.setFill(toFill);
                    curr.add(field, j%hw, (j - j%hw)/hw);
                }
            }
            lay.add(curr);
        }
        for(int i = 0; i < 12; i++) {
            gridPane.add(lay.get(i), i % 4, (i- i%4)/ 4);
        }
        return gridPane;
    }


    public void doMagic() {
        cube = new ArrayList<>();
        Stage stage = new Stage();
        stage.setResizable(false);
        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        stage.setScene(new Scene(layout, 600, 470));
        populateStandard();
        Rotations a = new Rotations(cube);
        a.executeMoves(moves);
        layout.getChildren().add(draw(3));
        stage.show();
    }
    public void populateStandard() {
        for(int i = 0; i < 3; i++) {
            ArrayList<Color> toAdd = new ArrayList<>();
            toAdd.add(Color.AZURE); toAdd.add(Color.AZURE); toAdd.add(Color.AZURE);
            toAdd.add(Color.WHITE); toAdd.add(Color.WHITE); toAdd.add(Color.WHITE);
            toAdd.add(Color.AZURE); toAdd.add(Color.AZURE); toAdd.add(Color.AZURE);
            toAdd.add(Color.AZURE); toAdd.add(Color.AZURE); toAdd.add(Color.AZURE);
            cube.add(toAdd);
        }
        for(int i = 0; i < 3; i++) {
            ArrayList<Color> toAdd = new ArrayList<>();
            toAdd.add(Color.ORANGE); toAdd.add(Color.ORANGE); toAdd.add(Color.ORANGE);
            toAdd.add(Color.GREEN); toAdd.add(Color.GREEN); toAdd.add(Color.GREEN);
            toAdd.add(Color.RED); toAdd.add(Color.RED); toAdd.add(Color.RED);
            toAdd.add(Color.BLUE); toAdd.add(Color.BLUE); toAdd.add(Color.BLUE);
            cube.add(toAdd);
        }
        for(int i = 0; i < 3; i++) {
            ArrayList<Color> toAdd = new ArrayList<>();
            toAdd.add(Color.AZURE); toAdd.add(Color.AZURE); toAdd.add(Color.AZURE);
            toAdd.add(Color.YELLOW); toAdd.add(Color.YELLOW); toAdd.add(Color.YELLOW);
            toAdd.add(Color.AZURE); toAdd.add(Color.AZURE); toAdd.add(Color.AZURE);
            toAdd.add(Color.AZURE); toAdd.add(Color.AZURE); toAdd.add(Color.AZURE);
            cube.add(toAdd);
        }
    }
}