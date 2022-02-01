package com.example.visitnow;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class PaisAdapter extends RecyclerView.Adapter<PaisAdapter.ViewHolder> {

    Context context;
    String[] idPais;
    String[] paises;
    String[] descripciones;
    String[] banderas;
    String[] imagenes;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView rowPais;
        ImageView rowBandera;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rowPais = itemView.findViewById(R.id.descripcionItem);
            rowBandera = itemView.findViewById(R.id.checkItem);
        }
    }

    public PaisAdapter(Context context, String[] idPais, String[] paises, String[] descripciones, String[] banderas, String[] imagenes){
        this.context = context;
        this.idPais = idPais;
        this.paises = paises;
        this.descripciones = descripciones;
        this.banderas = banderas;
        this.imagenes = imagenes;
    }


    @NonNull
    @Override
    public PaisAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_lista, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PaisAdapter.ViewHolder holder, int position) {
        holder.rowPais.setText(paises[position]);
        if(banderas[position] != null){
            byte[] banderaBytes = Base64.decode(banderas[position], Base64.DEFAULT);
            holder.rowBandera.setImageBitmap(BitmapFactory.decodeByteArray(banderaBytes, 0, banderaBytes.length));
        }
        //holder.rowBandera.setImageResource(banderas[position]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.detalle);

                SharedPreferences sharedPref = context.getSharedPreferences("preferencias", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("idPais", idPais[holder.getAdapterPosition()]);
                editor.putString("pais", paises[holder.getAdapterPosition()]);
                editor.putString("descripcion", descripciones[holder.getAdapterPosition()]);
                editor.putString("imagen", imagenes[holder.getAdapterPosition()]);
                editor.putString("bandera", banderas[holder.getAdapterPosition()]);
                editor.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return paises.length;
    }
}
