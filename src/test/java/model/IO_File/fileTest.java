package model.IO_File;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class fileTest {
    @Test
    public void testFound(){
        try {
            Scanner scanner = new Scanner(new File("src/main/resources/testIOFILE"));
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testRead(){
        try {
            Scanner scanner = new Scanner(new File("src/main/resources/testIOFILE"));
            while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void ParseToSolve(){
        try {
            Scanner scanner = new Scanner(new File("src/main/resources/testIOFILE"));
            while (scanner.hasNextLine()) {
                String[] array=scanner.nextLine().split(":");
                for(String a : array) System.out.println(a);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
