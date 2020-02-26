package com.petri;

import com.errors.IllegalPetriStateException;
import com.errors.OutsideWindowException;
import com.util.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TInvariant extends Invariant {

    private final static String LOGPATH = "res/log.txt";
    private final static String INVPATH = "res/t-invariantes.txt";

    public TInvariant(){
        parseInvariants(INVPATH);
    }

    public TInvariant(String file){
        parseInvariants(file);
    }


    /* Chequea que tras disparar las transiciones restantes, luego de remover las pertenecientes a una
         invariante de transición, de manera ordenada, el marcado (o estado) de la red,
         sea el mismo en el que se encontraba tras disparar la última transición de la simulación */
    @Override
    public boolean checkInvariants(Integer[] initialState) throws IllegalPetriStateException {

        Pattern[] patterns = new Pattern[invariantList.size()];
        ArrayList<String> replacers = new ArrayList<>();
        ArrayList<Matcher> matchers = new ArrayList<>();

        String data = "";

        try {
            data = new String(Files.readAllBytes(Paths.get(LOGPATH)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int index = 0;

        System.out.println("1° for @ "+new Timestamp(System.currentTimeMillis()));

        for(ArrayList<Integer> invariant : invariantList){

            String invariantPattern = "";
            String replace = "";

            for(int i=0; i<invariant.size()-1; i++) {
                invariantPattern += String.format("(T%d\\s)([\\S\\s]+?)", invariant.get(i));
                replace += String.format("$%d", i*2+2);
            }

            invariantPattern += String.format("(T%d\\s)", invariant.get(invariant.size()-1));

            patterns[index] = Pattern.compile(invariantPattern);
            matchers.add(patterns[index].matcher(data));
            replacers.add(replace);

            index++;
        }

        for(Matcher m : matchers){
            System.out.println("____ Pattern: "+m.pattern().pattern());
        }

        System.out.println("for ended @ "+new Timestamp(System.currentTimeMillis()));

        System.out.println("while @ "+new Timestamp(System.currentTimeMillis()));

        while (matchers.size() > 0){

            int position = data.length();
            int invariant = -1;
            Vector<Matcher> delete = new Vector<>();

            for(int i=0; i<matchers.size(); i++){

                System.out.println("busco en "+i);
                boolean found = matchers.get(i).find();

                if(!found){
                    System.out.println("not found");
                    delete.add(matchers.get(i));
                    System.out.println("Elimino: "+i);
                    /*matchers.remove(i);
                    replacers.remove(i);*/
                }
                else{
                    System.out.println("found!!");
                    int pos = matchers.get(i).end();
                    if(pos < position){
                        position = pos;
                        invariant = i;
                    }

                }
            }
            System.out.print("///// eliminando.....");

            for (Matcher toremove : delete) {
                replacers.remove(matchers.indexOf(toremove));
                matchers.remove(toremove);
            }

            System.out.println("eliminados;;; Ahora son "+matchers.size()+" \\\\\\\\\\\\");

            if(invariant >= 0){
                System.out.println(matchers.get(invariant).pattern().pattern()+" ++++ Primera ocurrencia");
                data = matchers.get(invariant).replaceFirst(replacers.get(invariant));
            }

            for (Matcher matcher : matchers) {
                matcher.reset(data);
            }

        }

        System.out.println("while ended @ "+new Timestamp(System.currentTimeMillis()));
        System.out.println("2° for @ "+new Timestamp(System.currentTimeMillis()));

        /*for(ArrayList<Integer> invariant : invariantList){

            for(int j=0; j<invariant.size()-1; j++){

                String invariantPattern = "";
                String replace = "";

                for(int i=0; i<invariant.size()-2-j; i++) {
                    invariantPattern += String.format("(T%d\\s)([\\S\\s]+?)", invariant.get(i));
                    replace += String.format("$%d", i*2+2);
                }

                invariantPattern += String.format("(T%d\\s)", invariant.get(invariant.size()-2-j));

                Pattern pattern = Pattern.compile(invariantPattern);
                Matcher matcher = pattern.matcher(data);


                while (matcher.find()){
                    data = matcher.replaceFirst(replace);
                    matcher.reset(data);
                }



            }

        }*/

        System.out.println("2° for ended @ "+new Timestamp(System.currentTimeMillis()));

        System.out.println(data);

        /*Pattern pattern = Pattern.compile("T\\d+");
        Matcher matcher = pattern.matcher(data);

        if(matcher.find()){
            throw new IllegalPetriStateException("T-Invariants are not met\n"+data);
        }*/

        System.out.println("T-invariants are met");

        return true;
    }

    public void checkcheck(){

        String data = "";

        try {
            data = new String(Files.readAllBytes(Paths.get(LOGPATH)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Pattern pattern = Pattern.compile("(.+)(T\\d+)(.+\\s)");
        Matcher matcher = pattern.matcher(data);

        data = matcher.replaceAll("$2 ");

        System.out.println(data);

        Pattern[] patterns = new Pattern[invariantList.size()];
        ArrayList<String> replacers = new ArrayList<>();
        ArrayList<Matcher> matchers = new ArrayList<>();
        ArrayList<ArrayList<String>> regex = new ArrayList<>();

        int index = 0;

        for(ArrayList<Integer> invariant : invariantList){

            String invariantPattern = "";
            String replace = "";
            ArrayList<String> expression = new ArrayList<>();

            for(int i=0; i<invariant.size()-1; i++) {
                invariantPattern += String.format("(?:T%d\\b)(.+?)", invariant.get(i));
                //invariantPattern += String.format("(T%d\\b)(.+?)", invariant.get(i));
                expression.add(String.format("(?:T%d\\b)", invariant.get(i)));
                //expression.add("(.+?)");
                expression.add("(.+?)");
                replace += String.format("$%d", i+1);
            }

            invariantPattern += String.format("(?:T%d\\b)", invariant.get(invariant.size()-1));
            expression.add(String.format("(?:T%d\\b)", invariant.get(invariant.size()-1)));

            patterns[index] = Pattern.compile(invariantPattern);
            matchers.add(patterns[index].matcher(data));
            replacers.add(replace);
            regex.add(expression);

            index++;
        }

        for(Matcher m : matchers){
            System.out.println("____ Pattern: "+m.pattern().pattern());
            System.out.println("++++ Replace: "+replacers.get(matchers.indexOf(m)));
        }

        System.out.println("for ended @ "+new Timestamp(System.currentTimeMillis()));

        System.out.println("while @ "+new Timestamp(System.currentTimeMillis()));

        int matches = 0;
        int position;
        int beg;
        Matcher m1;

        while (!matchers.isEmpty()){

            position = data.length();
            beg = position;
            Vector<Matcher> delete = new Vector<>();
            m1 = null;


            for(Matcher m : matchers) {

                boolean found = m.find();

                if (!found) {
                    delete.add(m);
                }
                else {

                    int first = m.start();
                    if (first < beg) {
                        beg = first;
                        m1 = m;
                    } else if (first == beg) {

                        int prevIndex = matchers.indexOf(m1);
                        int thisIndex = matchers.indexOf(m);

                        for(int i=0; i<regex.get(prevIndex).size(); i++){

                            String prevGroup = regex.get(prevIndex).get(i);
                            String thisGroup = regex.get(thisIndex).get(i);

                            if(!prevGroup.equals(thisGroup)){

                                Pattern prevPattern = Pattern.compile(prevGroup);
                                Matcher prevMatcher = prevPattern.matcher(data);

                                prevMatcher.find();

                                Pattern thisPattern = Pattern.compile(thisGroup);
                                Matcher thisMatcher = thisPattern.matcher(data);

                                thisMatcher.find();

                                int p = prevMatcher.start();
                                int t = thisMatcher.start();

                                if(t < p){
                                    m1 = m;
                                    break;
                                }
                                 break;

                            }
                        }

                    }
                }
            }


            if(m1 != null){
                data = matchers.get(matchers.indexOf(m1)).replaceFirst(replacers.get(matchers.indexOf(m1)));

                Pattern space = Pattern.compile("(\\s{2,})");
                Matcher mat = space.matcher(data);
                data = mat.replaceAll(" ");

                matches++;
                System.out.println(data);
                System.out.println("@@@@"+matches);

                for (Matcher m : matchers) {
                    m.reset(data);
                }
            }

            for (Matcher toremove : delete) {
                replacers.remove(matchers.indexOf(toremove));
                regex.remove(matchers.indexOf(toremove));
                matchers.remove(toremove);
            }



        }

        System.out.println("while ended @ "+new Timestamp(System.currentTimeMillis()));

        System.out.println("");
        System.out.println(data);


    }
}