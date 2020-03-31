package db;


import model.CubeType;
import model.DatabaseService;
import model.DatabaseServiceImplementation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class main {
    public static void main(String args[]) {
        DatabaseService db = new DatabaseServiceImplementation();
        db.dropDatabase();
        db.start();
        db.insertIntoThreeByThree();
        db.pullAndParseAllSolves(CubeType.THREEBYTHREE);
    }
}