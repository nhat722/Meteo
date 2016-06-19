package com.example.Hane;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.Hane.R;

import java.util.ArrayList;


public class StationListActivity extends ActionBarActivity {
ArrayList<String> items=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle bundle=getIntent().getExtras();
        items=bundle.getStringArrayList("listItems");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_list);
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedI = parent.getItemAtPosition(position).toString();
                Intent i=getIntent();
                i.putExtra("selected",selectedI);
                setResult(RESULT_OK,i);
                finish();
            }
        });
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_station_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
