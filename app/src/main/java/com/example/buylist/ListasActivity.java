package com.example.buylist;

import static com.example.buylist.utils.Utils.deleteMemoryUser;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.buylist.BD.SQLiteOpenHelper;
import com.example.buylist.Constants.Constants;
import com.example.buylist.Model.ListaModel;
import com.example.buylist.utils.Utils;

import java.util.ArrayList;


public class ListasActivity extends AppCompatActivity {
    SQLiteOpenHelper db;
    Button btncrear, btncerrarsesion;
    int idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listas);

        //checkear usuario
        boolean isLooged = Utils.isLoogedUser(getApplicationContext());
        if (!isLooged) {
            Intent intent = new Intent(ListasActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        int idUser = Utils.getMemoryInt(getApplicationContext(), Constants.KEY_MEMORY_ID_USER);
        this.idUser = idUser;

        //iniciar bd

        db = new SQLiteOpenHelper(this);

        //Inicializar las vistas del botón crear
        btncrear = (Button) findViewById(R.id.btncrear);

        ArrayList<ListaModel> listRellena = new ArrayList<>();
        Cursor cursorBD = db.getListas(idUser);


        if (cursorBD != null && cursorBD.moveToFirst()) {
            // Recorrer el cursor
            do {
                // Obtener datos de cada columna
                int idItem = cursorBD.getInt(cursorBD.getColumnIndexOrThrow("id"));
                String nombreItem = cursorBD.getString(cursorBD.getColumnIndexOrThrow("nombre"));

                // Crear un nuevo objeto ItemList y añadirlo a la lista
                ListaModel item = new ListaModel(idItem, nombreItem);
                listRellena.add(item);

            } while (cursorBD.moveToNext()); // Avanzar al siguiente registro

            if (cursorBD != null) {
                cursorBD.close();
            }
        }

        ListView listaDatos = ((ListView) findViewById(R.id.lv_lista));
        MiArrayAdapterList<ListaModel> adaptador = new MiArrayAdapterList<ListaModel>(ListasActivity.this, R.layout.item_list, listRellena); //Con R.layout.item_list, le estoy diciendo la distribucion que tiene que seguir cada item
        listaDatos.setAdapter(adaptador);

        //Configurar el boton de crear nueva lista

        btncrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder constructor = new AlertDialog.Builder(ListasActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.activity_crear_lista, null);

                // Infla el layout del diálogo y lo establece en el constructor
                constructor.setView(dialogView);

                // Obtén referencias a los campos del diálogo
                final EditText ednuevaLista = dialogView.findViewById(R.id.ednuevaLista);

                //Configurar el botón de cancelar
                constructor.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Cierra el DIALOGO y vuelve a ListasActivity
                    }
                });

                // Configura el botón crear
                constructor.setPositiveButton("Crear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Obtengo los valores del EditText
                        String nombreLista = ednuevaLista.getText().toString();

                        //Verificar que el edittext está lleno
                        if (nombreLista.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                            //No se cierra el diálogo aquí
                        } else {
                            db.newLista(nombreLista, idUser);

                        }
                        recreate();
                    }
                });

                //Muestra el diálogo
                constructor.create().show();

            }

        });

        //Inicializar las vistas del botón de cerrar sesion
        btncerrarsesion = findViewById(R.id.btncerrarsesion);

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
}