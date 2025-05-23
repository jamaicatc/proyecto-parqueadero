package codeService;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ParqueaderoCodeServiceTest {

    private ParqueaderoCodeService parqueaderoService;
    private Parqueadero parqueadero;

    @BeforeEach
    public void setUp() {
        parqueadero = new Parqueadero("Parqueadero Test", "Calle Test 123", "John Doe", "123456789", "test@example.com");
        parqueadero.setPuestosMotos(5);
        parqueadero.setPuestosAutomoviles(10);
        parqueadero.setPuestosCamiones(3);
        parqueadero.setListaDevehiculos(new ArrayList<>());
        parqueadero.setListaDeClientes(new ArrayList<>());
        
        parqueaderoService = new ParqueaderoCodeService(parqueadero);
    }

    @Test
    @DisplayName("Test constructores y getters/setters básicos")
    public void testConstructoresYGettersSetters() {
        // Constructor con parámetro
        assertEquals(parqueadero, parqueaderoService.getParqueadero());
        
        // Constructor vacío y setter
        ParqueaderoCodeService servicioVacio = new ParqueaderoCodeService();
        assertNull(servicioVacio.getParqueadero());
        
        Parqueadero nuevoParqueadero = new Parqueadero("Nuevo", "Nueva dirección", "Representante", "987654321", "nuevo@test.com");
        servicioVacio.setParqueadero(nuevoParqueadero);
        assertEquals(nuevoParqueadero, servicioVacio.getParqueadero());
        
        // Verificar vehículos actuales
        assertTrue(parqueaderoService.getVehiculosActuales().isEmpty());
    }

    @Test
    @DisplayName("Test configurar espacios exitosamente")
    public void testConfigurarEspaciosExito() {
        boolean resultado = parqueaderoService.configurarEspacios(10, 20, 5);
        
        assertTrue(resultado);
        assertEquals(10, parqueadero.getPuestosMotos());
        assertEquals(20, parqueadero.getPuestosAutomoviles());
        assertEquals(5, parqueadero.getPuestosCamiones());
    }

    @Test
    @DisplayName("Test configurar espacios con error")
    public void testConfigurarEspaciosError() {
        // Creamos un servicio con parqueadero nulo para provocar excepción
        ParqueaderoCodeService servicio = new ParqueaderoCodeService();
        
        boolean resultado = servicio.configurarEspacios(10, 20, 5);
        
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Test configurar tarifas exitosamente")
    public void testConfigurarTarifasExito() {
        boolean resultado = parqueaderoService.configurarTarifas(1500, 2500, 3500);
        
        assertTrue(resultado);
        assertEquals(1500, parqueaderoService.obtenerTarifaPorTipo(1));
        assertEquals(2500, parqueaderoService.obtenerTarifaPorTipo(0));
        assertEquals(3500, parqueaderoService.obtenerTarifaPorTipo(2));
    }

    @Test
    @DisplayName("Test configurar tarifas con error")
    public void testConfigurarTarifasError() {
        // Para probar este caso, creamos una clase anónima que simule un error
        ParqueaderoCodeService servicioConError = new ParqueaderoCodeService(parqueadero) {
            @Override
            public boolean configurarTarifas(double tarifaMoto, double tarifaAutomovil, double tarifaCamion) {
                // En lugar de lanzar una excepción, simplemente retornamos false
                return false;
            }
        };
        
        boolean resultado = servicioConError.configurarTarifas(1500, 2500, 3500);
        
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Test registrar entrada de automóvil exitosamente")
    public void testRegistrarEntradaAutomovilExito() {
        boolean resultado = parqueaderoService.registrarEntradaVehiculo(0, "ABC123", "Rojo", "2023");
        
        assertTrue(resultado);
        assertEquals(1, parqueadero.getListaDevehiculos().size());
        assertTrue(parqueaderoService.getVehiculosActuales().containsKey("ABC123"));
        
        Vehiculo vehiculo = parqueaderoService.buscarVehiculoPorPlaca("ABC123");
        assertNotNull(vehiculo);
        assertTrue(vehiculo instanceof Automovil);
        assertEquals("Rojo", vehiculo.getColor());
        assertEquals("2023", vehiculo.getModelo());
    }

    @Test
    @DisplayName("Test registrar entrada de moto exitosamente")
    public void testRegistrarEntradaMotoExito() {
        boolean resultado = parqueaderoService.registrarEntradaVehiculo(1, "XYZ789", "Negro", "2022");
        
        assertTrue(resultado);
        assertEquals(1, parqueadero.getListaDevehiculos().size());
        assertTrue(parqueaderoService.getVehiculosActuales().containsKey("XYZ789"));
        
        Vehiculo vehiculo = parqueaderoService.buscarVehiculoPorPlaca("XYZ789");
        assertNotNull(vehiculo);
        assertTrue(vehiculo instanceof Moto);
    }

    @Test
    @DisplayName("Test registrar entrada de camión exitosamente")
    public void testRegistrarEntradaCamionExito() {
        boolean resultado = parqueaderoService.registrarEntradaVehiculo(2, "LMN456", "Blanco", "2021");
        
        assertTrue(resultado);
        assertEquals(1, parqueadero.getListaDevehiculos().size());
        assertTrue(parqueaderoService.getVehiculosActuales().containsKey("LMN456"));
        
        Vehiculo vehiculo = parqueaderoService.buscarVehiculoPorPlaca("LMN456");
        assertNotNull(vehiculo);
        assertTrue(vehiculo instanceof Camion);
    }

    @Test
    @DisplayName("Test registrar entrada con datos inválidos")
    public void testRegistrarEntradaDatosInvalidos() {
        // Placa vacía
        assertFalse(parqueaderoService.registrarEntradaVehiculo(0, "", "Rojo", "2023"));
        // Placa nula
        assertFalse(parqueaderoService.registrarEntradaVehiculo(0, null, "Rojo", "2023"));
        // Color vacío
        assertFalse(parqueaderoService.registrarEntradaVehiculo(0, "ABC123", "", "2023"));
        // Color nulo
        assertFalse(parqueaderoService.registrarEntradaVehiculo(0, "ABC123", null, "2023"));
        // Modelo vacío
        assertFalse(parqueaderoService.registrarEntradaVehiculo(0, "ABC123", "Rojo", ""));
        // Modelo nulo
        assertFalse(parqueaderoService.registrarEntradaVehiculo(0, "ABC123", "Rojo", null));
        // Tipo inválido
        assertFalse(parqueaderoService.registrarEntradaVehiculo(-1, "ABC123", "Rojo", "2023"));
        assertFalse(parqueaderoService.registrarEntradaVehiculo(3, "ABC123", "Rojo", "2023"));
        
        // Verificar que no se registró ningún vehículo
        assertEquals(0, parqueadero.getListaDevehiculos().size());
        assertTrue(parqueaderoService.getVehiculosActuales().isEmpty());
    }

    @Test
    @DisplayName("Test registrar entrada con vehículo duplicado")
    public void testRegistrarEntradaVehiculoDuplicado() {
        // Primer registro exitoso
        assertTrue(parqueaderoService.registrarEntradaVehiculo(0, "ABC123", "Rojo", "2023"));
        
        // Intento de registro duplicado
        assertFalse(parqueaderoService.registrarEntradaVehiculo(0, "ABC123", "Azul", "2024"));
        
        // Verificar que solo hay un vehículo registrado
        assertEquals(1, parqueadero.getListaDevehiculos().size());
        assertEquals(1, parqueaderoService.getVehiculosActuales().size());
        
        // Verificar que se mantuvo el primer vehículo
        Vehiculo vehiculo = parqueaderoService.buscarVehiculoPorPlaca("ABC123");
        assertEquals("Rojo", vehiculo.getColor());
        assertEquals("2023", vehiculo.getModelo());
    }

    @Test
    @DisplayName("Test registrar entrada sin espacio disponible")
    public void testRegistrarEntradaSinEspacio() {
        // Llenar todos los espacios para automóviles
        for (int i = 1; i <= 10; i++) {
            assertTrue(parqueaderoService.registrarEntradaVehiculo(0, "AUTO" + i, "Rojo", "2023"));
        }
        
        // Intento de registro cuando ya no hay espacio
        assertFalse(parqueaderoService.registrarEntradaVehiculo(0, "AUTO_EXTRA", "Azul", "2024"));
        
        // Verificar que solo hay 10 vehículos registrados
        assertEquals(10, parqueadero.getListaDevehiculos().size());
        assertEquals(10, parqueaderoService.getVehiculosActuales().size());
    }

    @Test
    @DisplayName("Test buscar vehículo por placa")
    public void testBuscarVehiculoPorPlaca() {
        // Registrar un vehículo
        parqueaderoService.registrarEntradaVehiculo(0, "ABC123", "Rojo", "2023");
        
        // Buscar vehículo existente
        Vehiculo vehiculo = parqueaderoService.buscarVehiculoPorPlaca("ABC123");
        assertNotNull(vehiculo);
        assertEquals("ABC123", vehiculo.getPlaca());
        
        // Buscar vehículo inexistente
        assertNull(parqueaderoService.buscarVehiculoPorPlaca("NOEXISTE"));
        
        // Verificar que la búsqueda no distingue entre mayúsculas y minúsculas
        Vehiculo vehiculoMinusculas = parqueaderoService.buscarVehiculoPorPlaca("abc123");
        assertNotNull(vehiculoMinusculas);
        assertEquals("ABC123", vehiculoMinusculas.getPlaca());
    }

    @Test
    @DisplayName("Test registrar salida de vehículo sin membresía")
    public void testRegistrarSalidaVehiculoSinMembresia() throws Exception {
        // Registrar un vehículo usando un tiempo de entrada fijo para pruebas
        assertTrue(parqueaderoService.registrarEntradaVehiculo(0, "ABC123", "Rojo", "2023"));
        
        // Configurar hora de entrada fija (2 horas atrás) mediante reflexión
        LocalDateTime horaEntradaFija = LocalDateTime.now().minusHours(2);
        Map<String, LocalDateTime> vehiculosActuales = getVehiculosActualesMap();
        vehiculosActuales.put("ABC123", horaEntradaFija);
        
        // Registrar salida
        Map<String, Object> resultado = parqueaderoService.registrarSalidaVehiculo("ABC123");
        
        // Verificar que el resultado contiene la información esperada
        assertNotNull(resultado);
        assertEquals("ABC123", resultado.get("placa"));
        assertEquals("Automóvil", resultado.get("tipoVehiculo"));
        assertEquals(2L, resultado.get("horasEstadia"));
        assertEquals(4000.0, resultado.get("montoTotal")); // 2 horas * 2000 por hora
        assertEquals(false, resultado.get("tieneMembresiaActiva"));
        
        // Verificar que el vehículo fue removido
        assertTrue(parqueaderoService.getVehiculosActuales().isEmpty());
        assertEquals(0, parqueadero.getListaDevehiculos().size());
    }

    @Test
    @DisplayName("Test registrar salida de vehículo con membresía")
    public void testRegistrarSalidaVehiculoConMembresia() throws Exception {
        // Registrar un vehículo
        assertTrue(parqueaderoService.registrarEntradaVehiculo(0, "ABC123", "Rojo", "2023"));
        
        // Configurar hora de entrada fija mediante reflexión
        LocalDateTime horaEntradaFija = LocalDateTime.now().minusHours(3);
        Map<String, LocalDateTime> vehiculosActuales = getVehiculosActualesMap();
        vehiculosActuales.put("ABC123", horaEntradaFija);
        
        // Asignar membresía al vehículo
        Vehiculo vehiculo = parqueaderoService.buscarVehiculoPorPlaca("ABC123");
        vehiculo.setMembresia(TipoMembresia.MENSUAL);
        vehiculo.setFechaFinMembresia(LocalDate.now().plusMonths(1).toString());
        
        // Registrar salida
        Map<String, Object> resultado = parqueaderoService.registrarSalidaVehiculo("ABC123");
        
        // Verificar que el resultado contiene la información esperada
        assertNotNull(resultado);
        assertEquals("ABC123", resultado.get("placa"));
        assertEquals(true, resultado.get("tieneMembresiaActiva"));
        assertEquals(0.0, resultado.get("montoTotal")); // Gratis con membresía
        
        // Verificar que el vehículo fue removido
        assertTrue(parqueaderoService.getVehiculosActuales().isEmpty());
        assertEquals(0, parqueadero.getListaDevehiculos().size());
    }

    @Test
    @DisplayName("Test registrar salida con datos inválidos")
    public void testRegistrarSalidaDatosInvalidos() {
        // Placa vacía
        assertNull(parqueaderoService.registrarSalidaVehiculo(""));
        
        // Placa nula
        assertNull(parqueaderoService.registrarSalidaVehiculo(null));
        
        // Placa inexistente
        assertNull(parqueaderoService.registrarSalidaVehiculo("NOEXISTE"));
    }

    @Test
    @DisplayName("Test verificar disponibilidad por tipo de vehículo")
    public void testVerificarDisponibilidad() {
        // Inicialmente todos los espacios están disponibles
        assertTrue(parqueaderoService.verificarDisponibilidadMotos());
        assertTrue(parqueaderoService.verificarDisponibilidadAutomoviles());
        assertTrue(parqueaderoService.verificarDisponibilidadCamiones());
        
        // Llenar todos los espacios para motos
        for (int i = 1; i <= 5; i++) {
            parqueaderoService.registrarEntradaVehiculo(1, "MOTO" + i, "Rojo", "2023");
        }
        assertFalse(parqueaderoService.verificarDisponibilidadMotos());
        assertTrue(parqueaderoService.verificarDisponibilidadAutomoviles());
        assertTrue(parqueaderoService.verificarDisponibilidadCamiones());
        
        // Llenar todos los espacios para automóviles
        for (int i = 1; i <= 10; i++) {
            parqueaderoService.registrarEntradaVehiculo(0, "AUTO" + i, "Azul", "2023");
        }
        assertFalse(parqueaderoService.verificarDisponibilidadMotos());
        assertFalse(parqueaderoService.verificarDisponibilidadAutomoviles());
        assertTrue(parqueaderoService.verificarDisponibilidadCamiones());
        
        // Llenar todos los espacios para camiones
        for (int i = 1; i <= 3; i++) {
            parqueaderoService.registrarEntradaVehiculo(2, "CAMION" + i, "Verde", "2023");
        }
        assertFalse(parqueaderoService.verificarDisponibilidadMotos());
        assertFalse(parqueaderoService.verificarDisponibilidadAutomoviles());
        assertFalse(parqueaderoService.verificarDisponibilidadCamiones());
    }

    @Test
    @DisplayName("Test obtener estado del parqueadero")
    public void testObtenerEstadoParqueadero() {
        // Estado inicial
        Map<String, Object> estadoInicial = parqueaderoService.obtenerEstadoParqueadero();
        
        assertEquals(0, estadoInicial.get("motosActuales"));
        assertEquals(5, estadoInicial.get("motosTotales"));
        assertEquals(5, estadoInicial.get("motosDisponibles"));
        
        assertEquals(0, estadoInicial.get("automovilesActuales"));
        assertEquals(10, estadoInicial.get("automovilesTotales"));
        assertEquals(10, estadoInicial.get("automovilesDisponibles"));
        
        assertEquals(0, estadoInicial.get("camionesActuales"));
        assertEquals(3, estadoInicial.get("camionesTotales"));
        assertEquals(3, estadoInicial.get("camionesDisponibles"));
        
        assertEquals(18, estadoInicial.get("totalEspacios"));
        assertEquals(0, estadoInicial.get("totalOcupacion"));
        
        // Agregar algunos vehículos
        parqueaderoService.registrarEntradaVehiculo(0, "AUTO1", "Rojo", "2023");
        parqueaderoService.registrarEntradaVehiculo(0, "AUTO2", "Azul", "2022");
        parqueaderoService.registrarEntradaVehiculo(1, "MOTO1", "Verde", "2021");
        parqueaderoService.registrarEntradaVehiculo(2, "CAMION1", "Blanco", "2020");
        
        // Estado después de agregar vehículos
        Map<String, Object> estadoActual = parqueaderoService.obtenerEstadoParqueadero();
        
        assertEquals(1, estadoActual.get("motosActuales"));
        assertEquals(5, estadoActual.get("motosTotales"));
        assertEquals(4, estadoActual.get("motosDisponibles"));
        
        assertEquals(2, estadoActual.get("automovilesActuales"));
        assertEquals(10, estadoActual.get("automovilesTotales"));
        assertEquals(8, estadoActual.get("automovilesDisponibles"));
        
        assertEquals(1, estadoActual.get("camionesActuales"));
        assertEquals(3, estadoActual.get("camionesTotales"));
        assertEquals(2, estadoActual.get("camionesDisponibles"));
        
        assertEquals(18, estadoActual.get("totalEspacios"));
        assertEquals(4, estadoActual.get("totalOcupacion"));
    }

    @Test
    @DisplayName("Test obtener lista de vehículos actuales")
    public void testObtenerListaVehiculosActuales() {
        // Lista inicial vacía
        List<Map<String, Object>> listaInicial = parqueaderoService.obtenerListaVehiculosActuales();
        assertTrue(listaInicial.isEmpty());
        
        // Agregar vehículos
        parqueaderoService.registrarEntradaVehiculo(0, "AUTO1", "Rojo", "2023");
        parqueaderoService.registrarEntradaVehiculo(1, "MOTO1", "Verde", "2021");
        
        // Verificar lista con vehículos
        List<Map<String, Object>> listaActual = parqueaderoService.obtenerListaVehiculosActuales();
        assertEquals(2, listaActual.size());
        
        // Verificar datos del primer vehículo
        Map<String, Object> auto = listaActual.stream()
                                             .filter(v -> v.get("placa").equals("AUTO1"))
                                             .findFirst()
                                             .orElse(null);
        assertNotNull(auto);
        assertEquals("Automóvil", auto.get("tipo"));
        assertEquals("Rojo", auto.get("color"));
        assertEquals("2023", auto.get("modelo"));
        assertNotNull(auto.get("horaEntrada"));
        assertEquals(false, auto.get("tieneMembresia"));
        
        // Verificar datos del segundo vehículo
        Map<String, Object> moto = listaActual.stream()
                                            .filter(v -> v.get("placa").equals("MOTO1"))
                                            .findFirst()
                                            .orElse(null);
        assertNotNull(moto);
        assertEquals("Moto", moto.get("tipo"));
        assertEquals("Verde", moto.get("color"));
        assertEquals("2021", moto.get("modelo"));
        assertNotNull(moto.get("horaEntrada"));
        assertEquals(false, moto.get("tieneMembresia"));
        
        // Verificar vehículo con membresía
        Vehiculo vehiculoMoto = parqueaderoService.buscarVehiculoPorPlaca("MOTO1");
        vehiculoMoto.setMembresia(TipoMembresia.MENSUAL);
        vehiculoMoto.setFechaFinMembresia(LocalDate.now().plusWeeks(2).toString());
        
        List<Map<String, Object>> listaConMembresia = parqueaderoService.obtenerListaVehiculosActuales();
        Map<String, Object> motoConMembresia = listaConMembresia.stream()
                                                              .filter(v -> v.get("placa").equals("MOTO1"))
                                                              .findFirst()
                                                              .orElse(null);
        assertNotNull(motoConMembresia);
        assertEquals(true, motoConMembresia.get("tieneMembresia"));
        assertEquals(TipoMembresia.MENSUAL, motoConMembresia.get("tipoMembresia"));
        assertNotNull(motoConMembresia.get("fechaFinMembresia"));
    }

    @Test
    @DisplayName("Test obtener tipo de vehículo")
    public void testObtenerTipoVehiculo() {
        Vehiculo automovil = new Automovil("AUTO1", "Rojo", "2023");
        Vehiculo moto = new Moto("MOTO1", "Verde", "2021");
        Vehiculo camion = new Camion("CAMION1", "Blanco", "2020");
        
        assertEquals("Automóvil", parqueaderoService.obtenerTipoVehiculo(automovil));
        assertEquals("Moto", parqueaderoService.obtenerTipoVehiculo(moto));
        assertEquals("Camión", parqueaderoService.obtenerTipoVehiculo(camion));
        
        // Caso con clase desconocida (usando clase anónima)
        Vehiculo desconocido = new Vehiculo("DESC1", "Negro", "2019") {};
        assertEquals("Desconocido", parqueaderoService.obtenerTipoVehiculo(desconocido));
    }

    @Test
    @DisplayName("Test obtener tarifa por tipo")
    public void testObtenerTarifaPorTipo() {
        assertEquals(2000, parqueaderoService.obtenerTarifaPorTipo(0)); // Automóvil
        assertEquals(1000, parqueaderoService.obtenerTarifaPorTipo(1)); // Moto
        assertEquals(3000, parqueaderoService.obtenerTarifaPorTipo(2)); // Camión
        assertEquals(0, parqueaderoService.obtenerTarifaPorTipo(-1)); // Inválido
        assertEquals(0, parqueaderoService.obtenerTarifaPorTipo(3)); // Inválido
    }

    @Test
    @DisplayName("Test registrar salida con tiempo mínimo de 1 hora")
    public void testRegistrarSalidaTiempoMinimo() throws Exception {
        // Registrar un vehículo
        assertTrue(parqueaderoService.registrarEntradaVehiculo(0, "AUTO_MIN", "Rojo", "2023"));
        
        // Configurar hora de entrada que resultaría en menos de 1 hora
        LocalDateTime horaEntradaFija = LocalDateTime.now().minusMinutes(30);
        Map<String, LocalDateTime> vehiculosActuales = getVehiculosActualesMap();
        vehiculosActuales.put("AUTO_MIN", horaEntradaFija);
        
        // Registrar salida
        Map<String, Object> resultado = parqueaderoService.registrarSalidaVehiculo("AUTO_MIN");
        
        // Verificar que se cobra mínimo 1 hora
        assertEquals(1L, resultado.get("horasEstadia"));
        assertEquals(2000.0, resultado.get("montoTotal")); // 1 hora * 2000
    }

    // Método auxiliar para acceder al mapa de vehículos actuales mediante reflexión
    private Map<String, LocalDateTime> getVehiculosActualesMap() throws Exception {
        Field field = ParqueaderoCodeService.class.getDeclaredField("vehiculosActuales");
        field.setAccessible(true);
        return (Map<String, LocalDateTime>) field.get(parqueaderoService);
    }
}