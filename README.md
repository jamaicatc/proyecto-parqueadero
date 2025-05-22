# Sistema de Gestión de Parqueadero

Proyecto orientado a la gestión de un parqueadero, hecho en Java para practicar programación orientada a objetos.

## Manual de Usuario

### Índice
1. [Introducción](#introducción)
2. [Menú Principal](#menú-principal)
3. [Gestión de Clientes](#gestión-de-clientes)
4. [Gestión de Vehículos](#gestión-de-vehículos)
5. [Gestión de Parqueadero](#gestión-de-parqueadero)
6. [Gestión de Membresías](#gestión-de-membresías)
7. [Gestión de Pagos](#gestión-de-pagos)
8. [Reportes](#reportes)

### Introducción

Bienvenido al Sistema de Gestión de Parqueadero, una aplicación diseñada para administrar de manera eficiente las operaciones diarias de un parqueadero. Este manual le guiará a través de todas las funcionalidades disponibles en el sistema.

### Menú Principal

Al iniciar la aplicación, se mostrará el menú principal con las siguientes opciones:

- **Gestión de Clientes**: Administrar la información de los clientes.
- **Gestión de Vehículos**: Administrar los vehículos registrados.
- **Gestión de Parqueadero**: Configurar y administrar el parqueadero.
- **Gestión de Membresías**: Administrar las membresías de los clientes.
- **Gestión de Pagos**: Realizar y consultar pagos.
- **Reportes**: Generar diversos informes del sistema.
- **Salir**: Cerrar la aplicación.

Para seleccionar una opción, haga clic en el botón correspondiente.

### Gestión de Clientes

Este módulo permite administrar la información de los clientes del parqueadero.

#### Añadir Cliente
1. Seleccione "Añadir Cliente" en el menú de Gestión de Clientes.
2. Complete el formulario con la información solicitada:
   - Nombre
   - Cédula
   - Teléfono
   - Correo Electrónico
3. Haga clic en "Aceptar" para guardar la información.

#### Buscar Cliente
1. Seleccione "Buscar Cliente" en el menú.
2. Ingrese la cédula del cliente que desea buscar.
3. El sistema mostrará la información del cliente si existe.

#### Actualizar Cliente
1. Seleccione "Actualizar Cliente" en el menú.
2. Busque el cliente por su cédula.
3. Seleccione qué información desea actualizar:
   - Nombre
   - Teléfono
   - Correo Electrónico
4. Ingrese la nueva información y confirme.

#### Ver Todos los Clientes
1. Seleccione "Ver Todos los Clientes" para mostrar un listado de todos los clientes registrados.
2. Navegue entre páginas si hay múltiples clientes.

### Gestión de Vehículos

Este módulo permite administrar los vehículos asociados a los clientes.

#### Registrar Vehículo
1. Seleccione "Registrar Vehículo" en el menú de Gestión de Vehículos.
2. Busque primero el cliente al que se asociará el vehículo.
3. Complete el formulario con la información del vehículo:
   - Placa
   - Color
   - Modelo
   - Tipo de Vehículo (Automóvil, Moto o Camión)
4. Haga clic en "Aceptar" para registrar el vehículo.

#### Buscar Vehículo
1. Seleccione "Buscar Vehículo" en el menú.
2. Ingrese la placa del vehículo que desea buscar.
3. El sistema mostrará la información del vehículo si existe.

#### Actualizar Vehículo
1. Seleccione "Actualizar Vehículo" en el menú.
2. Ingrese la placa del vehículo a actualizar.
3. Seleccione qué información desea modificar:
   - Placa
   - Color
   - Modelo
4. Ingrese la nueva información y confirme.

#### Ver Vehículos Asociados
1. Seleccione "Ver Vehículos Asociados" en el menú.
2. Busque el cliente cuyos vehículos desea ver.
3. El sistema mostrará todos los vehículos asociados a ese cliente.

### Gestión de Parqueadero

Este módulo permite configurar y administrar el parqueadero.

#### Configurar Espacios
1. Seleccione "Configurar Espacios" en el menú de Gestión de Parqueadero.
2. Ingrese la cantidad de espacios disponibles para:
   - Motos
   - Automóviles
   - Camiones
3. Confirme la configuración.

#### Configurar Tarifas
1. Seleccione "Configurar Tarifas" en el menú.
2. Ingrese la tarifa por hora para cada tipo de vehículo:
   - Motos
   - Automóviles
   - Camiones
3. Confirme las tarifas configuradas.

#### Registrar Entrada de Vehículo
1. Seleccione "Registrar Entrada de Vehículo" en el menú.
2. Seleccione el tipo de vehículo.
3. Verifique que haya espacios disponibles.
4. Ingrese la información del vehículo:
   - Placa
   - Color
   - Modelo
5. El sistema registrará la hora de entrada y asignará un espacio.

#### Registrar Salida de Vehículo
1. Seleccione "Registrar Salida de Vehículo" en el menú.
2. Ingrese la placa del vehículo que sale.
3. El sistema calculará el tiempo de permanencia y el monto a pagar.
4. Se mostrará una factura con los detalles del servicio.

#### Mostrar Estado del Parqueadero
1. Seleccione "Mostrar Estado del Parqueadero" para ver:
   - Capacidad total
   - Espacios ocupados y disponibles por tipo de vehículo
   - Porcentaje de ocupación

#### Listar Vehículos Actuales
1. Seleccione "Listar Vehículos Actuales" para ver todos los vehículos que están actualmente en el parqueadero.
2. El listado incluye:
   - Placa
   - Tipo de vehículo
   - Hora de entrada
   - Información de membresía si aplica

### Gestión de Membresías

Este módulo permite administrar las membresías de los clientes.

#### Registrar Membresía
1. Seleccione "Registrar Membresía" en el menú de Gestión de Membresías.
2. Busque el vehículo para el cual desea registrar la membresía.
3. Seleccione el cliente o cree uno nuevo.
4. Elija el tipo de membresía:
   - Mensual
   - Trimestral
   - Anual
5. Confirme la membresía para procesar el pago.

#### Verificar Vigencia
1. Seleccione "Verificar Vigencia" en el menú.
2. Busque el vehículo cuya membresía desea verificar.
3. El sistema mostrará el estado de la membresía:
   - Vigente (con días restantes)
   - Vencida
   - Sin membresía

#### Renovar Membresía
1. Seleccione "Renovar Membresía" en el menú.
2. Busque el vehículo cuya membresía desea renovar.
3. Confirme si desea renovar con el mismo tipo o cambiar.
4. Si cambia, seleccione el nuevo tipo de membresía.
5. Confirme para procesar el pago de renovación.

#### Generar Reporte de Membresías
1. Seleccione "Generar Reporte de Membresías" para ver:
   - Total de clientes con membresías activas
   - Membresías próximas a vencer (30 días o menos)
   - Detalle de todas las membresías activas

### Gestión de Pagos

Este módulo permite gestionar los pagos realizados en el parqueadero.

#### Registrar Pago
1. Seleccione "Registrar Pago" en el menú de Gestión de Pagos.
2. Ingrese la placa del vehículo.
3. El sistema calculará el monto a pagar según:
   - Tipo de vehículo
   - Horas de estacionamiento
4. Confirme para registrar el pago.

#### Registrar Pago por Período
1. Seleccione "Registrar Pago por Período" en el menú.
2. Busque el cliente y seleccione uno de sus vehículos.
3. Elija el período de membresía:
   - 1 Mes
   - 3 Meses
   - 1 Año
4. Confirme el pago mostrando:
   - Monto
   - Fechas de inicio y fin
   - Tipo de membresía

#### Buscar Pago
1. Seleccione "Buscar Pago" en el menú.
2. Ingrese el ID del pago que desea buscar.
3. El sistema mostrará los detalles del pago si existe.

### Reportes

Este módulo permite generar diferentes reportes financieros.

#### Ingresos del Día
1. Seleccione "Ingresos del Día" en el menú de Reportes.
2. El sistema mostrará un reporte con:
   - Total de ingresos del día actual
   - Desglose por tipo de servicio (estacionamiento/membresías)
   - Desglose por tipo de vehículo
   - Listado detallado de pagos

#### Ingresos del Mes
1. Seleccione "Ingresos del Mes" en el menú.
2. Elija el mes para el reporte.
3. El sistema generará un reporte similar al diario pero para todo el mes seleccionado.

#### Ingresos del Año
1. Seleccione "Ingresos del Año" en el menú.
2. Ingrese el año para el reporte.
3. El sistema generará un reporte consolidado para todo el año.

#### Ingresos por Rango de Fechas
1. Seleccione "Ingresos por Rango de Fechas" en el menú.
2. Ingrese la fecha de inicio y la fecha de fin.
3. El sistema generará un reporte para el período específico.

### Consejos Útiles

- **Búsqueda de Clientes**: Al buscar un cliente, asegúrese de ingresar la cédula completa y correcta.
- **Membresías**: Las membresías ofrecen descuentos significativos para clientes frecuentes.
- **Reportes**: Utilice los reportes regularmente para analizar el rendimiento del parqueadero.
- **Configuración de Espacios**: Actualice la configuración de espacios cuando sea necesario para optimizar el uso del parqueadero.
- **Backup**: Se recomienda realizar copias de seguridad periódicas de la información del sistema.

---

¡Gracias por utilizar nuestro Sistema de Gestión de Parqueadero! Si tiene alguna pregunta adicional o necesita soporte, por favor contacte al administrador del sistema.