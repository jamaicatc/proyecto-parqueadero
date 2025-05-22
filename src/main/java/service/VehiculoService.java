package service;

import model.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.BorderFactory;

public class VehiculoService {
    private Map<String, List<Vehiculo>> vehiculosPorCliente;
    private List<Vehiculo> listaGeneralVehiculos; // Lista general para guardar todos los vehículos

    public VehiculoService() {
        this.vehiculosPorCliente = new HashMap<>();
        this.listaGeneralVehiculos = new ArrayList<>(); // Inicialización de la lista general
    }

    // Método para registrar un vehículo
    public void registrarVehiculo(Cliente cliente) {
        if (cliente != null) {
            JTextField placaField = new JTextField();
            JTextField colorField = new JTextField();
            JTextField modeloField = new JTextField();

            // Opciones para el tipo de vehículo
            String[] tiposVehiculo = {"Automóvil", "Moto", "Camión"};
            JComboBox<String> tipoVehiculoCombo = new JComboBox<>(tiposVehiculo);

            // Cambiar el color del texto a negro
            tipoVehiculoCombo.setForeground(new java.awt.Color(0, 0, 0));
            // Configurar el fondo para mejor contraste
            tipoVehiculoCombo.setBackground(new java.awt.Color(255, 255, 255));

            Object[] campos = {
                    "Placa:", placaField,
                    "Color:", colorField,
                    "Modelo:", modeloField,
                    "Tipo de Vehículo:", tipoVehiculoCombo
            };

            int opcion = JOptionPane.showConfirmDialog(null, campos, "Registrar Vehículo", JOptionPane.OK_CANCEL_OPTION);

            if (opcion == JOptionPane.OK_OPTION) {
                String placa = placaField.getText().trim();
                String color = colorField.getText().trim();
                String modelo = modeloField.getText().trim();
                int tipoSeleccionado = tipoVehiculoCombo.getSelectedIndex();

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
                        // Crear el vehículo según el tipo seleccionado
                        Vehiculo nuevoVehiculo;
                        String tipoVehiculoStr = tiposVehiculo[tipoSeleccionado];

                        switch (tipoSeleccionado) {
                            case 0:
                                nuevoVehiculo = new Automovil(placa, color, modelo);
                                break;
                            case 1:
                                nuevoVehiculo = new Moto(placa, color, modelo);
                                break;
                            case 2:
                                nuevoVehiculo = new Camion(placa, color, modelo);
                                break;
                            default:
                                nuevoVehiculo = new Vehiculo(placa, color, modelo);
                                break;
                        }

                        listaVehiculos.add(nuevoVehiculo);
                        vehiculosPorCliente.put(cliente.getCedula(), listaVehiculos);
                        JOptionPane.showMessageDialog(null,
                                "Vehículo registrado exitosamente.\n" +
                                        "Tipo: " + tipoVehiculoStr + "\n" +
                                        "Placa: " + placa);
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Cliente no encontrado.");
        }
    }

    // Método sin parámetros para buscar un vehículo (usado desde el Main)
    public Vehiculo buscarVehiculo() {
        String placa = JOptionPane.showInputDialog(null, "Ingrese la placa del vehículo a buscar:");
        
        if (placa == null || placa.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Búsqueda cancelada.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        
        // Primero buscar en la lista general
        for (Vehiculo vehiculo : listaGeneralVehiculos) {
            if (vehiculo.getPlaca().equalsIgnoreCase(placa.trim())) {
                JOptionPane.showMessageDialog(null, "Vehículo encontrado:\nPlaca: " + vehiculo.getPlaca() +
                        "\nColor: " + vehiculo.getColor() + "\nModelo: " + vehiculo.getModelo());
                return vehiculo;
            }
        }
        
        // Si no se encuentra en la lista general, buscar en las listas por cliente
        for (List<Vehiculo> vehiculos : vehiculosPorCliente.values()) {
            for (Vehiculo vehiculo : vehiculos) {
                if (vehiculo.getPlaca().equalsIgnoreCase(placa.trim())) {
                    JOptionPane.showMessageDialog(null, "Vehículo encontrado:\nPlaca: " + vehiculo.getPlaca() +
                            "\nColor: " + vehiculo.getColor() + "\nModelo: " + vehiculo.getModelo());
                    return vehiculo;
                }
            }
        }
        
        JOptionPane.showMessageDialog(null, "Vehículo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
        return null;
    }

    // Método para buscar un vehículo por placa
    public Vehiculo buscarVehiculo(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            return null;
        }
        
        // Primero buscar en la lista general
        for (Vehiculo vehiculo : listaGeneralVehiculos) {
            if (vehiculo.getPlaca().equalsIgnoreCase(placa.trim())) {
                return vehiculo;
            }
        }
        
        // Si no se encuentra en la lista general, buscar en las listas por cliente
        for (List<Vehiculo> vehiculos : vehiculosPorCliente.values()) {
            for (Vehiculo vehiculo : vehiculos) {
                if (vehiculo.getPlaca().equalsIgnoreCase(placa.trim())) {
                    return vehiculo;
                }
            }
        }
        
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
                // Crear un modelo de tabla para mostrar los vehículos
                String[] columnNames = {"Placa", "Color", "Modelo"};
                Object[][] data = new Object[vehiculos.size()][3];
                
                // Llenar la tabla con los datos de los vehículos
                for (int i = 0; i < vehiculos.size(); i++) {
                    Vehiculo v = vehiculos.get(i);
                    data[i][0] = v.getPlaca();
                    data[i][1] = v.getColor();
                    data[i][2] = v.getModelo();
                }
                
                // Crear la tabla y aplicar algunas propiedades
                final JTable table = new JTable(data, columnNames);
                table.setFillsViewportHeight(true);
                table.setRowHeight(25); // Altura de filas más cómoda
                table.getTableHeader().setReorderingAllowed(false); // Evitar reordenar columnas
                table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Solo seleccionar una fila a la vez
                
                // Ajustar el ancho de las columnas
                table.getColumnModel().getColumn(0).setPreferredWidth(100); // Placa
                table.getColumnModel().getColumn(1).setPreferredWidth(150); // Color
                table.getColumnModel().getColumn(2).setPreferredWidth(200); // Modelo
                
                // Añadir listener para doble clic en una fila
                table.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) { // Doble clic
                            int row = table.getSelectedRow();
                            if (row >= 0 && row < vehiculos.size()) {
                                // Mostrar detalles del vehículo seleccionado
                                mostrarDetallesVehiculo(vehiculos.get(row));
                            }
                        }
                    }
                });
                
                // Crear un panel de desplazamiento y añadir la tabla
                JScrollPane scrollPane = new JScrollPane(table);
                scrollPane.setPreferredSize(new Dimension(450, Math.min(300, 50 + vehiculos.size() * 25))); // Altura dinámica según cantidad de vehículos
                
                // Crear un panel principal que contendrá el título, la tabla y las instrucciones
                JPanel panel = new JPanel(new BorderLayout(0, 10));
                panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                
                // Añadir un título al panel
                JLabel titulo = new JLabel("Vehículos asociados a " + cliente.getNombre());
                titulo.setFont(new Font("Dialog", Font.BOLD, 14));
                titulo.setHorizontalAlignment(JLabel.CENTER);
                panel.add(titulo, BorderLayout.NORTH);
                
                // Añadir la tabla con scroll al panel
                panel.add(scrollPane, BorderLayout.CENTER);
                
                // Añadir instrucciones para ver detalles
                JLabel instrucciones = new JLabel("Doble clic en un vehículo para ver más detalles");
                instrucciones.setFont(new Font("Dialog", Font.ITALIC, 12));
                instrucciones.setHorizontalAlignment(JLabel.CENTER);
                panel.add(instrucciones, BorderLayout.SOUTH);
                
                // Mostrar el panel en un diálogo
                JOptionPane.showMessageDialog(
                    null, 
                    panel, 
                    "Vehículos de " + cliente.getNombre(), 
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
            return vehiculos;
        } else {
            JOptionPane.showMessageDialog(null, "Cliente no encontrado.");
            return new ArrayList<>();
        }
    }

    /**
     * Muestra los detalles de un vehículo específico
     * @param vehiculo El vehículo del que se mostrarán los detalles
     */
    private void mostrarDetallesVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null) return;
        
        // Crear un panel para mostrar los detalles
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Añadir los campos con los detalles
        JLabel titulo = new JLabel("Detalles del Vehículo");
        titulo.setFont(new Font("Dialog", Font.BOLD, 16));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titulo);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Crear campos para cada detalle
        String[] etiquetas = {"Placa:", "Color:", "Modelo:"};
        String[] valores = {vehiculo.getPlaca(), vehiculo.getColor(), vehiculo.getModelo()};
        
        for (int i = 0; i < etiquetas.length; i++) {
            JPanel filaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            
            JLabel etiqueta = new JLabel(etiquetas[i]);
            etiqueta.setFont(new Font("Dialog", Font.BOLD, 14));
            etiqueta.setPreferredSize(new Dimension(80, 25));
            
            JLabel valor = new JLabel(valores[i]);
            valor.setFont(new Font("Dialog", Font.PLAIN, 14));
            
            filaPanel.add(etiqueta);
            filaPanel.add(valor);
            panel.add(filaPanel);
        }
        
        // Mostrar el panel en un diálogo
        JOptionPane.showMessageDialog(
            null, 
            panel, 
            "Detalles del Vehículo", 
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Obtiene la lista de vehículos asociados a un cliente específico
     * @param cliente Cliente del que se quieren obtener los vehículos
     * @return Lista de vehículos del cliente o una lista vacía si no tiene vehículos
     */
    public List<Vehiculo> obtenerVehiculosPorCliente(Cliente cliente) {
        if (cliente == null) {
            return new ArrayList<>();
        }

        String cedula = cliente.getCedula();
        if (cedula == null || cedula.trim().isEmpty()) {
            return new ArrayList<>();
        }

        List<Vehiculo> vehiculos = vehiculosPorCliente.get(cedula);
        return vehiculos != null ? new ArrayList<>(vehiculos) : new ArrayList<>();
    }
    
    /**
     * Obtiene todos los vehículos registrados en el sistema
     * @return Lista con todos los vehículos
     */
    public List<Vehiculo> obtenerTodosLosVehiculos() {
        return new ArrayList<>(listaGeneralVehiculos);
    }
}