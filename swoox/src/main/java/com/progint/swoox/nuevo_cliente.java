package com.progint.swoox;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.progint.swoox.library.JSONParsers;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eduardo on 23/01/14.
 */
public class nuevo_cliente extends Activity {
    TabHost tabHost;
    private ProgressDialog pDialog;
    EditText iempresa;
    EditText inombre;
    EditText isexo;
    EditText iemail;
    EditText itelefono;
    EditText iextencion;
    EditText icelular;
    EditText igiro;
    EditText ipuesto;
    EditText ifrfc;
    EditText ifdireccion;
    EditText ifcolonia;
    EditText ifciudad;
    EditText ifmunicipio;
    EditText ifestado;
    EditText ifcp;
    EditText ifmail;
    CheckBox isae;
    String id_user;
    EditText icomentarios;
    EditText irazon;
    boolean save;
    String sqlresult;
    String empresa ;
    String nombre ;
    String sexo = "H";
    String email;
    String telefono1 ;
    String extencion ;
    String celular ;
    String giro ;
    String puesto;
    String rfc;
    String direccion ;
    String colonia;
    String ciudad ;
    String municipio;
    String estado ;
    String cp ;
    String fiscal_email;
    String Comentarios;
    String origen;
    String zona;
    String razon;
    String existerfc;
    String sae;

    private static String url_crear_cliente = "http://www.swoox.com.mx/app/add_cliente.php";
    private static final String TAG_SUCCESS = "success";
    //JSONParsers jsonParser = new JSONParsers();

    @Override
    public void onCreate(Bundle savedIstanceState) {
        super.onCreate(savedIstanceState);
        setContentView(R.layout.nuevo_cliente);
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        id_user =sharedPrefs.getString("idusuario", "NULL");
        iempresa = (EditText) findViewById(R.id.new_empresa);
        inombre = (EditText) findViewById(R.id.new_nombre);
        iemail = (EditText) findViewById(R.id.new_email);
        itelefono = (EditText) findViewById(R.id.new_telefono1);
        iextencion = (EditText) findViewById(R.id.new_extencion);
        icelular = (EditText) findViewById(R.id.new_celular);
        ipuesto = (EditText) findViewById(R.id.new_puesto);
        ifrfc = (EditText) findViewById(R.id.new_RFC);
        ifdireccion = (EditText) findViewById(R.id.new_direccion);
        ifcolonia = (EditText) findViewById(R.id.new_colonia);
        ifciudad = (EditText) findViewById(R.id.new_ciudad);
        ifmunicipio = (EditText) findViewById(R.id.new_municipio);
        ifestado = (EditText) findViewById(R.id.new_estado);
        ifcp = (EditText) findViewById(R.id.new_cp);
        ifmail = (EditText) findViewById(R.id.new_emailfiscal);
        icomentarios = (EditText) findViewById(R.id.new_comentarios);
        irazon =(EditText) findViewById(R.id.new_razon);
        isae=(CheckBox) findViewById(R.id.sae);
        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec ts = tabHost.newTabSpec("tab1");
        ts.setContent(R.id.tab1);
        ts.setIndicator("Generales");
        tabHost.addTab(ts);

        ts = tabHost.newTabSpec("tab2");
        ts.setContent(R.id.tab2);
        ts.setIndicator("Fiscales");
        tabHost.addTab(ts);

        ts = tabHost.newTabSpec("tab3");
        ts.setContent(R.id.tab3);
        ts.setIndicator("Mas");
        tabHost.addTab(ts);

    //control del spiner de Giros
        Spinner sp = (Spinner) findViewById(R.id.spinner_giros);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this, R.array.giros, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
            giro=parentView.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        //control del spiner de Origenes
        Spinner sp1 = (Spinner) findViewById(R.id.spinner_origen);
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(
                this, R.array.origen, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(adapter1);

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
                origen=parentView.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });


