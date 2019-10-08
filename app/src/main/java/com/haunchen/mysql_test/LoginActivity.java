package com.haunchen.mysql_test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    static LoginActivity instance;

    Button send_bt;
    EditText acc_et, pass_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MyContext.setContext(this);
        instance = this;

        acc_et = findViewById(R.id.acc_et);
        pass_et = findViewById(R.id.pass_et);

        send_bt = findViewById(R.id.send_bt);
        send_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!acc_et.getText().toString().equals("") && !pass_et.getText().toString().equals("")) {
                    new LoginAsyncTask().execute(acc_et.getText().toString(), pass_et.getText().toString());
                }
                else{
                    if(acc_et.getText().toString().equals("")){
                        acc_et.setError("請輸入帳號");
                    }
                    if(pass_et.getText().toString().equals("")){
                        pass_et.setError("請輸入密碼");
                    }
                    Toast.makeText(MyContext.getContext(), "帳號/密碼 不得空白", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
