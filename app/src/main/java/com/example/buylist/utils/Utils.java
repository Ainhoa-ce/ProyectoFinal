package com.example.buylist.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.buylist.Constants.Constants;

public class Utils {

    // Nombre del archivo SharedPreferences
    private static final String PREFS_NAME = "MyAppPreferences";

    // Método para guardar un valor en SharedPreferences
    public static void setMemoryString(Context context, String clave, String valor) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(clave, valor);
        editor.apply();  // Asegura que los cambios se guarden
    }

    // Método para obtener un valor de SharedPreferences
    public static String getMemoryString(Context context, String clave) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString(clave, "");
    }

    // Método adicional para guardar un entero (si se necesita)
    public static void setMemoryInt(Context context, String clave, int valor) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(clave, valor);
        editor.apply();
    }

    // Método adicional para obtener un entero (si se necesita)
    public static int getMemoryInt(Context context, String clave) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPref.getInt(clave, -1);
    }

    public static void deleteMemoryUser(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(Constants.KEY_MEMORY_ID_USER); // Eliminar la clave
        editor.apply(); // Aplicar los cambios
    }

    // Método para chequear si el usuario está logeado
    public static boolean isLoogedUser(Context context) {
        int idUser = getMemoryInt(context, Constants.KEY_MEMORY_ID_USER);
        return idUser != -1;
    }


}


/*
Ejemplo para usarlo

// Guardar un int
Utils.setMemoryInt(getApplicationContext(), Constants.KEY_MEMORY_ID_USER, 2);
Utils.setMemoryInt(getApplicationContext(), "id_user", 2);

// Obtener un int
int userAge = Utils.getMemoryInt(getApplicationContext(), Constants.KEY_MEMORY_ID_USER); // si no hay datos devuelve un -1
Utils.getMemoryInt(getApplicationContext(), "id_user");
*
*/