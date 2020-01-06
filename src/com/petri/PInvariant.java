package com.petri;

import java.util.ArrayList;
import java.util.Arrays;

public class PInvariant extends Invariant{

    private Integer[] invariantTokens;

    public PInvariant(Integer[] initialMarking){
        parseInvariants("res/p-invariantes.txt");
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

    /* Setea la suma inicial de tokens para cada invariante */
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
