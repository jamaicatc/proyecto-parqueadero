package service;

import model.Cliente;
import model.Vehiculo;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ClienteService {
    private List<Cliente> listaDeClientes;

    public ClienteService() {
        this.listaDeClientes = new ArrayList<>();
    }

    // Método para añadir un cliente
    public void añadirCliente() {
        JTextField nombreField = new JTextField();
        JTextField cedulaField = new JTextField();
        JTextField telefonoField = new JTextField();
        JTextField correoField = new JTextField();

        Object[] campos = {
                "Nombre:", nombreField,
                "Cédula:", cedulaField,
                "Teléfono:", telefonoField,
                "Correo:", correoField
        };

        while (true) {
            int opcion = JOptionPane.showConfirmDialog(null, campos, "Añadir Cliente", JOptionPane.OK_CANCEL_OPTION);

            if (opcion == JOptionPane.OK_OPTION) {
                String nombre = nombreField.getText().trim();
                String cedula = cedulaField.getText().trim();
                String telefono = telefonoField.getText().trim();
                String correo = correoField.getText().trim();

                if (nombre.isEmpty() || cedula.isEmpty() || telefono.isEmpty() || correo.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor llene todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Validar duplicados por cédula
                    boolean cedulaExiste = listaDeClientes.stream().anyMatch(c -> c.getCedula().equalsIgnoreCase(cedula));

                    if (cedulaExiste) {
                        JOptionPane.showMessageDialog(null, "Ya existe un cliente con esa cédula.", "Duplicado", JOptionPane.WARNING_MESSAGE);
                    } else {
                        Cliente nuevoCliente = new Cliente(nombre, cedula, telefono, correo);
                        listaDeClientes.add(nuevoCliente);
                        JOptionPane.showMessageDialog(null, "Cliente añadido exitosamente.");
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Ingreso cancelado.");
                break;
            }
        }
    }

    public Cliente buscarCliente() {
        String[] opcionesBusqueda = {"Nombre", "Cédula", "Teléfono"};
        int opcion = JOptionPane.showOptionDialog(
                null,
                "Seleccione el criterio de búsqueda:",
                "Buscar Cliente",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcionesBusqueda,
                opcionesBusqueda[0]
        );
    
        if (opcion == JOptionPane.CLOSED_OPTION) {
            JOptionPane.showMessageDialog(null, "Búsqueda cancelada.");
            return null;
        }
    
        String criterio = JOptionPane.showInputDialog("Ingrese el " + opcionesBusqueda[opcion] + " del cliente:");
        if (criterio == null || criterio.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El criterio de búsqueda no puede estar vacío.");
            return null;
        }
    
        // Buscar según el criterio seleccionado
        Cliente clienteEncontrado = null;
        switch (opcion) {
            case 0 -> clienteEncontrado = listaDeClientes.stream()
                    .filter(c -> c.getNombre().equalsIgnoreCase(criterio))
                    .findFirst()
                    .orElse(null);
            case 1 -> clienteEncontrado = listaDeClientes.stream()
                    .filter(c -> c.getCedula().equalsIgnoreCase(criterio))
                    .findFirst()
                    .orElse(null);
            case 2 -> clienteEncontrado = listaDeClientes.stream()
                    .filter(c -> c.getTelefono().equalsIgnoreCase(criterio))
                    .findFirst()
                    .orElse(null);
        }
    
        if (clienteEncontrado != null) {
            // Formatear los datos del cliente con espacios después de los dos puntos
            String mensaje = "Cliente encontrado:\n" +
                    "Nombre: " + clienteEncontrado.getNombre() + "\n" +
                    "Cédula: " + clienteEncontrado.getCedula() + "\n" +
                    "Teléfono: " + clienteEncontrado.getTelefono() + "\n" +
                    "Correo: " + clienteEncontrado.getCorreo();
    
            JOptionPane.showMessageDialog(null, mensaje);
            return clienteEncontrado;
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró ningún cliente con el " + opcionesBusqueda[opcion] + " proporcionado.");
            return null;
        }
    }

    // Método para actualizar un cliente
    public void actualizarCliente() {
        Cliente cliente = buscarCliente();
        if (cliente == null) {
            return;
        }

        String[] opciones = {"Cambiar Nombre", "Cambiar Teléfono", "Cambiar Correo", "Salir"};
        boolean continuar = true;

        while (continuar) {
            int opcion = JOptionPane.showOptionDialog(null, "Seleccione el dato que desea actualizar:\n\n" + cliente,
                    "Actualizar Cliente", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

            switch (opcion) {
                case 0 -> cliente.setNombre(JOptionPane.showInputDialog("Ingrese el nuevo nombre:", cliente.getNombre()));
                case 1 -> cliente.setTelefono(JOptionPane.showInputDialog("Ingrese el nuevo teléfono:", cliente.getTelefono()));
                case 2 -> cliente.setCorreo(JOptionPane.showInputDialog("Ingrese el nuevo correo:", cliente.getCorreo()));
                case 3, JOptionPane.CLOSED_OPTION -> continuar = false;
                default -> JOptionPane.showMessageDialog(null, "Opción inválida.");
            }
        }
    }

    // Método para eliminar un cliente
    public void eliminarCliente() {
        Cliente cliente = buscarCliente();
        if (cliente == null) {
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar este cliente?\n\n" + cliente,
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            listaDeClientes.remove(cliente);
            JOptionPane.showMessageDialog(null, "Cliente eliminado exitosamente.");
        } else {
            JOptionPane.showMessageDialog(null, "Eliminación cancelada.");
        }
    }
    
    /**
     * Muestra todos los vehículos asociados a un cliente y si tienen cobertura activa
     * Este método busca un cliente y muestra información detallada de sus vehículos
     * incluyendo si la membresía está vigente o ha vencido
     */
    public void mostrarVehiculosCliente() {
        // Primero buscamos al cliente
        Cliente cliente = buscarCliente();
        if (cliente == null) {
            return; // Si no se encuentra el cliente, termina el método
        }
        
        List<Vehiculo> vehiculos = cliente.getVehiculos();
        
        // Verificamos si el cliente tiene vehículos
        if (vehiculos == null || vehiculos.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                    "El cliente " + cliente.getNombre() + " no tiene vehículos registrados.",
                    "Sin vehículos", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Construimos el mensaje con la información de los vehículos
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Vehículos de ").append(cliente.getNombre()).append(" (").append(cliente.getCedula()).append("):\n\n");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaActual = LocalDate.now();
        
        // Iteramos sobre cada vehículo para mostrar su información y estado de cobertura
        for (Vehiculo vehiculo : vehiculos) {
            mensaje.append("Placa: ").append(vehiculo.getPlaca()).append("\n");
            mensaje.append("Modelo: ").append(vehiculo.getModelo()).append("\n");
            mensaje.append("Color: ").append(vehiculo.getColor()).append("\n");
            
            // Verificamos si el vehículo tiene membresía
            if (vehiculo.getMembresia() != null && vehiculo.getFechaFinMembresia() != null) {
                try {
                    LocalDate fechaFin = LocalDate.parse(vehiculo.getFechaFinMembresia(), formatter);
                    
                    if (fechaActual.isBefore(fechaFin) || fechaActual.isEqual(fechaFin)) {
                        mensaje.append("Estado: CON COBERTURA ACTIVA (hasta ").append(vehiculo.getFechaFinMembresia()).append(")\n");
                    } else {
                        mensaje.append("Estado: MEMBRESÍA VENCIDA (venció el ").append(vehiculo.getFechaFinMembresia()).append(")\n");
                    }
                } catch (Exception e) {
                    mensaje.append("Estado: INFORMACIÓN DE MEMBRESÍA INCORRECTA\n");
                }
            } else {
                mensaje.append("Estado: SIN MEMBRESÍA\n");
            }
            
            mensaje.append("\n"); // Separador entre vehículos
        }
        
        // Mostramos la información en un diálogo
        JOptionPane.showMessageDialog(null, mensaje.toString(), 
                "Vehículos del Cliente", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Obtiene todos los clientes registrados en el sistema
     * @return Lista con todos los clientes
     */
    public List<Cliente> obtenerTodosLosClientes() {
        if (listaDeClientes == null || listaDeClientes.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "No hay clientes registrados en el sistema",
                    "Sin datos", JOptionPane.INFORMATION_MESSAGE);
            return new ArrayList<>();
        }
        return new ArrayList<>(listaDeClientes);
    }
}