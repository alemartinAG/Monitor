package com.petri;

import java.util.ArrayList;

public class PInvariant extends Invariant{



    public PInvariant(){
        parseInvariants("res/p-invariantes.txt");
    }

    public PInvariant(String file){
        parseInvariants(file);
    }


    /* Chequea que la suma de tokens en las plazas
    de la invariante se mantenga siempre igual */
    @Override
    public boolean checkInvariants(Integer[] initialState) {

        parseLogStates();

        for (ArrayList<Integer> invariant : invariantList) {

            int tokens_init = 0;

            // Suma inicial, debe ser igual en todos los estados
            for (Integer place : invariant) {
                tokens_init += initialState[place-1];
            }

            //System.out.printf("tokens_init = %d\n", tokens_init);

            for (ArrayList<Integer> state : stateList) {

                int tokens = 0;

                // Suma de tokens en el estado actual
                for (Integer place : invariant) {
                    tokens += state.get(place-1);
                }

                //System.out.printf("tokens = %d\n", tokens);

                if(tokens != tokens_init){
                    return false;
                }
            }
        }

        return true;
    }


}
