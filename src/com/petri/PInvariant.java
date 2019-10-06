package com.petri;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PInvariant extends Invariant{

    private ArrayList<ArrayList<Integer>> states;

    public PInvariant(){
        parseInvariants("res/p-invariantes.txt");
        states = new ArrayList<>();
    }

    public PInvariant(String file){
        parseInvariants(file);
    }


    /* Chequea que la cantidad de tokens en las plazas
    de la invariante se mantenga siempre igual */
    @Override
    public boolean checkInvariants() {

        parseLogStates();

        for (ArrayList<Integer> invariant : invariantsList) {

            int tokens_init = 0;

            for (Integer place : invariant) {
                tokens_init += states.get(0).get(place-1);
            }

            for (ArrayList<Integer> state : states) {

                int tokens = 0;

                for (Integer place : invariant) {
                    tokens += state.get(place-1);
                }

                if(tokens != tokens_init){
                    return false;
                }
            }
        }

        return true;
    }

    /* Se encarga de parsear los estados del log */
    private void parseLogStates(){

        String line;
        FileReader fileReader;

        try {

            fileReader = new FileReader("res/log.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {

                ArrayList<Integer> state = new ArrayList<>();

                try {

                    String substr = line.substring(line.indexOf("[")+1, line.indexOf("]"));
                    Pattern pattern = Pattern.compile("\\d+");
                    Matcher matcher = pattern.matcher(substr);

                    while (matcher.find()) {
                        state.add(Integer.parseInt(matcher.group().trim()));
                    }

                    states.add(state);

                }
                catch (IndexOutOfBoundsException e){
                    bufferedReader.close();
                }

            }

            bufferedReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
