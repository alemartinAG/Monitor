package com.petri;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Invariant {

    ArrayList<ArrayList<Integer>> invariantsList;

    /* Se encarga de comprobar que se cumplan las invariantes */
    abstract public boolean checkInvariants();

    /* Se encarga de parsear el documento que especifica las invariantes */
    void parseInvariants(String file){

        String line;
        FileReader fileReader;

        invariantsList = new ArrayList<>();

        try {

            fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {

                String substr = line.substring(line.indexOf("("), line.indexOf(")"));

                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(substr);

                ArrayList<Integer> invariants = new ArrayList<>();

                while (matcher.find()) {
                    invariants.add(Integer.parseInt(matcher.group().trim()));
                }

                invariantsList.add(invariants);

            }

            bufferedReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
