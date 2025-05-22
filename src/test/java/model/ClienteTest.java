package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

class ClienteTest {

    private Cliente cliente;
    private static final String NOMBRE = "Juan Pérez";
    private static final String CEDULA = "1234567890";
    private static final String TELEFONO = "555-1234";
    private static final String CORREO = "juan@example.com";

    @BeforeEach
    void setUp() {
        cliente = new Cliente(NOMBRE, CEDULA, TELEFONO, CORREO);
    }

    @Test
    @DisplayName("Test constructor y getters iniciales")
    void testConstructorYGettersIniciales() {
        assertEquals(NOMBRE, cliente.getNombre());
        assertEquals(CEDULA, cliente.getCedula());
        assertEquals(TELEFONO, cliente.getTelefono());
        assertEquals(CORREO, cliente.getCorreo());
        assertNotNull(cliente.getMembresias());
        assertTrue(cliente.getMembresias().isEmpty());
        assertNotNull(cliente.getVehiculos());
        assertTrue(cliente.getVehiculos().isEmpty());
    }

    @Test
    @DisplayName("Test setters y getters para nombre")
    void testSetterGetterNombre() {
        String nuevoNombre = "María López";
        cliente.setNombre(nuevoNombre);
        assertEquals(nuevoNombre, cliente.getNombre());
    }

    @Test
    @DisplayName("Test setters y getters para cédula")
    void testSetterGetterCedula() {
        String nuevaCedula = "0987654321";
        cliente.setCedula(nuevaCedula);
        assertEquals(nuevaCedula, cliente.getCedula());
    }

    @Test
    @DisplayName("Test setters y getters para teléfono")
    void testSetterGetterTelefono() {
        String nuevoTelefono = "555-5678";
        cliente.setTelefono(nuevoTelefono);
        assertEquals(nuevoTelefono, cliente.getTelefono());
    }

    @Test
    @DisplayName("Test setters y getters para correo")
    void testSetterGetterCorreo() {
        String nuevoCorreo = "maria@example.com";
        cliente.setCorreo(nuevoCorreo);
        assertEquals(nuevoCorreo, cliente.getCorreo());
    }

    @Test
    @DisplayName("Test agregar vehículo válido")
    void testAgregarVehiculoValido() {
        Vehiculo vehiculo = new Vehiculo("ABC123", "Rojo", "2020");
        cliente.agregarVehiculo(vehiculo);
        
        assertEquals(1, cliente.getVehiculos().size());
        assertTrue(cliente.getVehiculos().contains(vehiculo));
    }

    @Test
    @DisplayName("Test agregar vehículo nulo")
    void testAgregarVehiculoNulo() {
        cliente.agregarVehiculo(null);
        assertEquals(0, cliente.getVehiculos().size());
    }

    @Test
    @DisplayName("Test eliminar vehículo existente")
    void testEliminarVehiculoExistente() {
        Vehiculo vehiculo = new Vehiculo("ABC123", "Rojo", "2020");
        cliente.agregarVehiculo(vehiculo);
        
        assertTrue(cliente.eliminarVehiculo(vehiculo));
        assertEquals(0, cliente.getVehiculos().size());
    }

    @Test
    @DisplayName("Test eliminar vehículo no existente")
    void testEliminarVehiculoNoExistente() {
        Vehiculo vehiculo1 = new Vehiculo("ABC123", "Rojo", "2020");
        Vehiculo vehiculo2 = new Vehiculo("XYZ789", "Azul", "2019");
        
        cliente.agregarVehiculo(vehiculo1);
        
        assertFalse(cliente.eliminarVehiculo(vehiculo2));
        assertEquals(1, cliente.getVehiculos().size());
    }

    @Test
    @DisplayName("Test eliminar vehículo nulo")
    void testEliminarVehiculoNulo() {
        assertFalse(cliente.eliminarVehiculo(null));
    }

    @Test
    @DisplayName("Test buscar vehículo por placa existente")
    void testBuscarVehiculoPorPlacaExistente() {
        Vehiculo vehiculo1 = new Vehiculo("ABC123", "Rojo", "2020");
        Vehiculo vehiculo2 = new Vehiculo("XYZ789", "Azul", "2019");
        
        cliente.agregarVehiculo(vehiculo1);
        cliente.agregarVehiculo(vehiculo2);
        
        Vehiculo encontrado = cliente.buscarVehiculoPorPlaca("ABC123");
        assertNotNull(encontrado);
        assertEquals("ABC123", encontrado.getPlaca());
        assertEquals("Rojo", encontrado.getColor());
    }

    @Test
    @DisplayName("Test buscar vehículo por placa inexistente")
    void testBuscarVehiculoPorPlacaInexistente() {
        Vehiculo vehiculo = new Vehiculo("ABC123", "Rojo", "2020");
        cliente.agregarVehiculo(vehiculo);
        
        assertNull(cliente.buscarVehiculoPorPlaca("XYZ789"));
    }