        //control del spiner de Zonas
        Spinner sp2 = (Spinner) findViewById(R.id.spinner_zona);
        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(
                this, R.array.zonas, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp2.setAdapter(adapter2);

        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
                zona=parentView.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nuevo_cliente, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.GuardarCliente:
                new busca_rfc().execute();
                return true;

            case R.id.salir_sin_guardar:
              finish();
              return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private  boolean valida () {
       // carga_variables();

        Boolean result;
        result=true;
        StringBuilder msg = new StringBuilder();
        msg.append("Verifique la siguiente Inf.: :\n\n");
        //if (ifrfc.length()<12) {msg.append("R.F.C. INVALIDO. \n"); result=false;}
        if (irazon.length()==0) {msg.append("Falta Razon Social. \n"); result=false;}
        if (inombre.length()==0) {msg.append("Falta Nombre de Contacto. \n"); result=false;}
        if (itelefono.length()==0) {msg.append("Falta telefono. \n\n"); result=false;}
        if (!result) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(msg)
                    .setTitle(" Registro")
                    .setIcon(R.drawable.droid_swoox)
                    .setCancelable(false)
                    .setNeutralButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            }
                    );
            AlertDialog alert = builder.create();
            alert.show();
            return result;
        }
        else {
            return result;
        }

    }

    private class busca_rfc  extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(nuevo_cliente.this);
            pDialog.setMessage("Validando RFC...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {
          if (ifrfc.getText().length()==0) {
              sqlresult="0";
              return sqlresult;
          }

          else {
              List<NameValuePair> params = new ArrayList<NameValuePair>();
              params.add(new BasicNameValuePair("rfc", ifrfc.getText().toString()));
              JSONParsers sh = new JSONParsers();
              sqlresult = sh.ExecutarSql("HTTP://www.swoox.com.mx/app/validar_rfc.php", JSONParsers.GET, params);
              return sqlresult;
          }
        }
        protected void onPostExecute(String sqlresult) {
            pDialog.dismiss();
            int n = Integer.parseInt(sqlresult);
            if(n>0) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(nuevo_cliente.this);
                alertDialog.setTitle("RFC DUPLICADO "+sqlresult);
                alertDialog.setMessage("Este RFC YA EXISTE EN LA BASE DE DATOS, AUN ASI QUIERE REGISTRARLO?");
                alertDialog.setIcon(R.drawable.abc_ic_go);
                // Setting Negative "YES" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        if (valida()) {
                            guardar();
                        }
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                alertDialog.show();
                existerfc = sqlresult;
            }
            else {
                if (valida()) {
                    guardar();
                }
            }
            return ;
        }

    }

    private void guardar (){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(nuevo_cliente.this);
        alertDialog.setTitle("Nuevo Contacto");
        alertDialog.setMessage("Esta seguro?");
        alertDialog.setIcon(R.drawable.nuevo);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                new CrearCliente().execute();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private class CrearCliente extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(nuevo_cliente.this);
            pDialog.setMessage("Preparando Info....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            save=false;
        }

        /*** Creating Cliente */

        protected String doInBackground(String... strings) {
            pDialog.setMessage("Enviando Info...");

            empresa = iempresa.getText().toString();
            nombre = inombre.getText().toString();
            sexo = "H";
            email = iemail.getText().toString();
            telefono1 = itelefono.getText().toString();
            extencion = iextencion.getText().toString();
            celular = icelular.getText().toString();
            //giro = igiro.getText().toString();
            puesto = ipuesto.getText().toString();
            rfc = ifrfc.getText().toString();
            direccion = ifdireccion.getText().toString();
            colonia = ifcolonia.getText().toString();
            ciudad = ifciudad.getText().toString();
            municipio = ifmunicipio.getText().toString();
            estado = ifestado.getText().toString();
            cp = ifcp.getText().toString();
            fiscal_email = ifmail.getText().toString();
            Comentarios = icomentarios.getText().toString();
            razon = irazon.getText().toString();
            if (isae.isChecked()){sae="1";}else{sae="0";}

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("empresa", empresa));
            params.add(new BasicNameValuePair("nombre", nombre));
            params.add(new BasicNameValuePair("sexo", sexo));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("telefono", telefono1));
            params.add(new BasicNameValuePair("extencion", extencion));
            params.add(new BasicNameValuePair("celular", celular));
            params.add(new BasicNameValuePair("giro", giro));
            params.add(new BasicNameValuePair("puesto", puesto));
            params.add(new BasicNameValuePair("idusuario",id_user ));
            params.add(new BasicNameValuePair("rfc", rfc));
            params.add(new BasicNameValuePair("direccion", direccion));
            params.add(new BasicNameValuePair("colonia", colonia));
            params.add(new BasicNameValuePair("ciudad", ciudad));
            params.add(new BasicNameValuePair("municipio", municipio));
            params.add(new BasicNameValuePair("estado", estado));
            params.add(new BasicNameValuePair("cp", cp));
            params.add(new BasicNameValuePair("fiscalmail", fiscal_email));
            params.add(new BasicNameValuePair("comentarios",Comentarios));
            params.add(new BasicNameValuePair("razon",razon));
            params.add(new BasicNameValuePair("sae",sae));
            JSONParsers  sh = new JSONParsers();
            String resultadoSQL = sh.ExecutarSql(url_crear_cliente, JSONParsers.POST, params);

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
