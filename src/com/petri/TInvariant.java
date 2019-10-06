package com.petri;

import com.util.Parser;

import java.util.ArrayList;
import java.util.Arrays;

public class TInvariant extends Invariant {

    public TInvariant(){
        parseInvariants("res/t-invariantes.txt");
    }

    public TInvariant(String file){
        parseInvariants(file);
    }

    /* Chequea que tras disparar las transiciones de la invariante, de manera ordenada,
        el marcado (o estado) de la red, sea el mismo en el que se encontraba antes de
        disparar la primer transicion del conjunto */
    @Override
    public boolean checkInvariants(Integer[] initialState) {

        ArrayList<ArrayList<Integer>> transitions;
        transitions = new Parser(LOGPATH, "T\\d").getParsedElements();

        parseLogStates();

        for(ArrayList<Integer> invariant : invariantList){

            int next = 0;

            for(int i=0; i<transitions.size(); i++){

                if(transitions.get(i).get(0).equals(invariant.get(next))){

                    // TODO: PREGUNTAR COMO ES
                    if(next == 0){
                        try {
                            initialState = stateList.get(i-1).toArray(new Integer[initialState.length]);
                        }
                        catch (ArrayIndexOutOfBoundsException e){
                            // El estado inicial será el estado inicial de la red
                        }
                    }

                    next++;

                    //System.out.printf("T%d == INV T%d  -- next: %d\n", transitions.get(i).get(0), invariant.get(next-1), next);

                    if(next == invariant.size()){
                        // Se dispararon todas las transiciones de la invariante de forma continua
                        Integer[] state = stateList.get(i).toArray(new Integer[initialState.length]);

                        //System.out.printf("Marcado inicial: %s == Marcado final: %s ??\n", Arrays.toString(initialState), Arrays.toString(state));

                        if(!Arrays.equals(state, initialState)){
                            return false;
                        }

                        next = 0;
                    }
                }
                else {
                    // Reinicio la cuenta
                    next = 0;
                }
            }
        }

        return true;
    }
}
