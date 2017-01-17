package com.example.pranjal.medicalweb;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
public class Navdrawer extends AppCompatActivity implements AdapterView.OnItemClickListener{

    TextView tvName,tvInst,tvEmail,tvPhone;
    Bundle b=null;
    private DrawerLayout drawerLayout;
    private ListView listView;
    private MyAdapter myAdapter;
    private ActionBarDrawerToggle drawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navdrawer);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        listView = (ListView) findViewById(R.id.lvdrawer);
        myAdapter=new MyAdapter(this);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(this);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                listView.bringToFront();//yo duita line vaesi matra listview ma clicks register garda raicha
                listView.requestLayout();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);


        tvName = (TextView) findViewById(R.id.forname);
        tvInst = (TextView) findViewById(R.id.forinstitute);
        tvEmail = (TextView) findViewById(R.id.foremail);
        tvPhone = (TextView) findViewById(R.id.forphone);

        b=this.getIntent().getExtras();

        String[] infoarray = b.getStringArray("key");
        try {
            tvName.setText(infoarray[0]);
            tvInst.setText(infoarray[1]);
            tvEmail.setText(infoarray[2]);
            tvPhone.setText(infoarray[3]);
        }catch (Exception e){
            Log.d("PRANJAL", infoarray[2]);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        super.onSaveInstanceState(outState);
        outState.putString("namestr", tvName.getText().toString());
        outState.putString("emstr",tvEmail.getText().toString());
        outState.putString("instr",tvInst.getText().toString());
        outState.putString("phstr", tvPhone.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Read values from the "savedInstanceState"-object and put them in your textview
        String nameset=savedInstanceState.getString("namestr");
        String emset=savedInstanceState.getString("emstr");
        String inset=savedInstanceState.getString("instr");
        String phset=savedInstanceState.getString("phstr");
        tvName.setText(nameset);
        tvEmail.setText(emset);
        tvInst.setText(inset);
        tvPhone.setText(phset);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        drawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                drawerLayout.closeDrawers();
                break;
            case 1:
                Intent i = new Intent("MedicalWeb.createcase");
                i.putExtras(b);
                startActivity(i);
                break;
            case 2:
                Intent k = new Intent("MedicalWeb.loadaddedfriends");
                k.putExtras(b);
                startActivity(k);
                break;
            case 3:
                Intent j = new Intent("MedicalWeb.loadfriends");
                j.putExtras(b);
                startActivity(j);
                break;
            case 4:
                Intent l = new Intent("MedicalWeb.loadmycases");
                l.putExtras(b);
                startActivity(l);
                break;
        }
    }
    class MyAdapter extends BaseAdapter{
        String[] list;
        private Context context;
        MyAdapter(Context context){
            this.context = context;
         list=context.getResources().getStringArray(R.array.drawer_list);
        }

        @Override
        public int getCount() {
            return list.length;
        }

        @Override
        public Object getItem(int position) {
            return list[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row;
            if(convertView==null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row=inflater.inflate(R.layout.nav_text_design,parent,false);

            }else{
                row=convertView;
            }
            TextView title = (TextView) row.findViewById(R.id.textViewfordraw);
            title.setText(list[position]);
            return row;
        }
    }
}
