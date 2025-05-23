package model;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import interfaces.IMembresiaActiva;

public class Cliente implements IMembresiaActiva {
    private String nombre;
    private String cedula;
    private String telefono;
    private String correo;
    private List<Membresia> membresias;
    private List<Vehiculo> vehiculos; // Nueva lista para almacenar los vehículos del cliente

    public Cliente(String nombre, String cedula, String telefono, String correo) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.telefono = telefono;
        this.correo = correo;
        this.membresias = new ArrayList<>();
        this.vehiculos = new ArrayList<>(); // Inicialización de la lista de vehículos
    }

    public List<Membresia> getMembresias() {
        return membresias;
    }

    public void setMembresias(List<Membresia> membresias) {
        this.membresias = membresias;
    }

    public void setVehiculos(List<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }

    /**
     * Obtiene la lista de vehículos asociados al cliente
     * @return Lista de vehículos del cliente
     */
    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }
    
    /**
     * Agrega un vehículo a la lista de vehículos del cliente
     * @param vehiculo El vehículo a agregar
     */
    public void agregarVehiculo(Vehiculo vehiculo) {
        if (vehiculo != null) {
            this.vehiculos.add(vehiculo);
        }
    }
    
    /**
     * Elimina un vehículo de la lista de vehículos del cliente
     * @param vehiculo El vehículo a eliminar
     * @return true si el vehículo fue eliminado, false en caso contrario
     */
    public boolean eliminarVehiculo(Vehiculo vehiculo) {
        if (vehiculo != null) {
            return this.vehiculos.remove(vehiculo);
        }
        return false;
    }
    
    /**
     * Busca un vehículo por su placa
     * @param placa La placa del vehículo a buscar
     * @return El vehículo encontrado o null si no existe
     */
    public Vehiculo buscarVehiculoPorPlaca(String placa) {
        if (placa != null && !placa.isEmpty()) {
            return this.vehiculos.stream()
                    .filter(v -> v.getPlaca().equalsIgnoreCase(placa))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public boolean tieneMembresiaActiva() {
        // Si el cliente no tiene membresías, retorna false
        if (membresias == null || membresias.isEmpty()) {
            return false;
        }

        // Verificar si alguna de las membresías está activa
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Membresia membresia : membresias) {
            if (membresia.getFechaFin() != null) {
                try {
                    LocalDate fechaFin = LocalDate.parse(membresia.getFechaFin(), formatter);
                    if (fechaActual.isBefore(fechaFin) || fechaActual.isEqual(fechaFin)) {
                        return true;
                    }
                } catch (Exception e) {
                    // Si hay un error al parsear la fecha, continuamos con la siguiente membresía
                    continue;
                }
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return "nombre: " + nombre + "\ncedula: " + cedula + "\ntelefono: " + telefono + "\ncorreo: " + correo;
    }
}