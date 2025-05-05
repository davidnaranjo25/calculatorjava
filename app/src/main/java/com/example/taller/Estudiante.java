package com.example.taller;

public class Estudiante {
    public String cedula, nombre, telefono, correo, genero, carrera, universidad;
    public int semestre, creditos;

    // Constructor vacío
    public Estudiante() {
    }

    // Constructor con parámetros
    public Estudiante(String cedula, String nombre, String telefono, String correo, String genero,
                      String carrera, int semestre, String universidad, int creditos) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.genero = genero;
        this.carrera = carrera;
        this.semestre = semestre;
        this.universidad = universidad;
        this.creditos = creditos;
    }

    // Getters
    public String getCedula() { return cedula; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }
    public String getGenero() { return genero; }
    public String getCarrera() { return carrera; }
    public int getSemestre() { return semestre; }
    public String getUniversidad() { return universidad; }
    public int getCreditos() { return creditos; }

    // Setters
    public void setCedula(String cedula) { this.cedula = cedula; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setGenero(String genero) { this.genero = genero; }
    public void setCarrera(String carrera) { this.carrera = carrera; }
    public void setSemestre(int semestre) { this.semestre = semestre; }
    public void setUniversidad(String universidad) { this.universidad = universidad; }
    public void setCreditos(int creditos) { this.creditos = creditos; }
}