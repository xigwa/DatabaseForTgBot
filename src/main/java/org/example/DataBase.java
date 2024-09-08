package org.example;

import java.io.*;
import java.util.ArrayList;

public class DataBase implements IDB {
    private static final String finalName = "database.txt";
    private static DataBase instance;

    private DataBase() {

    }

    public static DataBase getInstance() {
        if (instance == null)
            instance = new DataBase();
        return instance;
    }
    public static void writTofile(String fileName, String text) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))){
            writer.write(text+ "\n");
            System.out.println("Text written to " + fileName);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void saveData(String data) {
        writTofile(finalName, data);
    }

    @Override
    public ArrayList<String> loadData() {
        ArrayList<String> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(finalName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return list;
    }
    @Override
    public void deleteData(String what) {
        ArrayList<String> list = loadData();
        list.removeIf(line -> line.equals(what));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(finalName))) {
            for (String line : list) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void updateData(String what, String to) {
    ArrayList<String> list = loadData();
    String replace = "";
    for (String s : list) {
        if (s.equals(what)) {
            replace = to;
            list.remove(s);
            list.add(replace);
            break;
        }
    }
    }
}
