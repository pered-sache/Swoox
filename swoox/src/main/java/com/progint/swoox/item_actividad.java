package com.progint.swoox;

/**
 * Created by eduardo on 8/01/14.
 */
public class item_actividad {
    private String id;
    private String Actividad;
    private String Empresa;
    private String nombre;
    private String fecha;
    private int icono;

   public item_actividad (String id,String Actividad,String Empresa,String nombre, String fecha,int icono){
       this.id=id;
       this.Actividad=Actividad;
       this.Empresa =Empresa;
       this.nombre = nombre ;
       this.fecha=fecha;
       this.icono=icono;
   }

    public String getid(){
        return id;
    }
    public void setId (String id) {
        this.id=id;
    }

    public String getActividad(){
        return Actividad;
    }
    public void setActividad (String Actividad) {
        this.Actividad=Actividad;
    }

    public String getEmpresa(){
        return Empresa;
    }
    public void setEmpresa(){
        this.Empresa=Empresa;
    }

    public  String getNombre() {
        return nombre;
    }
    public void setNombre(){
        this.nombre=nombre;
    }

    public String getFecha(){
        return fecha;
    }
    public void setFecha(){
        this.fecha=fecha;
    }

    public int geticono(){
        return icono;
    }
    public void setIcono(){
        this.icono=icono;
    }

}
