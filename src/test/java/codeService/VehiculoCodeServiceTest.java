package codeService;

import model.Cliente;
import model.Vehiculo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class VehiculoCodeServiceTest {

    private VehiculoCodeService vehiculoService;
    private Cliente cliente1;
    private Cliente cliente2;

    @BeforeEach
    void setUp() {
        vehiculoService = new VehiculoCodeService();
        cliente1 = new Cliente("Juan Pérez", "1234567890", "3001234567", "juan@example.com");
        cliente2 = new Cliente("María Gómez", "0987654321", "3009876543", "maria@example.com");
    }

    @Nested
    @DisplayName("Tests para registrarVehiculo(String, String, String)")
    class RegistrarVehiculoSinCliente {

        @Test
        @DisplayName("Registrar vehículo con datos válidos")
        void testRegistrarVehiculoConDatosValidos() {
            boolean resultado = vehiculoService.registrarVehiculo("ABC123", "Rojo", "Toyota Corolla");
            assertTrue(resultado);
            assertEquals(1, vehiculoService.obtenerTodosLosVehiculos().size());
        }

        @Test
        @DisplayName("Registrar vehículo con placa null")
        void testRegistrarVehiculoConPlacaNull() {
            boolean resultado = vehiculoService.registrarVehiculo(null, "Rojo", "Toyota Corolla");
            assertFalse(resultado);
            assertEquals(0, vehiculoService.obtenerTodosLosVehiculos().size());
        }

        @Test
        @DisplayName("Registrar vehículo con placa vacía")
        void testRegistrarVehiculoConPlacaVacia() {
            boolean resultado = vehiculoService.registrarVehiculo("", "Rojo", "Toyota Corolla");
            assertFalse(resultado);
            assertEquals(0, vehiculoService.obtenerTodosLosVehiculos().size());
        }

        @Test
        @DisplayName("Registrar vehículo con color null")
        void testRegistrarVehiculoConColorNull() {
            boolean resultado = vehiculoService.registrarVehiculo("ABC123", null, "Toyota Corolla");
            assertFalse(resultado);
            assertEquals(0, vehiculoService.obtenerTodosLosVehiculos().size());
        }

        @Test
        @DisplayName("Registrar vehículo con color vacío")
        void testRegistrarVehiculoConColorVacio() {
            boolean resultado = vehiculoService.registrarVehiculo("ABC123", "", "Toyota Corolla");
            assertFalse(resultado);
            assertEquals(0, vehiculoService.obtenerTodosLosVehiculos().size());
        }

        @Test
        @DisplayName("Registrar vehículo con modelo null")
        void testRegistrarVehiculoConModeloNull() {
            boolean resultado = vehiculoService.registrarVehiculo("ABC123", "Rojo", null);
            assertFalse(resultado);
            assertEquals(0, vehiculoService.obtenerTodosLosVehiculos().size());
        }

        @Test
        @DisplayName("Registrar vehículo con modelo vacío")
        void testRegistrarVehiculoConModeloVacio() {
            boolean resultado = vehiculoService.registrarVehiculo("ABC123", "Rojo", "");
            assertFalse(resultado);
            assertEquals(0, vehiculoService.obtenerTodosLosVehiculos().size());
        }

        @Test
        @DisplayName("Registrar vehículo con placa duplicada")
        void testRegistrarVehiculoConPlacaDuplicada() {
            vehiculoService.registrarVehiculo("ABC123", "Rojo", "Toyota Corolla");
            boolean resultado = vehiculoService.registrarVehiculo("ABC123", "Azul", "Honda Civic");
            assertFalse(resultado);
            assertEquals(1, vehiculoService.obtenerTodosLosVehiculos().size());
        }

        @Test
        @DisplayName("Registrar vehículo con placa en diferente case")
        void testRegistrarVehiculoConPlacaDiferenteCase() {
            vehiculoService.registrarVehiculo("ABC123", "Rojo", "Toyota Corolla");
            boolean resultado = vehiculoService.registrarVehiculo("abc123", "Azul", "Honda Civic");
            assertFalse(resultado);
            assertEquals(1, vehiculoService.obtenerTodosLosVehiculos().size());
        }
    }

    @Nested
    @DisplayName("Tests para registrarVehiculo(Cliente, String, String, String)")
    class RegistrarVehiculoConCliente {

        @Test
        @DisplayName("Registrar vehículo de cliente con datos válidos")
        void testRegistrarVehiculoClienteConDatosValidos() {
            boolean resultado = vehiculoService.registrarVehiculo(cliente1, "ABC123", "Rojo", "Toyota Corolla");
            assertTrue(resultado);
            assertEquals(1, vehiculoService.obtenerTodosLosVehiculos().size());
            assertEquals(1, vehiculoService.obtenerVehiculosPorCliente(cliente1).size());
        }

        @Test
        @DisplayName("Registrar vehículo con cliente null")
        void testRegistrarVehiculoConClienteNull() {
            boolean resultado = vehiculoService.registrarVehiculo(null, "ABC123", "Rojo", "Toyota Corolla");
            assertFalse(resultado);
            assertEquals(0, vehiculoService.obtenerTodosLosVehiculos().size());
        }

        @Test
        @DisplayName("Registrar vehículo de cliente con placa null")
        void testRegistrarVehiculoClienteConPlacaNull() {
            boolean resultado = vehiculoService.registrarVehiculo(cliente1, null, "Rojo", "Toyota Corolla");
            assertFalse(resultado);
            assertEquals(0, vehiculoService.obtenerVehiculosPorCliente(cliente1).size());
        }

        @Test
        @DisplayName("Registrar vehículo de cliente con placa vacía")
        void testRegistrarVehiculoClienteConPlacaVacia() {
            boolean resultado = vehiculoService.registrarVehiculo(cliente1, "", "Rojo", "Toyota Corolla");
            assertFalse(resultado);
            assertEquals(0, vehiculoService.obtenerVehiculosPorCliente(cliente1).size());
        }

        @Test
        @DisplayName("Registrar vehículo de cliente con placa duplicada")
        void testRegistrarVehiculoClienteConPlacaDuplicada() {
            vehiculoService.registrarVehiculo(cliente1, "ABC123", "Rojo", "Toyota Corolla");
            boolean resultado = vehiculoService.registrarVehiculo(cliente2, "ABC123", "Azul", "Honda Civic");
            assertFalse(resultado);
            assertEquals(1, vehiculoService.obtenerTodosLosVehiculos().size());
            assertEquals(1, vehiculoService.obtenerVehiculosPorCliente(cliente1).size());
            assertEquals(0, vehiculoService.obtenerVehiculosPorCliente(cliente2).size());
        }

        @Test
        @DisplayName("Registrar múltiples vehículos para un cliente")
        void testRegistrarMultiplesVehiculosParaUnCliente() {
            vehiculoService.registrarVehiculo(cliente1, "ABC123", "Rojo", "Toyota Corolla");
            vehiculoService.registrarVehiculo(cliente1, "XYZ789", "Azul", "Honda Civic");
            assertEquals(2, vehiculoService.obtenerTodosLosVehiculos().size());
            assertEquals(2, vehiculoService.obtenerVehiculosPorCliente(cliente1).size());
        }

        @Test
        @DisplayName("Registrar vehículos para múltiples clientes")
        void testRegistrarVehiculosParaMultiplesClientes() {
            vehiculoService.registrarVehiculo(cliente1, "ABC123", "Rojo", "Toyota Corolla");
            vehiculoService.registrarVehiculo(cliente2, "XYZ789", "Azul", "Honda Civic");
            assertEquals(2, vehiculoService.obtenerTodosLosVehiculos().size());
            assertEquals(1, vehiculoService.obtenerVehiculosPorCliente(cliente1).size());
            assertEquals(1, vehiculoService.obtenerVehiculosPorCliente(cliente2).size());
        }
    }

    @Nested
    @DisplayName("Tests para buscarVehiculo(String)")
    class BuscarVehiculo {

        @Test
        @DisplayName("Buscar vehículo existente")
        void testBuscarVehiculoExistente() {
            vehiculoService.registrarVehiculo("ABC123", "Rojo", "Toyota Corolla");
            Vehiculo vehiculo = vehiculoService.buscarVehiculo("ABC123");
            assertNotNull(vehiculo);
            assertEquals("ABC123", vehiculo.getPlaca());
            assertEquals("Rojo", vehiculo.getColor());
            assertEquals("Toyota Corolla", vehiculo.getModelo());
        }

        @Test
        @DisplayName("Buscar vehículo no existente")
        void testBuscarVehiculoNoExistente() {
            Vehiculo vehiculo = vehiculoService.buscarVehiculo("ABC123");
            assertNull(vehiculo);
        }

        @Test
        @DisplayName("Buscar vehículo con placa null")
        void testBuscarVehiculoConPlacaNull() {
            Vehiculo vehiculo = vehiculoService.buscarVehiculo(null);
            assertNull(vehiculo);
        }

        @Test
        @DisplayName("Buscar vehículo con placa vacía")
        void testBuscarVehiculoConPlacaVacia() {
            Vehiculo vehiculo = vehiculoService.buscarVehiculo("");
            assertNull(vehiculo);
        }

        @Test
        @DisplayName("Buscar vehículo con diferente case")
        void testBuscarVehiculoConDiferenteCase() {
            vehiculoService.registrarVehiculo("ABC123", "Rojo", "Toyota Corolla");
            Vehiculo vehiculo = vehiculoService.buscarVehiculo("abc123");
            assertNotNull(vehiculo);
            assertEquals("ABC123", vehiculo.getPlaca());
        }
    }

    @Nested
    @DisplayName("Tests para actualizarVehiculo(String, String, String, String)")
    class ActualizarVehiculo {

        @Test
        @DisplayName("Actualizar vehículo existente")
        void testActualizarVehiculoExistente() {
            vehiculoService.registrarVehiculo("ABC123", "Rojo", "Toyota Corolla");
            boolean resultado = vehiculoService.actualizarVehiculo("ABC123", "XYZ789", "Azul", "Honda Civic");
            assertTrue(resultado);
            
            Vehiculo vehiculo = vehiculoService.buscarVehiculo("XYZ789");
            assertNotNull(vehiculo);
            assertEquals("XYZ789", vehiculo.getPlaca());
            assertEquals("Azul", vehiculo.getColor());
            assertEquals("Honda Civic", vehiculo.getModelo());
            
            // Verificar que no se puede encontrar con la placa antigua
            assertNull(vehiculoService.buscarVehiculo("ABC123"));
        }

        @Test
        @DisplayName("Actualizar vehículo no existente")
        void testActualizarVehiculoNoExistente() {
            boolean resultado = vehiculoService.actualizarVehiculo("ABC123", "XYZ789", "Azul", "Honda Civic");
            assertFalse(resultado);
        }

        @Test
        @DisplayName("Actualizar vehículo con placa actual null")
        void testActualizarVehiculoConPlacaActualNull() {
            boolean resultado = vehiculoService.actualizarVehiculo(null, "XYZ789", "Azul", "Honda Civic");
            assertFalse(resultado);
        }

        @Test
        @DisplayName("Actualizar solo el color")
        void testActualizarSoloColor() {
            vehiculoService.registrarVehiculo("ABC123", "Rojo", "Toyota Corolla");
            boolean resultado = vehiculoService.actualizarVehiculo("ABC123", null, "Azul", null);
            assertTrue(resultado);
            
            Vehiculo vehiculo = vehiculoService.buscarVehiculo("ABC123");
            assertNotNull(vehiculo);
            assertEquals("ABC123", vehiculo.getPlaca());
            assertEquals("Azul", vehiculo.getColor());
            assertEquals("Toyota Corolla", vehiculo.getModelo());
        }

        @Test
        @DisplayName("Actualizar solo el modelo")
        void testActualizarSoloModelo() {
            vehiculoService.registrarVehiculo("ABC123", "Rojo", "Toyota Corolla");
            boolean resultado = vehiculoService.actualizarVehiculo("ABC123", null, null, "Honda Civic");
            assertTrue(resultado);
            
            Vehiculo vehiculo = vehiculoService.buscarVehiculo("ABC123");
            assertNotNull(vehiculo);
            assertEquals("ABC123", vehiculo.getPlaca());
            assertEquals("Rojo", vehiculo.getColor());
            assertEquals("Honda Civic", vehiculo.getModelo());
        }

        @Test
        @DisplayName("Actualizar a una placa ya existente")
        void testActualizarAPlacaYaExistente() {
            vehiculoService.registrarVehiculo("ABC123", "Rojo", "Toyota Corolla");
            vehiculoService.registrarVehiculo("XYZ789", "Azul", "Honda Civic");
            
            boolean resultado = vehiculoService.actualizarVehiculo("ABC123", "XYZ789", "Verde", "Nissan Sentra");
            assertFalse(resultado);
            
            // Verificar que no se realizaron cambios
            Vehiculo vehiculo1 = vehiculoService.buscarVehiculo("ABC123");
            assertNotNull(vehiculo1);
            assertEquals("Rojo", vehiculo1.getColor());
            assertEquals("Toyota Corolla", vehiculo1.getModelo());
            
            Vehiculo vehiculo2 = vehiculoService.buscarVehiculo("XYZ789");
            assertNotNull(vehiculo2);
            assertEquals("Azul", vehiculo2.getColor());
            assertEquals("Honda Civic", vehiculo2.getModelo());
        }
    }

    @Nested
    @DisplayName("Tests para obtenerVehiculosPorCliente(Cliente)")
    class ObtenerVehiculosPorCliente {

        @Test
        @DisplayName("Obtener vehículos de cliente con vehículos")
        void testObtenerVehiculosDeClienteConVehiculos() {
            vehiculoService.registrarVehiculo(cliente1, "ABC123", "Rojo", "Toyota Corolla");
            vehiculoService.registrarVehiculo(cliente1, "XYZ789", "Azul", "Honda Civic");
            
            List<Vehiculo> vehiculos = vehiculoService.obtenerVehiculosPorCliente(cliente1);
            assertEquals(2, vehiculos.size());
            
            // Verificar que los vehículos son los correctos
            boolean tieneVehiculo1 = vehiculos.stream()
                    .anyMatch(v -> v.getPlaca().equals("ABC123"));
            boolean tieneVehiculo2 = vehiculos.stream()
                    .anyMatch(v -> v.getPlaca().equals("XYZ789"));
            
            assertTrue(tieneVehiculo1);
            assertTrue(tieneVehiculo2);
        }

        @Test
        @DisplayName("Obtener vehículos de cliente sin vehículos")
        void testObtenerVehiculosDeClienteSinVehiculos() {
            List<Vehiculo> vehiculos = vehiculoService.obtenerVehiculosPorCliente(cliente1);
            assertTrue(vehiculos.isEmpty());
        }

        @Test
        @DisplayName("Obtener vehículos con cliente null")
        void testObtenerVehiculosConClienteNull() {
            List<Vehiculo> vehiculos = vehiculoService.obtenerVehiculosPorCliente(null);
            assertTrue(vehiculos.isEmpty());
        }

        @Test
        @DisplayName("Obtener vehículos de cliente con cédula null")
        void testObtenerVehiculosDeClienteConCedulaNull() {
            Cliente clienteInvalido = new Cliente("Sin Cédula", null, "3001234567", "sincedula@example.com");
            List<Vehiculo> vehiculos = vehiculoService.obtenerVehiculosPorCliente(clienteInvalido);
            assertTrue(vehiculos.isEmpty());
        }
    }

    @Nested
    @DisplayName("Tests para obtenerTodosLosVehiculos()")
    class ObtenerTodosLosVehiculos {

        @Test
        @DisplayName("Obtener todos los vehículos cuando hay vehículos")
        void testObtenerTodosLosVehiculosCuandoHayVehiculos() {
            vehiculoService.registrarVehiculo("ABC123", "Rojo", "Toyota Corolla");
            vehiculoService.registrarVehiculo("XYZ789", "Azul", "Honda Civic");
            vehiculoService.registrarVehiculo(cliente1, "DEF456", "Verde", "Nissan Sentra");
            
            List<Vehiculo> vehiculos = vehiculoService.obtenerTodosLosVehiculos();
            assertEquals(3, vehiculos.size());
        }

        @Test
        @DisplayName("Obtener todos los vehículos cuando no hay vehículos")
        void testObtenerTodosLosVehiculosCuandoNoHayVehiculos() {
            List<Vehiculo> vehiculos = vehiculoService.obtenerTodosLosVehiculos();
            assertTrue(vehiculos.isEmpty());
        }
        
        @Test
        @DisplayName("Modificar la lista devuelta no afecta a la original")
        void testModificarListaDevueltaNoAfectaOriginal() {
            vehiculoService.registrarVehiculo("ABC123", "Rojo", "Toyota Corolla");
            
            List<Vehiculo> vehiculos = vehiculoService.obtenerTodosLosVehiculos();
            assertEquals(1, vehiculos.size());
            
            // Intentar modificar la lista devuelta
            vehiculos.clear();
            
            // Verificar que la lista original no se modificó
            List<Vehiculo> vehiculosNuevamente = vehiculoService.obtenerTodosLosVehiculos();
            assertEquals(1, vehiculosNuevamente.size());
        }
    }

    @Nested
    @DisplayName("Tests para eliminarVehiculo(String)")
    class EliminarVehiculo {

        @Test
        @DisplayName("Eliminar vehículo existente")
        void testEliminarVehiculoExistente() {
            vehiculoService.registrarVehiculo("ABC123", "Rojo", "Toyota Corolla");
            boolean resultado = vehiculoService.eliminarVehiculo("ABC123");
            assertTrue(resultado);
            assertEquals(0, vehiculoService.obtenerTodosLosVehiculos().size());
        }

        @Test
        @DisplayName("Eliminar vehículo no existente")
        void testEliminarVehiculoNoExistente() {
            boolean resultado = vehiculoService.eliminarVehiculo("ABC123");
            assertFalse(resultado);
        }

        @Test
        @DisplayName("Eliminar vehículo con placa null")
        void testEliminarVehiculoConPlacaNull() {
            boolean resultado = vehiculoService.eliminarVehiculo(null);
            assertFalse(resultado);
        }

        @Test
        @DisplayName("Eliminar vehículo con placa vacía")
        void testEliminarVehiculoConPlacaVacia() {
            boolean resultado = vehiculoService.eliminarVehiculo("");
            assertFalse(resultado);
        }

        @Test
        @DisplayName("Eliminar vehículo asociado a cliente")
        void testEliminarVehiculoAsociadoACliente() {
            vehiculoService.registrarVehiculo(cliente1, "ABC123", "Rojo", "Toyota Corolla");
            boolean resultado = vehiculoService.eliminarVehiculo("ABC123");
            assertTrue(resultado);
            
            // Verificar que se eliminó de la lista general
            assertEquals(0, vehiculoService.obtenerTodosLosVehiculos().size());
            
            // Verificar que se eliminó de la lista del cliente
            assertEquals(0, vehiculoService.obtenerVehiculosPorCliente(cliente1).size());
        }
    }

    @Nested
    @DisplayName("Tests para clienteTieneVehiculos(Cliente)")
    class ClienteTieneVehiculos {

        @Test
        @DisplayName("Cliente con vehículos")
        void testClienteConVehiculos() {
            vehiculoService.registrarVehiculo(cliente1, "ABC123", "Rojo", "Toyota Corolla");
            assertTrue(vehiculoService.clienteTieneVehiculos(cliente1));
        }

        @Test
        @DisplayName("Cliente sin vehículos")
        void testClienteSinVehiculos() {
            assertFalse(vehiculoService.clienteTieneVehiculos(cliente1));
        }

        @Test
        @DisplayName("Cliente null")
        void testClienteNull() {
            assertFalse(vehiculoService.clienteTieneVehiculos(null));
        }
    }

    @Nested
    @DisplayName("Tests para contarVehiculosPorCliente(Cliente)")
    class ContarVehiculosPorCliente {

        @Test
        @DisplayName("Contar vehículos de cliente con múltiples vehículos")
        void testContarVehiculosDeClienteConMultiplesVehiculos() {
            vehiculoService.registrarVehiculo(cliente1, "ABC123", "Rojo", "Toyota Corolla");
            vehiculoService.registrarVehiculo(cliente1, "XYZ789", "Azul", "Honda Civic");
            vehiculoService.registrarVehiculo(cliente1, "DEF456", "Verde", "Nissan Sentra");
            
            assertEquals(3, vehiculoService.contarVehiculosPorCliente(cliente1));
        }

        @Test
        @DisplayName("Contar vehículos de cliente con un vehículo")
        void testContarVehiculosDeClienteConUnVehiculo() {
            vehiculoService.registrarVehiculo(cliente1, "ABC123", "Rojo", "Toyota Corolla");
            assertEquals(1, vehiculoService.contarVehiculosPorCliente(cliente1));
        }

        @Test
        @DisplayName("Contar vehículos de cliente sin vehículos")
        void testContarVehiculosDeClienteSinVehiculos() {
            assertEquals(0, vehiculoService.contarVehiculosPorCliente(cliente1));
        }

        @Test
        @DisplayName("Contar vehículos con cliente null")
        void testContarVehiculosConClienteNull() {
            assertEquals(0, vehiculoService.contarVehiculosPorCliente(null));
        }
    }

    @Nested
    @DisplayName("Tests para existeVehiculo(String)")
    class ExisteVehiculo {

        @Test
        @DisplayName("Verificar vehículo existente")
        void testVerificarVehiculoExistente() {
            vehiculoService.registrarVehiculo("ABC123", "Rojo", "Toyota Corolla");
            assertTrue(vehiculoService.existeVehiculo("ABC123"));
        }

        @Test
        @DisplayName("Verificar vehículo no existente")
        void testVerificarVehiculoNoExistente() {
            assertFalse(vehiculoService.existeVehiculo("ABC123"));
        }

        @Test
        @DisplayName("Verificar con placa null")
        void testVerificarConPlacaNull() {
            assertFalse(vehiculoService.existeVehiculo(null));
        }

        @Test
        @DisplayName("Verificar con placa vacía")
        void testVerificarConPlacaVacia() {
            assertFalse(vehiculoService.existeVehiculo(""));
        }

        @Test
        @DisplayName("Verificar con diferente case")
        void testVerificarConDiferenteCase() {
            vehiculoService.registrarVehiculo("ABC123", "Rojo", "Toyota Corolla");
            assertTrue(vehiculoService.existeVehiculo("abc123"));
        }
    }

    @Nested
    @DisplayName("Tests para obtenerDetallesVehiculo(String)")
    class ObtenerDetallesVehiculo {

        @Test
        @DisplayName("Obtener detalles de vehículo existente")
        void testObtenerDetallesDeVehiculoExistente() {
            vehiculoService.registrarVehiculo("ABC123", "Rojo", "Toyota Corolla");
            Map<String, String> detalles = vehiculoService.obtenerDetallesVehiculo("ABC123");
            
            assertNotNull(detalles);
            assertEquals("ABC123", detalles.get("placa"));
            assertEquals("Rojo", detalles.get("color"));
            assertEquals("Toyota Corolla", detalles.get("modelo"));
        }

        @Test
        @DisplayName("Obtener detalles de vehículo no existente")
        void testObtenerDetallesDeVehiculoNoExistente() {
            Map<String, String> detalles = vehiculoService.obtenerDetallesVehiculo("ABC123");
            assertNull(detalles);
        }

        @Test
        @DisplayName("Obtener detalles con placa null")
        void testObtenerDetallesConPlacaNull() {
            Map<String, String> detalles = vehiculoService.obtenerDetallesVehiculo(null);
            assertNull(detalles);
        }

        @Test
        @DisplayName("Obtener detalles con placa vacía")
        void testObtenerDetallesConPlacaVacia() {
            Map<String, String> detalles = vehiculoService.obtenerDetallesVehiculo("");
            assertNull(detalles);
        }
    }

    @Nested
    @DisplayName("Tests para buscarPropietarioVehiculo(String)")
    class BuscarPropietarioVehiculo {

        @Test
        @DisplayName("Buscar propietario de vehículo existente")
        void testBuscarPropietarioDeVehiculoExistente() {
            vehiculoService.registrarVehiculo(cliente1, "ABC123", "Rojo", "Toyota Corolla");
            String cedula = vehiculoService.buscarPropietarioVehiculo("ABC123");
            
            assertNotNull(cedula);
            assertEquals(cliente1.getCedula(), cedula);
        }

        @Test
        @DisplayName("Buscar propietario de vehículo no existente")
        void testBuscarPropietarioDeVehiculoNoExistente() {
            String cedula = vehiculoService.buscarPropietarioVehiculo("ABC123");
            assertNull(cedula);
        }

        @Test
        @DisplayName("Buscar propietario con placa null")
        void testBuscarPropietarioConPlacaNull() {
            String cedula = vehiculoService.buscarPropietarioVehiculo(null);
            assertNull(cedula);
        }

        @Test
        @DisplayName("Buscar propietario con placa vacía")
        void testBuscarPropietarioConPlacaVacia() {
            String cedula = vehiculoService.buscarPropietarioVehiculo("");
            assertNull(cedula);
        }

        @Test
        @DisplayName("Buscar propietario de vehículo sin propietario")
        void testBuscarPropietarioDeVehiculoSinPropietario() {
            vehiculoService.registrarVehiculo("ABC123", "Rojo", "Toyota Corolla");
            String cedula = vehiculoService.buscarPropietarioVehiculo("ABC123");
            assertNull(cedula);
        }
    }

    @Nested
    @DisplayName("Tests para asignarVehiculoACliente(Cliente, String)")
    class AsignarVehiculoACliente {

        @Test
        @DisplayName("Asignar vehículo existente a cliente")
        void testAsignarVehiculoExistenteACliente() {
            vehiculoService.registrarVehiculo("ABC123", "Rojo", "Toyota Corolla");
            boolean resultado = vehiculoService.asignarVehiculoACliente(cliente1, "ABC123");
            
            assertTrue(resultado);
            assertEquals(1, vehiculoService.obtenerVehiculosPorCliente(cliente1).size());
        }

        @Test
        @DisplayName("Asignar vehículo no existente a cliente")
        void testAsignarVehiculoNoExistenteACliente() {
            boolean resultado = vehiculoService.asignarVehiculoACliente(cliente1, "ABC123");
            assertFalse(resultado);
        }

        @Test
        @DisplayName("Asignar vehículo con cliente null")
        void testAsignarVehiculoConClienteNull() {
            vehiculoService.registrarVehiculo("ABC123", "Rojo", "Toyota Corolla");
            boolean resultado = vehiculoService.asignarVehiculoACliente(null, "ABC123");
            assertFalse(resultado);
        }

        @Test
        @DisplayName("Asignar vehículo con placa null")
        void testAsignarVehiculoConPlacaNull() {
            boolean resultado = vehiculoService.asignarVehiculoACliente(cliente1, null);
            assertFalse(resultado);
        }

        @Test
        @DisplayName("Asignar vehículo ya asignado al mismo cliente")
        void testAsignarVehiculoYaAsignadoAlMismoCliente() {
            vehiculoService.registrarVehiculo("ABC123", "Rojo", "Toyota Corolla");
            vehiculoService.asignarVehiculoACliente(cliente1, "ABC123");
            
            boolean resultado = vehiculoService.asignarVehiculoACliente(cliente1, "ABC123");
            assertFalse(resultado);
            assertEquals(1, vehiculoService.obtenerVehiculosPorCliente(cliente1).size());
        }
    }

    @Nested
    @DisplayName("Tests para desvincularVehiculoDeCliente(Cliente, String)")
    class DesvincularVehiculoDeCliente {

        @Test
        @DisplayName("Desvincular vehículo de cliente")
        void testDesvincularVehiculoDeCliente() {
            vehiculoService.registrarVehiculo(cliente1, "ABC123", "Rojo", "Toyota Corolla");
            boolean resultado = vehiculoService.desvincularVehiculoDeCliente(cliente1, "ABC123");
            
            assertTrue(resultado);
            assertEquals(0, vehiculoService.obtenerVehiculosPorCliente(cliente1).size());
            assertEquals(1, vehiculoService.obtenerTodosLosVehiculos().size()); // El vehículo sigue en la lista general
        }

        @Test
        @DisplayName("Desvincular vehículo no existente")
        void testDesvincularVehiculoNoExistente() {
            boolean resultado = vehiculoService.desvincularVehiculoDeCliente(cliente1, "ABC123");
            assertFalse(resultado);
        }

        @Test
        @DisplayName("Desvincular vehículo con cliente null")
        void testDesvincularVehiculoConClienteNull() {
            boolean resultado = vehiculoService.desvincularVehiculoDeCliente(null, "ABC123");
            assertFalse(resultado);
        }

        @Test
        @DisplayName("Desvincular vehículo con placa null")
        void testDesvincularVehiculoConPlacaNull() {
            boolean resultado = vehiculoService.desvincularVehiculoDeCliente(cliente1, null);
            assertFalse(resultado);
        }

        @Test
        @DisplayName("Desvincular vehículo de cliente que no tiene vehículos")
        void testDesvincularVehiculoDeClienteSinVehiculos() {
            boolean resultado = vehiculoService.desvincularVehiculoDeCliente(cliente1, "ABC123");
            assertFalse(resultado);
        }
    }

    @Nested
    @DisplayName("Tests para vehiculoPerteneceACliente(Cliente, String)")
    class VehiculoPerteneceACliente {

        @Test
        @DisplayName("Verificar que vehículo pertenece a cliente")
        void testVerificarQueVehiculoPerteneceACliente() {
            vehiculoService.registrarVehiculo(cliente1, "ABC123", "Rojo", "Toyota Corolla");
            assertTrue(vehiculoService.vehiculoPerteneceACliente(cliente1, "ABC123"));
        }

        @Test
        @DisplayName("Verificar que vehículo no pertenece a cliente")
        void testVerificarQueVehiculoNoPerteneceACliente() {
            vehiculoService.registrarVehiculo(cliente2, "ABC123", "Rojo", "Toyota Corolla");
            assertFalse(vehiculoService.vehiculoPerteneceACliente(cliente1, "ABC123"));
        }

        @Test
        @DisplayName("Verificar con cliente null")
        void testVerificarConClienteNull() {
            vehiculoService.registrarVehiculo("ABC123", "Rojo", "Toyota Corolla");
            assertFalse(vehiculoService.vehiculoPerteneceACliente(null, "ABC123"));
        }

        @Test
        @DisplayName("Verificar con placa null")
        void testVerificarConPlacaNull() {
            assertFalse(vehiculoService.vehiculoPerteneceACliente(cliente1, null));
        }

        @Test
        @DisplayName("Verificar con placa vacía")
        void testVerificarConPlacaVacia() {
            assertFalse(vehiculoService.vehiculoPerteneceACliente(cliente1, ""));
        }
    }

    @Nested
    @DisplayName("Tests para verVehiculosAsociados(Cliente)")
    class VerVehiculosAsociados {

        @Test
        @DisplayName("Ver vehículos asociados a cliente con vehículos")
        void testVerVehiculosAsociadosAClienteConVehiculos() {
            vehiculoService.registrarVehiculo(cliente1, "ABC123", "Rojo", "Toyota Corolla");
            vehiculoService.registrarVehiculo(cliente1, "XYZ789", "Azul", "Honda Civic");
            
            List<Vehiculo> vehiculos = vehiculoService.verVehiculosAsociados(cliente1);
            assertEquals(2, vehiculos.size());
        }

        @Test
        @DisplayName("Ver vehículos asociados a cliente sin vehículos")
        void testVerVehiculosAsociadosAClienteSinVehiculos() {
            List<Vehiculo> vehiculos = vehiculoService.verVehiculosAsociados(cliente1);
            assertTrue(vehiculos.isEmpty());
        }

        @Test
        @DisplayName("Ver vehículos asociados con cliente null")
        void testVerVehiculosAsociadosConClienteNull() {
            List<Vehiculo> vehiculos = vehiculoService.verVehiculosAsociados(null);
            assertTrue(vehiculos.isEmpty());
        }
    }
}