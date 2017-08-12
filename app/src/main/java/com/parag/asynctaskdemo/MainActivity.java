package com.parag.asynctaskdemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private final String URL_STRING = "https://dog.ceo/api/breeds/list/all";
    ProgressBar progressBar;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        textView = (TextView)findViewById(R.id.txtview);
        new MyTask().execute(URL_STRING);
    }

    private class MyTask extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... url) {
            String stringUrl = url[0];
            String result;
            String inputLine;
            try {

                URL myUrl = new URL(stringUrl);
                HttpURLConnection connection =(HttpURLConnection) myUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(15000);
                connection.connect();
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                reader.close();
                streamReader.close();
                result = stringBuilder.toString();
            }
            catch(IOException e){
                e.printStackTrace();
                result = null;
            }
            return result;
        }


        @Override
        protected void onPostExecute(String ouputJson) {
            super.onPostExecute(ouputJson);
            progressBar.setVisibility(View.INVISIBLE);

            try {
                JSONObject json = new JSONObject(ouputJson);
                JSONObject messageJsonObject = json.getJSONObject("message");
                Iterator<String> dogNames = messageJsonObject.keys();
                StringBuilder dogStringBuilder = new StringBuilder();
                while(dogNames.hasNext())
                {
                    String dogName = dogNames.next();
                    dogStringBuilder.append(" | ").append(dogName);
                }
                textView.setText(dogStringBuilder);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}


