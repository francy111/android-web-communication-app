package com.example.multe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {
    private long mLastClickTime = 0;

    private int super_secret = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getWindow().setNavigationBarColor(Color.BLACK);

        if(getSharedPreferences("theme", MODE_PRIVATE).getBoolean("dark", true)){
            dark_theme();
        }else{
            light_theme();
        }

        View.OnClickListener info = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) return;
                mLastClickTime = SystemClock.elapsedRealtime();

                startActivity(new Intent(MenuActivity.this, InfoActivity.class));
            }
        };
        View.OnClickListener logout = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) return;
                mLastClickTime = SystemClock.elapsedRealtime();

                startActivity(new Intent(MenuActivity.this, LoginActivity.class));
                finish();
            }
        };
        View.OnClickListener nuovaMulta = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) return;
                mLastClickTime = SystemClock.elapsedRealtime();

                startActivity(new Intent(MenuActivity.this, NuovaMultaActivity.class));
            }
        };
        View.OnClickListener vediMulte = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) return;
                mLastClickTime = SystemClock.elapsedRealtime();

                startActivity(new Intent(MenuActivity.this, VisualizzaMultaActivity.class));
            }
        };

        View.OnClickListener switch1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(super_secret < 5) super_secret ++;
                else super_secret = 0;
            }
        };
        View.OnClickListener switch2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences s = getSharedPreferences("theme", MODE_PRIVATE);
                boolean b = s.getBoolean("dark", true);

                if(super_secret == 5) {
                    SharedPreferences.Editor e = s.edit();
                    e.putBoolean("dark", !b);
                    e.commit();
                    if(b) light_theme();
                    else dark_theme();
                }
            }
        };

        (findViewById(R.id.binfo1)).setOnClickListener(info);
        (findViewById(R.id.binfo2)).setOnClickListener(info);

        (findViewById(R.id.bout1)).setOnClickListener(logout);
        (findViewById(R.id.bout2)).setOnClickListener(logout);

        (findViewById(R.id.bnuovo1)).setOnClickListener(nuovaMulta);
        (findViewById(R.id.bnuovo2)).setOnClickListener(nuovaMulta);

        (findViewById(R.id.bvedi1)).setOnClickListener(vediMulte);
        (findViewById(R.id.bvedi2)).setOnClickListener(vediMulte);


        (findViewById(R.id.imageView6)).setOnClickListener(switch1);
        (findViewById(R.id.badge)).setOnClickListener(switch2);
    }

    public void dark_theme(){
        findViewById(R.id.bnuovo1).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio2));
        findViewById(R.id.bvedi1).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio2));
        findViewById(R.id.binfo1).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio2));
        findViewById(R.id.view7).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio2));
        findViewById(R.id.bout1).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio2));
        findViewById(R.id.view8).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio2));
        findViewById(R.id.view).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio1));
        findViewById(R.id.view3).setBackground(getResources().getDrawable(R.drawable.rettangolinoantracite1));
    }
    public void light_theme(){
        findViewById(R.id.bnuovo1).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio4));
        findViewById(R.id.bvedi1).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio4));
        findViewById(R.id.binfo1).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio4));
        findViewById(R.id.view7).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio4));
        findViewById(R.id.bout1).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio4));
        findViewById(R.id.view8).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio4));
        findViewById(R.id.view).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio3));
        findViewById(R.id.view3).setBackground(getResources().getDrawable(R.drawable.rettangolinoantracite2));
    }
}