package com.example.buylist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.buylist.BD.SQLiteOpenHelper;
import com.example.buylist.Model.ProductModel;

import java.util.ArrayList;

public class MyArrayAdapterProduct <T> extends ArrayAdapter {

    ProductsActivity productsActivity;
    ArrayList<T> listaProductos;
    SQLiteOpenHelper db;
    int idLista;

    public MyArrayAdapterProduct(ProductsActivity productsActivity, int item_product, ArrayList<T> listaProductos, int idLista) {
        super(productsActivity, item_product, listaProductos);
        this.productsActivity = productsActivity;
        this.listaProductos = listaProductos;
        this.idLista = idLista;
        this.db = new SQLiteOpenHelper(this.getContext());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) { //este View me construye el linearLayout de item_list ya con los datos que tenga
        LayoutInflater constructor = productsActivity.getLayoutInflater();
        View elementoProducto = constructor.inflate(R.layout.item_product, null);

        ProductModel item = (ProductModel) getItem(position);

        TextView tv_nombre = (TextView) elementoProducto.findViewById(R.id.tv_nombrep);

        //Configura el nombre del item
        tv_nombre.setText(((ProductModel) getItem(position)).getNombre());

        Button btn_borrar = (Button) elementoProducto.findViewById(R.id.btn_borrarp);

        //Configura el botón de borrar
        btn_borrar.setOnClickListener(v -> {
            //Elimina el ítem de la lista
            ProductModel productModelSeleccionado = (ProductModel) listaProductos.get(position);
            db.deleteListaProducto(idLista, productModelSeleccionado.getId());
            listaProductos.remove(position);

            //Notifica al adaptador que los datos han cambiado
            notifyDataSetChanged();
        });

        return elementoProducto;
    }
}
