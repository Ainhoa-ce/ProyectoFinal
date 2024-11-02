package com.example.buylist;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buylist.BD.SQLiteOpenHelper;


public class RegisterActivity extends AppCompatActivity {

    EditText etusuario_register, etemail_register,etclave_register;
    SQLiteOpenHelper db;
    Button btnregister, btnvolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //iniciar bd

        db = new SQLiteOpenHelper(this);

        //Inicializar las vistas

        etusuario_register=(EditText) findViewById(R.id.etusuario_register);
        etemail_register=(EditText) findViewById(R.id.etemail_register);
        etclave_register=(EditText) findViewById(R.id.etclave_register);
        btnregister=(Button) findViewById(R.id.btnregister);
        btnvolver=(Button)findViewById(R.id.btnvolver);

        //Configurar el botón de registro
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = etusuario_register.getText().toString();
                String email = etemail_register.getText().toString();
                String password = etclave_register.getText().toString();

                if (usuario.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    //Verificar si el nombre de usuario ya está en uso
                    if(db.checkUser(usuario)){
                        System.out.println("usuario " + usuario);
                        Toast.makeText(RegisterActivity.this, "El nombre de usuario ya está en uso", Toast.LENGTH_SHORT).show();
                    } else {
                        // Aquí podrías manejar el registro, como guardar en una base de datos o hacer una autenticación externa
                        System.out.println("NO ENTRA " + usuario);

                        db.agregarUsuario(usuario, password,email);
                        Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        // Finaliza la actividad y regresa al Login
                        finish();
                    }

                }
            }
        });

        btnvolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cierra RegisterActivity y vuelve a LoginActivity
            }
        });

      //  return new String[0];
    }
}