package com.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private String filename;
    private String regex;

    private boolean trimming = false;
    private String start;
    private String end;

    public Parser(String filename, String regex){
        this.filename = filename;
        this.regex = regex;
    }

    public Parser(String filename, String regex, String start, String end){
        this.filename = filename;
        this.regex = regex;

        trimming = true;

        this.start = start;
        this.end = end;
    }

    public ArrayList<ArrayList<Integer>> getParsedElements(){

        String line;
        FileReader fileReader;

        ArrayList<ArrayList<Integer>> elements = new ArrayList<>();

        try {

            fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {

                ArrayList<Integer> element = new ArrayList<>();

                try {

                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher;

                    if(trimming){
                        String substr = line.substring(line.indexOf(start)+1, line.indexOf(end));
                        matcher = pattern.matcher(substr);
                    }
                    else {
                        matcher = pattern.matcher(line);
                    }

                    while (matcher.find()) {
                        try {
                            element.add(Integer.parseInt(matcher.group().trim()));
                        }
                        catch (NumberFormatException e){
                            //TODO: VER
                            element.add(Integer.parseInt(matcher.group().trim().substring(1)));
                        }

                    }

                    elements.add(element);

                }
                catch (IndexOutOfBoundsException e){
                    bufferedReader.close();
                }

            }

            bufferedReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return elements;
    }

}
