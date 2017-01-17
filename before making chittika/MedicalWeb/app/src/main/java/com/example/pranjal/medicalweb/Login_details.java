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


public class Login_details extends AppCompatActivity {

    private Button subButton;
    private EditText editName,editEmail,editPassword;
    private ProgressDialog progressDialog;
    private Intent fornextpage=null;
    private Bundle b=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_details);

        subButton = (Button)findViewById(R.id.loginButton);
        editName = (EditText) findViewById(R.id.loginName);
        editEmail = (EditText) findViewById(R.id.loginEmail);
        editPassword = (EditText) findViewById(R.id.loginPassword);

        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //copy the content of edit text field to strings
                String yourName = editName.getText().toString().trim();
                String yourEmail = editEmail.getText().toString().trim();
                String yourPassword = editPassword.getText().toString().trim();

                //putting all strings above to an arraylist
                ArrayList arrayList = new ArrayList();
                arrayList.add(yourName);
                arrayList.add(yourEmail);
                arrayList.add(yourPassword);

                //check if all fields are filled and initiate a background thread if all are filled
                //else give appropriate message
                if (!yourName.isEmpty() && !yourEmail.isEmpty() && !yourPassword.isEmpty() ) {
                    Log.d("PRANJAL","tyo saab milyo aba pathauna lagya");
                    new runLoginAsync().execute(arrayList);
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

    public class runLoginAsync extends AsyncTask<ArrayList, Void, JSONObject> {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        final private String REGISTER_URL = "http://10.0.2.2/auth.php";//to use local host

        //before the thread completes its task
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //to show loading message
            progressDialog = new ProgressDialog(Login_details.this);
            progressDialog.setMessage("Logging in! ... ");
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
                NameValuePairs.add(new BasicNameValuePair("password", params[0].get(2).toString()));
                Log.d("PRANJAL", params[0].get(2).toString());

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
                        String name =json_obj.getString("Name");
                        String email =json_obj.getString("Email");
                        String institute =json_obj.getString("Institute");
                        String phone =json_obj.getString("Phone");
                        //to send info to next page

                        b = new Bundle();
                        b.putStringArray("key", new String[]{name,email,institute,phone});
                        fornextpage= new Intent("MedicalWeb.navpg");
                        fornextpage.putExtras(b);
                        startActivity(fornextpage);
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        hideRegisterDialog();
                    } else {    //if unsuccesful
                            String msg = json_obj.getString("msg");
                            editName.setText("");
                            editName.setHintTextColor(getResources().getColor(R.color.redForError));
                            editName.setHint("Wrong name,try again!");
                            editEmail.setText("");
                            editEmail.setHintTextColor(getResources().getColor(R.color.redForError));
                            editEmail.setHint("Wrong email,try again!");
                            editPassword.setText("");
                            editPassword.setHintTextColor(getResources().getColor(R.color.redForError));
                            editPassword.setHint("Wrong password,try again!");
                            Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();
                            hideRegisterDialog();

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
