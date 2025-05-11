package model;

public class Pago {
    private String idPago; // Identificador único del pago
    private Cliente cliente; // Cliente asociado al pago
    private Vehiculo vehiculo; // Vehículo asociado al pago
    private double monto; // Monto total a pagar
    private String fechaPago; // Fecha del pago
    private String metodoPago; // Método de pago (Efectivo, Tarjeta, etc.)
    private String estadoPago; // Estado del pago (Pendiente, Completado, Cancelado)
    private String descripcion; // Descripción del pago

    // Constructor
    public Pago(String idPago, Cliente cliente, Vehiculo vehiculo, double monto, String fechaPago,
            String metodoPago, String estadoPago, String descripcion) {
        this.idPago = idPago;
        this.cliente = cliente;
        this.vehiculo = vehiculo;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.metodoPago = metodoPago;
        this.estadoPago = estadoPago;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public String getIdPago() {
        return idPago;
    }

    public void setIdPago(String idPago) {
        this.idPago = idPago;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}