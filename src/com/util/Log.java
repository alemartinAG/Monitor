package com.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

public class Log {

    public static final String SEPARATOR = " // ";

    private final String OUT = "res/log.txt";

    private FileWriter fileReader = null;

    public Log(){

        try {
            fileReader = new FileWriter(OUT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void log(String event){
        
        try {
            fileReader = new FileWriter(OUT, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String stamp = new Timestamp(System.currentTimeMillis()).toString();
        event =  String.format("%-23s%s%s", stamp, SEPARATOR, event);

        BufferedWriter bufferedWriter = new BufferedWriter(fileReader);

        try {
            bufferedWriter.newLine();
            bufferedWriter.write(event);
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
