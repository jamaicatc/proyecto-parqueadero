package service;

import model.Cliente;
import model.TipoMembresia;
import model.Vehiculo;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class MembresiaService {

    // Método para registrar una membresía
    public void registrarMembresia(Vehiculo vehiculo) {
        if (vehiculo != null) {
            String[] opciones = {"Anual", "Trimestral", "Mensual", "Ninguna"};
            int opcion = JOptionPane.showOptionDialog(
                    null,
                    "Seleccione una membresía",
                    "Registrar Membresía",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            if (opcion == JOptionPane.CLOSED_OPTION) {
                JOptionPane.showMessageDialog(null, "Registro cancelado.");
                return;
            }

            TipoMembresia tipo = TipoMembresia.valueOf(opciones[opcion].toUpperCase());
            vehiculo.setMembresia(tipo);

            // Fecha de inicio y fin
            LocalDate hoy = LocalDate.now();
            LocalDate fechaFin;

            switch (tipo) {
                case ANUAL -> fechaFin = hoy.plusYears(1);
                case TRIMESTRAL -> fechaFin = hoy.plusMonths(3);
                case MENSUAL -> fechaFin = hoy.plusMonths(1);
                default -> fechaFin = hoy;
            }

            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            vehiculo.setFechaFinMembresia(fechaFin.format(formato));

            // Mensaje final
            String mensaje = "El vehículo con placa: " + vehiculo.getPlaca() +
                    "\ntiene una membresía " + tipo +
                    " hasta " + vehiculo.getFechaFinMembresia();

            JOptionPane.showMessageDialog(null, mensaje);
        } else {
            JOptionPane.showMessageDialog(null, "Vehículo no encontrado.");
        }
    }

    // Método para verificar la vigencia de una membresía
    public void verificarVigencia(Vehiculo vehiculo) {
        if (vehiculo == null) {
            JOptionPane.showMessageDialog(null, "Vehículo no encontrado.");
            return;
        }

        TipoMembresia tipoMembresia = vehiculo.getMembresia();
        String fechaFinMembresia = vehiculo.getFechaFinMembresia();

        if (tipoMembresia != TipoMembresia.NINGUNA && fechaFinMembresia != null) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

            try {
                Date fechaFin = formatoFecha.parse(fechaFinMembresia);
                Date hoy = new Date();

                String estado = fechaFin.after(hoy) ? "VIGENTE" : "VENCIDA";

                String mensaje = "Vehículo con placa: " + vehiculo.getPlaca() + "\n" +
                        "Membresía: " + tipoMembresia + "\n" +
                        "Caduca el: " + fechaFinMembresia + "\n" +
                        "Estado: " + estado;

                JOptionPane.showMessageDialog(null, mensaje);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, "Error al procesar la fecha.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "El vehículo con placa " + vehiculo.getPlaca() + " no tiene una membresía activa.");
        }
    }

    // Método para renovar una membresía
    public void renovarMembresia(Vehiculo vehiculo) {
        if (vehiculo == null) {
            JOptionPane.showMessageDialog(null, "Vehículo no encontrado.");
            return;
        }

        String fechaFinActual = vehiculo.getFechaFinMembresia();
        TipoMembresia tipoActual = vehiculo.getMembresia();

        if (tipoActual == TipoMembresia.NINGUNA || fechaFinActual == null) {
            JOptionPane.showMessageDialog(null, "El vehículo no tiene una membresía activa que pueda renovarse.");
            return;
        }

        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        Date hoy = new Date();

        try {
            Date fechaFin = formatoFecha.parse(fechaFinActual);

            if (fechaFin.after(hoy)) {
                JOptionPane.showMessageDialog(null, "La membresía aún está vigente y no necesita ser renovada.");
                return;
            }

        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Error al procesar la fecha de membresía.");
            return;
        }

        // Selección de nueva membresía
        String[] opciones = {"Anual", "Trimestral", "Mensual", "Ninguna"};
        int opcion = JOptionPane.showOptionDialog(
                null,
                "Seleccione una nueva membresía para renovar:",
                "Renovar Membresía",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (opcion == JOptionPane.CLOSED_OPTION) {
            JOptionPane.showMessageDialog(null, "Renovación cancelada.");
            return;
        }

        TipoMembresia nuevoTipo = TipoMembresia.valueOf(opciones[opcion].toUpperCase());
        vehiculo.setMembresia(nuevoTipo);

        LocalDate nuevaFechaInicio = LocalDate.now();
        LocalDate nuevaFechaFin;

        switch (nuevoTipo) {
            case ANUAL -> nuevaFechaFin = nuevaFechaInicio.plusYears(1);
            case TRIMESTRAL -> nuevaFechaFin = nuevaFechaInicio.plusMonths(3);
            case MENSUAL -> nuevaFechaFin = nuevaFechaInicio.plusMonths(1);
            default -> nuevaFechaFin = nuevaFechaInicio;
        }

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        vehiculo.setFechaFinMembresia(nuevaFechaFin.format(formato));

        // Mensaje de confirmación
        JOptionPane.showMessageDialog(null, "Vehículo con placa: " + vehiculo.getPlaca() +
                "\nRenovó membresía hasta: " + vehiculo.getFechaFinMembresia());
    }
}