package view.drawing;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
    Color[][] cube;
    CubeType type;
    Rotator rotator;

    private GridPane draw() {
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
    DrawScramble(CubeType type) {
        this.type = type;
        cube = new Color[4*3][4*4];
        populateStandard();
        if(type == CubeType.THREEBYTHREE)
        rotator = new Rotator3(cube);
        else if(type == CubeType.FOURBYFOUR)
            rotator = new Rotator4(cube);
        else rotator = new Rotator2(cube);
    }
    public Rotator getRotator() {
        return rotator;
    }
    public void doMagic() {
        Stage stage = new Stage();
        stage.setResizable(false);
        BorderPane layout = new BorderPane();
        stage.setScene(new Scene(layout, 1000, 800));

        //Rotations a = new Rotations(cube);
        //a.executeMoves(moves);
        Text text = new Text();
        //text.setText(new ScrambleGeneratorImplementation(CubeType.FOURBYFOUR).scrambleToString(moves));
        layout.setCenter(draw());
        layout.setBottom(text);
        layout.setAlignment(text, Pos.TOP_CENTER);
        text.setTextAlignment(TextAlignment.JUSTIFY);
        text.setFont(new Font(15));
        stage.show();
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