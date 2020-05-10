package model.logic;

import model.enums.Axis;
import model.enums.CubeType;
import model.enums.Direction;
import model.enums.Face;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

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

    @Override
    public String scrambleToString(ArrayList<Move> list) {
        String string = "";
        StringBuilder sb = new StringBuilder(string);
        for(int i = 0; i<list.size();i++){
            sb.append(list.get(i));
            sb.append(" ");
        }
        sb.deleteCharAt(sb.lastIndexOf(" "));
        return sb.toString();
    }

    @Override
    public ArrayList<Move> fromString(String scramble) {
        Scanner in = new Scanner(scramble);
        ArrayList<Move> toRet = new ArrayList<>();
        while(in.hasNext()){
            String next = in.next();
            int width = 0;
            Direction direction;
            Face face = null;
            Move move;
            if(!Character.isLetter(next.charAt(0))){
                width = next.charAt(0)-'0';
                next = next.substring(1);
            }else{
                width = 1;
            }
            switch(Character.toLowerCase(next.charAt(0))){
                case 'f':
                    face = Face.F;
                    break;
                case 'b':
                    face = Face.B;
                    break;
                case 'u':
                    face = Face.U;
                    break;
                case 'd':
                    face = Face.D;
                    break;
                case 'r':
                    face = Face.R;
                    break;
                case 'l':
                    face = Face.L;
                    break;

            }
            next = next.substring(1);
            if(next.length()==0){
                direction = Direction.CLOCKWISE;
                move = new MoveImplementation(face, direction, width);
                toRet.add(move);
                continue;
            }
            if(next.charAt(0)=='w'){
                next = next.substring(1);
                if(width==1){
                    width=2;
                }
            }
            if(next.length()==0){
                direction = Direction.CLOCKWISE;
                move = new MoveImplementation(face, direction, width);
                toRet.add(move);
                continue;
            }
            if(next.substring(0, 1).equals("'")){
                direction = Direction.ANTICLOCKWISE;
                move = new MoveImplementation(face, direction, width);
                toRet.add(move);
                continue;
            }
            if(next.charAt(0)=='2'){
                direction = Direction.DOUBLE;
                move = new MoveImplementation(face, direction, width);
                toRet.add(move);
                continue;
            }
        }
        return toRet;
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
        scramble.add(new MoveImplementation(nextAxis(), nextDirection()));
        MoveImplementation move;
        for(int i = 1; i<length; i++){
            move = new MoveImplementation(nextAxis(), nextDirection());
            while(move.getAxis()==scramble.get(i-1).getAxis()){
                move = new MoveImplementation(nextAxis(), nextDirection());
            }
            scramble.add(move);
        }
        return scramble;
    }
    private ArrayList<Move> generateElse(int length, int width){
        ArrayList<Move> scramble = new ArrayList<>();
        MoveImplementation move = new MoveImplementation(nextFace(), nextDirection(), random.nextInt(width)+1);
        while((width>1 && move.getWidth()==width && move.getFace()==Face.L) || (width>1 && move.getWidth()==width && move.getFace()==Face.B) || (width>1 && move.getWidth()==width && move.getFace()==Face.D)){
            move = new MoveImplementation(nextFace(), nextDirection(), random.nextInt(width)+1);
        }
        scramble.add(move);
        while(move.getFace()==scramble.get(0).getFace() || (width>1 && move.getWidth()==width && move.getFace()==Face.L) || (width>1 && move.getWidth()==width && move.getFace()==Face.B) || (width>1 && move.getWidth()==width && move.getFace()==Face.D)){
            move = new MoveImplementation(nextFace(), nextDirection(), random.nextInt(width)+1);
        }
        scramble.add(move);
        for(int i = 2; i<length;i++){
            move = new MoveImplementation(nextFace(), nextDirection(), random.nextInt(width)+1);
            while(move.getFace()==scramble.get(i-1).getFace() || move.getAxis()==scramble.get(i-2).getAxis() || (width>1 && move.getWidth()==width && move.getFace()==Face.L) || (width>1 && move.getWidth()==width && move.getFace()==Face.B) || (width>1 && move.getWidth()==width && move.getFace()==Face.D)){
                move = new MoveImplementation(nextFace(), nextDirection(), random.nextInt(width)+1);
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
