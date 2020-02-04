package com.petri;

import com.errors.DeadlockStateException;
import com.errors.OutsideWindowException;
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

import com.util.Parser;

public class PetriNet {

    public static final int FIM = 0; // Forwards Incidence Matrix (I+)
    public static final int BIM = 1; // Backwards Incidence Matrix (I-)
    public static final int CIM = 2; // Combined Incidence Matrix (I)
    public static final int INM = 3; // Inhibition Matrix (H)
    public static final int RST = 4; // Inhibition Matrix (H)
    public static final int RDR = 5; // Inhibition Matrix (H)
    public static final int MRK = 6; // Marking
    public static final int ETR = 7; // Enabled Transitions

    public static final int INITIAL = 0;
    public static final int CURRENT = 1;

    private static final String PETRI = "res/tiempotest.html"; // matrices file's path
    private static final String TIMED = "res/timed-transitions.txt";

    private int nPlaces = 0; // number of places in the petri net
    private int nTransitions = 0; // number of transitions in the petri net

    private ArrayList<String> tableList;
    private Integer[][] combinedIMatrix;
    private Integer[][] backwardMatrix;
    private Integer[][] forwardMatrix;
    private Integer[][] inhibitionMatrix;
    private Integer[] initialMarking;
    private Integer[] currentMarking;

    private Time[] timedTransitions;

    public PetriNet(boolean timed) {

        // Parseo el archivo .html de las matrices de la red de petri generado por PIPE
        tableList = getTableList(PETRI);

        // Cuento la cantidad de plazas y transiciones
        countPlacesAndTransitions();

        /*
         * Obtengo las matrices de enteros a partir de las lineas de texto generadas
         * previamente
         */
        setMatrices();

        try {
            checkMarking();
        } catch (DeadlockStateException e) {
            e.printStackTrace();
        }

        // Arreglo de tiempos, con tamaño = Cant. de transiciones
        timedTransitions = new Time[nTransitions];
        Arrays.fill(timedTransitions, null);

        if(timed){
            // Obtengo transiciones con tiempo
            setTimedTransitions(TIMED);
        }

    }

    public PetriNet(String petri_file, String timed_file) {

        // Parseo el archivo .html de las matrices de la red de petri generado por PIPE
        tableList = getTableList(petri_file);

        // Cuento la cantidad de plazas y transiciones
        countPlacesAndTransitions();

        /*
         * Obtengo las matrices de enteros a partir de las lineas de texto generadas
         * previamente
         */
        setMatrices();

        // Arreglo de tiempos, con tamaño = Cant. de transiciones
        timedTransitions = new Time[nTransitions];
        Arrays.fill(timedTransitions, null);

        // Obtengo transiciones con tiempo
        setTimedTransitions(timed_file);

    }

    private void checkMarking() throws DeadlockStateException {

        for (Integer tokens : currentMarking) {
            if (tokens > 0) {
                return;
            }
        }

        throw new DeadlockStateException("The net does not have any token in it's initial marking");
    }

    /* Se encarga de disparar una transicion y actualizar el marcado de la red */
    public boolean trigger(int transition) throws OutsideWindowException {

        /*
         * Si la transición a disparar no se encuentra sensibilizada devuelvo falso
         */
        if (!isEnabled(transition)) {
            return false;
        }

        Time timed = timedTransitions[transition];

        if (timed != null ) {

            if(!timed.testTimeWindow()){
                //No esta dentro de la ventana
                timed.setWaiting();
                throw new OutsideWindowException(timed.beforeWindow(), timed.getSleepTime());
            }

            timed.resetWaiting();
            System.out.printf("Tiempo de espera de T%d: %d [ms]\n", transition, timed.getElapsedTime());

        }

        /* Genero vector delta para calcular función de transferencia */
        Integer[] delta = new Integer[nTransitions];
        Arrays.fill(delta, 0);
        delta[transition] = 1;

        Integer[] tf = new Integer[nPlaces];
        Arrays.fill(tf, 0);

        /* Cálculo del nuevo marcado a partir de la ecuación de estado */
        for (int i = 0; i < nPlaces; i++) {
            for (int j = 0; j < nTransitions; j++) {
                // Funcion de transferencia = I x delta
                tf[i] += combinedIMatrix[i][j] * delta[j];
            }
            // M(i+1) = M(i) + I * delta
            currentMarking[i] = currentMarking[i] + tf[i];
        }

        // La transicion se disparo exitosamente
        return true;
    }

    /* Debugging, imprime la sensibilizacion de las transiciones */
    protected void printEnabled() {
        System.out.print("Enabled Transitions: ");
        boolean[] enabledTransitions = areEnabled();
        for (int i = 0; i < enabledTransitions.length; i++) {
            System.out.printf(" | T%d: %s | ", i + 1, enabledTransitions[i]);
        }
        System.out.print("\n\n");
    }

    /* Debugging, impime el marcado actual */
    protected void printCurrentMarking() {
        System.out.println("Current Marking");
        printMatrix(currentMarking);
    }

    /* Devuelve el marcado inicial de la red */
    public Integer[] getInitialMarking() {
        return initialMarking;
    }

    /* Devuelve el marcado actual de la red */
    public Integer[] getCurrentMarking() {
        return currentMarking;
    }

