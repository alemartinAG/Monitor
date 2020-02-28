package com.monitor;

import com.util.Parser;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class Politica {

    private ArrayList<ArrayList<Integer>> priorities;

    public Politica(String file){
        priorities = new Parser(file, "\\d+", "(", ")").getParsedElements();
    }

    public int getNext(boolean[] andVector) {

        for (ArrayList<Integer> priority : priorities) {
            for (int j = 0; j < priority.size(); j++) {
                Vector<Integer> enabled = new Vector<>();

                if (andVector[priority.get(j) - 1]) {
                    enabled.add(priority.get(j) - 1);
                }

                if (!enabled.isEmpty()) {
                    return enabled.get(new Random().nextInt(enabled.size()));
                }

            }
        }

        return trueVector(andVector).get(new Random().nextInt(trueVector(andVector).size()));

    }

    private ArrayList<Integer> trueVector(boolean[] vector){

        ArrayList<Integer> candidates = new ArrayList<Integer>();

        for(int i=0; i<vector.length; i++){
            if(vector[i]){
                candidates.add(i);
            }
        }

        return candidates;
    }

    public ArrayList<ArrayList<Integer>> getPriorities(){return priorities;}
}
