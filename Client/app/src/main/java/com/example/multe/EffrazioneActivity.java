package com.example.multe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class EffrazioneActivity extends AppCompatActivity {
    private long mLastClickTime = 0;
    private static int id, min, max, importo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effrazione);
        getWindow().setNavigationBarColor(Color.BLACK);

        if(getSharedPreferences("theme", MODE_PRIVATE).getBoolean("dark", true)){
            dark_theme();
        }else{
            light_theme();
        }

        RequestQueue queue = Volley.newRequestQueue(EffrazioneActivity.this);
        //for POST requests, only the following line should be changed to
        StringRequest sr = new StringRequest(Request.Method.POST, "http://"+MainActivity.IP+":8080/sito/API-PHP/api.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("HttpClient", "success! response: " + response.toString());
                        try {
                            JSONObject rispostaJSON = new JSONObject(response);
                            JSONArray array = rispostaJSON.getJSONArray("effrazioni");

                            JSONObject obj;
                            for(int i = 0; i < array.length(); i++){
                                obj = array.getJSONObject(i);
                                MainActivity.effrazioniTotali.add(obj);
                            }

                            ArrayList<String> ss = new ArrayList<>();
                            for(JSONObject o : MainActivity.effrazioniTotali){
                                ss.add(o.getString("nome"));
                            }

                            Spinner s = findViewById(R.id.tipoeffrazione);
                            ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.item_spinner, ss);
                            adapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
                            s.setAdapter(adapter);
                            s.setSelection(0);

                            s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    try {
                                        JSONObject o = MainActivity.effrazioniTotali.get(s.getSelectedItemPosition());

                                        id = o.getInt("id");
                                        min = o.getInt("min");
                                        max = o.getInt("max");

                                        ((TextView) findViewById(R.id.nomeEffrazione)).setText(
                                                o.get("nome") +
                                                System.getProperty("line.separator") +
                                                System.getProperty("line.separator") +
                                                min + "€  -  " + max + "€"
                                        );
                                        ((EditText)findViewById(R.id.importo)).setText(min+"€");

                                        ((SeekBar)findViewById(R.id.seekBar)).setMin(min);
                                        ((SeekBar)findViewById(R.id.seekBar)).setMax(max);
                                        ((SeekBar)findViewById(R.id.seekBar)).setProgress(min);
                                    }catch(Exception e){}
                                }

                                public void onNothingSelected(AdapterView<?> adapterView) {
                                    return;
                                }
                            });

                            ((SeekBar)findViewById(R.id.seekBar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                    ((EditText)findViewById(R.id.importo)).setText(i+"€");
                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {
                                }

                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {
                                }
                            });
                            ((EditText)findViewById(R.id.importo)).addTextChangedListener(new TextWatcher() {

                                @Override
                                public void afterTextChanged(Editable s) {}
                                @Override
                                public void beforeTextChanged(CharSequence s, int start,int count, int after) { }
                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    String c = ((EditText)findViewById(R.id.importo)).getText().toString();
                                    c = c.substring(0, c.length()-1);
                                    importo = Integer.valueOf(c);
                                    ((SeekBar)findViewById(R.id.seekBar)).setProgress(importo);
                                }
                            });
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
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
                params.put("token", getSharedPreferences("vigile", MODE_PRIVATE).getString("token", ""));
                params.put("function","app-geteffrazioni");
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


        View.OnClickListener aggiungiEffrazione = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) return;
                mLastClickTime = SystemClock.elapsedRealtime();

                JSONObject o = MainActivity.effrazioniTotali.get(((Spinner)findViewById(R.id.tipoeffrazione)).getSelectedItemPosition());
                MainActivity.effrazioni.add(o);
                MainActivity.effrazioniTotali.remove(o);

                MainActivity.importi.add(importo);
                finish();
            }
        };
        ((Button)findViewById(R.id.aggiungi)).setOnClickListener(aggiungiEffrazione);
    }
    public void dark_theme(){
        findViewById(R.id.view12).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio2));
        findViewById(R.id.bin1).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio2));
        findViewById(R.id.bin2).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio2));
        findViewById(R.id.bin3).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio2));
        findViewById(R.id.view13).setBackground(getResources().getDrawable(R.drawable.rettangolinoantracite1));
        findViewById(R.id.view9).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio1));
        ((Button)findViewById(R.id.aggiungi)).setBackgroundTintList(ColorStateList.valueOf(Color.rgb(222, 222, 222)));
    }
    public void light_theme(){
        findViewById(R.id.view12).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio4));
        findViewById(R.id.bin1).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio4));
        findViewById(R.id.bin2).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio4));
        findViewById(R.id.bin3).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio4));
        findViewById(R.id.view13).setBackground(getResources().getDrawable(R.drawable.rettangolinoantracite2));
        findViewById(R.id.view9).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio3));
        ((Button)findViewById(R.id.aggiungi)).setBackgroundTintList(ColorStateList.valueOf(Color.rgb(245, 247, 255)));
    }
}