package com.example.optiroute;


import java.net.URL;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStreamReader;
public class DistanceTable {

    int[][] tab = new int[9][9];
    String[] listOfPoints = new String[]{"Wroclaw", "warszawa", "szczecin","olsztyn", "bialystok", "gdansk", "krakow","lublin", "suwalki"};

    public DistanceTable() throws IOException {
        this.tab = distanceTab(listOfPoints);
    }

    public int[][] distanceTab(String[] listOfPoints) throws IOException {
        this.listOfPoints = listOfPoints;
        int[][] distanceTab = new int[9][9];

        for (int i = 0; i < distanceTab.length; i++) {
            for (int j = 0; j < distanceTab[i].length; j++) {
                distanceTab[i][j] = calculateDistance(listOfPoints[i], listOfPoints[j]);
            }
        }
        return distanceTab;
    }

    public int calculateDistance(String x, String y) throws IOException {
        if(x.equals(y)){
            int distance = 100000;
            System.out.println("distance "+distance + " "+ x + "-" + y);
            return distance;
        }

        URL dist = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?origins="+x+"&destinations="+y+"&key=AIzaSyDRdc0rfJc72y6ypdGFe0u6BpKfk30hIg8");
        InputStreamReader googleUrl = new InputStreamReader(dist.openStream());
        JsonObject jsonObject = new JsonParser().parse(googleUrl).getAsJsonObject();
        String a =jsonObject.toString();
        int index = a.indexOf("text") + 7;
        String piece = a.substring(index).substring(0, 3);
        int distance = Integer.parseInt(piece);
        System.out.println("distance "+distance + " "+ x + "-" + y);
        return distance;
    }

    public int[][] getTab() {
        return tab;
    }
}