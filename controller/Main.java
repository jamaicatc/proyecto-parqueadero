package controller;

import javax.swing.JOptionPane;

import model.Cliente;
import model.Membresia;
import model.Parqueadero;
import model.Vehiculo;
import service.ClienteService;
import service.MembresiaService;
import service.PagoService;
import service.VehiculoService;

public class Main {
    public static void main(String[] args) {
        Parqueadero parque = new Parqueadero();
        ClienteService clienteService = new ClienteService();
        VehiculoService vehiculoService = new VehiculoService();
        MembresiaService membresiaService = new MembresiaService();
        PagoService pagoService = new PagoService(clienteService, vehiculoService);
        boolean continuar = true;

        while (continuar) {
            String[] opcionesPrincipales = {
                "Gestión de Clientes",
                "Gestión de Vehículos",
                "Gestion de Membresias",
                "Pagos",
                "Salir"
            };

            int opcionPrincipal = JOptionPane.showOptionDialog(
                null,
                "Selecciona una categoría:",
                "Sistema de Gestión de Parqueadero",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                opcionesPrincipales,
                opcionesPrincipales[0]
            );

            switch (opcionPrincipal) {

                // ------------------- GESTIÓN DE CLIENTES -------------------
                case 0 -> {
                    boolean volverMenuPrincipal = false;

                    while (!volverMenuPrincipal) {
                        String[] opcionesCliente = {
                            "Añadir Cliente",
                            "Buscar Cliente",
                            "Actualizar Cliente",
                            "Eliminar Cliente",
                            "Volver al menu principal"
                        };
                        
                        int seleccion = JOptionPane.showOptionDialog(
                            null,
                            "Opciones de Cliente:",
                            "Gestión de Clientes",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            opcionesCliente,
                            opcionesCliente[0]
                        );

                        switch (seleccion) {
                            case 0 -> clienteService.añadirCliente();
                            case 1 -> clienteService.buscarCliente();
                            case 2 -> clienteService.actualizarCliente();
                            case 3 -> clienteService.eliminarCliente();
                            case 4, JOptionPane.CLOSED_OPTION -> volverMenuPrincipal = true;
                            default -> JOptionPane.showMessageDialog(null, "Opción inválida.");
                        }
                    }


                }

                // ------------------- GESTIÓN DE VEHÍCULOS -------------------
                case 1 -> {
                    boolean volverMenuPrincipal = false;

                    while (!volverMenuPrincipal) {
                        String[] opcionesVehiculo = {
                                "Ver Vehículos Asociados a Cliente",
                                "Registrar Vehículo",
                                "Buscar Vehículo",
                                "Actualizar Vehículo",
                                "Cancelar"
                        };

                        int seleccion = JOptionPane.showOptionDialog(
                                null,
                                "Opciones de Vehículos:",
                                "Gestión de Vehículos",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                opcionesVehiculo,
                                opcionesVehiculo[0]);

                        switch (seleccion) {
                            case 0 -> {
                                Cliente cliente = clienteService.buscarCliente();
                                if (cliente != null) {
                                    vehiculoService.verVehiculosAsociados(cliente);
                                }
                            }
                            case 1 -> {
                                // Buscar cliente solo una vez
                                Cliente cliente = clienteService.buscarCliente();
                                if (cliente != null) {
                                    // Registrar vehículo si el cliente es válido
                                    vehiculoService.registrarVehiculo(cliente);
                                } else {
                                    JOptionPane.showMessageDialog(null,
                                            "No se pudo registrar el vehículo porque no se encontró un cliente.");
                                }
                            }
                            case 2 -> {
                                String placa = JOptionPane.showInputDialog("Ingrese la placa del vehículo:");
                                vehiculoService.buscarVehiculo(placa);
                            }
                            case 3 -> {
                                String placa = JOptionPane
                                        .showInputDialog("Ingrese la placa del vehículo a actualizar:");
                                vehiculoService.actualizarVehiculo(placa);
                            }
                            case 4, JOptionPane.CLOSED_OPTION -> volverMenuPrincipal = true;
                            default -> JOptionPane.showMessageDialog(null, "Opción inválida.");
                        }
                    }
                }

                // ------------------- GESTIÓN DE VEHÍCULOS -------------------

                case 2 -> {
                    boolean volverMenuPrincipal = false;

                    while (!volverMenuPrincipal) {
                        String[] opcionesMembresias = {
                            "Registrar Membresia",
                            "Verificar Vigencia",
                            "Renovar Membresia"
                        };
                        
                        int seleccion = JOptionPane.showOptionDialog(
                            null,
                            "Opciones de Membresias:",
                            "Gestión de Membresias",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            opcionesMembresias,
                            opcionesMembresias[0]
                        );

                        switch (seleccion) {
                            case 0 -> {
                                Cliente cliente = clienteService.buscarCliente();
                                if (cliente != null) {
                                    Vehiculo vehiculo = vehiculoService.buscarVehiculo(
                                            JOptionPane.showInputDialog("Ingrese la placa del vehículo:"));
                                    if (vehiculo != null) {
                                        membresiaService.registrarMembresia(vehiculo);
                                    }
                                }
                            }
    
                            case 1 -> {
                                Cliente cliente = clienteService.buscarCliente();
                                if (cliente != null) {
                                    Vehiculo vehiculo = vehiculoService.buscarVehiculo(
                                            JOptionPane.showInputDialog("Ingrese la placa del vehículo:"));
                                    if (vehiculo != null) {
                                        membresiaService.verificarVigencia(vehiculo);
                                    }
                                }  
                            }
    
                            case 2 -> {
                                Cliente cliente = clienteService.buscarCliente();
                                if (cliente != null) {
                                    Vehiculo vehiculo = vehiculoService.buscarVehiculo(
                                            JOptionPane.showInputDialog("Ingrese la placa del vehículo:"));
                                    if (vehiculo != null) {
                                        membresiaService.renovarMembresia(vehiculo);
                                    }
                                }
                            }

                            case 3, JOptionPane.CLOSED_OPTION -> volverMenuPrincipal = true;
                            default -> JOptionPane.showMessageDialog(null, "Opción inválida.");
                        }
                    }


                }

                // ------------------- GESTIÓN DE PAGOS -------------------

                case 3 -> {
                    boolean volverMenuPrincipal = false;

                    while (!volverMenuPrincipal) {
                        String[] opcionesPagos = {
                                "Registrar Pago",
                                "Calcular Monto a Pagar",
                                "Buscar Pagos",
                                "Volver al menú principal"
                        };

                        int seleccion = JOptionPane.showOptionDialog(
                                null,
                                "Opciones de Pagos:",
                                "Gestión de Pagos",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                opcionesPagos,
                                opcionesPagos[0]);

                        switch (seleccion) {
                            case 0 -> {
                                // Llamar al método registrarPago
                                pagoService.registrarPago();
                            }

                            case 1 -> {
                                // Llamar al método calcularMontoAPagar
                                pagoService.calcularMontoAPagar();
                            }

                            case 2 -> {
                                // Llamar al método buscarPagoPorId
                                String idPago = JOptionPane.showInputDialog("Ingrese el ID del pago que desea buscar:");
                                if (idPago != null && !idPago.trim().isEmpty()) {
                                    pagoService.buscarPagoPorId(idPago);
                                } else {
                                    JOptionPane.showMessageDialog(null, "ID de pago no válido.");
                                }
                            }

                            case 3, JOptionPane.CLOSED_OPTION -> volverMenuPrincipal = true;

                            default -> JOptionPane.showMessageDialog(null, "Opción inválida.");
                        }
                    }
                }

                // ------------------- SALIR -------------------
                case 4 -> {
                    JOptionPane.showMessageDialog(null, "Gracias por usar el sistema. ¡Hasta luego!");
                    continuar = false;
                }

                // ------------------- DEFAULT -------------------
                default -> JOptionPane.showMessageDialog(null, "Opción inválida.");
            }
        }
    }
}

