package codeService;

import model.Cliente;
import model.Membresia;
import model.TipoMembresia;
import model.Vehiculo;
import model.Automovil;
import model.Moto;
import model.Camion;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MembresiaCodeService {
    private PagoCodeService pagoService;
    private ClienteCodeService clienteService;
    private VehiculoCodeService vehiculoService;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final int DIAS_PROXIMIDAD_VENCIMIENTO = 30; // Días para considerar una membresía próxima a vencer

    public MembresiaCodeService() {
        this.pagoService = null; // Se inicializará después para evitar dependencia circular
    }

    /**
     * Establece el servicio de pagos - para evitar dependencia circular
     */
    public void setPagoService(PagoCodeService pagoService) {
        this.pagoService = pagoService;
    }

    /**
     * Establece el servicio de clientes
     */
    public void setClienteService(ClienteCodeService clienteService) {
        this.clienteService = clienteService;
    }
    
    /**
     * Establece el servicio de vehículos
     */
    public void setVehiculoService(VehiculoCodeService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    public PagoCodeService getPagoService() {
        return pagoService;
    }

    public ClienteCodeService getClienteService() {
        return clienteService;
    }

    public VehiculoCodeService getVehiculoService() {
        return this.vehiculoService;
    }

    /**
     * Registra una nueva membresía para un vehículo
     * @param vehiculo El vehículo al que se le registrará la membresía
     * @param cliente El cliente asociado a la membresía
     * @param tipoMembresia El tipo de membresía a registrar
     * @return true si el registro fue exitoso, false en caso contrario
     */
    public boolean registrarMembresia(Vehiculo vehiculo, Cliente cliente, TipoMembresia tipoMembresia) {
        if (vehiculo == null || cliente == null || tipoMembresia == null || tipoMembresia == TipoMembresia.NINGUNA) {
            return false;
        }

        // Verificar si ya tiene membresía activa
        if (vehiculo.getMembresia() != null && vehiculo.getMembresia() != TipoMembresia.NINGUNA) {
            return false;
        }

        // Calcular fecha de fin según el tipo de membresía
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaFin;

        switch (tipoMembresia) {
            case MENSUAL: fechaFin = fechaInicio.plusMonths(1); break;
            case TRIMESTRAL: fechaFin = fechaInicio.plusMonths(3); break;
            case ANUAL: fechaFin = fechaInicio.plusYears(1); break;
            default: return false;
        }

        // Registrar la membresía en el vehículo
        vehiculo.setMembresia(tipoMembresia);
        vehiculo.setFechaInicioMembresia(fechaInicio.format(FORMATTER));
        vehiculo.setFechaFinMembresia(fechaFin.format(FORMATTER));

        // Crear y agregar la membresía al cliente
        Membresia membresia = new Membresia(
                tipoMembresia,
                fechaInicio.format(FORMATTER),
                fechaFin.format(FORMATTER),
                calcularTarifaMembresia(vehiculo, tipoMembresia)
        );
        cliente.getMembresias().add(membresia);

        // Registrar el pago si se ha configurado el pagoService
        if (pagoService != null) {
            return pagoService.registrarPagoMembresia(vehiculo, cliente, tipoMembresia);
        }
        
        return true;
    }

    /**
     * Verifica si una membresía está vigente
     * @param vehiculo El vehículo cuya membresía se verificará
     * @return Un mapa con la información de vigencia o null si no tiene membresía
     */
    public Map<String, Object> verificarVigencia(Vehiculo vehiculo) {
        if (vehiculo == null) {
            return null;
        }
        
        if (vehiculo.getMembresia() == null || vehiculo.getMembresia() == TipoMembresia.NINGUNA) {
            return null;
        }
        
        Map<String, Object> resultado = new HashMap<>();
        
        // Extraer fecha de fin y verificar vigencia
        try {
            String fechaFinStr = vehiculo.getFechaFinMembresia();
            LocalDate fechaFin = LocalDate.parse(fechaFinStr, FORMATTER);
            LocalDate hoy = LocalDate.now();
            
            resultado.put("tipoMembresia", vehiculo.getMembresia());
            resultado.put("fechaVencimiento", fechaFinStr);
            
            if (fechaFin.isBefore(hoy)) {
                resultado.put("vigente", false);
                resultado.put("mensaje", "La membresía ha vencido");
            } else {
                long diasRestantes = ChronoUnit.DAYS.between(hoy, fechaFin);
                resultado.put("vigente", true);
                resultado.put("diasRestantes", diasRestantes);
                resultado.put("mensaje", "La membresía está vigente");
            }
            
            return resultado;
        } catch (Exception e) {
            resultado.put("error", "Error al verificar la vigencia: " + e.getMessage());
            return resultado;
        }
    }

    /**
     * Renueva una membresía existente con el mismo tipo
     * @param vehiculo El vehículo cuya membresía se renovará
     * @param cliente El cliente asociado a la membresía
     * @return true si la renovación fue exitosa, false en caso contrario
     */
    public boolean renovarMembresia(Vehiculo vehiculo, Cliente cliente) {
        if (vehiculo == null || cliente == null) {
            return false;
        }
        
        if (vehiculo.getMembresia() == null || vehiculo.getMembresia() == TipoMembresia.NINGUNA) {
            return false;
        }
        
        TipoMembresia tipoActual = vehiculo.getMembresia();
        return renovarConMismoTipo(vehiculo, cliente, tipoActual);
    }

    /**
     * Renueva una membresía cambiando su tipo
     * @param vehiculo El vehículo cuya membresía se renovará
     * @param cliente El cliente asociado a la membresía
     * @param nuevoTipo El nuevo tipo de membresía
     * @return true si la renovación fue exitosa, false en caso contrario
     */
    public boolean renovarMembresiaConNuevoTipo(Vehiculo vehiculo, Cliente cliente, TipoMembresia nuevoTipo) {
        if (vehiculo == null || cliente == null || nuevoTipo == null || nuevoTipo == TipoMembresia.NINGUNA) {
            return false;
        }
        
        if (vehiculo.getMembresia() == null || vehiculo.getMembresia() == TipoMembresia.NINGUNA) {
            return false;
        }
        
        // Actualizar tipo de membresía
        vehiculo.setMembresia(nuevoTipo);
        
        // Renovar con el nuevo tipo
        return renovarConMismoTipo(vehiculo, cliente, nuevoTipo);
    }

    /**
     * Renueva con el mismo tipo de membresía
     * @param vehiculo El vehículo cuya membresía se renovará
     * @param cliente El cliente asociado a la membresía
     * @param tipo El tipo de membresía a renovar
     * @return true si la renovación fue exitosa, false en caso contrario
     */
    private boolean renovarConMismoTipo(Vehiculo vehiculo, Cliente cliente, TipoMembresia tipo) {
        if (vehiculo == null || cliente == null || tipo == null || tipo == TipoMembresia.NINGUNA) {
            return false;
        }
        
        // Determinar fecha de inicio (puede ser la fecha actual o la fecha de fin de la membresía actual)
        LocalDate fechaFin;
        try {
            fechaFin = LocalDate.parse(vehiculo.getFechaFinMembresia(), FORMATTER);
        } catch (Exception e) {
            fechaFin = LocalDate.now();
        }

        // Si la fecha fin ya pasó, usar la fecha actual como inicio
        LocalDate fechaInicio = LocalDate.now();
        if (fechaFin.isAfter(fechaInicio)) {
            fechaInicio = fechaFin; // Continuar desde donde termina la membresía actual
        }

        // Calcular nueva fecha de fin
        LocalDate nuevaFechaFin;
        switch (tipo) {
            case MENSUAL: nuevaFechaFin = fechaInicio.plusMonths(1); break;
            case TRIMESTRAL: nuevaFechaFin = fechaInicio.plusMonths(3); break;
            case ANUAL: nuevaFechaFin = fechaInicio.plusYears(1); break;
            default: return false;
        }

        // Actualizar datos en el vehículo
        vehiculo.setFechaInicioMembresia(fechaInicio.format(FORMATTER));
        vehiculo.setFechaFinMembresia(nuevaFechaFin.format(FORMATTER));

        // Crear y agregar la membresía renovada al cliente
        Membresia membresia = new Membresia(
                tipo,
                fechaInicio.format(FORMATTER),
                nuevaFechaFin.format(FORMATTER),
                calcularTarifaMembresia(vehiculo, tipo)
        );
        cliente.getMembresias().add(membresia);

        // Registrar pago si está disponible el servicio
        if (pagoService != null) {
            return pagoService.registrarPagoMembresia(vehiculo, cliente, tipo);
        }
        
        return true;
    }

    /**
     * Genera un reporte de clientes con membresías activas y próximas a vencer
     * @return Un mapa con la información del reporte o null si hay error
     */
    public Map<String, Object> generarReporteMembresiasActivas() {
        if (clienteService == null || vehiculoService == null) {
            return null;
        }
        
        // Estructuras para almacenar resultados
        Map<Cliente, List<Vehiculo>> clientesConMembresiasActivas = new HashMap<>();
        Map<Cliente, List<Vehiculo>> clientesConMembresiasProximasAVencer = new HashMap<>();
        Map<String, Object> reporte = new HashMap<>();
        
        try {
            // Obtener todos los clientes
            List<Cliente> clientes = clienteService.obtenerTodosLosClientes();
            
            if (clientes == null || clientes.isEmpty()) {
                reporte.put("error", "No hay clientes registrados en el sistema");
                return reporte;
            }
            
            // Fecha actual para comparaciones
            LocalDate fechaActual = LocalDate.now();
            
            // Procesar cada cliente
            for (Cliente cliente : clientes) {
                // Verificar si el cliente tiene membresía activa
                if (cliente.tieneMembresiaActiva()) {
                    // Obtener vehículos del cliente
                    List<Vehiculo> vehiculosCliente = vehiculoService.obtenerVehiculosPorCliente(cliente);
                    
                    if (vehiculosCliente != null && !vehiculosCliente.isEmpty()) {
                        List<Vehiculo> vehiculosActivos = new ArrayList<>();
                        List<Vehiculo> vehiculosProximosAVencer = new ArrayList<>();
                        
                        // Verificar estado de cada vehículo
                        for (Vehiculo vehiculo : vehiculosCliente) {
                            if (vehiculo.getMembresia() != null && 
                                vehiculo.getMembresia() != TipoMembresia.NINGUNA &&
                                vehiculo.getFechaFinMembresia() != null) {
                                
                                try {
                                    LocalDate fechaFin = LocalDate.parse(vehiculo.getFechaFinMembresia(), FORMATTER);
                                    
                                    // Verificar si está activa (no ha vencido)
                                    if (fechaActual.isBefore(fechaFin) || fechaActual.isEqual(fechaFin)) {
                                        vehiculosActivos.add(vehiculo);
                                        
                                        // Verificar si está próxima a vencer (30 días o menos)
                                        long diasRestantes = ChronoUnit.DAYS.between(fechaActual, fechaFin);
                                        if (diasRestantes <= DIAS_PROXIMIDAD_VENCIMIENTO) {
                                            vehiculosProximosAVencer.add(vehiculo);
                                        }
                                    }
                                } catch (Exception e) {
                                    // Ignorar errores de formato de fecha
                                    continue;
                                }
                            }
                        }
                        
                        // Almacenar resultados
                        if (!vehiculosActivos.isEmpty()) {
                            clientesConMembresiasActivas.put(cliente, vehiculosActivos);
                        }
                        
                        if (!vehiculosProximosAVencer.isEmpty()) {
                            clientesConMembresiasProximasAVencer.put(cliente, vehiculosProximosAVencer);
                        }
                    }
                }
            }
            
            // Estadísticas
            int totalClientes = clientesConMembresiasActivas.size();
            int totalVehiculos = 0;
            for (List<Vehiculo> vehiculos : clientesConMembresiasActivas.values()) {
                totalVehiculos += vehiculos.size();
            }
            
            int totalProximosVencer = 0;
            for (List<Vehiculo> vehiculos : clientesConMembresiasProximasAVencer.values()) {
                totalProximosVencer += vehiculos.size();
            }
            
            // Generar el reporte
            reporte.put("totalClientes", totalClientes);
            reporte.put("totalVehiculos", totalVehiculos);
            reporte.put("totalProximosVencer", totalProximosVencer);
            reporte.put("clientesConMembresiasActivas", clientesConMembresiasActivas);
            reporte.put("clientesConMembresiasProximasAVencer", clientesConMembresiasProximasAVencer);
            reporte.put("diasProximidadVencimiento", DIAS_PROXIMIDAD_VENCIMIENTO);
            
            return reporte;
        } catch (Exception e) {
            reporte.put("error", "Error al generar el reporte: " + e.getMessage());
            return reporte;
        }
    }

    /**
     * Calcula la tarifa de membresía según el tipo de vehículo y membresía
     * @param vehiculo El vehículo para el que se calculará la tarifa
     * @param tipo El tipo de membresía
     * @return El valor de la tarifa
     */
    public int calcularTarifaMembresia(Vehiculo vehiculo, TipoMembresia tipo) {
        if (vehiculo == null || tipo == null) {
            return 0;
        }
        
        if (pagoService != null) {
            // Si tenemos el servicio de pagos, usar su método
            return (int) pagoService.calcularTarifaMembresia(vehiculo, tipo);
        } else {
            // Tarifas por defecto si no hay servicio de pagos
            if (vehiculo instanceof Automovil) {
                switch (tipo) {
                    case MENSUAL: return 100000;
                    case TRIMESTRAL: return 270000;
                    case ANUAL: return 960000;
                    default: return 0;
                }
            } else if (vehiculo instanceof Moto) {
                switch (tipo) {
                    case MENSUAL: return 50000;
                    case TRIMESTRAL: return 135000;
                    case ANUAL: return 480000;
                    default: return 0;
                }
            } else if (vehiculo instanceof Camion) {
                switch (tipo) {
                    case MENSUAL: return 150000;
                    case TRIMESTRAL: return 405000;
                    case ANUAL: return 1440000;
                    default: return 0;
                }
            }
            // Valor predeterminado para otros tipos de vehículos
            switch (tipo) {
                case MENSUAL: return 100000;
                case TRIMESTRAL: return 270000;
                case ANUAL: return 960000;
                default: return 0;
            }
        }
    }
    
    /**
     * Verifica si un vehículo tiene una membresía activa
     * @param vehiculo El vehículo a verificar
     * @return true si tiene membresía activa, false en caso contrario
     */
    public boolean tieneMembresiasActiva(Vehiculo vehiculo) {
        if (vehiculo == null) {
            return false;
        }
        
        if (vehiculo.getMembresia() == null || vehiculo.getMembresia() == TipoMembresia.NINGUNA) {
            return false;
        }
        
        try {
            String fechaFinStr = vehiculo.getFechaFinMembresia();
            LocalDate fechaFin = LocalDate.parse(fechaFinStr, FORMATTER);
            LocalDate hoy = LocalDate.now();
            
            return hoy.isBefore(fechaFin) || hoy.isEqual(fechaFin);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Verifica si la membresía de un vehículo está próxima a vencer
     * @param vehiculo El vehículo a verificar
     * @return true si la membresía está próxima a vencer, false en caso contrario
     */
    public boolean membresiaProximaAVencer(Vehiculo vehiculo) {
        if (!tieneMembresiasActiva(vehiculo)) {
            return false;
        }
        
        try {
            String fechaFinStr = vehiculo.getFechaFinMembresia();
            LocalDate fechaFin = LocalDate.parse(fechaFinStr, FORMATTER);
            LocalDate hoy = LocalDate.now();
            
            long diasRestantes = ChronoUnit.DAYS.between(hoy, fechaFin);
            return diasRestantes <= DIAS_PROXIMIDAD_VENCIMIENTO && diasRestantes >= 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Obtiene los días restantes para el vencimiento de una membresía
     * @param vehiculo El vehículo a verificar
     * @return Los días restantes o -1 si la membresía no está activa o hay error
     */
    public long diasRestantesMembresia(Vehiculo vehiculo) {
        if (!tieneMembresiasActiva(vehiculo)) {
            return -1;
        }
        
        try {
            String fechaFinStr = vehiculo.getFechaFinMembresia();
            LocalDate fechaFin = LocalDate.parse(fechaFinStr, FORMATTER);
            LocalDate hoy = LocalDate.now();
            
            return ChronoUnit.DAYS.between(hoy, fechaFin);
        } catch (Exception e) {
            return -1;
        }
    }
    
    /**
     * Cancela la membresía de un vehículo
     * @param vehiculo El vehículo cuya membresía se cancelará
     * @return true si la cancelación fue exitosa, false en caso contrario
     */
    public boolean cancelarMembresia(Vehiculo vehiculo) {
        if (vehiculo == null) {
            return false;
        }
        
        if (vehiculo.getMembresia() == null || vehiculo.getMembresia() == TipoMembresia.NINGUNA) {
            return false;
        }
        
        vehiculo.setMembresia(TipoMembresia.NINGUNA);
        vehiculo.setFechaInicioMembresia(null);
        vehiculo.setFechaFinMembresia(null);
        
        return true;
    }
}