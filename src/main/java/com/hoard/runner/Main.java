package com.hoard.runner;

import com.hoard.entity.Videogame;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static void main(String[] args){
        System.out.println("My beefs!");

        Videogame gamer = new Videogame(1,"email@email.com","The Adventures of Pupps","Noopendo",
                "SNES",false,false,false);

        String licker = gamer.toString();
        System.out.println(licker);

        //DBConnection myConn = new DBConnection("192.168.0.6", 3306, "HOARD", "nthorp", "nopat");
        DBConnection myConn = new DBConnection("73.65.12.173", 3306, "HOARD", "nthorp", "nopat");

        Statement statement = null;
        PreparedStatement preparedStatement = null;
        try {
            String sql = "INSERT INTO hoard_videogame (videogame_id, user_email, videogame_title, videogame_developer, "
                    + "videogame_is_played, videogame_is_playing, videogame_is_complete) VALUES (?, ?, ?, ?, ?, ?, ?) "
                    + "ON DUPLICATE KEY UPDATE " +"user_email=?, videogame_title=?, videogame_developer=?, " +
                    "videogame_is_played=?, videogame_is_playing=?, videogame_is_complete=?";
            preparedStatement = myConn.getConn().prepareStatement(sql);
            preparedStatement.setInt(1, gamer.getId());
            preparedStatement.setString(2, gamer.getEmail());
            preparedStatement.setString(3, gamer.getTitle());
            preparedStatement.setString(4, gamer.getDeveloper());
            preparedStatement.setBoolean(5, gamer.getIsPlayed());
            preparedStatement.setBoolean(6, gamer.getIsPlaying());
            preparedStatement.setBoolean(7, gamer.getIsComplete());
            preparedStatement.setString(8, gamer.getEmail());
            preparedStatement.setString(9, gamer.getTitle());
            preparedStatement.setString(10, gamer.getDeveloper());
            preparedStatement.setBoolean(11, gamer.getIsPlayed());
            preparedStatement.setBoolean(12, gamer.getIsPlaying());
            preparedStatement.setBoolean(13, gamer.getIsComplete());

            int result = preparedStatement.executeUpdate();

            //STEP 5: Extract data from result set
            System.out.println("Number of rows inserted: " + result);
            //STEP 6: Clean-up environment
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement = myConn.getConn().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM hoard_videogame;");

            //STEP 5: Extract data from result set
            while(rs.next()){
                //Retrieve by column name
                int vg_id = rs.getInt("videogame_id");
                String user_email  = rs.getString("user_email");
                String vg_title = rs.getString("videogame_title");
                String vg_developer = rs.getString("videogame_developer");
                boolean vg_is_played = rs.getBoolean("videogame_is_played");
                boolean vg_is_playing = rs.getBoolean("videogame_is_playing");
                boolean vg_is_complete = rs.getBoolean("videogame_is_complete");

                //Display values
                System.out.print("Id: " + vg_id);
                System.out.print(", Email: " + user_email);
                System.out.print(", Title: " + vg_title);
                System.out.print(", Developer: " + vg_developer);
                System.out.print(", Is Played: " + vg_is_played);
                System.out.print(", Is Playing: " + vg_is_playing);
                System.out.println(", Is Complete: " + vg_is_complete);
            }
            //STEP 6: Clean-up environment
            rs.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}