    @Test
    @DisplayName("Test buscar vehículo por placa con case insensitive")
    void testBuscarVehiculoPorPlacaCaseInsensitive() {
        Vehiculo vehiculo = new Vehiculo("ABC123", "Rojo", "2020");
        cliente.agregarVehiculo(vehiculo);
        
        Vehiculo encontrado = cliente.buscarVehiculoPorPlaca("abc123");
        assertNotNull(encontrado);
        assertEquals("ABC123", encontrado.getPlaca());
    }

    @Test
    @DisplayName("Test buscar vehículo con placa nula")
    void testBuscarVehiculoPorPlacaNula() {
        assertNull(cliente.buscarVehiculoPorPlaca(null));
    }

    @Test
    @DisplayName("Test buscar vehículo con placa vacía")
    void testBuscarVehiculoPorPlacaVacia() {
        assertNull(cliente.buscarVehiculoPorPlaca(""));
    }

    @Test
    @DisplayName("Test cliente sin membresías activas")
    void testClienteSinMembresiasActivas() {
        assertFalse(cliente.tieneMembresiaActiva());
    }

    @Test
    @DisplayName("Test cliente con membresía activa")
    void testClienteConMembresiaActiva() {
        // Crear una membresía con fecha fin en el futuro
        Membresia membresia = new Membresia();
        
        // Fecha de fin futura (un mes después de hoy)
        LocalDate fechaFin = LocalDate.now().plusMonths(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaFinStr = fechaFin.format(formatter);
        
        membresia.setFechaFin(fechaFinStr);
        
        // Agregar la membresía al cliente
        List<Membresia> membresias = cliente.getMembresias();
        membresias.add(membresia);
        
        assertTrue(cliente.tieneMembresiaActiva());
    }

    @Test
    @DisplayName("Test cliente con membresía expirada")
    void testClienteConMembresiaExpirada() {
        // Crear una membresía con fecha fin en el pasado
        Membresia membresia = new Membresia();
        
        // Fecha de fin pasada (un mes antes de hoy)
        LocalDate fechaFin = LocalDate.now().minusMonths(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaFinStr = fechaFin.format(formatter);
        
        membresia.setFechaFin(fechaFinStr);
        
        // Agregar la membresía al cliente
        List<Membresia> membresias = cliente.getMembresias();
        membresias.add(membresia);
        
        assertFalse(cliente.tieneMembresiaActiva());
    }

    @Test
    @DisplayName("Test cliente con membresía que expira hoy")
    void testClienteConMembresiaQueExpiraHoy() {
        // Crear una membresía con fecha fin en hoy
        Membresia membresia = new Membresia();
        
        // Fecha de fin hoy
        LocalDate fechaFin = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaFinStr = fechaFin.format(formatter);
        
        membresia.setFechaFin(fechaFinStr);
        
        // Agregar la membresía al cliente
        List<Membresia> membresias = cliente.getMembresias();
        membresias.add(membresia);
        
        assertTrue(cliente.tieneMembresiaActiva());
    }

    @Test
    @DisplayName("Test cliente con membresía con formato de fecha inválido")
    void testClienteConMembresiaFormatoFechaInvalido() {
        // Crear una membresía con formato de fecha inválido
        Membresia membresia = new Membresia();
        membresia.setFechaFin("formato-invalido");
        
        // Agregar la membresía al cliente
        List<Membresia> membresias = cliente.getMembresias();
        membresias.add(membresia);
        
        // Debería retornar false ya que la fecha no se puede parsear
        assertFalse(cliente.tieneMembresiaActiva());
    }

    @Test
    @DisplayName("Test cliente con membresía sin fecha fin")
    void testClienteConMembresiaSinFechaFin() {
        // Crear una membresía sin fecha fin
        Membresia membresia = new Membresia();
        membresia.setFechaFin(null);
        
        // Agregar la membresía al cliente
        List<Membresia> membresias = cliente.getMembresias();
        membresias.add(membresia);
        
        // Debería retornar false ya que no hay fecha fin
        assertFalse(cliente.tieneMembresiaActiva());
    }

    @Test
    @DisplayName("Test cliente con múltiples membresías, al menos una activa")
    void testClienteConMultiplesMembresiasUnaActiva() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        // Membresía 1: expirada
        Membresia membresia1 = new Membresia();
        String fechaFin1 = LocalDate.now().minusMonths(1).format(formatter);
        membresia1.setFechaFin(fechaFin1);
        
        // Membresía 2: activa
        Membresia membresia2 = new Membresia();
        String fechaFin2 = LocalDate.now().plusMonths(1).format(formatter);
        membresia2.setFechaFin(fechaFin2);
        
        // Membresía 3: con formato inválido
        Membresia membresia3 = new Membresia();
        membresia3.setFechaFin("formato-invalido");
        
        // Agregar las membresías al cliente
        List<Membresia> membresias = cliente.getMembresias();
        membresias.add(membresia1);
        membresias.add(membresia2);
        membresias.add(membresia3);
        
        // Debería retornar true ya que al menos una membresía está activa
        assertTrue(cliente.tieneMembresiaActiva());
    }

    @Test
    @DisplayName("Test toString")
    void testToString() {
        String expected = "nombre: " + NOMBRE + "\ncedula: " + CEDULA + "\ntelefono: " + TELEFONO + "\ncorreo: " + CORREO;
        assertEquals(expected, cliente.toString());
    }
}