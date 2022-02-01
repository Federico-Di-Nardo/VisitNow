package com.example.visitnow;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Grilla extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter paisAdapter;
    RecyclerView.LayoutManager layoutManager;

    int idUsuario;
    int flgMigrar = 1;
    String actividad;
    ArrayList<String> idPais = new ArrayList<String>();
    ArrayList<String> paises = new ArrayList<String>();
    ArrayList<String> descripciones = new ArrayList<String>();
    ArrayList<String> banderas = new ArrayList<String>();
    ArrayList<String> imagenes = new ArrayList<String>();

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public Grilla() {
        // Required empty public constructor
    }

    public static Grilla newInstance(String param1, String param2) {
        Grilla fragment = new Grilla();
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

        View v = this.getView();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grilla, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //recycler
        RecyclerView recyclerView = view.findViewById(R.id.containerPaises);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        idUsuario = sharedPref.getInt("idUsuario", 0);

        actividad = (sharedPref.getInt("flgParams",0) == 0 ? "08" : "18");

        Switch chkMigrar = view.findViewById(R.id.flgMigrar);
        if (actividad == "08"){
            TextView tvMigrar = view.findViewById(R.id.tvMigrar);
            TextView tvVisitar = view.findViewById(R.id.tvVisitar);
            tvMigrar.setVisibility(View.INVISIBLE);
            tvMigrar.setHeight(0);
            tvVisitar.setVisibility(View.INVISIBLE);
            tvVisitar.setHeight(0);
            chkMigrar.setVisibility(View.INVISIBLE);
            chkMigrar.setHeight(0);

            ConstraintLayout.LayoutParams newLayoutParams = (ConstraintLayout.LayoutParams) recyclerView.getLayoutParams();
            newLayoutParams.topMargin = 20;
            newLayoutParams.leftMargin = 0;
            newLayoutParams.rightMargin = 0;
            recyclerView.setLayoutParams(newLayoutParams);
        }

        chkMigrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flgMigrar = chkMigrar.isChecked() ? 0 : 1;
                paisesGetList(recyclerView);
            }
        });

        paisesGetList(recyclerView);
    }

    private void paisesGetList(RecyclerView recyclerView){
        // recupera datos de la base de datos
        idPais.clear();
        paises.clear();
        descripciones.clear();
        imagenes.clear();
        banderas.clear();

        ResultSet rs = null;
        if(actividad == "08"){
            rs = Database.paisSP("08", idUsuario, flgMigrar);
        }else if (actividad == "18")
            rs = Database.paisSP("18", idUsuario, flgMigrar);
        try {
            while (rs.next()){
                idPais.add(rs.getString(1));
                paises.add(rs.getString(2));
                descripciones.add(rs.getString(3));
                imagenes.add(rs.getString(4));
                banderas.add(rs.getString(5));
            }
        }catch (Exception e){
            System.out.println(e);
        }

        // crea un adapter
        paisAdapter = new PaisAdapter(getActivity(), //paises, descripciones, imagenes, banderas
                idPais.toArray(new String[0]),
                paises.toArray(new String[0]),
                descripciones.toArray(new String[0]),
                banderas.toArray(new String[0]),
                imagenes.toArray(new String[0])
        );
        recyclerView.setAdapter(paisAdapter);
    }
}