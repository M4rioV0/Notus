package com.example.proyectofinciclo.database;

public class Utilidades {

    public static final String tablaNotas="notas";
    public static final String campoTitulo="titulo";
    public static final String campoContenido="usuario";
    public static final String campoId = "id";


    public static final String crearTablaNotas = "CREATE TABLE "+tablaNotas+" ("+campoId+" INTEGER PRIMARY KEY AUTOINCREMENT,"+campoTitulo+" TEXT, "+campoContenido+" TEXT)";


}
