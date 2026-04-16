# 🚗 Sistema de Parqueo Público

Aplicación de escritorio desarrollada en **Java + Swing** para la gestión de un parqueo público.  
Este proyecto fue construido con **arquitectura por capas** y **persistencia en archivos `.txt`**, cumpliendo con los requisitos de la evaluación práctica de **Programación II**.

---

## ✨ Descripción

El sistema permite:

- Registrar el **ingreso** de vehículos
- Registrar la **salida** de vehículos
- Calcular automáticamente el **monto a pagar**
- Visualizar los vehículos **activos** en el parqueo
- Consultar el **historial** de registros
- Mantener la información guardada en archivos `.txt`

---

## 🧩 Funcionalidades principales

### ✅ Registro de ingreso
- Ingreso de la **placa**
- Selección del tipo de vehículo:
  - 🚘 Carro
  - 🏍️ Moto
- Registro automático de la **hora de entrada**

### ✅ Registro de salida
- Selección del vehículo desde la tabla de activos
- Registro automático de la **hora de salida**
- Cálculo automático del **monto a pagar**

### ✅ Visualización
- 📋 Tabla con vehículos actualmente en el parqueo
- 🕘 Tabla con historial de entradas y salidas

### ✅ Extras implementados
- 🧹 Limpieza del historial
- 💾 Persistencia local en archivos `.txt`

---

## 📌 Reglas de negocio

- ❌ No se permite ingresar una placa que ya esté activa en el parqueo.
- ✅ Todos los campos obligatorios son validados antes de procesar.
- 💰 El cobro se realiza con una tarifa de **CRC 500 por hora o fracción**.
- 🧠 Toda la lógica, validaciones y cálculos están en la capa de negocio.

---

## 🏗️ Arquitectura del proyecto

El sistema sigue esta estructura:

`Presentación -> Lógica de Negocio -> Acceso a Datos`



