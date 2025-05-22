package controller;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import javax.swing.plaf.basic.BasicLabelUI;

import model.Cliente;
import model.Parqueadero;
import model.Vehiculo;
import model.Automovil;
import model.Moto;
import model.Camion;
import model.Vehiculo;
import service.ClienteService;
import service.MembresiaService;
import service.PagoService;
import service.ParqueaderoService;
import service.VehiculoService;

public class Main {
    // Definir colores para la interfaz
    private static final Color COLOR_PRIMARIO = new Color(53, 152, 220);       // Azul principal
    private static final Color COLOR_SECUNDARIO = new Color(230, 236, 240);    // Gris claro para fondos
    private static final Color COLOR_TEXTO = new Color(44, 62, 80);           // Azul oscuro para texto
    private static final Color COLOR_HIGHLIGHT = new Color(26, 188, 156);     // Verde para resaltar
    private static final Color COLOR_BACKGROUND = new Color(245, 246, 250);   // Blanco hueso para fondo

    public static void main(String[] args) {
        try {
            // 1. Primero establece el Look and Feel nativo
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // 2. Luego aplica tus personalizaciones que sobreescriben el Look and Feel
            personalizarUI();
        } catch (Exception e) {
            e.printStackTrace();
        }


        Parqueadero parque = new Parqueadero();
        ClienteService clienteService = new ClienteService();
        VehiculoService vehiculoService = new VehiculoService();
        PagoService pagoService = new PagoService(clienteService, vehiculoService);
        MembresiaService membresiaService = new MembresiaService();

        // Establecer referencias circulares
        membresiaService.setPagoService(pagoService);
        membresiaService.setClienteService(clienteService);

        ParqueaderoService parqueaderoService = new ParqueaderoService(parque);
        boolean continuar = true;

        while (continuar) {
            int opcion = mostrarMenuPrincipal();
            switch (opcion) {
                case 0 -> mostrarMenuClientes(clienteService);
                case 1 -> mostrarMenuVehiculos(clienteService, vehiculoService);
                case 2 -> mostrarMenuMembresias(clienteService, vehiculoService, membresiaService);
                case 3 -> mostrarMenuPagos(pagoService);
                case 4 -> mostrarMenuParqueadero(parque, parqueaderoService);
                case 5, -1 -> {
                    JOptionPane.showMessageDialog(null,
                            "Gracias por usar el sistema. ¡Hasta luego!",
                            "Saliendo del Sistema",
                            JOptionPane.INFORMATION_MESSAGE);
                    continuar = false;
                }
            }
        }
    }


