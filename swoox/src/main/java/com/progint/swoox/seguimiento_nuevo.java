package com.progint.swoox;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.progint.swoox.library.JSONParsers;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by eduardo on 26/02/14.
 */
public class seguimiento_nuevo extends Activity{
    String id_user;
    String id_cliente;
   int id_suceso;
    EditText nuevo_fecha;
    EditText nuevo_hora;
    EditText nuevo_descripcion;
    String suceso;
    boolean save;
    ProgressDialog pDialog;

    //Variables locales
    String lfecha;
    String lhora;
    String lconcepto;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int hour;
    private int minute;
    static final int DATE_DIALOG_ID = 0;
    String gempresa;
    TextView Empresa;
    ImageView icono;
    private static String url_crear_seguimiento = "http://www.swoox.com.mx/app/add_seguimiento.php";
    private static final String TAG_SUCCESS = "success";
    @Override
    public void onCreate(Bundle savedIstanceState) {
        super.onCreate(savedIstanceState);
        setContentView(R.layout.seguimiento_nuevo);
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        id_user =sharedPrefs.getString("idusuario", "NULL");
        Bundle extras = getIntent().getExtras();
        gempresa = extras.getString("empresa");
        Empresa = (TextView) findViewById(R.id.empresa);
        Empresa.setText(gempresa);
        id_cliente = extras.getString("id_cliente");
        nuevo_fecha = (EditText) findViewById(R.id.nuevo_fecha);
        nuevo_hora = (EditText) findViewById(R.id.nuevo_hora);
        nuevo_descripcion = (EditText) findViewById(R.id.nuevo_descripcion);

        Spinner sp = (Spinner) findViewById(R.id.spinner_suceso);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this, R.array.sucesos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                int position, long id) {
                id_suceso=position;
                suceso=parentView.getItemAtPosition(position).toString();
                icono = (ImageView) findViewById(R.id.icono_actividad);
                int idimagen = getResources().getIdentifier(suceso.toLowerCase() , "drawable","com.progint.swoox");
                icono.setImageResource(idimagen);
            }
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        findViewById(R.id.btn_fecha).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Alternativa 1
        getMenuInflater().inflate(R.menu.menu_seguimiento_nuevo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Menu_segimiento1:
                new Crear_seguimineto().execute();
                return true;
            case R.id.Menu_segimiento_cerrar:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
            nuevo_fecha.setText(String.valueOf(year) + "-" + String.valueOf(monthOfYear)
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
            nuevo_hora.setText( String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
        }
    }

    private class Crear_seguimineto extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(seguimiento_nuevo.this);
            pDialog.setMessage("Preparando Info....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            save=false;
        }

        /*** Creating Cliente */

        protected String doInBackground(String... strings) {
            pDialog.setMessage("Enviando Info...");

            lfecha= nuevo_fecha.getText().toString();
            lhora = nuevo_hora.getText().toString();
            lconcepto =nuevo_descripcion.getText().toString();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_usuario", id_user));
            params.add(new BasicNameValuePair("id_cliente", id_cliente));
            params.add(new BasicNameValuePair("id_suceso", String.valueOf(id_suceso+1)));
            params.add(new BasicNameValuePair("duracion", "05:00"));
            params.add(new BasicNameValuePair("fecha", lfecha));
            params.add(new BasicNameValuePair("hora", lhora));
            params.add(new BasicNameValuePair("descripcion",lconcepto));
            JSONParsers sh = new JSONParsers();
            String resultadoSQL = sh.ExecutarSql(url_crear_seguimiento, JSONParsers.POST, params);
            return resultadoSQL;
        }

        protected void onPostExecute(String resultado) {
            String msg ="";
            if (resultado.equals("0")) msg="No se agrego el registro"; else msg="Se agrego "+resultado.toString() ;
            Toast tmsg = Toast.makeText(getApplicationContext(),msg.toString(), Toast.LENGTH_SHORT );
            tmsg.show();
            finish();
            pDialog.dismiss();
        }

    }

}
