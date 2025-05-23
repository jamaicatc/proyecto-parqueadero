package codeService;

import model.Cliente;
import model.Vehiculo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ClienteCodeService {
    private List<Cliente> listaDeClientes;

    public ClienteCodeService() {
        this.listaDeClientes = new ArrayList<>();
    }

    /**
     * Añade un cliente a la lista si no existe otro con la misma cédula
     * @param nombre Nombre del cliente
     * @param cedula Cédula del cliente
     * @param telefono Teléfono del cliente
     * @param correo Correo del cliente
     * @return true si se añadió correctamente, false si ya existe un cliente con esa cédula
     */
    public boolean añadirCliente(String nombre, String cedula, String telefono, String correo) {
        // Validar que todos los campos estén completos
        if (nombre == null || cedula == null || telefono == null || correo == null ||
                nombre.trim().isEmpty() || cedula.trim().isEmpty() ||
                telefono.trim().isEmpty() || correo.trim().isEmpty()) {
            return false;
        }

        // Validar duplicados por cédula
        boolean cedulaExiste = listaDeClientes.stream()
                .anyMatch(c -> c.getCedula().equalsIgnoreCase(cedula));

        if (cedulaExiste) {
            return false;
        }

        Cliente nuevoCliente = new Cliente(nombre, cedula, telefono, correo);
        listaDeClientes.add(nuevoCliente);
        return true;
    }

    /**
     * Busca un cliente según el criterio especificado
     * @param criterio Valor a buscar
     * @param tipoBusqueda Tipo de búsqueda: 0-Nombre, 1-Cédula, 2-Teléfono
     * @return Cliente encontrado o null si no existe
     */
    public Cliente buscarCliente(String criterio, int tipoBusqueda) {
        if (criterio == null || criterio.trim().isEmpty()) {
            return null;
        }

        // Buscar según el criterio seleccionado
        Cliente clienteEncontrado = null;
        switch (tipoBusqueda) {
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
            default -> clienteEncontrado = null;
        }

        return clienteEncontrado;
    }

    /**
     * Actualiza la información de un cliente
     * @param cliente Cliente a actualizar
     * @param campo Campo a actualizar: 0-Nombre, 1-Teléfono, 2-Correo
     * @param nuevoValor Nuevo valor para el campo
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean actualizarCliente(Cliente cliente, int campo, String nuevoValor) {
        if (cliente == null || nuevoValor == null || nuevoValor.trim().isEmpty()) {
            return false;
        }

        switch (campo) {
            case 0 -> cliente.setNombre(nuevoValor);
            case 1 -> cliente.setTelefono(nuevoValor);
            case 2 -> cliente.setCorreo(nuevoValor);
            default -> {
                return false;
            }
        }

        return true;
    }

    /**
     * Elimina un cliente de la lista
     * @param cliente Cliente a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminarCliente(Cliente cliente) {
        if (cliente == null || !listaDeClientes.contains(cliente)) {
            return false;
        }

        return listaDeClientes.remove(cliente);
    }

    /**
     * Verifica si un vehículo tiene cobertura activa
     * @param vehiculo Vehículo a verificar
     * @return true si tiene cobertura activa, false en caso contrario
     */
    public boolean verificarCoberturaVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null || vehiculo.getMembresia() == null || vehiculo.getFechaFinMembresia() == null) {
            return false;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fechaActual = LocalDate.now();
            LocalDate fechaFin = LocalDate.parse(vehiculo.getFechaFinMembresia(), formatter);

            return fechaActual.isBefore(fechaFin) || fechaActual.isEqual(fechaFin);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtiene todos los vehículos de un cliente
     * @param cliente Cliente del que se quieren obtener los vehículos
     * @return Lista de vehículos del cliente o lista vacía si no tiene
     */
    public List<Vehiculo> obtenerVehiculosCliente(Cliente cliente) {
        if (cliente == null) {
            return new ArrayList<>();
        }

        List<Vehiculo> vehiculos = cliente.getVehiculos();
        return vehiculos != null ? vehiculos : new ArrayList<>();
    }

    /**
     * Obtiene todos los clientes registrados en el sistema
     * @return Lista con todos los clientes o lista vacía si no hay ninguno
     */
    public List<Cliente> obtenerTodosLosClientes() {
        return new ArrayList<>(listaDeClientes);
    }

    /**
     * Obtiene el número de clientes registrados
     * @return Número de clientes
     */
    public int obtenerNumeroClientes() {
        return listaDeClientes.size();
    }
}