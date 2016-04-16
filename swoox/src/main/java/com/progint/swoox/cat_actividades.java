package com.progint.swoox;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.progint.swoox.library.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class cat_actividades extends Activity {
    ListView list;
    TextView mlogin;
    TextView name;
    TextView password;
    Button Btngetdata;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();

    //URL to get JSON Array
    private static String url = "http://www.swoox.com.mx/app/consultarUsuario.php";

    //JSON Node Names
    private static final String TAG_CONSULTA = "android";
    private static final String TAG_LOGIN = "login";
    private static final String TAG_PASSWORD = "password";
    private static final String TAG_NAME = "name";

    JSONArray android = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cat_actividades);
        oslist = new ArrayList<HashMap<String, String>>();

        Btngetdata = (Button) findViewById(R.id.getdata);
        Btngetdata.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new JSONParse().execute();

            }
        });


    }


    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mlogin = (TextView) findViewById(R.id.login);
            name = (TextView) findViewById(R.id.name);
            password = (TextView) findViewById(R.id.password);
            pDialog = new ProgressDialog(cat_actividades.this);
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
                for (int i = 0; i < android.length(); i++) {
                    JSONObject c = android.getJSONObject(i);
                    // Storing  JSON item in a Variable
                    String slogin = c.getString(TAG_LOGIN);
                    String sname = c.getString(TAG_NAME);
                    String spassword = c.getString(TAG_PASSWORD);


                    // Adding value HashMap key => value


                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put(TAG_LOGIN, slogin);
                    map.put(TAG_PASSWORD, spassword);
                    map.put(TAG_NAME, sname);

                    oslist.add(map);
                    list = (ListView) findViewById(R.id.list);

                    ListAdapter adapter = new SimpleAdapter(cat_actividades.this, oslist,
                            R.layout.list_v,
                            new String[]{TAG_LOGIN, TAG_PASSWORD, TAG_NAME}, new int[]{
                            R.id.login, R.id.password, R.id.name});

                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            Toast.makeText(cat_actividades.this, "You Clicked at " + oslist.get(+position).get("name"), Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

}
