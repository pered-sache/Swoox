package com.progint.swoox.library;

/**
 * Created by eduardo on 25/12/13.
 */
public class Actividad {
    private String nombre;
    private String empresa;
    private String actividad;
    private String cel;
    private String email;
    private String fecha;
    private String id;

    public Actividad(String nom, String emp, String act, String ce, String mail, String fe, String miid) {
        nombre = nom;
        empresa = emp;
        actividad = act;
        cel = ce;
        email = mail;
        fecha = fe;
        id = miid;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmpresa() {
        return empresa;
    }

    public String getActividad() {
        return actividad;
    }

    public String getCel() {
        return cel;

    }

    public String getEmail() {
        return email;
    }

    public String getFecha() {
        return fecha;
    }

    public String getId() {
        return id;
    }

}
