package com.example.pranjal.medicalweb;

import android.content.Context;
import android.content.Intent;
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
import java.util.ArrayList;


public class Display_cases extends AppCompatActivity implements AdapterView.OnItemClickListener{
    int intStuff=0;
    Bundle b,c=null;
    ListView list;
    String[] infoarray=null;
    String doc_name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_cases);
        list = (ListView) findViewById(R.id.listmycases);
        b = this.getIntent().getExtras();
        infoarray=b.getStringArray("key1");
        intStuff = Integer.parseInt(b.getString("key2"));
        Log.d("PRANJAL", "yha pugiyo" + intStuff);
        list.setAdapter(new formadecasesAdapter(this,infoarray,intStuff));
        list.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("PRANJAL", infoarray[position]);
       try{
           Intent j = new Intent("MedicalWeb.loadcasedetails");
           c=new Bundle();
           c.putString("keys",infoarray[position]);
           j.putExtras(c);
           startActivity(j);
    }catch (Exception e){
           Log.d("PRANJAL"," "+e.getMessage());
       }
    }
}
class SingleRowCases{
    String name;

    SingleRowCases(String name){
        this.name=name;
    }
}

class formadecasesAdapter extends BaseAdapter{

    ArrayList<SingleRowCases> list = new ArrayList<>();
    Context context;
    formadecasesAdapter(Context c,String[] par1, int sizeofarr){
        context = c;
        String[] name_array = par1;
        int k= sizeofarr;

        for(int i=0;i<k;i++){
            list.add(new SingleRowCases(name_array[i]));
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
        SingleRowCases temp = list.get(position);
        textName.setText(temp.name);
        return row;
    }
}