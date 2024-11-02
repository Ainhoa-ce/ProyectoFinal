package com.example.buylist.Model;

public class ListaModel {

    private int id;
    private String nombre;
    private int FK_idUser;

    //Constructor
    public ListaModel(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
//        this.FK_idUser = FK_idUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getFK_idUser() {
        return FK_idUser;
    }

    public void setFK_idUser(int FK_idUser) {
        this.FK_idUser = FK_idUser;
    }
}


