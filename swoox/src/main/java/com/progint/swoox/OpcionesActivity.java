package com.progint.swoox;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.progint.swoox.library.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class OpcionesActivity extends PreferenceActivity {
    private static final String TAG_CONSULTA = "android";
    JSONArray consulta = null;
    String url;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.opciones);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_opciones, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.validar:
                String id;
                String login;
                String password;
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
                id=sharedPrefs.getString("idusuario", "NULL");
                login=sharedPrefs.getString("usuario","NULL");
                password=sharedPrefs.getString("Password","NULL");
                url="http://www.swoox.com.mx/app/sql_validar.php?id_usuario="+id+"&login="+login+"&password="+password;
                new JSONParse().execute();
                return true;
            case R.id.opciones_salir:
                SharedPreferences.Editor Prefs1 = getPreferences(MODE_PRIVATE).edit();
                Prefs1.putString("idusuario","0");
                Prefs1.commit();
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(OpcionesActivity.this);
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
                consulta = json.getJSONArray(TAG_CONSULTA);
                JSONObject c = consulta.getJSONObject(0);
                //ller los datos de c
                String nombre;
                String nivel;
                nivel=c.getString("nivel");
                nombre="Bienvenido "+c.getString("name")+" Nivel "+nivel.toString();
                Toast toast1 = Toast.makeText(getApplicationContext(), nombre, Toast.LENGTH_LONG);
                toast1.show();
                Intent i = new Intent(OpcionesActivity.this, resumen.class);
                startActivity(i);

            } catch (JSONException e) {
                Toast toast1 = Toast.makeText(getApplicationContext(), "Verificar Informacion", Toast.LENGTH_LONG);
                toast1.show();
                e.printStackTrace();
            }
        }
    }
}