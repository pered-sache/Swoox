package com.progint.swoox;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
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

public class Resumen_Actividades extends Activity {
    private static final String TAG_CONSULTA = "android";
    private static final String TAG_ID = "id";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_V = "V";
    private static final String TAG_H = "H";
    private static final String TAG_P = "P";
    private static final String TAG_T = "T";

    private static String url;
    String nivel;
    ImageView image;
    ImageView icono;
    String kseguimiento;
    LinkedList<HashMap<String, String>> entries = new LinkedList<HashMap<String, String>>();
    ListView list;
    TextView id;
    TextView nombre;
    TextView sv;
    TextView h;
    TextView p;
    TextView t;
    static LinkedList<HashMap<String, String>> oslist;
    static LinkedList<HashMap<String, String>> data;
    JSONArray android = null;
    int elementos;
    public Resumen_Actividades() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resumen_tareas );
        SharedPreferences sharedPrefs = PreferenceManager
        .getDefaultSharedPreferences(this);
        nivel=sharedPrefs.getString("nivel","0");
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dfsql = new SimpleDateFormat("yyyyMMdd");
        url = "http://www.swoox.com.mx/app/resumen_de_seguimientos.php";
            oslist = new LinkedList<HashMap<String, String>>();
            new JSONParse().execute();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_actividades, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        TextView titulo = (TextView) findViewById(R.id.titulo_actividades);
        switch (item.getItemId()) {
            case R.id.menu_act1:
                url = url;
                oslist.clear();
                new JSONParse().execute();
                titulo.setText("Seguimientos  Pendientes" );
                return true;
            case R.id.menu_act2:
                url = url;
                oslist.clear();
                new JSONParse().execute();
                titulo.setText("Seguimientos  anteriores ");
                return true;
            case R.id.menu_act3:
                oslist.clear();
                url = url;
                oslist.clear();
                new JSONParse().execute();
                titulo.setText("Seguimientos  proximo ");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu1:
                Toast.makeText(getApplicationContext(), "Terminar tarea", Toast.LENGTH_LONG).show();
                return true;

            case R.id.menu2:
                Toast.makeText(getApplicationContext(), "Editar Tarea", Toast.LENGTH_LONG).show();
                return true;

            case R.id.menu3:
                Toast.makeText(getApplicationContext(), "Proximament", Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onContextItemSelected(item);

        }

    }


    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            id = (TextView) findViewById(R.id.id);
            nombre = (TextView) findViewById(R.id.nombre);
            sv = (TextView) findViewById(R.id.v);
            h = (TextView) findViewById(R.id.h);
            p = (TextView) findViewById(R.id.p);
            t = (TextView) findViewById(R.id.t);

            pDialog = new ProgressDialog(Resumen_Actividades.this);
            pDialog.setMessage("Conectando ...");
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
                    // Storing  JSON item in a Variable
                    String sid = c.getString(TAG_ID);
                    String snombre = c.getString(TAG_NOMBRE);
                    String sv = c.getString(TAG_V);
                    String sh = c.getString(TAG_H);
                    String sp = c.getString(TAG_P);
                    String st = c.getString(TAG_T);

                // Adding value HashMap key => value
                    HashMap<String, String> map =   new HashMap<String, String>();
                    map.put(TAG_ID, sid);
                    map.put(TAG_NOMBRE, snombre);
                    map.put(TAG_V, sv);
                    map.put(TAG_H, sh);
                    map.put(TAG_P, sp);
                    map.put(TAG_T, st);
                    entries.add(map);
                    list = (ListView) findViewById(R.id.list);

                }
            } catch (JSONException e) {
                Toast toast1 = Toast.makeText(getApplicationContext(), "SIN ACTIVIDADES", Toast.LENGTH_SHORT);
                toast1.show();
                e.printStackTrace();
            }
            setData(entries);
        }
    }


    private void setData(LinkedList<HashMap<String, String>> data){

                MySimpleAdapter mAdapter = new MySimpleAdapter(Resumen_Actividades.this, data, R.layout.list_resumen,
                new String[]{TAG_ID,  TAG_NOMBRE, TAG_V, TAG_H, TAG_P, TAG_T},
                new int[]{R.id.id, R.id.nombre, R.id.v, R.id.h, R.id.p, R.id.t});

        //TextView titulo = (TextView) findViewById((R.id.titulo_actividades));
       // elementos = mAdapter.getCount();
       // titulo.setText("Seguimientos " + String.valueOf(elementos) );
        ListView list = (ListView) findViewById(R.id.list );
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
                v = vi.inflate(R.layout.list_resumen, null);
            }

            id = (TextView) v.findViewById(R.id.id);
            nombre = (TextView) v.findViewById(R.id.nombre);
            sv = (TextView) v.findViewById(R.id.v);
            h = (TextView) v.findViewById(R.id.h);
            p = (TextView) v.findViewById(R.id.p);
            t = (TextView) v.findViewById(R.id.t);


            nombre.setText(results.get(position).get(TAG_NOMBRE));
            sv.setText(results.get(position).get(TAG_V));
            h.setText(results.get(position).get(TAG_H));
            p.setText(results.get(position).get(TAG_P));
            t.setText(results.get(position).get(TAG_T));

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Intent i = new Intent(Resumen_Actividades.this, ActividadesXAgente.class);
                    i.putExtra("id_user", results.get(+position).get(TAG_ID));
                    startActivity(i);
                }
            });
            return v;
        }
    }



}
