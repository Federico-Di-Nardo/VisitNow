package com.example.visitnow;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Parametros#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Parametros extends Fragment {

    ArrayList<String> paises = new ArrayList<String>();
    ArrayList<Integer> paisesID = new ArrayList<Integer>();
    ArrayList<String> idiomas = new ArrayList<String>();
    ArrayList<Integer> idiomasID = new ArrayList<Integer>();
    ArrayList<Integer> paisesIDSeleccionados = new ArrayList<Integer>();
    ArrayList<Integer> idiomasSeleccionados = new ArrayList<Integer>();
    ArrayList<String> nivelesSeleccionados = new ArrayList<>();

    ArrayList<Integer> idiomasSeleccionadosCopia;
    ArrayList<String> nivelesSeleccionadosCopia;
    ArrayList<String> idiomasCopia;

    TextView tvNacionalidades;
    TextView tvIdiomas;
    RadioButton radMasc;
    RadioButton radFem;
    EditText txtEdad;
    EditText txtMail;
    String error = "";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Parametros() {
        // Required empty public constructor
    }

    public static Parametros newInstance(String param1, String param2) {
        Parametros fragment = new Parametros();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_parametros, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        paises.clear();
        tvNacionalidades = view.findViewById(R.id.tvNacionalidades);
        tvIdiomas = view.findViewById(R.id.tvIdiomas);
        radMasc = view.findViewById(R.id.sexMasc);
        radFem = view.findViewById(R.id.sexFem);
        txtEdad = view.findViewById(R.id.edad);
        txtMail = view.findViewById(R.id.mail);
        final NavController navController = Navigation.findNavController(view);

        // recupera datos de la base de datos
        SharedPreferences sharedPref = getActivity().getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        int idUsuario = sharedPref.getInt("idUsuario", 0);
        ResultSet rs = null;
        if(idUsuario != 0) {
            rs = Database.usuarioSP("03", idUsuario, idUsuario, "", view.getContext());
            try {
                while (rs.next()) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("idUsuario", rs.getInt(1));
                    editor.commit();
                    txtMail.setText(rs.getString(2));
                    String codExt = rs.getString(4);
                    if (rs.getString(4).equals("EDAD")) {
                        txtEdad.setText(rs.getString(3));
                    } else if (rs.getString(4).equals("SEXO")) {
                        if (rs.getString(3).equals("F")) {
                            radFem.setChecked(true);
                        } else if (rs.getString(3).equals("M")) {
                            radMasc.setChecked(true);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }

            rs = Database.nacionalidadSP("08", idUsuario, 0, idUsuario,"",0);
            try {
                tvNacionalidades.setText(tvNacionalidades.getText()+"\n");
                while (rs.next()) {
                    tvNacionalidades.setText(tvNacionalidades.getText()+ "\n"+rs.getString(2));
                    paisesIDSeleccionados.add(rs.getInt(1));
                }
            } catch (Exception e) {
                System.out.println(e);
            }

            rs = Database.usuarioIdiomaSP("08",idUsuario,0,idUsuario,0,0,"","");
            try {
                tvIdiomas.setText(tvIdiomas.getText()+"\n");
                while (rs.next()) {
                    tvIdiomas.setText(tvIdiomas.getText() + "\n"+rs.getString(3) +" - "+ rs.getString(4));
                    idiomasSeleccionados.add(rs.getInt(2));
                    nivelesSeleccionados.add(rs.getString(4));
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        rs = Database.paisSP("08", 1,0);
        try {
            while (rs.next()){
                paises.add(rs.getString(2));
                paisesID.add(rs.getInt(1));
            }
        }catch (Exception e){
            System.out.println(e);
        }

        rs = Database.idiomaSP("08", 1);
        try {
            while (rs.next()){
                int idiomaID = rs.getInt(1);
                System.out.println(idiomasSeleccionados);
                System.out.println(idiomaID);
                if (idiomasSeleccionados.contains(idiomaID)){
                    idiomas.add(rs.getString(2) + "  -  " + nivelesSeleccionados.get(idiomasSeleccionados.indexOf(idiomaID)));
                }else{
                    idiomas.add(rs.getString(2));
                }
                idiomasID.add(idiomaID);
            }
        }catch (Exception e){
            System.out.println(e);
        }

        Button btnNacionalidad = view.findViewById(R.id.btnNacionalidades);
        btnNacionalidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupNacionalidades(view);
            }
        });

        Button btnIdiomas = view.findViewById(R.id.btnIdiomas);
        btnIdiomas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupIdiomas(view, true);
            }
        });

        Button btnConfirmar = view.findViewById(R.id.btnConfirmar);
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grabarParametros(view);
                if (error.equals("")){
                    navController.navigate(R.id.menuPrincipal);
                }else{
                    Toast.makeText(view.getContext(),error,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void grabarParametros(View view) {
        //String[] nacionalidades = tvNacionalidades.getText().toString().split(";");
        error = "";
        String sexo = "";

        sexo = (radMasc.isChecked() ? "M" : (radFem.isChecked() ? "F" : ""));

        // recupero el usuario activo de las opciones de configuración del teléfono
        SharedPreferences sharedPref = getActivity().getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        int idUsuario = sharedPref.getInt("idUsuario", 0);

        // crear nuevo usuario. En caso de que ya exista, lo actualiza y recupera los datos
        ResultSet rs = Database.usuarioSP("01", idUsuario, idUsuario, txtMail.getText().toString(), view.getContext());
        try {
            rs.next();
            idUsuario = rs.getInt(1);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("idUsuario", idUsuario);
            editor.commit();
        }catch (Exception e){
            System.out.println(e);
        }

        Database.parametroSP("01", idUsuario, 0, idUsuario, "EDAD", txtEdad.getText().toString());
        Database.parametroSP("01", idUsuario, 0, idUsuario, "SEXO", sexo);

        for (int i = 0; i<= paisesID.size() - 1; i++){
            if(!paisesIDSeleccionados.contains(paisesID.get(i))){
                ResultSet rs2 = Database.nacionalidadSP("04", idUsuario, 0, idUsuario, null, paisesID.get(i));
            }
        }
        for (int i = 0; i <= paisesIDSeleccionados.size() - 1; i++) {
            ResultSet rs3 = Database.nacionalidadSP("01", idUsuario, 0, idUsuario, null, paisesIDSeleccionados.get(i));
            try {
                if(rs3 != null && rs3.getFetchSize() > 0){
                    rs3.next();
                    if (rs3.getInt(1) < 0) {
                        error = rs3.getString(2);
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }

        for (int i = 0; i<= idiomas.size() - 1; i++){
            if (idiomas.get(i).contains("-")){
                ResultSet rs4 = Database.usuarioIdiomaSP("01",idUsuario,0,idUsuario,0,0,idiomas.get(i).split("-")[0].trim(),idiomas.get(i).split("-")[1].trim());
            }else{
                ResultSet rs4 = Database.usuarioIdiomaSP("04",idUsuario,0,idUsuario,0,0,idiomas.get(i),"");
            }
        }
    }

    public void popupNacionalidades(View view){
        ArrayList<String> itemsSeleccionados = new ArrayList<String>();
        ArrayList<Integer> paisesIDSeleccionadosCopia = new ArrayList<>(paisesIDSeleccionados);
        ArrayList<Boolean> itemsCheckeados = new ArrayList<Boolean>();

        // crea una lista marcando qué items están seleccionados y cuales no. Luego se transforma la lista en array
        for (int i = 0; i < paisesID.size(); i++) {
            boolean check = false;
            for (int j = 0; j < paisesIDSeleccionados.size(); j++){
                if(paisesID.get(i) == paisesIDSeleccionados.get(j)){
                    itemsSeleccionados.add(paises.get(i));
                    check = true;
                    break;
                }
            }
            itemsCheckeados.add(check);
        }
        boolean[] arrayItemsCheckeados = new boolean[itemsCheckeados.size()];
        for (int i = 0; i < itemsCheckeados.size(); i++){
            arrayItemsCheckeados[i] = itemsCheckeados.get(i);
        }

        //paisesIDSeleccionados.clear();

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Seleccione su/s nacionalidad/es");
        builder.setMultiChoiceItems(paises.toArray(new String[0]), arrayItemsCheckeados, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    // If the user checked the item, add it to the selected items
                    itemsSeleccionados.add(paises.get(which));
                    paisesIDSeleccionadosCopia.add(paisesID.get(which));
                } else/* if (itemsSeleccionados.contains(Integer.toString(which)))*/ {
                    // Else, if the item is already in the array, remove it
                    itemsSeleccionados.remove( itemsSeleccionados.indexOf(paises.get(which)));
                    paisesIDSeleccionadosCopia.remove(paisesIDSeleccionadosCopia.indexOf(paisesID.get(which)));
                }
            }
        });
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface dialog, int id) {
                paisesIDSeleccionados = new ArrayList<>(paisesIDSeleccionadosCopia);
                tvNacionalidades.setText("Nacionalidades\n\n"+itemsSeleccionados.stream().collect(Collectors.joining("\n")));
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //tvIdiomas.setText("");
            }
        });
        builder.show();
    }

    public void popupIdiomas(View view, boolean primeraVez){
        if (primeraVez){
            idiomasSeleccionadosCopia = new ArrayList<>(idiomasSeleccionados);
            nivelesSeleccionadosCopia = new ArrayList<>(nivelesSeleccionados);
            idiomasCopia = new ArrayList<>(idiomas);
        }
        nivelesSeleccionados.clear();

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Seleccione los idiomas que habla");
        builder.setItems(idiomas.toArray(new String[0]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //popupNiveles(view, (TextView) ((AlertDialog)dialog).getListView().getItemAtPosition(which), which);
                popupNiveles(view, dialog, which);
            }
        });
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface dialog, int id) {
                ArrayList<String> idiomasMostrar = new ArrayList<>();
                for (int i = 0; i<idiomas.size(); i++){
                    if (idiomas.get(i).contains("-")){
                        idiomasMostrar.add(idiomas.get(i));
                    }
                }
                tvIdiomas.setText("Idiomas\n\n"+ idiomasMostrar.stream().collect(Collectors.joining("\n")));
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                idiomasSeleccionados = idiomasSeleccionadosCopia;
                idiomas = idiomasCopia;
                nivelesSeleccionados = nivelesSeleccionadosCopia;
            }
        });
        builder.show();
    }

    public void popupNiveles(View view, DialogInterface idiomaDialog, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        ArrayList<String> niveles = new ArrayList<>();
        ArrayList<Integer> nivelesId = new ArrayList<>();
        ResultSet rs = Database.nivelSP("08", 0, idiomasID.get(position).toString());

        niveles.add("(Nada)");
        nivelesId.add(0);
        try {
            while (rs.next()){
                niveles.add(rs.getString(2));
                nivelesId.add(rs.getInt(1));
            }
        }catch (Exception e){
            System.out.println(e);
        }
        builder.setTitle("Seleccione el nivel");
        builder.setItems(niveles.toArray(new String[0]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    idiomas.set(position, idiomas.get(position).split("-")[0].trim());
                }else{
                    idiomas.set(position, idiomas.get(position).split("-")[0].trim() + "   -   "+ niveles.get(which));
                }
                popupIdiomas(view, false);
                //System.out.println(((AlertDialog)idiomaDialog).getListView());
                //textView.setText(((AlertDialog)idiomaDialog).getListView().getItemAtPosition(position) + " - " + finalNiveles[which]);
            }
        });
        builder.show();
    }
}