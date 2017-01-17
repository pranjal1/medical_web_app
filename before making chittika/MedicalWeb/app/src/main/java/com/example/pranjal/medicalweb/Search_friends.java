package com.example.pranjal.medicalweb;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Search_friends extends AppCompatActivity implements AdapterView.OnItemClickListener{
    String[] name_arr=null;
    String[] inst_arr=null;
    int intStuff=0;
    Bundle b=null;
    ListView list;
    ArrayList arrayList=null;
    private ProgressDialog progressDialog;
    int topassId=0;
    String[] infoarray=null;
    String doc_name = null;
    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        Log.d("PRANJAL", "yha pugiyo");
        list = (ListView) findViewById(R.id.listfriends);
        b = this.getIntent().getExtras();
        infoarray = new String[4];
        infoarray=b.getStringArray("key");
        intStuff = Integer.parseInt(b.getString("key3"));
        name_arr = new String[intStuff / 2];
        inst_arr = new String[intStuff / 2];
        name_arr = b.getStringArray("key1");
        inst_arr = b.getStringArray("key2");
        Log.d("PRANJAL", "yha pugiyo" + intStuff);
        doc_name = b.getString("key4");
        list.setAdapter(new forfriendAdapter(this, name_arr, inst_arr, intStuff));
        list.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("PRANJAL", view.toString()+ " " +position);
        topassId=position;
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.cancel();
                        arrayList = new ArrayList();
                        arrayList.add(Integer.toString(topassId + 1));
                        Log.d("PRANJAL", arrayList.get(0).toString());
                        arrayList.add(name_arr[topassId]);
                        Log.d("PRANJAL", arrayList.get(1).toString());
                        new runAddFriendAsync().execute(arrayList);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.cancel();
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();


    }
    public class runAddFriendAsync extends AsyncTask<ArrayList, Void, JSONObject> {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        final private String REGISTER_URL = "http://10.0.2.2/update_doctor_friend_record.php";
        //before the thread completes its task
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //to show loading message
            progressDialog = new ProgressDialog(Search_friends.this);
            progressDialog.setMessage("adding new friend \n in your database ... ");
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
                NameValuePairs.add(new BasicNameValuePair("d_name", doc_name));
                Log.d("PRANJAL", params[0].get(0).toString());
                NameValuePairs.add(new BasicNameValuePair("f_id", params[0].get(0).toString()));
                Log.d("PRANJAL", params[0].get(0).toString());
                NameValuePairs.add(new BasicNameValuePair("f_name", params[0].get(1).toString()));
                Log.d("PRANJAL", params[0].get(1).toString());







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
                        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();
                        hideRegisterDialog();
                    } else {    //if unsuccesful
                            String msg = json_obj.getString("msg");
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

    class SingleRow{
    String name;
    String institution;

    SingleRow(String name, String institution){
        this.name=name;
        this.institution=institution;
    }
}

class forfriendAdapter extends BaseAdapter{

    ArrayList<SingleRow> list = new ArrayList<>();
    Context context;
    forfriendAdapter(Context c,String[] par1, String[] par2, int sizeofarr){
        context = c;
        String[] name_array = par1;
        String[] inst_array = par2;
        int k= sizeofarr;
        for(int i=0;i<k/2;i++){
            Log.d("PRANJAL",name_array[i]+ " " + inst_array[i]);
        }

        for(int i=0;i<k/2;i++){
            list.add(new SingleRow(name_array[i],inst_array[i]));
        }

    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=inflater.inflate(R.layout.friend_list, parent, false);
        TextView textName = (TextView) row.findViewById(R.id.textViewName);
        TextView textInst = (TextView) row.findViewById(R.id.textViewInst);

        SingleRow temp = list.get(position);
        textName.setText(temp.name);
        textInst.setText(temp.institution);
        return row;
    }
}

