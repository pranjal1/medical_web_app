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



public class Signup_details extends AppCompatActivity {
    private Button bSubmit;
    private int fromprevpage =0;
    private Intent fornextpages=null;
    private Bundle b=null;
    private Intent fifth = null;
    private EditText editName,editEmail,editInstitution,editPhone,editFirstpass,editSecondpass;
    private ProgressDialog progressDialog;
    private ArrayList arrayList=null;
    String yourName,yourEmail,yourInstitution,yourPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_details);
        Bundle extra1 = getIntent().getExtras();
        if (extra1 != null){
            try {
                fromprevpage = Integer.parseInt(extra1.getString("fromCatPage"));
            } catch (Exception e){
            }
        }

        bSubmit = (Button) findViewById(R.id.bButtonSignUp);
        editName = (EditText) findViewById(R.id.nameFill);
        editEmail = (EditText) findViewById(R.id.emailFill);
        editInstitution = (EditText) findViewById(R.id.instFill);
        editPhone = (EditText) findViewById(R.id.phoneFill);
        editFirstpass = (EditText) findViewById(R.id.firstpassFill);
        editSecondpass = (EditText) findViewById(R.id.secondpassFill);

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //copy the content of edit text field to strings
                 yourName = editName.getText().toString().trim();
                 yourEmail = editEmail.getText().toString().trim();
                 yourInstitution = editInstitution.getText().toString().trim();
                 yourPhone = editPhone.getText().toString().trim();
                String yourFirstpass = editFirstpass.getText().toString().trim();
                String yourSecondpass = editSecondpass.getText().toString().trim();

                //putting all strings above to an arraylist
                arrayList = new ArrayList();
                arrayList.add(yourName);
                arrayList.add(yourEmail);
                arrayList.add(yourInstitution);
                arrayList.add(yourPhone);
                arrayList.add(yourFirstpass);
                arrayList.add(yourSecondpass);

                //check if all fields are filled and initiate a background thread if all are filled
                //else give appropriate message
                if (!yourName.isEmpty() && !yourEmail.isEmpty() && !yourInstitution.isEmpty() && !yourPhone.isEmpty() && !yourFirstpass.isEmpty() && !yourSecondpass.isEmpty() && (yourFirstpass.equals(yourSecondpass))) {
                    Log.d("PRANJAL","tyo saab milyo aba pathauna lagya");
                    new runRegisterAsync().execute(arrayList);
                }else if(!yourFirstpass.equals(yourSecondpass)){
                    editSecondpass.setText("");
                    editSecondpass.setHintTextColor(getResources().getColor(R.color.redForError));
                    editSecondpass.setHint("passwords donot match");
                    Toast.makeText(getApplicationContext(), "different passwords in the two fields.\n confirm passwords!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Enter the required fields!", Toast.LENGTH_SHORT).show();
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


    public class runRegisterAsync extends AsyncTask<ArrayList, Void, JSONObject> {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        final private String REGISTER_URL = "http://10.0.2.2/create_doctor_record.php";//to use local host

        //before the thread completes its task
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //to show loading message
            progressDialog = new ProgressDialog(Signup_details.this);
            progressDialog.setMessage("Registering Your Account ... ");
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
                NameValuePairs.add(new BasicNameValuePair("name", params[0].get(0).toString()));
                Log.d("PRANJAL", params[0].get(0).toString());
                NameValuePairs.add(new BasicNameValuePair("email", params[0].get(1).toString()));
                Log.d("PRANJAL", params[0].get(1).toString());
                NameValuePairs.add(new BasicNameValuePair("institution", params[0].get(2).toString()));
                Log.d("PRANJAL", params[0].get(2).toString());
                NameValuePairs.add(new BasicNameValuePair("phone", params[0].get(3).toString()));
                Log.d("PRANJAL", params[0].get(3).toString());
                NameValuePairs.add(new BasicNameValuePair("password", params[0].get(4).toString()));
                Log.d("PRANJAL", params[0].get(4).toString());


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

            Log.d("PRANJAL", "onpost reached" + json_obj);
            //if response is not empty
            if(json_obj != null) {
                Log.d("PRANJAL", "onpost pachi ko json obj is not null");
                boolean success;
                //in php file if entry to database is successful we've sent true in success and msg is sent accordingly
                //msg may be like "your info is stored" or "new account created"
                try {
                    success = json_obj.getBoolean("success");
                    if(success) {
                        Log.d("PRANJAL", "onpost reached");
                        String msg = json_obj.getString("msg");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        b = new Bundle();
                        b.putStringArray("key", new String[]{yourName, yourEmail, yourInstitution, yourPhone});
                        hideRegisterDialog();
                        fornextpages= new Intent("MedicalWeb.navpg");
                        fornextpages.putExtras(b);
                        startActivity(fornextpages);
                        finish();

                    } else {    //if unsuccesful
                        boolean esuccess = json_obj.getBoolean("esuccess");
                        boolean usuccess = json_obj.getBoolean("usuccess");
                        if(!esuccess) { //specifying cause of error to user
                            String email_error = json_obj.getString("email_err");
                            editEmail.setText("");
                            editEmail.setHintTextColor(getResources().getColor(R.color.redForError));
                            editEmail.setHint("try a different email");
                            Toast.makeText(getApplicationContext(),email_error, Toast.LENGTH_SHORT).show();
                        } else {    //specifying cause of error to user
                            editEmail.setText("");
                        }
                        if(!usuccess) { //specifying cause of error to user
                            String user_error = json_obj.getString("username_err");
                            editName.setText("");
                            editEmail.setHintTextColor(getResources().getColor(R.color.redForError));
                            editEmail.setHint("try a different email");
                            Toast.makeText(getApplicationContext(),user_error, Toast.LENGTH_SHORT).show();
                        } else {    //specifying cause of error to user
                            editName.setText("");
                        }
                        hideRegisterDialog();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                Log.d("PRANJAL", "json abj is null");
                hideRegisterDialog();
                finish();
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



