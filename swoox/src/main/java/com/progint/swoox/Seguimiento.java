package com.progint.swoox;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.progint.swoox.library.JSONParser;
import com.progint.swoox.library.JSONParsers;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Seguimiento extends Activity {
    private ProgressDialog pDialog;
    private static final String TAG_CONSULTA = "android";
    private static final String TAG_ID_SEGUIMIENTO = "id_seguimiento";
    private static final String TAG_ACTIVIDAD = "actividad";
    private static final String TAG_EMPRESA = "empresa";
    private static final String TAG_FECHA = "cita_fecha";
    private static final String TAG_HORA = "cita_hora";
    private static final String TAG_DESCRIPCION = "descripcion";
    ImageView image;
    String kseguimiento;
    private static String url;
    String id_seguimiento;
    TextView id;
    TextView empresa;
    TextView actividad;
    TextView fecha;
    TextView hora;
    TextView hecho_fecha;
    static TextView hecho_hora;
    TextView descripcion;
    JSONArray android = null;
    boolean save;
    String url_cerrar_seguimineto;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int hour;
    private int minute;
    static final int DATE_DIALOG_ID = 0;

    public Seguimiento() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seguimiento);
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        Bundle extras = getIntent().getExtras();
        id_seguimiento = extras.getString("id_seguimiento");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        url = "http://www.swoox.com.mx/app/sql_una_actividad.php?id=" + id_seguimiento;
        url_cerrar_seguimineto="http://www.swoox.com.mx/app/cerrar_seguimiento.php";
        new JSONParse().execute();
        hecho_fecha = (TextView) findViewById(R.id.hecho_fecha);
        hecho_hora =(TextView) findViewById(R.id.hecho_hora);
        setCurrentTimeOnView();


        findViewById(R.id.btn_fecha).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
    }

//Set time en la variable hecho_hora
    public void setCurrentTimeOnView() {
        TextView tvhora = (TextView) findViewById(R.id.hecho_hora);
        TextView tvfecha = (TextView) findViewById(R.id.hecho_fecha);

        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // set current time into textview
        tvhora.setText(
                new StringBuilder().append(pad(hour))
                        .append(":").append(pad(minute)));
        tvfecha.setText(
                new StringBuilder().append(mYear).append("-").append(pad(mMonth)).append("-").append(pad(mDay))
        );


    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

       DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            hecho_fecha.setText( String.valueOf(year) + "-" + String.valueOf(monthOfYear)
                            + "-" + String.valueOf(dayOfMonth));
        }
    };

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Alternativa 1
        getMenuInflater().inflate(R.menu.menu_seguimiento, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Menu_segimiento_cerrar:
                new cerrar_seguimiento().execute();
                return true;
            case R.id.Menu_segimiento1:
               finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //nombre = (TextView) findViewById(R.id.nombre);
            actividad = (TextView) findViewById(R.id.seguimiento);
            empresa = (TextView) findViewById(R.id.empresa);
            fecha = (TextView) findViewById(R.id.fecha);
            hora = (TextView) findViewById(R.id.hora);
            descripcion = (TextView) findViewById(R.id.descripcion);
            image = (ImageView) findViewById(R.id.icono_actividad);
            //movil = (TextView) findViewById(R.id.movil);
            //email = (TextView) findViewById(R.id.email);

            pDialog = new ProgressDialog(Seguimiento.this);
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

                JSONObject c = android.getJSONObject(0);
                empresa.setText(c.getString(TAG_EMPRESA));
                actividad.setText(c.getString(TAG_ACTIVIDAD));
                fecha.setText(c.getString(TAG_FECHA));
                hora.setText(c.getString(TAG_HORA));
                descripcion.setText(c.getString(TAG_DESCRIPCION));
                kseguimiento = c.get(TAG_ACTIVIDAD).toString();
                int idimagen = getResources().getIdentifier(kseguimiento.toLowerCase() , "drawable","com.progint.swoox");
                image.setImageResource(idimagen);


            } catch (JSONException e) {
                Toast toast1 = Toast.makeText(getApplicationContext(), "No se encontro actividad", Toast.LENGTH_SHORT);
                toast1.show();
                e.printStackTrace();
            }


        }
    }


//==============================================================================
//Rutina para atualizar seguimiento
//==============================================================================
    private class cerrar_seguimiento extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Seguimiento.this);
            pDialog.setMessage("Preparando Info....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            save=false;
        }
        /*** Creating Cliente */
        protected String doInBackground(String... strings) {
            pDialog.setMessage("Enviando Info...");
            EditText ihecho_fecha = (EditText) findViewById(R.id.hecho_fecha);
            String fecha = ihecho_fecha.getText().toString();

            EditText ihecho_hora =(EditText) findViewById(R.id.hecho_hora);
            String hora= ihecho_hora.getText().toString() ;

            EditText icomentarios = (EditText) findViewById(R.id.comentarios);
            String comentarios = icomentarios.getText().toString();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", id_seguimiento));
            params.add(new BasicNameValuePair("fecha", fecha));
            params.add(new BasicNameValuePair("hora", hora));
            params.add(new BasicNameValuePair("comentarios", comentarios));

            JSONParsers sh = new JSONParsers();
            String resultadoSQL = sh.ExecutarSql(url_cerrar_seguimineto, JSONParsers.POST, params);

            return resultadoSQL;
        }

        protected void onPostExecute(String resultado) {
            String msg ="";
            if (resultado.equals("0")) msg="No se actualizo"; else msg="Se Actualizo" ;
            Toast tmsg = Toast.makeText(getApplicationContext(),msg.toString(), Toast.LENGTH_SHORT );
            tmsg.show();
            finish();
            pDialog.dismiss();
        }

    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            hecho_hora.setText( String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
        }
    }
}
