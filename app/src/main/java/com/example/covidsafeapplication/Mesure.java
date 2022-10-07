package com.example.covidsafeapplication;

public class Mesure {
    int id,taux,typeData,idLocal;
    String date;


    Mesure(int _i,int _t, int _td,int _l,String _d){
        id=_i;
        taux=_t;
        typeData=_td;
        date=_d;
        idLocal=_l;
    }


    public String getType(){
        if (typeData==1)
            return "Temperature";
        if (typeData==2)
            return "Humidity";
        if (typeData==3)
            return "CO2";

        return "null";
    }
}
