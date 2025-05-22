package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Parqueadero {
    private String nombre;
    private String direccion;
    private String representante;
    private String telefono;
    private String correo;
    private int puestosMotos;
    private int puestosAutomoviles;
    private int puestosCamiones;
    private ArrayList<Vehiculo> listaDevehiculos = new ArrayList<>();
    private ArrayList<Cliente> listaDeClientes = new ArrayList<>();
    private Map<String, List<Vehiculo>> vehiculosPorCliente = new HashMap<>();

    public Parqueadero() {
    }

    public Parqueadero(String nombre, String direccion, String representante, String telefono, String correo) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.representante = representante;
        this.telefono = telefono;
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getRepresentante() {
        return representante;
    }

    public void setRepresentante(String representante) {
        this.representante = representante;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getPuestosMotos() {
        return puestosMotos;
    }

    public void setPuestosMotos(int puestosMotos) {
        this.puestosMotos = puestosMotos;
    }

    public int getPuestosAutomoviles() {
        return puestosAutomoviles;
    }

    public void setPuestosAutomoviles(int puestosAutomoviles) {
        this.puestosAutomoviles = puestosAutomoviles;
    }

    public int getPuestosCamiones() {
        return puestosCamiones;
    }

    public void setPuestosCamiones(int puestosCamiones) {
        this.puestosCamiones = puestosCamiones;
    }

    public ArrayList<Vehiculo> getListaDevehiculos() {
        return listaDevehiculos;
    }

    public void setListaDevehiculos(ArrayList<Vehiculo> listaDevehiculos) {
        this.listaDevehiculos = listaDevehiculos;
    }

    public ArrayList<Cliente> getListaDeClientes() {
        return listaDeClientes;
    }

    public void setListaDeClientes(ArrayList<Cliente> listaDeClientes) {
        this.listaDeClientes = listaDeClientes;
    }

    // metodos para la informacion del parqueadero

    public void ingresarInformacion() {
        String[] opcionesVentana = { "Ingresar datos", "Salir" };

        JTextField nombreField = new JTextField();
        JTextField direccionField = new JTextField();
        JTextField representanteField = new JTextField();
        JTextField telefonoField = new JTextField();
        JTextField correoField = new JTextField();

        Object[] campos = {
                "Nombre:", nombreField,
                "Direccion:", direccionField,
                "Nombre del Representante:", representanteField,
                "Telefono:", telefonoField,
                "Correo:", correoField
        };

        boolean continuar = true;

        while (continuar) {
            int opcion = JOptionPane.showOptionDialog(null, "Selecciona una opción:", "Ingreso de información",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcionesVentana,
                    opcionesVentana[0]);

            switch (opcion) {
                case 0:
                    while (true) {
                        int opcionesCampoIngreso = JOptionPane.showConfirmDialog(null, campos, "Ingrese los datos",
                                JOptionPane.OK_CANCEL_OPTION);

                        if (opcionesCampoIngreso == JOptionPane.OK_OPTION) {
                            nombre = nombreField.getText().trim();
                            direccion = direccionField.getText().trim();
                            representante = representanteField.getText().trim();
                            telefono = telefonoField.getText().trim();
                            correo = correoField.getText().trim();

                            if (nombre.isEmpty() || direccion.isEmpty() || representante.isEmpty() || telefono.isEmpty()
                                    || correo.isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Porfavor llene todos los campos", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            } else {
                                mostrarInformacion();

                                nombreField.setText("");
                                direccionField.setText("");
                                representanteField.setText("");
                                telefonoField.setText("");
                                correoField.setText("");
                                break;
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Ingreso cancelado.");
                            break;
                        }
                    }
                    break; // AÑADIR ESTE BREAK PARA EVITAR QUE PASE AL SIGUIENTE CASO

                case 1:
                    continuar = false;
                    break;

                default:
                    continuar = false; // También salir si se cierra la ventana
                    break;
            }
        }
    }

    public void mostrarInformacion() {
        String datos = "Nombre: " + nombre + "\nDirección: " + direccion + "\nRepresentante: " + representante
                + "\nTeléfono: " + telefono + "\nCorreo: " + correo;

        JOptionPane.showMessageDialog(null, datos);
    }

    public void actualizarInformacion() {
        String[] opciones = { "Seleccionar", "Actualizar nombre", "Actualizar dirección", "Actualizar representante",
                "Actualizar teléfono", "Actualizar correo", "Ver datos actuales", "Salir" };

        boolean continuar = true;

        while (continuar) {
            String seleccion = (String) JOptionPane.showInputDialog(
                    null,
                    "¿Qué información deseas actualizar?",
                    "Actualizar Información",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    "Seleccionar");
        
            // Si el usuario cancela o cierra el diálogo
            if (seleccion == null) {
                continuar = false;
                break;
            }

            switch (seleccion) {
                case "Actualizar nombre":
                    nombre = JOptionPane.showInputDialog("Ingrese el nuevo nombre:");
                    break;
                case "Actualizar dirección":
                    direccion = JOptionPane.showInputDialog("Ingrese la nueva dirección:");
                    break;
                case "Actualizar representante":
                    representante = JOptionPane.showInputDialog("Ingrese el nuevo representante:");
                    break;
                case "Actualizar teléfono":
                    telefono = JOptionPane.showInputDialog("Ingrese el nuevo teléfono:");
                    break;
                case "Actualizar correo":
                    correo = JOptionPane.showInputDialog("Ingrese el nuevo correo:");
                    break;
                case "Ver datos actuales":
                    mostrarInformacion();
                    break;
                case "Salir":
                    continuar = false;
                    break;
            }
        }
    }

}