package com.example.kacper.translator;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Locale;

public class Activity2 extends Activity implements TextToSpeech.OnInitListener {

    ImageButton imageButton;
    EditText tu;
    Spinner spiner1;
    Spinner spiner2;
    String message;
    String from = "pl";
    String to = "en";
    private TextToSpeech tts;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        imageButton = (ImageButton) findViewById(R.id.button);
        tu = (EditText) findViewById(R.id.tu);

        spiner1 = (Spinner) findViewById(R.id.spinner);
        spiner2 = (Spinner) findViewById(R.id.spinner2);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(Activity2.this,
                                          android.R.layout.simple_list_item_1,
                                          getResources().getStringArray(R.array.jezyki));

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        from=message;

        spiner1.setAdapter(myAdapter);
        spiner2.setAdapter(myAdapter);

        spiner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                from = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spiner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                to = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if(from.equals("pl") || from.equals("PL"))
        {
            spiner1.setSelection(0);
        }
        else if(from.equals("en") || from.equals("EN"))
        {
            spiner1.setSelection(1);
        }
        else if(from.equals("de") || from.equals("DE"))
        {
            spiner1.setSelection(2);
        }
        else if(from.equals("fr") || from.equals("FR"))
        {
            spiner1.setSelection(3);
        }
        else
        {
            spiner1.setSelection(4);
        }


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Podaj sentencje do przetłumaczenia");
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
                startActivityForResult(intent, 100);

            }
        });

        tts = new TextToSpeech(this, this);
        ((Button) findViewById(R.id.bSpeak)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onInit(1);
                speakOut(((TextView) findViewById(R.id.tvTranslatedText)).getText().toString());
            }
        });

        ((Button) findViewById(R.id.bTranslate)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Lepsze tłumaczenie

                String s = tu.getText().toString();

                TextView tv = (TextView) findViewById(R.id.tvTranslatedText);
                RetrieveFeedTask rt = new RetrieveFeedTask();
                rt.execute(s, from, to);
                while ((rt.response1 == null)) ;
                String translation = rt.response1.substring(36);
                int lenght = translation.length();
                translation = translation.substring(0, lenght - 3);
                tv.setText(translation);
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == 1) {
            ArrayList<String> results;
            results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            //Toast.makeText(this, results.get(0), Toast.LENGTH_SHORT).show();

            //if the name has an ' then the SQL is failing. Hence replacing them.
            String text = results.get(0).replace("'", "");
            tu.setText(text);

        }

    }

    @Override
    public void onInit(int status) {
        // TODO Auto-generated method stub
        if (status == TextToSpeech.SUCCESS) {
            int result;
            if(to == "fr") {
                result = tts.setLanguage(Locale.FRENCH);
            }
            else if(to == "de") {
                 result = tts.setLanguage(Locale.GERMAN);
            }
            else
            {
                result = tts.setLanguage(Locale.ENGLISH);
            }
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {

                //speakOut("Ich");
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    private void speakOut(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
}
