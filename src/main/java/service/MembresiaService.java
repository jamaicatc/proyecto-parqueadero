package service;

import model.Cliente;
import model.Membresia;
import model.TipoMembresia;
import model.Vehiculo;
import model.Automovil;
import model.Moto;
import model.Camion;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Dimension;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MembresiaService {
    private PagoService pagoService;
    private ClienteService clienteService;
    private VehiculoService vehiculoService;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final int DIAS_PROXIMIDAD_VENCIMIENTO = 30; // Días para considerar una membresía próxima a vencer

    public MembresiaService() {
        this.pagoService = null; // Se inicializará después para evitar dependencia circular
    }

    /**
     * Establece el servicio de pagos - para evitar dependencia circular
     */
    public void setPagoService(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    /**
     * Establece el servicio de clientes
     */
    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * Establece el servicio de vehículos
     */
    public void setVehiculoService(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    public PagoService getPagoService() {
        return pagoService;
    }

    public ClienteService getClienteService() {
        return clienteService;
    }

    public VehiculoService getVehiculoService() {
        return this.vehiculoService;
    }

    /**
     * Registra una nueva membresía para un vehículo
     */
    public void registrarMembresia(Vehiculo vehiculo) {
        if (vehiculo == null) {
            JOptionPane.showMessageDialog(null, "Vehículo no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificar si ya tiene membresía activa
        if (vehiculo.getMembresia() != null && vehiculo.getMembresia() != TipoMembresia.NINGUNA) {
            String fechaFin = vehiculo.getFechaFinMembresia();
            int opcion = JOptionPane.showConfirmDialog(null,
                    "Este vehículo ya tiene una membresía " + vehiculo.getMembresia() +
                            " activa hasta " + fechaFin + ".\n¿Desea renovarla?",
                    "Membresía Existente", JOptionPane.YES_NO_OPTION);

            if (opcion == JOptionPane.YES_OPTION) {
                renovarMembresia(vehiculo);
            }
            return;
        }

        // Buscar cliente asociado al vehículo o solicitar datos del cliente
        Cliente cliente = buscarClienteParaMembresia();
        if (cliente == null) {
            return;
        }

        // Seleccionar tipo de membresía
        String[] tiposMembresiaStr = {"Mensual", "Trimestral", "Anual"};
        int tipoSeleccionado = JOptionPane.showOptionDialog(
                null,
                "Seleccione el tipo de membresía:",
                "Registro de Membresía",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                tiposMembresiaStr,
                tiposMembresiaStr[0]
        );

        if (tipoSeleccionado == JOptionPane.CLOSED_OPTION) {
            return;
        }

        TipoMembresia tipoMembresia;
        switch (tipoSeleccionado) {
            case 0: tipoMembresia = TipoMembresia.MENSUAL; break;
            case 1: tipoMembresia = TipoMembresia.TRIMESTRAL; break;
            case 2: tipoMembresia = TipoMembresia.ANUAL; break;
            default: return;
        }

        // Calcular fecha de fin según el tipo de membresía
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaFin;

        switch (tipoMembresia) {
            case MENSUAL: fechaFin = fechaInicio.plusMonths(1); break;
            case TRIMESTRAL: fechaFin = fechaInicio.plusMonths(3); break;
            case ANUAL: fechaFin = fechaInicio.plusYears(1); break;
            default: return;
        }

        // Registrar la membresía en el vehículo
        vehiculo.setMembresia(tipoMembresia);
        vehiculo.setFechaInicioMembresia(fechaInicio.format(FORMATTER));
        vehiculo.setFechaFinMembresia(fechaFin.format(FORMATTER));

        // AÑADIR ESTA NUEVA SECCIÓN: Crear y agregar la membresía al cliente
        Membresia membresia = new Membresia(
                tipoMembresia,
                fechaInicio.format(FORMATTER),
                fechaFin.format(FORMATTER),
                calcularTarifaMembresia(vehiculo, tipoMembresia)  // Usar un valor entero para la tarifa
        );
        cliente.getMembresias().add(membresia);

        // Registrar el pago si se ha configurado el pagoService
        if (pagoService != null) {
            pagoService.registrarPagoMembresia(vehiculo, cliente, tipoMembresia);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Membresía registrada exitosamente:\n" +
                            "Tipo: " + tipoMembresia + "\n" +
                            "Fecha de inicio: " + fechaInicio.format(FORMATTER) + "\n" +
                            "Fecha de fin: " + fechaFin.format(FORMATTER)
            );
        }
    }

    /**
     * Busca o solicita información del cliente para registrar la membresía
     */
    private Cliente buscarClienteParaMembresia() {
        // Si tenemos el servicio de cliente configurado
        if (clienteService != null) {
            // Solicitar al usuario si desea buscar un cliente existente o crear uno nuevo
            int opcion = JOptionPane.showConfirmDialog(null,
                    "¿Desea buscar un cliente existente?",
                    "Cliente para Membresía",
                    JOptionPane.YES_NO_OPTION);

            if (opcion == JOptionPane.YES_OPTION) {
                return clienteService.buscarCliente();
            } else {
                // Crear un cliente nuevo
                clienteService.añadirCliente();
                return clienteService.buscarCliente(); // Buscar el cliente recién creado
            }
        } else {
            // Si no tenemos el servicio de cliente, crear un objeto Cliente temporal
            String nombre = JOptionPane.showInputDialog("Ingrese el nombre del cliente:");
            if (nombre == null || nombre.trim().isEmpty()) {
                return null;
            }

            String cedula = JOptionPane.showInputDialog("Ingrese la cédula del cliente:");
            if (cedula == null || cedula.trim().isEmpty()) {
                return null;
            }

            String telefono = JOptionPane.showInputDialog("Ingrese el teléfono del cliente:");
            String correo = JOptionPane.showInputDialog("Ingrese el correo del cliente:");

            return new Cliente(nombre, cedula, telefono != null ? telefono : "", correo != null ? correo : "");
        }
    }

    /**
     * Verifica si una membresía está vigente
     */
    public void verificarVigencia(Vehiculo vehiculo) {
        if (vehiculo == null) {
            JOptionPane.showMessageDialog(null, "Vehículo no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (vehiculo.getMembresia() == null || vehiculo.getMembresia() == TipoMembresia.NINGUNA) {
            JOptionPane.showMessageDialog(null, "Este vehículo no tiene membresía registrada", "Sin Membresía", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Extraer fecha de fin y verificar vigencia
        try {
            String fechaFinStr = vehiculo.getFechaFinMembresia();
            LocalDate fechaFin = LocalDate.parse(fechaFinStr, FORMATTER);
            LocalDate hoy = LocalDate.now();

            if (fechaFin.isBefore(hoy)) {
                JOptionPane.showMessageDialog(null,
                        "La membresía de este vehículo ha VENCIDO.\n" +
                                "Tipo: " + vehiculo.getMembresia() + "\n" +
                                "Fecha de vencimiento: " + fechaFinStr + "\n" +
                                "Se recomienda renovar la membresía.",
                        "Membresía Vencida", JOptionPane.WARNING_MESSAGE
                );
            } else {
                long diasRestantes = java.time.temporal.ChronoUnit.DAYS.between(hoy, fechaFin);
                JOptionPane.showMessageDialog(null,
                        "La membresía de este vehículo está VIGENTE.\n" +
                                "Tipo: " + vehiculo.getMembresia() + "\n" +
                                "Fecha de vencimiento: " + fechaFinStr + "\n" +
                                "Días restantes: " + diasRestantes,
                        "Membresía Vigente", JOptionPane.INFORMATION_MESSAGE
                );
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al verificar la vigencia de la membresía.\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Renueva una membresía existente
     */
    public void renovarMembresia(Vehiculo vehiculo) {
        if (vehiculo == null) {
            JOptionPane.showMessageDialog(null, "Vehículo no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (vehiculo.getMembresia() == null || vehiculo.getMembresia() == TipoMembresia.NINGUNA) {
            JOptionPane.showMessageDialog(null,
                    "Este vehículo no tiene membresía para renovar.\n" +
                            "Por favor, registre una nueva membresía.",
                    "Sin Membresía", JOptionPane.INFORMATION_MESSAGE
            );
            registrarMembresia(vehiculo);
            return;
        }

        // Mostrar información actual y opciones de renovación
        try {
            String fechaFinStr = vehiculo.getFechaFinMembresia();
            TipoMembresia tipoActual = vehiculo.getMembresia();

            String mensaje = "Membresía actual:\n" +
                    "Tipo: " + tipoActual + "\n" +
                    "Vencimiento: " + fechaFinStr + "\n\n" +
                    "¿Desea renovar con el mismo tipo de membresía?";

            int opcion = JOptionPane.showConfirmDialog(null, mensaje, "Renovar Membresía", JOptionPane.YES_NO_CANCEL_OPTION);

            if (opcion == JOptionPane.YES_OPTION) {
                // Renovar con el mismo tipo
                renovarConMismoTipo(vehiculo, tipoActual);
            } else if (opcion == JOptionPane.NO_OPTION) {
                // Cambiar tipo de membresía
                cambiarTipoMembresia(vehiculo);
            }
            // Si es CANCEL_OPTION, simplemente se regresa
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al renovar la membresía.\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Renueva con el mismo tipo de membresía
     */
    private void renovarConMismoTipo(Vehiculo vehiculo, TipoMembresia tipo) {
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
            default: return;
        }

        // Actualizar datos en el vehículo
        vehiculo.setFechaInicioMembresia(fechaInicio.format(FORMATTER));
        vehiculo.setFechaFinMembresia(nuevaFechaFin.format(FORMATTER));

        // Buscar o solicitar cliente
        Cliente cliente = buscarClienteParaMembresia();
        if (cliente == null) {
            return;
        }

        // AÑADIR ESTA NUEVA SECCIÓN: Crear y agregar la membresía renovada al cliente
        Membresia membresia = new Membresia(
                tipo,
                fechaInicio.format(FORMATTER),
                nuevaFechaFin.format(FORMATTER),
                calcularTarifaMembresia(vehiculo, tipo)  // Usar un valor entero para la tarifa
        );
        cliente.getMembresias().add(membresia);

        // Registrar pago si está disponible el servicio
        if (pagoService != null) {
            pagoService.registrarPagoMembresia(vehiculo, cliente, tipo);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Membresía renovada exitosamente:\n" +
                            "Tipo: " + tipo + "\n" +
                            "Nueva fecha de inicio: " + fechaInicio.format(FORMATTER) + "\n" +
                            "Nueva fecha de vencimiento: " + nuevaFechaFin.format(FORMATTER)
            );
        }
    }

    /**
     * Permite cambiar el tipo de membresía durante la renovación
     */
    private void cambiarTipoMembresia(Vehiculo vehiculo) {
        String[] tiposMembresiaStr = {"Mensual", "Trimestral", "Anual"};
        int tipoSeleccionado = JOptionPane.showOptionDialog(
                null,
                "Seleccione el nuevo tipo de membresía:",
                "Cambiar Tipo de Membresía",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                tiposMembresiaStr,
                tiposMembresiaStr[0]
        );

        if (tipoSeleccionado == JOptionPane.CLOSED_OPTION) {
            return;
        }

        TipoMembresia nuevoTipo;
        switch (tipoSeleccionado) {
            case 0: nuevoTipo = TipoMembresia.MENSUAL; break;
            case 1: nuevoTipo = TipoMembresia.TRIMESTRAL; break;
            case 2: nuevoTipo = TipoMembresia.ANUAL; break;
            default: return;
        }

        // Actualizar tipo de membresía
        vehiculo.setMembresia(nuevoTipo);

        // Renovar con el nuevo tipo
        renovarConMismoTipo(vehiculo, nuevoTipo);
    }


    /**
     * Genera un reporte de clientes con membresías activas y próximas a vencer
     */
    public void generarReporteMembresiasActivas() {
        if (clienteService == null || vehiculoService == null) {
            JOptionPane.showMessageDialog(null,
                    "No se puede generar el reporte sin los servicios de cliente y vehículo",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Estructuras para almacenar resultados
        Map<Cliente, List<Vehiculo>> clientesConMembresiasActivas = new HashMap<>();
        Map<Cliente, List<Vehiculo>> clientesConMembresiasProximasAVencer = new HashMap<>();

        try {
            // Obtener todos los clientes (suponemos que existe este método)
            List<Cliente> clientes = clienteService.obtenerTodosLosClientes();

            if (clientes == null || clientes.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "No hay clientes registrados en el sistema",
                        "Sin Datos", JOptionPane.INFORMATION_MESSAGE);
                return;
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

            // Generar y mostrar el reporte
            mostrarReporteMembresias(clientesConMembresiasActivas, clientesConMembresiasProximasAVencer);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al generar el reporte: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Muestra el reporte de membresías activas y próximas a vencer
     */
    private void mostrarReporteMembresias(
            Map<Cliente, List<Vehiculo>> clientesConMembresiasActivas,
            Map<Cliente, List<Vehiculo>> clientesConMembresiasProximasAVencer) {

        if (clientesConMembresiasActivas.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "No se encontraron clientes con membresías activas",
                    "Sin Datos", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Crear el reporte
        StringBuilder reporte = new StringBuilder();
        reporte.append("REPORTE DE CLIENTES CON MEMBRESÍAS ACTIVAS\n");
        reporte.append("===========================================\n\n");

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

        // Sección de resumen
        reporte.append("RESUMEN:\n");
        reporte.append("------------------------------------------\n");
        reporte.append("Total de clientes con membresías activas: ").append(totalClientes).append("\n");
        reporte.append("Total de vehículos con membresías activas: ").append(totalVehiculos).append("\n");
        reporte.append("Total de membresías próximas a vencer: ").append(totalProximosVencer).append("\n\n");

        // Sección de membresías próximas a vencer
        if (!clientesConMembresiasProximasAVencer.isEmpty()) {
            reporte.append("MEMBRESÍAS PRÓXIMAS A VENCER (").append(DIAS_PROXIMIDAD_VENCIMIENTO).append(" días o menos):\n");
            reporte.append("------------------------------------------\n");

            for (Map.Entry<Cliente, List<Vehiculo>> entry : clientesConMembresiasProximasAVencer.entrySet()) {
                Cliente cliente = entry.getKey();
                List<Vehiculo> vehiculos = entry.getValue();

                reporte.append("Cliente: ").append(cliente.getNombre()).append(" (Cédula: ").append(cliente.getCedula()).append(")\n");
                reporte.append("Contacto: ").append(cliente.getTelefono()).append(" / ").append(cliente.getCorreo()).append("\n");
                reporte.append("Vehículos con membresías próximas a vencer:\n");

                for (Vehiculo vehiculo : vehiculos) {
                    try {
                        LocalDate fechaFin = LocalDate.parse(vehiculo.getFechaFinMembresia(), FORMATTER);
                        long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), fechaFin);

                        reporte.append("  - Placa: ").append(vehiculo.getPlaca())
                                .append(" | Tipo: ").append(vehiculo.getClass().getSimpleName())
                                .append(" | Membresía: ").append(vehiculo.getMembresia())
                                .append(" | Vence: ").append(vehiculo.getFechaFinMembresia())
                                .append(" (").append(diasRestantes).append(" días restantes) ⚠️\n");
                    } catch (Exception e) {
                        reporte.append("  - Placa: ").append(vehiculo.getPlaca())
                                .append(" (Error al procesar fecha)\n");
                    }
                }
                reporte.append("\n");
            }
            reporte.append("\n");
        }

        // Sección de todas las membresías activas
        reporte.append("DETALLE DE TODOS LOS CLIENTES CON MEMBRESÍAS ACTIVAS:\n");
        reporte.append("------------------------------------------\n");

        for (Map.Entry<Cliente, List<Vehiculo>> entry : clientesConMembresiasActivas.entrySet()) {
            Cliente cliente = entry.getKey();
            List<Vehiculo> vehiculos = entry.getValue();

            reporte.append("Cliente: ").append(cliente.getNombre()).append(" (Cédula: ").append(cliente.getCedula()).append(")\n");
            reporte.append("Contacto: ").append(cliente.getTelefono()).append(" / ").append(cliente.getCorreo()).append("\n");
            reporte.append("Vehículos con membresías activas:\n");

            for (Vehiculo vehiculo : vehiculos) {
                try {
                    LocalDate fechaFin = LocalDate.parse(vehiculo.getFechaFinMembresia(), FORMATTER);
                    long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), fechaFin);

                    String simbolo = diasRestantes <= DIAS_PROXIMIDAD_VENCIMIENTO ? " ⚠️" : "";

                    reporte.append("  - Placa: ").append(vehiculo.getPlaca())
                            .append(" | Tipo: ").append(vehiculo.getClass().getSimpleName())
                            .append(" | Membresía: ").append(vehiculo.getMembresia())
                            .append(" | Vence: ").append(vehiculo.getFechaFinMembresia())
                            .append(" (").append(diasRestantes).append(" días restantes)")
                            .append(simbolo).append("\n");
                } catch (Exception e) {
                    reporte.append("  - Placa: ").append(vehiculo.getPlaca())
                            .append(" (Error al procesar fecha)\n");
                }
            }
            reporte.append("\n");
        }

        // Mostrar el reporte en un JTextArea con scroll
        JTextArea textArea = new JTextArea(reporte.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(700, 500));

        JOptionPane.showMessageDialog(null, scrollPane, "Reporte de Membresías Activas", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Calcula la tarifa de membresía según el tipo de vehículo y membresía
     */
    private int calcularTarifaMembresia(Vehiculo vehiculo, TipoMembresia tipo) {
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
}