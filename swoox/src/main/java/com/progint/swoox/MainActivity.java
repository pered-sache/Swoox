package com.progint.swoox;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.jar.JarEntry;

import static android.location.LocationManager.*;

public class MainActivity extends Activity {
    String id_user;
    String longitud;
    String Latitud;
    String niveles;
    private MediaPlayer mp;

    Globals g = Globals.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        Globals g = Globals.getInstance();
        g.setestatus_visita(0);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        TextView Nusuario = (TextView) findViewById(R.id.txtuserid);
        Nusuario.setText(sharedPrefs.getString("usuario", "NULL").toUpperCase());
        id_user=sharedPrefs.getString("idusuario","Null");
        niveles=sharedPrefs.getString("nivel","0");

        TextView ver= (TextView) findViewById(R.id.txtver);
        TextView idcelular =(TextView) findViewById(R.id.txtidcelular);
        String miver=getVersion();
        String micel=getPhoneNumber().toString();
        miver=miver+" N "+niveles.toString();
        ver.setText(miver);
        idcelular.setText(getPhoneNumber());

        if (Nusuario.getText().toString().equals("NULL")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            StringBuilder sb = new StringBuilder();
            sb.append("Debe registrar su App, requiere :\n\n");
            sb.append("ID, Nobre de usuario y Contraseña.");
            builder.setMessage(sb)
                    .setTitle(" Registro")
                    .setIcon(R.drawable.droid_swoox)
                    .setCancelable(false)
                    .setNeutralButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    startActivity(new Intent(MainActivity.this, OpcionesActivity.class));
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }

         //----- usa la instancaia de myLocationListener
        //LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //MyLocationListener mlocListener = new MyLocationListener();
       //mlocListener.setMainActivity(this);
       //mlocManager.requestLocationUpdates( GPS_PROVIDER, 15*60000 ,100 , mlocListener);

        TextView nfecha = (TextView) findViewById(R.id.txtfecha);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        nfecha.setText(df.format(c.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        if (niveles.contains("1")) {
        menu.findItem(R.id.resumen_total).setEnabled(true);
        }else {
            menu.findItem(R.id.resumen_total).setEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this,
                        OpcionesActivity.class));
                return true;
            case R.id.about:

                            Toast.makeText(getApplicationContext(), g.getIdcliente_visita() , Toast.LENGTH_SHORT).show();
              return true;
            case R.id.Resumen:
                startActivity(new Intent(MainActivity.this,
                        resumen.class));
                return true;
            case R.id.resumen_total:
                    startActivity(new Intent(MainActivity.this,Resumen_Actividades.class));
                    return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    public void Cat_actividades(View view) {
        Intent i = new Intent(this, Actividades.class);
        startActivity(i);
    }

    public void nuevo_cliente(View view) {
        Intent i = new Intent(this, nuevo_cliente.class);
        startActivity(i);
    }

    public void Directorio(View view) {
        Intent i = new Intent(this, Directorio.class);
        startActivity(i);
    }

    public void Actividades(View view) {
        Intent i = new Intent(this, Actividades.class);
        startActivity(i);
    }

    public void Checkpoint (View view){
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (mlocManager.isProviderEnabled(GPS_PROVIDER)) {

            MyLocationListener mlocListener = new MyLocationListener();
            mlocListener.setMainActivity(this);
                //  mlocManager.requestSingleUpdate(GPS_PROVIDER, (LocationListener) this, null);
            mlocManager.requestLocationUpdates(GPS_PROVIDER, 15 * 60000, 100, mlocListener);
           // mlocManager.requestLocationUpdates(GPS_PROVIDER , 15*60000,  0, (LocationListener) this);
            try  {
                Location Loc =mlocManager.getLastKnownLocation(GPS_PROVIDER);
                setLocation(Loc);
                Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(200);
            }  catch  (  SecurityException e )  { e . printStackTrace ();  }

           // Location Loc =mlocManager.getLastKnownLocation(GPS_PROVIDER);

           // mlocManager.removeUpdates((LocationListener) this);
           // setLocation(Loc);
            Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(200);
        } else {
            // Genera aviso de prender GPS
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Encender GPS!")
                    .setTitle(" GPS-UBICACION")
                    .setIcon(R.drawable.droid_swoox)
                    .setCancelable(false)
                    .setNeutralButton("Aceptar", null);
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void vecinos (View view) {
        Intent i=new Intent(this,Alaredonda.class);
        startActivity(i);
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    private static String Hora(){
        final Calendar c = Calendar.getInstance();
        return String.valueOf(new StringBuilder().append(pad( c.get(Calendar.HOUR_OF_DAY))).append(":").append(pad(c.get(Calendar.MINUTE))));
    }

    public void setLocation(Location loc){
        try {
        Latitud=String.valueOf(loc.getLatitude());
        longitud=String.valueOf(loc.getLongitude());
        Gps_drive gps = new Gps_drive(id_user,longitud,Latitud );
        gps.update(id_user, longitud, Latitud);
            Toast toast1 = Toast.makeText(getApplicationContext(), "Ubicacion registrada Ok.", Toast.LENGTH_SHORT);
            toast1.show();
            mp = MediaPlayer.create(MainActivity.this,R.raw.click);
            mp.start();
        } catch (Exception e) {
            Toast toast1 = Toast.makeText(getApplicationContext(),e.getMessage().toString(), Toast.LENGTH_SHORT);
            toast1.show();
        }
    }

    private  String getVersion ()  {
        String version =  "Ver. 2.9" ;
             return version ;
    }

    private String getPhoneNumber(){
        TelephonyManager mTelephonyManager;
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyManager.getDeviceId() ;
    }



}