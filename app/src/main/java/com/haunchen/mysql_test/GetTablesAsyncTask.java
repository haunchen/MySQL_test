package com.haunchen.mysql_test;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetTablesAsyncTask extends AsyncTask<String, Integer, String> {
    private ProgressDialog progressBar;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressBar = new ProgressDialog(MyContext.getContext());
        progressBar.setMessage("處理中...");
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        StringBuilder serverResponse =  new StringBuilder();

        try {
            URL url = new URL(Url.getTables);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setConnectTimeout(2000);
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if(httpURLConnection.getResponseCode() == 200){
                String temp;
                InputStream is = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                while((temp = reader.readLine()) != null)
                    serverResponse.append(temp);

                reader.close();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        publishProgress(100);

        return serverResponse.toString();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressBar.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        progressBar.dismiss();

        WelcomeActivity.tables = result.split(" ");
        ArrayAdapter<String> tablesList = new ArrayAdapter<>(MyContext.getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                WelcomeActivity.tables);
        WelcomeActivity.spinner.setAdapter(tablesList);
    }
}
