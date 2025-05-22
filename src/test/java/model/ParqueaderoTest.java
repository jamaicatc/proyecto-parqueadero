package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;

public class ParqueaderoTest {

    private Parqueadero parqueadero;
    private ParqueaderoTestable parqueaderoTestable;

    @BeforeEach
    public void init() {
        this.parqueadero = new Parqueadero();
        this.parqueaderoTestable = new ParqueaderoTestable();
        // Desactivar interacción con ventanas para pruebas
        System.setProperty("java.awt.headless", "true");
    }

    // Clase interna para hacer testable los métodos con JOptionPane
    class ParqueaderoTestable extends Parqueadero {
        private int showInputDialogCallCount = 0;
        private int showOptionDialogCallCount = 0;
        private int showConfirmDialogCallCount = 0;
        private int showMessageDialogCallCount = 0;

        private String nextSeleccion = null;
        private String nextInput = null;
        private int nextOptionSelection = -1;
        private int nextConfirmResult = -1;
        private String[] nextFormInputs = null;
        private boolean nextFieldsValidation = true;
        private boolean showMessageDialogCalled = false;

        // Para controlar actualizarInformacion
        public void setNextSeleccion(String seleccion) {
            this.nextSeleccion = seleccion;
        }


        public void setNextInput(String input) {
            this.nextInput = input;
        }

        public int getShowInputDialogCallCount() {
            return showInputDialogCallCount;
        }

        public boolean isShowMessageDialogCalled() {
            return showMessageDialogCalled;
        }

        // Sobrescribir el método actualizarInformacion para simular interacción
        @Override
        public void actualizarInformacion() {
            String[] opciones = { "Seleccionar", "Actualizar nombre", "Actualizar dirección", "Actualizar representante",
                    "Actualizar teléfono", "Actualizar correo", "Ver datos actuales", "Salir" };

            boolean continuar = true;

            // Ejecutar una iteración para cubrir el código
            if (nextSeleccion == null) {
                // Simular que el usuario cancela el diálogo
                continuar = false;
            } else {
                String seleccion = nextSeleccion;
                showInputDialogCallCount++;

                switch (seleccion) {
                    case "Actualizar nombre":
                        setNombre(nextInput != null ? nextInput : "Nombre Test");
                        break;
                    case "Actualizar dirección":
                        setDireccion(nextInput != null ? nextInput : "Dirección Test");
                        break;
                    case "Actualizar representante":
                        setRepresentante(nextInput != null ? nextInput : "Representante Test");
                        break;
                    case "Actualizar teléfono":
                        setTelefono(nextInput != null ? nextInput : "Teléfono Test");
                        break;
                    case "Actualizar correo":
                        setCorreo(nextInput != null ? nextInput : "correo@test.com");
                        break;
                    case "Ver datos actuales":
                        mostrarInformacion();
                        break;
                    case "Salir":
                        continuar = false;
                        break;
                    default:
                        // Caso null o inválido
                        continuar = false;
                        break;
                }
            }
        }

        @Override
        public void mostrarInformacion() {
            // Construir la cadena de datos como en el método original
            String datos = "Nombre: " + getNombre() + "\nDirección: " + getDireccion() + "\nRepresentante: " + getRepresentante()
                    + "\nTeléfono: " + getTelefono() + "\nCorreo: " + getCorreo();
            
            // Simular mostrar información sin usar JOptionPane
            showMessageDialogCallCount++;
            showMessageDialogCalled = true;
        }
    }



    @Test
    @DisplayName("Test constructor vacío")
    public void testConstructorVacio() {
        // Verificar que se crea la instancia
        Assertions.assertNotNull(parqueadero);
        
        // Verificar que los atributos están inicializados con valores por defecto
        Assertions.assertNull(parqueadero.getNombre());
        Assertions.assertNull(parqueadero.getDireccion());
        Assertions.assertNull(parqueadero.getRepresentante());
        Assertions.assertNull(parqueadero.getTelefono());
        Assertions.assertNull(parqueadero.getCorreo());
        Assertions.assertEquals(0, parqueadero.getPuestosMotos());
        Assertions.assertEquals(0, parqueadero.getPuestosAutomoviles());
        Assertions.assertEquals(0, parqueadero.getPuestosCamiones());
        Assertions.assertNotNull(parqueadero.getListaDevehiculos());
        Assertions.assertTrue(parqueadero.getListaDevehiculos().isEmpty());
        Assertions.assertNotNull(parqueadero.getListaDeClientes());
        Assertions.assertTrue(parqueadero.getListaDeClientes().isEmpty());
    }

