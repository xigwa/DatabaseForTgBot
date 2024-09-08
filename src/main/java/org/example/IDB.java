package org.example;

import java.util.ArrayList;

public interface IDB {
    void saveData(String data);

    ArrayList<String> loadData();

    void deleteData(String data);

    void updateData(String what, String to);


}
