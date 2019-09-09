package com.haunchen.mysql_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private String postUrl = "http://192.168.1.73/login.php";

    static Handler handler; //宣告成static讓service可以直接使用
    Button send_bt;

    private String acc = null, pass = null;  //存放要Post的訊息
    private String result;  //存放Post回傳值

    EditText acc_et, pass_et;
    Http_Post HP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        send_bt = findViewById(R.id.send_bt);
        acc_et = findViewById(R.id.acc_et);
        pass_et = findViewById(R.id.pass_et);

        HP = new Http_Post();

        handler = new Handler(){
            public void handleMessage(Message msg){
                switch (msg.what){
                    case 123:
                        String ss = (String)msg.obj;
                        Toast.makeText(MainActivity.this, ss,Toast.LENGTH_LONG).show();
                        if(ss.equals("success")){
                            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("account", acc);

                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                        break;
                }
            }
        };

        send_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (acc_et != null && pass_et != null) {
                    //取得EditText的內容
                    acc = acc_et.getEditableText().toString();
                    pass = pass_et.getEditableText().toString();
                    HP.Post(acc, pass, postUrl);
                }
            }
        });
    }
}