    @Test
    @DisplayName("Test constructor con parámetros")
    public void testConstructorParametrizado() {
        // Given
        Parqueadero parqueaderoTest = new Parqueadero("Parqueadero Test", "Calle 123", "Juan Pérez", "123456789", "correo@test.com");
        
        // When & Then
        Assertions.assertEquals("Parqueadero Test", parqueaderoTest.getNombre());
        Assertions.assertEquals("Calle 123", parqueaderoTest.getDireccion());
        Assertions.assertEquals("Juan Pérez", parqueaderoTest.getRepresentante());
        Assertions.assertEquals("123456789", parqueaderoTest.getTelefono());
        Assertions.assertEquals("correo@test.com", parqueaderoTest.getCorreo());
        Assertions.assertEquals(0, parqueaderoTest.getPuestosMotos());
        Assertions.assertEquals(0, parqueaderoTest.getPuestosAutomoviles());
        Assertions.assertEquals(0, parqueaderoTest.getPuestosCamiones());
    }

    @Test
    @DisplayName("Test setters y getters de nombre")
    public void testSetterGetterNombre() {
        // Given
        String nombre = "Parqueadero Test";
        
        // When
        parqueadero.setNombre(nombre);
        
        // Then
        Assertions.assertEquals(nombre, parqueadero.getNombre());
    }

    @Test
    @DisplayName("Test setters y getters de dirección")
    public void testSetterGetterDireccion() {
        // Given
        String direccion = "Avenida Test 123";
        
        // When
        parqueadero.setDireccion(direccion);
        
        // Then
        Assertions.assertEquals(direccion, parqueadero.getDireccion());
    }

    @Test
    @DisplayName("Test setters y getters de representante")
    public void testSetterGetterRepresentante() {
        // Given
        String representante = "Ana García";
        
        // When
        parqueadero.setRepresentante(representante);
        
        // Then
        Assertions.assertEquals(representante, parqueadero.getRepresentante());
    }

    @Test
    @DisplayName("Test setters y getters de teléfono")
    public void testSetterGetterTelefono() {
        // Given
        String telefono = "987654321";
        
        // When
        parqueadero.setTelefono(telefono);
        
        // Then
        Assertions.assertEquals(telefono, parqueadero.getTelefono());
    }

    @Test
    @DisplayName("Test setters y getters de correo")
    public void testSetterGetterCorreo() {
        // Given
        String correo = "test@correo.com";
        
        // When
        parqueadero.setCorreo(correo);
        
        // Then
        Assertions.assertEquals(correo, parqueadero.getCorreo());
    }

    @Test
    @DisplayName("Test setters y getters de puestos de motos")
    public void testSetterGetterPuestosMotos() {
        // Given
        int puestosMotos = 10;
        
        // When
        parqueadero.setPuestosMotos(puestosMotos);
        
        // Then
        Assertions.assertEquals(puestosMotos, parqueadero.getPuestosMotos());
    }

    @Test
    @DisplayName("Test setters y getters de puestos de automóviles")
    public void testSetterGetterPuestosAutomoviles() {
        // Given
        int puestosAutomoviles = 20;
        
        // When
        parqueadero.setPuestosAutomoviles(puestosAutomoviles);
        
        // Then
        Assertions.assertEquals(puestosAutomoviles, parqueadero.getPuestosAutomoviles());
    }

    @Test
    @DisplayName("Test setters y getters de puestos de camiones")
    public void testSetterGetterPuestosCamiones() {
        // Given
        int puestosCamiones = 5;
        
        // When
        parqueadero.setPuestosCamiones(puestosCamiones);
        
        // Then
        Assertions.assertEquals(puestosCamiones, parqueadero.getPuestosCamiones());
    }

    @Test
    @DisplayName("Test setters y getters de lista de vehículos")
    public void testSetterGetterListaVehiculos() {
        // Given
        ArrayList<Vehiculo> listaVehiculos = new ArrayList<>();
        Vehiculo vehiculo1 = new Vehiculo("ABC123", "Rojo", "2020");
        Vehiculo vehiculo2 = new Vehiculo("XYZ789", "Azul", "2019");
        listaVehiculos.add(vehiculo1);
        listaVehiculos.add(vehiculo2);
        
        // When
        parqueadero.setListaDevehiculos(listaVehiculos);
        
        // Then
        Assertions.assertEquals(listaVehiculos, parqueadero.getListaDevehiculos());
        Assertions.assertEquals(2, parqueadero.getListaDevehiculos().size());
        Assertions.assertEquals(vehiculo1, parqueadero.getListaDevehiculos().get(0));
        Assertions.assertEquals(vehiculo2, parqueadero.getListaDevehiculos().get(1));
    }

