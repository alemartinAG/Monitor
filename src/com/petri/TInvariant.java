package com.petri;

import com.errors.IllegalPetriStateException;
import com.util.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TInvariant extends Invariant {

    private final static String LOGPATH = "res/log.txt";
    private final static String INVPATH = "res/t-invariantes.txt";

    private ArrayList<ArrayList<Integer>> stateList;

    public TInvariant(){
        parseInvariants(INVPATH);
        stateList = parseLogStates();
    }

    public TInvariant(String file){
        parseInvariants(file);
        stateList = parseLogStates();
    }


    /* Chequea que tras disparar las transiciones restantes, luego de remover las pertenecientes a una
         invariante de transición, de manera ordenada, el marcado (o estado) de la red,
         sea el mismo en el que se encontraba tras disparar la última transición de la simulación */
    @Override
    public boolean checkInvariants(Integer[] initialState) throws IllegalPetriStateException {

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

        PetriNet petriTest = new PetriNet();

        while (matcher.find()) {
            int partial = Integer.parseInt(matcher.group().replaceAll("T", ""));
            petriTest.trigger(partial-1);
        }

//        System.out.println(Arrays.toString(petriTest.getCurrentMarking()));
//        System.out.println(Arrays.toString(stateList.get(stateList.size()-1).toArray()));

        //TODO: ANDA DE VEZ EN CUANDO, VER COMO ES REALMENTE
        if(Arrays.equals(stateList.get(stateList.size()-1).toArray(), petriTest.getCurrentMarking())){
            return true;
        }
        else {
            throw new IllegalPetriStateException("T-Invariants are not met");
        }
    }

    /* Se encarga de parsear los estados del log */
    private ArrayList<ArrayList<Integer>> parseLogStates(){
        return new Parser(LOGPATH, "\\d+", "[", "]").getParsedElements();
    }
}