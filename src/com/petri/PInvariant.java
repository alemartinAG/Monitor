package com.petri;

public class PInvariant extends Invariant{

    public PInvariant(){
        parseInvariants("res/p-invariantes.txt");
    }

    public PInvariant(String file){
        parseInvariants(file);
    }

    @Override
    boolean checkInvariants() {
        return false;
    }
}
