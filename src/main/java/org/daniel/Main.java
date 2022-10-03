package org.daniel;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        User user = new User(2, "Geraldine Geralds", "American");
        Gson gson = new Gson();
        String filePath = "./data/data.json";

        try (FileWriter fw = new FileWriter("test.json")) {
            gson.toJson(user, fw);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}