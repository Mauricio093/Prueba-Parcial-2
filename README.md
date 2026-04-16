# Sistema de Parqueo Publico

Aplicacion desarrollada en Java con Swing, usando arquitectura por capas y persistencia en archivos `.txt`.

## Estructura

- `src/entidades`: clases del dominio.
- `src/logica`: validaciones, reglas y calculo de cobro.
- `src/accesodatos`: manejo de archivos de persistencia.
- `src/presentacion`: interfaz grafica Swing.
- `datos`: archivos generados automaticamente para registros activos e historial.

## Funcionalidades

- Registro de ingreso con placa, tipo y hora automatica.
- Registro de salida desde la tabla de vehiculos activos.
- Calculo de cobro con tarifa de `CRC 500` por hora o fraccion.
- Visualizacion de vehiculos activos e historial completo.
- Limpieza del historial de registros.

## Reglas implementadas

- No se permite ingresar una placa que ya este activa.
- Se validan campos obligatorios antes de procesar acciones.
- La capa de presentacion no accede directamente a archivos.
- La persistencia se realiza en archivos `.txt`.

## Ejecucion

1. Abrir el proyecto en NetBeans.
2. Ejecutar el proyecto.
3. La clase principal configurada es `presentacion.Main`.

## Persistencia

Los archivos se crean automaticamente en la carpeta `datos`:

- `registros_activos.txt`
- `historial_registros.txt`
