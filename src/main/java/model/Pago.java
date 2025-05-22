package model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Pago {
    private String id;
    private double monto;
    private LocalDateTime fechaHora;
    private String concepto;  // "Estacionamiento" o "Membresía"
    private Vehiculo vehiculo;
    private Cliente cliente;
    private String tipoVehiculo;  // "Automóvil", "Moto", "Camión"

    /**
     * Constructor para pagos de estacionamiento
     */
    public Pago(double monto, Vehiculo vehiculo, String tipoVehiculo) {
        this.id = UUID.randomUUID().toString().substring(0, 8);  // ID único
        this.monto = monto;
        this.fechaHora = LocalDateTime.now();
        this.concepto = "Estacionamiento";
        this.vehiculo = vehiculo;
        this.tipoVehiculo = tipoVehiculo;
    }

    /**
     * Constructor para pagos de membresía
     */
    public Pago(double monto, Vehiculo vehiculo, Cliente cliente, TipoMembresia tipoMembresia) {
        this.id = UUID.randomUUID().toString().substring(0, 8);  // ID único
        this.monto = monto;
        this.fechaHora = LocalDateTime.now();
        this.concepto = "Membresía " + tipoMembresia.toString();
        this.vehiculo = vehiculo;
        this.cliente = cliente;
        this.tipoVehiculo = determinarTipoVehiculo(vehiculo);
    }

    private String determinarTipoVehiculo(Vehiculo vehiculo) {
        if (vehiculo instanceof Automovil) {
            return "Automóvil";
        } else if (vehiculo instanceof Moto) {
            return "Moto";
        } else if (vehiculo instanceof Camion) {
            return "Camión";
        }
        return "Desconocido";
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public double getMonto() {
        return monto;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getConcepto() {
        return concepto;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    @Override
    public String toString() {
        return "Pago{" +
                "id='" + id + '\'' +
                ", monto=" + monto +
                ", fechaHora=" + fechaHora +
                ", concepto='" + concepto + '\'' +
                ", placa='" + (vehiculo != null ? vehiculo.getPlaca() : "N/A") + '\'' +
                ", tipo='" + tipoVehiculo + '\'' +
                '}';
    }
}