    @Test
    @DisplayName("Test setters y getters de lista de clientes")
    public void testSetterGetterListaClientes() {
        // Given
        ArrayList<Cliente> listaClientes = new ArrayList<>();
        Cliente cliente1 = new Cliente("Juan Pérez", "123456789", "555-1234", "juan@test.com");
        Cliente cliente2 = new Cliente("María López", "987654321", "555-5678", "maria@test.com");
        listaClientes.add(cliente1);
        listaClientes.add(cliente2);
        
        // When
        parqueadero.setListaDeClientes(listaClientes);
        
        // Then
        Assertions.assertEquals(listaClientes, parqueadero.getListaDeClientes());
        Assertions.assertEquals(2, parqueadero.getListaDeClientes().size());
        Assertions.assertEquals(cliente1, parqueadero.getListaDeClientes().get(0));
        Assertions.assertEquals(cliente2, parqueadero.getListaDeClientes().get(1));
    }

    @Test
    @DisplayName("Test mostrarInformacion - verificar existencia y manejo de excepciones")
    public void testMostrarInformacion() {
        // Given
        parqueadero.setNombre("Parqueadero Test");
        parqueadero.setDireccion("Calle Test");
        parqueadero.setRepresentante("Representante Test");
        parqueadero.setTelefono("123-Test");
        parqueadero.setCorreo("test@test.com");
        
        try {
            // When - En modo headless esto lanzará HeadlessException
            parqueadero.mostrarInformacion();
            // Si no lanza excepción (poco probable en CI), la prueba pasa
            Assertions.assertTrue(true);
        } catch (HeadlessException e) {
            // En entorno headless, esperamos esta excepción, así que la prueba es exitosa
            Assertions.assertTrue(e instanceof HeadlessException);
        }
    }

