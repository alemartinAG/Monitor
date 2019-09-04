package com.main;

import com.monitor.GestorDeMonitor;
import com.monitor.PetriNet;
import com.util.ThreadDistribution;

import java.util.ArrayList;

public class main {

    public static void main(String[] args){

        GestorDeMonitor monitor = new GestorDeMonitor();
        ThreadDistribution threadDistr = new ThreadDistribution();
        threadDistr.printThreads();
    }



}
