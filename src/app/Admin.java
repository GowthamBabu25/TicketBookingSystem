package app;

import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;

public class Admin {
    public int selectChoice() {
        Scanner sin = new Scanner(System.in);
        System.out.println("1. Add Theater");
        System.out.println("2. Add Movie");
        System.out.println("3. Exit");
        System.out.println("Enter your choice:");
        int ch = sin.nextInt();
        sin.nextLine();
        return ch;
    }
    
    @SuppressWarnings("unchecked")
    public void addTheater(Connection conn, HashMap<String, Object> theaterData) {
        int theatreId=0;
        //System.out.println(" is of type " + ((Object)theaterData.get("address")).getClass().getSimpleName()); 
        String fullAddress = buildFullAddress((StringMap<String>) theaterData.get("address"));
        List<StringMap<Object>> screens = (List<StringMap<Object>>) theaterData.get("screens");
        try{
            String insertQuery = "INSERT INTO Theatres (TheatreName, NumberOfScreens, Address) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, (String) theaterData.get("theatreName"));
            preparedStatement.setInt(2, ((Double)theaterData.get("numberOfScreens")).intValue());
            preparedStatement.setString(3, fullAddress);
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                theatreId = generatedKeys.getInt(1);
            } else {
                System.out.println("Failed to retrieve TheatreID for the inserted theatre.");
            }
            for(StringMap<Object> screenMap : screens){
                addScreen(screenMap,conn,theatreId);
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    private String buildFullAddress(StringMap<String> addressMap) {
        return (String)addressMap.get("streetAddress") + " " + (String)addressMap.get("city") + " " + (String)addressMap.get("state") + " " + (String)addressMap.get("postalCode");
    }

    public void addScreen(StringMap<Object> screenData, Connection conn, int theatreId){
        try{
            String insertQuery = "INSERT INTO Screens (ScreenNo, TheatreId, TotalSeats, NumberOfRows, NumberOfColumns, SoundSystem) VALUES (?,?,?,?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, ((Double)screenData.get("screenNo")).intValue());
            preparedStatement.setInt(2, theatreId);
            preparedStatement.setInt(3, ((Double)screenData.get("totalSeats")).intValue());
            preparedStatement.setInt(4, ((Double)screenData.get("numberOfRows")).intValue());
            preparedStatement.setInt(5, ((Double)screenData.get("numberOfColumns")).intValue());
            preparedStatement.setString(6, (String)screenData.get("soundSystem"));
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int screenId = generatedKeys.getInt(1);
            } else {
                System.out.println("Failed to retrieve TheatreID for the inserted theatre.");
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    public void addMovie(Connection conn, HashMap<String, Object> movieData) {
        int movieId=0;;
        try{
            String insertQuery = "INSERT INTO Movies (Title, Duration, Genre, Language, TheatreId, ScreenId) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, (String) movieData.get("title"));
            preparedStatement.setInt(2, ((Double)movieData.get("duration")).intValue());
            preparedStatement.setString(3, (String) movieData.get("genre"));
            preparedStatement.setString(4, (String) movieData.get("language"));
            preparedStatement.setInt(5, ((Double)movieData.get("theatreId")).intValue());
            preparedStatement.setInt(6, ((Double)movieData.get("screenId")).intValue());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                movieId = generatedKeys.getInt(1);
            } else {
                System.out.println("Failed to retrieve TheatreID for the inserted theatre.");
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }
}
