package view.drawing;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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

public class ClickDrawer extends DrawScramble {
    Color[][] cube;
    GridPane rubics;
    ChoiceBox<String> curCol;
    @Override
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
                    field.setFill(cube[((j - j%hw)/hw + (i - i%4)/4 * hw)][j%hw + (i % 4)*hw]);
                    curr.add(field, j%hw, (j - j%hw)/hw);
                    field.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            try {
                                Color newCol = (Color) Color.class.getField(curCol.getValue().toUpperCase()).get(null);
                                field.setFill(newCol);
                            } catch (IllegalAccessException | NoSuchFieldException e) {
                                e.printStackTrace();
                            }
                            //Color newCol = (Color) Color.class.getField(colorNames.get((Integer) ne).toUpperCase()).get(null);
                            //field.setFill();
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
   @Override
    public void doMagic() {
        Stage stage = new Stage();
        stage.setResizable(false);
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
        stage.show();
    }
    ClickDrawer() {
        cube = new Color[3*3][4*3];
        for(int i = 0; i < 3*3; i++) {
            for(int j = 0; j < 4*3; j++) {
                cube[i][j] = Color.LIGHTSKYBLUE;
            }
        }
    }
}
