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

public class ActividadesXAgente extends Activity {
    private static final String TAG_CONSULTA = "android";
    private static final String TAG_ID = "id";
    private static final String TAG_EMPRESA = "empresa";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_MOVIL = "movil";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_ACTIVIDAD = "actividad";
    private static final String TAG_FECHA = "fecha";
    private static final String TAG_ICONO = "icono_actividad";
    private static String url;
    private static String url_anteriores;
    private static String url_hoy;
    private static String url_proximos;
    private static String url_pendientes;
    ImageView image;
    ImageView icono;
    String kseguimiento;
    LinkedList<HashMap<String, String>> entries = new LinkedList<HashMap<String, String>>();
    ListView list;
    TextView id;
    TextView empresa;
    TextView nombre;
    TextView movil;
    TextView email;
    TextView actividad;
    TextView fecha;
    String id_user;
    static LinkedList<HashMap<String, String>> oslist;
    static LinkedList<HashMap<String, String>> data;
    JSONArray android = null;
    int elementos;
    public ActividadesXAgente() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividades);
        SharedPreferences sharedPrefs = PreferenceManager
        .getDefaultSharedPreferences(this);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dfsql = new SimpleDateFormat("yyyyMMdd");
        Bundle extras = getIntent().getExtras();
        id_user = extras.getString("id_user");
        url_pendientes = "http://www.swoox.com.mx/app/sql_actividades_pendientes.php?usuario=" +id_user;
        url_hoy = "http://www.swoox.com.mx/app/sql_actividades_del_dia.php?usuario=" +id_user
                + "&fecha=" + dfsql.format(c.getTime());

        url_anteriores = "http://www.swoox.com.mx/app/sql_actividades_pasadas.php?usuario=" + id_user
                + "&fecha=" + dfsql.format(c.getTime());

        url_proximos = "http://www.swoox.com.mx/app/sql_actividades_proximas.php?usuario=" + id_user
                + "&fecha=" + dfsql.format(c.getTime());

        oslist = new LinkedList<HashMap<String, String>>();
        url = url_pendientes;
        new JSONParse().execute();

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
            empresa = (TextView) findViewById(R.id.empresa);
            movil = (TextView) findViewById(R.id.movil);
            email = (TextView) findViewById(R.id.email);
            actividad = (TextView) findViewById(R.id.actividad);
            fecha = (TextView) findViewById(R.id.fecha);
            image = (ImageView) findViewById(R.id.icono_actividad);

            pDialog = new ProgressDialog(ActividadesXAgente.this);
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
                    String sempresa = c.getString(TAG_EMPRESA);
                    String smovil = c.getString(TAG_MOVIL);
                    String semail = c.getString(TAG_EMAIL);
                    String sactividad = c.getString(TAG_ACTIVIDAD).trim();
                    String sfecha = c.getString(TAG_FECHA);
                    kseguimiento = sactividad.toString();
                // Adding value HashMap key => value
                    HashMap<String, String> map =   new HashMap<String, String>();
                    map.put(TAG_ID, sid);
                    map.put(TAG_EMPRESA, sempresa);
                    map.put(TAG_NOMBRE, snombre);
                    map.put(TAG_MOVIL, smovil);
                    map.put(TAG_EMAIL, semail);
                    map.put(TAG_ACTIVIDAD, sactividad);
                    map.put(TAG_FECHA, sfecha);
                    map.put(TAG_ICONO, kseguimiento);
                    entries.add(map);
                    list = (ListView) findViewById(R.id.list);
                   // registerForContextMenu(list);
                }
            } catch (JSONException e) {
                Toast toast1 = Toast.makeText(getApplicationContext(), "SIN ACTIVIDADES", Toast.LENGTH_SHORT);
                toast1.show();
                e.printStackTrace();
            }
            setData(entries);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();

        if (v.getId() == R.id.list) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(entries.get(info.position).get("empresa"));
            inflater.inflate(R.menu.context_actividades, menu);
      }
    super.onCreateContextMenu(menu, v, menuInfo);
 }

    private void setData(LinkedList<HashMap<String, String>> data){

                MySimpleAdapter mAdapter = new MySimpleAdapter(ActividadesXAgente.this, data, R.layout.list_activdades,
                new String[]{TAG_ID, TAG_EMPRESA, TAG_NOMBRE, TAG_MOVIL, TAG_EMAIL, TAG_ACTIVIDAD, TAG_FECHA,TAG_ICONO},
                new int[]{R.id.id, R.id.empresa, R.id.nombre, R.id.movil, R.id.email, R.id.actividad, R.id.fecha,R.id.icono_actividad});

        TextView titulo = (TextView) findViewById((R.id.titulo_actividades));
        elementos = mAdapter.getCount();
        titulo.setText("Seguimientos " + String.valueOf(elementos) );
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
                v = vi.inflate(R.layout.list_activdades, null);
            }

            id = (TextView) v.findViewById(R.id.id);
            nombre = (TextView) v.findViewById(R.id.nombre);
            empresa = (TextView) v.findViewById(R.id.empresa);
            movil = (TextView) v.findViewById(R.id.movil);
            email = (TextView) v.findViewById(R.id.email);
            actividad = (TextView) v.findViewById(R.id.actividad);
            fecha = (TextView) v.findViewById(R.id.fecha);
            image = (ImageView) v.findViewById(R.id.icono_actividad);
            icono = (ImageView) v.findViewById(R.id.imagen_seguimiento);

            id.setText(results.get(position).get(TAG_ID));
            nombre.setText(results.get(position).get("nombre"));
            empresa.setText(results.get(position).get("empresa"));
            actividad.setText(results.get(position).get("actividad"));
            fecha.setText(results.get(position).get("fecha"));
            kseguimiento=results.get(position).get("actividad");
           int idimagen = getResources().getIdentifier(kseguimiento.toLowerCase() , "drawable","com.progint.swoox");
           image.setImageResource(idimagen);
            icono.setVisibility(ImageView.INVISIBLE);

            return v;
        }
    }



}
