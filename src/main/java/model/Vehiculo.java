package model;

public class Vehiculo {
    public String placa;
    public String color;
    public String modelo;
    private TipoMembresia membresia;
    private String fechaFinMembresia;
    private String fechaInicioMembresia;

    public Vehiculo(String placa, String color, String modelo) {
        this.placa = placa;
        this.color = color;
        this.modelo = modelo;

    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public TipoMembresia getMembresia() {
        return membresia;
    }

    public void setMembresia(TipoMembresia membresia) {
        this.membresia = membresia;
    }

    public String getFechaFinMembresia() {
        return fechaFinMembresia;
    }

    public void setFechaFinMembresia(String fechaFinMembresia) {
        this.fechaFinMembresia = fechaFinMembresia;
    }

    public String getFechaInicioMembresia() {
        return fechaInicioMembresia;
    }

    public void setFechaInicioMembresia(String fechaInicoMembresia) {
        this.fechaInicioMembresia = fechaInicoMembresia;
    }
}