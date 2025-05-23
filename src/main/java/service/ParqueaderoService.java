package service;

import model.Parqueadero;
import model.Vehiculo;
import model.Automovil;
import model.Moto;
import model.Camion;
import model.TipoMembresia;

import javax.swing.JOptionPane;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Dimension;

public class ParqueaderoService {
    private Parqueadero parqueadero;
    // Registro de vehículos actualmente en el parqueadero con su hora de entrada
    private Map<String, LocalDateTime> vehiculosActuales = new HashMap<>();
    // Tarifas por hora para cada tipo de vehículo
    private double tarifaHoraAutomovil = 2000;
    private double tarifaHoraMoto = 1000;
    private double tarifaHoraCamion = 3000;

    public ParqueaderoService(Parqueadero parqueadero) {
        this.parqueadero = parqueadero;
    }

    /**
     * Configura las capacidades del parqueadero
     */
    public void configurarEspacios() {
        try {
            String motosStr = JOptionPane.showInputDialog("Ingrese la cantidad de espacios para motos:");
            String automovilesStr = JOptionPane.showInputDialog("Ingrese la cantidad de espacios para automóviles:");
            String camionesStr = JOptionPane.showInputDialog("Ingrese la cantidad de espacios para camiones:");

            int motos = Integer.parseInt(motosStr);
            int automoviles = Integer.parseInt(automovilesStr);
            int camiones = Integer.parseInt(camionesStr);

            parqueadero.setPuestosMotos(motos);
            parqueadero.setPuestosAutomoviles(automoviles);
            parqueadero.setPuestosCamiones(camiones);

            JOptionPane.showMessageDialog(null,
                    "Configuración exitosa:\n" +
                            "Motos: " + motos + " espacios\n" +
                            "Automóviles: " + automoviles + " espacios\n" +
                            "Camiones: " + camiones + " espacios"
            );
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor ingrese números válidos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Configura las tarifas por hora para cada tipo de vehículo
     */
    public void configurarTarifas() {
        try {
            String tarifaMotoStr = JOptionPane.showInputDialog("Ingrese la tarifa por hora para motos:");
            String tarifaAutomovilStr = JOptionPane.showInputDialog("Ingrese la tarifa por hora para automóviles:");
            String tarifaCamionStr = JOptionPane.showInputDialog("Ingrese la tarifa por hora para camiones:");

            double tarifaMoto = Double.parseDouble(tarifaMotoStr);
            double tarifaAutomovil = Double.parseDouble(tarifaAutomovilStr);
            double tarifaCamion = Double.parseDouble(tarifaCamionStr);

            this.tarifaHoraMoto = tarifaMoto;
            this.tarifaHoraAutomovil = tarifaAutomovil;
            this.tarifaHoraCamion = tarifaCamion;

            JOptionPane.showMessageDialog(null,
                    "Tarifas configuradas exitosamente:\n" +
                            "Motos: $" + tarifaMoto + " por hora\n" +
                            "Automóviles: $" + tarifaAutomovil + " por hora\n" +
                            "Camiones: $" + tarifaCamion + " por hora"
            );
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor ingrese valores numéricos válidos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Registra la entrada de un vehículo temporal al parqueadero
     */
    /**
     * Registra la entrada de un vehículo temporal al parqueadero solicitando solo la placa
     */
    public void registrarEntradaVehiculo() {
        String[] tiposVehiculo = {"Automóvil", "Moto", "Camión"};

        int tipoSeleccionado = JOptionPane.showOptionDialog(
                null,
                "Seleccione el tipo de vehículo:",
                "Entrada de Vehículo",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                tiposVehiculo,
                tiposVehiculo[0]
        );

        if (tipoSeleccionado == JOptionPane.CLOSED_OPTION) {
            return;
        }

        // Verificar disponibilidad de espacios
        boolean hayEspacio = false;
        switch (tipoSeleccionado) {
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
            JOptionPane.showMessageDialog(null, "No hay espacios disponibles para este tipo de vehículo", "Sin espacio", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Recolectar solo la placa del vehículo
        String placa = JOptionPane.showInputDialog("Ingrese la placa del vehículo:");
        if (placa == null || placa.trim().isEmpty()) {
            return;
        }

        // Verificar si el vehículo ya está en el parqueadero
        if (vehiculosActuales.containsKey(placa)) {
            JOptionPane.showMessageDialog(null, "Este vehículo ya se encuentra en el parqueadero", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear el vehículo según el tipo, usando valores predeterminados para color y modelo
        Vehiculo vehiculo = null;
        switch (tipoSeleccionado) {
            case 0: // Automóvil
                vehiculo = new Automovil(placa, "No registrado", "No registrado");
                break;
            case 1: // Moto
                vehiculo = new Moto(placa, "No registrado", "No registrado");
                break;
            case 2: // Camión
                vehiculo = new Camion(placa, "No registrado", "No registrado");
                break;
        }

        // Registrar hora de entrada
        vehiculosActuales.put(placa, LocalDateTime.now());

        // Agregar a la lista de vehículos del parqueadero
        parqueadero.getListaDevehiculos().add(vehiculo);

        JOptionPane.showMessageDialog(null,
                "Vehículo registrado correctamente:\n" +
                        "Placa: " + placa + "\n" +
                        "Tipo: " + tiposVehiculo[tipoSeleccionado] + "\n" +
                        "Hora de entrada: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
    }

    /**
     * Registra la salida de un vehículo y calcula el monto a pagar
     */
    public void registrarSalidaVehiculo() {
        String placa = JOptionPane.showInputDialog("Ingrese la placa del vehículo que sale:");
        if (placa == null || placa.trim().isEmpty()) {
            return;
        }

        // Verificar si el vehículo está en el parqueadero
        if (!vehiculosActuales.containsKey(placa)) {
            JOptionPane.showMessageDialog(null, "Este vehículo no se encuentra registrado en el parqueadero", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Buscar el vehículo en la lista
        Vehiculo vehiculo = buscarVehiculoPorPlaca(placa);
        if (vehiculo == null) {
            JOptionPane.showMessageDialog(null, "Error al buscar el vehículo en el sistema", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Calcular tiempo de permanencia
        LocalDateTime horaEntrada = vehiculosActuales.get(placa);
        LocalDateTime horaSalida = LocalDateTime.now();
        long horasEstadia = java.time.Duration.between(horaEntrada, horaSalida).toHours();
        // Mínimo 1 hora
        if (horasEstadia < 1) {
            horasEstadia = 1;
        }

        // Verificar si tiene membresía activa
        if (vehiculo.getMembresia() != null && vehiculo.getMembresia() != TipoMembresia.NINGUNA) {
            JOptionPane.showMessageDialog(null,
                    "El vehículo tiene una membresía activa.\n" +
                            "No hay cargo adicional por estacionamiento."
            );

            // Remover vehículo de la lista de actuales
            vehiculosActuales.remove(placa);
            return;
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

        // Mostrar factura
        String factura =
                "FACTURA DE ESTACIONAMIENTO\n" +
                        "-------------------------\n" +
                        "Parqueadero: " + parqueadero.getNombre() + "\n" +
                        "Dirección: " + parqueadero.getDireccion() + "\n" +
                        "Teléfono: " + parqueadero.getTelefono() + "\n\n" +
                        "Placa Vehículo: " + placa + "\n" +
                        "Tipo: " + tipoVehiculo + "\n" +
                        "Hora de entrada: " + horaEntrada.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n" +
                        "Hora de salida: " + horaSalida.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n" +
                        "Tiempo de estancia: " + horasEstadia + " hora(s)\n\n" +
                        "Tarifa por hora: $" + String.format("%,.0f", tarifaHora) + "\n" +
                        "TOTAL A PAGAR: $" + String.format("%,.0f", montoTotal);

        JOptionPane.showMessageDialog(null, factura, "Factura de Estacionamiento", JOptionPane.INFORMATION_MESSAGE);

        // Remover vehículo de las listas
        vehiculosActuales.remove(placa);
        parqueadero.getListaDevehiculos().remove(vehiculo);

        JOptionPane.showMessageDialog(null, "Salida registrada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Muestra el estado actual de ocupación del parqueadero
     */
    public void mostrarEstadoParqueadero() {
        int motosActuales = contarVehiculosPorTipo(Moto.class);
        int automovilesActuales = contarVehiculosPorTipo(Automovil.class);
        int camionesActuales = contarVehiculosPorTipo(Camion.class);

        StringBuilder estado = new StringBuilder("ESTADO ACTUAL DEL PARQUEADERO\n");
        estado.append("---------------------------\n\n");

        estado.append("Motos: ").append(motosActuales).append("/").append(parqueadero.getPuestosMotos())
                .append(" (").append(parqueadero.getPuestosMotos() - motosActuales).append(" disponibles)\n");

        estado.append("Automóviles: ").append(automovilesActuales).append("/").append(parqueadero.getPuestosAutomoviles())
                .append(" (").append(parqueadero.getPuestosAutomoviles() - automovilesActuales).append(" disponibles)\n");

        estado.append("Camiones: ").append(camionesActuales).append("/").append(parqueadero.getPuestosCamiones())
                .append(" (").append(parqueadero.getPuestosCamiones() - camionesActuales).append(" disponibles)\n");

        estado.append("\nCapacidad Total: ").append(parqueadero.getPuestosMotos() + parqueadero.getPuestosAutomoviles() + parqueadero.getPuestosCamiones());
        estado.append("\nOcupación Actual: ").append(motosActuales + automovilesActuales + camionesActuales);

        JOptionPane.showMessageDialog(null, estado.toString(), "Estado del Parqueadero", JOptionPane.INFORMATION_MESSAGE);
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
    private Vehiculo buscarVehiculoPorPlaca(String placa) {
        for (Vehiculo vehiculo : parqueadero.getListaDevehiculos()) {
            if (vehiculo.getPlaca().equalsIgnoreCase(placa)) {
                return vehiculo;
            }
        }
        return null;
    }

    /**
     * Muestra la lista de vehículos actualmente en el parqueadero con paginación
     */
    public void listarVehiculosActuales() {
        if (vehiculosActuales.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay vehículos en el parqueadero actualmente");
            return;
        }

        // Convertir el mapa a una lista para facilitar la paginación
        List<Map.Entry<String, LocalDateTime>> listaVehiculos = new ArrayList<>(vehiculosActuales.entrySet());

        // Configuración de paginación
        final int ITEMS_POR_PAGINA = 5;
        int totalVehiculos = listaVehiculos.size();
        int totalPaginas = (int) Math.ceil((double) totalVehiculos / ITEMS_POR_PAGINA);
        int paginaActual = 1;

        boolean continuarPaginacion = true;

        while (continuarPaginacion) {
            // Calcular índices de inicio y fin para la página actual
            int indiceInicio = (paginaActual - 1) * ITEMS_POR_PAGINA;
            int indiceFin = Math.min(indiceInicio + ITEMS_POR_PAGINA, totalVehiculos);

            StringBuilder listado = new StringBuilder();
            listado.append("VEHÍCULOS ACTUALMENTE EN EL PARQUEADERO (Página ").append(paginaActual).append(" de ").append(totalPaginas).append(")\n");
            listado.append("----------------------------------------\n\n");

            // Mostrar los vehículos de la página actual
            for (int i = indiceInicio; i < indiceFin; i++) {
                Map.Entry<String, LocalDateTime> entry = listaVehiculos.get(i);
                String placa = entry.getKey();
                LocalDateTime horaEntrada = entry.getValue();
                Vehiculo vehiculo = buscarVehiculoPorPlaca(placa);

                if (vehiculo != null) {
                    String tipoVehiculo = obtenerTipoVehiculo(vehiculo);

                    listado.append("Vehículo ").append(i + 1).append(" de ").append(totalVehiculos).append("\n");
                    listado.append("Placa: ").append(placa).append("\n");
                    listado.append("Tipo: ").append(tipoVehiculo).append("\n");
                    listado.append("Color: ").append(vehiculo.getColor()).append("\n");
                    listado.append("Modelo: ").append(vehiculo.getModelo()).append("\n");
                    listado.append("Hora de entrada: ").append(horaEntrada.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");

                    // Verificar si tiene membresía
                    if (vehiculo.getMembresia() != null && vehiculo.getMembresia() != TipoMembresia.NINGUNA) {
                        listado.append("Membresía: ").append(vehiculo.getMembresia()).append("\n");
                        listado.append("Fecha fin membresía: ").append(vehiculo.getFechaFinMembresia()).append("\n");
                    } else {
                        listado.append("Sin membresía\n");
                    }

                    listado.append("----------------------------------------\n");
                }
            }

            // Crear panel de opciones de navegación
            Object[] opciones;
            if (totalPaginas <= 1) {
                opciones = new Object[]{"Cerrar"};
            } else if (paginaActual == 1) {
                opciones = new Object[]{"Siguiente", "Cerrar"};
            } else if (paginaActual == totalPaginas) {
                opciones = new Object[]{"Anterior", "Cerrar"};
            } else {
                opciones = new Object[]{"Anterior", "Siguiente", "Cerrar"};
            }

            // Crear un JTextArea para mostrar el contenido con scroll
            JTextArea textArea = new JTextArea(listado.toString());
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);

            // Crear un panel con scroll
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));

            // Mostrar el diálogo con opciones de paginación
            int seleccion = JOptionPane.showOptionDialog(
                    null,
                    scrollPane,
                    "Vehículos en el Parqueadero",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            // Procesar la selección
            if (seleccion == 0 && opciones[0].equals("Anterior")) {
                paginaActual--;
            } else if ((seleccion == 0 && opciones[0].equals("Siguiente")) ||
                    (seleccion == 1 && opciones[1].equals("Siguiente"))) {
                paginaActual++;
            } else {
                continuarPaginacion = false;
            }
        }
    }

    /**
     * Determina el tipo de vehículo basado en su instancia
     */
    private String obtenerTipoVehiculo(Vehiculo vehiculo) {
        if (vehiculo instanceof Automovil) {
            return "Automóvil";
        } else if (vehiculo instanceof Moto) {
            return "Moto";
        } else if (vehiculo instanceof Camion) {
            return "Camión";
        }
        return "Desconocido";
    }
}