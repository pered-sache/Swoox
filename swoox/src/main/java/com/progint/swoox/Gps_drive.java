package com.progint.swoox;

import android.os.AsyncTask;
import android.widget.Toast;

import com.progint.swoox.library.JSONParsers;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Eduardo on 21/03/14.
 */


public class Gps_drive {
    String id;
    String longitud;
    String latitud;
    String resultadoSQL;
    String fecha;
    String id_cliente;
    String nota;

    public Gps_drive (String id,String longitud,String latitud){
        this.id = id;
        this.longitud=longitud;
        this.latitud=latitud;
    }

    public void update ( String id,String longitud,String latitud){
        this.id = id;
        this.longitud=longitud;
        this.latitud=latitud;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm");
        fecha= df.format(c.getTime()).toString();

        new guarda_ubicacion().execute();
    }


    //asigna o actualiza ubicacion de un cliente
    public void set_point ( String id,String longitud,String latitud, String id_cliente){
        this.id = id;
        this.longitud=longitud;
        this.latitud=latitud;
        this.id_cliente = id_cliente;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm");
        fecha= df.format(c.getTime()).toString();
        new define_ubicacion_cliente().execute();
    }

    //GeneraFuncion Checkin origen =1
    public void check_in ( String id,String longitud,String latitud, String id_cliente, String nota){
        this.id = id;
        this.longitud=longitud;
        this.latitud=latitud;
        this.id_cliente = id_cliente;
        this.nota=nota;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm");
        fecha= df.format(c.getTime()).toString();
        new ejecuta_checkin().execute();

    }

    //GeneraFuncion checout origen =2
    public void check_out ( String id,String longitud,String latitud, String id_cliente, String nota){
        this.id = id;
        this.longitud=longitud;
        this.latitud=latitud;
        this.id_cliente = id_cliente;
        this.nota=nota;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm");
        fecha= df.format(c.getTime()).toString();
        new ejecuta_checkout().execute();

    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    public class guarda_ubicacion extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_usuario", id));
            params.add(new BasicNameValuePair("longitud", longitud));
            params.add(new BasicNameValuePair("latitud", latitud));
            params.add(new BasicNameValuePair("fecha", fecha));
            JSONParsers sh = new JSONParsers();
            resultadoSQL = sh.ExecutarSql("HTTP://www.swoox.com.mx/app/add_gps.php", JSONParsers.POST, params);
            return resultadoSQL;
        }
    }

    public class define_ubicacion_cliente extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_usuario", id));
            params.add(new BasicNameValuePair("longitud", longitud));
            params.add(new BasicNameValuePair("latitud", latitud));
            params.add(new BasicNameValuePair("id_cliente", id_cliente));
            JSONParsers sh = new JSONParsers();
            resultadoSQL = sh.ExecutarSql("HTTP://www.swoox.com.mx/app/actualiza_ubicacion_cliente.php", JSONParsers.POST, params);
            return resultadoSQL;
        }
    }

    public class ejecuta_checkin extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_usuario", id));
            params.add(new BasicNameValuePair("longitud", longitud));
            params.add(new BasicNameValuePair("latitud", latitud));
            params.add(new BasicNameValuePair("id_cliente", id_cliente));
            params.add(new BasicNameValuePair("nota", nota));
            JSONParsers sh = new JSONParsers();
            resultadoSQL = sh.ExecutarSql("HTTP://www.swoox.com.mx/app/checkin.php", JSONParsers.POST, params);
            return resultadoSQL;
        }
    }

    public class ejecuta_checkout extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_usuario", id));
            params.add(new BasicNameValuePair("longitud", longitud));
            params.add(new BasicNameValuePair("latitud", latitud));
            params.add(new BasicNameValuePair("id_cliente", id_cliente));
            params.add(new BasicNameValuePair("nota", nota));
            JSONParsers sh = new JSONParsers();
            resultadoSQL = sh.ExecutarSql("HTTP://www.swoox.com.mx/app/checkout.php", JSONParsers.POST, params);
            return resultadoSQL;
        }
    }

}





