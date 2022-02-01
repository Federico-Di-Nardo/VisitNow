package com.example.visitnow;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class MenuPrincipal extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MenuPrincipal() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuPrincipal.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuPrincipal newInstance(String param1, String param2) {
        MenuPrincipal fragment = new MenuPrincipal();
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
        return inflater.inflate(R.layout.fragment_menu_principal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnVerPaises = view.findViewById(R.id.btnVerPaises);
        Button btnVerPosibilidades = view.findViewById(R.id.btnVerPosibilidades);
        Button btnParametros = view.findViewById(R.id.btnParametros);

        final NavController navController = Navigation.findNavController(view);
        btnVerPaises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = getActivity().getSharedPreferences("preferencias", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("flgParams", 0);
                editor.commit();
                navController.navigate(R.id.grilla);
            }
        });

        btnVerPosibilidades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = getActivity().getSharedPreferences("preferencias", Context.MODE_PRIVATE);
                int idUsuario = sharedPref.getInt("idUsuario", 0);
                if(idUsuario == 0){
                    Toast.makeText(view.getContext(),"Debe establecer parámetros para utilizar esta funcionalidad.",Toast.LENGTH_SHORT).show();
                }else{
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("flgParams", 1);
                    editor.commit();
                    navController.navigate(R.id.grilla);
                }
            }
        });

        btnParametros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.parametros);
            }
        });
    }
}