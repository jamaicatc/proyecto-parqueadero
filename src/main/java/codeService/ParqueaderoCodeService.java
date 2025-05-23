package codeService;

import model.Parqueadero;
import model.Vehiculo;
import model.Automovil;
import model.Moto;
import model.Camion;
import model.TipoMembresia;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParqueaderoCodeService {
    private Parqueadero parqueadero;
    // Registro de vehículos actualmente en el parqueadero con su hora de entrada
    private Map<String, LocalDateTime> vehiculosActuales = new HashMap<>();
    // Tarifas por hora para cada tipo de vehículo
    private double tarifaHoraAutomovil = 2000;
    private double tarifaHoraMoto = 1000;
    private double tarifaHoraCamion = 3000;

    public ParqueaderoCodeService(Parqueadero parqueadero) {
        this.parqueadero = parqueadero;
    }
    
    /**
     * Constructor vacío para pruebas
     */
    public ParqueaderoCodeService() {
    }
    
    /**
     * Setter para parqueadero (útil para pruebas)
     */
    public void setParqueadero(Parqueadero parqueadero) {
        this.parqueadero = parqueadero;
    }
    
    /**
     * Getter para parqueadero
     */
    public Parqueadero getParqueadero() {
        return this.parqueadero;
    }
    
    /**
     * Getter para vehículos actuales
     */
    public Map<String, LocalDateTime> getVehiculosActuales() {
        return new HashMap<>(vehiculosActuales);
    }

    /**
     * Configura las capacidades del parqueadero
     * @param puestosMotos cantidad de espacios para motos
     * @param puestosAutomoviles cantidad de espacios para automóviles
     * @param puestosCamiones cantidad de espacios para camiones
     * @return true si la configuración fue exitosa
     */
    public boolean configurarEspacios(int puestosMotos, int puestosAutomoviles, int puestosCamiones) {
        try {
            parqueadero.setPuestosMotos(puestosMotos);
            parqueadero.setPuestosAutomoviles(puestosAutomoviles);
            parqueadero.setPuestosCamiones(puestosCamiones);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Configura las tarifas por hora para cada tipo de vehículo
     * @param tarifaMoto tarifa por hora para motos
     * @param tarifaAutomovil tarifa por hora para automóviles
     * @param tarifaCamion tarifa por hora para camiones
     * @return true si la configuración fue exitosa
     */
    public boolean configurarTarifas(double tarifaMoto, double tarifaAutomovil, double tarifaCamion) {
        try {
            this.tarifaHoraMoto = tarifaMoto;
            this.tarifaHoraAutomovil = tarifaAutomovil;
            this.tarifaHoraCamion = tarifaCamion;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Registra la entrada de un vehículo temporal al parqueadero
     * @param tipoVehiculo 0=Automóvil, 1=Moto, 2=Camión
     * @param placa placa del vehículo
     * @param color color del vehículo
     * @param modelo modelo del vehículo
     * @return true si la entrada fue registrada exitosamente
     */
    public boolean registrarEntradaVehiculo(int tipoVehiculo, String placa, String color, String modelo) {
        // Validar datos de entrada
        if (placa == null || placa.trim().isEmpty() || 
            color == null || color.trim().isEmpty() || 
            modelo == null || modelo.trim().isEmpty() ||
            tipoVehiculo < 0 || tipoVehiculo > 2) {
            return false;
        }
        
        // Verificar disponibilidad de espacios
        boolean hayEspacio = false;
        switch (tipoVehiculo) {
            case 0: // Automóvil
                hayEspacio = verificarDisponibilidadAutomoviles();
                break;
            case 1: // Moto
                hayEspacio = verificarDisponibilidadMotos();
                break;
            case 2: // Camión
                hayEspacio = verificarDisponibilidadCamiones();
                break;
        }
        
        if (!hayEspacio) {
            return false;
        }
        
        // Verificar si el vehículo ya está en el parqueadero
        if (vehiculosActuales.containsKey(placa)) {
            return false;
        }
        
        // Crear el vehículo según el tipo
        Vehiculo vehiculo = null;
        switch (tipoVehiculo) {
            case 0: // Automóvil
                vehiculo = new Automovil(placa, color, modelo);
                break;
            case 1: // Moto
                vehiculo = new Moto(placa, color, modelo);
                break;
            case 2: // Camión
                vehiculo = new Camion(placa, color, modelo);
                break;
        }
        
        // Registrar hora de entrada
        vehiculosActuales.put(placa, LocalDateTime.now());
        
        // Agregar a la lista de vehículos del parqueadero
        parqueadero.getListaDevehiculos().add(vehiculo);
        
        return true;
    }

    /**
     * Registra la salida de un vehículo y calcula el monto a pagar
     * @param placa placa del vehículo que sale
     * @return mapa con información sobre la salida del vehículo o null si falló
     */
    public Map<String, Object> registrarSalidaVehiculo(String placa) {
        Map<String, Object> resultado = new HashMap<>();
        
        // Validar datos de entrada
        if (placa == null || placa.trim().isEmpty()) {
            return null;
        }
        
        // Verificar si el vehículo está en el parqueadero
        if (!vehiculosActuales.containsKey(placa)) {
            return null;
        }
        
        // Buscar el vehículo en la lista
        Vehiculo vehiculo = buscarVehiculoPorPlaca(placa);
        if (vehiculo == null) {
            return null;
        }
        
        // Calcular tiempo de permanencia
        LocalDateTime horaEntrada = vehiculosActuales.get(placa);
        LocalDateTime horaSalida = LocalDateTime.now();
        long horasEstadia = java.time.Duration.between(horaEntrada, horaSalida).toHours();
        // Mínimo 1 hora
        if (horasEstadia < 1) {
            horasEstadia = 1;
        }
        
        // Guardar información base
        resultado.put("placa", placa);
        resultado.put("horaEntrada", horaEntrada);
        resultado.put("horaSalida", horaSalida);
        resultado.put("horasEstadia", horasEstadia);
        
        // Verificar si tiene membresía activa
        if (vehiculo.getMembresia() != null && vehiculo.getMembresia() != TipoMembresia.NINGUNA) {
            resultado.put("tieneMembresiaActiva", true);
            resultado.put("montoTotal", 0.0);
            
            // Remover vehículo de la lista de actuales
            vehiculosActuales.remove(placa);
            parqueadero.getListaDevehiculos().remove(vehiculo);
            
            return resultado;
        }
        
        // Calcular tarifa según tipo de vehículo
        double tarifaHora = 0;
        String tipoVehiculo = "";
        
        if (vehiculo instanceof Automovil) {
            tarifaHora = tarifaHoraAutomovil;
            tipoVehiculo = "Automóvil";
        } else if (vehiculo instanceof Moto) {
            tarifaHora = tarifaHoraMoto;
            tipoVehiculo = "Moto";
        } else if (vehiculo instanceof Camion) {
            tarifaHora = tarifaHoraCamion;
            tipoVehiculo = "Camión";
        }
        
        double montoTotal = tarifaHora * horasEstadia;
        
        // Guardar información adicional
        resultado.put("tipoVehiculo", tipoVehiculo);
        resultado.put("tarifaHora", tarifaHora);
        resultado.put("montoTotal", montoTotal);
        resultado.put("tieneMembresiaActiva", false);
        
        // Remover vehículo de las listas
        vehiculosActuales.remove(placa);
        parqueadero.getListaDevehiculos().remove(vehiculo);
        
        return resultado;
    }

    /**
     * Obtiene información del estado actual de ocupación del parqueadero
     * @return Mapa con datos de ocupación
     */
    public Map<String, Object> obtenerEstadoParqueadero() {
        Map<String, Object> estado = new HashMap<>();
        
        int motosActuales = contarVehiculosPorTipo(Moto.class);
        int automovilesActuales = contarVehiculosPorTipo(Automovil.class);
        int camionesActuales = contarVehiculosPorTipo(Camion.class);
        
        estado.put("motosActuales", motosActuales);
        estado.put("motosTotales", parqueadero.getPuestosMotos());
        estado.put("motosDisponibles", parqueadero.getPuestosMotos() - motosActuales);
        
        estado.put("automovilesActuales", automovilesActuales);
        estado.put("automovilesTotales", parqueadero.getPuestosAutomoviles());
        estado.put("automovilesDisponibles", parqueadero.getPuestosAutomoviles() - automovilesActuales);
        
        estado.put("camionesActuales", camionesActuales);
        estado.put("camionesTotales", parqueadero.getPuestosCamiones());
        estado.put("camionesDisponibles", parqueadero.getPuestosCamiones() - camionesActuales);
        
        estado.put("totalEspacios", parqueadero.getPuestosMotos() + parqueadero.getPuestosAutomoviles() + parqueadero.getPuestosCamiones());
        estado.put("totalOcupacion", motosActuales + automovilesActuales + camionesActuales);
        
        return estado;
    }
    
    /**
     * Verifica la disponibilidad de espacios para motos
     * @return true si hay espacio disponible
     */
    public boolean verificarDisponibilidadMotos() {
        int motosActuales = contarVehiculosPorTipo(Moto.class);
        return motosActuales < parqueadero.getPuestosMotos();
    }

    /**
     * Verifica la disponibilidad de espacios para automóviles
     * @return true si hay espacio disponible
     */
    public boolean verificarDisponibilidadAutomoviles() {
        int automovilesActuales = contarVehiculosPorTipo(Automovil.class);
        return automovilesActuales < parqueadero.getPuestosAutomoviles();
    }

    /**
     * Verifica la disponibilidad de espacios para camiones
     * @return true si hay espacio disponible
     */
    public boolean verificarDisponibilidadCamiones() {
        int camionesActuales = contarVehiculosPorTipo(Camion.class);
        return camionesActuales < parqueadero.getPuestosCamiones();
    }

    /**
     * Cuenta cuántos vehículos de un tipo específico hay actualmente en el parqueadero
     * @param clase La clase del tipo de vehículo
     * @return Número de vehículos de ese tipo
     */
    private int contarVehiculosPorTipo(Class<?> clase) {
        int contador = 0;
        for (Vehiculo vehiculo : parqueadero.getListaDevehiculos()) {
            if (clase.isInstance(vehiculo)) {
                contador++;
            }
        }
        return contador;
    }

    /**
     * Busca un vehículo por su placa en la lista de vehículos del parqueadero
     * @param placa La placa a buscar
     * @return El vehículo encontrado o null si no existe
     */
    public Vehiculo buscarVehiculoPorPlaca(String placa) {
        for (Vehiculo vehiculo : parqueadero.getListaDevehiculos()) {
            if (vehiculo.getPlaca().equalsIgnoreCase(placa)) {
                return vehiculo;
            }
        }
        return null;
    }

    /**
     * Obtiene la lista de vehículos actualmente en el parqueadero
     * @return Lista de mapas con información de cada vehículo
     */
    public List<Map<String, Object>> obtenerListaVehiculosActuales() {
        List<Map<String, Object>> resultado = new ArrayList<>();
        
        if (vehiculosActuales.isEmpty()) {
            return resultado;
        }
        
        // Convertir el mapa a una lista
        List<Map.Entry<String, LocalDateTime>> listaVehiculos = new ArrayList<>(vehiculosActuales.entrySet());
        
        // Procesar cada vehículo
        for (Map.Entry<String, LocalDateTime> entry : listaVehiculos) {
            String placa = entry.getKey();
            LocalDateTime horaEntrada = entry.getValue();
            Vehiculo vehiculo = buscarVehiculoPorPlaca(placa);
            
            if (vehiculo != null) {
                Map<String, Object> infoVehiculo = new HashMap<>();
                
                infoVehiculo.put("placa", placa);
                infoVehiculo.put("tipo", obtenerTipoVehiculo(vehiculo));
                infoVehiculo.put("color", vehiculo.getColor());
                infoVehiculo.put("modelo", vehiculo.getModelo());
                infoVehiculo.put("horaEntrada", horaEntrada);
                
                // Verificar si tiene membresía
                if (vehiculo.getMembresia() != null && vehiculo.getMembresia() != TipoMembresia.NINGUNA) {
                    infoVehiculo.put("tieneMembresia", true);
                    infoVehiculo.put("tipoMembresia", vehiculo.getMembresia());
                    infoVehiculo.put("fechaFinMembresia", vehiculo.getFechaFinMembresia());
                } else {
                    infoVehiculo.put("tieneMembresia", false);
                }
                
                resultado.add(infoVehiculo);
            }
        }
        
        return resultado;
    }

    /**
     * Determina el tipo de vehículo basado en su instancia
     * @param vehiculo El vehículo a evaluar
     * @return Tipo de vehículo como texto
     */
    public String obtenerTipoVehiculo(Vehiculo vehiculo) {
        if (vehiculo instanceof Automovil) {
            return "Automóvil";
        } else if (vehiculo instanceof Moto) {
            return "Moto";
        } else if (vehiculo instanceof Camion) {
            return "Camión";
        }
        return "Desconocido";
    }
    
    /**
     * Obtiene la tarifa por hora para un tipo de vehículo
     * @param tipo El tipo de vehículo (0=Auto, 1=Moto, 2=Camión)
     * @return La tarifa correspondiente
     */
    public double obtenerTarifaPorTipo(int tipo) {
        switch (tipo) {
            case 0: return tarifaHoraAutomovil;
            case 1: return tarifaHoraMoto;
            case 2: return tarifaHoraCamion;
            default: return 0;
        }
    }
}