package com.example.pranjal.medicalweb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

public class Profession extends AppCompatActivity implements View.OnClickListener {

    TextView medical, nonmedical;
    Intent third = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            setContentView(R.layout.activity_profession);
        }
        else{
            setContentView(R.layout.less_activity_profession);
        }


        medical = (TextView)findViewById(R.id.tvMedical);
        nonmedical = (TextView)findViewById(R.id.tvNonMedical);
        nonmedical.setOnClickListener(this);
        medical.setOnClickListener(this);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Read values from the "savedInstanceState"-object and put them in your textview
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the values you need from your textview into "outState"-object
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.tvMedical:
                third = new Intent("MedicalWeb.category");
                third.putExtra("fromProfPage","1");
                startActivity(third);
                break;
            case R.id.tvNonMedical:
                third= new Intent("MedicalWeb.signupdetails");
                startActivity(third);
                break;
        }

    }
}
