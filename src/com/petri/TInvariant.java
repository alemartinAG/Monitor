package com.petri;

import com.errors.IllegalPetriStateException;
import com.errors.OutsideWindowException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TInvariant extends Invariant {

    private final static String LOGPATH = "res/log.txt";
    private final static String REGPATH = "res/regex.txt";
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
    public void checkInvariants(Integer[] initialState) throws IllegalPetriStateException{

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

        String expressions = "";

        try {
            expressions = new String(Files.readAllBytes(Paths.get(REGPATH)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Pattern regexPattern = Pattern.compile("(?:Regex.+:\\s)(.+)");
        Pattern replacePattern = Pattern.compile("(?:Replace.+:\\s)(.+)");
        Matcher regexMatcher = regexPattern.matcher(expressions);
        Matcher replaceMatcher = replacePattern.matcher(expressions);

        ArrayList<String> regex = new ArrayList<>();
        ArrayList<String> replacers = new ArrayList<>();

        while (regexMatcher.find()){
            regex.add(regexMatcher.group(1));
        }

        while(replaceMatcher.find()){
            replacers.add(replaceMatcher.group(1));
        }

        if(replacers.size() != regex.size()){
            System.out.println("Regular Expressions and Replacer doesn't match");
            return;
        }

        for(String expr : regex){

            pattern = Pattern.compile(expr);
            matcher = pattern.matcher(data);

            while (matcher.find()){
                data = matcher.replaceFirst(replacers.get(regex.indexOf(expr)));
                matcher.reset(data);
            }

            pattern = Pattern.compile("(\\s{2,})");
            matcher = pattern.matcher(data);
            data = matcher.replaceAll(" ");

            System.out.println(regex.indexOf(expr)+" --> "+data);
        }

        pattern = Pattern.compile("(\\s{2,})");
        matcher = pattern.matcher(data);
        data = matcher.replaceAll(" ");

        System.out.println(data);


    }
}