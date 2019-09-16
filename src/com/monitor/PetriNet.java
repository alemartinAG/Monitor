package com.monitor;

import com.errors.IllegalTriggerException;

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

    public static final int FIM = 0; // Forwards Incidence Matrix (I+)
    public static final int BIM = 1; // Backwards Incidence Matrix (I-)
    public static final int CIM = 2; // Combined Incidence Matrix (I)
    public static final int INM = 3; // Inhibition Matrix (H)
    public static final int RST = 4; // Inhibition Matrix (H)
    public static final int RDR = 5; // Inhibition Matrix (H)
    public static final int MRK = 6; // Marking
    public static final int ETR = 7; // Enabled Transitions

    private static final String PETRI = "res/petri.html"; // matrices file's path

    private int nPlaces = 0;        // number of places in the petri net
    private int nTransitions = 0;   // number of transitions in the petri net

    private ArrayList<String> tableList;
    private Integer[][] combinedIMatrix;
    private Integer[][] backwardMatrix;
    private Integer[][] forwardMatrix;
    private Integer[][] inhibitionMatrix;
    private Integer[]   initalMarking;
    private Integer[]   currentMarking;

    public PetriNet(){

        // Parseo el archivo .html de las matrices de la red de petri generado por PIPE
        tableList = getTableList(PETRI);

        // Cuento la cantidad de plazas y transiciones
        countPlacesAndTransitions();

        /* Obtengo las matrices de enteros a partir de las lineas de texto generadas previamente */
        setMatrices();

    }

    public PetriNet(String file){

        // Parseo el archivo .html de las matrices de la red de petri generado por PIPE
        tableList = getTableList(file);

        // Cuento la cantidad de plazas y transiciones
        countPlacesAndTransitions();

        /* Obtengo las matrices de enteros a partir de las lineas de texto generadas previamente */
        setMatrices();

    }

    /* Se encarga de disparar una transicion y actualizar el marcado de la red */
    public boolean trigger(int transition) {

        /* Si la transición a disparar no se encuentra
        sensibilizada devuelvo falso */
        if(!areEnabled()[transition]){
            return false;
        }

        /* Genero vector delta para calcular función de transferencia */
        Integer[] delta = new Integer[nTransitions];
        Arrays.fill(delta, 0);
        delta[transition] = 1;

        Integer[] tf = new Integer[nPlaces];
        Arrays.fill(tf, 0);

        /* Cálculo del nuevo marcado a partir de la ecuación de estado */
        for(int i=0; i<nPlaces; i++){
            for(int j=0; j<nTransitions; j++){
                // Funcion de transferencia = I x delta
                tf[i] += combinedIMatrix[i][j] * delta[j];
            }
            // M(i+1) = M(i) + I * delta
            currentMarking[i] = currentMarking[i]+tf[i];
        }

        //La transicion se disparo exitosamente
        return true;
    }

    /* Debugging, imprime la sensibilizacion de las transiciones */
    protected void printEnabled(){
        System.out.print("Enabled Transitions: ");
        boolean[] enabledTransitions = areEnabled();
        for(int i=0; i<enabledTransitions.length; i++){
            System.out.printf(" | T%d: %s | ", i+1, enabledTransitions[i]);
        }
        System.out.print("\n\n");
    }

    /* Debugging, impime el marcado actual */
    protected void printCurrentMarking(){
        System.out.println("Current Marking");
        printMatrix(currentMarking);
    }

    /* Devuelve la matriz especificada */
    public Integer[][] getMatrix(int index){

        Integer[][] def = {{0},{0}};

        switch (index){
            case FIM: return forwardMatrix;
            case BIM: return backwardMatrix;
            case CIM: return combinedIMatrix;
            case INM: return inhibitionMatrix;
             default: return def;
        }
    }

    /* Devuelve el numero de plazas de la red */
    public int getPlacesCount(){
        return nPlaces;
    }

    /* Devuelve el numero de transiciones de la red */
    public int getTransitionsCount(){
        return nTransitions;
    }

    /* Método para imprimir matrices, utilizado en el debugging */
    public void printMatrix(Object[][] matrix){

        System.out.print("\n");

        for(int i=0; i<matrix.length; i++){
            for(int j=0; j<matrix[i].length; j++){
                System.out.printf("%3s ",matrix[i][j].toString());
            }
            System.out.println("");
        }

        System.out.print("\n");

    }

    /* Método para imprimir matrices, utilizado en el debugging */
    public void printMatrix(Object[] matrix){

        System.out.print("\n");

        for(int i=0; i<matrix.length; i++){
            System.out.printf("%3s ",matrix[i].toString());
        }

        System.out.print("\n\n");

    }

    /* Se encarga de calcular qué transiciones se encuentran sensibilizadas */
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

    /* Se encarga de inicializar las matrices */
    private void setMatrices(){
        combinedIMatrix     =   parseMatrix(tableList.get(CIM), nPlaces, nTransitions);
        backwardMatrix      =   parseMatrix(tableList.get(BIM), nPlaces, nTransitions);
        forwardMatrix       =   parseMatrix(tableList.get(FIM), nPlaces, nTransitions);
        inhibitionMatrix    =   parseMatrix(tableList.get(INM), nPlaces, nTransitions);
        initalMarking       =   parseMatrix(tableList.get(MRK), nPlaces);
        currentMarking      =   initalMarking.clone();
    }

    /* Se encarga de parsear el html que contiene las tablas */
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

    /* Se encarga de contar la cantidad de plazas y transiciones de la red */
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

    /* Se encarga de generar matrices (2d) a partir de un String */
    private Integer[][] parseMatrix(String plainText, int rows, int columns){

        Integer[][] matrix = new Integer[rows][columns];

        Pattern pattern = Pattern.compile("\\s-?\\d+");
        Matcher matcher = pattern.matcher(plainText);

        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                if(matcher.find())
                matrix[i][j] = Integer.parseInt(matcher.group().trim());
            }
        }

        return matrix;
    }

    /* Se encarga de generar matrices (1d) a partir de un String */
    private Integer[] parseMatrix(String plainText, int columns){

        Integer[] matrix = new Integer[columns];

        Pattern pattern = Pattern.compile("\\s-?\\d+");
        Matcher matcher = pattern.matcher(plainText);

        for(int j=0; j<columns; j++){
            if(matcher.find())
            matrix[j] = Integer.parseInt(matcher.group().trim());
        }

        return matrix;

    }

}
