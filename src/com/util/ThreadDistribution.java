package com.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThreadDistribution {

    private ArrayList<ArrayList<Integer>> threads_transitions;

    public ThreadDistribution(){
        threads_transitions = new ArrayList<>();
        parseThreadDistribution("res/threads.txt");
    }

    public ThreadDistribution(String file){
        threads_transitions = new ArrayList<>();
        parseThreadDistribution(file);
    }

    /* Se encarga de parsear el documento que especifica la relacion Thread-Transicion */
    private void parseThreadDistribution(String file){

        String line;
        FileReader fileReader;

        try {

            fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {

                String substr = line.substring(line.indexOf("("), line.indexOf(")"));

                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(substr);

                ArrayList<Integer> transitions = new ArrayList<>();

                while (matcher.find()) {
                    transitions.add(Integer.parseInt(matcher.group().trim()));
                }

                threads_transitions.add(transitions);
            }

            bufferedReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /* Devuelve el numero de threads necesarios */
    public int getNumberOfThreads(){
        return threads_transitions.size();
    }

    /* Devuelve las transiciones del thread especificado */
    public ArrayList<Integer> getTransitionsOfThread(int thread_number){
        return threads_transitions.get(thread_number);
    }

    /* Funcion utilizada para debugging */
    public void printThreads(){
        for(int i=0; i<threads_transitions.size(); i++){
            System.out.printf("Thread-%02d: ", i);
            for(int j=0; j<threads_transitions.get(i).size(); j++){
                System.out.printf("T%d ", threads_transitions.get(i).get(j));
            }
            System.out.println();
        }
    }
}
