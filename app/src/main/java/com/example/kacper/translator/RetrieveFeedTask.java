package com.example.kacper.translator;

/**
 * Created by Kacper on 2016-12-10.
 */

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;


import com.google.gson.Gson;




class RetrieveFeedTask extends AsyncTask<String, Void, String>
{
    public static String access_token="trnsl.1.1.20161219T093245Z.45672d23715fc294.d2774f07b176eaca334a13e8661f205aa61f77fc";
    public String token_type;
    public String expires_in;
    public String scope;
    public String response1;


    @NonNull




    private Exception exception;
    @Override
    protected String  doInBackground(String... strings) {

        try {

            URL url = new URL("https://translate.yandex.net/api/v1.5/tr.json/translate?");
            HttpURLConnection client = (HttpURLConnection) url.openConnection();

            client.setRequestMethod("POST");
            client.setConnectTimeout(20000);
            client.setReadTimeout(20000);
            client.setDoInput(true);
            client.setDoOutput(true);

            String urlParameters = "text=" + strings[0] + "&" + "lang=" + strings[1] + "-" + strings[2] + "&key=" + access_token;

            OutputStream os = client.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            bw.write(urlParameters);
            bw.flush();
            bw.close();
            os.close();

            int responseCode = client.getResponseCode();

            System.out.println("Sending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            response1=response.toString();

            return response1;  //nie wiadomo czemu wpierdala sie do catch
        }
        catch(IOException ie)
        {
            exception=ie;
            return null;
        }



    }
    @Override
    protected void onPostExecute(String response) {
        response1=response.toString();

    }


}