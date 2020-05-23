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
import javafx.scene.control.Dialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Pair;
import model.enums.CubeType;
import model.logic.Corner;
import model.logic.Edge;
import model.logic.OrientationLessCorner;
import model.logic.OrientationLessEdge;

import javax.swing.event.ChangeEvent;
import java.util.*;

public class ClickDrawer extends Application {

    Color[][] cube;
    ArrayList<Edge> edges;
    ArrayList<Corner> corners;
    GridPane rubics;
    ChoiceBox<String> curCol;
    BorderPane layout;
    Map<OrientationLessEdge, Integer> correctEdgeNumbers;
    Map<OrientationLessCorner, Integer> correctCornerNumbers;
    Map<Edge, Integer> edgeNumbers;
    Map<Corner, Integer> cornerNumbers;
    ArrayList<Integer> actualEdgePos = new ArrayList<>();
    ArrayList<Integer> actualCornerPos = new ArrayList<>();

    public GridPane draw() {
        int hw = 3;
        GridPane gridPane = new GridPane();
        gridPane.setVgap(15);
        gridPane.setHgap(15);
        gridPane.setAlignment(Pos.CENTER);

        ArrayList<GridPane> lay = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            GridPane curr = new GridPane();
            if (i % 4 == 1 || (i - i % 4) / 4 == 1) {
                curr.setStyle("-fx-background-color: black ; -fx-vgap: 1; -fx-hgap: 1; -fx-padding: 1;");
                for (int j = 0; j < hw * hw; j++) {
                    Rectangle field = new Rectangle(40, 40);
                    ArrayList<Integer> ids = new ArrayList<>();
                    ids.add(((j - j % hw) / hw + (i - i % 4) / 4 * hw));
                    ids.add(j % hw + (i % 4) * hw);
                    field.setUserData(ids);
                    field.setFill(cube[((j - j % hw) / hw + (i - i % 4) / 4 * hw)][j % hw + (i % 4) * hw]);
                    curr.add(field, j % hw, (j - j % hw) / hw);
                    field.setOnMouseClicked(mouseEvent -> {
                        try {
                            Color newCol = (Color) Color.class.getField(curCol.getValue().toUpperCase()).get(null);
                            ArrayList<Integer> xd = ((ArrayList<Integer>) field.getUserData());
                            if (xd.get(0) == 1 && xd.get(1) == 4) {
                                return;
                            }
                            if (xd.get(0) == 4 && xd.get(1) == 1) {
                                return;
                            }
                            if (xd.get(0) == 4 && xd.get(1) == 4) {
                                return;
                            }
                            if (xd.get(0) == 4 && xd.get(1) == 7) {
                                return;
                            }
                            if (xd.get(0) == 4 && xd.get(1) == 10) {
                                return;
                            }
                            if (xd.get(0) == 7 && xd.get(1) == 4) {
                                return;
                            }
                            field.setFill(newCol);
                            cube[xd.get(0)][xd.get(1)] = newCol;
                            //System.out.println(cube[xd.get(0)][xd.get(1)]);
                            //cube[i][j]=newCol;
                        } catch (IllegalAccessException | NoSuchFieldException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
            lay.add(curr);
        }
        for (int i = 0; i < 12; i++) {
            gridPane.add(lay.get(i), i % 4, (i - i % 4) / 4);
        }
        return gridPane;
    }

    public ClickDrawer() {
        cube = new Color[3 * 3][4 * 3];

        int n = 3;
        for (int i = 0; i < n; i++) {
            for (int j = n; j < 2 * n; j++) cube[i][j] = Color.WHITE;
        }
        for (int i = n; i < 2 * n; i++) {
            for (int j = 0; j < n; j++) cube[i][j] = Color.ORANGE;
            for (int j = n; j < 2 * n; j++) cube[i][j] = Color.GREEN;
            for (int j = 2 * n; j < 3 * n; j++) cube[i][j] = Color.RED;
            for (int j = 3 * n; j < 4 * n; j++) cube[i][j] = Color.BLUE;
        }
        for (int i = 2 * n; i < 3 * n; i++) {
            for (int j = n; j < 2 * n; j++) cube[i][j] = Color.YELLOW;

        }
    }

    public void reset() {

        int n = 3;
        for (int i = 0; i < n; i++) {
            for (int j = n; j < 2 * n; j++) cube[i][j] = Color.WHITE;
        }
        for (int i = n; i < 2 * n; i++) {
            for (int j = 0; j < n; j++) cube[i][j] = Color.ORANGE;
            for (int j = n; j < 2 * n; j++) cube[i][j] = Color.GREEN;
            for (int j = 2 * n; j < 3 * n; j++) cube[i][j] = Color.RED;
            for (int j = 3 * n; j < 4 * n; j++) cube[i][j] = Color.BLUE;
        }
        for (int i = 2 * n; i < 3 * n; i++) {
            for (int j = n; j < 2 * n; j++) cube[i][j] = Color.YELLOW;

        }
        GridPane rubics = draw();
        this.rubics = rubics;
        layout.setCenter(rubics);
    }

    public void clean() {
        for (int i = 0; i < 3 * 3; i++) {
            for (int j = 0; j < 4 * 3; j++) {
                cube[i][j] = Color.LIGHTSKYBLUE;
            }
        }
        cube[1][4] = Color.WHITE;
        cube[4][1] = Color.ORANGE;
        cube[4][4] = Color.GREEN;
        cube[4][7] = Color.RED;
        cube[4][10] = Color.BLUE;
        cube[7][4] = Color.YELLOW;
        GridPane rubics = draw();
        this.rubics = rubics;
        layout.setCenter(rubics);
    }

    boolean validate() {
        int oriented = 0;
        int test;
        test = findEdge(Color.WHITE, Color.BLUE);
        boolean works = true;
        String msg = "";
        if (test == -1) {
            msg = "There is no WHITE-BLUE edge";
            works = false;
        }
        oriented += test;
        test = findEdge(Color.WHITE, Color.GREEN);
        if (test == -1) {
            msg = "There is no WHITE-GREEN edge";
            works = false;
        }
        oriented += test;
        test = findEdge(Color.WHITE, Color.RED);
        if (test == -1) {
            msg = "There is no WHITE-RED edge";
            works = false;
        }
        oriented += test;
        test = findEdge(Color.WHITE, Color.ORANGE);
        if (test == -1) {
            msg = "There is no WHITE-ORANGE edge";
            works = false;
        }
        test = findEdge(Color.YELLOW, Color.BLUE);
        if (test == -1) {
            msg = "There is no YELLOW-BLUE edge";
            works = false;
        }
        oriented += test;
        test = findEdge(Color.YELLOW, Color.GREEN);
        if (test == -1) {
            msg = "There is no YELLOW-GREEN edge";
            works = false;
        }
        oriented += test;
        test = findEdge(Color.YELLOW, Color.RED);
        if (test == -1) {
            msg = "There is no YELLOW-RED edge";
            works = false;
        }
        oriented += test;
        test = findEdge(Color.YELLOW, Color.ORANGE);
        if (test == -1) {
            msg = "There is no YELLOW-ORANGE edge";
            works = false;
        }
        test = findEdge(Color.RED, Color.BLUE);
        if (test == -1) {
            msg = "There is no RED-BLUE edge";
            works = false;
        }
        oriented += test;
        test = findEdge(Color.RED, Color.GREEN);
        if (test == -1) {
            msg = "There is no RED-GREEn edge";
            works = false;
        }
        test = findEdge(Color.ORANGE, Color.BLUE);
        if (test == -1) {
            msg = "There is no ORANGE-BLUE edge";
            works = false;
        }
        oriented += test;
        test = findEdge(Color.ORANGE, Color.GREEN);
        if (test == -1) {
            msg = "There is no ORANGE-GREEN edge";
            works = false;
        }
        if (!works) {
            Dialog d = new Dialog();
            Window window = d.getDialogPane().getScene().getWindow();
            window.setOnCloseRequest(e -> window.hide());
            d.setTitle("Mistake found");
            d.setContentText(msg);
            d.show();
            return false;
        }
        if (oriented % 2 == 1) {
            msg = "Flip one of the edges";
            works = false;
        }
        oriented = 0;
        test = findCorner(Color.WHITE, Color.BLUE, Color.ORANGE);
        if (test == -1) {
            msg = "There is no WHITE-BLUE-ORANGE corner";
            works = false;
        }
        oriented += test;
        test = findCorner(Color.WHITE, Color.RED, Color.BLUE);
        if (test == -1) {
            msg = "There is no WHITE-RED-BLUE corner";
            works = false;
        }
        oriented += test;
        test = findCorner(Color.WHITE, Color.ORANGE, Color.GREEN);
        if (test == -1) {
            msg = "There is no WHITE-ORANGE-GREEN corner";
            works = false;
        }
        oriented += test;
        test = findCorner(Color.WHITE, Color.GREEN, Color.RED);
        if (test == -1) {
            msg = "There is no WHITE-GREEN-RED corner";
            works = false;
        }
        oriented += test;
        test = findCorner(Color.YELLOW, Color.GREEN, Color.ORANGE);
        if (test == -1) {
            msg = "There is no YELLOW-GREEN-ORANGE corner";
            works = false;
        }
        oriented += test;
        test = findCorner(Color.YELLOW, Color.RED, Color.GREEN);
        if (test == -1) {
            msg = "There is no YELLOW-RED-GREEN corner";
            works = false;
        }
        oriented += test;
        test = findCorner(Color.YELLOW, Color.BLUE, Color.RED);
        if (test == -1) {
            msg = "There is no YELLOW-BLUE-RED corner";
            works = false;
        }
        oriented += test;
        test = findCorner(Color.YELLOW, Color.ORANGE, Color.BLUE);
        if (test == -1) {
            msg = "There is no YELLOW-ORANGE-BLUE corner";
            works = false;
        }
        if (!works) {
            Dialog d = new Dialog();
            Window window = d.getDialogPane().getScene().getWindow();
            window.setOnCloseRequest(e -> window.hide());
            d.setTitle("Mistake found");
            d.setContentText(msg);
            d.show();
            return false;
        }
        oriented += test;
        if (oriented % 3 != 0) {
            msg = "Rotate a corner.";
            works = false;
        }
        if (!works) {
            Dialog d = new Dialog();
            Window window = d.getDialogPane().getScene().getWindow();
            window.setOnCloseRequest(e -> window.hide());
            d.setTitle("Mistake found");
            d.setContentText(msg);
            d.show();
            return false;
        }
        System.out.println(actualEdgePos);
        System.out.println(actualCornerPos);
        int parity1 = findSignum(actualEdgePos, 12);
        int parity2 = findSignum(actualCornerPos, 8);
        if (parity1 != parity2) {
            works = false;
            msg = "Swap two edges with each other.";
        }
        if (!works) {
            Dialog d = new Dialog();
            Window window = d.getDialogPane().getScene().getWindow();
            window.setOnCloseRequest(e -> window.hide());
            d.setTitle("Mistake found");
            d.setContentText(msg);
            d.show();
            return false;
        }
        Dialog d = new Dialog();
        Window window = d.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(e -> window.hide());
        d.setTitle("Colouring correct.");
        d.setContentText("YAY!");
        d.show();
        return true;
    }

    int findEdge(Color one, Color two) {
        for (Edge e : edges) {
            Color firstColor = cube[e.getFirst().getKey()][e.getFirst().getValue()];
            Color secondColor = cube[e.getSecond().getKey()][e.getSecond().getValue()];
            if (firstColor.equals(one) && secondColor.equals(two)) {
                actualEdgePos.set(edgeNumbers.get(e), correctEdgeNumbers.get(new OrientationLessEdge(one, two)));
                if (one.equals(Color.WHITE) || one.equals(Color.YELLOW)) {
                    if (e.getFirst().getKey() <= 2 || e.getFirst().getKey() >= 6) {
                        return 1;
                    }
                    if (e.getFirst().getKey() == 4 && e.getFirst().getValue() >= 3 && e.getFirst().getValue() <= 5) {
                        return 1;
                    }
                    if (e.getFirst().getKey() == 4 && e.getFirst().getValue() >= 9 && e.getFirst().getValue() <= 11) {
                        return 1;
                    }
                    return 0;
                }
                if (two.equals(Color.WHITE) || two.equals(Color.YELLOW)) {
                    if (e.getSecond().getKey() <= 2 || e.getSecond().getKey() >= 6) {
                        return 1;
                    }
                    if (e.getSecond().getKey() == 4 && e.getSecond().getValue() >= 3 && e.getSecond().getValue() <= 5) {
                        return 1;
                    }
                    if (e.getSecond().getKey() == 4 && e.getSecond().getValue() >= 9 && e.getSecond().getValue() <= 11) {
                        return 1;
                    }
                    return 0;
                }
                //doesnt have yellow or white, must have blue or green
                if (one.equals(Color.GREEN) || one.equals(Color.BLUE)) {
                    if (e.getFirst().getKey() <= 2 || e.getFirst().getKey() >= 6) {
                        return 1;
                    }
                    if (e.getFirst().getKey() == 4 && e.getFirst().getValue() >= 3 && e.getFirst().getValue() <= 5) {
                        return 1;
                    }
                    if (e.getFirst().getKey() == 4 && e.getFirst().getValue() >= 9 && e.getFirst().getValue() <= 11) {
                        return 1;
                    }
                    return 0;
                }
                if (two.equals(Color.GREEN) || two.equals(Color.BLUE)) {
                    if (e.getSecond().getKey() <= 2 || e.getSecond().getKey() >= 6) {
                        return 1;
                    }
                    if (e.getSecond().getKey() == 4 && e.getSecond().getValue() >= 3 && e.getSecond().getValue() <= 5) {
                        return 1;
                    }
                    if (e.getSecond().getKey() == 4 && e.getSecond().getValue() >= 9 && e.getSecond().getValue() <= 11) {
                        return 1;
                    }
                    return 0;
                }
                return 0;
            }
            if (firstColor.equals(two) && secondColor.equals(one)) {
                actualEdgePos.set(edgeNumbers.get(e), correctEdgeNumbers.get(new OrientationLessEdge(one, two)));
                if (two.equals(Color.WHITE) || two.equals(Color.YELLOW)) {
                    if (e.getFirst().getKey() <= 2 || e.getFirst().getKey() >= 6) {
                        return 1;
                    }
                    if (e.getFirst().getKey() == 4 && e.getFirst().getValue() >= 3 && e.getFirst().getValue() <= 5) {
                        return 1;
                    }
                    if (e.getFirst().getKey() == 4 && e.getFirst().getValue() >= 9 && e.getFirst().getValue() <= 11) {
                        return 1;
                    }
                    return 0;
                }
                if (one.equals(Color.WHITE) || one.equals(Color.YELLOW)) {
                    if (e.getSecond().getKey() <= 2 || e.getSecond().getKey() >= 6) {
                        return 1;
                    }
                    if (e.getSecond().getKey() == 4 && e.getSecond().getValue() >= 3 && e.getSecond().getValue() <= 5) {
                        return 1;
                    }
                    if (e.getSecond().getKey() == 4 && e.getSecond().getValue() >= 9 && e.getSecond().getValue() <= 11) {
                        return 1;
                    }
                    return 0;
                }
                //doesnt have yellow or white, must have blue or green
                if (two.equals(Color.GREEN) || two.equals(Color.BLUE)) {
                    if (e.getFirst().getKey() <= 2 || e.getFirst().getKey() >= 6) {
                        return 1;
                    }
                    if (e.getFirst().getKey() == 4 && e.getFirst().getValue() >= 3 && e.getFirst().getValue() <= 5) {
                        return 1;
                    }
                    if (e.getFirst().getKey() == 4 && e.getFirst().getValue() >= 9 && e.getFirst().getValue() <= 11) {
                        return 1;
                    }
                    return 0;
                }
                if (one.equals(Color.GREEN) || one.equals(Color.BLUE)) {
                    if (e.getSecond().getKey() <= 2 || e.getSecond().getKey() >= 6) {
                        return 1;
                    }
                    if (e.getSecond().getKey() == 4 && e.getSecond().getValue() >= 3 && e.getSecond().getValue() <= 5) {
                        return 1;
                    }
                    if (e.getSecond().getKey() == 4 && e.getSecond().getValue() >= 9 && e.getSecond().getValue() <= 11) {
                        return 1;
                    }
                    return 0;
                }
                return 0;
            }
        }
        return -1;
    }

    int findCorner(Color one, Color two, Color three) {
        for (Corner c : corners) {
            Color first = cube[c.getFirst().getKey()][c.getFirst().getValue()];
            Color second = cube[c.getSecond().getKey()][c.getSecond().getValue()];
            Color third = cube[c.getThird().getKey()][c.getThird().getValue()];
            if (first.equals(one) && second.equals(two) && third.equals(three)) {
                actualCornerPos.set(cornerNumbers.get(c), correctCornerNumbers.get(new OrientationLessCorner(one, two, three)));
                //must have a white/yellow piece
                int yellow = 0;
                int top = 0;
                if ((c.getFirst().getKey() >= 0 && c.getFirst().getKey() <= 2) || (c.getFirst().getKey() >= 6 && c.getFirst().getKey() <= 8)) {
                    top = 1;
                }
                if (first.equals(Color.WHITE) || first.equals(Color.YELLOW)) {
                    yellow = 1;
                }
                if ((c.getSecond().getKey() >= 0 && c.getSecond().getKey() <= 2) || (c.getSecond().getKey() >= 6 && c.getSecond().getKey() <= 8)) {
                    top = 2;
                }
                if (second.equals(Color.WHITE) || second.equals(Color.YELLOW)) {
                    yellow = 2;
                }
                if ((c.getThird().getKey() >= 0 && c.getThird().getKey() <= 2) || (c.getThird().getKey() >= 6 && c.getThird().getKey() <= 8)) {
                    top = 3;
                }
                if (third.equals(Color.WHITE) || third.equals(Color.YELLOW)) {
                    top = 3;
                }
                if (top == yellow) {
                    return 0;
                }
                //lewo - 1 prawo 2
                if (top == 1 && yellow == 2) {
                    return 1;
                }
                if (top == 1 && yellow == 3) {
                    return 2;
                }
                if (top == 2 && yellow == 3) {
                    return 1;
                }
                if (top == 2 && yellow == 1) {
                    return 2;
                }
                if (top == 3 && yellow == 2) {
                    return 1;
                }
                return 2;
            }
            if (first.equals(two) && second.equals(three) && third.equals(one)) {
                actualCornerPos.set(cornerNumbers.get(c), correctCornerNumbers.get(new OrientationLessCorner(one, two, three)));
                //must have a white/yellow piece
                int yellow = 0;
                int top = 0;
                if ((c.getFirst().getKey() >= 0 && c.getFirst().getKey() <= 2) || (c.getFirst().getKey() >= 6 && c.getFirst().getKey() <= 8)) {
                    top = 1;
                }
                if (first.equals(Color.WHITE) || first.equals(Color.YELLOW)) {
                    yellow = 1;
                }
                if ((c.getSecond().getKey() >= 0 && c.getSecond().getKey() <= 2) || (c.getSecond().getKey() >= 6 && c.getSecond().getKey() <= 8)) {
                    top = 2;
                }
                if (second.equals(Color.WHITE) || second.equals(Color.YELLOW)) {
                    yellow = 2;
                }
                if ((c.getThird().getKey() >= 0 && c.getThird().getKey() <= 2) || (c.getThird().getKey() >= 6 && c.getThird().getKey() <= 8)) {
                    top = 3;
                }
                if (third.equals(Color.WHITE) || third.equals(Color.YELLOW)) {
                    yellow = 3;
                }
                if (top == yellow) {
                    return 0;
                }
                //lewo - 1 prawo 2
                if (top == 1 && yellow == 2) {
                    return 1;
                }
                if (top == 1 && yellow == 3) {
                    return 2;
                }
                if (top == 2 && yellow == 3) {
                    return 1;
                }
                if (top == 2 && yellow == 1) {
                    return 2;
                }
                if (top == 3 && yellow == 2) {
                    return 1;
                }
                return 2;
            }

            if (first.equals(three) && second.equals(one) && third.equals(two)) {
                actualCornerPos.set(cornerNumbers.get(c), correctCornerNumbers.get(new OrientationLessCorner(one, two, three)));
                //must have a white/yellow piece
                int yellow = 0;
                int top = 0;
                if ((c.getFirst().getKey() >= 0 && c.getFirst().getKey() <= 2) || (c.getFirst().getKey() >= 6 && c.getFirst().getKey() <= 8)) {
                    top = 1;
                }
                if (first.equals(Color.WHITE) || first.equals(Color.YELLOW)) {
                    yellow = 1;
                }
                if ((c.getSecond().getKey() >= 0 && c.getSecond().getKey() <= 2) || (c.getSecond().getKey() >= 6 && c.getSecond().getKey() <= 8)) {
                    top = 2;
                }
                if (second.equals(Color.WHITE) || second.equals(Color.YELLOW)) {
                    yellow = 2;
                }
                if ((c.getThird().getKey() >= 0 && c.getThird().getKey() <= 2) || (c.getThird().getKey() >= 6 && c.getThird().getKey() <= 8)) {
                    top = 3;
                }
                if (third.equals(Color.WHITE) || third.equals(Color.YELLOW)) {
                    top = 3;
                }
                if (top == yellow) {
                    return 0;
                }
                //lewo - 1 prawo 2
                if (top == 1 && yellow == 2) {
                    return 1;
                }
                if (top == 1 && yellow == 3) {
                    return 2;
                }
                if (top == 2 && yellow == 3) {
                    return 1;
                }
                if (top == 2 && yellow == 1) {
                    return 2;
                }
                if (top == 3 && yellow == 2) {
                    return 1;
                }
                return 2;
            }
        }
        return -1;
    }

    int findSignum(ArrayList<Integer> elems, int length) {
        boolean visited[] = new boolean[length];
        int sumOfLength = 0;
        for (int i = 0; i < length; i++) {
            int start = elems.get(i);
            int size = 1;
            if (!visited[start]) {
                int curr = elems.get(start);
                while (curr != start) {
                    visited[curr] = true;
                    size++;
                    curr = elems.get(curr);
                }
            }
            visited[start] = true;
            size--;
            sumOfLength += size;
        }
        return sumOfLength % 2;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(true);
        BorderPane layout = new BorderPane();
        stage.setScene(new Scene(layout, 1000, 800));
        this.layout = layout;
        GridPane rubics = draw();
        this.rubics = rubics;
        layout.setCenter(rubics);
        curCol = new ChoiceBox<>();
        ArrayList<String> colorNames = new ArrayList<>(Arrays.asList("Blue", "Green", "Red", "Orange", "White", "Yellow"));
        edges = new ArrayList<>();
        edgeNumbers = new HashMap<>();
        correctEdgeNumbers = new HashMap<>();
        edges.add(new Edge(new Pair(1, 3), new Pair(3, 1)));
        actualEdgePos.add(0);
        edgeNumbers.put(new Edge(new Pair(1, 3), new Pair(3, 1)), 0);
        correctEdgeNumbers.put(new OrientationLessEdge(Color.WHITE, Color.ORANGE), 0);
        edges.add(new Edge(new Pair(0, 4), new Pair(3, 10)));
        actualEdgePos.add(0);
        edgeNumbers.put(new Edge(new Pair(0, 4), new Pair(3, 10)), 1);
        correctEdgeNumbers.put(new OrientationLessEdge(Color.WHITE, Color.BLUE), 1);
        edges.add(new Edge(new Pair(1, 5), new Pair(3, 7)));
        actualEdgePos.add(0);
        edgeNumbers.put(new Edge(new Pair(1, 5), new Pair(3, 7)), 2);
        correctEdgeNumbers.put(new OrientationLessEdge(Color.RED, Color.WHITE), 2);
        edges.add(new Edge(new Pair(2, 4), new Pair(3, 4)));
        actualEdgePos.add(0);
        edgeNumbers.put(new Edge(new Pair(2, 4), new Pair(3, 4)), 3);
        correctEdgeNumbers.put(new OrientationLessEdge(Color.WHITE, Color.GREEN), 3);
        edges.add(new Edge(new Pair(4, 0), new Pair(4, 11)));
        actualEdgePos.add(0);
        edgeNumbers.put(new Edge(new Pair(4, 0), new Pair(4, 11)), 4);
        correctEdgeNumbers.put(new OrientationLessEdge(Color.ORANGE, Color.BLUE), 4);
        edges.add(new Edge(new Pair(5, 1), new Pair(7, 3)));
        actualEdgePos.add(0);
        edgeNumbers.put(new Edge(new Pair(5, 1), new Pair(7, 3)), 5);
        correctEdgeNumbers.put(new OrientationLessEdge(Color.ORANGE, Color.YELLOW), 5);
        edges.add(new Edge(new Pair(4, 2), new Pair(4, 3)));
        actualEdgePos.add(0);
        edgeNumbers.put(new Edge(new Pair(4, 2), new Pair(4, 3)), 6);
        correctEdgeNumbers.put(new OrientationLessEdge(Color.ORANGE, Color.GREEN), 6);
        edges.add(new Edge(new Pair(5, 4), new Pair(6, 4)));
        actualEdgePos.add(0);
        edgeNumbers.put(new Edge(new Pair(5, 4), new Pair(6, 4)), 7);
        correctEdgeNumbers.put(new OrientationLessEdge(Color.YELLOW, Color.GREEN), 7);
        edges.add(new Edge(new Pair(4, 5), new Pair(4, 6)));
        actualEdgePos.add(0);
        edgeNumbers.put(new Edge(new Pair(4, 5), new Pair(4, 6)), 8);
        correctEdgeNumbers.put(new OrientationLessEdge(Color.RED, Color.GREEN), 8);
        edges.add(new Edge(new Pair(5, 7), new Pair(7, 5)));
        actualEdgePos.add(0);
        edgeNumbers.put(new Edge(new Pair(5, 7), new Pair(7, 5)), 9);
        correctEdgeNumbers.put(new OrientationLessEdge(Color.RED, Color.YELLOW), 9);
        edges.add(new Edge(new Pair(4, 8), new Pair(4, 9)));
        actualEdgePos.add(0);
        edgeNumbers.put(new Edge(new Pair(4, 8), new Pair(4, 9)), 10);
        correctEdgeNumbers.put(new OrientationLessEdge(Color.RED, Color.BLUE), 10);
        edges.add(new Edge(new Pair(5, 10), new Pair(8, 4)));
        actualEdgePos.add(0);
        edgeNumbers.put(new Edge(new Pair(5, 10), new Pair(8, 4)), 11);
        correctEdgeNumbers.put(new OrientationLessEdge(Color.YELLOW, Color.BLUE), 11);
        corners = new ArrayList<>();
        correctCornerNumbers = new HashMap<>();
        cornerNumbers = new HashMap<>();
        corners.add(new Corner(new Pair<>(0, 3), new Pair<>(3, 11), new Pair<>(3, 0)));
        actualCornerPos.add(0);
        cornerNumbers.put(new Corner(new Pair(0, 3), new Pair(3, 11), new Pair(3, 0)), 0);
        correctCornerNumbers.put(new OrientationLessCorner(Color.WHITE, Color.BLUE, Color.ORANGE), 0);
        corners.add(new Corner(new Pair<>(0, 5), new Pair<>(3, 8), new Pair<>(3, 9)));
        actualCornerPos.add(0);
        cornerNumbers.put(new Corner(new Pair(0, 5), new Pair(3, 8), new Pair(3, 9)), 1);
        correctCornerNumbers.put(new OrientationLessCorner(Color.WHITE, Color.RED, Color.BLUE), 1);
        corners.add(new Corner(new Pair<>(2, 3), new Pair<>(3, 2), new Pair<>(3, 3)));
        actualCornerPos.add(0);
        cornerNumbers.put(new Corner(new Pair(2, 3), new Pair(3, 2), new Pair(3, 3)), 2);
        correctCornerNumbers.put(new OrientationLessCorner(Color.WHITE, Color.ORANGE, Color.GREEN), 2);
        corners.add(new Corner(new Pair<>(2, 5), new Pair<>(3, 5), new Pair<>(3, 6)));
        actualCornerPos.add(0);
        cornerNumbers.put(new Corner(new Pair(2, 5), new Pair(3, 5), new Pair(3, 6)), 3);
        correctCornerNumbers.put(new OrientationLessCorner(Color.WHITE, Color.GREEN, Color.RED), 3);
        corners.add(new Corner(new Pair<>(5, 0), new Pair<>(5, 11), new Pair<>(8, 3)));
        actualCornerPos.add(0);
        cornerNumbers.put(new Corner(new Pair(5, 0), new Pair(5, 11), new Pair(8, 3)), 4);
        correctCornerNumbers.put(new OrientationLessCorner(Color.YELLOW, Color.ORANGE, Color.BLUE), 4);
        corners.add(new Corner(new Pair<>(5, 2), new Pair<>(6, 3), new Pair<>(5, 3)));
        actualCornerPos.add(0);
        cornerNumbers.put(new Corner(new Pair(5, 2), new Pair(6, 3), new Pair(5, 3)), 5);
        correctCornerNumbers.put(new OrientationLessCorner(Color.YELLOW, Color.GREEN, Color.ORANGE), 5);
        corners.add(new Corner(new Pair<>(5, 5), new Pair<>(6, 5), new Pair<>(5, 6)));
        actualCornerPos.add(0);
        cornerNumbers.put(new Corner(new Pair(5, 5), new Pair(6, 5), new Pair(5, 6)), 6);
        correctCornerNumbers.put(new OrientationLessCorner(Color.YELLOW, Color.RED, Color.GREEN), 6);
        corners.add(new Corner(new Pair<>(8, 5), new Pair<>(5, 9), new Pair<>(5, 8)));
        actualCornerPos.add(0);
        cornerNumbers.put(new Corner(new Pair(8, 5), new Pair(5, 9), new Pair(5, 8)), 7);
        correctCornerNumbers.put(new OrientationLessCorner(Color.YELLOW, Color.BLUE, Color.RED), 7);
        colorNames.sort(Comparator.comparing(String::toString));
        curCol.getItems().addAll(colorNames);
        curCol.setValue(colorNames.get(0));
        layout.setTop(curCol);
        Button val = new Button();
        Button clean = new Button();
        Button reset = new Button();
        val.setOnMouseClicked(mouseEvent -> {
            System.out.println(validate());
        });
        clean.setOnMouseClicked(mouseEvent -> {
            clean();
        });
        reset.setOnMouseClicked(mouseEvent -> {
            reset();
        });
        val.setText("aaaaaa");
        clean.setText("clean");
        reset.setText("reset");
        VBox vb = new VBox();
        vb.getChildren().addAll(val, clean, reset);
        layout.setRight(vb);
        stage.show();
    }
}
