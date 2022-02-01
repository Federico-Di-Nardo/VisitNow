package com.example.visitnow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RestriccionAdapter extends RecyclerView.Adapter<RestriccionAdapter.ViewHolder> {

    Context context;
    String[] descRestricciones;
    String[] imgRestricciones;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView rowDesc;
        ImageView rowCheck;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rowDesc = itemView.findViewById(R.id.descripcionItem);
            rowCheck = itemView.findViewById(R.id.checkItem);
        }
    }

    public RestriccionAdapter(Context context, String[] descRestricciones, String[] imgRestricciones){
        this.context = context;
        this.descRestricciones = descRestricciones;
        this.imgRestricciones = imgRestricciones;
    }


    @NonNull
    @Override
    public RestriccionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_restriccion, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestriccionAdapter.ViewHolder holder, int position) {
        if(descRestricciones[position] != null){
            holder.rowDesc.setText(descRestricciones[position]);
            if(imgRestricciones[position].equals("1")){
                holder.rowCheck.setImageResource(R.drawable.circle_check_yes);
            }else if(imgRestricciones[position].equals("0")){
                holder.rowCheck.setImageResource(R.drawable.circle_check_no);
            }else{
                holder.rowCheck.setImageResource(R.drawable.circle_check_gray);
            }
        }
        /* FUNCIONALIDAD ETAPA 2: Detalle
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.detalle);

                SharedPreferences sharedPref = context.getSharedPreferences("preferencias", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("pais", paises[holder.getAdapterPosition()]);
                editor.putString("descripcion", descripciones[holder.getAdapterPosition()]);
                editor.putString("imagen", imagenes[holder.getAdapterPosition()]);
                editor.putString("bandera", banderas[holder.getAdapterPosition()]);
                editor.commit();


                //detNombrePais.setText(imagenes[holder.getAdapterPosition()]);
                //detBanderaPais.setImageResource(banderas[holder.getAdapterPosition()]);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return descRestricciones.length;
    }
}
