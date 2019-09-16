package com.monitor;

public interface Politicas {

    /**
     * Interfaz que utilizan las distintas politicas del monitor
     * @param andVector vector resultante de la operacion (vectorColas AND vectorSensibilzadas)
     */

    abstract int getNext(boolean[] andVector);

}
