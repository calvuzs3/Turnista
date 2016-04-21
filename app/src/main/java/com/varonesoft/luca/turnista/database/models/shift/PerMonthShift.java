package com.varonesoft.luca.turnista.database.models.shift;

import java.util.HashMap;

/**
 * Created by luca on 21/04/16.
 *
 * Ritorniamo tutti i turni
 * Nella classe si calcolano i turni, dato lo schema
 */
public class PerMonthShift implements PerMonthShiftInterface {
    @Override
    public HashMap<Long, String> getShifts(long schema, long start_date, int year, int month) {
        return null;
    }

    // Membri
    private HashMap<Long,String> mResult;

    // Lo schema, lo impostiamo a livello di classe
    private Long mSchemeID; // Sostituire con mCreator(obj) ?

    // Data
    private Long mStartDate;


}
