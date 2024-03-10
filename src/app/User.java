package app;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;

public class User {
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
    
    public List<String> bookTicket(){
        List<String> list = new ArrayList<String>();

        return list;
    }
    
}
