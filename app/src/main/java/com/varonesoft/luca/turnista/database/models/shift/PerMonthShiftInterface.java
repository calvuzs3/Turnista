package com.varonesoft.luca.turnista.database.models.shift;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by luca on 21/04/16.
 *
 * Questa interfaccia definisce i metodi che intendo sviluppare dalle future classi
 * Voglio ottenere una lista corrispondente ai giorni del mese
 * che ricalchi lo schema precedentemente creato
 *
 * Ovverosia, dato uno schema, di lungezza n, con giorno di partenza, p,
 * voglio ottenere tutti i turni del mese calcolando la differenza in giorni
 * e ricalcando lo schema per tutti i giorni avvenire o in precedenza
 */
public interface PerMonthShiftInterface {

    /*
     * @returns a HashMap of <Date(Long), Shift(String)>
     *
     * Data una partenza ne calcola i giorni di differenza con
     * lo {@param }schema (id dal database)
     */
    HashMap<Long, String> getShifts(long schema, long start_date, int year, int month);



}
