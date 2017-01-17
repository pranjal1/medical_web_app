package com.example.pranjal.medicalweb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.util.Log;

public class Case_details extends AppCompatActivity {
    Bundle abc=null;
    TextView[] tvs=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_details);
        try{
        abc=this.getIntent().getExtras();
        String[] fillall = abc.getStringArray("key1");


         for(int k=6;k<11;k++){
             fillall[k]=fillall[k].replaceAll("#","\n");
         }

        tvs =new TextView[11];
        tvs[0]= (TextView)findViewById(R.id.patTitle);
        tvs[1]= (TextView)findViewById(R.id.patName);
        tvs[2]= (TextView)findViewById(R.id.patAge);
        tvs[3]= (TextView)findViewById(R.id.patSex);
        tvs[4]= (TextView)findViewById(R.id.patWeight);
        tvs[5]= (TextView)findViewById(R.id.patAddr);
        tvs[6]= (TextView)findViewById(R.id.patComp);
        tvs[7]= (TextView)findViewById(R.id.patOnExam);
        tvs[8]= (TextView)findViewById(R.id.patAssess);
        tvs[9]= (TextView)findViewById(R.id.patDiag);
        tvs[10]= (TextView)findViewById(R.id.patPlan);

        for(int i=0; i<11;i++){
            tvs[i].setText(fillall[i]);
        }
    }catch (Exception e){
            Log.d("PRANJAL",e.getMessage());
        }
    }
}
