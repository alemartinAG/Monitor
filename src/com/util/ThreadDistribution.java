package com.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO: Ver si una transición puede encontrarse en varios hilos
public class ThreadDistribution {

    private ArrayList<ArrayList<Integer>> threads_transitions;

    public ThreadDistribution(){

        threads_transitions = new ArrayList<ArrayList<Integer>>();
        parseThreadDistribution();


    }

    private void parseThreadDistribution(){

        String line;
        FileReader fileReader = null;

        try {

            fileReader = new FileReader("res/threads.txt");
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

    public int getNumberOfThreads(){
        return threads_transitions.size();
    }

    public ArrayList<Integer> getTransitionsOfThread(int thread_number){
        return threads_transitions.get(thread_number);
    }

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
