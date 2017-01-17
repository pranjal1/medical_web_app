package com.example.pranjal.medicalweb;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
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


public class Load_my_cases extends AppCompatActivity {
    private Bundle b,c = null;
    String[] doc_name = null;
    private ProgressDialog progressDialog;
    ListView list;
    ArrayList arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        b = this.getIntent().getExtras();
        doc_name = new String[4];
        doc_name = b.getStringArray("key");
        arrayList = new ArrayList();
        arrayList.add(doc_name[0]);
        new runtoArrayAsync().execute(arrayList);
    }


    public class runtoArrayAsync extends AsyncTask<ArrayList, Void, JSONArray> {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        final private String REGISTER_URL = "http://10.0.2.2/my_registered_cases.php";

        //before the thread completes its task
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //to show loading message
            progressDialog = new ProgressDialog(Load_my_cases.this);
            progressDialog.setMessage("loading your cases \n from your database ... ");
            progressDialog.setCancelable(false);
            showRegisterDialog();

            Log.d("PRANJAL", "this is after onPreExecute");
        }

        //main task here
        @Override
        protected JSONArray doInBackground(ArrayList... params) {  //this is how parameters are declared in this class's methods
            StrictMode.setThreadPolicy(policy);
            Log.d("PRANJAL", "doInBackground wala thau ko suru");

            JSONArray json_array = null;//declaring a json object
            String result = null;
            InputStream is = null;//input stream

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(REGISTER_URL);
            //imagine namevaluepair as 2 dimensional array
            //each row has unique data set to be stored and each column is different sections of the data set
            try {
                ArrayList<NameValuePair> NameValuePairs = new ArrayList<NameValuePair>();
                NameValuePairs.add(new BasicNameValuePair("d_name", params[0].get(0).toString()));
                Log.d("PRANJAL", params[0].get(0).toString());


                Log.d("PRANJAL", "namevalue pair wala array banyo");
                //this may be step to send info to database
                httppost.setEntity(new UrlEncodedFormEntity(NameValuePairs));
                Log.d("PRANJAL.", "pathako awastha");
                //this is the response from database
                //this response is defined in my php file
                HttpResponse response = httpclient.execute(httppost);
                Log.d("PRANJAL", "ayo arko tira bata");
                //loading the inputstream with data from response
                is = response.getEntity().getContent();
                Log.d("PRANJAL", "uta bata k ayo" + is);


            } catch (Exception e) {
                e.printStackTrace();//this will print the error in logcat
                Log.d("PRANJAL", "" + e.getMessage());
            }

            //this part is to convert the response in input stream is into a form from which it can be further manipulated
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, HTTP.UTF_8), 8);
                StringBuilder SB = new StringBuilder();
                String line = null;

                while ((line = reader.readLine()) != null) {
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
                json_array = new JSONArray(result);
                Log.d("PRANJAL", "json fetched");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return json_array;
        }


        //after main process is completed
        @Override
        protected void onPostExecute(JSONArray json_array) {
            super.onPostExecute(json_array);

            Log.d("PRANJAL", "onpost reached");
            if(json_array != null) {

                try {
                    int intStuff = json_array.length();
                    Log.d("PRANJAL", "length = "+ intStuff);


                        String[] tot_arr = new String[intStuff];

                        for (int i = 0; i < intStuff; i++) {
                            tot_arr[i]=json_array.getString(i);
                            Log.d("PRANJAL",tot_arr[i]);
                        }
                        c = new Bundle();
                        c.putStringArray("key1", tot_arr);
                        c.putString("key2", String.valueOf(intStuff));
                        hideRegisterDialog();
                        Intent fornextpage= new Intent("MedicalWeb.displaycases");
                        fornextpage.putExtras(c);

                        startActivity(fornextpage);
                        finish();


                } catch (Exception e) {
                    e.printStackTrace();
                    hideRegisterDialog();
                    Toast.makeText(getApplicationContext(), "parsing error", Toast.LENGTH_SHORT).show();
                    Log.d("PRANJAL","parsing problem"+e.getMessage());
                    finish();
                }
            }
            else{
                hideRegisterDialog();
                Toast.makeText(getApplicationContext(), "no cases", Toast.LENGTH_SHORT).show();
            }
        }


        private void showRegisterDialog() {
            if (!progressDialog.isShowing())
                progressDialog.show();
        }

        private void hideRegisterDialog() {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }
}

