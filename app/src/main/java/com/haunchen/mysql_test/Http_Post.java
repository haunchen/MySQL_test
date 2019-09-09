package com.haunchen.mysql_test;

import android.app.Service;
import android.content.Entity;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;

import androidx.annotation.Nullable;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Http_Post extends Service {

    String strTxt = null, pass = null, postURL = null, strResult = null;

    public void Post(final String strTxt, final String pass, final String postURL){
        this.strTxt = strTxt;
        this.pass = pass;
        this.postURL = postURL;

        new Thread(new Runnable() {
            @Override
            public void run() {
                //HttpClient httpClient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost(postURL);

                List params = new ArrayList();

                params.add(new BasicNameValuePair("account", strTxt));
                params.add(new BasicNameValuePair("password", pass));

                try{
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);

                    if(httpResponse.getStatusLine().getStatusCode() == 200){
                        strResult = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);

                        Message msg = Message.obtain();

                        msg.what = 123;
                        msg.obj = strResult;

                        MainActivity.handler.sendMessage(msg);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
