package view.drawing;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.enums.CubeType;
import model.logic.Move;

import java.util.ArrayList;

public class DrawScramble extends Stage {
    Color[][] cube = new Color[12][16];
    CubeType type;
    GridPane rubics;
    Rotator2 rotator2 = new Rotator2(cube);
    Rotator3 rotator3 = new Rotator3(cube);
    Rotator4 rotator4 = new Rotator4(cube);
    public GridPane draw() {
        int hw = 4;
        if(type == CubeType.THREEBYTHREE) hw = 3;
        if(type == CubeType.TWOBYTWO) hw = 2;
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
                    field.setFill(cube[((j - j%hw)/hw + (i - i%4)/4 * hw)][j%hw + (i % 4)*hw]);
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
    public void update() {
        int size = 4;
        if(type == CubeType.THREEBYTHREE) size = 3;
        if(type == CubeType.TWOBYTWO) size = 2;
        for(int i = 0; i < rubics.getChildren().size(); i++) {
            if(!(i%4 == 1 || (i-i%4)/4 == 1)) continue;
            for(int j = 0; j < size*size; j++) {
                ((Rectangle)((GridPane)rubics.getChildren().get(i)).getChildren().get(j)).setFill(cube[((j-j%size)/size + (i-i%4)/4 * size)][j%size + (i%4)*size]);
            }
        }
    }
    public GridPane doMagic(ArrayList<Move> moves, CubeType type) {
        this.type = type;
        populateStandard();
        Rotator rotator = rotator4;
        if(type==CubeType.THREEBYTHREE) rotator = rotator3;
        else if(type == CubeType.TWOBYTWO) rotator = rotator2;
        rotator.executeMoves(moves);
        GridPane rubics = draw();
        this.rubics = rubics;
        return rubics;
    }
    public void populateStandard() {
        int n = 4;
        if(type == CubeType.THREEBYTHREE) n = 3;
        else if(type == CubeType.TWOBYTWO) n = 2;
        for(int i = 0; i < n; i++) {
            for(int j = n; j < 2*n; j++) cube[i][j] = Color.WHITE;
        }
        for(int i = n; i < 2*n; i++) {
            for(int j = 0; j < n; j++) cube[i][j] = Color.ORANGE;
            for(int j = n; j < 2*n; j++) cube[i][j] = Color.GREEN;
            for(int j = 2*n; j < 3*n; j++) cube[i][j] = Color.RED;
            for(int j = 3*n; j < 4*n; j++) cube[i][j] = Color.BLUE;
        }
        for(int i = 2*n; i < 3*n; i++) {
            for(int j = n; j < 2*n; j++) cube[i][j] = Color.YELLOW;

        }
    }
}