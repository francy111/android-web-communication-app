package com.example.multe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<JSONObject> effrazioni = new ArrayList<>();
    public static ArrayList<Integer> importi = new ArrayList<>();
    public static ArrayList<JSONObject> effrazioniTotali = new ArrayList<>();
    private static double time = 0.0;
    private static String TOKEN;
    public static String IP = "multeonline03.ddns.net";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setNavigationBarColor(Color.BLACK);

        if(getSharedPreferences("theme", MODE_PRIVATE).getBoolean("dark", true)){
            dark_theme();
        }else{
            light_theme();
        }

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        //for POST requests, only the following line should be changed to
        StringRequest sr = new StringRequest(Request.Method.POST, "http://"+MainActivity.IP+":8080/sito/API-PHP/api.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("HttpClient", "success! response: " + response.toString());
                        try {
                            JSONObject rispostaJSON = new JSONObject(response);
                            Intent i;
                            if(rispostaJSON.getString("valido").equals("1")){
                                i = new Intent(MainActivity.this, MenuActivity.class);
                            }else{
                                i = new Intent(MainActivity.this, LoginActivity.class);
                            }
                            startActivity(i);
                            finish();

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
                params.put("function","app-checktoken");
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

        Handler handler = new Handler();
        Runnable runn = new Runnable(){
            @Override
            public void run() {
                time += 1.0;
                ((TextView)findViewById(R.id.time)).setText(MainActivity.secsToMins(time));
                if(time > 30.0) notifyServerDown();
                else handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runn, 1000);
    }

    public static String secsToMins(double time){
        String t;
        if(time%60 < 9)
            t = ""+(int)(time/60)+":0"+(int)(time%60);
        else
            t = ""+(int)(time/60)+":"+(int)(time%60);
        return t;
    }
    public void notifyServerDown(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Warning");
        builder.setMessage("multe.ddns.net:8080 is unable to respond right now, please try again later");
        builder.setCancelable(false);
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertdialog = builder.create();
        alertdialog.show();

    }

    public void dark_theme(){
        findViewById(R.id.bin1).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio2));
        findViewById(R.id.view12).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio2));
        findViewById(R.id.view13).setBackground(getResources().getDrawable(R.drawable.rettangolinoantracite1));
        findViewById(R.id.view9).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio1));
        ((TextView)findViewById(R.id.textView)).setTextColor(Color.rgb(255, 255, 255));
    }
    public void light_theme(){
        findViewById(R.id.bin1).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio4));
        findViewById(R.id.view12).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio4));
        findViewById(R.id.view13).setBackground(getResources().getDrawable(R.drawable.rettangolinoantracite2));
        findViewById(R.id.view9).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio3));
        ((TextView)findViewById(R.id.textView)).setTextColor(Color.rgb(43, 43, 43));
    }
}