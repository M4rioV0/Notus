package com.example.proyectofinciclo.models;

public class NotasModel {

    private int id;
    private  String titulo;
    private  String contenido;
    private  String imagen;

    public NotasModel() {
    }

    public NotasModel(String titulo, String contenido, int id, String imagen) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.id = id;
        this.imagen = imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}

