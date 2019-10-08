package com.haunchen.mysql_test;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetDatasAsyncTask extends AsyncTask<String, Integer, String> {
    private ProgressDialog progressBar;

    public AsyncTaskResult<String> connectionResult = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressBar = new ProgressDialog(MyContext.getContext());
        progressBar.setMessage("請稍後...");
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        String result = "";

        result = getData(strings[0]);
        publishProgress(100);
        //Log.e("getData", result);

        return result;
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

        this.connectionResult.taskFinish(result);
    }

    private String getData(String... strings){
        StringBuilder serverResponse =  new StringBuilder();

        try{
            URL url = new URL(Url.getDatas + "?table=" + strings[0]);
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

        return serverResponse.toString();
    }
}
