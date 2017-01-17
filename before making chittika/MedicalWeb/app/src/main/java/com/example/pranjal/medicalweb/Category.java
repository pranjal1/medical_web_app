package com.example.pranjal.medicalweb;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.util.Log;


public class Category extends AppCompatActivity implements View.OnClickListener{
    TextView TextIntern,TextStudent,TextTeacher,TextMP;
    Intent fourth=null;
    int fromprevpage=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            setContentView(R.layout.activity_category);
        }
        else{
            setContentView(R.layout.less_activity_category);
        }


        Bundle extras = getIntent().getExtras();
        if(extras!= null){
            try {
                fromprevpage = Integer.parseInt(extras.getString("fromProfPage"));
            }catch (Exception e){
            }
        }



        TextIntern = (TextView)findViewById(R.id.tvIntern);
        TextStudent = (TextView)findViewById(R.id.tvStudent);
        TextTeacher = (TextView)findViewById(R.id.tvTeacher);
        TextMP = (TextView)findViewById(R.id.tvMedical_profession);
        TextIntern.setOnClickListener(this);
        TextStudent.setOnClickListener(this);
        TextTeacher.setOnClickListener(this);
        TextMP.setOnClickListener(this);
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
        fourth = new Intent("MedicalWeb.signupdetails");
        String convertedinteger = String.valueOf(fromprevpage);
        fourth.putExtra("fromCatPage", convertedinteger);
        startActivity(fourth);
    }
}
