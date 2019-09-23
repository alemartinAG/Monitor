package com.petri;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Invariant {

    private ArrayList<ArrayList<Integer>> invariantsList;

    abstract boolean checkInvariants();

    void parseInvariants(String file){

        String line;
        FileReader fileReader;

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
