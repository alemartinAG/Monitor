package com.petri;

import com.util.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TInvariant extends Invariant {

    final static String LOGPATH = "res/log.txt";
    final static String INVPATH = "res/t-invariantes.txt";

    private ArrayList<ArrayList<Integer>> stateList;

    private ArrayList<Integer> partialInvariants;

    public TInvariant(){
        parseInvariants(INVPATH);
        partialInvariants = new ArrayList<>();
        stateList = parseLogStates();
    }

    public TInvariant(String file){
        parseInvariants(file);
        partialInvariants = new ArrayList<>();
        stateList = parseLogStates();
    }


    /* TODO:   Chequea que tras disparar las transiciones de la invariante, de manera ordenada,
        el marcado (o estado) de la red, sea el mismo en el que se encontraba antes de
        disparar la primer transicion del conjunto */
    @Override
    public boolean checkInvariants(Integer[] initialState) {

        String data = "";

        try {
            data = new String(Files.readAllBytes(Paths.get(LOGPATH)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(ArrayList<Integer> invariant : invariantList){

            String pattern = "";
            String replace = "";

            for(int i=0; i<invariant.size()-1; i++) {
                pattern += String.format("(T%d)([\\S\\s]+?)", invariant.get(i));
                replace += String.format("$%d", i*2+2);
            }

            pattern += String.format("(T%d)", invariant.get(invariant.size()-1));

            data = data.replaceAll(pattern, replace);

        }

        Pattern pattern = Pattern.compile("(T\\d+)");
        Matcher matcher = pattern.matcher(data);

        PetriNet petri = new PetriNet();

        while (matcher.find()) {
            int partial = Integer.parseInt(matcher.group().replaceAll("T", ""));
            partialInvariants.add(partial);
            petri.trigger(partial-1);
        }

        System.out.println(Arrays.toString(petri.getCurrentMarking()));
        System.out.println(Arrays.toString(stateList.get(stateList.size()-1).toArray()));

        //TODO: Parse log final state
        return Arrays.equals(stateList.get(stateList.size()-1).toArray(), petri.getCurrentMarking());
    }

    /* Se encarga de parsear los estados del log */
    private ArrayList<ArrayList<Integer>> parseLogStates(){
        return new Parser(LOGPATH, "\\d+", "[", "]").getParsedElements();
    }
}
