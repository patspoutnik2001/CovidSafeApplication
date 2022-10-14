package com.example.covidsafeapplication;

import java.util.Calendar;
import java.util.Date;

public class Mesure {
    String id,taux,typeData,idLocal;
    Calendar date;


    Mesure(String _i, String _t, String _td, String _l, Calendar _d){
        id=_i;
        taux=_t;
        typeData=_td;
        date=_d;
        idLocal=_l;
    }


    public String getType(){
        if (typeData.equals("1"))
            return "Temperature";
        if (typeData.equals("2"))
            return "Humidity";
        if (typeData.equals("3"))
            return "CO2";

        return "null";
    }
}
