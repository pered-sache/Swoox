package com.progint.swoox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Created by eduardo on 18/02/14.
 */
public  class email extends Activity  {
    String email;
    String asunto;
    String mensaje;
    EditText edasunto;
    EditText edmensaje;
    public void onCreate(Bundle savedIstanceState) {
        super.onCreate(savedIstanceState);
        Bundle extras = getIntent().getExtras();
       email = extras.getString("correo");
        setContentView(R.layout.email);

        TextView mmail = (TextView) findViewById(R.id.mail_email);
        mmail.setText(email);
        edasunto = (EditText) findViewById(R.id.mail_asunto);
        edmensaje = (EditText) findViewById(R.id.mail_contenido);


        findViewById(R.id.btn_cancelar).setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                finish();
            }
        });

        findViewById(R.id.btn_enviar).setOnClickListener(new OnClickListener(){
            public void onClick(View arg0) {
                asunto=edasunto.getText().toString();
                mensaje=edmensaje.getText().toString();
                Intent nemail = new Intent(Intent.ACTION_SEND);
                nemail.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                nemail.putExtra(Intent.EXTRA_SUBJECT, asunto);
                nemail.putExtra(Intent.EXTRA_TEXT, mensaje);
                nemail.setType("message/rfc822");
                startActivity(Intent.createChooser(nemail, "Seleccione cliente de Envio :"));
                finish();

            }
        });


    }


}