package com.petri;

public class TInvariants extends Invariant {

    public TInvariants(){
        parseInvariants("res/t-invariantes.txt");
    }

    public TInvariants(String file){
        parseInvariants(file);
    }

    @Override
    public boolean checkInvariants() {
        return false;
    }
}
