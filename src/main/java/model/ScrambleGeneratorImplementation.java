package model;

import java.util.ArrayList;
import java.util.Random;

public class ScrambleGeneratorImplementation implements ScrambleGenerator {
    private CubeType cubeType;
    private Random random;
    public ScrambleGeneratorImplementation(CubeType cubeType){
        this.cubeType = cubeType;
        random = new Random();
    }

    public void setCubeType(CubeType cubeType) {
        this.cubeType = cubeType;
    }
    public ArrayList<Move> generate() {
        switch (cubeType){
            case TWOBYTWO:
                return generateTwoByTwo(11);
            case THREEBYTHREE:
                return generateElse(20, 1);
            case FOURBYFOUR:
                return generateElse(40, 2);
        }
        return null;
    }

    private ArrayList<Move> generateTwoByTwo(int length){
        ArrayList<Move> scramble = new ArrayList<>();
        scramble.add(new Move(nextAxis(), nextDirection()));
        Move move;
        for(int i = 1; i<length; i++){
            move = new Move(nextAxis(), nextDirection());
            while(move.getAxis()==scramble.get(i-1).getAxis()){
                move = new Move(nextAxis(), nextDirection());
            }
            scramble.add(move);
        }
        return scramble;
    }
    private ArrayList<Move> generateElse(int length, int width){
        ArrayList<Move> scramble = new ArrayList<>();
        scramble.add(new Move(nextFace(), nextDirection(), random.nextInt(width)+1));
        Move move = new Move(nextFace(), nextDirection(), random.nextInt(width)+1);
        while(move.getFace()==scramble.get(0).getFace()){
            move = new Move(nextFace(), nextDirection(), random.nextInt(width)+1);
        }
        scramble.add(move);
        for(int i = 2; i<length;i++){
            move = new Move(nextFace(), nextDirection(), random.nextInt(width)+1);
            while(move.getFace()==scramble.get(i-1).getFace() || move.getAxis()==scramble.get(i-2).getAxis()){
                move = new Move(nextFace(), nextDirection(), random.nextInt(width)+1);
            }
            scramble.add(move);
        }
        return scramble;
    }

    private Axis nextAxis(){
        int a = random.nextInt(3);
        switch (a){
            case 0:
                return Axis.X;
            case 1:
                return Axis.Y;
            default:
                return Axis.Z;
        }
    }

    private Direction nextDirection(){
        int a = random.nextInt(3);
        switch (a){
            case 0:
                return Direction.ANTICLOCKWISE;
            case 1:
                return Direction.DOUBLE;
            default:
                return Direction.CLOCKWISE;
        }
    }

    private Face nextFace(){
        int a = random.nextInt(6);
        switch (a){
            case 0:
                return Face.U;
            case 1:
                return Face.D;
            case 2:
                return Face.R;
            case 3:
                return Face.L;
            case 4:
                return Face.B;
            default:
                return Face.F;
        }
    }
}
