package com.example.pranjal.medicalweb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Create_case extends AppCompatActivity {
    private EditText editTitle, editName,editAge, editSex, editWeight, editAddress, editChief, editVitals, editOnExam, editAssessment, editProv,editPlan;
    private ProgressDialog progressDialog;
    private Button bCreate;
    private ArrayList arrayList=null;
    private Bundle b=null;
    String[] infoarray=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_case);

        b=this.getIntent().getExtras();
        infoarray=b.getStringArray("key");

        bCreate = (Button) findViewById(R.id.bCreateRecord);
        editTitle = (EditText) findViewById(R.id.patTitleFill);
        editName = (EditText) findViewById(R.id.patNameFill);
        editAge = (EditText) findViewById(R.id.patAgeFill);
        editSex = (EditText) findViewById(R.id.patSexFill);
        editWeight = (EditText) findViewById(R.id.patWeightFill);
        editAddress = (EditText) findViewById(R.id.patAddrFill);
        editChief = (EditText) findViewById(R.id.patCompFill);
        editVitals = (EditText) findViewById(R.id.patVitalsFill);
        editOnExam = (EditText) findViewById(R.id.patOnExamFill);
        editAssessment = (EditText) findViewById(R.id.patAssesmentFill);
        editProv = (EditText) findViewById(R.id.patDiagFill);
        editPlan = (EditText) findViewById(R.id.patPlanFill);



        bCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //copy the content of edit text field to strings
                String yourTitle = editTitle.getText().toString().trim();
                String yourName = editName.getText().toString().trim();
                String yourAge = editAge.getText().toString().trim();
                String yourSex = editSex.getText().toString().trim();
                String yourWeight = editWeight.getText().toString().trim();
                String yourAddr = editAddress.getText().toString().trim();
                String yourChief = editChief.getText().toString().trim();
                yourChief=yourChief.replaceAll("\\n","#");//this will replace all \n with 1234
                String yourVitals = editVitals.getText().toString().trim();
                yourVitals=yourVitals.replaceAll("\\n","#");
                String yourOnExam = editOnExam.getText().toString().trim();
                yourOnExam=yourOnExam.replaceAll("\\n","#");
                String yourAssessment = editAssessment.getText().toString().trim();
                yourAssessment=yourAssessment.replaceAll("\\n","#");
                String yourProv = editProv.getText().toString().trim();
                yourProv=yourProv.replaceAll("\\n","#");
                String yourPlan = editPlan.getText().toString().trim();
                yourPlan=yourPlan.replaceAll("\\n","#");
                //putting all strings above to an arraylist
                arrayList = new ArrayList();
                arrayList.add(yourTitle);
                arrayList.add(yourName);
                arrayList.add(yourAge);
                arrayList.add(yourSex);
                arrayList.add(yourWeight);
                arrayList.add(yourAddr);
                arrayList.add(yourChief);
                arrayList.add(yourVitals);
                arrayList.add(yourOnExam);
                arrayList.add(yourAssessment);
                arrayList.add(yourProv);
                arrayList.add(yourPlan);

                //check if all fields are filled and initiate a background thread if all are filled
                //else give appropriate message
                if (!yourTitle.isEmpty() && !yourAge.isEmpty() && !yourName.isEmpty() && !yourSex.isEmpty() && (yourSex.equals("male") || yourSex.equals("female") || yourSex.equals("other")) && !yourWeight.isEmpty() && !yourChief.isEmpty() && !yourVitals.isEmpty() && !yourOnExam.isEmpty() && !yourAssessment.isEmpty() && !yourProv.isEmpty() && !yourPlan.isEmpty() ) {
                    Log.d("PRANJAL", "tyo saab milyo aba pathauna lagya");
                    new runCreateAsync().execute(arrayList);
                }else if(!(yourSex.equals("male") || yourSex.equals("female") || yourSex.equals("other"))){
                    Toast.makeText(getApplicationContext(), "Sex field entry can only be male\n female or other", Toast.LENGTH_SHORT).show();
                    editSex.setText("");
                }
                else{
                    Toast.makeText(getApplicationContext(), "Some fields are empty.\n only address field can be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    public class runCreateAsync extends AsyncTask<ArrayList, Void, JSONObject> {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        final private String REGISTER_URL = "http://10.0.2.2/insert_patient_records.php";
        //before the thread completes its task
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //to show loading message
            progressDialog = new ProgressDialog(Create_case.this);
            progressDialog.setMessage("Creating patient record \n in your database ... ");
            progressDialog.setCancelable(false);
            showRegisterDialog();

            Log.d("PRANJAL", "this is after onPreExecute");
        }
        //main task here
        @Override
        protected JSONObject doInBackground(ArrayList... params) {  //this is how parameters are declared in this class's methods
            StrictMode.setThreadPolicy(policy);
            Log.d("PRANJAL", "doInBackground wala thau ko suru");

            JSONObject json_obj = null;//declaring a json object
            String result = null;
            InputStream is = null;//input stream

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(REGISTER_URL);
            //imagine namevaluepair as 2 dimensional array
            //each row has unique data set to be stored and each column is different sections of the data set
            try {
                ArrayList<NameValuePair> NameValuePairs = new ArrayList<NameValuePair>();
                NameValuePairs.add(new BasicNameValuePair("title", params[0].get(0).toString()));
                Log.d("PRANJAL", params[0].get(0).toString());
                NameValuePairs.add(new BasicNameValuePair("name", params[0].get(1).toString()));
                Log.d("PRANJAL", params[0].get(1).toString());
                NameValuePairs.add(new BasicNameValuePair("age", params[0].get(2).toString()));
                Log.d("PRANJAL", params[0].get(2).toString());
                NameValuePairs.add(new BasicNameValuePair("sex", params[0].get(3).toString()));
                Log.d("PRANJAL", params[0].get(3).toString());
                NameValuePairs.add(new BasicNameValuePair("weight", params[0].get(4).toString()));
                Log.d("PRANJAL", params[0].get(4).toString());
                NameValuePairs.add(new BasicNameValuePair("address", params[0].get(5).toString()));
                Log.d("PRANJAL", params[0].get(5).toString());
                NameValuePairs.add(new BasicNameValuePair("chief", params[0].get(6).toString()));
                Log.d("PRANJAL", params[0].get(6).toString());
                NameValuePairs.add(new BasicNameValuePair("vitals", params[0].get(7).toString()));
                Log.d("PRANJAL", params[0].get(7).toString());
                NameValuePairs.add(new BasicNameValuePair("onexam", params[0].get(8).toString()));
                Log.d("PRANJAL", params[0].get(8).toString());
                NameValuePairs.add(new BasicNameValuePair("assessment", params[0].get(9).toString()));
                Log.d("PRANJAL", params[0].get(9).toString());
                NameValuePairs.add(new BasicNameValuePair("prov", params[0].get(10).toString()));
                Log.d("PRANJAL", params[0].get(10).toString());
                NameValuePairs.add(new BasicNameValuePair("plan", params[0].get(11).toString()));
                Log.d("PRANJAL", params[0].get(11).toString());






                Log.d("PRANJAL","namevalue pair wala array banyo");
                //this may be step to send info to database
                httppost.setEntity(new UrlEncodedFormEntity(NameValuePairs));
                Log.d("PRANJAL.", "pathako awastha");
                //this is the response from database
                //this response is defined in my php file
                HttpResponse response = httpclient.execute(httppost);
                Log.d("PRANJAL", "ayo arko tira bata");
                //loading the inputstream with data from response
                is =  response.getEntity().getContent();
                Log.d("PRANJAL", "uta bata k ayo" + is);


            } catch (Exception e) {
                e.printStackTrace();//this will print the error in logcat
                Log.d("PRANJAL", ""+e.getMessage());
            }

            //this part is to convert the response in input stream is into a form from which it can be further manipulated
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, HTTP.UTF_8), 8);
                StringBuilder SB = new StringBuilder();
                String line = null;

                while((line =  reader.readLine()) != null) {
                    SB.append(line + "\n");
                }

                is.close();
                result = SB.toString();
                Log.d("PRANJAL", result);

            } catch (IOException e) {
                e.printStackTrace();
            }

            //this part is for parsing the content of response "json parsing"
            //parsed content is in json_obj
            try {
                //json_obj = new JSONObject(result);
                json_obj = new JSONObject(result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1));
                Log.d("PRANJAL", "json fetched");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return json_obj;
        }




        //after main process is completed
        @Override
        protected void onPostExecute(JSONObject json_obj) {
            super.onPostExecute(json_obj);

            Log.d("PRANJAL", "onpost reached");
            //if response is not empty
            if(json_obj != null) {
                boolean success;
                //in php file if entry to database is successful we've sent true in success and msg is sent accordingly
                //msg may be like "your info is stored" or "new account created"
                try {
                    success = json_obj.getBoolean("success");
                    if(success) {
                        String yourId = json_obj.getString("id");
                        arrayList.add(yourId);
                        arrayList.add(infoarray[0]);
                        arrayList.add(infoarray[1]);
                        hideRegisterDialog();
                        new runAddAsync().execute(arrayList);
                    } else {    //if unsuccesful
                        boolean tsuccess = json_obj.getBoolean("tsuccess");
                        if(!tsuccess) { //specifying cause of error to user
                            String title_error = json_obj.getString("title_err");
                            editTitle.setText("");
                            editTitle.setHintTextColor(getResources().getColor(R.color.redForError));
                            editTitle.setHint("try a different title");
                            Toast.makeText(getApplicationContext(),title_error, Toast.LENGTH_SHORT).show();
                            hideRegisterDialog();
                        } else {    //specifying cause of error to user
                            editTitle.setText("");
                            hideRegisterDialog();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public class runAddAsync extends AsyncTask<ArrayList, Void, JSONObject> {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        final private String SECOND_URL ="http://10.0.2.2/update_doctor_record.php";

        //before the thread completes its task
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("PRANJAL", "this is after onPreExecute");
        }
        //main task here
        @Override
        protected JSONObject doInBackground(ArrayList... params) {  //this is how parameters are declared in this class's methods
            StrictMode.setThreadPolicy(policy);
            Log.d("PRANJAL", "doInBackground wala thau ko suru");

            JSONObject json_obj = null;//declaring a json object
            String result = null;
            InputStream is = null;//input stream

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(SECOND_URL);
            try {
                ArrayList<NameValuePair> NameValuePairs = new ArrayList<NameValuePair>();
                NameValuePairs.add(new BasicNameValuePair("id", params[0].get(12).toString()));
                Log.d("PRANJAL", params[0].get(12).toString());
                NameValuePairs.add(new BasicNameValuePair("title", params[0].get(0).toString()));
                Log.d("PRANJAL", params[0].get(0).toString());
                NameValuePairs.add(new BasicNameValuePair("doc_name", params[0].get(13).toString()));
                Log.d("PRANJAL", params[0].get(13).toString());
                NameValuePairs.add(new BasicNameValuePair("doc_email", params[0].get(14).toString()));
                Log.d("PRANJAL", params[0].get(14).toString());







                Log.d("PRANJAL","namevalue pair wala array banyo");
                //this may be step to send info to database
                httppost.setEntity(new UrlEncodedFormEntity(NameValuePairs));
                Log.d("PRANJAL.", "pathako awastha");
                //this is the response from database
                //this response is defined in my php file
                HttpResponse response = httpclient.execute(httppost);
                Log.d("PRANJAL", "ayo arko tira bata");
                //loading the inputstream with data from response
                is =  response.getEntity().getContent();
                Log.d("PRANJAL", "uta bata k ayo" + is);


            } catch (Exception e) {
                e.printStackTrace();//this will print the error in logcat
                Log.d("PRANJAL", ""+e.getMessage());
            }

            //this part is to convert the response in input stream is into a form from which it can be further manipulated
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, HTTP.UTF_8), 8);
                StringBuilder SB = new StringBuilder();
                String line = null;

                while((line =  reader.readLine()) != null) {
                    SB.append(line + "\n");
                }

                is.close();
                result = SB.toString();
                Log.d("PRANJAL", result);

            } catch (IOException e) {
                e.printStackTrace();
            }

            //this part is for parsing the content of response "json parsing"
            //parsed content is in json_obj
            try {
                //json_obj = new JSONObject(result);
                json_obj = new JSONObject(result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1));
                Log.d("PRANJAL", "json fetched");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return json_obj;
        }




        //after main process is completed
        @Override
        protected void onPostExecute(JSONObject json_obj) {
            super.onPostExecute(json_obj);

            Log.d("PRANJAL", "onpost reached");
            //if response is not empty
            if(json_obj != null) {
                boolean success;
                //in php file if entry to database is successful we've sent true in success and msg is sent accordingly
                //msg may be like "your info is stored" or "new account created"
                try {
                    success = json_obj.getBoolean("success");
                    if(success) {
                        String msg = json_obj.getString("msg");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        editTitle.setText("");
                        editName.setText("");
                        editAge.setText("");
                        editSex.setText("");
                        editWeight.setText("");
                        editAddress.setText("");
                        editChief.setText("");
                        editVitals.setText("");
                        editOnExam.setText("");
                        editAssessment.setText("");
                        editProv.setText("");
                        hideRegisterDialog();
                        finish();

                    } else {    //if unsuccesful
                        String msg = json_obj.getString("msg");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        hideRegisterDialog();
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void showRegisterDialog() {
        if(!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideRegisterDialog() {
        if(progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
