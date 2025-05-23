package codeService;

import model.Cliente;
import model.Pago;
import model.TipoMembresia;
import model.Vehiculo;
import model.Automovil;
import model.Moto;
import model.Camion;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PagoCodeService {
    private List<Pago> historialPagos;
    
    // Tarifas de membresía por tipo de vehículo y período
    private static final double TARIFA_MENSUAL_AUTO = 100000;
    private static final double TARIFA_TRIMESTRAL_AUTO = 270000;
    private static final double TARIFA_ANUAL_AUTO = 960000;
    
    private static final double TARIFA_MENSUAL_MOTO = 50000;
    private static final double TARIFA_TRIMESTRAL_MOTO = 135000;
    private static final double TARIFA_ANUAL_MOTO = 480000;
    
    private static final double TARIFA_MENSUAL_CAMION = 150000;
    private static final double TARIFA_TRIMESTRAL_CAMION = 405000;
    private static final double TARIFA_ANUAL_CAMION = 1440000;

    public PagoCodeService() {
        this.historialPagos = new ArrayList<>();
    }

    /**
     * Registra un pago por estacionamiento temporal
     */
    public boolean registrarPago(Vehiculo vehiculo, double monto) {
        if (vehiculo == null || monto <= 0) {
            return false;
        }
        
        String tipoVehiculo = determinarTipoVehiculo(vehiculo);
        Pago pago = new Pago(monto, vehiculo, tipoVehiculo);
        historialPagos.add(pago);
        
        return true;
    }

    /**
     * Registra un pago por membresía
     */
    public boolean registrarPagoMembresia(Vehiculo vehiculo, Cliente cliente, TipoMembresia tipoMembresia) {
        if (vehiculo == null || cliente == null || tipoMembresia == null) {
            return false;
        }
        
        double monto = calcularTarifaMembresia(vehiculo, tipoMembresia);
        if (monto <= 0) {
            return false;
        }
        
        Pago pago = new Pago(monto, vehiculo, cliente, tipoMembresia);
        historialPagos.add(pago);
        
        return true;
    }

    /**
     * Calcula el monto a pagar por una membresía según el tipo de vehículo y período
     */
    public double calcularTarifaMembresia(Vehiculo vehiculo, TipoMembresia tipoMembresia) {
        if (vehiculo == null || tipoMembresia == null) {
            return 0;
        }
        
        if (vehiculo instanceof Automovil) {
            switch (tipoMembresia) {
                case MENSUAL: return TARIFA_MENSUAL_AUTO;
                case TRIMESTRAL: return TARIFA_TRIMESTRAL_AUTO;
                case ANUAL: return TARIFA_ANUAL_AUTO;
                default: return 0;
            }
        } else if (vehiculo instanceof Moto) {
            switch (tipoMembresia) {
                case MENSUAL: return TARIFA_MENSUAL_MOTO;
                case TRIMESTRAL: return TARIFA_TRIMESTRAL_MOTO;
                case ANUAL: return TARIFA_ANUAL_MOTO;
                default: return 0;
            }
        } else if (vehiculo instanceof Camion) {
            switch (tipoMembresia) {
                case MENSUAL: return TARIFA_MENSUAL_CAMION;
                case TRIMESTRAL: return TARIFA_TRIMESTRAL_CAMION;
                case ANUAL: return TARIFA_ANUAL_CAMION;
                default: return 0;
            }
        }
        return 0;
    }

    /**
     * Determina el tipo de vehículo como texto
     */
    public String determinarTipoVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null) {
            return "Desconocido";
        }
        
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
     * Busca un pago por su ID
     */
    public Pago buscarPagoPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        
        for (Pago pago : historialPagos) {
            if (pago.getId().equals(id)) {
                return pago;
            }
        }
        return null;
    }

    /**
     * Calcula monto a pagar según tipo de vehículo y horas
     */
    public double calcularMontoAPagar(String tipoVehiculo, int horas) {
        if (tipoVehiculo == null || horas <= 0) {
            return 0;
        }
        
        double tarifaHora = 0;
        
        switch (tipoVehiculo) {
            case "Automóvil": tarifaHora = 2000; break;
            case "Moto": tarifaHora = 1000; break;
            case "Camión": tarifaHora = 3000; break;
            default: return 0;
        }
        
        return tarifaHora * horas;
    }

    /**
     * Obtiene pagos filtrados por día
     */
    public List<Pago> obtenerPagosPorDia(LocalDate dia) {
        if (dia == null) {
            return new ArrayList<>();
        }
        
        return historialPagos.stream()
            .filter(pago -> pago.getFechaHora().toLocalDate().equals(dia))
            .collect(Collectors.toList());
    }

    /**
     * Obtiene pagos filtrados por mes y año
     */
    public List<Pago> obtenerPagosPorMes(int mes, int anio) {
        if (mes < 1 || mes > 12 || anio < 0) {
            return new ArrayList<>();
        }
        
        return historialPagos.stream()
            .filter(pago -> pago.getFechaHora().getMonthValue() == mes && 
                           pago.getFechaHora().getYear() == anio)
            .collect(Collectors.toList());
    }

    /**
     * Obtiene pagos filtrados por año
     */
    public List<Pago> obtenerPagosPorAnio(int anio) {
        if (anio < 0) {
            return new ArrayList<>();
        }
        
        return historialPagos.stream()
            .filter(pago -> pago.getFechaHora().getYear() == anio)
            .collect(Collectors.toList());
    }

    /**
     * Obtiene pagos filtrados por rango de fechas
     */
    public List<Pago> obtenerPagosPorRango(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null || fechaInicio.isAfter(fechaFin)) {
            return new ArrayList<>();
        }
        
        return historialPagos.stream()
            .filter(pago -> {
                LocalDate fechaPago = pago.getFechaHora().toLocalDate();
                return (fechaPago.isEqual(fechaInicio) || fechaPago.isAfter(fechaInicio)) && 
                       (fechaPago.isEqual(fechaFin) || fechaPago.isBefore(fechaFin));
            })
            .collect(Collectors.toList());
    }

    /**
     * Calcula totales para un reporte a partir de una lista de pagos
     */
    public ReporteTotales calcularTotalesReporte(List<Pago> pagos) {
        if (pagos == null || pagos.isEmpty()) {
            return new ReporteTotales(0, 0, 0, 0, 0);
        }
        
        double totalEstacionamiento = 0;
        double totalMembresias = 0;
        double totalAutomoviles = 0;
        double totalMotos = 0;
        double totalCamiones = 0;
        
        for (Pago pago : pagos) {
            if (pago.getConcepto().startsWith("Estacionamiento")) {
                totalEstacionamiento += pago.getMonto();
            } else {
                totalMembresias += pago.getMonto();
            }
            
            switch (pago.getTipoVehiculo()) {
                case "Automóvil": totalAutomoviles += pago.getMonto(); break;
                case "Moto": totalMotos += pago.getMonto(); break;
                case "Camión": totalCamiones += pago.getMonto(); break;
            }
        }
        
        return new ReporteTotales(
            totalEstacionamiento, 
            totalMembresias, 
            totalAutomoviles, 
            totalMotos, 
            totalCamiones
        );
    }
    
    public List<Pago> getHistorialPagos() {
        return new ArrayList<>(historialPagos);
    }
    
    // Clase interna para encapsular los resultados del reporte
    public static class ReporteTotales {
        private double totalEstacionamiento;
        private double totalMembresias;
        private double totalAutomoviles;
        private double totalMotos;
        private double totalCamiones;
        
        public ReporteTotales(double totalEstacionamiento, double totalMembresias, 
                             double totalAutomoviles, double totalMotos, double totalCamiones) {
            this.totalEstacionamiento = totalEstacionamiento;
            this.totalMembresias = totalMembresias;
            this.totalAutomoviles = totalAutomoviles;
            this.totalMotos = totalMotos;
            this.totalCamiones = totalCamiones;
        }
        
        public double getTotalEstacionamiento() {
            return totalEstacionamiento;
        }
        
        public double getTotalMembresias() {
            return totalMembresias;
        }
        
        public double getTotalAutomoviles() {
            return totalAutomoviles;
        }
        
        public double getTotalMotos() {
            return totalMotos;
        }
        
        public double getTotalCamiones() {
            return totalCamiones;
        }
        
        public double getTotalGeneral() {
            return totalEstacionamiento + totalMembresias;
        }
    }
}