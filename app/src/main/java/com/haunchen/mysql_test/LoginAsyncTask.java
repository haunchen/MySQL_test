package com.haunchen.mysql_test;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LoginAsyncTask extends AsyncTask<String, Integer, String> {
    private ProgressDialog  progressBar;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressBar = new ProgressDialog(MyContext.getContext());
        progressBar.setMessage("登入中...");
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.show();
    }

    @Override       //執行中
    protected String doInBackground(String... strings) {
        String login = "";
        try{
            login = login(strings);
            publishProgress(100);

            Log.e("login", login);
        } catch (Exception e){
            e.printStackTrace();
        }
        return login;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressBar.setProgress(values[0]);
    }

    @Override   //處裡網頁回傳資料
    protected void onPostExecute(String login) {
        super.onPostExecute(login);

        progressBar.dismiss();

        if(login.equals("success")){
            Log.e("Login State", "登入成功");
            Toast.makeText(MyContext.getContext(), "登入成功", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent();
            intent.setClass(MyContext.getContext(), WelcomeActivity.class);

            MyContext.getContext().startActivity(intent);
            LoginActivity.instance.finish();
        }
        else{
            Log.e("Login State", "登入失敗");
        }
    }

    private String login(String... strs){
        StringBuilder serverResponse =  new StringBuilder();

        Map<String, String> params = new HashMap<>();
        params.put("account", strs[0]);
        params.put("password", strs[1]);

        try {
            URL url = new URL(Url.login);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setConnectTimeout(2000);
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");

            OutputStream os = httpURLConnection.getOutputStream();
            DataOutputStream writer = new DataOutputStream(os);
            String JsonString = new JSONObject(params).toString(2);
            Log.e("JsonString", JsonString);
            writer.writeBytes(JsonString);
            writer.close();
            os.close();

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
