package com.example.buylist;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.buylist.BD.SQLiteOpenHelper;
import com.example.buylist.Model.ListaModel;

import java.util.ArrayList;

public class MiArrayAdapterList<T> extends ArrayAdapter {

    ListasActivity listasActivity;
    ArrayList<T> listaDatos;
    SQLiteOpenHelper db;


    public MiArrayAdapterList(ListasActivity listasActivity, int item_list, ArrayList<T> datos) {
        super(listasActivity, R.layout.item_list, datos);
        this.listasActivity = listasActivity;
        this.listaDatos = datos; // Guarda la referencia a la lista de datos
        this.db = new SQLiteOpenHelper(this.getContext());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) { //este View me construye el linearLayout de item_list ya con los datos que tenga
        LayoutInflater constructor = listasActivity.getLayoutInflater();
        View elementoLista = constructor.inflate(R.layout.item_list, null);

        ListaModel item = (ListaModel) getItem(position);

        TextView tv_nombre = (TextView) elementoLista.findViewById(R.id.tv_nombre);
        //Configura el nombre del item
        tv_nombre.setText(((ListaModel) getItem(position)).getNombre());

        Button btn_borrar = (Button) elementoLista.findViewById(R.id.btn_borrar);
        //Configura el botón de borrar
        btn_borrar.setOnClickListener(v -> {
            //Elimina el ítem de la lista
            ListaModel listModelSeleccionado = (ListaModel) listaDatos.get(position);
            db.deleteItemLista(listModelSeleccionado.getId());
            listaDatos.remove(position);

            //Notifica al adaptador que los datos han cambiado
            notifyDataSetChanged();
        });

        //Configurar el click en el item
        elementoLista.setOnClickListener(v -> {
            if (item != null) {
                //Iniciar la nueva actividad y pasar el nombre del ítem
                System.out.println("item " + item.getId() + "  nombre " + item.getNombre());
                Intent intent = new Intent(listasActivity, ProductsActivity.class);
                intent.putExtra("id_item", item.getId());
                intent.putExtra("nombre_item", item.getNombre());
                listasActivity.startActivity(intent);
            }

        });

        return elementoLista;
    }
}


