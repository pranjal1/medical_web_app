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
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Load_friend extends AppCompatActivity {
    private Intent fornextpage=null;
    private Bundle b,c=null;
    String[] doc_name =null;
    String[] name_arr=null;
    String[] inst_arr=null;
    String[] tot_arr=null;
    int intStuff=0;
    private ProgressDialog progressDialog;
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        new rungetArrayAsync().execute();
        c=this.getIntent().getExtras();
        doc_name = new String[4];
        doc_name= c.getStringArray("key");
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



    public class rungetArrayAsync extends AsyncTask<Void, Void, JSONArray> {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        final private String REGISTER_URL = "http://10.0.2.2/display_to_add_friends.php";//to use local host

        //before the thread completes its task
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //to show loading message
            progressDialog = new ProgressDialog(Load_friend.this);
            progressDialog.setMessage("Loading information from database ... ");
            progressDialog.setCancelable(false);
            showRegisterDialog();

            Log.d("PRANJAL", "this is after onPreExecute");

        }
        //main task here
        @Override
        protected JSONArray doInBackground(Void... args) {  //this is how parameters are declared in this class's methods
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

            try {

                json_array = new JSONArray(result);
                Log.d("PRANJAL", "json fetched");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("PRANJAL", "json fetched"+json_array);
                return json_array;

        }

        //after main process is completed
        @Override
        protected void onPostExecute(JSONArray json_array) {

            super.onPostExecute(json_array);
            Log.d("PRANJAL", "onpost reached");
            //if response is not empty
            if(json_array != null) {

                try {
                    intStuff = json_array.length();
                    Log.d("PRANJAL", "length = "+ intStuff);
                    if (intStuff != 0) {

                        tot_arr = new String[intStuff];
                        name_arr = new String[intStuff/2];
                        inst_arr = new String[intStuff/2];

                        for (int i = 0; i < intStuff; i++) {
                            tot_arr[i]=json_array.getString(i);
                            Log.d("PRANJAL",tot_arr[i]);
                        }
                        for (int i = 0; i < intStuff; i+=2) {
                            name_arr[i/2]=tot_arr[i];
                            Log.d("PRANJAL",name_arr[i/2]);
                        }
                        for (int i = 1; i < intStuff; i+=2) {
                            inst_arr[(i-1)/2]=tot_arr[i];
                            Log.d("PRANJAL",inst_arr[(i-1)/2]);
                        }
                        b = new Bundle();
                        b.putStringArray("key1",name_arr);
                        b.putStringArray("key2", inst_arr);
                        b.putString("key3",String.valueOf(intStuff));
                        b.putString("key4",doc_name[0]);
                        hideRegisterDialog();
                        fornextpage= new Intent("MedicalWeb.searchfriends");
                        fornextpage.putExtras(b);
                        startActivity(fornextpage);
                        finish();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    hideRegisterDialog();
                    Toast.makeText(getApplicationContext(), "parsing error", Toast.LENGTH_SHORT).show();
                    Log.d("PRANJAL","parsing problem"+e.getMessage());
                    finish();
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "no friends", Toast.LENGTH_SHORT).show();
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

