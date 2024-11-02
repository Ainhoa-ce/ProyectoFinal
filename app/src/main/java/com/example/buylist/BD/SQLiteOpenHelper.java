package com.example.buylist.BD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteOpenHelper extends android.database.sqlite.SQLiteOpenHelper {

    // Nombre de la base de datos y versión
    private static final String DATABASE_NAME = "app_database";
    private static final int DATABASE_VERSION = 1;

    // Tabla de usuarios
    public static final String TABLE_USERS = "usuario";
    public static final String COLUMN_USER_ID = ("id");
    public static final String COLUMN_USERNAME = "nombre_usuario";
    public static final String COLUMN_PASSWORD = "contraseña";
    public static final String COLUMN_EMAIL = "email";

    //Tabla de productos
    public static final String TABLE_PRODUCTS = "producto";
    public static final String COLUMN_PRODUCT_ID = ("id");
    public static final String COLUMN_PRODUCT_NAME = "nombre";
    public static final String COLUMN_PRODUCT_FK_ID_USER = ("fkIdUser");

    //Tabla listas
    public static final String TABLE_LISTS = "lista";
    public static final String COLUMN_LIST_ID = ("id");
    public static final String COLUMN_LIST_NAME = "nombre";
    public static final String COLUMN_LIST_FK_ID_USER = ("fkIdUser");

    //Tabla listaProducto
    public static final String TABLE_LISTS_PRODUCTS = "listaProducto";
    public static final String COLUMN_LISTPRODUCT_ID_LISTA = "listaID";
    public static final String COLUMN_LISTPRODUCT_ID_PRODUCTO = "productID";

    public SQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Crear tabla de usuario
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " ( "
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_EMAIL + " TEXT )";
        db.execSQL(CREATE_USERS_TABLE);

        //Crear tabla de productos
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + " ( "
                + COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PRODUCT_NAME + " TEXT, "
                + COLUMN_PRODUCT_FK_ID_USER + " INTEGER DEFAULT NULL )"; // Agregado 'NULL' y espacio
        db.execSQL(CREATE_PRODUCTS_TABLE);

        //Crear tabla de listas
        String CREATE_LISTS_TABLE = "CREATE TABLE " + TABLE_LISTS + " ( "
                + COLUMN_LIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_LIST_NAME + " TEXT, "
                + COLUMN_LIST_FK_ID_USER + " INTEGER )"; // Sin especificar NULL
        db.execSQL(CREATE_LISTS_TABLE);

        //Crear tabla lista-producto
        String CREATE_LISTS_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_LISTS_PRODUCTS + " ( "
                + COLUMN_LISTPRODUCT_ID_LISTA + " INTEGER, "
                + COLUMN_LISTPRODUCT_ID_PRODUCTO + " INTEGER, "
                + "PRIMARY KEY (" + COLUMN_LISTPRODUCT_ID_LISTA + ", " + COLUMN_LISTPRODUCT_ID_PRODUCTO + ")"
                + " );";
        db.execSQL(CREATE_LISTS_PRODUCTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eliminar tablas si existen
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTS_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        // Crear nuevamente las tablas
        onCreate(db);
    }

    //Métodos CRUD para la tabla usuario
    public void agregarUsuario(String nombre_usuario, String pass, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, nombre_usuario);
        values.put(COLUMN_PASSWORD, pass);
        values.put(COLUMN_EMAIL, email);
        db.insert(TABLE_USERS, null, values);
        db.close(); // Cerrar la conexión a la base de datos

    }

    // Método para verificar credenciales del usuario
    public boolean verificarCredenciales(String nombre_usuario, String pass) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?";

        // Ejecutar la consulta con los parámetros proporcionados
        Cursor cursor = db.rawQuery(query, new String[]{nombre_usuario, pass});

        // Verificar si se encontró un registro con las credenciales
        boolean existe = cursor.moveToFirst();

        cursor.close();
        db.close();
        return existe;
    }

    // Método para verificar si el email ya está registrado
    public boolean checkUser(String pNombreUsuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ?", new String[]{pNombreUsuario});

        if (cursor.getCount() > 0) {
            return true; // El usuario ya existe
        }
        return false;
    }

    public Cursor getUsuarios() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
    }

    public void cerrarSesion(String nombre_usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("logged", 0); // Establecer estado de sesión cerrada
        // Actualizar el estado de sesión donde el nombre de usuario coincida
        db.update(TABLE_USERS, values, COLUMN_USERNAME + " = ?", new String[]{nombre_usuario});
        db.close();
    }


    //Métodos CRUD para la tabla productos

    public Cursor getProductos(int pIdUser) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCT_FK_ID_USER + "=? OR " + COLUMN_PRODUCT_FK_ID_USER +" IS NULL";

        // Ejecutar la consulta con los parámetros proporcionados
        return db.rawQuery(query, new String[]{String.valueOf(pIdUser)});
    }

    public long newProducto(String nombreProducto, int pIdUser) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, nombreProducto);
        values.put(COLUMN_PRODUCT_FK_ID_USER, pIdUser);
        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
        return 0;
    }

    public long newProductoPredeterm(String nombreProducto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, nombreProducto);
        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
        return 0;
    }

    //Métodos CRUD para la tabla listas
    public Cursor getListas(int pIdUser) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_LISTS + " WHERE " + COLUMN_LIST_FK_ID_USER + " = " + pIdUser, null);
    }

    public void newLista(String nombreLista, int pIdUser) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LIST_NAME, nombreLista);
        values.put(COLUMN_LIST_FK_ID_USER, pIdUser);
        db.insert(TABLE_LISTS, null, values);
        db.close();
    }

    //Método para eliminar un ítem de la lista con su ID con sus respectivos productos asociados, buena práctica eliminar primero los productos relacionados antes que el principal
    public void deleteItemLista(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LISTS_PRODUCTS, COLUMN_LISTPRODUCT_ID_LISTA + " = ?", new String[]{String.valueOf(id)});
        db.delete(TABLE_LISTS, COLUMN_LIST_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    //Método para insertar (relacionar) productos con una lista en la tabla lista-productos
    public void insertListaProducto(int idLista, int idProducto, String check) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LISTPRODUCT_ID_LISTA, idLista);
        cv.put(COLUMN_LISTPRODUCT_ID_PRODUCTO, idProducto);
        db.insert(TABLE_LISTS_PRODUCTS, null, cv);
        db.close();
    }

    //Método para borrar los productos de la lista en producto-list
    public void deleteListaProducto(int idLista, int idProducto) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Definir cláusulas Where para el método delete( String table, String whereClause, String[] whereArgs)
        String whereClause = COLUMN_LISTPRODUCT_ID_LISTA + " = ? AND " + COLUMN_LISTPRODUCT_ID_PRODUCTO + " = ?"; //La condición para la eliminación. En este caso, estamos especificando que queremos eliminar la fila donde coincidan tanto el idLista como el idProducto
        String[] whereArgs = {String.valueOf(idLista), String.valueOf(idProducto)}; //Los valores que serán usados en los ? de la cláusula whereClause. En este caso, pasamos el idLista y idProducto como argumentos en forma de String.

        db.delete(TABLE_LISTS_PRODUCTS, whereClause, whereArgs);

        db.close();
    }

    //Método para obtener todos los productos de una lista
    public Cursor getProductosLists(int listId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] Lista = {String.valueOf(listId)};
        return db.rawQuery("SELECT p." + COLUMN_PRODUCT_ID + ", p." + COLUMN_PRODUCT_NAME +
                " FROM " + TABLE_LISTS_PRODUCTS + " lp " +
                "INNER JOIN " + TABLE_PRODUCTS + " p " + " ON p." + COLUMN_PRODUCT_ID + "= lp." + COLUMN_LISTPRODUCT_ID_PRODUCTO +
                " WHERE lp." + COLUMN_LISTPRODUCT_ID_LISTA + "= ?", Lista);
    }

    //Método para verificar que un producto ya existe en la lista
    public boolean checkProductExistsLists(int listaID, int ProductID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_LISTS_PRODUCTS + " WHERE " + COLUMN_LISTPRODUCT_ID_LISTA + " = ? AND " + COLUMN_LISTPRODUCT_ID_PRODUCTO + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(listaID), String.valueOf(ProductID)});

        boolean exists = cursor.getCount() > 0;  // Si el cursor devuelve al menos 1 fila, el producto ya existe en la lista
        cursor.close();
        return exists;
    }

    private void chargeProductPredeterm() {
        newProductoPredeterm("Queso");
        newProductoPredeterm("Macarrones");
        newProductoPredeterm("Merluza");
        newProductoPredeterm("Tomate");
        newProductoPredeterm("Leche");
        newProductoPredeterm("Pimiento");
        newProductoPredeterm("Espinacas");
        newProductoPredeterm("Aguacate");
        newProductoPredeterm("Naranja");
        newProductoPredeterm("Pollo");
        newProductoPredeterm("Cerdo");
        newProductoPredeterm("Ajo");
        newProductoPredeterm("Cebolla");
        newProductoPredeterm("Patatas");
        newProductoPredeterm("Aceite");
        newProductoPredeterm("Calabacín");
        newProductoPredeterm("Sandía");
        newProductoPredeterm("Atún");
        newProductoPredeterm("Arroz");
        newProductoPredeterm("Azúcar");
    }

    public void inicializarProductosPredeterminados() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Verificar si ya se han insertado los productos predeterminados
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_PRODUCTS, null);
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            if (count == 0) {  // Solo si la tabla está vacía, se cargan los productos
                chargeProductPredeterm();
            }
        }
        cursor.close();
        db.close();
    }

}
