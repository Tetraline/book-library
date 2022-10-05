package org.daniel;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtility {
    public static Gson gson = new Gson();

    public static User[] readUsers(String path) {
        try {
            JsonReader jr = new JsonReader(new FileReader(path));
            return gson.fromJson(jr, User[].class);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Book[] readBooks(String path){
        try {
            JsonReader jr = new JsonReader(new FileReader(path));
            return gson.fromJson(jr, Book[].class);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void write(Object[] data, String path) {
        try (FileWriter fw = new FileWriter(path)) {
            gson.toJson(data, fw);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
