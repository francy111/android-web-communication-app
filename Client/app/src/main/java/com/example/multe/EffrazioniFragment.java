package com.example.multe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EffrazioniFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EffrazioniFragment extends Fragment implements View.OnClickListener{
    private long mLastClickTime = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EffrazioniFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EffrazioniFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EffrazioniFragment newInstance(String param1, String param2) {
        EffrazioniFragment fragment = new EffrazioniFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View c = inflater.inflate(R.layout.fragment_effrazioni, container, false);
        c.findViewById(R.id.addbreakin1).setOnClickListener(this);
        c.findViewById(R.id.addbreakin2).setOnClickListener(this);

        c.findViewById(R.id.Aggiungi).setOnClickListener(this);

        if(getActivity().getSharedPreferences("theme", 0).getBoolean("dark", true)){
            c.findViewById(R.id.addbreakin1).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio2));
            c.findViewById(R.id.view6).setBackground(getResources().getDrawable(R.drawable.rettangolinoantracite1));
            ((Button)c.findViewById(R.id.Aggiungi)).setBackgroundTintList(ColorStateList.valueOf(Color.rgb(222, 222, 222)));
        }else{
            c.findViewById(R.id.addbreakin1).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio4));
            c.findViewById(R.id.view6).setBackground(getResources().getDrawable(R.drawable.rettangolinoantracite2));
            ((Button)c.findViewById(R.id.Aggiungi)).setBackgroundTintList(ColorStateList.valueOf(Color.rgb(245, 247, 255)));
        }

        Button effr, divider;
        divider = new Button(getActivity());
        if(getActivity().getSharedPreferences("theme", 0).getBoolean("dark", true)) {
            divider.setBackgroundColor(Color.rgb(43, 43, 43));
        }else{
            divider.setBackgroundColor(Color.rgb(245, 247, 255));
        }
        ((LinearLayout)c.findViewById(R.id.scrolcoc)).addView(divider, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 35));

        try {
            for (int i = 0; i < MainActivity.effrazioni.size(); i++) {
                effr = new Button(getActivity());
                divider = new Button(getActivity());


                Drawable d;
                if(getActivity().getSharedPreferences("theme", 0).getBoolean("dark", true)) {
                    d = getActivity().getResources().getDrawable(R.drawable.rettangolinogrigio2);
                }else{
                    d = getActivity().getResources().getDrawable(R.drawable.rettangolinogrigio4);
                }
                effr.setBackground(d);
                effr.setTextColor(Color.rgb(43, 43, 43));
                effr.setText(MainActivity.effrazioni.get(i).getString("nome"));
                effr.setId(i);
                effr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) return;
                        mLastClickTime = SystemClock.elapsedRealtime();

                        JSONObject o = MainActivity.effrazioni.get(view.getId());

                        MainActivity.importi.remove(MainActivity.effrazioni.indexOf(o));
                        MainActivity.effrazioni.remove(o);
                        MainActivity.effrazioniTotali.add(o);


                        getActivity().finish();
                        getActivity().startActivity(getActivity().getIntent());
                    }
                });


                if(getActivity().getSharedPreferences("theme", 0).getBoolean("dark", true)) {
                    divider.setBackgroundColor(Color.rgb(43, 43, 43));
                }else{
                    divider.setBackgroundColor(Color.rgb(245, 247, 255));
                }

                ((LinearLayout) c.findViewById(R.id.scrolcoc)).addView(divider, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 35));
                ((LinearLayout) c.findViewById(R.id.scrolcoc)).addView(effr, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 165));
            }
        }catch(Exception e){}

        return c;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.addbreakin1:
                a(this);
                break;
            case R.id.addbreakin2:
                a(this);
                break;
            case R.id.Aggiungi:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) return;
                mLastClickTime = SystemClock.elapsedRealtime();
                Intent i = new Intent(getActivity(), EffrazioneActivity.class);
                startActivity(i);
                break;
        }
    }
    public void a(Fragment f){

        getActivity().findViewById(R.id.effrazioni).setVisibility(View.GONE);
        getActivity().recreate();
        SharedPreferences.Editor e = getActivity().getSharedPreferences("fragment" , 0).edit();
        e.putInt("visibility", 8);
        e.commit();
    }

}