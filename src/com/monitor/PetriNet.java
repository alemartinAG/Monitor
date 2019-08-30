package com.monitor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class PetriNet {

    private static final int FIM = 0; // Forwards Incidence Matrix (I+)
    private static final int BIM = 1; // Backwards Incidence Matrix (I-)
    private static final int CIM = 2; // Combined Incidence Matrix (I)
    private static final int INM = 3; // Inhibition Matrix (H)
    private static final int MRK = 4; // Marking
    private static final int ETR = 5; // Enabled Transitions

    private static final String PETRI = "res/petri.html";

    private int nPlaces = 0;
    private int nTransitions = 0;

    private ArrayList<String> tableList;
    private Integer[][] combinedIMatrix;
    private Integer[][] backwardMatrix;
    private Integer[][] forwardMatrix;
    private Integer[][] inhibitionMatrix;
    private Integer[]   currentMarking;
    private boolean[]   enabledTransitions;

    public PetriNet(){

        // Parseo el archivo .html de las matrices de la red de petri generado por PIPE
        tableList = getTableList(PETRI);

        // Cuento la cantidad de plazas y transiciones
        countPlacesAndTransitions();

        /* Obtengo las matrices de enteros a partir de las lineas de texto generadas previamente */
        combinedIMatrix     =   parseMatrix(tableList.get(CIM), nPlaces, nTransitions);
        backwardMatrix      =   parseMatrix(tableList.get(BIM), nPlaces, nTransitions);
        forwardMatrix       =   parseMatrix(tableList.get(FIM), nPlaces, nTransitions);
        inhibitionMatrix    =   parseMatrix(tableList.get(INM), nPlaces, nTransitions);
        currentMarking      =   parseMatrix(tableList.get(MRK), nPlaces);

        System.out.print("Combined Incidence Matrix\n");
        printMatrix(combinedIMatrix);
        System.out.print("Backwards Incidence Matrix\n");
        printMatrix(backwardMatrix);
        System.out.print("Current Marking\n");
        printMatrix(currentMarking);

        System.out.print("Enabled Transitions: ");
        enabledTransitions = areEnabled();
        for(int i=0; i<enabledTransitions.length; i++){
            System.out.printf(" | T%d: %s | ", i+1, enabledTransitions[i]);
        }
        System.out.print("\n\n");

    }

    public boolean trigger(){
        return false;
    }

    private void printMatrix(Object[][] matrix){

        System.out.print("\n");

        for(int i=0; i<matrix.length; i++){
            for(int j=0; j<matrix[i].length; j++){
                System.out.printf("%3s ",matrix[i][j].toString());
            }
            System.out.println("");
        }

        System.out.print("\n");

    }

    private void printMatrix(Object[] matrix){

        System.out.print("\n");

        for(int i=0; i<matrix.length; i++){
            System.out.printf("%3s ",matrix[i].toString());
        }

        System.out.print("\n\n");

    }

    public boolean[] areEnabled(){

        boolean[] enabledTransitions = new boolean[nTransitions];

        Arrays.fill(enabledTransitions, true);

        for(int t=0; t<nTransitions; t++){
            for(int p=0; p<nPlaces; p++){
                if (backwardMatrix[p][t] > this.currentMarking[p]) {
                    enabledTransitions[t] = false;
                    break;
                }
            }
        }

        //TODO: ver matriz de inhibición

        return enabledTransitions;

    }

    private ArrayList<String> getTableList(String petriFile){

        File input = new File(petriFile);

        Document doc = null;
        Elements tables = null;
        ArrayList<String> list = new ArrayList<>();

        try {
            doc = Jsoup.parse(input, "UTF-8", "");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            tables = doc.getElementsByTag("table");
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }

        int nTable = 0;
        for(Element table : tables){

            if(nTable%2 == 0){
                list.add(table.text());
            }

            nTable++;
        }

        return  list;

    }

    private void countPlacesAndTransitions(){

        int p = 0;
        int t = 0;

        Pattern pattern = Pattern.compile("P\\d");
        Matcher matcher = pattern.matcher(tableList.get(FIM));

        while (matcher.find()) {
            p++;
        }

        pattern = Pattern.compile("T\\d");
        matcher = pattern.matcher(tableList.get(FIM));

        while (matcher.find()){
            t++;
        }

        nPlaces = p;
        nTransitions = t;

    }

    private Integer[][] parseMatrix(String plainText, int rows, int columns){

        Integer[][] matrix = new Integer[rows][columns];

        Pattern pattern = Pattern.compile("\\s-?\\d+");
        Matcher matcher = pattern.matcher(plainText);

        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                matcher.find();
                matrix[i][j] = Integer.parseInt(matcher.group().trim());
            }
        }

        return matrix;
    }

    private Integer[] parseMatrix(String plainText, int columns){

        Integer[] matrix = new Integer[columns];

        Pattern pattern = Pattern.compile("\\s-?\\d+");
        Matcher matcher = pattern.matcher(plainText);

        for(int j=0; j<columns; j++){
            matcher.find();
            matrix[j] = Integer.parseInt(matcher.group().trim());
        }

        return matrix;

    }

}
