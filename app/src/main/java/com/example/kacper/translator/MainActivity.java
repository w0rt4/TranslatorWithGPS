package com.example.kacper.translator;

import android.content.Intent;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.ArrayList;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;
import android.speech.tts.TextToSpeech.OnInitListener;



public class MainActivity extends AppCompatActivity {

    String message = "pl";
    int REQ_CODE =1;
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Button) findViewById(R.id.tlumacz)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent startTranslateActivity = new Intent(MainActivity.this, Activity2.class);
                startTranslateActivity.putExtra(EXTRA_MESSAGE,message);
                startActivity(startTranslateActivity);
            }
        });
        ((Button) findViewById(R.id.wykryj)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent startLocationActivity = new Intent(getApplicationContext(),Location.class);
                startActivityForResult(startLocationActivity,REQ_CODE);
            }
        });

        ((Button) findViewById(R.id.informacje)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent startInfoActivity = new Intent(MainActivity.this,CreditsActivity.class);
                startActivity(startInfoActivity);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode==REQ_CODE&&resultCode==RESULT_OK&&data!=null)
        {
            message=data.getStringExtra("Lokalizacja");
            Button wykryj=(Button) findViewById(R.id.wykryj);
            wykryj.setText(message);
        }
    }
}