package com.example.Hane;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Hane.R;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;


public class MainActivity extends ActionBarActivity {
    static final int SELECTED_OPT = 6;
    ArrayList<String> listItems;
    Button searchBouton,changeLang,locateBouton,selectBouton;
    TextView tempMatin, tempAprem, vitVente, nivNeige, isOpen;
    ProgressBar progressBar;
    EditText searchInput;
    ImageView tempImg;
    SharedPreferences sharedPref;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectBouton=(Button) findViewById(R.id.select_station);
        locateBouton=(Button) findViewById(R.id.locate_bouton);
        searchBouton = (Button) findViewById(R.id.search_bouton);
        tempMatin = (TextView) findViewById((R.id.temp_matin));
        tempAprem = (TextView) findViewById(R.id.temp_aprem);
        vitVente = (TextView) findViewById(R.id.vit_vente);
        nivNeige = (TextView) findViewById(R.id.niv_neige);
        isOpen = (TextView) findViewById(R.id.is_open);
        searchInput = (EditText) findViewById(R.id.search_input);
        tempImg = (ImageView) findViewById(R.id.temp_img);
        progressBar=(ProgressBar) findViewById(R.id.pBAsync);
        changeLang=(Button) findViewById(R.id.changeLang);
        sharedPref = getSharedPreferences("sharedSNOW",MODE_PRIVATE);
        searchInput.setText(sharedPref.getString("station", null));
        listItems = new ArrayList<String>();
        if(savedInstanceState==null){
            Log.i("cv","yo");
        }
        else{
            Log.i("cv","Hello");
            listItems=savedInstanceState.getStringArrayList("listItem");
        }
        Log.i("cv", "oncreate");
        searchBouton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                RequestTask request = new RequestTask();
                listItems.add(searchInput.getText().toString());
                Log.i("cv", "http://snowlabri.appspot.com/snow?station=gourette");
                request.execute("http://snowlabri.appspot.com/snow?station=gourette");
            }
        });

        selectBouton.setOnClickListener(new View.OnClickListener(){
            public  void onClick(View view){
                Intent secAct = new Intent(MainActivity.this,StationListActivity.class);
                secAct.putStringArrayListExtra("listItems",listItems);
                MainActivity.this.startActivityForResult(secAct, SELECTED_OPT);
            }
        });
        locateBouton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Uri locateUri=Uri.parse("geo:0,0?q="+searchInput.getText().toString());
                Intent locateIntent = new Intent(Intent.ACTION_VIEW, locateUri);
                locateIntent.setPackage("com.google.android.apps.maps");
                MainActivity.this.startActivity(locateIntent);
            }
        });
        //bouton "Fran�ais/English" pour changer le langage
        changeLang.setOnClickListener(new View.OnClickListener(){
            @TargetApi(Build.VERSION_CODES.KITKAT)
            public void onClick(View view){
                SharedPreferences.Editor editorPref = sharedPref.edit();

                Log.i("cv", getResources().getString(R.string.locale));
                if(Objects.equals(getResources().getString(R.string.locale), "en")){
                    Locale frrr=new Locale("fr");
                    Resources res = getResources();
                    DisplayMetrics dm = res.getDisplayMetrics();
                    Configuration conf = res.getConfiguration();
                    conf.locale = frrr;
                    res.updateConfiguration(conf, dm);
                    Log.i("cv", "im in en");
                    editorPref.putString("station", searchInput.getText().toString());
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }else {
                    Locale engg=new Locale("en");
                    Resources res = getResources();
                    DisplayMetrics dm = res.getDisplayMetrics();
                    Configuration conf = res.getConfiguration();
                    conf.locale = engg;
                    res.updateConfiguration(conf, dm);
                    Log.i("cv", "Fr");
                    editorPref.putString("station", searchInput.getText().toString());
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
                editorPref.commit();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECTED_OPT) {
            if (resultCode == RESULT_OK) {
                if(data.getExtras().containsKey("selected")){
                    searchInput.setText(data.getStringExtra("selected"));
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("cv", "onstart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("cv", "onrestart");
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Log.i("cv", "onStop");
        SharedPreferences.Editor editorPref = sharedPref.edit();
        editorPref.putString("station", searchInput.getText().toString());
        editorPref.commit();

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.i("cv","onpause");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.i("cv","onresume");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.i("cv", "ondestroy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList("listItem",listItems);
        outState.putString("isOpen", (String) isOpen.getText());
        outState.putString("tempMatin", (String) tempMatin.getText());
        outState.putString("tempAprem", (String) tempAprem.getText());
        outState.putString("vitVente", (String) vitVente.getText());
        outState.putString("nivNeige", (String) nivNeige.getText());
        outState.putInt("tempImg", getResources().getIdentifier("tempImg", "drawable", getPackageName()));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onRestoreInstanceState(savedInstanceState);
        isOpen.setText(savedInstanceState.getString("isOpen"));
        tempAprem.setText(savedInstanceState.getString("tempAprem"));
        tempMatin.setText(savedInstanceState.getString("tempMatin"));
        vitVente.setText(savedInstanceState.getString("vitVente"));
        nivNeige.setText(savedInstanceState.getString("nivNeige"));
        tempImg.setImageResource(savedInstanceState.getInt("tempImg"));
        listItems=savedInstanceState.getStringArrayList("listItem");
        Log.i("cv", Integer.toString(savedInstanceState.getInt("tempImg")));
    }


    class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    responseString = out.toString();
                    out.close();
                } else {
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            }  catch (IOException e) {

                //TODO Handle problems..
            }

            int progress;
            for (progress=0;progress<=100;progress++)
            {

                publishProgress(Integer.toString(progress));
                progress++;
            }

            Log.i("cv", responseString);
            return responseString;
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //if(!Objects.equals(result, "oui") || !Objects.equals(result, "non")) Toast.makeText(getApplicationContext(),"Error!",Toast.LENGTH_LONG).show();
            try {
                JSONObject weather = new JSONObject(result);
                String ouverte = weather.getString("ouverte");
                if (Objects.equals(ouverte, "oui"))
                    isOpen.setText(R.string.isOpen1);
                else
                    isOpen.setText(R.string.isOpen0);

                tempMatin.setText(weather.getString("temperatureMatin"));
                tempAprem.setText(weather.getString("temperatureMidi"));
                vitVente.setText(weather.getString("vent"));
                nivNeige.setText(weather.getString("neige"));
                String temps_status = weather.getString("temps");
                switch (temps_status) {
                    case "neigeux":
                        tempImg.setBackgroundResource(R.drawable.neige);
                        break;
                    case "beau":
                        tempImg.setBackgroundResource(R.drawable.beau);
                        break;
                    case "couvert":
                        tempImg.setBackgroundResource(R.drawable.couvert);
                        break;
                }
            }   catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(String... values){
            super.onProgressUpdate(values);
            progressBar.setProgress(Integer.parseInt(values[0]));
        }



    }
}


