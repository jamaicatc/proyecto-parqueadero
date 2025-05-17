package model;

import java.util.List;
import java.util.ArrayList;
import interfaces.IMembresiaActiva;

public abstract class Cliente implements IMembresiaActiva {
    private String nombre;
    private String cedula;
    private String telefono;
    private String correo;
    private List<Membresia> membresias;


    public Cliente(String nombre, String cedula, String telefono, String correo) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.telefono = telefono;
        this.correo = correo;
        this.membresias = new ArrayList<>(); 
    }

    public List<Membresia> getMembresias() {
        return membresias;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String toString() {
        return "nombre: " + nombre + "\ncedula:" + cedula + "\ntelefono:" + telefono + "\ncorreo:" + correo;
    }
}
