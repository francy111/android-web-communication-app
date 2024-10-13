package com.example.multe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setNavigationBarColor(Color.BLACK);

        if(getSharedPreferences("theme", MODE_PRIVATE).getBoolean("dark", true)){
            dark_theme();
        }else{
            light_theme();
        }

        SharedPreferences.Editor  e = getSharedPreferences("vigile", MODE_PRIVATE).edit();
        e.remove("nome");
        e.remove("cognome");
        e.remove("ruolo");
        e.remove("matricola");
        e.remove("token");
        e.commit();

        View.OnClickListener login = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) return;
                mLastClickTime = SystemClock.elapsedRealtime();

                String mcode, pssw;
                mcode = ((EditText) findViewById(R.id.plate)).getText().toString();
                pssw = ((EditText) findViewById(R.id.pssw)).getText().toString();

                if (!mcode.equals("") && !pssw.equals("")) {

                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    //for POST requests, only the following line should be changed to

                    StringRequest sr = new StringRequest(Request.Method.POST, "http://"+MainActivity.IP+":8080/sito/API-PHP/api.php",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("HttpClient", "success! response: " + response.toString());
                                    try {
                                        JSONObject rispostaJSON = new JSONObject(response);

                                        if (rispostaJSON.getInt("esito") == 1) {

                                            SharedPreferences sp = getSharedPreferences("vigile", MODE_PRIVATE);
                                            SharedPreferences.Editor e = sp.edit();

                                            e.putString("nome", rispostaJSON.getString("nome"));
                                            e.putString("cognome", rispostaJSON.getString("cognome"));
                                            e.putString("matricola", rispostaJSON.getString("mcode"));

                                            String adminLevel = rispostaJSON.getString("adminLevel");

                                            String ruolo = adminLevel.equals("0") ? "Vigile" : adminLevel.equals("1") ? "Admin" : "BigPP";
                                            e.putString("ruolo", ruolo);
                                            e.putString("token", rispostaJSON.getString("token"));
                                            e.commit();
                                            startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                                            finish();
                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                            builder.setTitle("Attenzione");
                                            builder.setMessage("Il tuo token è scaduto, sei pregato di rinnovarlo sul sito");
                                            builder.setCancelable(false);
                                            builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                }
                                            });
                                            AlertDialog alertdialog = builder.create();
                                            alertdialog.show();
                                        }

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
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("mcode", ((EditText) findViewById(R.id.plate)).getText().toString());
                            params.put("password", ((EditText) findViewById(R.id.pssw)).getText().toString());
                            params.put("function", "app-authentication");
                            return params;
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Content-Type", "application/x-www-form-urlencoded");
                            return params;
                        }
                    };
                    queue.add(sr);
                }
            }
        };

        (findViewById(R.id.bin)).setOnClickListener(login);
    }
    public static String decodeValue(String value)
    {
        try {
            return new String(value.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            {
                throw new RuntimeException(ex.getCause());
            }
        }
    }
    public void dark_theme(){
        findViewById(R.id.view12).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio2));
        findViewById(R.id.mcode3).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio2));
        findViewById(R.id.viewewew).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio2));
        findViewById(R.id.viewewew12).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio2));
        ((Button)findViewById(R.id.bin)).setBackgroundTintList(ColorStateList.valueOf(Color.rgb(222, 222, 222)));
        findViewById(R.id.view9).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio1));
        findViewById(R.id.view13).setBackground(getResources().getDrawable(R.drawable.rettangolinoantracite1));
    }


    public void light_theme(){
        findViewById(R.id.view12).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio4));
        findViewById(R.id.mcode3).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio4));
        findViewById(R.id.viewewew).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio4));
        findViewById(R.id.viewewew12).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio4));
        ((Button)findViewById(R.id.bin)).setBackgroundTintList(ColorStateList.valueOf(Color.rgb(245, 247, 255)));
        findViewById(R.id.view9).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio3));
        findViewById(R.id.view13).setBackground(getResources().getDrawable(R.drawable.rettangolinoantracite2));
    }
}
