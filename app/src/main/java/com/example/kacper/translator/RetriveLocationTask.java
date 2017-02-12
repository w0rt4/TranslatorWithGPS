package com.example.kacper.translator;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Kacper on 2016-12-26.
 */

 class RetriveLocationTask extends AsyncTask<String, Void, String> {

    public String response1,response2;
    private Exception exception;
    public static String access_token="nvqutv8uL1jGfZ6aUpuN6UhGPm4orcSp";

    @Override
    protected String doInBackground(String... strings) {
        try {

            URL url = new URL("http://www.mapquestapi.com/geocoding/v1/reverse?");
            HttpURLConnection client = (HttpURLConnection) url.openConnection();

            client.setRequestMethod("GET");
            client.setConnectTimeout(20000);
            client.setReadTimeout(20000);
            client.setDoInput(true);
            client.setDoOutput(true);

            String urlParameters = "key=" + access_token + "&location=" + strings[0]+","+strings[1];

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
            JSONObject jsonObject = new JSONObject(response1);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            int j = jsonArray.length();
            JSONObject jsonObject1 =(JSONObject) jsonArray.get(0);
            JSONArray jsonArray1 = jsonObject1.getJSONArray("locations");
            JSONObject jsonObject2 =  jsonArray1.getJSONObject(0);



            response2 = jsonObject2.get("adminArea1").toString();

            return response2;
        }
        catch(Exception ie)
        {
            exception=ie;
            return null;
        }


    }
    @Override
    protected void onPostExecute(String response) {
        response1 = response.toString();

    }
}
