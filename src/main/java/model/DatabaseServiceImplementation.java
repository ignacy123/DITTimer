package model;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseServiceImplementation implements DatabaseService {
    Connection c;
    Statement stmt;
    PreparedStatement pstmt;
    ResultSet rs;
    public void start() {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver" );
            c = DriverManager.getConnection("jdbc:hsqldb:file:testdb;shutdown=true", "SA", "");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
            System.out.println("Couldn't open db");
        }
        System.out.println("Opened database successfully");

        try {
            DatabaseMetaData meta = c.getMetaData();
            ResultSet tables = meta.getTables(null, null, "THREEBYTHREE", null);
            if(tables.next()){
                System.out.println("Table THREEBYTHREE exists, skipping creation.");
            }else{
                stmt = c.createStatement();
                String sql = "CREATE TABLE THREEBYTHREE " +
                        "(ID INT PRIMARY KEY     NOT NULL," +
                        " TIME          time    NOT NULL, " +
                        " STATE            INT     NOT NULL, " +
                        " COMMENT        CHAR(50), " +
                        " SCRAMBLE       CHAR(200)     NOT NULL," +
                        " DATE         TIMESTAMP NOT NULL)";
                stmt.executeUpdate(sql);
                System.out.println("Table THREEBYTHREE has been created");

            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed creating table THREEBYTHREE");
        }

        try {
            DatabaseMetaData meta = c.getMetaData();
            ResultSet tables = meta.getTables(null, null, "FOURBYFOUR", null);
            if(tables.next()){
                System.out.println("Table FOURBYFOUR exists, skipping creation.");
            }else{
                stmt = c.createStatement();
                String sql = "CREATE TABLE FOURBYFOUR " +
                        "(ID INT PRIMARY KEY     NOT NULL," +
                        " TIME          time    NOT NULL, " +
                        " STATE            INT     NOT NULL, " +
                        " COMMENT        CHAR(50), " +
                        " SCRAMBLE       CHAR(200)     NOT NULL," +
                        " DATE         TIMESTAMP NOT NULL)";
                stmt.executeUpdate(sql);
                System.out.println("Table FOURBYFOUR has been created");

            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed creating table FOURBYFOUR");
        }

        try {
            DatabaseMetaData meta = c.getMetaData();
            ResultSet tables = meta.getTables(null, null, "TWOBYTWO", null);
            if(tables.next()){
                System.out.println("Table TWOBYTWO exists, skipping creation.");
            }else{
                stmt = c.createStatement();
                String sql = "CREATE TABLE TWOBYTWO " +
                        "(ID INT PRIMARY KEY     NOT NULL," +
                        " TIME          time    NOT NULL, " +
                        " STATE            INT     NOT NULL, " +
                        " COMMENT        CHAR(50), " +
                        " SCRAMBLE       CHAR(200)     NOT NULL," +
                        " DATE         TIMESTAMP NOT NULL)";
                stmt.executeUpdate(sql);
                System.out.println("Table TWOBYTWO has been created");

            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed creating table TWOBYTWO");
        }
    }

    public void insertIntoThreeByThree(){
        try {
            String sql = "INSERT INTO THREEBYTHREE (ID, TIME, STATE, COMMENT, SCRAMBLE, DATE) " +
                    " Values (?, ?, ?, ?, ?, ?)";
            pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, 1);
            pstmt.setTime(2, new Time(1000));
            pstmt.setInt(3, 1);
            pstmt.setString(4, "test comment");
            pstmt.setString(5, new ScrambleGeneratorImplementation(CubeType.THREEBYTHREE).generate().toString());
            pstmt.setDate(6, Date.valueOf(java.time.LocalDate.now()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //0 - TWOBYTWO
    //1 - THREEBYTHREE
    //2 - FOURBYFOUR
    @Override
    public void insert(Solve solve) {
        try {
            String sql = "INSERT INTO THREEBYTHREE (ID, TIME, STATE, COMMENT, SCRAMBLE, DATE) " +
                    " Values (?, ?, ?, ?, ?, ?)";
            pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, 1);
            pstmt.setTime(2, new Time(1000));
            pstmt.setInt(3, 1);
            pstmt.setString(4, "test comment");
            pstmt.setString(5, new ScrambleGeneratorImplementation(CubeType.THREEBYTHREE).generate().toString());
            pstmt.setDate(6, Date.valueOf(java.time.LocalDate.now()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void dropDatabase() {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver" );
            c = DriverManager.getConnection("jdbc:hsqldb:file:testdb;shutdown=true", "SA", "");
            stmt = c.createStatement();
            stmt.execute("DROP SCHEMA PUBLIC CASCADE");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Solve> pullAndParseAllSolves(CubeType cubeType) {
        ArrayList<Solve> solves = new ArrayList<>();
        try {
            stmt = c.createStatement();
            rs = stmt.executeQuery("SELECT * FROM THREEBYTHREE");
            while(rs.next()){
                Solve solve = new SolveImplementation();
                solve.setID(rs.getInt(1));
                solve.setTime(rs.getTime(2));
                switch (rs.getInt(3)){
                    case 0:
                        solve.setType(CubeType.TWOBYTWO);
                        break;
                    case 1:
                        solve.setType(CubeType.THREEBYTHREE);
                        break;
                    default:
                        solve.setType(cubeType.FOURBYFOUR);
                }
                solve.setDate(rs.getDate(6));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return solves;
    }

    public ArrayList<Solve> pullAndParseSolves(CubeType cubeType, int size) {
        return null;
    }
}
