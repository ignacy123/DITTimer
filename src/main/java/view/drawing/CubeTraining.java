package view.drawing;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.enums.CubeType;
import model.logic.Move;
import model.logic.ScrambleGeneratorImplementation;

import java.util.*;

public class CubeTraining extends WeirdCube {
    int old;
    @Override
    public void populateStandard() {
        super.populateStandard();
        for(int i = 0; i < 12; i++) {
            cube[3][i]=Color.DIMGRAY;
        }
    }
    @FXML
    private Button playButton;

    @FXML
    private ListView<String> list;

    @FXML
    private Text executed;

    @FXML
    private Text algName;
    @FXML
    void game(ActionEvent event) {
        int rando = rand.nextInt(alg.size())+1;
        while(rando == old) {
            rando = rand.nextInt(alg.size())+1;
        }
        old=rando;
        String name = "WV "+rando;
        System.out.println(name);
        algName.setText(name);
        populateStandard();
        String toExecute= alg.get(name).get(0);
        ArrayList<Move> to = doRev(toExecute, false);
        executed.setText(new ScrambleGeneratorImplementation(CubeType.THREEBYTHREE).scrambleToString(to));
        list.getItems().clear();
        list.getItems().setAll(alg.get(name));
        update();
    }
    HashMap<String, List<String>> alg=new HashMap<>();


    @Override
    public void start(Stage stage) throws Exception {

        alg.put("WV 1", Arrays.asList("U L' U2 R U R' U2 L", "U' L' U2 R U' R' U2 L","y' U r D r' U' r D' r'", "R2 D R' U R D' R2 U R U' R'"));

        alg.put("WV 2", Collections.singletonList("U R U' R'"));

        alg.put("WV 3", Arrays.asList("R' F R U R U' R' F'",
                "U' L U' R U L' U R'"));

        alg.put("WV 4", Collections.singletonList("U R2 D R' U' R D' R2"));
        alg.put("WV 5", Arrays.asList("U R U' R' U R' U' R U' R' U2 R",
                "U R U' R' U2 R U2 R' U' R U' R'",
                "U2 R2 B R' U' R' U R B' U2 R'",
                "U R U' R' U R' U L U' R U L'"));


        alg.put("WV 6", Arrays.asList("R U' R' U2 R U' R' U2 R U R'",
                "U' R' D' R U2 R' D R2 U' R'"));

        alg.put("WV 7", Arrays.asList("U R U R' U' R U' R'",
                "R' U' R U R U' R' U' R' U R"));

        alg.put("WV 8", Collections.singletonList("U2 R U' R' U R U2 R'"));

        alg.put("WV 9", Arrays.asList("U2 F' R U2 R' U2 R' F R",
                "U L' U2 R U L U' L' U R' U2 L",
                "U2 F2 R U2 R' U2 R' F2 R",
                "U2 L' R U R' U' R U R' U' L U' R U' R'"));

        alg.put("WV 10", Arrays.asList("R' F R2 U R' U' R U R' U' F'",
                "U R U R2 U' R2 U' R2 U2 R",
                "U2 F2 L' U L U L' U' L F2"));

        alg.put("WV 11", Collections.singletonList("U2 R' U' R2 U' R2 U2 R"));

        alg.put("WV 12", Arrays.asList("R' F2 R F2 U L' U L",
                "U R U2 L U L' R' U2 L U L'"));

        alg.put("WV 13", Arrays.asList("U2 R U2 R2 U' R U' R' U2 R",
                "R' F R F' R' U' F' U F R",
                "U' F2 L' U L U' L' U' L F2"));

        alg.put("WV 14", Arrays.asList("U2 R2 D R' U2 R D' R2",
                "U2 L' U R U' L U2 R'"));

        alg.put("WV 15", Arrays.asList("L' U R U' R' L",
                "r' F R F' M'",
                "L' U R U' M' x'"));

        alg.put("WV 16", Arrays.asList("U2 L' R U R' U' L R U2 R'",
                "U R' D' R U R' D R2 U2 R'",
                "U2 R U' R' U' R' F R U R U' R' F'",
                "U2 M x U R' U' L R U2 R'"));

        alg.put("WV 17", Arrays.asList("R' F' R U2 R U2 R' F",
                "U L' U2 R U' R' U2 L U R U' R'",
                "U2 F2 L F2 L' U' L' U L"));

        alg.put("WV 18", Collections.singletonList("U2 R U2 R'"));

        alg.put("WV 19", Arrays.asList("R' F2 R2 U' R' U' R U R' F2",
                "U2 L' U R U' R' L U' R U' R'"));

        alg.put("WV 20", Arrays.asList("U R U' R' U' R U R' U R U2 R'",
                "U L' U2 L R U R' U L' U L"));

        alg.put("WV 21", Arrays.asList("U R U' R2 U2 R U R' U R",
                "U2 R U' R D R' U' R D' R2"));

        alg.put("WV 22", Arrays.asList("U R U R D R' U2 R D' R2",
                "U R2 D R' U R D' R' U2 R'"));

        alg.put("WV 23", Arrays.asList("R2 U R' U R' U' R U R U2 R2",
                "U2 R U R' U' R U R D R' U R D' R2",
                "U2 R' U L U' R2 U L' U R'",
                "U2 R U' R' U L' U R U' R' L U' R U' R'"));

        alg.put("WV 24", Collections.singletonList("U2 R U' R' U R U' R' U R U2 R'"));

        alg.put("WV 25", Arrays.asList("U2 R U2 R2 U2 R U R' U R",
                "U2 R U' R' U R U' R D R' U' R D' R2"));

        alg.put("WV 26", Arrays.asList("U R U' R2 U' R U' R' U2 R",
                "U R U R' U F2 L' U L U' L' U' L F2"));

        alg.put("WV 27", Collections.singletonList("U R U R' U' R U R' U' R U' R'"));


        GridPane layout = new GridPane();
        FXMLLoader loader=new FXMLLoader(getClass().getClassLoader().getResource("CubeTraining.fxml"));
        inito(loader, stage, layout);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
