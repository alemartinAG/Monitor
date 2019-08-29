package com.monitor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class PetriNet {

    private static final int FIM = 0; // Forwards Incidence Matrix (I+)
    private static final int BIM = 1; // Backwards Incidence Matrix (I-)
    private static final int CIM = 2; // Combined Incidence Matrix (I)
    private static final int INM = 3; // Inhibition Matrix (H)
    private static final int MRK = 4; // Marking
    private static final int ETR = 5; // Enabled Transitions

    private int nPlaces = 0;
    private int nTransitions = 0;

    private ArrayList<String> tableList;
    private Integer[][] combinedIMatrix;

    public PetriNet(){

        File input = new File("/home/ale/Desktop/p-c.html"); //get env $HOME

        Document doc = null;
        Elements tables = null;
        tableList = new ArrayList<>();

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
                tableList.add(table.text());
            }

            nTable++;
        }

        countPT();

        combinedIMatrix = parseMatrix(tableList.get(CIM), nPlaces, nTransitions);

        for(int i=0; i<combinedIMatrix.length; i++){
            for(int j=0; j<combinedIMatrix[i].length; j++){
                System.out.printf("%3d ",combinedIMatrix[i][j]);
            }
            System.out.println("");
        }


    }

    private void countPT(){

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

}
