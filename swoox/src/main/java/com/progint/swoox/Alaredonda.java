package com.progint.swoox;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.progint.swoox.library.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;


public class Alaredonda extends Activity {
    //JSON Node Names
    private static final String TAG_CONSULTA = "android";
    private static final String TAG_ID = "id";
    private static final String TAG_RAZON = "razon";
    private static final String TAG_VENDEDOR = "vendedor";
    private static final String TAG_FECHA = "fecha";
    private static String url;
    LinkedList<HashMap<String, String>> entries = new LinkedList<HashMap<String, String>>();
    ListView list;
    TextView id;
    TextView razon;
    TextView vendedor;
    TextView fecha;
    static LinkedList<HashMap<String, String>> oslist;
    static LinkedList<HashMap<String, String>> data;
    JSONArray android = null;
    int elementos;
    String lat;
    String lng;

    public Alaredonda() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clientesalaredonda);
     //  Define ubicacion GPS
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Location Loc = mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                lat=String.valueOf(Loc.getLatitude());
                lng=String.valueOf(Loc.getLongitude());
                 }
        else {
            // Genera aviso de prender GPS
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Encender GPS !!")
                    .setTitle(" GPS-UBICACION")
                    .setIcon(R.drawable.droid_swoox)
                    .setCancelable(false)
                    .setNeutralButton("Aceptar", null);
            AlertDialog alert = builder.create();
            alert.show();

        }

        String parametros = "latitud="+lat+"&longitud="+lng+"&diametro=500";
        url = "http://www.swoox.com.mx/app/sql_clientes_a_la_redonda.php?"+parametros;
        oslist = new LinkedList<HashMap<String, String>>();
      new JSONParse().execute();
    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            id = (TextView) findViewById(R.id.id);
            razon = (TextView) findViewById(R.id.razon);
            vendedor = (TextView) findViewById(R.id.vendedor);
           // fecha = (TextView) findViewById(R.id.fecha);

            pDialog = new ProgressDialog(Alaredonda.this);
            pDialog.setMessage("Rastreando ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser jParser = new JSONParser();
            // Getting JSON from URL
            JSONObject json = jParser.getJSONFromUrl(url);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();

            try {
                // Getting JSON Array from URL

                android = json.getJSONArray(TAG_CONSULTA);
                oslist.clear();
                for (int i = 0; i < android.length(); i++) {
                    JSONObject c = android.getJSONObject(i);
                    String sid = c.getString(TAG_ID);
                   // String sfecha = c.getString(TAG_FECHA);
                    String srazon = c.getString(TAG_RAZON);
                    String svendedor = c.getString(TAG_VENDEDOR);
                    HashMap<String, String> map =   new HashMap<String, String>();
                    map.put(TAG_ID, sid);
                    map.put(TAG_RAZON, srazon);
                    map.put(TAG_VENDEDOR, svendedor);
                    //map.put(TAG_FECHA, sfecha);
                    entries.add(map);
                    list = (ListView) findViewById(R.id.list);
                    registerForContextMenu(list);
                }
            } catch (JSONException e) {
                Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(200);
                Toast toast1 = Toast.makeText(getApplicationContext(),"Sin clientes a la redonda en nuestros registros", Toast.LENGTH_SHORT);
                toast1.show();
                finish();
            }
            setData(entries);
        }
    }

    private void setData(LinkedList<HashMap<String, String>> data){

                MySimpleAdapter mAdapter = new MySimpleAdapter(Alaredonda.this, data, R.layout.list_alaredonda,
                new String[]{TAG_ID, TAG_RAZON, TAG_VENDEDOR},
                new int[]{R.id.id, R.id.razon, R.id.vendedor});
                ListView list = (ListView) findViewById(R.id.list);
                list.setAdapter(mAdapter);
    }

    public class MySimpleAdapter extends SimpleAdapter {
        private LinkedList<HashMap<String, String>> results;
        public MySimpleAdapter(Context context, LinkedList<HashMap<String, String>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            this.results = data;
        }

        public View getView(int position, View view, ViewGroup parent){
            View v = view;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_alaredonda, null);
            }

            id = (TextView) v.findViewById(R.id.id);
            razon = (TextView) v.findViewById(R.id.razon);
            vendedor = (TextView) v.findViewById(R.id.vendedor);

            id.setText(results.get(position).get("id"));
            razon.setText(results.get(position).get("razon"));
            vendedor.setText(results.get(position).get("vendedor"));
            return v;
        }
    }
}