package com.progint.swoox;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by eduardo on 24/11/13.
 */
public class about extends Activity {
    @Override
    public void onCreate(Bundle savedIstanceState) {
        super.onCreate(savedIstanceState);
        setContentView(R.layout.about);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        TextView usuario = (TextView) findViewById(R.id.woox_user);
        TextView url = (TextView) findViewById(R.id.woox_url);
        TextView pw = (TextView) findViewById(R.id.lblpw);

        usuario.setText(settings.getString("usuario", ""));
        url.setText(settings.getString("url", ""));
        pw.setText((settings.getString("Password", "")));
        String IP_Server = settings.getString("url", "");
        Toast t = Toast.makeText(this, IP_Server, Toast.LENGTH_LONG);
        t.show();


    }
}
