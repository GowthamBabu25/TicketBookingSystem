package app;

import java.util.HashMap;
import java.util.Scanner;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import com.google.gson.Gson;

public class App {
    @SuppressWarnings("unchecked")
    public static HashMap<String, Object> readJsonFile(String jsonFilePath) {
        Gson gson = new Gson();
        HashMap<String, Object> hashMap = new HashMap<>();

        try (Reader reader = new FileReader(jsonFilePath)) {
            // Convert JSON to HashMap
            hashMap = gson.fromJson(reader, HashMap.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hashMap;
    }
    public static void main(String[] args) throws Exception {
        int ch;
        String loc;
        Scanner sin = new Scanner(System.in);
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ticketbookingsystem","root","root");
            while (true) {
                System.out.println("+-------------------------------------------------------+");
                System.out.println("|         Welcome to Movie Ticket Booking System        |");
                System.out.println("+-------------------------------------------------------+");
                System.out.println("|\t\t 1. Login \t\t\t\t|");
                System.out.println("|\t\t 2. Signup \t\t\t\t|");
                System.out.println("|\t\t 3. Exit \t\t\t\t|");
                System.out.println("+-------------------------------------------------------+");
                System.out.print("Enter Your Choice: ");
                ch = sin.nextInt();
                sin.nextLine();
                if (ch == 1) {
                    try{
                        System.out.println("Login Page");
                        System.out.print("Enter UserName: ");
                        String userName = sin.nextLine();
                        System.out.print("Enter Password: ");
                        String passWord = sin.nextLine();
                        String queryString = "SELECT password FROM admins WHERE username = ? AND password = ?";
                        PreparedStatement preparedStatement = conn.prepareStatement(queryString);
                        preparedStatement.setString(1, userName);
                        preparedStatement.setString(2, passWord);
                        ResultSet resultSet = preparedStatement.executeQuery();
                        if(resultSet.next()){
                            Admin ad = new Admin();
                            int adChoice = ad.selectChoice();
                            if(adChoice==1){
                                loc = "json\\createTheatre.json";
                                ad.addTheater(conn, readJsonFile(loc));
                            }
                            else if(adChoice==2){
                                loc = "json\\addMovie.json";
                                System.out.println(readJsonFile(loc));
                                ad.addMovie(conn, readJsonFile(loc));
                            }
                        }
                        else{
                            System.out.println("Wrong Credentials");
                        }
                    }
                    catch(SQLException ex){
                        ex.printStackTrace();;
                    }
                }
                else if (ch == 2) {
                    System.out.println("Signup Page");
                }
                else if(ch == 3){
                    break;
                }          
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}