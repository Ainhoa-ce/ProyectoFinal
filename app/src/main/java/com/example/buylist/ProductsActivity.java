package com.example.buylist;

import static com.example.buylist.utils.Utils.deleteMemoryUser;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.buylist.BD.SQLiteOpenHelper;
import com.example.buylist.Constants.Constants;
import com.example.buylist.Model.ProductModel;
import com.example.buylist.R.id;
import com.example.buylist.utils.Utils;

import java.util.ArrayList;

public class ProductsActivity extends AppCompatActivity {

    Button btnadd, btnvolver, btncerrarsesion;
    SQLiteOpenHelper db;
    int idListaIntent;
    RadioButton rbanadir, rbelegir;
    RadioGroup grupobtn;
    LinearLayout contenedor;
    EditText etnuevoproducto;
    Spinner spinner;
    int idUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        //checkear usuario
        boolean isLooged = Utils.isLoogedUser(getApplicationContext());
        if(!isLooged){
            Intent intent = new Intent(ProductsActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        int idUser = Utils.getMemoryInt(getApplicationContext(), Constants.KEY_MEMORY_ID_USER);
        this.idUser = idUser;

        //Obtener el nombre del Item a la nueva actividad
        idListaIntent = getIntent().getIntExtra("id_item", 1000);
        String nombreListaIntent = getIntent().getStringExtra("nombre_item");

        //Configurar el TextView para mostar el nombre del Item
        TextView tvNombreItem = (TextView) findViewById(R.id.tv_nombre_item);
        tvNombreItem.setText(nombreListaIntent);

        //iniciar bd
        db = new SQLiteOpenHelper(this);

        ArrayList<ProductModel> productsLists = obtenerListaProductoDB(idListaIntent);

        // Configurar el ListView para mostrar los productos
        ListView productListView = ((ListView) findViewById(R.id.lv_producto));
        MyArrayAdapterProduct<ProductModel> adaptador = new MyArrayAdapterProduct<>(ProductsActivity.this, R.layout.item_product, productsLists, idListaIntent);
        productListView.setAdapter(adaptador);


        //Inicializar las vistas del botón añadir, volver y cerrar sesión
        btnadd = (Button) findViewById(R.id.btnadd);
        btnvolver = findViewById(R.id.btnvolverP);
        btncerrarsesion = findViewById(R.id.btncerrarsesionP);

        // Configurar botón de añadir nuevos productos
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoAgregarProducto();
            }
        });

        // Configurar botón de volver atrás
        btnvolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cierra ProductsActivity y vuelve a ListasActivity
            }
        });

        //Configurar el botón para cerrar la sesión

        btncerrarsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMemoryUser(getApplicationContext());
                // Crea un Intent para ir a LoginActivity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                // Inicia LoginActivity
                startActivity(intent);
                // Cierra la actividad actual (ListasActivity)
                finish();
            }
        });



    }

    // Método para obtener los productos de una lista de la base de datos
    public ArrayList<ProductModel> obtenerListaProductoDB(int pIdLista) {

        //Crea el arrayList de listaProducto
        ArrayList<ProductModel> productsLists = new ArrayList<>();
        Cursor cursorBD = db.getProductosLists(pIdLista);

        if (cursorBD != null) {
            if (cursorBD.moveToFirst()) {
                do {
                    int idproduct = cursorBD.getInt(cursorBD.getColumnIndexOrThrow("id"));
                    String product_name = cursorBD.getString(cursorBD.getColumnIndexOrThrow("nombre"));

                    ProductModel itemProduct = new ProductModel(idproduct, product_name);
                    productsLists.add(itemProduct);
                } while (cursorBD.moveToNext());
            }

        }
        cursorBD.close();

        return productsLists;

    }

    // Método para obtener el ID del producto pasado su nombre
    private int obtenerIdProductoPorNombre(String nombreProducto) {
        int idProducto = -1;
        Cursor cursor = db.getProductos(idUser);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                if (nombre.equals(nombreProducto)) {
                    idProducto = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    break;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return idProducto;
    }

    // Método para mostrar el diálogo de añadir producto
    private void mostrarDialogoAgregarProducto() {
        AlertDialog.Builder constructor = new AlertDialog.Builder(ProductsActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_add_product, null);
        constructor.setView(dialogView);

        //Inicializar el boton de elegir y añadir
        grupobtn = dialogView.findViewById(id.radiogroup);
        rbanadir = dialogView.findViewById(id.btnanadir);
        rbelegir = dialogView.findViewById(id.btnelegir);
        contenedor = dialogView.findViewById(id.contenedor);
        etnuevoproducto = dialogView.findViewById(id.etnuevop);
        spinner = dialogView.findViewById(id.spinner);

        //Configurar radiobuttons
        grupobtn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rbelegir.isChecked()) {
                    etnuevoproducto.setVisibility(View.GONE);
                    spinner.setVisibility(View.VISIBLE);
                    contenedor.setVisibility(View.VISIBLE);
                } else if (rbanadir.isChecked()) {
                    etnuevoproducto.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.GONE);
                    contenedor.setVisibility(View.VISIBLE);
                }
            }
        });

        //Método cargar productos en el Spinner
        chargedProductsSpinner();

        //Configurar el botón de cancelar
        constructor.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Cierra el DIALOGO y vuelve a ProductsActivity
            }
        });

        // Configura el botón Añadir
        constructor.setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (rbanadir.isChecked()) {
                    //Obtener el valor del Editext
                    String nuevoProducto = etnuevoproducto.getText().toString();

                    //Verificar si está vacío
                    if (nuevoProducto.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Escriba un producto", Toast.LENGTH_SHORT).show();
                    } else {
                        db.newProducto(nuevoProducto, idUser);

                        int idNuevoProducto = obtenerIdProductoPorNombre(nuevoProducto);
                        boolean existeProductoNuevo = db.checkProductExistsLists(idListaIntent, idNuevoProducto);

                        if (existeProductoNuevo) {
                            Toast.makeText(getApplicationContext(), "Este producto ya está en tu lista", Toast.LENGTH_SHORT).show();

                        } else {
                            db.insertListaProducto(idListaIntent, idNuevoProducto, "N");

                            Toast.makeText(getApplicationContext(), "Producto añadido: " + nuevoProducto, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                if (rbelegir.isChecked()) {
                    // Obtener el valor seleccionado del Spinner
                    String productoSeleccionado = (String) spinner.getSelectedItem();

                    //Obtener el id del producto desde la bbdd
                    int idProductoSeleccionado = obtenerIdProductoPorNombre(productoSeleccionado);

                    //// Verificar si el producto ya existe en la lista
                    boolean existeProducto = db.checkProductExistsLists(idListaIntent, idProductoSeleccionado);

                    if (existeProducto) {
                        Toast.makeText(getApplicationContext(), "Este producto ya está en la lista", Toast.LENGTH_SHORT).show();
                    } else {
                        db.insertListaProducto(idListaIntent, idProductoSeleccionado, "N");
                        Toast.makeText(getApplicationContext(), "Producto añadido: " + productoSeleccionado, Toast.LENGTH_SHORT).show();
                    }
                }
                recreate();
            }
        });

        //Muestra el diálogo
        constructor.create().show();
    }

    private void chargedProductsSpinner() {

        // Crear el ArrayList de productos
        ArrayList<String> productos = new ArrayList<>();
        Cursor cursorP = db.getProductos(idUser);  // Llamada al método que devuelve el cursor con los productos

        if (cursorP != null && cursorP.moveToFirst()) {
            do {
                String nombreProducto = cursorP.getString(cursorP.getColumnIndexOrThrow("nombre"));
                productos.add(nombreProducto);
            } while (cursorP.moveToNext());
        }
        cursorP.close();

        // Configurar el adaptador para poblar el Spinner con los productos
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ProductsActivity.this, android.R.layout.simple_spinner_dropdown_item, productos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}