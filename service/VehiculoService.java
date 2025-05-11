package service;

import model.Cliente;
import model.Vehiculo;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehiculoService {
    private Map<String, List<Vehiculo>> vehiculosPorCliente;

    public VehiculoService() {
        this.vehiculosPorCliente = new HashMap<>();
    }

    // Método para registrar un vehículo
    public void registrarVehiculo(Cliente cliente) {
        if (cliente != null) {
            JTextField placaField = new JTextField();
            JTextField colorField = new JTextField();
            JTextField modeloField = new JTextField();

            Object[] campos = {
                "Placa:", placaField,
                "Color:", colorField,
                "Modelo:", modeloField
            };

            int opcion = JOptionPane.showConfirmDialog(null, campos, "Registrar Vehículo", JOptionPane.OK_CANCEL_OPTION);

            if (opcion == JOptionPane.OK_OPTION) {
                String placa = placaField.getText().trim();
                String color = colorField.getText().trim();
                String modelo = modeloField.getText().trim();

                if (placa.isEmpty() || color.isEmpty() || modelo.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, llene todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Obtener la lista de vehículos del cliente
                    List<Vehiculo> listaVehiculos = vehiculosPorCliente.getOrDefault(cliente.getCedula(), new ArrayList<>());

                    // Verificar si ya existe la placa
                    boolean placaExiste = listaVehiculos.stream().anyMatch(v -> v.getPlaca().equalsIgnoreCase(placa));

                    if (placaExiste) {
                        JOptionPane.showMessageDialog(null, "Ya existe un vehículo con esa placa.");
                    } else {
                        Vehiculo nuevoVehiculo = new Vehiculo(placa, color, modelo);
                        listaVehiculos.add(nuevoVehiculo);
                        vehiculosPorCliente.put(cliente.getCedula(), listaVehiculos);
                        JOptionPane.showMessageDialog(null, "Vehículo registrado exitosamente.");
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Cliente no encontrado.");
        }
    }

    // Método para buscar un vehículo por placa
    public Vehiculo buscarVehiculo(String placa) {
        for (List<Vehiculo> vehiculos : vehiculosPorCliente.values()) {
            for (Vehiculo vehiculo : vehiculos) {
                if (vehiculo.getPlaca().equalsIgnoreCase(placa)) {
                    JOptionPane.showMessageDialog(null, "Vehículo encontrado:\nPlaca: " + vehiculo.getPlaca() +
                            "\nColor: " + vehiculo.getColor() + "\nModelo: " + vehiculo.getModelo());
                    return vehiculo;
                }
            }
        }
        JOptionPane.showMessageDialog(null, "Vehículo no encontrado.");
        return null;
    }

    // Método para actualizar un vehículo
    public void actualizarVehiculo(String placa) {
        Vehiculo vehiculo = buscarVehiculo(placa);

        if (vehiculo != null) {
            String[] opciones = {"Cambiar Placa", "Cambiar Color", "Cambiar Modelo"};
            boolean continuar = true;

            while (continuar) {
                int opcion = JOptionPane.showOptionDialog(null,
                        "Seleccione qué desea actualizar:\n\nPlaca: " + vehiculo.getPlaca() +
                                "\nColor: " + vehiculo.getColor() + "\nModelo: " + vehiculo.getModelo(),
                        "Actualizar Vehículo",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

                switch (opcion) {
                    case 0 -> vehiculo.setPlaca(JOptionPane.showInputDialog("Ingrese la nueva placa:", vehiculo.getPlaca()));
                    case 1 -> vehiculo.setColor(JOptionPane.showInputDialog("Ingrese el nuevo color:", vehiculo.getColor()));
                    case 2 -> vehiculo.setModelo(JOptionPane.showInputDialog("Ingrese el nuevo modelo:", vehiculo.getModelo()));
                    default -> continuar = false;
                }

                int respuesta = JOptionPane.showConfirmDialog(null, "¿Desea actualizar otro dato?");
                if (respuesta != JOptionPane.YES_OPTION) {
                    continuar = false;
                }
            }
        }
    }

    // Método para ver vehículos asociados a un cliente
    public List<Vehiculo> verVehiculosAsociados(Cliente cliente) {
        if (cliente != null) {
            List<Vehiculo> vehiculos = vehiculosPorCliente.getOrDefault(cliente.getCedula(), new ArrayList<>());

            if (vehiculos.isEmpty()) {
                JOptionPane.showMessageDialog(null, "El cliente no tiene vehículos asociados.");
            } else {
                StringBuilder info = new StringBuilder("Vehículos asociados a " + cliente.getNombre() + ":\n");
                for (Vehiculo v : vehiculos) {
                    info.append("- Placa: ").append(v.getPlaca())
                            .append(", Color: ").append(v.getColor())
                            .append(", Modelo: ").append(v.getModelo()).append("\n");
                }
                JOptionPane.showMessageDialog(null, info.toString());
            }
            return vehiculos;
        } else {
            JOptionPane.showMessageDialog(null, "Cliente no encontrado.");
            return new ArrayList<>();
        }
    }
}