package codeService;

import model.Cliente;
import model.Vehiculo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehiculoCodeService {
    private Map<String, List<Vehiculo>> vehiculosPorCliente;
    private List<Vehiculo> listaGeneralVehiculos; // Lista general para guardar todos los vehículos

    public VehiculoCodeService() {
        this.vehiculosPorCliente = new HashMap<>();
        this.listaGeneralVehiculos = new ArrayList<>(); // Inicialización de la lista general
    }

    /**
     * Registra un vehículo en la lista general
     * 
     * @param placa  La placa del vehículo
     * @param color  El color del vehículo
     * @param modelo El modelo del vehículo
     * @return true si el registro fue exitoso, false en caso contrario
     */
    public boolean registrarVehiculo(String placa, String color, String modelo) {
        // Validar que no haya campos vacíos
        if (placa == null || placa.trim().isEmpty() || 
            color == null || color.trim().isEmpty() || 
            modelo == null || modelo.trim().isEmpty()) {
            return false;
        }

        // Verificar si ya existe la placa en la lista general
        boolean placaExiste = listaGeneralVehiculos.stream()
                .anyMatch(v -> v.getPlaca().equalsIgnoreCase(placa));

        if (placaExiste) {
            return false;
        }

        Vehiculo nuevoVehiculo = new Vehiculo(placa, color, modelo);
        listaGeneralVehiculos.add(nuevoVehiculo);
        return true;
    }

    /**
     * Registra un vehículo asociado a un cliente
     * 
     * @param cliente El cliente al que se asociará el vehículo
     * @param placa   La placa del vehículo
     * @param color   El color del vehículo
     * @param modelo  El modelo del vehículo
     * @return true si el registro fue exitoso, false en caso contrario
     */
    public boolean registrarVehiculo(Cliente cliente, String placa, String color, String modelo) {
        // Validar que el cliente no sea nulo y que no haya campos vacíos
        if (cliente == null || 
            placa == null || placa.trim().isEmpty() || 
            color == null || color.trim().isEmpty() || 
            modelo == null || modelo.trim().isEmpty()) {
            return false;
        }

        // Obtener la lista de vehículos del cliente
        List<Vehiculo> listaVehiculos = vehiculosPorCliente.getOrDefault(cliente.getCedula(), new ArrayList<>());

        // Verificar si ya existe la placa
        boolean placaExiste = listaVehiculos.stream()
                .anyMatch(v -> v.getPlaca().equalsIgnoreCase(placa));
        boolean placaExisteGeneral = listaGeneralVehiculos.stream()
                .anyMatch(v -> v.getPlaca().equalsIgnoreCase(placa));

        if (placaExiste || placaExisteGeneral) {
            return false;
        }

        Vehiculo nuevoVehiculo = new Vehiculo(placa, color, modelo);
        listaVehiculos.add(nuevoVehiculo);
        listaGeneralVehiculos.add(nuevoVehiculo); // Agregar a la lista general también
        vehiculosPorCliente.put(cliente.getCedula(), listaVehiculos);
        return true;
    }

    /**
     * Busca un vehículo por su placa
     * 
     * @param placa La placa del vehículo a buscar
     * @return El vehículo encontrado o null si no existe
     */
    public Vehiculo buscarVehiculo(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            return null;
        }
        
        // Primero buscar en la lista general
        for (Vehiculo vehiculo : listaGeneralVehiculos) {
            if (vehiculo.getPlaca().equalsIgnoreCase(placa.trim())) {
                return vehiculo;
            }
        }
        
        // Si no se encuentra en la lista general, buscar en las listas por cliente
        for (List<Vehiculo> vehiculos : vehiculosPorCliente.values()) {
            for (Vehiculo vehiculo : vehiculos) {
                if (vehiculo.getPlaca().equalsIgnoreCase(placa.trim())) {
                    return vehiculo;
                }
            }
        }
        
        return null;
    }

    /**
     * Actualiza los datos de un vehículo
     * 
     * @param placaActual  La placa actual del vehículo
     * @param nuevaPlaca   La nueva placa del vehículo (null o vacío si no cambia)
     * @param nuevoColor   El nuevo color del vehículo (null o vacío si no cambia)
     * @param nuevoModelo  El nuevo modelo del vehículo (null o vacío si no cambia)
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean actualizarVehiculo(String placaActual, String nuevaPlaca, String nuevoColor, String nuevoModelo) {
        Vehiculo vehiculo = buscarVehiculo(placaActual);

        if (vehiculo == null) {
            return false;
        }

        // Actualizar placa si se proporciona una nueva
        if (nuevaPlaca != null && !nuevaPlaca.trim().isEmpty() && !nuevaPlaca.equals(placaActual)) {
            // Verificar que la nueva placa no exista ya en otro vehículo
            boolean placaExiste = listaGeneralVehiculos.stream()
                    .anyMatch(v -> v.getPlaca().equalsIgnoreCase(nuevaPlaca) && !v.equals(vehiculo));
            
            if (placaExiste) {
                return false;
            }
            vehiculo.setPlaca(nuevaPlaca);
        }

        // Actualizar color si se proporciona uno nuevo
        if (nuevoColor != null && !nuevoColor.trim().isEmpty()) {
            vehiculo.setColor(nuevoColor);
        }

        // Actualizar modelo si se proporciona uno nuevo
        if (nuevoModelo != null && !nuevoModelo.trim().isEmpty()) {
            vehiculo.setModelo(nuevoModelo);
        }

        return true;
    }

    /**
     * Obtiene los vehículos asociados a un cliente específico
     * 
     * @param cliente Cliente del que se obtendrán los vehículos
     * @return Lista de vehículos asociados al cliente
     */
    public List<Vehiculo> obtenerVehiculosPorCliente(Cliente cliente) {
        if (cliente == null) {
            return new ArrayList<>();
        }

        String cedula = cliente.getCedula();
        if (cedula == null || cedula.trim().isEmpty()) {
            return new ArrayList<>();
        }

        List<Vehiculo> vehiculos = vehiculosPorCliente.get(cedula);
        return vehiculos != null ? new ArrayList<>(vehiculos) : new ArrayList<>();
    }

    /**
     * Obtiene todos los vehículos registrados en el sistema
     * 
     * @return Lista con todos los vehículos
     */
    public List<Vehiculo> obtenerTodosLosVehiculos() {
        return new ArrayList<>(listaGeneralVehiculos);
    }
    
    /**
     * Elimina un vehículo del sistema
     * 
     * @param placa La placa del vehículo a eliminar
     * @return true si se eliminó correctamente, false si no se encontró el vehículo
     */
    public boolean eliminarVehiculo(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            return false;
        }
        
        // Verificar si el vehículo existe
        Vehiculo vehiculo = buscarVehiculo(placa);
        if (vehiculo == null) {
            return false;
        }
        
        // Eliminar de la lista general
        boolean removido = listaGeneralVehiculos.removeIf(v -> v.getPlaca().equalsIgnoreCase(placa));
        
        // Eliminar de las listas por cliente
        for (Map.Entry<String, List<Vehiculo>> entry : vehiculosPorCliente.entrySet()) {
            entry.getValue().removeIf(v -> v.getPlaca().equalsIgnoreCase(placa));
        }
        
        return removido;
    }
    
    /**
     * Verifica si un cliente tiene vehículos asociados
     * 
     * @param cliente El cliente a verificar
     * @return true si el cliente tiene al menos un vehículo, false en caso contrario
     */
    public boolean clienteTieneVehiculos(Cliente cliente) {
        if (cliente == null) {
            return false;
        }
        
        List<Vehiculo> vehiculos = obtenerVehiculosPorCliente(cliente);
        return !vehiculos.isEmpty();
    }
    
    /**
     * Cuenta la cantidad de vehículos asociados a un cliente
     * 
     * @param cliente El cliente del que se quiere contar los vehículos
     * @return Número de vehículos del cliente
     */
    public int contarVehiculosPorCliente(Cliente cliente) {
        List<Vehiculo> vehiculos = obtenerVehiculosPorCliente(cliente);
        return vehiculos.size();
    }
    
    /**
     * Verifica si existe un vehículo con la placa especificada
     * 
     * @param placa La placa a verificar
     * @return true si existe un vehículo con esa placa, false en caso contrario
     */
    public boolean existeVehiculo(String placa) {
        return buscarVehiculo(placa) != null;
    }
    
    /**
     * Obtiene los detalles de un vehículo en formato de mapa
     * 
     * @param placa La placa del vehículo
     * @return Mapa con los detalles del vehículo o null si no se encuentra
     */
    public Map<String, String> obtenerDetallesVehiculo(String placa) {
        Vehiculo vehiculo = buscarVehiculo(placa);
        if (vehiculo == null) {
            return null;
        }
        
        Map<String, String> detalles = new HashMap<>();
        detalles.put("placa", vehiculo.getPlaca());
        detalles.put("color", vehiculo.getColor());
        detalles.put("modelo", vehiculo.getModelo());
        
        return detalles;
    }
    
    /**
     * Encuentra al cliente propietario de un vehículo
     * 
     * @param placa La placa del vehículo
     * @return La cédula del cliente propietario o null si no se encuentra
     */
    public String buscarPropietarioVehiculo(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            return null;
        }
        
        for (Map.Entry<String, List<Vehiculo>> entry : vehiculosPorCliente.entrySet()) {
            for (Vehiculo vehiculo : entry.getValue()) {
                if (vehiculo.getPlaca().equalsIgnoreCase(placa.trim())) {
                    return entry.getKey(); // Retorna la cédula del propietario
                }
            }
        }
        
        return null;
    }
    
    /**
     * Asigna un vehículo existente a un cliente
     * 
     * @param cliente El cliente al que se asignará el vehículo
     * @param placa La placa del vehículo a asignar
     * @return true si la asignación fue exitosa, false en caso contrario
     */
    public boolean asignarVehiculoACliente(Cliente cliente, String placa) {
        if (cliente == null || placa == null || placa.trim().isEmpty()) {
            return false;
        }
        
        Vehiculo vehiculo = buscarVehiculo(placa);
        if (vehiculo == null) {
            return false;
        }
        
        // Verificar si el vehículo ya está asignado a este cliente
        List<Vehiculo> vehiculosCliente = vehiculosPorCliente.getOrDefault(cliente.getCedula(), new ArrayList<>());
        if (vehiculosCliente.contains(vehiculo)) {
            return false; // Ya está asignado
        }
        
        // Añadir a la lista de vehículos del cliente
        vehiculosCliente.add(vehiculo);
        vehiculosPorCliente.put(cliente.getCedula(), vehiculosCliente);
        
        return true;
    }
    
    /**
     * Desvincula un vehículo de un cliente sin eliminarlo del sistema
     * 
     * @param cliente El cliente del que se desvinculará el vehículo
     * @param placa La placa del vehículo a desvincular
     * @return true si la desvinculación fue exitosa, false en caso contrario
     */
    public boolean desvincularVehiculoDeCliente(Cliente cliente, String placa) {
        if (cliente == null || placa == null || placa.trim().isEmpty()) {
            return false;
        }
        
        List<Vehiculo> vehiculosCliente = vehiculosPorCliente.get(cliente.getCedula());
        if (vehiculosCliente == null || vehiculosCliente.isEmpty()) {
            return false;
        }
        
        boolean removido = vehiculosCliente.removeIf(v -> v.getPlaca().equalsIgnoreCase(placa));
        if (removido) {
            vehiculosPorCliente.put(cliente.getCedula(), vehiculosCliente);
            return true;
        }
        
        return false;
    }
    
    /**
     * Verifica si un vehículo pertenece a un cliente específico
     * 
     * @param cliente El cliente a verificar
     * @param placa La placa del vehículo
     * @return true si el vehículo pertenece al cliente, false en caso contrario
     */
    public boolean vehiculoPerteneceACliente(Cliente cliente, String placa) {
        if (cliente == null || placa == null || placa.trim().isEmpty()) {
            return false;
        }
        
        List<Vehiculo> vehiculosCliente = vehiculosPorCliente.get(cliente.getCedula());
        if (vehiculosCliente == null || vehiculosCliente.isEmpty()) {
            return false;
        }
        
        return vehiculosCliente.stream()
                .anyMatch(v -> v.getPlaca().equalsIgnoreCase(placa));
    }
    
    /**
     * Retorna una lista de vehículos asociados a un cliente para visualización
     * Esta es una versión del método verVehiculosAsociados que no usa JOptionPane
     * 
     * @param cliente El cliente del que se obtendrán los vehículos
     * @return Lista de vehículos del cliente
     */
    public List<Vehiculo> verVehiculosAsociados(Cliente cliente) {
        if (cliente == null) {
            return new ArrayList<>();
        }
        
        return obtenerVehiculosPorCliente(cliente);
    }
}