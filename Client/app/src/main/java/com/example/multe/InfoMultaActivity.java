package com.example.multe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class InfoMultaActivity extends AppCompatActivity {
    private long mLastClickTime = 0;
    private JSONObject o;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_multa);
        getWindow().setNavigationBarColor(Color.BLACK);

        if(getSharedPreferences("theme", MODE_PRIVATE).getBoolean("dark", true)){
            dark_theme();
        }else{
            light_theme();
        }

        Intent i = getIntent();
        Bundle b = i.getExtras();
        String multa = b.getString("multa");

        String targa, dataora, luogo, coordinate, effrazioni, importo;
        try {
            o = new JSONObject(multa);
            targa = o.getString("targa");
            dataora = o.getString("dataora");
            luogo = o.getString("luogo");
            importo = o.getString("importo") + "€";
            coordinate = o.getString("latitudine") + "," + o.getString("longitudine");
            effrazioni = o.getString("effrazioni");

            ((TextView)findViewById(R.id.targhetta)).setText(targa);
            ((TextView)findViewById(R.id.datoretta)).setText(dataora);
            ((TextView)findViewById(R.id.luoghetto)).setText(luogo);
            ((TextView)findViewById(R.id.coordinatine)).setText(coordinate);
            ((TextView)findViewById(R.id.cocimporto)).setText(importo);
            String[] effr = effrazioni.split(",");
            TextView t;
            for(String s : effr){
                t = new TextView(InfoMultaActivity.this);
                t.setTextColor(Color.rgb(43, 43, 43));
                t.setTextSize(16);
                t.setTypeface(ResourcesCompat.getFont(this, R.font.ock));
                t.setText(s);

                ((LinearLayout)findViewById(R.id.scrollerino)).addView(t);
            }
        }catch(Exception e){}

        Button photo = findViewById(R.id.showPhoto);
        Button delete = findViewById(R.id.delete);

        photo.setEnabled(false);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) return;
                mLastClickTime = SystemClock.elapsedRealtime();

                AlertDialog.Builder builder = new AlertDialog.Builder(InfoMultaActivity.this);
                builder.setTitle("Warning");
                builder.setMessage("By pressing YES you will invalidate the fine. Proceed?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            eliminaMulta(o.getInt("id"));
                        }catch(Exception e){}
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog alertdialog = builder.create();
                alertdialog.show();

            }
        });
    }

    private void eliminaMulta(int id){
        RequestQueue queue = Volley.newRequestQueue(InfoMultaActivity.this);
        //for POST requests, only the following line should be changed to
        StringRequest sr = new StringRequest(Request.Method.POST, "http://"+MainActivity.IP+":8080/sito/API-PHP/api.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("HttpClient", "success! response: " + response.toString());

                        try {
                            JSONObject t = new JSONObject(response);
                            if(t.getString("esito").equals("ok")){
                                Intent i = new Intent();
                                setResult(2, i);
                                finish();
                            }
                        }catch(Exception e){}
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("HttpClient", "error: " + error.toString());
                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                try {
                    params.put("id_multa",""+id);

                    params.put("token", getSharedPreferences("vigile", MODE_PRIVATE).getString("token", ""));
                    params.put("function","app-eliminamulta");
                }catch(Exception e){}
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        queue.add(sr);
    }

    public void dark_theme(){
        findViewById(R.id.view12).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio2));
        findViewById(R.id.view4).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio2));
        findViewById(R.id.view13).setBackground(getResources().getDrawable(R.drawable.rettangolinoantracite1));
        findViewById(R.id.view9).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio1));
        ((Button)findViewById(R.id.showPhoto)).setBackgroundTintList(ColorStateList.valueOf(Color.rgb(222, 222, 222)));
        ((Button)findViewById(R.id.delete)).setBackgroundTintList(ColorStateList.valueOf(Color.rgb(222, 222, 222)));
    }
    public void light_theme(){
        findViewById(R.id.view12).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio4));
        findViewById(R.id.view4).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio4));
        findViewById(R.id.view13).setBackground(getResources().getDrawable(R.drawable.rettangolinoantracite2));
        findViewById(R.id.view9).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio3));
        ((Button)findViewById(R.id.showPhoto)).setBackgroundTintList(ColorStateList.valueOf(Color.rgb(245, 247, 255)));
        ((Button)findViewById(R.id.delete)).setBackgroundTintList(ColorStateList.valueOf(Color.rgb(245, 247, 255)));
    }
}