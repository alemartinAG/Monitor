package com.petri;

import java.util.ArrayList;
import java.util.Arrays;

public class PInvariant extends Invariant{

    final static String INVPATH = "res/p-invariantes.txt";

    private Integer[] invariantTokens;

    public PInvariant(Integer[] initialMarking){
        parseInvariants(INVPATH);
        setInvariantTokens(initialMarking);
    }

    public PInvariant(String file, Integer[] initialMarking){
        parseInvariants(file);
        setInvariantTokens(initialMarking);
    }

    /* Chequea que la suma de tokens en las plazas
    de la invariante se mantenga siempre igual */
    @Override
    public boolean checkInvariants(Integer[] currentMarking) {

        int index = 0;
        for (ArrayList<Integer> invariant : invariantList) {
            int invariantCurrentTokens = 0;

            for (Integer place : invariant) {
                invariantCurrentTokens += currentMarking[place-1];
            }

            if (invariantCurrentTokens != invariantTokens[index]) {
                return false;
            }

            index++;
        }

        return true;
    }

    /* Setea la cantidad inicial de tokens para cada invariante */
    private void setInvariantTokens(Integer[] initialMarking){

        invariantTokens = new Integer[invariantList.size()];
        Arrays.fill(invariantTokens, 0);

        int index = 0;

        for (ArrayList<Integer> invariant : invariantList) {
            for (Integer place : invariant) {
                invariantTokens[index] += initialMarking[place-1];
            }

            index++;
        }
    }

}
