package codeService;

import model.Automovil;
import model.Camion;
import model.Cliente;
import model.Moto;
import model.Pago;
import model.TipoMembresia;
import model.Vehiculo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PagoCodeServiceTest {

    private PagoCodeService pagoService;
    private Cliente cliente;
    private Vehiculo automovil;
    private Vehiculo moto;
    private Vehiculo camion;

    @BeforeEach
    void setUp() {
        pagoService = new PagoCodeService();
        cliente = new Cliente("Juan Pérez", "1234567890", "3001234567", "juan@example.com");
        automovil = new Automovil("ABC123", "Rojo", "2022");
        moto = new Moto("XYZ789", "Azul", "2021");
        camion = new Camion("QWE456", "Blanco", "2020");
    }

    @Nested
    @DisplayName("Pruebas para registrarPago")
    class RegistrarPagoTests {

        @Test
        @DisplayName("Registrar pago exitoso")
        void registrarPagoExitoso() {
            // Act
            boolean resultado = pagoService.registrarPago(automovil, 10000);
            
            // Assert
            assertTrue(resultado);
            assertEquals(1, pagoService.getHistorialPagos().size());
            Pago pago = pagoService.getHistorialPagos().get(0);
            assertEquals(10000, pago.getMonto());
            assertEquals(automovil, pago.getVehiculo());
            assertEquals("Automóvil", pago.getTipoVehiculo());
        }

        @Test
        @DisplayName("Registrar pago con vehículo nulo")
        void registrarPagoVehiculoNulo() {
            // Act
            boolean resultado = pagoService.registrarPago(null, 10000);
            
            // Assert
            assertFalse(resultado);
            assertEquals(0, pagoService.getHistorialPagos().size());
        }

        @Test
        @DisplayName("Registrar pago con monto inválido")
        void registrarPagoMontoInvalido() {
            // Act
            boolean resultado = pagoService.registrarPago(automovil, 0);
            
            // Assert
            assertFalse(resultado);
            assertEquals(0, pagoService.getHistorialPagos().size());
            
            // Probar con monto negativo
            resultado = pagoService.registrarPago(automovil, -100);
            assertFalse(resultado);
            assertEquals(0, pagoService.getHistorialPagos().size());
        }
    }

    @Nested
    @DisplayName("Pruebas para registrarPagoMembresia")
    class RegistrarPagoMembresiaTests {

        @Test
        @DisplayName("Registrar pago de membresía exitoso")
        void registrarPagoMembresiaExitoso() {
            // Act
            boolean resultado = pagoService.registrarPagoMembresia(automovil, cliente, TipoMembresia.MENSUAL);
            
            // Assert
            assertTrue(resultado);
            assertEquals(1, pagoService.getHistorialPagos().size());
            Pago pago = pagoService.getHistorialPagos().get(0);
            assertEquals(100000, pago.getMonto());
            assertEquals(automovil, pago.getVehiculo());
            assertEquals(cliente, pago.getCliente());
        }

        @Test
        @DisplayName("Registrar pago de membresía con vehículo nulo")
        void registrarPagoMembresiaVehiculoNulo() {
            // Act
            boolean resultado = pagoService.registrarPagoMembresia(null, cliente, TipoMembresia.MENSUAL);
            
            // Assert
            assertFalse(resultado);
            assertEquals(0, pagoService.getHistorialPagos().size());
        }

        @Test
        @DisplayName("Registrar pago de membresía con cliente nulo")
        void registrarPagoMembresiaClienteNulo() {
            // Act
            boolean resultado = pagoService.registrarPagoMembresia(automovil, null, TipoMembresia.MENSUAL);
            
            // Assert
            assertFalse(resultado);
            assertEquals(0, pagoService.getHistorialPagos().size());
        }

        @Test
        @DisplayName("Registrar pago de membresía con tipo de membresía nulo")
        void registrarPagoMembresiaTipoNulo() {
            // Act
            boolean resultado = pagoService.registrarPagoMembresia(automovil, cliente, null);
            
            // Assert
            assertFalse(resultado);
            assertEquals(0, pagoService.getHistorialPagos().size());
        }
    }

    @Nested
    @DisplayName("Pruebas para calcularTarifaMembresia")
    class CalcularTarifaMembresiaTests {

        @Test
        @DisplayName("Calcular tarifas para automóvil")
        void calcularTarifasAutomovil() {
            // Act & Assert
            assertEquals(100000, pagoService.calcularTarifaMembresia(automovil, TipoMembresia.MENSUAL));
            assertEquals(270000, pagoService.calcularTarifaMembresia(automovil, TipoMembresia.TRIMESTRAL));
            assertEquals(960000, pagoService.calcularTarifaMembresia(automovil, TipoMembresia.ANUAL));
        }

        @Test
        @DisplayName("Calcular tarifas para moto")
        void calcularTarifasMoto() {
            // Act & Assert
            assertEquals(50000, pagoService.calcularTarifaMembresia(moto, TipoMembresia.MENSUAL));
            assertEquals(135000, pagoService.calcularTarifaMembresia(moto, TipoMembresia.TRIMESTRAL));
            assertEquals(480000, pagoService.calcularTarifaMembresia(moto, TipoMembresia.ANUAL));
        }

        @Test
        @DisplayName("Calcular tarifas para camión")
        void calcularTarifasCamion() {
            // Act & Assert
            assertEquals(150000, pagoService.calcularTarifaMembresia(camion, TipoMembresia.MENSUAL));
            assertEquals(405000, pagoService.calcularTarifaMembresia(camion, TipoMembresia.TRIMESTRAL));
            assertEquals(1440000, pagoService.calcularTarifaMembresia(camion, TipoMembresia.ANUAL));
        }

        @Test
        @DisplayName("Calcular tarifa con vehículo nulo")
        void calcularTarifaVehiculoNulo() {
            // Act & Assert
            assertEquals(0, pagoService.calcularTarifaMembresia(null, TipoMembresia.MENSUAL));
        }

        @Test
        @DisplayName("Calcular tarifa con tipo membresía nulo")
        void calcularTarifaTipoNulo() {
            // Act & Assert
            assertEquals(0, pagoService.calcularTarifaMembresia(automovil, null));
        }
    }

    @Nested
    @DisplayName("Pruebas para determinarTipoVehiculo")
    class DeterminarTipoVehiculoTests {

        @Test
        @DisplayName("Determinar tipo para automóvil")
        void determinarTipoAutomovil() {
            // Act & Assert
            assertEquals("Automóvil", pagoService.determinarTipoVehiculo(automovil));
        }

        @Test
        @DisplayName("Determinar tipo para moto")
        void determinarTipoMoto() {
            // Act & Assert
            assertEquals("Moto", pagoService.determinarTipoVehiculo(moto));
        }

        @Test
        @DisplayName("Determinar tipo para camión")
        void determinarTipoCamion() {
            // Act & Assert
            assertEquals("Camión", pagoService.determinarTipoVehiculo(camion));
        }

        @Test
        @DisplayName("Determinar tipo para vehículo nulo")
        void determinarTipoVehiculoNulo() {
            // Act & Assert
            assertEquals("Desconocido", pagoService.determinarTipoVehiculo(null));
        }

        @Test
        @DisplayName("Determinar tipo para vehículo no reconocido")
        void determinarTipoVehiculoDesconocido() {
            // Arrange
            Vehiculo vehiculoDesconocido = new Vehiculo("TEST123", "Negro", "Ford Fusion") {
                // Clase anónima que extiende de Vehiculo
            };
            
            // Act & Assert
            assertEquals("Desconocido", pagoService.determinarTipoVehiculo(vehiculoDesconocido));
        }
    }

    @Nested
    @DisplayName("Pruebas para buscarPagoPorId")
    class BuscarPagoPorIdTests {

        @Test
        @DisplayName("Buscar pago existente por ID")
        void buscarPagoExistente() {
            // Arrange
            pagoService.registrarPago(automovil, 10000);
            String pagoId = pagoService.getHistorialPagos().get(0).getId();
            
            // Act
            Pago pagoBuscado = pagoService.buscarPagoPorId(pagoId);
            
            // Assert
            assertNotNull(pagoBuscado);
            assertEquals(pagoId, pagoBuscado.getId());
            assertEquals(10000, pagoBuscado.getMonto());
        }

        @Test
        @DisplayName("Buscar pago con ID nulo")
        void buscarPagoIdNulo() {
            // Act & Assert
            assertNull(pagoService.buscarPagoPorId(null));
        }

        @Test
        @DisplayName("Buscar pago con ID vacío")
        void buscarPagoIdVacio() {
            // Act & Assert
            assertNull(pagoService.buscarPagoPorId(""));
            assertNull(pagoService.buscarPagoPorId("   "));
        }

        @Test
        @DisplayName("Buscar pago no existente")
        void buscarPagoNoExistente() {
            // Act & Assert
            assertNull(pagoService.buscarPagoPorId("ID_INEXISTENTE"));
        }
    }

    @Nested
    @DisplayName("Pruebas para calcularMontoAPagar")
    class CalcularMontoAPagarTests {

        @Test
        @DisplayName("Calcular monto para automóvil")
        void calcularMontoAutomovil() {
            // Act & Assert
            assertEquals(6000, pagoService.calcularMontoAPagar("Automóvil", 3));
        }

        @Test
        @DisplayName("Calcular monto para moto")
        void calcularMontoMoto() {
            // Act & Assert
            assertEquals(2000, pagoService.calcularMontoAPagar("Moto", 2));
        }

        @Test
        @DisplayName("Calcular monto para camión")
        void calcularMontoCamion() {
            // Act & Assert
            assertEquals(9000, pagoService.calcularMontoAPagar("Camión", 3));
        }

        @Test
        @DisplayName("Calcular monto con tipo de vehículo nulo")
        void calcularMontoTipoNulo() {
            // Act & Assert
            assertEquals(0, pagoService.calcularMontoAPagar(null, 2));
        }

        @Test
        @DisplayName("Calcular monto con tipo de vehículo inválido")
        void calcularMontoTipoInvalido() {
            // Act & Assert
            assertEquals(0, pagoService.calcularMontoAPagar("TipoInexistente", 2));
        }

        @Test
        @DisplayName("Calcular monto con horas inválidas")
        void calcularMontoHorasInvalidas() {
            // Act & Assert
            assertEquals(0, pagoService.calcularMontoAPagar("Automóvil", 0));
            assertEquals(0, pagoService.calcularMontoAPagar("Automóvil", -1));
        }
    }

    @Nested
    @DisplayName("Pruebas para filtros de pagos por fecha")
    class FiltrosPagosPorFechaTests {
        
        private Pago pagoHoy;
        private Pago pagoAyer;
        private Pago pagoMesAnterior;
        
        @BeforeEach
        void setUp() {
            // Crear pagos con fechas diferentes para las pruebas
            pagoService.registrarPago(automovil, 10000); // Hoy
            pagoService.registrarPago(moto, 5000); // Hoy
            pagoHoy = pagoService.getHistorialPagos().get(0);
            
            // Crear un pago de ayer modificando la fecha manualmente
            pagoService.registrarPago(camion, 15000);
            pagoAyer = pagoService.getHistorialPagos().get(2);
            // Simulamos que el pago es de ayer
            setFechaPago(pagoAyer, LocalDateTime.now().minus(1, ChronoUnit.DAYS));
            
            // Crear un pago del mes anterior
            pagoService.registrarPago(moto, 8000);
            pagoMesAnterior = pagoService.getHistorialPagos().get(3);
            // Simulamos que el pago es del mes anterior
            setFechaPago(pagoMesAnterior, LocalDateTime.now().minus(1, ChronoUnit.MONTHS));
        }
        
        // Método auxiliar para simular fechas en los pagos para pruebas
        private void setFechaPago(Pago pago, LocalDateTime fecha) {
            // Este método es solo para pruebas y simula cambiar la fecha del pago
            // En una implementación real, esto requeriría acceso a métodos setter o reflexión
            try {
                java.lang.reflect.Field field = Pago.class.getDeclaredField("fechaHora");
                field.setAccessible(true);
                field.set(pago, fecha);
            } catch (Exception e) {
                fail("No se pudo modificar la fecha del pago para las pruebas: " + e.getMessage());
            }
        }

        @Test
        @DisplayName("Obtener pagos por día")
        void obtenerPagosPorDia() {
            // Act
            List<Pago> pagosHoy = pagoService.obtenerPagosPorDia(LocalDate.now());
            List<Pago> pagosAyer = pagoService.obtenerPagosPorDia(LocalDate.now().minus(1, ChronoUnit.DAYS));
            
            // Assert
            assertEquals(2, pagosHoy.size()); // Los 2 pagos de hoy
            assertEquals(1, pagosAyer.size()); // El pago de ayer
            assertTrue(pagosAyer.contains(pagoAyer));
        }

        @Test
        @DisplayName("Obtener pagos con día nulo")
        void obtenerPagosDiaNulo() {
            // Act
            List<Pago> pagos = pagoService.obtenerPagosPorDia(null);
            
            // Assert
            assertTrue(pagos.isEmpty());
        }

        @Test
        @DisplayName("Obtener pagos por mes y año")
        void obtenerPagosPorMes() {
            // Arrange
            int mesActual = LocalDate.now().getMonthValue();
            int anioActual = LocalDate.now().getYear();
            int mesAnterior = LocalDate.now().minus(1, ChronoUnit.MONTHS).getMonthValue();
            int anioMesAnterior = LocalDate.now().minus(1, ChronoUnit.MONTHS).getYear();
            
            // Act
            List<Pago> pagosMesActual = pagoService.obtenerPagosPorMes(mesActual, anioActual);
            List<Pago> pagosMesAnterior = pagoService.obtenerPagosPorMes(mesAnterior, anioMesAnterior);
            
            // Assert
            assertEquals(3, pagosMesActual.size()); // Los 3 pagos del mes actual
            assertEquals(1, pagosMesAnterior.size()); // El pago del mes anterior
            assertTrue(pagosMesAnterior.contains(pagoMesAnterior));
        }

        @Test
        @DisplayName("Obtener pagos con mes inválido")
        void obtenerPagosMesInvalido() {
            // Act & Assert
            assertTrue(pagoService.obtenerPagosPorMes(0, 2023).isEmpty());
            assertTrue(pagoService.obtenerPagosPorMes(13, 2023).isEmpty());
            assertTrue(pagoService.obtenerPagosPorMes(5, -1).isEmpty());
        }

        @Test
        @DisplayName("Obtener pagos por año")
        void obtenerPagosPorAnio() {
            // Arrange
            int anioActual = LocalDate.now().getYear();
            
            // Act
            List<Pago> pagosAnioActual = pagoService.obtenerPagosPorAnio(anioActual);
            
            // Assert
            assertEquals(4, pagosAnioActual.size()); // Todos los pagos son de este año
        }

        @Test
        @DisplayName("Obtener pagos con año inválido")
        void obtenerPagosAnioInvalido() {
            // Act & Assert
            assertTrue(pagoService.obtenerPagosPorAnio(-1).isEmpty());
        }

        @Test
        @DisplayName("Obtener pagos por rango de fechas")
        void obtenerPagosPorRango() {
            // Arrange
            LocalDate hoy = LocalDate.now();
            LocalDate ayer = hoy.minus(1, ChronoUnit.DAYS);
            LocalDate mesAnterior = hoy.minus(1, ChronoUnit.MONTHS);
            
            // Act
            List<Pago> pagosUltimaSemana = pagoService.obtenerPagosPorRango(
                    hoy.minus(7, ChronoUnit.DAYS), hoy);
            List<Pago> pagosMesAnterior = pagoService.obtenerPagosPorRango(
                    mesAnterior, mesAnterior.plus(5, ChronoUnit.DAYS));
            
            // Assert
            assertEquals(3, pagosUltimaSemana.size()); // Los 2 de hoy y el de ayer
            assertEquals(1, pagosMesAnterior.size()); // El del mes anterior
        }

        @Test
        @DisplayName("Obtener pagos con rango inválido")
        void obtenerPagosRangoInvalido() {
            // Arrange
            LocalDate hoy = LocalDate.now();
            LocalDate ayer = hoy.minus(1, ChronoUnit.DAYS);
            
            // Act & Assert
            assertTrue(pagoService.obtenerPagosPorRango(null, hoy).isEmpty());
            assertTrue(pagoService.obtenerPagosPorRango(hoy, null).isEmpty());
            assertTrue(pagoService.obtenerPagosPorRango(hoy, ayer).isEmpty()); // Rango invertido
        }
    }

    @Nested
    @DisplayName("Pruebas para calcularTotalesReporte")
    class CalcularTotalesReporteTests {

        @Test
        @DisplayName("Calcular totales con lista vacía")
        void calcularTotalesListaVacia() {
            // Act
            PagoCodeService.ReporteTotales totales = pagoService.calcularTotalesReporte(new ArrayList<>());
            
            // Assert
            assertEquals(0, totales.getTotalEstacionamiento());
            assertEquals(0, totales.getTotalMembresias());
            assertEquals(0, totales.getTotalAutomoviles());
            assertEquals(0, totales.getTotalMotos());
            assertEquals(0, totales.getTotalCamiones());
            assertEquals(0, totales.getTotalGeneral());
        }

        @Test
        @DisplayName("Calcular totales con lista nula")
        void calcularTotalesListaNula() {
            // Act
            PagoCodeService.ReporteTotales totales = pagoService.calcularTotalesReporte(null);
            
            // Assert
            assertEquals(0, totales.getTotalEstacionamiento());
            assertEquals(0, totales.getTotalGeneral());
        }

        @Test
        @DisplayName("Calcular totales con pagos de diferentes tipos")
        void calcularTotalesDiferentesTipos() {
            // Arrange - Creamos pagos manualmente para controlar el concepto
            Pago pagoEstacionamientoAuto = new Pago(10000, automovil, "Automóvil");
            // Modificamos el concepto para que sea de estacionamiento
            setCampoPago(pagoEstacionamientoAuto, "concepto", "Estacionamiento Temporal");
            
            Pago pagoEstacionamientoMoto = new Pago(5000, moto, "Moto");
            setCampoPago(pagoEstacionamientoMoto, "concepto", "Estacionamiento Temporal");
            
            Pago pagoMembresiaCamion = new Pago(150000, camion, cliente, TipoMembresia.MENSUAL);
            // Para membresía no hay que cambiar el concepto, ya se define en el constructor
            
            List<Pago> pagos = Arrays.asList(pagoEstacionamientoAuto, pagoEstacionamientoMoto, pagoMembresiaCamion);
            
            // Act
            PagoCodeService.ReporteTotales totales = pagoService.calcularTotalesReporte(pagos);
            
            // Assert
            assertEquals(15000, totales.getTotalEstacionamiento());
            assertEquals(150000, totales.getTotalMembresias());
            assertEquals(10000, totales.getTotalAutomoviles());
            assertEquals(5000, totales.getTotalMotos());
            assertEquals(150000, totales.getTotalCamiones());
            assertEquals(165000, totales.getTotalGeneral());
        }
        
        // Método auxiliar para modificar campos de un pago para pruebas
        private void setCampoPago(Pago pago, String nombreCampo, Object valor) {
            try {
                java.lang.reflect.Field field = Pago.class.getDeclaredField(nombreCampo);
                field.setAccessible(true);
                field.set(pago, valor);
            } catch (Exception e) {
                fail("No se pudo modificar el campo " + nombreCampo + " del pago para las pruebas: " + e.getMessage());
            }
        }
    }
    
    @Nested
    @DisplayName("Pruebas para clase ReporteTotales")
    class ReporteTotalesTests {
        
        @Test
        @DisplayName("Verificar getters de ReporteTotales")
        void verificarGetters() {
            // Arrange
            PagoCodeService.ReporteTotales totales = new PagoCodeService.ReporteTotales(
                    100, 200, 50, 150, 100);
            
            // Act & Assert
            assertEquals(100, totales.getTotalEstacionamiento());
            assertEquals(200, totales.getTotalMembresias());
            assertEquals(50, totales.getTotalAutomoviles());
            assertEquals(150, totales.getTotalMotos());
            assertEquals(100, totales.getTotalCamiones());
            assertEquals(300, totales.getTotalGeneral()); // 100 + 200
        }
    }
}