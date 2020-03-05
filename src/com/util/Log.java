package com.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

public class Log {

    public static final String SEPARATOR = " // ";
    private final String OUT = "res/log.txt";

    public Log(){

        try {
            new FileWriter(OUT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void log(String event){

        FileWriter fileWriter;
        
        try {
            fileWriter = new FileWriter(OUT, true);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String stamp = new Timestamp(System.currentTimeMillis()).toString();
        event =  String.format("%-23s%s%s", stamp, SEPARATOR, event);

        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        try {

            bufferedWriter.write(event);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter.close();

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

}
