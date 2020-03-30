package model;

public class main {
    public static void main(String[] args) {
        ScrambleGenerator scr = new ScrambleGeneratorImplementation(CubeType.TWOBYTWO);
        System.out.println("scr.generate()");
    }

}
