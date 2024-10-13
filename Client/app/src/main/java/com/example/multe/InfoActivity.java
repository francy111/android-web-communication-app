package com.example.multe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        getWindow().setNavigationBarColor(Color.BLACK);

        if(getSharedPreferences("theme", MODE_PRIVATE).getBoolean("dark", true)){
            dark_theme();
        }else{
            light_theme();
        }

        SharedPreferences sp = getSharedPreferences("vigile", MODE_PRIVATE);

        ((TextView)findViewById(R.id.nome)).setText(sp.getString("nome", ""));
        ((TextView)findViewById(R.id.cognome)).setText(sp.getString("cognome", ""));
        ((TextView)findViewById(R.id.ruolo)).setText(sp.getString("ruolo", ""));
        ((TextView)findViewById(R.id.matricola)).setText(sp.getString("matricola", ""));
    }

    public void dark_theme(){
        findViewById(R.id.bin1).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio2));
        findViewById(R.id.view12).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio2));
        findViewById(R.id.view13).setBackground(getResources().getDrawable(R.drawable.rettangolinoantracite1));
        findViewById(R.id.view9).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio1));
        ((TextView)findViewById(R.id.textView)).setTextColor(Color.rgb(255, 255, 255));
        ((TextView)findViewById(R.id.textView2)).setTextColor(Color.rgb(255, 255, 255));
    }
    public void light_theme(){
        findViewById(R.id.bin1).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio4));
        findViewById(R.id.view12).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio4));
        findViewById(R.id.view13).setBackground(getResources().getDrawable(R.drawable.rettangolinoantracite2));
        findViewById(R.id.view9).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio3));
        ((TextView)findViewById(R.id.textView)).setTextColor(Color.rgb(43, 43, 43));
        ((TextView)findViewById(R.id.textView2)).setTextColor(Color.rgb(43, 43, 43));
    }
}