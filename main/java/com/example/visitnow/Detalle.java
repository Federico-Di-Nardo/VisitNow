package com.example.visitnow;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Detalle#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Detalle extends Fragment {

    ArrayList<String> descRestricciones = new ArrayList<String>();
    ArrayList<String> imgRestricciones = new ArrayList<String>();
    int flgMigrar = 1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Detalle() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Detalle.
     */
    // TODO: Rename and change types and number of parameters
    public static Detalle newInstance(String param1, String param2) {
        Detalle fragment = new Detalle();
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        int idUsuario = sharedPref.getInt("idUsuario", 0);
        String idPais = sharedPref.getString("idPais", "");
        String pais = sharedPref.getString("pais", "");
        String descripcion = sharedPref.getString("descripcion", "");
        String imagen = sharedPref.getString("imagen", "");
        String bandera = sharedPref.getString("bandera", "");

        TextView detNombrePais = view.findViewById(R.id.detNombrePais);
        TextView detDescripcionPais = view.findViewById(R.id.detDescripcionPais);
        ImageView detImagenPais = view.findViewById(R.id.detImagenPais);
        ImageView detBanderaPais = view.findViewById(R.id.detBanderaPais);

        detDescripcionPais.setMovementMethod(new ScrollingMovementMethod());

        detNombrePais.setText(pais);
        detDescripcionPais.setText(descripcion);

        if(bandera != null){
            byte[] banderaBytes = Base64.decode(bandera, Base64.DEFAULT);
            detBanderaPais.setImageBitmap(BitmapFactory.decodeByteArray(banderaBytes, 0, banderaBytes.length));
        }

        if(imagen != null){
            byte[] imagenBytes = Base64.decode(imagen, Base64.DEFAULT);
            detImagenPais.setImageBitmap(BitmapFactory.decodeByteArray(imagenBytes, 0, imagenBytes.length));
        }


        RecyclerView recyclerView = view.findViewById(R.id.containerRestricciones);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        Switch chkMigrar = view.findViewById(R.id.detalleFlgMigrar);
        chkMigrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flgMigrar = chkMigrar.isChecked() ? 0 : 1;
                restriccionesGetList(idUsuario, idPais, recyclerView);
            }
        });

        restriccionesGetList(idUsuario, idPais, recyclerView);
    }

    public void restriccionesGetList(int idUsuario, String idPais, RecyclerView recyclerView){
        // recupera datos de la base de datos
        descRestricciones.clear();
        imgRestricciones.clear();

        ResultSet rs = null;
        if(idUsuario == 0){
            rs = Database.restriccionSP("08", idUsuario, 0,"", "", String.valueOf(flgMigrar), idPais, 0);
        }else{
            rs = Database.restriccionSP("18", idUsuario, 0,"restriccionDes", "", String.valueOf(flgMigrar), idPais, 0);
        }
        try {
            while (rs.next()){
                descRestricciones.add(rs.getString(2));
                imgRestricciones.add( (idUsuario == 0 ? "2" : rs.getString(8))  );
            }
        }catch (Exception e){
            System.out.println(e);
        }

        // crea un adapter
        RecyclerView.Adapter restriccionAdapter = new RestriccionAdapter(getActivity(), //paises, descripciones, imagenes, banderas
                descRestricciones.toArray(new String[0]),
                imgRestricciones.toArray(new String[0])
        );
        recyclerView.setAdapter(restriccionAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detalle, container, false);
    }
}