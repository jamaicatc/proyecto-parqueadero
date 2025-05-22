package model;

import javax.swing.JOptionPane;

public class ClienteEventual extends Cliente{

    public ClienteEventual(String nombre, String cedula, String telefono, String correo) {
        super(nombre, cedula, telefono, correo);
    }

    @Override
    public boolean tieneMembresiaActiva() {
        return false; 
    }

    public static ClienteEventual crearClienteEventualDesdeFormulario() {
        String nombre = JOptionPane.showInputDialog("Ingrese el nombre del cliente eventual:");
        String cedula = JOptionPane.showInputDialog("Ingrese la cédula:");
        String telefono = JOptionPane.showInputDialog("Ingrese el número de teléfono:");
        String correo = JOptionPane.showInputDialog("Ingrese el correo electrónico:");

        ClienteEventual nuevoCliente = new ClienteEventual(nombre, cedula, telefono, correo);
        JOptionPane.showMessageDialog(null, "Cliente eventual creado:\n" + nuevoCliente);

        return nuevoCliente;
    }

    public void mostrarResumenCliente() {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Cliente Eventual\n");
        mensaje.append("Nombre: ").append(getNombre()).append("\n");
        mensaje.append("Cédula: ").append(getCedula()).append("\n");
        mensaje.append("Teléfono: ").append(getTelefono()).append("\n");
        mensaje.append("Correo: ").append(getCorreo()).append("\n");
        mensaje.append("Tipo: Cliente eventual (sin membresía)\n");

        JOptionPane.showMessageDialog(null, mensaje.toString());
    }

    @Override
    public String toString() {
        return super.toString() + "\nTipo de Membresía: NINGUNA";
    }
}
