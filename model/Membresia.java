package model;

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
}