package model;

import java.time.LocalDate;
import javax.swing.JOptionPane;

public class ClienteMembresia extends Cliente {
    private Membresia membresia;
    private LocalDate fechaExpiracion;

    public ClienteMembresia(String nombre, String cedula, String telefono, String correo,
                            Membresia membresia, LocalDate fechaExpiracion) {
        super(nombre, cedula, telefono, correo);
        this.membresia = membresia;
        this.fechaExpiracion = fechaExpiracion;
    }

    public Membresia getMembresia() {
        return membresia;
    }

    public void setMembresia(Membresia membresia) {
        this.membresia = membresia;
    }

    public LocalDate getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDate fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    @Override
    public boolean tieneMembresiaActiva() {
        return membresia != null && membresia.verificarVigencia();
    }

    public void mostrarInfoCliente() {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Nombre: ").append(getNombre()).append("\n");
        mensaje.append("Cédula: ").append(getCedula()).append("\n");
        mensaje.append("Teléfono: ").append(getTelefono()).append("\n");
        mensaje.append("Correo: ").append(getCorreo()).append("\n");
        mensaje.append("Tipo de Membresía: ").append(membresia.getTipo()).append("\n");
        mensaje.append("Fecha de expiración: ").append(fechaExpiracion).append("\n");
        JOptionPane.showMessageDialog(null, mensaje.toString(), "Información del Cliente con Membresía", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public String toString() {
        return super.toString() + ", Membresía: " + membresia.getTipo() + ", Expira: " + fechaExpiracion;
    }
}
