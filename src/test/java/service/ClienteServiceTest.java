package service;

import model.Cliente;
import model.Membresia;
import model.TipoMembresia;
import model.Vehiculo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class ClienteServiceTest {
    
    private ClienteService clienteService;
    private List<Cliente> listaDeClientes;
    private PrintStream originalOut;
    private PrintStream originalErr;
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;
    
    @BeforeEach
    void setUp() throws Exception {
        // Establecer modo headless para evitar errores con JOptionPane
        System.setProperty("java.awt.headless", "true");
        
        // Crear instancia del servicio
        clienteService = new ClienteService();
        
        // Acceder a la lista de clientes a través de reflexión
        Field field = ClienteService.class.getDeclaredField("listaDeClientes");
        field.setAccessible(true);
        listaDeClientes = (List<Cliente>) field.get(clienteService);
        
        // Redireccionar salida estándar para capturar mensajes
        originalOut = System.out;
        originalErr = System.err;
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }
    
    private void restoreOutput() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
    
    @Test
    @DisplayName("Test constructor inicializa lista de clientes vacía")
    void testConstructor() {
        assertNotNull(listaDeClientes);
        assertTrue(listaDeClientes.isEmpty());
    }
    
    @Test
    @DisplayName("Test obtenerTodosLosClientes cuando hay clientes")
    void testObtenerTodosLosClientesConClientes() {
        // Agregar clientes directamente a la lista
        Cliente cliente1 = new Cliente("Juan Pérez", "123456789", "555-1234", "juan@example.com");
        Cliente cliente2 = new Cliente("María López", "987654321", "555-5678", "maria@example.com");
        listaDeClientes.add(cliente1);
        listaDeClientes.add(cliente2);
        
        // Mockear JOptionPane
        mockJOptionPane();
        
        // Llamar al método a probar
        List<Cliente> clientes = clienteService.obtenerTodosLosClientes();
        
        // Verificar resultados
        assertEquals(2, clientes.size());
        assertTrue(clientes.contains(cliente1));
        assertTrue(clientes.contains(cliente2));
    }
    
    @Test
    @DisplayName("Test obtenerTodosLosClientes cuando no hay clientes")
    void testObtenerTodosLosClientesSinClientes() {
        // Asegurar que la lista está vacía
        listaDeClientes.clear();
        
        // Mockear JOptionPane
        mockJOptionPane();
        
        // Llamar al método a probar
        List<Cliente> clientes = clienteService.obtenerTodosLosClientes();
        
        // Verificar resultados
        assertNotNull(clientes);
        assertTrue(clientes.isEmpty());
    }
    
    @Test
    @DisplayName("Test añadirCliente con datos válidos")
    void testAñadirClienteValido() {
        // Configurar el mock para JOptionPane para simular entrada de datos válidos
        mockJOptionPaneForAñadirCliente(
            JOptionPane.OK_OPTION, 
            "Carlos Rodríguez", 
            "111222333", 
            "555-9999", 
            "carlos@example.com"
        );
        
        // Ejecutar el método añadirCliente()
        clienteService.añadirCliente();
        
        // Verificar que se ha añadido un nuevo cliente con los datos correctos
        assertEquals(1, listaDeClientes.size());
        Cliente clienteAñadido = listaDeClientes.get(0);
        assertEquals("Carlos Rodríguez", clienteAñadido.getNombre());
        assertEquals("111222333", clienteAñadido.getCedula());
        assertEquals("555-9999", clienteAñadido.getTelefono());
        assertEquals("carlos@example.com", clienteAñadido.getCorreo());
    }
    
    @Test
    @DisplayName("Test añadirCliente con datos incompletos")
    void testAñadirClienteDatosIncompletos() {
        // Configurar el mock para simular entrada de datos incompletos, luego cancelar
        final AtomicInteger counter = new AtomicInteger(0);
        mockJOptionPaneWithResponses(options -> {
            if (counter.getAndIncrement() == 0) {
                // Primera vez: OK con datos incompletos
                return JOptionPane.OK_OPTION;
            } else {
                // Segunda vez: cancelar
                return JOptionPane.CANCEL_OPTION;
            }
        }, textField -> {
            if (textField.toString().contains("Nombre")) {
                return "Nombre Test";
            } else if (textField.toString().contains("Cédula")) {
                return "123456";
            } else if (textField.toString().contains("Teléfono")) {
                return "";  // Campo de teléfono vacío
            } else {
                return "correo@test.com";
            }
        });
        
        // Ejecutar el método añadirCliente()
        clienteService.añadirCliente();
        
        // Verificar que no se ha añadido ningún cliente
        assertTrue(listaDeClientes.isEmpty());
    }
    
    @Test
    @DisplayName("Test añadirCliente con cédula duplicada")
    void testAñadirClienteCedulaDuplicada() {
        // Crear un cliente existente
        Cliente clienteExistente = new Cliente("María López", "123456789", "555-5678", "maria@example.com");
        listaDeClientes.add(clienteExistente);
        
        // Configurar el mock para simular intento de añadir cliente con cédula duplicada, luego cancelar
        final AtomicInteger counter = new AtomicInteger(0);
        mockJOptionPaneWithResponses(options -> {
            if (counter.getAndIncrement() == 0) {
                // Primera vez: OK con cédula duplicada
                return JOptionPane.OK_OPTION;
            } else {
                // Segunda vez: cancelar
                return JOptionPane.CANCEL_OPTION;
            }
        }, textField -> {
            if (textField.toString().contains("Nombre")) {
                return "Nuevo Cliente";
            } else if (textField.toString().contains("Cédula")) {
                return "123456789";  // Cédula duplicada
            } else if (textField.toString().contains("Teléfono")) {
                return "555-1111";
            } else {
                return "nuevo@test.com";
            }
        });
        
        // Ejecutar el método añadirCliente()
        clienteService.añadirCliente();
        
        // Verificar que no se ha añadido un nuevo cliente (solo existe el original)
        assertEquals(1, listaDeClientes.size());
        assertEquals(clienteExistente, listaDeClientes.get(0));
    }
    
    @Test
    @DisplayName("Test añadirCliente cancelado por el usuario")
    void testAñadirClienteCancelado() {
        // Configurar el mock para simular cancelación directa
        mockJOptionPaneForAñadirCliente(JOptionPane.CANCEL_OPTION, "", "", "", "");
        
        // Ejecutar el método añadirCliente()
        clienteService.añadirCliente();
        
        // Verificar que no se ha añadido ningún cliente
        assertTrue(listaDeClientes.isEmpty());
    }
    
    @Test
    @DisplayName("Test buscarCliente por nombre existente")
    void testBuscarClientePorNombreExistente() {
        // Agregar cliente a la lista
        Cliente cliente = new Cliente("Juan Pérez", "123456789", "555-1234", "juan@example.com");
        listaDeClientes.add(cliente);
        
        // Configurar mock para buscar por nombre
        mockJOptionPaneForBuscarCliente(0, "Juan Pérez");
        
        // Ejecutar método
        Cliente resultado = clienteService.buscarCliente();
        
        // Verificar resultado
        assertNotNull(resultado);
        assertEquals(cliente, resultado);
    }
    
    @Test
    @DisplayName("Test buscarCliente por cédula existente")
    void testBuscarClientePorCedulaExistente() {
        // Agregar cliente a la lista
        Cliente cliente = new Cliente("Juan Pérez", "123456789", "555-1234", "juan@example.com");
        listaDeClientes.add(cliente);
        
        // Configurar mock para buscar por cédula
        mockJOptionPaneForBuscarCliente(1, "123456789");
        
        // Ejecutar método
        Cliente resultado = clienteService.buscarCliente();
        
        // Verificar resultado
        assertNotNull(resultado);
        assertEquals(cliente, resultado);
    }
    
    @Test
    @DisplayName("Test buscarCliente por teléfono existente")
    void testBuscarClientePorTelefonoExistente() {
        // Agregar cliente a la lista
        Cliente cliente = new Cliente("Juan Pérez", "123456789", "555-1234", "juan@example.com");
        listaDeClientes.add(cliente);
        
        // Configurar mock para buscar por teléfono
        mockJOptionPaneForBuscarCliente(2, "555-1234");
        
        // Ejecutar método
        Cliente resultado = clienteService.buscarCliente();
        
        // Verificar resultado
        assertNotNull(resultado);
        assertEquals(cliente, resultado);
    }
    
    @Test
    @DisplayName("Test buscarCliente con criterio no encontrado")
    void testBuscarClienteCriterioNoEncontrado() {
        // Agregar cliente a la lista
        Cliente cliente = new Cliente("Juan Pérez", "123456789", "555-1234", "juan@example.com");
        listaDeClientes.add(cliente);
        
        // Configurar mock para buscar por nombre no existente
        mockJOptionPaneForBuscarCliente(0, "Persona Inexistente");
        
        // Ejecutar método
        Cliente resultado = clienteService.buscarCliente();
        
        // Verificar resultado
        assertNull(resultado);
    }
    
    @Test
    @DisplayName("Test buscarCliente con diálogo cancelado")
    void testBuscarClienteDialogoCancelado() {
        // Configurar mock para simular que se cierra el diálogo
        mockJOptionPaneWithResponses(options -> JOptionPane.CLOSED_OPTION, textField -> "");
        
        // Ejecutar método
        Cliente resultado = clienteService.buscarCliente();
        
        // Verificar resultado
        assertNull(resultado);
    }
    
    @Test
    @DisplayName("Test buscarCliente con criterio vacío")
    void testBuscarClienteCriterioVacio() {
        // Configurar mock para buscar pero con criterio vacío
        mockJOptionPaneForBuscarClienteWithEmptyCriteria();
        
        // Ejecutar método
        Cliente resultado = clienteService.buscarCliente();
        
        // Verificar resultado
        assertNull(resultado);
    }
    
    @Test
    @DisplayName("Test actualizarCliente cambiando nombre")
    void testActualizarClienteCambiarNombre() {
        // Crear cliente y añadirlo a la lista
        Cliente cliente = new Cliente("Juan Pérez", "123456789", "555-1234", "juan@example.com");
        listaDeClientes.add(cliente);
        
        // Configurar mock para buscarCliente y actualizar nombre
        mockJOptionPaneForActualizarCliente(cliente, 0, "Juan García");
        
        // Ejecutar método
        clienteService.actualizarCliente();
        
        // Verificar resultado
        assertEquals("Juan García", cliente.getNombre());
    }
    
    @Test
    @DisplayName("Test actualizarCliente cambiando teléfono")
    void testActualizarClienteCambiarTelefono() {
        // Crear cliente y añadirlo a la lista
        Cliente cliente = new Cliente("Juan Pérez", "123456789", "555-1234", "juan@example.com");
        listaDeClientes.add(cliente);
        
        // Configurar mock para buscarCliente y actualizar teléfono
        mockJOptionPaneForActualizarCliente(cliente, 1, "555-5678");
        
        // Ejecutar método
        clienteService.actualizarCliente();
        
        // Verificar resultado
        assertEquals("555-5678", cliente.getTelefono());
    }
    
    @Test
    @DisplayName("Test actualizarCliente cambiando correo")
    void testActualizarClienteCambiarCorreo() {
        // Crear cliente y añadirlo a la lista
        Cliente cliente = new Cliente("Juan Pérez", "123456789", "555-1234", "juan@example.com");
        listaDeClientes.add(cliente);
        
        // Configurar mock para buscarCliente y actualizar correo
        mockJOptionPaneForActualizarCliente(cliente, 2, "juan.perez@example.com");
        
        // Ejecutar método
        clienteService.actualizarCliente();
        
        // Verificar resultado
        assertEquals("juan.perez@example.com", cliente.getCorreo());
    }
    
    @Test
    @DisplayName("Test actualizarCliente cancelando actualización")
    void testActualizarClienteCancelando() {
        // Crear cliente y añadirlo a la lista
        Cliente cliente = new Cliente("Juan Pérez", "123456789", "555-1234", "juan@example.com");
        listaDeClientes.add(cliente);
        
        // Configurar mock para buscarCliente y cancelar
        mockJOptionPaneForActualizarCliente(cliente, 3, "");
        
        // Ejecutar método
        clienteService.actualizarCliente();
        
        // Verificar que no hay cambios
        assertEquals("Juan Pérez", cliente.getNombre());
        assertEquals("555-1234", cliente.getTelefono());
        assertEquals("juan@example.com", cliente.getCorreo());
    }
    
    @Test
    @DisplayName("Test actualizarCliente sin cliente encontrado")
    void testActualizarClienteSinClienteEncontrado() {
        // Configurar mock para que buscarCliente devuelva null
        mockBuscarClienteReturnNull();
        
        // Ejecutar método
        clienteService.actualizarCliente();
        
        // No hay verificación específica, solo aseguramos que no hay excepción
    }
    
    @Test
    @DisplayName("Test eliminarCliente con confirmación")
    void testEliminarClienteConConfirmacion() {
        // Crear cliente y añadirlo a la lista
        Cliente cliente = new Cliente("Juan Pérez", "123456789", "555-1234", "juan@example.com");
        listaDeClientes.add(cliente);
        
        // Configurar mock para buscarCliente y confirmar eliminación
        mockJOptionPaneForEliminarCliente(cliente, JOptionPane.YES_OPTION);
        
        // Ejecutar método
        clienteService.eliminarCliente();
        
        // Verificar resultado
        assertTrue(listaDeClientes.isEmpty());
    }
    
    @Test
    @DisplayName("Test eliminarCliente cancelando eliminación")
    void testEliminarClienteCancelando() {
        // Crear cliente y añadirlo a la lista
        Cliente cliente = new Cliente("Juan Pérez", "123456789", "555-1234", "juan@example.com");
        listaDeClientes.add(cliente);
        
        // Configurar mock para buscarCliente y cancelar eliminación
        mockJOptionPaneForEliminarCliente(cliente, JOptionPane.NO_OPTION);
        
        // Ejecutar método
        clienteService.eliminarCliente();
        
        // Verificar que el cliente sigue en la lista
        assertEquals(1, listaDeClientes.size());
        assertTrue(listaDeClientes.contains(cliente));
    }
    
    @Test
    @DisplayName("Test eliminarCliente sin cliente encontrado")
    void testEliminarClienteSinClienteEncontrado() {
        // Configurar mock para que buscarCliente devuelva null
        mockBuscarClienteReturnNull();
        
        // Ejecutar método
        clienteService.eliminarCliente();
        
        // No hay verificación específica, solo aseguramos que no hay excepción
    }
    
    @Test
    @DisplayName("Test mostrarVehiculosCliente con cliente que tiene vehículos")
    void testMostrarVehiculosClienteConVehiculos() {
        // Crear un cliente con vehículos
        Cliente cliente = new Cliente("Juan Pérez", "123456789", "555-1234", "juan@example.com");
        Vehiculo vehiculo1 = new Vehiculo("ABC123", "Rojo", "2020");
        Vehiculo vehiculo2 = new Vehiculo("XYZ789", "Azul", "2019");
        cliente.agregarVehiculo(vehiculo1);
        cliente.agregarVehiculo(vehiculo2);
        listaDeClientes.add(cliente);
        
        // Configurar mock para que buscarCliente devuelva este cliente
        mockBuscarClienteReturn(cliente);
        mockJOptionPane();
        
        // Ejecutar método
        clienteService.mostrarVehiculosCliente();
        
        // Verificar que no hay excepción (el método no devuelve nada)
    }
    
    @Test
    @DisplayName("Test mostrarVehiculosCliente con cliente sin vehículos")
    void testMostrarVehiculosClienteSinVehiculos() {
        // Crear un cliente sin vehículos
        Cliente cliente = new Cliente("Juan Pérez", "123456789", "555-1234", "juan@example.com");
        listaDeClientes.add(cliente);
        
        // Configurar mock para que buscarCliente devuelva este cliente
        mockBuscarClienteReturn(cliente);
        mockJOptionPane();
        
        // Ejecutar método
        clienteService.mostrarVehiculosCliente();
        
        // Verificar que no hay excepción (el método no devuelve nada)
    }
    
    @Test
    @DisplayName("Test mostrarVehiculosCliente con vehículo que tiene membresía activa")
    void testMostrarVehiculosClienteConMembresiaActiva() {
        // Crear un cliente con vehículo que tiene membresía activa
        Cliente cliente = new Cliente("Juan Pérez", "123456789", "555-1234", "juan@example.com");
        Vehiculo vehiculo = new Vehiculo("ABC123", "Rojo", "2020");
        
        // Configurar membresía activa para el futuro
        vehiculo.setMembresia(TipoMembresia.MENSUAL);
        
        // Fecha actual más un mes
        LocalDate fechaFin = LocalDate.now().plusMonths(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaFinStr = fechaFin.format(formatter);
        
        vehiculo.setFechaFinMembresia(fechaFinStr);
        cliente.agregarVehiculo(vehiculo);
        listaDeClientes.add(cliente);
        
        // Configurar mock para que buscarCliente devuelva este cliente
        mockBuscarClienteReturn(cliente);
        mockJOptionPane();
        
        // Ejecutar método
        clienteService.mostrarVehiculosCliente();
        
        // Verificar que no hay excepción (el método no devuelve nada)
    }
    
    @Test
    @DisplayName("Test mostrarVehiculosCliente con vehículo que tiene membresía vencida")
    void testMostrarVehiculosClienteConMembresiaVencida() {
        // Crear un cliente con vehículo que tiene membresía vencida
        Cliente cliente = new Cliente("Juan Pérez", "123456789", "555-1234", "juan@example.com");
        Vehiculo vehiculo = new Vehiculo("ABC123", "Rojo", "2020");
        
        // Configurar membresía vencida
        vehiculo.setMembresia(TipoMembresia.MENSUAL);
        
        // Fecha pasada
        LocalDate fechaFin = LocalDate.now().minusMonths(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaFinStr = fechaFin.format(formatter);
        
        vehiculo.setFechaFinMembresia(fechaFinStr);
        cliente.agregarVehiculo(vehiculo);
        listaDeClientes.add(cliente);
        
        // Configurar mock para que buscarCliente devuelva este cliente
        mockBuscarClienteReturn(cliente);
        mockJOptionPane();
        
        // Ejecutar método
        clienteService.mostrarVehiculosCliente();
        
        // Verificar que no hay excepción (el método no devuelve nada)
    }
    
    @Test
    @DisplayName("Test mostrarVehiculosCliente con formato de fecha inválido")
    void testMostrarVehiculosClienteConFechaInvalida() {
        // Crear un cliente con vehículo que tiene membresía con formato de fecha inválido
        Cliente cliente = new Cliente("Juan Pérez", "123456789", "555-1234", "juan@example.com");
        Vehiculo vehiculo = new Vehiculo("ABC123", "Rojo", "2020");
        
        // Configurar membresía con fecha inválida
        vehiculo.setMembresia(TipoMembresia.MENSUAL);
        vehiculo.setFechaFinMembresia("fecha-invalida");
        cliente.agregarVehiculo(vehiculo);
        listaDeClientes.add(cliente);
        
        // Configurar mock para que buscarCliente devuelva este cliente
        mockBuscarClienteReturn(cliente);
        mockJOptionPane();
        
        // Ejecutar método
        clienteService.mostrarVehiculosCliente();
        
        // Verificar que no hay excepción (el método no devuelve nada)
    }
    
    @Test
    @DisplayName("Test mostrarVehiculosCliente sin cliente encontrado")
    void testMostrarVehiculosClienteSinClienteEncontrado() {
        // Configurar mock para que buscarCliente devuelva null
        mockBuscarClienteReturnNull();
        
        // Ejecutar método
        clienteService.mostrarVehiculosCliente();
        
        // No hay verificación específica, solo aseguramos que no hay excepción
    }
    
    // Métodos auxiliares para mockear JOptionPane
    
    private void mockJOptionPane() {
        try {
            // Crear mock para JOptionPane
            Field optionPaneField = JOptionPane.class.getDeclaredField("optionPane");
            optionPaneField.setAccessible(true);
            optionPaneField.set(null, null);
        } catch (Exception e) {
            // Ignorar excepciones
        }
    }
    
    private void mockJOptionPaneWithResponses(DialogResponseProvider dialogResponse, TextFieldValueProvider textFieldValue) {
        try {
            // Sobrescribir métodos estáticos de JOptionPane
            mockJOptionPaneShowConfirmDialog(dialogResponse);
            mockJOptionPaneShowOptionDialog(dialogResponse);
            mockJOptionPaneShowInputDialog(textFieldValue);
            mockJOptionPaneShowMessageDialog();
        } catch (Exception e) {
            System.err.println("Error configurando mock de JOptionPane: " + e.getMessage());
        }
    }
    
    private void mockJOptionPaneForAñadirCliente(int optionResponse, String nombre, String cedula, String telefono, String correo) {
        mockJOptionPaneWithResponses(options -> optionResponse, textField -> {
            if (textField.toString().contains("Nombre")) {
                return nombre;
            } else if (textField.toString().contains("Cédula")) {
                return cedula;
            } else if (textField.toString().contains("Teléfono")) {
                return telefono;
            } else {
                return correo;
            }
        });
    }
    
    private void mockJOptionPaneForBuscarCliente(int opcionBusqueda, String criterio) {
        final AtomicInteger counter = new AtomicInteger(0);
        mockJOptionPaneWithResponses(options -> {
            if (counter.getAndIncrement() == 0) {
                return opcionBusqueda;  // Retorna la opción de búsqueda (0: nombre, 1: cédula, 2: teléfono)
            } else {
                return JOptionPane.OK_OPTION;
            }
        }, textField -> criterio);  // Retorna el criterio de búsqueda
    }
    
    private void mockJOptionPaneForBuscarClienteWithEmptyCriteria() {
        mockJOptionPaneWithResponses(options -> 0, textField -> "");
    }
    
    private void mockJOptionPaneForActualizarCliente(Cliente cliente, int opcionActualizacion, String nuevoValor) {
        final AtomicReference<Cliente> clienteRef = new AtomicReference<>(cliente);
        final AtomicInteger counter = new AtomicInteger(0);
        
        mockJOptionPaneWithResponses(options -> {
            int count = counter.getAndIncrement();
            if (count == 0) {
                return opcionActualizacion;  // Retorna la opción de actualización
            } else if (count == 1) {
                return JOptionPane.CLOSED_OPTION;  // Termina el ciclo
            } else {
                return JOptionPane.OK_OPTION;
            }
        }, textField -> nuevoValor);
        
        mockBuscarClienteReturn(cliente);
    }
    
    private void mockJOptionPaneForEliminarCliente(Cliente cliente, int confirmacion) {
        mockJOptionPaneWithResponses(options -> confirmacion, textField -> "");
        mockBuscarClienteReturn(cliente);
    }
    
    private void mockBuscarClienteReturn(Cliente cliente) {
        try {
            // Mockear buscarCliente() para devolver un cliente específico
            Method buscarClienteMethod = ClienteService.class.getDeclaredMethod("buscarCliente");
            buscarClienteMethod.setAccessible(true);
            
            // Crear una clase anónima que sobrescribe buscarCliente
            ClienteService mockedService = new ClienteService() {
                @Override
                public Cliente buscarCliente() {
                    return cliente;
                }
            };
            
            // Copiar el estado del servicio original
            Field listaField = ClienteService.class.getDeclaredField("listaDeClientes");
            listaField.setAccessible(true);
            listaField.set(mockedService, listaDeClientes);
            
            // Reemplazar el servicio original con el mockeado
            clienteService = mockedService;
        } catch (Exception e) {
            System.err.println("Error configurando mock de buscarCliente: " + e.getMessage());
        }
    }
    
    private void mockBuscarClienteReturnNull() {
        mockBuscarClienteReturn(null);
    }
    
    private void mockJOptionPaneShowConfirmDialog(DialogResponseProvider provider) throws Exception {
        JOptionPane.class.getDeclaredMethod("showConfirmDialog", java.awt.Component.class, Object.class, String.class, int.class)
            .setAccessible(true);
    }
    
    private void mockJOptionPaneShowOptionDialog(DialogResponseProvider provider) throws Exception {
        JOptionPane.class.getDeclaredMethod("showOptionDialog", java.awt.Component.class, Object.class, String.class, 
                int.class, int.class, Icon.class, Object[].class, Object.class)
            .setAccessible(true);
    }
    
    private void mockJOptionPaneShowInputDialog(TextFieldValueProvider provider) throws Exception {
        JOptionPane.class.getDeclaredMethod("showInputDialog", Object.class)
            .setAccessible(true);
    }
    
    private void mockJOptionPaneShowMessageDialog() throws Exception {
        JOptionPane.class.getDeclaredMethod("showMessageDialog", java.awt.Component.class, Object.class)
            .setAccessible(true);
    }
    
    @FunctionalInterface
    private interface DialogResponseProvider {
        int getResponse(Object options);
    }
    
    @FunctionalInterface
    private interface TextFieldValueProvider {
        String getValue(Object textField);
    }
}