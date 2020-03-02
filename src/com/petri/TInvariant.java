package com.petri;

import com.errors.IllegalPetriStateException;
import com.errors.OutsideWindowException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TInvariant extends Invariant {

    private final static String LOGPATH = "res/log.txt";
    private final static String REGPATH = "res/regex.txt";
    private final static String INVPATH = "res/t-invariantes.txt";

    private final static int NKNOTS = 2;

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
        Pattern knotPattern = Pattern.compile("(?:Knot.+:\\s)(.+)");
        Matcher regexMatcher = regexPattern.matcher(expressions);
        Matcher replaceMatcher = replacePattern.matcher(expressions);
        Matcher knotMatcher = knotPattern.matcher(expressions);

        ArrayList<String> regex = new ArrayList<>();
        ArrayList<String> replacers = new ArrayList<>();
        ArrayList<ArrayList<String>> knots = new ArrayList<>();

        while (regexMatcher.find()){
            regex.add(regexMatcher.group(1));
        }

        while(replaceMatcher.find()){
            replacers.add(replaceMatcher.group(1));
        }

        for (int i=0; i<NKNOTS; i++){
            ArrayList<String> knot = new ArrayList<>();

            for(int j=0; j<2; j++){
                if(!knotMatcher.find()){
                    System.out.println("ERROR");
                    return;
                }
                knot.add(knotMatcher.group(1));
            }

            knots.add((ArrayList<String>) knot.clone());
            knot.clear();
        }


        if(replacers.size() != regex.size()){
            System.out.println("Regular Expressions and Replacer doesn't match");
            return;
        }

        boolean[] reverse = new boolean[NKNOTS];
        Arrays.fill(reverse, false);

        for(String expr : regex){

            System.out.println("REGEX: "+expr);

            pattern = Pattern.compile(expr);
            matcher = pattern.matcher(data);

            //data = matcher.replaceAll(replacers.get(regex.indexOf(expr)));

            while (matcher.find()){
                //System.out.println("FOUND: "+matcher.group());
                data = matcher.replaceFirst(replacers.get(regex.indexOf(expr)));
                matcher.reset(data);

                for(int i=0; i<NKNOTS; i++){

                    if(!reverse[i]){

                        for (int j=0; j<2; j++){
                            knotPattern = Pattern.compile(knots.get(i).get(j));
                            knotMatcher = knotPattern.matcher(data);

                            if(!knotMatcher.find()){
                                System.out.println(knotMatcher.pattern().pattern()+": No encontre wachi, hay que meterle reveresa");
                                reverse[i] = true;
                            }
                        }
                    }

                }

                if(reverse[1]){
                    break;
                }

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

    public String check(){
        String[][] knots = {{"(?:T12)", "(?:T13)"},{"(?:T14)", "(?:T15)", "(?:T16)", "(?:T17)", "(?:T18)"},{"(?:T19)", "(?:T20)", "(?:T21)"},{"(?:T22)", "(?:T23)", "(?:T24)"}};

        String regex = "(?:(?:(?:T1\b)(.+?)(?:T2\b)(.+?)(?:T3))|(?:(?:T4)(.+?)(?:T5)(.+?)(?:T6))|(?:(?:T7)(.+?)(?:T8)(.+?)(?:T9)))(.+?)";

        String nudo = "";
        for (String[] knot : knots) {
            nudo += "(?:";
            for (int j = 0; j < knot.length; j++) {
                nudo += knot[j];
                nudo += "(.+?)";
            }
            nudo = nudo.substring(0, nudo.length()-5);
            nudo += ") ***";
        }

        return nudo;
    }

    public void checkcheck(){

        String data = "";

        try {
            data = new String(Files.readAllBytes(Paths.get("res/regex.json")));
        } catch (IOException e) {
            e.printStackTrace();
        }



        try{
            Object obj = new JSONParser().parse(data);
            JSONObject jo = (JSONObject) obj;

            // getting phoneNumbers
            JSONArray ja = (JSONArray) jo.get("Invariant");

            // iterating phoneNumbers
            Iterator itr2 = ja.iterator();

            while (itr2.hasNext())
            {
                Iterator itr1 = ((Map) itr2.next()).entrySet().iterator();
                while (itr1.hasNext()) {
                    Map.Entry pair = (Map.Entry) itr1.next();
                    System.out.println(pair.getKey() + " : " + pair.getValue());
                }
            }

            /*System.out.println("The 2nd element of array");
            System.out.println(ja.get(1));
            System.out.println();*/

            /*JSONObject obj2 = (JSONObject)array.get(0);
            System.out.println("Field \"1\"");
            System.out.println(obj2.get("1"));*/


        }catch(ParseException pe) {
            System.out.println("position: " + pe.getPosition());
            System.out.println(pe);
        }
    }
}