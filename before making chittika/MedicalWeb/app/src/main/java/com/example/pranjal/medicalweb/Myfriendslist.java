package com.example.pranjal.medicalweb;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;


public class Myfriendslist extends AppCompatActivity {
    int intStuff=0;
    Bundle b=null;
    ListView list;
    String[] infoarray=null;
    String doc_name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfriendslist);
        list = (ListView) findViewById(R.id.listaddedfriends);
        b = this.getIntent().getExtras();
        infoarray=b.getStringArray("key1");
        intStuff = Integer.parseInt(b.getString("key2"));
        Log.d("PRANJAL", "yha pugiyo" + intStuff);
        list.setAdapter(new foraddedfriendAdapter(this,infoarray,intStuff));
    }
}
class SingleRowfriend{
    String name;
    String institution;

    SingleRowfriend(String name){
        this.name=name;
    }
}

class foraddedfriendAdapter extends BaseAdapter{

    ArrayList<SingleRowfriend> list = new ArrayList<>();
    Context context;
    foraddedfriendAdapter(Context c,String[] par1, int sizeofarr){
        context = c;
        String[] name_array = par1;
        int k= sizeofarr;

        for(int i=0;i<k;i++){
            list.add(new SingleRowfriend(name_array[i]));
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
        View row=inflater.inflate(R.layout.my_added_friends_row, parent, false);
        TextView textName = (TextView) row.findViewById(R.id.textViewaddedfriend);
        SingleRowfriend temp = list.get(position);
        textName.setText(temp.name);
        return row;
    }
}