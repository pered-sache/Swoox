package com.progint.swoox;

/**
 * Created by perez on 15/04/2016.
 */
public class Globals {
    private static Globals instance;
    //varaibles globales
    private  int estatus_visita;
    private String idcliente_visita;

    private Globals(){}

    public static synchronized Globals getInstance (){
        if (instance==null){
            instance= new Globals();
        }
        return instance;
    }

    public void setestatus_visita(int t){
        this.estatus_visita=t;
    }

    public int getestatus_visita(){
        return this.estatus_visita;
    }


    public void setIdcliente_visita(String t){
        this.idcliente_visita=t;
    }

    public String getIdcliente_visita(){
        return this.idcliente_visita;

    }
}
