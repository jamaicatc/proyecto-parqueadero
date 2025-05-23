package codeService;

import model.Cliente;
import model.Vehiculo;
import model.TipoMembresia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClienteCodeServiceTest {

    private ClienteCodeService clienteService;

    @BeforeEach
    public void setUp() {
        clienteService = new ClienteCodeService();
    }

    // Tests para el constructor
    @Test
    @DisplayName("Test constructor inicializa lista vacía")
    public void testConstructor() {
        assertNotNull(clienteService);
        assertEquals(0, clienteService.obtenerNumeroClientes());
    }

    // Tests para el método añadirCliente
    @Test
    @DisplayName("Test añadir cliente con datos válidos")
    public void testAñadirClienteExitoso() {
        boolean resultado = clienteService.añadirCliente("Juan Pérez", "1234567890", "3001234567", "juan@example.com");
        
        assertTrue(resultado);
        assertEquals(1, clienteService.obtenerNumeroClientes());
        
        // Verificar que el cliente se puede encontrar
        Cliente cliente = clienteService.buscarCliente("1234567890", 1);
        assertNotNull(cliente);
        assertEquals("Juan Pérez", cliente.getNombre());
    }

    @Test
    @DisplayName("Test añadir cliente con datos incompletos")
    public void testAñadirClienteDatosIncompletos() {
        // Con un campo vacío
        boolean resultado1 = clienteService.añadirCliente("", "1234567890", "3001234567", "juan@example.com");
        assertFalse(resultado1);
        
        // Con un campo nulo
        boolean resultado2 = clienteService.añadirCliente("Juan Pérez", null, "3001234567", "juan@example.com");
        assertFalse(resultado2);
        
        assertEquals(0, clienteService.obtenerNumeroClientes());
    }

    @Test
    @DisplayName("Test añadir cliente con cédula duplicada")
    public void testAñadirClienteCedulaDuplicada() {
        // Añadimos un cliente primero
        clienteService.añadirCliente("Juan Pérez", "1234567890", "3001234567", "juan@example.com");
        
        // Intentamos añadir otro cliente con la misma cédula
        boolean resultado = clienteService.añadirCliente("Ana Gómez", "1234567890", "3007654321", "ana@example.com");
        
        assertFalse(resultado);
        assertEquals(1, clienteService.obtenerNumeroClientes());
    }

    // Tests para el método buscarCliente
    @Test
    @DisplayName("Test buscar cliente por nombre")
    public void testBuscarClientePorNombre() {
        // Añadimos algunos clientes para buscar
        clienteService.añadirCliente("Juan Pérez", "1234567890", "3001234567", "juan@example.com");
        clienteService.añadirCliente("María López", "0987654321", "3009876543", "maria@example.com");
        
        // Buscamos por nombre
        Cliente clienteEncontrado = clienteService.buscarCliente("Juan Pérez", 0);
        
        assertNotNull(clienteEncontrado);
        assertEquals("1234567890", clienteEncontrado.getCedula());
    }

    @Test
    @DisplayName("Test buscar cliente por cédula")
    public void testBuscarClientePorCedula() {
        // Añadimos algunos clientes para buscar
        clienteService.añadirCliente("Juan Pérez", "1234567890", "3001234567", "juan@example.com");
        clienteService.añadirCliente("María López", "0987654321", "3009876543", "maria@example.com");
        
        // Buscamos por cédula
        Cliente clienteEncontrado = clienteService.buscarCliente("0987654321", 1);
        
        assertNotNull(clienteEncontrado);
        assertEquals("María López", clienteEncontrado.getNombre());
    }

    @Test
    @DisplayName("Test buscar cliente por teléfono")
    public void testBuscarClientePorTelefono() {
        // Añadimos algunos clientes para buscar
        clienteService.añadirCliente("Juan Pérez", "1234567890", "3001234567", "juan@example.com");
        clienteService.añadirCliente("María López", "0987654321", "3009876543", "maria@example.com");
        
        // Buscamos por teléfono
        Cliente clienteEncontrado = clienteService.buscarCliente("3001234567", 2);
        
        assertNotNull(clienteEncontrado);
        assertEquals("Juan Pérez", clienteEncontrado.getNombre());
    }

    @Test
    @DisplayName("Test buscar cliente que no existe")
    public void testBuscarClienteNoExistente() {
        // Añadimos un cliente
        clienteService.añadirCliente("Juan Pérez", "1234567890", "3001234567", "juan@example.com");
        
        // Buscamos un cliente que no existe
        Cliente clienteEncontrado = clienteService.buscarCliente("Carlos Gómez", 0);
        
        assertNull(clienteEncontrado);
    }

    @Test
    @DisplayName("Test buscar cliente con criterio inválido")
    public void testBuscarClienteCriterioInvalido() {
        // Probamos con criterio vacío
        Cliente clienteEncontrado1 = clienteService.buscarCliente("", 0);
        assertNull(clienteEncontrado1);
        
        // Probamos con criterio nulo
        Cliente clienteEncontrado2 = clienteService.buscarCliente(null, 1);
        assertNull(clienteEncontrado2);
        
        // Probamos con tipo de búsqueda inválido
        Cliente clienteEncontrado3 = clienteService.buscarCliente("Juan", 5);
        assertNull(clienteEncontrado3);
    }

    // Tests para el método actualizarCliente
    @Test
    @DisplayName("Test actualizar nombre del cliente")
    public void testActualizarNombreCliente() {
        // Añadimos un cliente y lo buscamos
        clienteService.añadirCliente("Juan Pérez", "1234567890", "3001234567", "juan@example.com");
        Cliente cliente = clienteService.buscarCliente("1234567890", 1);
        
        // Actualizamos el nombre
        boolean resultado = clienteService.actualizarCliente(cliente, 0, "Juan Carlos Pérez");
        
        assertTrue(resultado);
        assertEquals("Juan Carlos Pérez", cliente.getNombre());
    }

    @Test
    @DisplayName("Test actualizar teléfono del cliente")
    public void testActualizarTelefonoCliente() {
        // Añadimos un cliente y lo buscamos
        clienteService.añadirCliente("Juan Pérez", "1234567890", "3001234567", "juan@example.com");
        Cliente cliente = clienteService.buscarCliente("1234567890", 1);
        
        // Actualizamos el teléfono
        boolean resultado = clienteService.actualizarCliente(cliente, 1, "3109876543");
        
        assertTrue(resultado);
        assertEquals("3109876543", cliente.getTelefono());
    }

    @Test
    @DisplayName("Test actualizar correo del cliente")
    public void testActualizarCorreoCliente() {
        // Añadimos un cliente y lo buscamos
        clienteService.añadirCliente("Juan Pérez", "1234567890", "3001234567", "juan@example.com");
        Cliente cliente = clienteService.buscarCliente("1234567890", 1);
        
        // Actualizamos el correo
        boolean resultado = clienteService.actualizarCliente(cliente, 2, "juanperez@example.com");
        
        assertTrue(resultado);
        assertEquals("juanperez@example.com", cliente.getCorreo());
    }

    @Test
    @DisplayName("Test actualizar cliente con parámetros inválidos")
    public void testActualizarClienteParametrosInvalidos() {
        // Añadimos un cliente y lo buscamos
        clienteService.añadirCliente("Juan Pérez", "1234567890", "3001234567", "juan@example.com");
        Cliente cliente = clienteService.buscarCliente("1234567890", 1);
        
        // Probamos con cliente nulo
        boolean resultado1 = clienteService.actualizarCliente(null, 0, "Nuevo Nombre");
        assertFalse(resultado1);
        
        // Probamos con valor nulo
        boolean resultado2 = clienteService.actualizarCliente(cliente, 0, null);
        assertFalse(resultado2);
        
        // Probamos con valor vacío
        boolean resultado3 = clienteService.actualizarCliente(cliente, 0, "");
        assertFalse(resultado3);
        
        // Probamos con campo inválido
        boolean resultado4 = clienteService.actualizarCliente(cliente, 5, "Valor");
        assertFalse(resultado4);
    }

    // Tests para el método eliminarCliente
    @Test
    @DisplayName("Test eliminar cliente existente")
    public void testEliminarClienteExitoso() {
        // Añadimos un cliente y lo buscamos
        clienteService.añadirCliente("Juan Pérez", "1234567890", "3001234567", "juan@example.com");
        Cliente cliente = clienteService.buscarCliente("1234567890", 1);
        
        // Eliminamos el cliente
        boolean resultado = clienteService.eliminarCliente(cliente);
        
        assertTrue(resultado);
        assertEquals(0, clienteService.obtenerNumeroClientes());
    }

    @Test
    @DisplayName("Test eliminar cliente inexistente")
    public void testEliminarClienteInexistente() {
        // Creamos un cliente pero no lo añadimos al servicio
        Cliente cliente = new Cliente("Juan Pérez", "1234567890", "3001234567", "juan@example.com");
        
        // Intentamos eliminar el cliente
        boolean resultado = clienteService.eliminarCliente(cliente);
        
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Test eliminar cliente nulo")
    public void testEliminarClienteNulo() {
        // Intentamos eliminar un cliente nulo
        boolean resultado = clienteService.eliminarCliente(null);
        
        assertFalse(resultado);
    }

    // Tests para el método verificarCoberturaVehiculo
    @Test
    @DisplayName("Test verificar cobertura de vehículo activa")
    public void testVerificarCoberturaVehiculoActiva() {
        // Creamos un vehículo con membresía activa
        Vehiculo vehiculo = new Vehiculo("ABC123", "Rojo", "Toyota Corolla");
        
        try {
            // Si TipoMembresia es un enum, usamos un valor válido
            vehiculo.setMembresia(TipoMembresia.NINGUNA);
            
            // Fecha fin en el futuro
            LocalDate fechaFin = LocalDate.now().plusMonths(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String fechaFinStr = fechaFin.format(formatter);
            vehiculo.setFechaFinMembresia(fechaFinStr);
            
            // Verificamos la cobertura
            boolean cobertura = clienteService.verificarCoberturaVehiculo(vehiculo);
            
            assertTrue(cobertura);
        } catch (Exception e) {
            // Si falla porque TipoMembresia no está definido, fallaremos el test con mensaje claro
            fail("Error al verificar cobertura: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Test verificar cobertura de vehículo vencida")
    public void testVerificarCoberturaVehiculoVencida() {
        try {
            // Creamos un vehículo con membresía vencida
            Vehiculo vehiculo = new Vehiculo("ABC123", "Rojo", "Toyota Corolla");
            vehiculo.setMembresia(TipoMembresia.NINGUNA);
            
            // Fecha fin en el pasado
            LocalDate fechaFin = LocalDate.now().minusMonths(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String fechaFinStr = fechaFin.format(formatter);
            vehiculo.setFechaFinMembresia(fechaFinStr);
            
            // Verificamos la cobertura
            boolean cobertura = clienteService.verificarCoberturaVehiculo(vehiculo);
            
            assertFalse(cobertura);
        } catch (Exception e) {
            fail("Error al verificar cobertura vencida: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Test verificar cobertura de vehículo sin membresía")
    public void testVerificarCoberturaVehiculoSinMembresia() {
        // Creamos un vehículo sin membresía
        Vehiculo vehiculo = new Vehiculo("ABC123", "Rojo", "Toyota Corolla");
        
        // Verificamos la cobertura
        boolean cobertura = clienteService.verificarCoberturaVehiculo(vehiculo);
        
        assertFalse(cobertura);
    }

    @Test
    @DisplayName("Test verificar cobertura con formato de fecha incorrecto")
    public void testVerificarCoberturaVehiculoFormatoIncorrecto() {
        try {
            // Creamos un vehículo con formato de fecha incorrecto
            Vehiculo vehiculo = new Vehiculo("ABC123", "Rojo", "Toyota Corolla");
            vehiculo.setMembresia(TipoMembresia.NINGUNA);
            vehiculo.setFechaFinMembresia("fecha-incorrecta");
            
            // Verificamos la cobertura
            boolean cobertura = clienteService.verificarCoberturaVehiculo(vehiculo);
            
            assertFalse(cobertura);
        } catch (Exception e) {
            fail("Error al verificar cobertura con formato incorrecto: " + e.getMessage());
        }
    }

    // Tests para el método obtenerVehiculosCliente
    @Test
    @DisplayName("Test obtener vehículos de cliente con vehículos")
    public void testObtenerVehiculosClienteConVehiculos() {
        try {
            // Añadimos un cliente
            clienteService.añadirCliente("Juan Pérez", "1234567890", "3001234567", "juan@example.com");
            Cliente cliente = clienteService.buscarCliente("1234567890", 1);
            
            // Añadimos vehículos al cliente
            Vehiculo vehiculo1 = new Vehiculo("ABC123", "Rojo", "Toyota Corolla");
            Vehiculo vehiculo2 = new Vehiculo("XYZ789", "Azul", "Honda Civic");
            
            // Verificamos si el método agregarVehiculo existe
            if (cliente.getClass().getMethod("agregarVehiculo", Vehiculo.class) != null) {
                cliente.agregarVehiculo(vehiculo1);
                cliente.agregarVehiculo(vehiculo2);
                
                // Obtenemos los vehículos
                List<Vehiculo> vehiculos = clienteService.obtenerVehiculosCliente(cliente);
                
                assertNotNull(vehiculos);
                assertEquals(2, vehiculos.size());
                assertTrue(vehiculos.contains(vehiculo1));
                assertTrue(vehiculos.contains(vehiculo2));
            } else {
                // Si el método no existe, saltamos el test
                fail("El método agregarVehiculo no existe en la clase Cliente");
            }
        } catch (NoSuchMethodException e) {
            // Si el método no existe, no fallamos el test sino que lo marcamos como ignorado
            fail("El método agregarVehiculo no existe en la clase Cliente: " + e.getMessage());
        } catch (Exception e) {
            fail("Error al obtener vehículos del cliente: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Test obtener vehículos de cliente sin vehículos")
    public void testObtenerVehiculosClienteSinVehiculos() {
        // Añadimos un cliente sin vehículos
        clienteService.añadirCliente("Juan Pérez", "1234567890", "3001234567", "juan@example.com");
        Cliente cliente = clienteService.buscarCliente("1234567890", 1);
        
        // Obtenemos los vehículos
        List<Vehiculo> vehiculos = clienteService.obtenerVehiculosCliente(cliente);
        
        assertNotNull(vehiculos);
        assertTrue(vehiculos.isEmpty());
    }

    @Test
    @DisplayName("Test obtener vehículos de cliente nulo")
    public void testObtenerVehiculosClienteNulo() {
        // Obtenemos los vehículos de un cliente nulo
        List<Vehiculo> vehiculos = clienteService.obtenerVehiculosCliente(null);
        
        assertNotNull(vehiculos);
        assertTrue(vehiculos.isEmpty());
    }

    // Tests para el método obtenerTodosLosClientes
    @Test
    @DisplayName("Test obtener todos los clientes sin clientes registrados")
    public void testObtenerTodosLosClientesSinClientes() {
        // Obtenemos todos los clientes cuando no hay ninguno
        List<Cliente> clientes = clienteService.obtenerTodosLosClientes();
        
        assertNotNull(clientes);
        assertTrue(clientes.isEmpty());
    }

    @Test
    @DisplayName("Test obtener todos los clientes con clientes registrados")
    public void testObtenerTodosLosClientesConClientes() {
        // Añadimos algunos clientes
        clienteService.añadirCliente("Juan Pérez", "1234567890", "3001234567", "juan@example.com");
        clienteService.añadirCliente("María López", "0987654321", "3009876543", "maria@example.com");
        
        // Obtenemos todos los clientes
        List<Cliente> clientes = clienteService.obtenerTodosLosClientes();
        
        assertNotNull(clientes);
        assertEquals(2, clientes.size());
    }

    // Tests para el método obtenerNumeroClientes
    @Test
    @DisplayName("Test obtener número de clientes sin clientes")
    public void testObtenerNumeroClientesSinClientes() {
        assertEquals(0, clienteService.obtenerNumeroClientes());
    }

    @Test
    @DisplayName("Test obtener número de clientes con varios clientes")
    public void testObtenerNumeroClientesConClientes() {
        // Añadimos algunos clientes
        clienteService.añadirCliente("Juan Pérez", "1234567890", "3001234567", "juan@example.com");
        clienteService.añadirCliente("María López", "0987654321", "3009876543", "maria@example.com");
        clienteService.añadirCliente("Carlos Gómez", "5678901234", "3005678901", "carlos@example.com");
        
        assertEquals(3, clienteService.obtenerNumeroClientes());
    }
}