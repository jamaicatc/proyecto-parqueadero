package service;

import model.Cliente;
import model.Pago;
import model.TipoMembresia;
import model.Vehiculo;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class PagoService {
    private List<Pago> pagos; // Lista para almacenar los pagos registrados
    private ClienteService clienteService;
    private VehiculoService vehiculoService;

    // Constructor
    public PagoService(ClienteService clienteService, VehiculoService vehiculoService) {
        this.pagos = new ArrayList<>();
        this.clienteService = clienteService;
        this.vehiculoService = vehiculoService;
    }

    // Método para registrar un pago
    public void registrarPago() {
        // Paso 1: Buscar al cliente
        Cliente cliente = clienteService.buscarCliente();
        if (cliente == null) {
            JOptionPane.showMessageDialog(null, "No se encontró el cliente. Registro de pago cancelado.");
            return;
        }

        // Paso 2: Ver vehículos asociados al cliente
        List<Vehiculo> vehiculos = vehiculoService.verVehiculosAsociados(cliente);
        if (vehiculos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El cliente no tiene vehículos asociados. Registro de pago cancelado.");
            return;
        }

        // Mostrar los vehículos asociados y seleccionar uno
        String[] opcionesVehiculos = vehiculos.stream()
                .map(v -> "Placa: " + v.getPlaca() + ", Modelo: " + v.getModelo())
                .toArray(String[]::new);

        int seleccion = JOptionPane.showOptionDialog(
                null,
                "Seleccione el vehículo para el pago:",
                "Vehículos Asociados",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcionesVehiculos,
                opcionesVehiculos[0]
        );

        if (seleccion == JOptionPane.CLOSED_OPTION) {
            JOptionPane.showMessageDialog(null, "Registro de pago cancelado.");
            return;
        }

        Vehiculo vehiculoSeleccionado = vehiculos.get(seleccion);

        // Paso 3: Solicitar el monto del pago
        double monto = 0.0;
        while (true) {
            String montoStr = JOptionPane.showInputDialog("Ingrese el monto del pago:");
            try {
                monto = Double.parseDouble(montoStr);
                if (monto <= 0) {
                    JOptionPane.showMessageDialog(null, "El monto debe ser mayor a 0. Inténtelo nuevamente.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Monto no válido. Inténtelo nuevamente.");
            }
        }

        // Paso 4: Crear el pago
        String idPago = "PAGO-" + (pagos.size() + 1); // Generar un ID único
        String fechaPago = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String descripcion = JOptionPane.showInputDialog("Ingrese una descripción para el pago:");

        if (descripcion == null || descripcion.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Descripción no válida. Registro de pago cancelado.");
            return;
        }

        Pago nuevoPago = new Pago(
                idPago,
                cliente,
                vehiculoSeleccionado,
                monto, // Asignar el monto ingresado
                fechaPago,
                "Efectivo", // Método de pago por defecto
                "Completado",
                descripcion
        );

        // Agregar el pago a la lista
        pagos.add(nuevoPago);

        // Paso 5: Mostrar la información del pago
        String mensaje = "Pago registrado exitosamente:\n" +
                "ID Pago: " + nuevoPago.getIdPago() + "\n" +
                "Cliente: " + cliente.getNombre() + "\n" +
                "Vehículo: " + vehiculoSeleccionado.getPlaca() + "\n" +
                "Fecha de Pago: " + nuevoPago.getFechaPago() + "\n" +
                "Monto: $" + new DecimalFormat("#,###").format(nuevoPago.getMonto()) + "\n" +
                "Descripción: " + nuevoPago.getDescripcion();

        JOptionPane.showMessageDialog(null, mensaje);
    }

    // Método para buscar un pago por ID
    public void buscarPagoPorId(String idPago) {
        for (Pago pago : pagos) {
            if (pago.getIdPago().equalsIgnoreCase(idPago)) {
                String mensaje = "Pago encontrado:\n" +
                        "ID Pago: " + pago.getIdPago() + "\n" +
                        "Cliente: " + pago.getCliente().getNombre() + "\n" +
                        "Vehículo (Placa): " + pago.getVehiculo().getPlaca() + "\n" +
                        "Fecha de Pago: " + pago.getFechaPago() + "\n" +
                        "Monto: $" + new DecimalFormat("#,###").format(pago.getMonto()) + "\n" +
                        "Descripción: " + pago.getDescripcion();
                JOptionPane.showMessageDialog(null, mensaje);
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "No se encontró ningún pago con el ID: " + idPago);
    }

    public void calcularMontoAPagar() {
        // Paso 1: Buscar al cliente
        Cliente cliente = clienteService.buscarCliente();
        if (cliente == null) {
            JOptionPane.showMessageDialog(null, "No se encontró el cliente. Cálculo de monto cancelado.");
            return;
        }
    
        // Paso 2: Ver vehículos asociados al cliente
        List<Vehiculo> vehiculos = vehiculoService.verVehiculosAsociados(cliente);
        if (vehiculos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El cliente no tiene vehículos asociados. Cálculo de monto cancelado.");
            return;
        }
    
        // Mostrar los vehículos asociados y seleccionar uno
        String[] opcionesVehiculos = vehiculos.stream()
                .map(v -> "Placa: " + v.getPlaca() + ", Modelo: " + v.getModelo())
                .toArray(String[]::new);
    
        int seleccion = JOptionPane.showOptionDialog(
                null,
                "Seleccione el vehículo para calcular el monto a pagar:",
                "Vehículos Asociados",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcionesVehiculos,
                opcionesVehiculos[0]
        );
    
        if (seleccion == JOptionPane.CLOSED_OPTION) {
            JOptionPane.showMessageDialog(null, "Cálculo de monto cancelado.");
            return;
        }
    
        Vehiculo vehiculoSeleccionado = vehiculos.get(seleccion);
    
        // Paso 3: Determinar el monto a pagar según la membresía
        TipoMembresia membresia = vehiculoSeleccionado.getMembresia();
        double montoAPagar = 0.0;
    
        switch (membresia) {
            case ANUAL -> montoAPagar = 500000; // Ejemplo: tarifa fija para membresía anual
            case TRIMESTRAL -> montoAPagar = 150000; // Ejemplo: tarifa fija para membresía trimestral
            case MENSUAL -> montoAPagar = 50000; // Ejemplo: tarifa fija para membresía mensual
            case NINGUNA -> {
                // Si no tiene membresía, calcular por horas
                String horasStr = JOptionPane.showInputDialog("Ingrese el número de horas que el vehículo estuvo en el parqueadero:");
                try {
                    int horas = Integer.parseInt(horasStr);
                    montoAPagar = horas * 5000; // Ejemplo: tarifa por hora
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Número de horas no válido. Cálculo de monto cancelado.");
                    return;
                }
            }
        }
    
        // Formatear el monto a pagar
        DecimalFormat formato = new DecimalFormat("#,###");
        String montoFormateado = formato.format(montoAPagar);
    
        // Paso 4: Mostrar el monto total a pagar
        String mensaje = "Monto total a pagar:\n" +
                "Cliente: " + cliente.getNombre() + "\n" +
                "Vehículo (Placa): " + vehiculoSeleccionado.getPlaca() + "\n" +
                "Membresía: " + membresia + "\n" +
                "Monto: $" + montoFormateado;
    
        JOptionPane.showMessageDialog(null, mensaje);
    }
}