    /* Devuelve la matriz especificada */
    public Integer[][] getMatrix(int index) {

        Integer[][] def = { { 0 }, { 0 } };
        Integer[][] marking = { initialMarking.clone(), currentMarking.clone() };

        switch (index) {
        case FIM:
            return forwardMatrix;
        case BIM:
            return backwardMatrix;
        case CIM:
            return combinedIMatrix;
        case INM:
            return inhibitionMatrix;
        case MRK:
            return marking;
        default:
            return def;
        }
    }

    /* Devuelve el numero de plazas de la red */
    public int getPlacesCount() {
        return nPlaces;
    }

    /* Devuelve el numero de transiciones de la red */
    public int getTransitionsCount() {
        return nTransitions;
    }

    /* Método para imprimir matrices, utilizado en el debugging */
    public void printMatrix(Object[][] matrix) {

        System.out.print("\n");

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.printf("%3s ", matrix[i][j].toString());
            }
            System.out.println("");
        }

        System.out.print("\n");

    }

    /* Método para imprimir matrices, utilizado en el debugging */
    public void printMatrix(Object[] matrix) {

        System.out.print("\n");

        for (int i = 0; i < matrix.length; i++) {
            System.out.printf("%3s ", matrix[i].toString());
        }

        System.out.print("\n\n");

    }

    private boolean isEnabled(int transition) {
        return areEnabled()[transition];
    }

    /* Se encarga de calcular qué transiciones se encuentran sensibilizadas */
    public boolean[] areEnabled() {

        boolean[] enabledTransitions = new boolean[nTransitions];

        Arrays.fill(enabledTransitions, true);

        for (int t = 0; t < nTransitions; t++) {
            for (int p = 0; p < nPlaces; p++) {

                // Chequeo si las plazas tienen los tokens suficientes
                if (backwardMatrix[p][t] > this.currentMarking[p]) {
                    enabledTransitions[t] = false;
                    break;
                }

                // Chequeo si hay un arco inhibidor
                if(inhibitionMatrix[p][t] > 0 && this.currentMarking[p] > 0){
                    enabledTransitions[t] = false;
                    break;
                }

            }
            if(enabledTransitions[t] && timedTransitions[t] != null && !timedTransitions[t].isWaiting()){
                timedTransitions[t].setNewTimeStamp();
            }
        }

        return enabledTransitions;

    }

    private void setTimedTransitions(String file) {

        // Creo lista de arreglos con N° de transición->[0], Alpha->[1], Beta->[2]
        ArrayList<ArrayList<Integer>> timed = new Parser(file, "\\d+", "(", ")").getParsedElements();

        // Agrego las transiciones con tiempo al arreglo, si el archivo está vacío no se agrega ninguna
        for (ArrayList<Integer> transition : timed) {
            timedTransitions[transition.get(0)-1] = new Time(transition.get(1), transition.get(2));
        }

    }

    public Time[] getTimedTransitions(){ return timedTransitions; }


    /* Se encarga de inicializar las matrices */
    private void setMatrices() {
        combinedIMatrix = parseMatrix(tableList.get(CIM), nPlaces, nTransitions);
        backwardMatrix = parseMatrix(tableList.get(BIM), nPlaces, nTransitions);
        forwardMatrix = parseMatrix(tableList.get(FIM), nPlaces, nTransitions);
        inhibitionMatrix = parseMatrix(tableList.get(INM), nPlaces, nTransitions);
        initialMarking = parseMatrix(tableList.get(MRK), nPlaces);
        currentMarking = initialMarking.clone();
    }

    /* Se encarga de parsear el html que contiene las tablas */
    private ArrayList<String> getTableList(String petriFile) {

        File input = new File(petriFile);

        Document doc = null;
        Elements tables = null;
        ArrayList<String> list = new ArrayList<>();

        try {
            doc = Jsoup.parse(input, "UTF-8", "");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            tables = doc.getElementsByTag("table");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        int nTable = 0;
        for (Element table : tables) {

            if (nTable % 2 == 0) {
                list.add(table.text());
            }

            nTable++;
        }

        return list;

    }

    /* Se encarga de contar la cantidad de plazas y transiciones de la red */
    private void countPlacesAndTransitions() {

        int p = 0;
        int t = 0;

        Pattern pattern = Pattern.compile("P\\d");
        Matcher matcher = pattern.matcher(tableList.get(FIM));

        while (matcher.find()) {
            p++;
        }

        pattern = Pattern.compile("T\\d");
        matcher = pattern.matcher(tableList.get(FIM));

        while (matcher.find()) {
            t++;
        }

        nPlaces = p;
        nTransitions = t;

    }

    /* Se encarga de generar matrices (2d) a partir de un String */
    private Integer[][] parseMatrix(String plainText, int rows, int columns) {

        Integer[][] matrix = new Integer[rows][columns];

        // Busco lineas que empiecen con un numero (puede ser negativo)
        Pattern pattern = Pattern.compile("\\s-?\\d+");
        Matcher matcher = pattern.matcher(plainText);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (matcher.find())
                    matrix[i][j] = Integer.parseInt(matcher.group().trim());
            }
        }

        return matrix;
    }

    /* Se encarga de generar matrices (1d) a partir de un String */
    private Integer[] parseMatrix(String plainText, int columns) {

        Integer[] matrix = new Integer[columns];

        Pattern pattern = Pattern.compile("\\s-?\\d+");
        Matcher matcher = pattern.matcher(plainText);

        for (int j = 0; j < columns; j++) {
            if (matcher.find())
                matrix[j] = Integer.parseInt(matcher.group().trim());
        }

        return matrix;

    }

}
