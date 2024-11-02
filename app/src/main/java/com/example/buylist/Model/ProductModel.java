package com.example.buylist.Model;

public class ProductModel {

    private int id;
    private String nombre;

    //Constructor
    public ProductModel(int id, String nombre){
        this.id = id;
        this.nombre = nombre;
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getNombre(){
        return nombre;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }

}
