package service;

import model.Cliente;
import model.Pago;
import model.TipoMembresia;
import model.Vehiculo;
import model.Automovil;
import model.Moto;
import model.Camion;
import model.Membresia;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PagoService {
    private ClienteService clienteService;
    private VehiculoService vehiculoService;
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

    public PagoService(ClienteService clienteService, VehiculoService vehiculoService) {
        this.clienteService = clienteService;
        this.vehiculoService = vehiculoService;
        this.historialPagos = new ArrayList<>();
    }

    /**
     * Registra un pago por estacionamiento temporal
     */
    public void registrarPago() {
        String placa = JOptionPane.showInputDialog("Ingrese la placa del vehículo:");
        if (placa == null || placa.trim().isEmpty()) {
            return;
        }

        Vehiculo vehiculo = vehiculoService.buscarVehiculo(placa);
        if (vehiculo == null) {
            JOptionPane.showMessageDialog(null, "Vehículo no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double monto = calcularMontoAPagar();
        if (monto <= 0) {
            return;
        }

        String tipoVehiculo = determinarTipoVehiculo(vehiculo);
        Pago pago = new Pago(monto, vehiculo, tipoVehiculo);
        historialPagos.add(pago);

        JOptionPane.showMessageDialog(null,
                "Pago registrado exitosamente\n" +
                        "ID: " + pago.getId() + "\n" +
                        "Monto: $" + String.format("%,.0f", monto)
        );
    }

    /**
     * Registra un pago por membresía
     */
    public void registrarPagoMembresia(Vehiculo vehiculo, Cliente cliente, TipoMembresia tipoMembresia) {
        double monto = calcularTarifaMembresia(vehiculo, tipoMembresia);
        Pago pago = new Pago(monto, vehiculo, cliente, tipoMembresia);
        historialPagos.add(pago);

        JOptionPane.showMessageDialog(null,
                "Pago de membresía registrado exitosamente\n" +
                        "ID: " + pago.getId() + "\n" +
                        "Monto: $" + String.format("%,.0f", monto) + "\n" +
                        "Tipo: " + tipoMembresia
        );
    }

    /**
     * Calcula el monto a pagar por una membresía según el tipo de vehículo y período
     */
    public double calcularTarifaMembresia(Vehiculo vehiculo, TipoMembresia tipoMembresia) {
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
    private String determinarTipoVehiculo(Vehiculo vehiculo) {
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
        for (Pago pago : historialPagos) {
            if (pago.getId().equals(id)) {
                mostrarDetallePago(pago);
                return pago;
            }
        }
        JOptionPane.showMessageDialog(null, "Pago no encontrado con ID: " + id, "Error", JOptionPane.ERROR_MESSAGE);
        return null;
    }

    /**
     * Muestra detalle de un pago
     */
    private void mostrarDetallePago(Pago pago) {
        String mensaje =
                "DETALLE DE PAGO\n" +
                        "--------------------\n" +
                        "ID: " + pago.getId() + "\n" +
                        "Fecha: " + pago.getFechaHora().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n" +
                        "Concepto: " + pago.getConcepto() + "\n" +
                        "Monto: $" + String.format("%,.0f", pago.getMonto()) + "\n" +
                        "Vehículo: " + (pago.getVehiculo() != null ? pago.getVehiculo().getPlaca() : "N/A") + "\n" +
                        "Tipo: " + pago.getTipoVehiculo() + "\n" +
                        "Cliente: " + (pago.getCliente() != null ? pago.getCliente().getNombre() : "N/A");

        JOptionPane.showMessageDialog(null, mensaje, "Detalle de Pago", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Permite calcular un monto a pagar
     */
    public double calcularMontoAPagar() {
        String[] tiposVehiculo = {"Automóvil", "Moto", "Camión"};
        int tipo = JOptionPane.showOptionDialog(null, "Seleccione el tipo de vehículo:", "Cálculo de Pago",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, tiposVehiculo, tiposVehiculo[0]);

        if (tipo == -1) return 0;

        String horasStr = JOptionPane.showInputDialog("Ingrese la cantidad de horas:");
        if (horasStr == null || horasStr.trim().isEmpty()) return 0;

        try {
            int horas = Integer.parseInt(horasStr);
            double tarifaHora = 0;

            switch (tipo) {
                case 0: tarifaHora = 2000; break; // Automóvil
                case 1: tarifaHora = 1000; break; // Moto
                case 2: tarifaHora = 3000; break; // Camión
            }

            double total = tarifaHora * horas;

            JOptionPane.showMessageDialog(null,
                    "Cálculo de pago:\n" +
                            "Vehículo: " + tiposVehiculo[tipo] + "\n" +
                            "Tarifa por hora: $" + String.format("%,.0f", tarifaHora) + "\n" +
                            "Horas: " + horas + "\n" +
                            "Total a pagar: $" + String.format("%,.0f", total)
            );

            return total;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor ingrese un número válido", "Error", JOptionPane.ERROR_MESSAGE);
            return 0;
        }
    }

    /**
     * Muestra el menú de reportes financieros
     */
    public void mostrarMenuReportes() {
        String[] opciones = {
                "Ingresos del Día",
                "Ingresos del Mes",
                "Ingresos del Año",
                "Ingresos por Rango de Fechas",
                "Volver"
        };

        int seleccion = JOptionPane.showOptionDialog(
                null,
                "Seleccione el tipo de reporte que desea generar:",
                "Reportes Financieros",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        switch (seleccion) {
            case 0: generarReporteDiario(); break;
            case 1: generarReporteMensual(); break;
            case 2: generarReporteAnual(); break;
            case 3: generarReportePorRango(); break;
            default: break; // Volver
        }
    }

    /**
     * Genera un reporte de ingresos del día actual
     */
    private void generarReporteDiario() {
        LocalDate hoy = LocalDate.now();

        List<Pago> pagosDia = historialPagos.stream()
                .filter(pago -> pago.getFechaHora().toLocalDate().equals(hoy))
                .collect(Collectors.toList());

        generarReporte(pagosDia, "REPORTE DE INGRESOS DEL DÍA " + hoy.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    /**
     * Genera un reporte de ingresos del mes actual o un mes específico
     */
    private void generarReporteMensual() {
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

        int mesActual = LocalDate.now().getMonthValue() - 1; // Índice base 0
        int anioActual = LocalDate.now().getYear();

        int mesSeleccionado = JOptionPane.showOptionDialog(
                null,
                "Seleccione el mes:",
                "Reporte Mensual",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                meses,
                meses[mesActual]
        );

        if (mesSeleccionado == -1) return;

        // Ajustar mes a base 1 para LocalDate
        int mes = mesSeleccionado + 1;

        List<Pago> pagosMes = historialPagos.stream()
                .filter(pago -> pago.getFechaHora().getMonthValue() == mes &&
                        pago.getFechaHora().getYear() == anioActual)
                .collect(Collectors.toList());

        generarReporte(pagosMes, "REPORTE DE INGRESOS DE " + meses[mesSeleccionado].toUpperCase() + " " + anioActual);
    }

    /**
     * Genera un reporte de ingresos del año actual o un año específico
     */
    private void generarReporteAnual() {
        int anioActual = LocalDate.now().getYear();
        String anioStr = JOptionPane.showInputDialog("Ingrese el año (YYYY):", anioActual);

        if (anioStr == null) return;

        try {
            int anio = Integer.parseInt(anioStr);

            List<Pago> pagosAnio = historialPagos.stream()
                    .filter(pago -> pago.getFechaHora().getYear() == anio)
                    .collect(Collectors.toList());

            generarReporte(pagosAnio, "REPORTE DE INGRESOS DEL AÑO " + anio);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor ingrese un año válido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Genera un reporte de ingresos por rango de fechas
     */
    private void generarReportePorRango() {
        // Fecha inicial
        String fechaInicioStr = JOptionPane.showInputDialog("Ingrese fecha de inicio (DD/MM/YYYY):");
        if (fechaInicioStr == null) return;

        // Fecha final
        String fechaFinStr = JOptionPane.showInputDialog("Ingrese fecha de fin (DD/MM/YYYY):");
        if (fechaFinStr == null) return;

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fechaInicio = LocalDate.parse(fechaInicioStr, formatter);
            LocalDate fechaFin = LocalDate.parse(fechaFinStr, formatter);

            // Validar que fechaInicio sea anterior o igual a fechaFin
            if (fechaInicio.isAfter(fechaFin)) {
                JOptionPane.showMessageDialog(null, "La fecha de inicio debe ser anterior o igual a la fecha de fin",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<Pago> pagosRango = historialPagos.stream()
                    .filter(pago -> {
                        LocalDate fechaPago = pago.getFechaHora().toLocalDate();
                        return (fechaPago.isEqual(fechaInicio) || fechaPago.isAfter(fechaInicio)) &&
                                (fechaPago.isEqual(fechaFin) || fechaPago.isBefore(fechaFin));
                    })
                    .collect(Collectors.toList());

            generarReporte(pagosRango, "REPORTE DE INGRESOS DEL " + fechaInicioStr + " AL " + fechaFinStr);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en el formato de fechas. Use DD/MM/YYYY",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Genera y muestra un reporte de ingresos basado en una lista de pagos
     */
    private void generarReporte(List<Pago> pagos, String titulo) {
        if (pagos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay pagos registrados en el período seleccionado",
                    "Sin Datos", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        double totalEstacionamiento = 0;
        double totalMembresias = 0;

        // Contadores por tipo de vehículo
        double totalAutomoviles = 0;
        double totalMotos = 0;
        double totalCamiones = 0;

        // Contadores por concepto
        for (Pago pago : pagos) {
            if (pago.getConcepto().startsWith("Estacionamiento")) {
                totalEstacionamiento += pago.getMonto();
            } else {
                totalMembresias += pago.getMonto();
            }

            // Contadores por tipo de vehículo
            switch (pago.getTipoVehiculo()) {
                case "Automóvil": totalAutomoviles += pago.getMonto(); break;
                case "Moto": totalMotos += pago.getMonto(); break;
                case "Camión": totalCamiones += pago.getMonto(); break;
            }
        }

        double totalGeneral = totalEstacionamiento + totalMembresias;

        // Crear el reporte
        StringBuilder reporte = new StringBuilder();
        reporte.append(titulo).append("\n");
        reporte.append("==========================================\n\n");

        reporte.append("RESUMEN DE INGRESOS\n");
        reporte.append("------------------------------------------\n");
        reporte.append("Ingresos por Estacionamiento: $").append(String.format("%.0f", totalEstacionamiento)).append("\n");
        reporte.append("Ingresos por Membresías: $").append(String.format("%.0f", totalMembresias)).append("\n");
        reporte.append("TOTAL GENERAL: $").append(String.format("%.0f", totalGeneral)).append("\n\n");

        reporte.append("DESGLOSE POR TIPO DE VEHÍCULO\n");
        reporte.append("------------------------------------------\n");
        reporte.append("Automóviles: $").append(String.format("%.0f", totalAutomoviles));
        if (totalGeneral > 0) {
            reporte.append(" (").append(String.format("%.1f", (totalAutomoviles/totalGeneral)*100)).append("%)");
        }
        reporte.append("\n");

        reporte.append("Motos: $").append(String.format("%.0f", totalMotos));
        if (totalGeneral > 0) {
            reporte.append(" (").append(String.format("%.1f", (totalMotos/totalGeneral)*100)).append("%)");
        }
        reporte.append("\n");

        reporte.append("Camiones: $").append(String.format("%.0f", totalCamiones));
        if (totalGeneral > 0) {
            reporte.append(" (").append(String.format("%.1f", (totalCamiones/totalGeneral)*100)).append("%)");
        }
        reporte.append("\n\n");

        reporte.append("DETALLE DE PAGOS\n");
        reporte.append("------------------------------------------\n");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (int i = 0; i < pagos.size(); i++) {
            Pago pago = pagos.get(i);
            reporte.append(i+1).append(". ");
            reporte.append("Fecha: ").append(pago.getFechaHora().format(formatter)).append(" | ");
            reporte.append("Concepto: ").append(pago.getConcepto()).append(" | ");
            reporte.append("Vehículo: ").append(pago.getTipoVehiculo()).append(" | ");
            if (pago.getVehiculo() != null) {
                reporte.append("Placa: ").append(pago.getVehiculo().getPlaca()).append(" | ");
            }
            reporte.append("Monto: $").append(String.format("%.0f", pago.getMonto())).append("\n");
        }

        // Mostrar el reporte en un JTextArea con scroll
        JTextArea textArea = new JTextArea(reporte.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(null, scrollPane, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Registra un pago por período (1 mes, 3 meses o 1 año)
     * Permite al cliente seleccionar el vehículo, el período y realiza el pago
     */
    public void registrarPagoPorPeriodo() {
        // Primero buscamos un cliente
        Cliente cliente = clienteService.buscarCliente();
        if (cliente == null) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un cliente válido para continuar.",
                    "Cliente no encontrado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener vehículos del cliente
        List<Vehiculo> vehiculosCliente = vehiculoService.obtenerVehiculosPorCliente(cliente);
        if (vehiculosCliente.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El cliente no tiene vehículos registrados. Por favor registre un vehículo primero.",
                    "Sin vehículos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Crear un array con las placas para mostrar en el selector
        String[] placas = new String[vehiculosCliente.size()];
        for (int i = 0; i < vehiculosCliente.size(); i++) {
            String tipoVehiculo = "";
            Vehiculo v = vehiculosCliente.get(i);

            if (v instanceof Automovil) {
                tipoVehiculo = "Automóvil";
            } else if (v instanceof Moto) {
                tipoVehiculo = "Moto";
            } else if (v instanceof Camion) {
                tipoVehiculo = "Camión";
            }

            placas[i] = v.getPlaca() + " - " + v.getModelo() + " (" + tipoVehiculo + ")";
        }

        // Seleccionar vehículo
        int seleccionVehiculo = JOptionPane.showOptionDialog(
                null,
                "Seleccione el vehículo para el pago de membresía:",
                "Selección de Vehículo",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                placas,
                placas[0]
        );

        if (seleccionVehiculo == JOptionPane.CLOSED_OPTION) {
            return;
        }

        Vehiculo vehiculoSeleccionado = vehiculosCliente.get(seleccionVehiculo);

        // Seleccionar período de membresía
        String[] periodos = {"1 Mes", "3 Meses", "1 Año"};
        int seleccionPeriodo = JOptionPane.showOptionDialog(
                null,
                "Seleccione el período de membresía:",
                "Período de Membresía",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                periodos,
                periodos[0]
        );

        if (seleccionPeriodo == JOptionPane.CLOSED_OPTION) {
            return;
        }

        // Convertir selección a TipoMembresia
        TipoMembresia tipoMembresia;
        switch (seleccionPeriodo) {
            case 0: tipoMembresia = TipoMembresia.MENSUAL; break;
            case 1: tipoMembresia = TipoMembresia.TRIMESTRAL; break;
            case 2: tipoMembresia = TipoMembresia.ANUAL; break;
            default: return; // No debería ocurrir
        }

        // Calcular fechas de inicio y fin
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaFin;

        switch (tipoMembresia) {
            case MENSUAL:
                fechaFin = fechaInicio.plusMonths(1);
                break;
            case TRIMESTRAL:
                fechaFin = fechaInicio.plusMonths(3);
                break;
            case ANUAL:
                fechaFin = fechaInicio.plusYears(1);
                break;
            default:
                return; // No debería ocurrir
        }

        // Formatear fechas para mostrar y guardar
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaInicioStr = fechaInicio.format(formatter);
        String fechaFinStr = fechaFin.format(formatter);

        // Calcular monto a pagar
        double monto = calcularTarifaMembresia(vehiculoSeleccionado, tipoMembresia);

        // Si el monto es 0, algo salió mal con el cálculo
        if (monto == 0) {
            // Detectar el tipo de vehículo para mostrar información de depuración
            String tipoVehiculo = "Desconocido";
            if (vehiculoSeleccionado instanceof Automovil) {
                tipoVehiculo = "Automóvil";
                // Asignar tarifa manualmente según el tipo de membresía
                switch (tipoMembresia) {
                    case MENSUAL: monto = TARIFA_MENSUAL_AUTO; break;
                    case TRIMESTRAL: monto = TARIFA_TRIMESTRAL_AUTO; break;
                    case ANUAL: monto = TARIFA_ANUAL_AUTO; break;
                }
            } else if (vehiculoSeleccionado instanceof Moto) {
                tipoVehiculo = "Moto";
                switch (tipoMembresia) {
                    case MENSUAL: monto = TARIFA_MENSUAL_MOTO; break;
                    case TRIMESTRAL: monto = TARIFA_TRIMESTRAL_MOTO; break;
                    case ANUAL: monto = TARIFA_ANUAL_MOTO; break;
                }
            } else if (vehiculoSeleccionado instanceof Camion) {
                tipoVehiculo = "Camión";
                switch (tipoMembresia) {
                    case MENSUAL: monto = TARIFA_MENSUAL_CAMION; break;
                    case TRIMESTRAL: monto = TARIFA_TRIMESTRAL_CAMION; break;
                    case ANUAL: monto = TARIFA_ANUAL_CAMION; break;
                }
            }

            JOptionPane.showMessageDialog(null,
                    "Se ha detectado un problema al calcular la tarifa.\n" +
                            "Tipo de vehículo detectado: " + tipoVehiculo + "\n" +
                            "Se utilizará la tarifa predeterminada para este tipo de vehículo.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
        }

        // Mostrar resumen antes de confirmar
        int confirmacion = JOptionPane.showConfirmDialog(
                null,
                "Confirme los detalles del pago:\n\n" +
                        "Cliente: " + cliente.getNombre() + "\n" +
                        "Vehículo: " + vehiculoSeleccionado.getPlaca() + " - " + vehiculoSeleccionado.getModelo() + "\n" +
                        "Tipo de membresía: " + tipoMembresia + "\n" +
                        "Fecha inicio: " + fechaInicioStr + "\n" +
                        "Fecha fin: " + fechaFinStr + "\n" +
                        "Monto a pagar: $" + String.format("%,.0f", monto) + "\n\n" +
                        "¿Desea confirmar el pago?",
                "Confirmar Pago",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(null, "Operación cancelada");
            return;
        }

        // Registrar el pago
        registrarPagoMembresia(vehiculoSeleccionado, cliente, tipoMembresia);

        // Crear y asignar membresía al vehículo
        Membresia membresia = new Membresia(tipoMembresia, fechaInicioStr, fechaFinStr, (int)monto);
        vehiculoSeleccionado.setMembresia(tipoMembresia);
        vehiculoSeleccionado.setFechaFinMembresia(fechaFinStr);

        JOptionPane.showMessageDialog(
                null,
                "Pago por período registrado exitosamente\n\n" +
                        membresia.toString(),
                "Pago Exitoso",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}