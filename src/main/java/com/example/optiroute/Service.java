package com.example.optiroute;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class Service {

    String[] listOfPoints = new String[]{"Wroclaw", "warszawa", "szczecin","olsztyn", "bialystok", "gdansk", "krakow","lublin", "suwalki"};

    public Service() {
    }

    public Service(String[] listOfPoints) {
        this.listOfPoints = listOfPoints;
    }

    //-------------------------------------------------------------
    public static void main(String[] args) throws IOException {
        Service service = new Service();
        String[] listOfPoints = service.listOfPoints;
        String[]sortedTab =  service.calculateRoad(listOfPoints);
        service.applicationReadyEvent(sortedTab);
    }

    public String[] calculateRoad(String[] listOfPoints) throws IOException {
        this.listOfPoints = listOfPoints;
        String[] sortedTab = new String[listOfPoints.length+1];
        DistanceTable distanceTable = new DistanceTable();
        int[][] distanceMatrix= distanceTable.getTab();
        int size = listOfPoints.length;
        int start = 0;

        int visited[] = new int[size + 1];
        for (int i = 0; i < visited.length; i++) {
            visited[i] = Integer.MAX_VALUE;
        }
        visited[0] = start;
        visited[size] = start;

        int lower;
        int nextLine = start;
        int[] road = new int[size + 1];
        for (int i = 0; i < size; i++) {
            lower = Integer.MAX_VALUE;
            for (int j = 0; j < size; j++) {
                boolean isVisited = false;
                for (int k = 0; k < size; k++) {
                    if (j == visited[k]) {
                        isVisited = true;
                    }
                }
                if (!isVisited && lower > distanceMatrix[nextLine][j]) {
                    lower = distanceMatrix[nextLine][j];
                    visited[i + 1] = j;
                    road[i + 1] = lower;
                }
            }
            nextLine = visited[i + 1];
        }

        int distanceTraveled = 0;
        road[size] = distanceMatrix[visited[size - 1]][start];//Ostatni element drogi: powrót do miejsca startowego
        System.out.println();
        System.out.println("Najkrótsza droga to: ");
        for (int i = 0; i < size + 1; i++) {
            System.out.print(" " + listOfPoints[visited[i]] + " " + road[i] + "-->");
            distanceTraveled += road[i];
            sortedTab[i]=listOfPoints[visited[i]];
        }
        System.out.println("Przebyta droga to: " + distanceTraveled + " KM");
        return sortedTab;
    }


    void applicationReadyEvent(String[] sortedMap) {
        System.out.println("Application started ... launching browser now");

        StringBuilder sB = new StringBuilder();
        sB.append("https://www.google.pl/maps/dir/");
        for(int i = 0; i< sortedMap.length; i++) {
            sB.append(sortedMap[i]+"/");
        }
        String roadMap = sB.toString();
        browse(roadMap);
    }

    public static void browse(String url) {
        if(Desktop.isDesktopSupported()){
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }else{
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