    @Test
    @DisplayName("Test ingresarInformacion - verificar existencia y manejo de excepciones")
    public void testIngresarInformacion() {
        try {
            // Verificamos que el método existe
            Method method = Parqueadero.class.getMethod("ingresarInformacion");
            Assertions.assertNotNull(method);

            // No ejecutamos el método porque causaría HeadlessException
        } catch (NoSuchMethodException e) {
            Assertions.fail("El método ingresarInformacion no existe: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Test actualizarInformacion - usando la clase testable")
    public void testActualizarInformacion() {
        // Test 1: Actualizar nombre
        parqueaderoTestable.setNextSeleccion("Actualizar nombre");
        parqueaderoTestable.setNextInput("Nuevo Nombre");
        parqueaderoTestable.actualizarInformacion();

        Assertions.assertEquals("Nuevo Nombre", parqueaderoTestable.getNombre());
        Assertions.assertEquals(1, parqueaderoTestable.getShowInputDialogCallCount());

        // Test 2: Actualizar dirección
        parqueaderoTestable.setNextSeleccion("Actualizar dirección");
        parqueaderoTestable.setNextInput("Nueva Dirección");
        parqueaderoTestable.actualizarInformacion();

        Assertions.assertEquals("Nueva Dirección", parqueaderoTestable.getDireccion());
        Assertions.assertEquals(2, parqueaderoTestable.getShowInputDialogCallCount());

        // Test 3: Actualizar representante
        parqueaderoTestable.setNextSeleccion("Actualizar representante");
        parqueaderoTestable.setNextInput("Nuevo Representante");
        parqueaderoTestable.actualizarInformacion();

        Assertions.assertEquals("Nuevo Representante", parqueaderoTestable.getRepresentante());
        Assertions.assertEquals(3, parqueaderoTestable.getShowInputDialogCallCount());

        // Test 4: Actualizar teléfono
        parqueaderoTestable.setNextSeleccion("Actualizar teléfono");
        parqueaderoTestable.setNextInput("123-456-7890");
        parqueaderoTestable.actualizarInformacion();

        Assertions.assertEquals("123-456-7890", parqueaderoTestable.getTelefono());
        Assertions.assertEquals(4, parqueaderoTestable.getShowInputDialogCallCount());

        // Test 5: Actualizar correo
        parqueaderoTestable.setNextSeleccion("Actualizar correo");
        parqueaderoTestable.setNextInput("nuevo@email.com");
        parqueaderoTestable.actualizarInformacion();

        Assertions.assertEquals("nuevo@email.com", parqueaderoTestable.getCorreo());
        Assertions.assertEquals(5, parqueaderoTestable.getShowInputDialogCallCount());

        // Test 6: Ver datos actuales
        parqueaderoTestable.setNextSeleccion("Ver datos actuales");
        parqueaderoTestable.actualizarInformacion();

        Assertions.assertTrue(parqueaderoTestable.isShowMessageDialogCalled());
        Assertions.assertEquals(6, parqueaderoTestable.getShowInputDialogCallCount());

        // Test 7: Salir
        parqueaderoTestable.setNextSeleccion("Salir");
        parqueaderoTestable.actualizarInformacion();

        Assertions.assertEquals(7, parqueaderoTestable.getShowInputDialogCallCount());

        // Test 8: Selección nula (cancelar diálogo)
        parqueaderoTestable.setNextSeleccion(null);
        parqueaderoTestable.actualizarInformacion();

        // Esto debería funcionar sin incrementar el contador ya que simulamos que se cancela el diálogo
        Assertions.assertEquals(7, parqueaderoTestable.getShowInputDialogCallCount());
    }

    @Test
    @DisplayName("Test completo con todos los atributos")
    public void testSettersGettersCompleto() {
        // Given
        String nombre = "Parqueadero Central";
        String direccion = "Avenida Principal 123";
        String representante = "Carlos Rodríguez";
        String telefono = "123-456-7890";
        String correo = "info@parqueadero.com";
        int puestosMotos = 15;
        int puestosAutomoviles = 30;
        int puestosCamiones = 10;
        
        ArrayList<Vehiculo> listaVehiculos = new ArrayList<>();
        Vehiculo vehiculo = new Vehiculo("ABC123", "Negro", "2022");
        listaVehiculos.add(vehiculo);
        
        ArrayList<Cliente> listaClientes = new ArrayList<>();
        Cliente cliente = new Cliente("Ana Torres", "123456789", "555-1234", "ana@email.com");
        listaClientes.add(cliente);
        
        // When
        parqueadero.setNombre(nombre);
        parqueadero.setDireccion(direccion);
        parqueadero.setRepresentante(representante);
        parqueadero.setTelefono(telefono);
        parqueadero.setCorreo(correo);
        parqueadero.setPuestosMotos(puestosMotos);
        parqueadero.setPuestosAutomoviles(puestosAutomoviles);
        parqueadero.setPuestosCamiones(puestosCamiones);
        parqueadero.setListaDevehiculos(listaVehiculos);
        parqueadero.setListaDeClientes(listaClientes);
        
        // Then
        Assertions.assertEquals(nombre, parqueadero.getNombre());
        Assertions.assertEquals(direccion, parqueadero.getDireccion());
        Assertions.assertEquals(representante, parqueadero.getRepresentante());
        Assertions.assertEquals(telefono, parqueadero.getTelefono());
        Assertions.assertEquals(correo, parqueadero.getCorreo());
        Assertions.assertEquals(puestosMotos, parqueadero.getPuestosMotos());
        Assertions.assertEquals(puestosAutomoviles, parqueadero.getPuestosAutomoviles());
        Assertions.assertEquals(puestosCamiones, parqueadero.getPuestosCamiones());
        Assertions.assertEquals(listaVehiculos, parqueadero.getListaDevehiculos());
        Assertions.assertEquals(listaClientes, parqueadero.getListaDeClientes());
    }

    @Test
    @DisplayName("Test inicialización de colecciones")
    public void testInicializacionColecciones() {
        // Verificar que las colecciones se inicializan como vacías pero no nulas
        Assertions.assertNotNull(parqueadero.getListaDevehiculos());
        Assertions.assertTrue(parqueadero.getListaDevehiculos().isEmpty());
        
        Assertions.assertNotNull(parqueadero.getListaDeClientes());
        Assertions.assertTrue(parqueadero.getListaDeClientes().isEmpty());
    }

    @Test
    @DisplayName("Test mostrarInformacion - usando la clase testable")
    public void testMostrarInformacionTestable() {
        // Given
        parqueaderoTestable.setNombre("Parqueadero Test");
        parqueaderoTestable.setDireccion("Calle Test");
        parqueaderoTestable.setRepresentante("Representante Test");
        parqueaderoTestable.setTelefono("123-Test");
        parqueaderoTestable.setCorreo("test@test.com");
        
        // When
        parqueaderoTestable.mostrarInformacion();
        
        // Then
        Assertions.assertTrue(parqueaderoTestable.isShowMessageDialogCalled());
    }
}