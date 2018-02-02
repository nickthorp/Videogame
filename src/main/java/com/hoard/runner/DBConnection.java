package com.hoard.runner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String DB_URL_PREFIX = "jdbc:mysql://";
    //    private static String USER = "nthorp";
//    private static String PASS = "nopat";
    private String URI;
    private int port;
    private String DBName;
    private String USER;
    private String PASS;

    private Connection conn = null;

    public DBConnection(String URI, int port, String DBName, String USER, String PASS){
        this.URI = URI;
        this.port = port;
        this.DBName = DBName;
        this.USER = USER;
        this.PASS = PASS;

        try{
            //String dbURL = DB_URL_PREFIX + URI + ":" + port + "/" + DBName;
            String dbURL = String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s", URI, port, DBName, USER, PASS);
            System.out.println("Connecting to a database...");
            //conn = DriverManager.getConnection(dbURL, USER, PASS);
            conn = DriverManager.getConnection(dbURL);

        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            System.out.println("Uh oh!");
            e.printStackTrace();
        }finally {
            //finally block used to close resources
            try {
                if (conn == null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }
    }

    public Connection getConn() {
        return conn;
    }

    public void closeConnection(){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
