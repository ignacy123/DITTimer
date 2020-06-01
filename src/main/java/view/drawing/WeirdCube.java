package view.drawing;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Shear;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import model.enums.CubeType;
import model.enums.Direction;
import model.enums.Face;
import model.logic.Move;
import model.logic.MoveImplementation;
import model.logic.ScrambleGeneratorImplementation;
import model.logic.Solve;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class WeirdCube extends Application {
    Random rand = new Random();
    Color[][] cube = new Color[9][12];
    Rotator3 rotator3 = new Rotator3(cube);
    HashMap<String, String> cases = new HashMap<>();
    public void populateStandard() {
        int n = 3;
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
    GridPane layout;
    public void update() {
        GridPane top = (GridPane) layout.getChildren().get(0);
        GridPane left = (GridPane) layout.getChildren().get(1);
        GridPane right = (GridPane) layout.getChildren().get(2);
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                ((Rectangle)top.getChildren().get(3*j+i)).setFill(cube[i][3+j]);
                ((Rectangle)left.getChildren().get(3*j+i+1)).setFill(cube[3+i][3+j]);
                ((Rectangle)right.getChildren().get(3*j+i)).setFill(cube[3+i][6+j]);
            }
        }
    }
    @FXML
    private Button leftB;

    @FXML
    private Button rightB;

    @FXML
    private Button startB;

    @FXML
    private Button downLeftB;

    @FXML
    private Button downRightB;

    @FXML
    void chooseLeftDown(ActionEvent event) {
        if(correct == 2) {
            downLeftB.setStyle("-fx-background-color: #dcf4c4");
        }
        else {
            getButton(correct).setStyle("-fx-background-color: #dcf4c4");
            downLeftB.setStyle("-fx-background-color: #fcc4d4");
        }
        transformButtons(true);
    }
    @FXML
    void chooseRightD(ActionEvent event) {
        if(correct == 3) {
            downRightB.setStyle("-fx-background-color: #dcf4c4");
        }
        else {
            getButton(correct).setStyle("-fx-background-color: #dcf4c4");
            downRightB.setStyle("-fx-background-color: #fcc4d4");
        }
        transformButtons(true);
    }

    public void doRev(String wh, boolean ran) {
        ArrayList<Move> mvs = new ScrambleGeneratorImplementation(CubeType.THREEBYTHREE).fromString(wh);
        Collections.reverse(mvs);
        ArrayList<Move> rdy=new ArrayList<>();
        for(Move cello: mvs) {
            Direction dr=Direction.CLOCKWISE;
            if(cello.getDirection() == Direction.CLOCKWISE) dr=Direction.ANTICLOCKWISE;
            else if(cello.getDirection()==Direction.DOUBLE) dr=Direction.DOUBLE;
            //fc, dir, width
            rdy.add(new MoveImplementation(cello.getFace(), dr, cello.getWidth()));
        }
        if(ran) {
            int upt=rand.nextInt(4);
            for(int i = 0; i < upt; i++) {
                rdy.add(new MoveImplementation(Face.U, Direction.CLOCKWISE, 1));
            }
        }

        rotator3.executeMoves(rdy);
        update();
    }
    void transformButtons(boolean disable) {//true for disable
        for(int i = 0; i < 4; i++) {
            getButton(i).setMouseTransparent(disable);
        }
    }
    int correct;
    Button getButton(int i) {
        switch (i) {
            case 0: return leftB;
            case 1: return rightB;
            case 2: return downLeftB;
            case 3: return downRightB;
        }
        return null;
    }
    ArrayList<Integer> getN(int n, ArrayList<Character> pref) { //3 different than n
        ArrayList<Integer> holder= new ArrayList<>();
        while(holder.size() < 3) {
            Integer l = rand.nextInt(cases.size());
            String al= (String) cases.keySet().toArray()[l];
            if(l.equals(n) || holder.contains(l) || (al.length() > 1 && pref.contains(al.charAt(0)))) continue;
            holder.add(l);
            if(al.length() > 1) pref.add(al.charAt(0));
        }
        return holder;
    }
    @FXML
    void startGm(ActionEvent event) {
        leftB.setStyle("");
        rightB.setStyle("");
        downRightB.setStyle("");
        downLeftB.setStyle("");
        transformButtons(false);
        populateStandard();
        //set random id
        int id= rand.nextInt(cases.size());//id of right alg
        String right = (String) cases.keySet().toArray()[id];
        doRev(cases.get(right), true);
        int okButton=rand.nextInt(4);
        correct=okButton;
        ArrayList<Character> prefs = new ArrayList<>();
        prefs.add(right.charAt(0));
        //get 3 random different algs
        ArrayList<Integer> algs = getN(id, prefs);

        //fill other buttons
        for(int i = 0; i < 4; i++) {
            if(i == okButton) {
                algs.add(i, 0);
                continue;
            }
            Button on = getButton(i);
            on.setText((String) cases.keySet().toArray()[algs.get(i)]);

        }
        getButton(correct).setText(right);
    }
    //correct = 0 means left is ok
    @FXML
    void chooseLeft(ActionEvent event) {
        if(correct == 0) {
            leftB.setStyle("-fx-background-color: #dcf4c4");
        }
        else {
            getButton(correct).setStyle("-fx-background-color: #dcf4c4");
            leftB.setStyle("-fx-background-color: #fcc4d4");
        }
        transformButtons(true);
    }

    @FXML
    void chooseRight(ActionEvent event) {
        if(correct == 1) {
            rightB.setStyle("-fx-background-color: #dcf4c4");
        }
        else {
            getButton(correct).setStyle("-fx-background-color: #dcf4c4");
            rightB.setStyle("-fx-background-color: #fcc4d4");
        }
        transformButtons(true);
    }
    void inito(FXMLLoader loader,Stage stage, GridPane layout) throws IOException {
        loader.setController(this);
        Pane root = loader.load();
        root.getChildren().add(layout);
        this.layout=layout;
        Scene scene = new Scene(root, 700, 700);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("css0.css").toExternalForm());
        stage.setScene(scene);
        layout.setLayoutY(200);
        layout.setLayoutX(100);
        //top
        GridPane top = new GridPane();
        Shear wool = new Shear();
        top.setStyle("-fx-background-color: black ; -fx-vgap: 1; -fx-hgap: 1; -fx-padding: 1;");
        wool.setX(-0.3);
        wool.setY(-0.3);
        //wool.setPivotX(100);
        top.setRotate(45);
        top.getTransforms().add(wool);
        Translate tra = new Translate();
        tra.setX(68);
        tra.setY(-10);
        top.getTransforms().add(tra);
        Scale scale = new Scale();
        scale.setY(0.765);
        scale.setX(0.765);
        scale.setPivotX(110);
        scale.setPivotY(155);
        top.getTransforms().add(scale);
        layout.add(top, 2, 2);
        //left one
        GridPane pane = new GridPane();

        pane.setStyle("-fx-background-color: black ; -fx-vgap: 1; -fx-hgap: 1; -fx-padding: 1;");
        pane.setGridLinesVisible(true);
        layout.add(pane, 2, 3);
        Shear sh = new Shear();

        sh.setY(0.5);
        pane.getTransforms().add(sh);
        Translate tr = new Translate();
        tr.setX(37);
        tr.setY(-62);
        pane.getTransforms().add(tr);
        Scale sca = new Scale();
        sca.setX(0.7);
        pane.getTransforms().add(sca);

        //right
        GridPane pane2=new GridPane();
        pane2.setStyle("-fx-background-color: black ; -fx-vgap: 1; -fx-hgap: 1; -fx-padding: 1;");
        Shear sh2 = new Shear();
        sh2.setY(-0.5);
        pane2.getTransforms().add(sh2);
        pane2.getTransforms().add(sca);
        Translate tro = new Translate();
        tro.setX(-1);
        pane2.getTransforms().add(tro);
        //layout.setGridLinesVisible(true);
        //pane2.set
        //pane2.set
        layout.add(pane2, 3, 3);
        populateStandard();
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Rectangle rec = new Rectangle(40, 40);
                rec.setFill(cube[3+i][3+j]);
                pane.add(rec, i, j);

                Rectangle rec2 = new Rectangle(40, 40);
                rec2.setFill(cube[3+i][6+i]);
                pane2.add(rec2, i, j);

                Rectangle rec3 = new Rectangle(40, 40);
                rec3.setFill(cube[i][3+j]);
                top.add(rec3, i, j);
            }
        }
        update();
        stage.show();
    }
    @Override
    public void start(Stage stage) throws Exception {


        //gen for cases
        //Aa
        cases.put("Aa", "R' F R' B2 R F' R' B2 R2");
        //Ab ??
        cases.put("Ab", "R2 B2 R F R' B2 R F' R");
        //E
        cases.put("E", "R2 U R2 U D R2 U' R2 U R2 U' D' R2 U R2 U2 R2");
        //F
        cases.put("F", "R' U R U' R2 F' U' F U R F R' F' R2");
        //Ga
        cases.put("Ga", "D' R2 U R' U R' U' R U' R2 U' D R' U R");
        //Gb
        cases.put("Gb", "R' U' R U D' R2 U R' U R U' R U' R2 D");
        //Gc
        cases.put("Gc", "R2 U' R U' R U R' U R2 D' U R U' R' D");
        //Gd
        cases.put("Gd", "D' R U R' U' D R2 U' R U' R' U R' U R2");
        //H
        cases.put("H", "R2 U2 R U2 R2 U2 R2 U2 R U2 R2");
        //Ja
        cases.put("Ja", "L' U' L F L' U' L U L F' L2 U L");
        //Jb
        cases.put("Jb", "R U R' F' R U R' U' R' F R2 U' R'");
        //Na
        cases.put("Na", "L U' R U2 L' U R' L U' R U2 L' U R'");
        //Nb
        cases.put("Nb", "R' U L' U2 R U' L R' U L' U2 R U' L");
        //Ra
        cases.put("Ra","L U2 L' U2 L F' L' U' L U L F L2");
        //Rb
        cases.put("Rb", "R' U2 R U2 R' F R U R' U' R' F' R2");
        //T
        cases.put("T", "R U R' U' R' F R2 U' R' U' R U R' F'");
        //Ua
        cases.put("Ua", "R2 U' R' U' R U R U R U' R");
        //Ub
        cases.put("Ub", "R' U R' U' R' U' R' U R U R2");
        //V ????
        cases.put("V", "R U2 R' D R U' R U' R U R2 D R' U' R D2");
        //Y
        cases.put("Y", "F R U' R' U' R U R' F' R U R' U' R' F R F'");
        //Z
        cases.put("Z", "R U R' U R' U' R' U R U' R' U' R2 U R");
        GridPane layout = new GridPane();
        FXMLLoader loader=new FXMLLoader(getClass().getClassLoader().getResource("WeirdCube.fxml"));
        inito(loader, stage, layout);
        for(int i = 0; i < 4; i++) {
            getButton(i).getTransforms().add(new Translate(0, 200));
        }
        startB.fire();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
