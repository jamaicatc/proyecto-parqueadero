package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Membresia {

    private String fechaInicio;
    private String fechaFin;
    private int tarifa;
    private TipoMembresia tipo; // Nuevo atributo

    public Membresia() {
    }

    public Membresia(String fechaInicio, String fechaFin, int tarifa, TipoMembresia tipo) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tarifa = tarifa;
        this.tipo = tipo;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getTarifa() {
        return tarifa;
    }

    public void setTarifa(int tarifa) {
        this.tarifa = tarifa;
    }

    public TipoMembresia getTipo() {
        return tipo;
    }

    public void setTipo(TipoMembresia tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Membresía:\n" +
                "Tipo: " + tipo + "\n" +
                "Fecha Inicio: " + fechaInicio + "\n" +
                "Fecha Fin: " + fechaFin + "\n" +
                "Tarifa: $" + tarifa;
    }

        public boolean verificarVigencia() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate hoy = LocalDate.now();
        try {
            LocalDate inicio = LocalDate.parse(fechaInicio, formatter);
            LocalDate fin = LocalDate.parse(fechaFin, formatter);
            return (hoy.isEqual(inicio) || hoy.isAfter(inicio)) && (hoy.isBefore(fin) || hoy.isEqual(fin));
        } catch (Exception e) {
            return false;
        }
    }
}