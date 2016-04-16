package com.progint.swoox;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings.Global;

import com.progint.swoox.library.Cliente;
import com.progint.swoox.library.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class Directorio extends Activity {
    //JSON Node Names
    private static final String TAG_CONSULTA = "android";
    private static final String TAG_ID = "id";
    private static final String TAG_EMPRESA = "empresa";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_MOVIL = "movil";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_TELEFONO = "telefono1";
    private static final String TAG_EXTENCION = "extencion";
    private static final String TAG_RAZON = "razon";

    private static String url;
    EditText et;
    ListView list;
    TextView id;
    TextView empresa;
    TextView nombre;
    TextView movil;
    TextView email;
    TextView telefono;
    TextView extencion;
    TextView titulo;
    TextView razon;
    int elementos;
    int textlength;
    String numero;
    String id_usuario;
    String id_cliente;
    LinearLayout panel;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    JSONArray android = null;
    SimpleAdapter adapter;
    HashMap<String, Object> objCliente;
    Globals g = Globals.getInstance();
    public Directorio() {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diretorio);

        SharedPreferences sharedPrefs = PreferenceManager
        .getDefaultSharedPreferences(this);
        id_usuario= sharedPrefs.getString("idusuario", "NULL");
        url = "http://www.swoox.com.mx/app/sql_directorio.php?id_usuario=" + id_usuario;
        oslist = new ArrayList<HashMap<String, String>>();
        new JSONParse().execute();
        et = (EditText) findViewById(R.id.inputSearch);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        et.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // Abstract Method of TextWatcher Interface.
                textlength = et.getText().length();
              //  ListView list = (ListView) findViewById(R.id.list);
               // list.setFilterText(et.getText().toString());
               // list.getFilterTouchesWhenObscured();
                adapter.getFilter().filter(et.getText().toString());
            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // Abstract Method of TextWatcher Interface.
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_directorio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        TextView titulo = (TextView) findViewById((R.id.titulo_directorio));
        switch (item.getItemId()) {
            case R.id.ayuda:
                Intent i = new Intent(Directorio.this, ayuda.class);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String campo;
        switch (item.getItemId()) {
                case R.id.menu3:
                    objCliente = (HashMap<String, Object>) adapter.getItem(info.position);
                    campo = (String) objCliente.get("email");
                    Intent i1 = new Intent(Directorio.this, email.class);
                    i1.putExtra("correo", campo);
                    startActivity(i1);

                return  true;
            case R.id.menu1:
                objCliente = (HashMap<String, Object>) adapter.getItem(info.position);
                campo = (String) objCliente.get("telefono1");
                Toast.makeText(Directorio.this, campo, Toast.LENGTH_SHORT).show();
                numero = "tel:"+campo;
                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse(numero));
                    startActivity(callIntent);
                } catch (ActivityNotFoundException activityException) {
                    Log.e("dialing-example", "Call failed", activityException);
                }
                return true;

            case R.id.menu2:
                objCliente = (HashMap<String, Object>) adapter.getItem(info.position);
                campo = (String) objCliente.get("movil");
                numero = "tel:" +  campo;
                 Toast.makeText(Directorio.this, campo, Toast.LENGTH_SHORT).show();
                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse(numero));
                    startActivity(callIntent);
                } catch (ActivityNotFoundException activityException) {
                    Log.e("dialing-example", "Call failed", activityException);
                }
                return true;

            case R.id.menu_nuevo_seguimiento:
                Intent i = new Intent(Directorio.this, seguimiento_nuevo.class);
                objCliente = (HashMap<String, Object>) adapter.getItem(info.position);
                campo = (String) objCliente.get("empresa");
                String campo1 = (String) objCliente.get("id");
                i.putExtra("empresa",campo  );
                i.putExtra("id_cliente",campo1);
                startActivity(i);
            return true;


            case R.id.menu_checkpoint:
                LocationManager mlocMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!mlocMan.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    // Genera aviso de prender GPS
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                    builder1.setMessage("Encender GPS!")
                            .setTitle(" GPS-UBICACION")
                            .setIcon(R.drawable.droid_swoox)
                            .setCancelable(false)
                            .setNeutralButton("Aceptar", null);
                    AlertDialog alert1 = builder1.create();
                    alert1.show();
                    return true;
                }

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                objCliente = (HashMap<String, Object>) adapter.getItem(info.position);
                String emp = (String) objCliente.get("empresa");
                id_cliente= objCliente.get("id").toString();
                alertDialog.setTitle(emp);
                alertDialog.setMessage("Para marcar una Ubicacion GPS ud. debe estar Fisicamente en las instalaciones " +
                        "del cliente, Quiere definir la ubicacion para este cliente?");
                alertDialog.setIcon(R.drawable.woox_logo);
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String msg;
                        String lng;
                        String lat;
                        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Location Loc = mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            lat = String.valueOf(Loc.getLatitude());
                            lng = String.valueOf(Loc.getLongitude());
                        } else {
                            msg = "Encianda GPS";
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(100);
                            lat = "";
                            lng = "";
                            Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(viewIntent);
                            return;
                        }

                        Gps_drive gps = new Gps_drive(id_usuario, lng, lat);
                        gps.set_point(id_usuario, lng, lat, id_cliente);
                        msg = "Ubicacion actualizada " + id_cliente;
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(200);
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        Toast.makeText(getApplicationContext(), "No se actualiza", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                // Setting Negative "NO" Button

                // Showing Alert Message
                alertDialog.show();
                return true;

            case R.id.menu_checkin:
                if(g.getestatus_visita()==1){
                    AlertDialog.Builder aviso = new AlertDialog.Builder(this);
                    aviso.setTitle("ALERTA");
                    aviso.setMessage("Tiene una visita sin concluir!!");
                    aviso.setIcon(R.drawable.woox_logo);
                    aviso.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(getApplicationContext(), "Contesto que si", Toast.LENGTH_SHORT).show();
                        }
                    });
                    // Showing Alert Message
                    aviso.show();
                 return true;
               }
                Toast.makeText(getApplicationContext(), "continua iniciando visita", Toast.LENGTH_SHORT).show();
                LocationManager mlocMan1 = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!mlocMan1.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    // Genera aviso de prender GPS
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                    builder1.setMessage("Encender GPS!")
                            .setTitle(" GPS-UBICACION")
                            .setIcon(R.drawable.droid_swoox)
                            .setCancelable(false)
                            .setNeutralButton("Aceptar", null);
                    AlertDialog alert1 = builder1.create();
                    alert1.show();
                    return true;
                }

                AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(this);
                objCliente = (HashMap<String, Object>) adapter.getItem(info.position);
                String emp1 = (String) objCliente.get("empresa");
                id_cliente= objCliente.get("id").toString();
                alertDialog1.setTitle(emp1);
                alertDialog1.setMessage(" Iniciar Visita ?");
                alertDialog1.setIcon(R.drawable.woox_logo);
                alertDialog1.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String msg;
                        String lng;
                        String lat;
                        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Location Loc = mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            lat = String.valueOf(Loc.getLatitude());
                            lng = String.valueOf(Loc.getLongitude());
                        } else {
                            msg = "Encianda GPS";
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(100);
                            lat = "";
                            lng = "";
                            Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(viewIntent);
                            return;
                        }
                        Gps_drive gps = new Gps_drive(id_usuario, lng, lat);
                        gps.check_in(id_usuario, lng, lat, id_cliente,"nota");
                        msg = "Checkin " + id_cliente;
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(200);
                        g.setestatus_visita(1);
                        g.setIdcliente_visita(id_cliente);
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        Toast.makeText(getApplicationContext(), "No se actualiza", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                // Setting Negative "NO" Button

                // Showing Alert Message
                alertDialog1.show();
                //return true;
                return false;


            case R.id.menu_checout:
                if(g.getestatus_visita()==0){
                    AlertDialog.Builder aviso = new AlertDialog.Builder(this);
                    aviso.setTitle("ALERTA");
                    aviso.setMessage("No ha Iniciado una visita");
                    aviso.setIcon(R.drawable.woox_logo);
                    aviso.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(getApplicationContext(), "Contesto que si", Toast.LENGTH_SHORT).show();
                        }
                    });
                    // Showing Alert Message
                    aviso.show();
                    return true;
                }

                LocationManager mlocMan2 = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!mlocMan2.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    // Genera aviso de prender GPS
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                    builder1.setMessage("Encender GPS!")
                            .setTitle(" GPS-UBICACION")
                            .setIcon(R.drawable.droid_swoox)
                            .setCancelable(false)
                            .setNeutralButton("Aceptar", null);
                    AlertDialog alert1 = builder1.create();
                    alert1.show();
                    return true;
                }

                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(this);
                objCliente = (HashMap<String, Object>) adapter.getItem(info.position);
                String emp2 = (String) objCliente.get("empresa");
                id_cliente= objCliente.get("id").toString();
                alertDialog2.setTitle(emp2);
                alertDialog2.setMessage(" Terminar Visita ?");
                alertDialog2.setIcon(R.drawable.woox_logo);
                alertDialog2.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String msg;
                        String lng;
                        String lat;
                        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Location Loc = mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            lat = String.valueOf(Loc.getLatitude());
                            lng = String.valueOf(Loc.getLongitude());
                        } else {
                            msg = "Encianda GPS";
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(100);
                            lat = "";
                            lng = "";
                            Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(viewIntent);
                            return;
                        }
                        Gps_drive gps = new Gps_drive(id_usuario, lng, lat);
                        gps.check_out(id_usuario, lng, lat, id_cliente, "cierre de visita");
                        msg = "Check out " + id_cliente;
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(200);
                        g.setestatus_visita(0);
                        g.setIdcliente_visita(id_cliente);
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        Toast.makeText(getApplicationContext(), "No se actualiza", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                // Setting Negative "NO" Button

                // Showing Alert Message
                alertDialog2.show();
                //return true;
                return false;

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
            telefono = (TextView) findViewById(R.id.telefono);
            extencion = (TextView) findViewById(R.id.extencion);
            movil = (TextView) findViewById(R.id.movil);
            email = (TextView) findViewById(R.id.email);
            razon = (TextView) findViewById(R.id.razon);
            pDialog = new ProgressDialog(Directorio.this);
            pDialog.setMessage("Conectando ...");
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
                // Getting JSON Array from URL
                android = json.getJSONArray(TAG_CONSULTA);
                oslist.clear();
                for (int i = 0; i < android.length(); i++) {
                    JSONObject c = android.getJSONObject(i);
                    // Storing  JSON item in a Variable
                    String sid = c.getString(TAG_ID);
                    String snombre = c.getString(TAG_NOMBRE).toUpperCase() ;
                    String sempresa = c.getString(TAG_EMPRESA).toUpperCase();
                    String stelefono = c.getString(TAG_TELEFONO);
                    String sextencion = c.getString(TAG_EXTENCION);
                    String smovil = c.getString(TAG_MOVIL);
                    String semail = c.getString(TAG_EMAIL);
                    String srazon = c.getString(TAG_RAZON);
                    // Adding value HashMap key => value
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(TAG_ID, sid);
                    map.put(TAG_EMPRESA, sempresa);
                    map.put(TAG_NOMBRE, snombre);
                    map.put(TAG_TELEFONO, stelefono);
                    map.put(TAG_EXTENCION, sextencion);
                    map.put(TAG_MOVIL, smovil);
                    map.put(TAG_EMAIL, semail);
                    map.put(TAG_RAZON,srazon);
                    oslist.add(map);
                    list = (ListView) findViewById(R.id.list);
                    adapter = new SimpleAdapter(Directorio.this, oslist,
                            R.layout.list_directorio,
                            new String[]{TAG_ID, TAG_EMPRESA, TAG_NOMBRE, TAG_TELEFONO, TAG_EXTENCION, TAG_MOVIL, TAG_EMAIL,TAG_RAZON}, new int[]{
                            R.id.id, R.id.empresa, R.id.nombre, R.id.telefono, R.id.extencion, R.id.movil, R.id.email,R.id.razon});
                    list.setAdapter(adapter);
                    list.setTextFilterEnabled(true);
                    TextView titulo = (TextView) findViewById(R.id.titulo_directorio);
                    elementos = list.getCount();
                    titulo.setText("Mis Clientes (" + String.valueOf(elementos) + ")");
                    registerForContextMenu(list);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                            extencion = (TextView) findViewById(R.id.extencion);
//                            movil = (TextView) findViewById(R.id.movil);
//                            email = (TextView) findViewById(R.id.email);
//                            extencion.setVisibility(View.VISIBLE);
//                            movil.setVisibility(View.VISIBLE);
//                            email.setVisibility(View.VISIBLE);
                        }
                    });
                    // ***** MENU Deslizar
                    SwipeListViewTouchListener touchListener =new SwipeListViewTouchListener(list,new SwipeListViewTouchListener.OnSwipeCallback() {
                    // ***** SI ES A LA IZQUIERDA SACA HISTORIAL
                        @Override
                        public void onSwipeLeft(ListView listView, int [] reverseSortedPositions) {
                            objCliente = (HashMap<String, Object>) adapter.getItem(reverseSortedPositions[0]);
                            String miid = (String) objCliente.get("id");
                            Intent i = new Intent(Directorio.this, Historial.class);
                            i.putExtra("id_cliente", miid);
                            startActivity(i);
                        }
                        // ***** SI ES A LA IZQUIERDA SACA HISTORIAL
                        @Override
                        public void onSwipeRight(ListView listView, int [] reverseSortedPositions) {
                            objCliente = (HashMap<String, Object>) adapter.getItem(reverseSortedPositions[0]);
                            String campo = (String) objCliente.get("movil");
                            numero = "tel:" +  campo;
                            Toast.makeText(Directorio.this, campo, Toast.LENGTH_SHORT).show();
                            try {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse(numero));
                                startActivity(callIntent);
                            } catch (ActivityNotFoundException activityException) {
                                Log.e("dialing-example", "Call failed", activityException);
                            }
                            }
                    },true, false);
                    list.setOnTouchListener(touchListener);
                    list.setOnScrollListener(touchListener.makeScrollListener());
                }
            } catch (JSONException e) {
                Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(200);
                Toast toast1 = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
                toast1.show();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
       if (v.getId() == R.id.list) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
           objCliente = (HashMap<String, Object>) adapter.getItem(info.position);
           String minombre= (String) objCliente.get("razon");
            menu.setHeaderTitle(minombre);
            inflater.inflate(R.menu.context_directorio, menu);
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }
}