    private static void personalizarUI() {
        // Registrar nuestro UI personalizado para botones
        UIManager.put("ButtonUI", BotonVerdeUI.class.getName());

        // Registrar UI personalizado para OptionPane
        UIManager.put("OptionPaneUI", OptionPaneVerdeUI.class.getName());

        // Registrar UI personalizado para Labels
        UIManager.put("LabelUI", LabelBlancoUI.class.getName());

        // Configuración de colores para toda la interfaz
        UIManager.put("OptionPane.background", COLOR_HIGHLIGHT);
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("Panel.background", COLOR_HIGHLIGHT);
        UIManager.put("Label.foreground", Color.WHITE);

        // Menús (configuración para evitar texto blanco en fondo blanco)
        UIManager.put("PopupMenu.background", COLOR_HIGHLIGHT);
        UIManager.put("PopupMenu.foreground", Color.WHITE);
        UIManager.put("PopupMenu.border", BorderFactory.createLineBorder(COLOR_HIGHLIGHT.darker()));
        UIManager.put("PopupMenu.opaque", Boolean.TRUE);

        UIManager.put("Menu.background", COLOR_HIGHLIGHT);
        UIManager.put("Menu.foreground", Color.WHITE);
        UIManager.put("Menu.selectionBackground", COLOR_HIGHLIGHT.darker());
        UIManager.put("Menu.selectionForeground", Color.WHITE);
        UIManager.put("Menu.opaque", Boolean.TRUE);

        UIManager.put("MenuItem.background", COLOR_HIGHLIGHT);
        UIManager.put("MenuItem.foreground", Color.WHITE);
        UIManager.put("MenuItem.selectionBackground", COLOR_HIGHLIGHT.darker());
        UIManager.put("MenuItem.selectionForeground", Color.WHITE);
        UIManager.put("MenuItem.opaque", Boolean.TRUE);

        // Configuraciones para la interfaz general
        UIManager.put("Button.focus", COLOR_HIGHLIGHT.darker());
        UIManager.put("Button.select", COLOR_HIGHLIGHT.darker().darker());

        // TextFields con fondo claro para que sean legibles
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", COLOR_TEXTO);
        UIManager.put("TextField.caretForeground", COLOR_PRIMARIO);
        UIManager.put("TextField.selectionBackground", COLOR_PRIMARIO);
        UIManager.put("TextField.selectionForeground", Color.WHITE);

        // JComboBox con colores adecuados
        UIManager.put("ComboBox.background", COLOR_HIGHLIGHT.brighter());
        UIManager.put("ComboBox.foreground", Color.WHITE);
        UIManager.put("ComboBox.selectionBackground", COLOR_HIGHLIGHT.darker());
        UIManager.put("ComboBox.selectionForeground", Color.WHITE);

        // Configuración para ComboBox.renderer
        UIManager.put("ComboBox.renderer", new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                c.setBackground(isSelected ? COLOR_HIGHLIGHT.darker() : COLOR_HIGHLIGHT);
                c.setForeground(Color.WHITE);
                return c;
            }
        });

        // Textos en español para los botones estándar
        UIManager.put("OptionPane.yesButtonText", "Sí");
        UIManager.put("OptionPane.noButtonText", "No");
        UIManager.put("OptionPane.okButtonText", "Aceptar");
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");

        // Personalización de bordes
        UIManager.put("OptionPane.border", BorderFactory.createEmptyBorder(10, 10, 10, 10));
        UIManager.put("OptionPane.messageAreaBorder", BorderFactory.createEmptyBorder(15, 15, 15, 15));
        UIManager.put("OptionPane.buttonAreaBorder", BorderFactory.createEmptyBorder(15, 0, 10, 0));
    }


    static class OptionPaneVerdeUI extends BasicOptionPaneUI {
        @Override
        protected void installDefaults() {
            super.installDefaults();
            UIManager.put("OptionPane.background", COLOR_HIGHLIGHT);
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Panel.background", COLOR_HIGHLIGHT);
        }
    }

    static class LabelBlancoUI extends BasicLabelUI {
        @Override
        public void installDefaults(JLabel c) {
            super.installDefaults(c);
            c.setForeground(Color.WHITE);
            c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        }
    }



    /**
     * Crea un botón personalizado para los diálogos
     */
    private static JButton createDialogButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(COLOR_PRIMARIO);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMargin(new Insets(8, 20, 8, 20));

        // Efectos al pasar el mouse
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(COLOR_HIGHLIGHT);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(COLOR_PRIMARIO);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(COLOR_HIGHLIGHT.darker());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(COLOR_HIGHLIGHT);
            }
        });

        return button;
    }

    /**
     * Muestra un JOptionPane con botones personalizados
     */
    private static int showCustomOptionDialog(Component parentComponent, Object message, String title,
                                              int optionType, int messageType, Icon icon,
                                              Object[] options, Object initialValue) {
        JOptionPane pane = new JOptionPane(message, messageType, optionType, icon, options, initialValue);
        pane.setBackground(COLOR_BACKGROUND);
        pane.setForeground(COLOR_TEXTO);

        // Reemplazar botones estándar con botones personalizados
        JPanel buttonPane = (JPanel) pane.getComponent(1); // La posición puede variar, verifica
        buttonPane.setBackground(COLOR_BACKGROUND);

        // Remover botones existentes y añadir botones personalizados
        buttonPane.removeAll();
        buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Buscar el índice del botón pulsado
                for (int i = 0; i < options.length; i++) {
                    if (e.getActionCommand().equals(options[i].toString())) {
                        pane.setValue(i);
                        break;
                    }
                }

                // Cerrar el diálogo
                JDialog dialog = (JDialog) SwingUtilities.getWindowAncestor((Component) e.getSource());
                dialog.dispose();
            }
        };

        // Crear y añadir botones personalizados
        for (Object option : options) {
            JButton button = createDialogButton(option.toString());
            button.setActionCommand(option.toString());
            button.addActionListener(actionListener);
            buttonPane.add(button);
        }

        // Crear y mostrar el diálogo
        JDialog dialog = pane.createDialog(parentComponent, title);
        dialog.setVisible(true);

        // Obtener el valor seleccionado
        Object selectedValue = pane.getValue();
        if (selectedValue == null || !(selectedValue instanceof Integer)) {
            return JOptionPane.CLOSED_OPTION;
        }
        return (Integer) selectedValue;
    }

    private static JButton crearBotonModerno(String texto, String icono) {
        JButton boton = new JButton(texto);

        // Configurar apariencia
        boton.setBackground(COLOR_PRIMARIO);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(true);
        boton.setOpaque(true);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Agregar ícono si se proporciona
        if (icono != null && !icono.isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(Main.class.getResource("/icons/" + icono));
                // Redimensionar icono si es necesario
                if (icon.getIconWidth() > 0) {  // Verificar que la imagen se cargó
                    Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                    icon = new ImageIcon(img);
                    boton.setIcon(icon);
                    boton.setIconTextGap(10);
                    boton.setHorizontalAlignment(SwingConstants.LEFT);
                }
            } catch (Exception e) {
                // Si no encuentra el ícono, continúa sin él
                System.err.println("No se pudo cargar el ícono: " + icono);
                // Simplemente continuar sin el ícono, sin deshabilitar el botón
                boton.setHorizontalAlignment(SwingConstants.CENTER);
            }
        } else {
            // Si no se proporciona ícono, centrar el texto
            boton.setHorizontalAlignment(SwingConstants.CENTER);
        }

        // Padding interno
        boton.setMargin(new Insets(10, 15, 10, 15));

        // Efectos al pasar el ratón
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(COLOR_HIGHLIGHT);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(COLOR_PRIMARIO);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                boton.setBackground(new Color(22, 160, 133)); // Verde más oscuro
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (boton.contains(e.getPoint())) {
                    boton.setBackground(COLOR_HIGHLIGHT);
                } else {
                    boton.setBackground(COLOR_PRIMARIO);
                }
            }
        });

        return boton;
    }




    static class BotonVerdeUI extends BasicButtonUI {
        @Override
        public void installDefaults(AbstractButton b) {
            super.installDefaults(b);
            b.setBackground(COLOR_HIGHLIGHT);
            b.setForeground(Color.WHITE);
            b.setFont(new Font("Segoe UI", Font.BOLD, 14));
            b.setOpaque(true);
            b.setBorderPainted(true);
            b.setFocusPainted(false);
            b.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_HIGHLIGHT.darker(), 1),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)
            ));
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton b = (AbstractButton) c;
            ButtonModel model = b.getModel();

            // Dibujar el fondo
            if (b.isOpaque()) {
                if (model.isPressed()) {
                    g.setColor(COLOR_HIGHLIGHT.darker());
                } else if (model.isRollover()) {
                    g.setColor(COLOR_HIGHLIGHT.brighter());
                } else {
                    g.setColor(COLOR_HIGHLIGHT);
                }
                g.fillRect(0, 0, c.getWidth(), c.getHeight());
            }

            super.paint(g, c);
        }
    }

    /**
     * Método auxiliar para personalizar un JOptionPane específico
     */
    public static void personalizarDialog(JDialog dialog) {
        // Personalizar el fondo del diálogo
        Container contentPane = dialog.getContentPane();
        if (contentPane instanceof JComponent) {
            JComponent component = (JComponent) contentPane;
            component.setBackground(COLOR_HIGHLIGHT);

            // Personalizar todos los componentes dentro del diálogo
            personalizarComponentes(component);
        }
    }

    /**
     * Personaliza todos los componentes dentro de un contenedor
     */
    private static void personalizarComponentes(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                panel.setBackground(COLOR_HIGHLIGHT);
                personalizarComponentes(panel);
            } else if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                label.setForeground(Color.WHITE);
                label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            } else if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                button.setBackground(COLOR_HIGHLIGHT);
                button.setForeground(Color.WHITE);
                button.setFont(new Font("Segoe UI", Font.BOLD, 14));
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_HIGHLIGHT.darker(), 1),
                        BorderFactory.createEmptyBorder(8, 15, 8, 15)
                ));
            } else if (comp instanceof Container) {
                personalizarComponentes((Container) comp);
            }
        }
    }

    /**
     * Muestra el menú principal con botones modernos
     */
    private static int mostrarMenuPrincipal() {
        // Panel principal con BoxLayout vertical
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_BACKGROUND);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Encabezado con título
        JLabel titulo = new JLabel("Sistema de Gestión de Parqueadero");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(COLOR_TEXTO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(titulo);

        // Logo o imagen decorativa (opcional)
        try {
            ImageIcon logoIcon = new ImageIcon(Main.class.getResource("/icons/parking_logo.png"));
            JLabel logo = new JLabel(logoIcon);
            logo.setAlignmentX(Component.CENTER_ALIGNMENT);
            logo.setBorder(new EmptyBorder(0, 0, 20, 0));
            panel.add(logo);
        } catch (Exception e) {
            // Si no encuentra la imagen, continúa sin él
        }

        // Crear botones con íconos representativos
        String[][] opcionesPrincipales = {
                {"Gestión de Clientes", "clients.png"},
                {"Gestión de Vehículos", "car.png"},
                {"Gestión de Membresías", "membership.png"},
                {"Pagos", "payment.png"},
                {"Administración del Parqueadero", "parking.png"},
                {"Salir", "exit.png"}
        };

        // Array para guardar los botones
        JButton[] botones = new JButton[opcionesPrincipales.length];

        // Panel para los botones con GridLayout
        JPanel botonesPanel = new JPanel(new GridLayout(0, 1, 0, 10));
        botonesPanel.setBackground(COLOR_BACKGROUND);
        botonesPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        // Crear y agregar cada botón
        for (int i = 0; i < opcionesPrincipales.length; i++) {
            botones[i] = crearBotonModerno(opcionesPrincipales[i][0], opcionesPrincipales[i][1]);
            final int index = i;
            botones[i].addActionListener(e -> {
                // Cerrar el diálogo cuando se hace clic en un botón
                ((JComponent) e.getSource()).getTopLevelAncestor().setVisible(false);
            });
            botonesPanel.add(botones[i]);
        }

        // Agregar panel de botones al panel principal
        panel.add(botonesPanel);

        // Agregar créditos o pie de página
        JLabel footer = new JLabel("© 2025 Sistema de Parqueadero");
        footer.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        footer.setForeground(new Color(149, 165, 166));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        footer.setBorder(new EmptyBorder(20, 0, 0, 0));
        panel.add(footer);

        // Crear JScrollPane para permitir scroll cuando sea necesario
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(400, 550));

        // Crear diálogo personalizado
        JDialog dialog = new JDialog((Frame) null, "Menú Principal", true);
        dialog.setContentPane(scrollPane);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);

        // Variable para almacenar el resultado
        final int[] resultado = {-1};

        // Agregar ActionListener a cada botón
        for (int i = 0; i < botones.length; i++) {
            final int index = i;
            botones[i].addActionListener(e -> resultado[0] = index);
        }

        // Mostrar diálogo
        dialog.setVisible(true);

        // Retornar opción seleccionada
        return resultado[0];
    }


    private static void mostrarMenuClientes(ClienteService clienteService) {
        // Panel principal con BoxLayout vertical
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_BACKGROUND);
        panel.setBorder(new CompoundBorder(
                new LineBorder(COLOR_SECUNDARIO, 1, true),  // Borde exterior con color secundario
                new EmptyBorder(15, 15, 15, 15)             // Padding interior
        ));

        // Encabezado con título
        JLabel titulo = new JLabel("Gestión de Clientes");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(COLOR_TEXTO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(titulo);

        // Opciones de menú (añadiendo la nueva opción para mostrar vehículos)
        String[][] opciones = {
                {"Añadir Cliente", "add_client.png"},
                {"Buscar Cliente", "search_client.png"},
                {"Actualizar Cliente", "update_client.png"},
                {"Eliminar Cliente", "delete_client.png"},
                {"Mostrar Vehículos", "car.png"}, // Nueva opción
                {"Volver al menú principal", "back.png"}
        };

        // Panel para botones
        JPanel botonesPanel = new JPanel(new GridLayout(0, 1, 0, 10));
        botonesPanel.setBackground(COLOR_BACKGROUND);
        botonesPanel.setBorder(new EmptyBorder(0, 0, 10, 0));  // Añadir padding inferior

        // Variable para almacenar el resultado
        final int[] resultado = {-1};

        // Crear y agregar cada botón
        for (int i = 0; i < opciones.length; i++) {
            JButton boton = crearBotonModerno(opciones[i][0], opciones[i][1]);
            final int index = i;
            boton.addActionListener(e -> {
                resultado[0] = index;
                ((JComponent) e.getSource()).getTopLevelAncestor().setVisible(false);
            });
            botonesPanel.add(boton);
        }

        panel.add(botonesPanel);

        // Añadir separador y pie de página
        JSeparator separador = new JSeparator();
        separador.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separador.setForeground(COLOR_SECUNDARIO);
        separador.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(separador);

        JLabel footer = new JLabel("Seleccione una opción");
        footer.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        footer.setForeground(new Color(149, 165, 166));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        footer.setBorder(new EmptyBorder(10, 0, 0, 0));
        panel.add(footer);

        // Crear JScrollPane
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(COLOR_BACKGROUND);  // Asegurar color de fondo del viewport
        scrollPane.setPreferredSize(new Dimension(400, 400));

        // Crear y mostrar diálogo
        JDialog dialog = new JDialog((Frame) null, "Gestión de Clientes", true);
        dialog.setContentPane(scrollPane);
        dialog.getContentPane().setBackground(COLOR_BACKGROUND);  // Asegurar color de fondo del diálogo
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);

        // Actualizar el resultado si se cierra con la X
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                resultado[0] = -1;
            }
        });

        dialog.setVisible(true);

        // Procesar la selección (añadiendo el nuevo caso para mostrar vehículos)
        switch (resultado[0]) {
            case 0 -> clienteService.añadirCliente();
            case 1 -> clienteService.buscarCliente();
            case 2 -> clienteService.actualizarCliente();
            case 3 -> clienteService.eliminarCliente();
            case 4 -> clienteService.mostrarVehiculosCliente(); // Nuevo caso para mostrar vehículos
            case 5, -1 -> { /* Volver al menú principal */ }
        }
    }

    private static void mostrarMenuVehiculos(ClienteService clienteService, VehiculoService vehiculoService) {
        // Panel principal con BoxLayout vertical
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_BACKGROUND);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Encabezado con título
        JLabel titulo = new JLabel("Gestión de Vehículos");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(COLOR_TEXTO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(titulo);

        // Opciones de menú
        String[][] opciones = {
                {"Registrar Vehículo", null},
                {"Buscar Vehículo", null},
                {"Actualizar Vehículo", null},
                {"Ver Vehículos Asociados", null},
                {"Asociar Vehículo a Cliente", null},
                {"Volver al menú principal", null}
        };

        // Panel para botones
        JPanel botonesPanel = new JPanel(new GridLayout(0, 1, 0, 10));
        botonesPanel.setBackground(COLOR_BACKGROUND);

        // Variable para almacenar el resultado
        final int[] resultado = {-1};

        // Crear y agregar cada botón
        for (int i = 0; i < opciones.length; i++) {
            JButton boton = crearBotonModerno(opciones[i][0], opciones[i][1]);
            final int index = i;
            boton.addActionListener(e -> {
                resultado[0] = index;
                ((JComponent) e.getSource()).getTopLevelAncestor().setVisible(false);
            });
            botonesPanel.add(boton);
        }

        panel.add(botonesPanel);

        // Crear JScrollPane
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(400, 400));

        // Crear y mostrar diálogo
        JDialog dialog = new JDialog((Frame) null, "Gestión de Vehículos", true);
        dialog.setContentPane(scrollPane);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);
        dialog.setVisible(true);

        // Procesar la selección
        Cliente cliente;
        String placa;
        switch (resultado[0]) {
            case 0 -> {
                cliente = clienteService.buscarCliente();
                if (cliente != null) {
                    vehiculoService.registrarVehiculo(cliente);
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un cliente válido para registrar un vehículo");
                }
            }
            case 1 -> {
                placa = JOptionPane.showInputDialog("Ingrese la placa del vehículo a buscar:");
                if (placa != null && !placa.trim().isEmpty()) {
                    vehiculoService.buscarVehiculo(placa);
                }
            }
            case 2 -> {
                placa = JOptionPane.showInputDialog("Ingrese la placa del vehículo a actualizar:");
                if (placa != null && !placa.trim().isEmpty()) {
                    vehiculoService.actualizarVehiculo(placa);
                }
            }
            case 3 -> {
                cliente = clienteService.buscarCliente();
                if (cliente != null) {
                    vehiculoService.verVehiculosAsociados(cliente);
                }
            }
            case 4 -> {
                asociarVehiculoCliente(clienteService, vehiculoService);
            }
            case 5, -1 -> { /* Volver al menú principal */ }
        }
    }

    /**
     * Método para asociar un vehículo a un cliente
     */
    /**
     * Método para asociar un vehículo a un cliente
     */
    private static void asociarVehiculoCliente(ClienteService clienteService, VehiculoService vehiculoService) {
        // Primero buscamos al cliente
        Cliente cliente = clienteService.buscarCliente();
        if (cliente == null) {
            JOptionPane.showMessageDialog(null, "Operación cancelada. No se seleccionó ningún cliente.",
                    "Operación Cancelada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Luego buscamos el vehículo
        Vehiculo vehiculo = vehiculoService.buscarVehiculo();
        if (vehiculo == null) {
            JOptionPane.showMessageDialog(null, "Operación cancelada. No se seleccionó ningún vehículo.",
                    "Operación Cancelada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Verificamos si el vehículo ya está asociado a este cliente
        boolean vehiculoYaAsociado = cliente.getVehiculos().contains(vehiculo);
        if (vehiculoYaAsociado) {
            JOptionPane.showMessageDialog(null,
                    "El vehículo con placa " + vehiculo.getPlaca() + " ya está asociado a este cliente.",
                    "Vehículo Ya Asociado", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Asociamos el vehículo al cliente
        cliente.agregarVehiculo(vehiculo);

        JOptionPane.showMessageDialog(null,
                "Vehículo con placa " + vehiculo.getPlaca() + " asociado exitosamente al cliente " + cliente.getNombre(),
                "Asociación Exitosa", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Método para asociar un vehículo a un cliente
     */

    private static void mostrarMenuMembresias(ClienteService clienteService, VehiculoService vehiculoService, MembresiaService membresiaService) {
        // Panel principal con BoxLayout vertical
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_BACKGROUND);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Encabezado con título
        JLabel titulo = new JLabel("Gestión de Membresías");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(COLOR_TEXTO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(titulo);

        // Opciones de menú (añadimos la opción de generar reporte)
        String[][] opciones = {
                {"Registrar Membresía", "membership_add.png"},
                {"Verificar Vigencia", "membership_verify.png"},
                {"Renovar Membresía", "membership_renew.png"},
                {"Generar Reporte de Membresías", "membership_report.png"},
                {"Volver al menú principal", "back.png"}
        };

        // Panel para botones
        JPanel botonesPanel = new JPanel(new GridLayout(0, 1, 0, 10));
        botonesPanel.setBackground(COLOR_BACKGROUND);

        // Variable para almacenar el resultado
        final int[] resultado = {-1};

        // Crear y agregar cada botón
        for (int i = 0; i < opciones.length; i++) {
            JButton boton = crearBotonModerno(opciones[i][0], opciones[i][1]);
            final int index = i;
            boton.addActionListener(e -> {
                resultado[0] = index;
                ((JComponent) e.getSource()).getTopLevelAncestor().setVisible(false);
            });
            botonesPanel.add(boton);
        }

        panel.add(botonesPanel);

        // Crear JScrollPane
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(400, 400));

        // Crear y mostrar diálogo
        JDialog dialog = new JDialog((Frame) null, "Gestión de Membresías", true);
        dialog.setContentPane(scrollPane);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);
        dialog.setVisible(true);

        // Procesar la selección
        String placa;
        Vehiculo vehiculo;
        switch (resultado[0]) {
            case 0 -> {
                placa = JOptionPane.showInputDialog("Ingrese la placa del vehículo:");
                if (placa != null && !placa.trim().isEmpty()) {
                    vehiculo = vehiculoService.buscarVehiculo(placa);
                    if (vehiculo != null) {
                        membresiaService.registrarMembresia(vehiculo);
                    }
                }
            }
            case 1 -> {
                placa = JOptionPane.showInputDialog("Ingrese la placa del vehículo:");
                if (placa != null && !placa.trim().isEmpty()) {
                    vehiculo = vehiculoService.buscarVehiculo(placa);
                    if (vehiculo != null) {
                        membresiaService.verificarVigencia(vehiculo);
                    }
                }
            }
            case 2 -> {
                placa = JOptionPane.showInputDialog("Ingrese la placa del vehículo:");
                if (placa != null && !placa.trim().isEmpty()) {
                    vehiculo = vehiculoService.buscarVehiculo(placa);
                    if (vehiculo != null) {
                        membresiaService.renovarMembresia(vehiculo);
                    }
                }
            }
            case 3 -> {
                // Aseguramos que MembresiaService tenga sus dependencias configuradas
                // Nota: Ya se configuró en el método main, pero lo hacemos por seguridad
                membresiaService.setVehiculoService(vehiculoService);
                membresiaService.setClienteService(clienteService);

                try {
                    membresiaService.generarReporteMembresiasActivas();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "Error al generar el reporte: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            case 4, -1 -> { /* Volver al menú principal */ }
        }
    }

    private static void mostrarMenuPagos(PagoService pagoService) {
        // Panel principal con BoxLayout vertical
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_BACKGROUND);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Encabezado con título
        JLabel titulo = new JLabel("Gestión de Pagos");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(COLOR_TEXTO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(titulo);

        // Opciones de menú - añadiendo la opción de pago por período (membresía)
        String[][] opciones = {
                {"Registrar Pago por Estacionamiento", null},
                {"Registrar Pago por Membresía", null},
                {"Buscar Pago por ID", null},
                {"Calcular Monto a Pagar", null},
                {"Generar Reportes Financieros", null},
                {"Volver al menú principal", null}
        };

        // Panel para botones
        JPanel botonesPanel = new JPanel(new GridLayout(0, 1, 0, 10));
        botonesPanel.setBackground(COLOR_BACKGROUND);

        // Variable para almacenar el resultado
        final int[] resultado = {-1};

        // Crear y agregar cada botón
        for (int i = 0; i < opciones.length; i++) {
            JButton boton = crearBotonModerno(opciones[i][0], opciones[i][1]);
            final int index = i;
            boton.addActionListener(e -> {
                resultado[0] = index;
                ((JComponent) e.getSource()).getTopLevelAncestor().setVisible(false);
            });
            botonesPanel.add(boton);
        }

        panel.add(botonesPanel);

        // Crear JScrollPane
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(400, 400));

        // Crear y mostrar diálogo
        JDialog dialog = new JDialog((Frame) null, "Gestión de Pagos", true);
        dialog.setContentPane(scrollPane);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);
        dialog.setVisible(true);

        // Procesar la selección
        switch (resultado[0]) {
            case 0 -> pagoService.registrarPago();
            case 1 -> pagoService.registrarPagoPorPeriodo(); // Nueva opción para registrar pago por membresía
            case 2 -> {
                String idPago = JOptionPane.showInputDialog("Ingrese el ID del pago a buscar:");
                if (idPago != null && !idPago.trim().isEmpty()) {
                    pagoService.buscarPagoPorId(idPago);
                }
            }
            case 3 -> pagoService.calcularMontoAPagar();
            case 4 -> pagoService.mostrarMenuReportes();
            case 5, -1 -> { /* Volver al menú principal */ }
        }
    }

    private static void mostrarMenuParqueadero(Parqueadero parque, ParqueaderoService parqueaderoService) {
        // Panel principal con BoxLayout vertical
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_BACKGROUND);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Encabezado con título
        JLabel titulo = new JLabel("Administración del Parqueadero");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(COLOR_TEXTO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(titulo);

        // Opciones de menú
        String[][] opciones = {
                {"Configurar Datos del Parqueadero", "config.png"},
                {"Configurar Espacios", "spaces.png"},
                {"Configurar Tarifas", "pricing.png"},
                {"Registrar Entrada de Vehículo", "car_in.png"},
                {"Registrar Salida de Vehículo", "car_out.png"},
                {"Ver Vehículos Actuales", "list_cars.png"},
                {"Ver Estado del Parqueadero", "status.png"},
                {"Volver al menú principal", "back.png"}
        };

        // Panel para botones
        JPanel botonesPanel = new JPanel(new GridLayout(0, 1, 0, 10));
        botonesPanel.setBackground(COLOR_BACKGROUND);

        // Variable para almacenar el resultado
        final int[] resultado = {-1};

        // Crear y agregar cada botón
        for (int i = 0; i < opciones.length; i++) {
            JButton boton = crearBotonModerno(opciones[i][0], opciones[i][1]);
            final int index = i;
            boton.addActionListener(e -> {
                resultado[0] = index;
                ((JComponent) e.getSource()).getTopLevelAncestor().setVisible(false);
            });
            botonesPanel.add(boton);
        }

        panel.add(botonesPanel);

        // Crear JScrollPane
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(450, 500));

        // Crear y mostrar diálogo
        JDialog dialog = new JDialog((Frame) null, "Administración del Parqueadero", true);
        dialog.setContentPane(scrollPane);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);
        dialog.setVisible(true);

        // Procesar la selección
        switch (resultado[0]) {
            case 0 -> parque.ingresarInformacion();
            case 1 -> parqueaderoService.configurarEspacios();
            case 2 -> parqueaderoService.configurarTarifas();
            case 3 -> parqueaderoService.registrarEntradaVehiculo();
            case 4 -> parqueaderoService.registrarSalidaVehiculo();
            case 5 -> parqueaderoService.listarVehiculosActuales();
            case 6 -> parqueaderoService.mostrarEstadoParqueadero();
            case 7, -1 -> { /* Volver al menú principal */ }
        }
    }
}