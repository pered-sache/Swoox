package com.progint.swoox;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.progint.swoox.library.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by eduardo on 22/02/14.
 */
public class resumen extends Activity implements View.OnClickListener {
    private static final String TAG_CONSULTA = "android";
    String url;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    JSONArray android = null;
    TextView tclientes;
    TextView tinactivos;
    TextView ultimaAlta;
    TextView tseguimientos;
    TextView terminados;
    TextView tpendientes;
    TextView ultimoseg;
    Button button;
    public resumen()     {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resumen);
        SharedPreferences sharedPrefs = PreferenceManager
        .getDefaultSharedPreferences(this);
        url = "http://www.swoox.com.mx/app/sql_resumen.php?id=" + sharedPrefs.getString("idusuario", "NULL");
        oslist = new ArrayList<HashMap<String, String>>();
        new JSONParse().execute();
        button = (Button) findViewById(R.id.button_resumen_salir);
        button.setOnClickListener(this);
       }

    @Override
    public void onClick(View v) {
        if(v.getId()==findViewById(R.id.button_resumen_salir).getId())
        {
            finish();
        }
    }


    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(resumen.this);
            pDialog.setMessage("Integrando Informacion ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.getJSONFromUrl(url);
            return json;



        }
        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                android = json.getJSONArray(TAG_CONSULTA);
                JSONObject c;

                c = android.getJSONObject(0);
                TextView tclientes = (TextView) findViewById(R.id.resumen_total);
                tclientes.setText("Clientes registrados : "+c.getString("total")) ;

                c = android.getJSONObject(1);
                TextView tinactivos  = (TextView) findViewById(R.id.resumen_inactivos);
                tinactivos.setText("Clientes Activos : "+c.getString("activos"));

                c = android.getJSONObject(2);
                TextView ultimaAlta = (TextView) findViewById(R.id.resumen_ultima_alta);
                ultimaAlta.setText("Fecha ultimo Cliente : "+c.getString("ultimo"));

                c = android.getJSONObject(3);
                TextView tseguimientos = (TextView) findViewById(R.id.resumen_seguimientos);
                tseguimientos.setText("Total de Seguimientos : "+c.getString("seguimientos"));

                c = android.getJSONObject(4);
                TextView terminados =(TextView) findViewById(R.id.resumen_seg_terminados);
                terminados.setText("Seguimientos Completados : "+c.getString("terminados"));

                c = android.getJSONObject(5);
                TextView tpendientes = (TextView) findViewById(R.id.resumen_seg_pentienes);
                tpendientes.setText("Seguimientos Pendientes : "+c.getString("pendientes"));

                c = android.getJSONObject(6);
                TextView ultimoseg = (TextView) findViewById(R.id.resumen_ult_seguimiento);
                ultimoseg.setText("Ultimo Seguimiento : "+c.getString("ultimoseguimiento"));



            } catch (JSONException e) {
                //Toast toast1 = Toast.makeText(getApplicationContext(),"SIN CLIENTES", Toast.LENGTH_SHORT);
                Toast toast1 = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
                toast1.show();
                e.printStackTrace();
            }
        }
    }


    }


