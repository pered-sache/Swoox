package com.progint.swoox.library;

/**
 * Created by Eduardo on 28/04/14.
 */
public class Cliente {
    String sid ;
    String snombre;
    String sempresa;
    String stelefono;
    String sextencion;
    String smovil ;
    String semail;
    String srazon;
    public Cliente (String id,String nombre,String empresa,String telefono,String ext,String movil,String mail,String razon){
        sid=id;
        snombre=nombre;
        sempresa=empresa;
        stelefono=telefono;
        sextencion=ext;
        smovil=movil;
        semail=mail;
        srazon=razon;
    }

    public String gettel() {
        return stelefono;
    }
    public String getcel() {
        return smovil;
    }

}
