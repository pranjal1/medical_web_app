package com.example.pranjal.medicalweb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView SignIN, LogIN;
    Intent first = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            setContentView(R.layout.activity_main);
        }
        else{
            setContentView(R.layout.less_api_main);
        }
        SignIN = (TextView)findViewById(R.id.tvSignUp);
        LogIN = (TextView)findViewById(R.id.tvLogIn);
        SignIN.setOnClickListener(this);
        LogIN.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tvSignUp:
                first = new Intent("MedicalWeb.profession");
                startActivity(first);
                break;
            case R.id.tvLogIn:
                first = new Intent("MedicalWeb.logindetails");
                startActivity(first);
                break;
        }
    }
}
