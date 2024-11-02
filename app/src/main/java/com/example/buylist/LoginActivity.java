package com.example.buylist;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buylist.BD.SQLiteOpenHelper;
import com.example.buylist.Constants.Constants;
import com.example.buylist.utils.Utils;

import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity {

    EditText usuario, clave;
    TextView registrar;
    Button btningresar;
    SQLiteOpenHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //iniciar bd

        db = new SQLiteOpenHelper(this);
        db.inicializarProductosPredeterminados();

        usuario=(EditText) findViewById(R.id.etusuario);
        clave=(EditText) findViewById(R.id.etclave);
        registrar=(TextView) findViewById(R.id.registrar);
        btningresar=(Button) findViewById(R.id.btningresar);

        btningresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarUsuarioconContrasena();
            }
        });

        registrar.setOnClickListener(new View.OnClickListener(){
            public void onClick (View v){
                // Navegar a la actividad de registro
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Crear el ArrayList de usuarios
        ArrayList<String> usuarios = new ArrayList<>();
        Cursor cursorU = db.getUsuarios();  // Llamada al método que devuelve el cursor con los usuarios

        if (cursorU != null && cursorU.moveToFirst()) {
            do {
                String nombreUsuario = cursorU.getString(cursorU.getColumnIndexOrThrow("nombre_usuario"));
                usuarios.add(nombreUsuario);
            } while (cursorU.moveToNext());
        }
        cursorU.close();
    }

    //método para verificar usuario con contraseña

    private void mostrarUsuarioconContrasena(){
        String user = usuario.getText().toString();
        String contrasena = clave.getText().toString();

        if (user.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            // Lógica de autenticación (por ejemplo, usando Firebase o base de datos)
            // Verificar las credenciales
            boolean autenticado = db.verificarCredenciales(user, contrasena);
            if(autenticado){
                Toast.makeText(LoginActivity.this,"Inicio de sesión correcto", Toast.LENGTH_SHORT).show();

                // consultar en la bd el id de usuario
                int iduser = obtenerIdUsuarioPorNombre(user);

                //guardar id de usuario en Utils.setMemoryInt(getApplicationContext(), Constants.KEY_MEMORY_ID_USER, idUsuario);
                Utils.setMemoryInt(getApplicationContext(), Constants.KEY_MEMORY_ID_USER, iduser);
                // Navegar a la siguiente pantalla
                Intent intent = new Intent(LoginActivity.this, ListasActivity.class);
                startActivity(intent);
                finish(); // Finaliza la actividad de login
            }else{
                Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrecto", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Método para obtener el ID del usuario pasando su nombre
    private int obtenerIdUsuarioPorNombre(String nombreUsuario) {
        int idUsuario = -1;
        Cursor cursorU = db.getUsuarios();
        if (cursorU != null && cursorU.moveToFirst()) {
            do {
                String nombreU = cursorU.getString(cursorU.getColumnIndexOrThrow("nombre_usuario"));
                if (nombreU.equals(nombreUsuario)) {
                    idUsuario = cursorU.getInt(cursorU.getColumnIndexOrThrow("id"));
                    break;
                }
            } while (cursorU.moveToNext());
        }
        cursorU.close();
        return idUsuario;
    }


}