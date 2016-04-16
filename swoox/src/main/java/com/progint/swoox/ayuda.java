package com.progint.swoox;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Eduardo on 29/04/14.
 */
public class ayuda extends Activity {
    @Override
    public void onCreate(Bundle savedIstanceState) {
        super.onCreate(savedIstanceState);
        setContentView(R.layout.ayuda);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        findViewById(R.id.btn_ayuda_ok).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                finish();
            }
        });
    }

}
