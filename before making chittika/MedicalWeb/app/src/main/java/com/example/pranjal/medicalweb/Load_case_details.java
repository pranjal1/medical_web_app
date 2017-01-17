package com.example.pranjal.medicalweb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Load_case_details extends AppCompatActivity {
    Bundle bc=null;
    String case_title=null;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_case_details);
        bc=this.getIntent().getExtras();
        case_title = bc.getString("keys");
        Log.d("PRANJAL","here");
        ArrayList arrayList= new ArrayList();
        arrayList.add(case_title);
        new casedetialsAsync().execute(arrayList);

    }

    public class casedetialsAsync extends AsyncTask<ArrayList, Void, JSONObject> {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        final private String REGISTER_URL = "http://10.0.2.2/get_case_details.php";//to use local host

        //before the thread completes its task
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //to show loading message
            progressDialog = new ProgressDialog(Load_case_details.this);
            progressDialog.setMessage("Loading case details ... ");
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
                Log.d("PRANJAL", "onpost reached");
                try {
                    success = json_obj.getBoolean("success");
                    if (success) {

                    String[] infos={json_obj.getString("title"),json_obj.getString("name"),json_obj.getString("age"),json_obj.getString("sex"),
                            json_obj.getString("weight"),json_obj.getString("address"),json_obj.getString("chiefcomp"),json_obj.getString("onexam"),
                            json_obj.getString("assess"),json_obj.getString("prov"),json_obj.getString("plan")};
                        Bundle c=new Bundle();
                        Log.d("PRANJAL", "onpost reached");
                        c.putStringArray("key1", infos);
                        Log.d("PRANJAL", "onpost reached");
                        Intent j = new Intent("MedicalWeb.showcasedetails");
                        Log.d("PRANJAL", "onpost reached");
                        j.putExtras(c);
                        startActivity(j);
                        hideRegisterDialog();
                    }else{
                        hideRegisterDialog();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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
}
