-- =========================================
-- CREACIÓN DE LA BASE DE DATOS
-- =========================================

-- Crea la base de datos llamada sistemaventas
create DATABASE sistemaventas;

-- Selecciona la base de datos para trabajar
USE sistemaventas;

-- =========================================
-- TABLA: usuarios
-- Almacena la información de los usuarios del sistema
-- =========================================
CREATE TABLE usuarios (
    Id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, -- Identificador único del usuario
    Nombre VARCHAR(100) NOT NULL,              -- Nombre del usuario
    Correo VARCHAR(50) NOT NULL,               -- Correo electrónico
    Pass VARCHAR(100) NOT NULL,                -- Contraseña
    Telefono VARCHAR (50) NOT NULL,            -- Teléfono del usuario
    Rol VARCHAR (50) NOT NULL                  -- Rol (Administrador / Asistente)
);

-- =========================================
-- TABLA: proveedor
-- Almacena la información de los proveedores
-- =========================================
CREATE TABLE proveedor (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, -- ID del proveedor
    dni INT  NOT NULL,                          -- Identificación del proveedor
    nombre VARCHAR(200) NOT NULL,               -- Nombre del proveedor
    telefono VARCHAR (20) NOT NULL,             -- Teléfono
    direccion VARCHAR(200) NOT NULL,            -- Dirección
    fecha VARCHAR  (50)                         -- Fecha de registro
);

-- =========================================
-- TABLA: productos
-- Almacena los productos disponibles en el sistema
-- =========================================
CREATE TABLE productos (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, -- ID del producto
    codigo VARCHAR(30) NOT NULL,                -- Código del producto
    nombre VARCHAR(200) NOT NULL,               -- Nombre del producto
    proveedor VARCHAR(100) NOT NULL,            -- Proveedor del producto
    stock INT NOT NULL,                         -- Cantidad disponible
    precio DECIMAL(10,2) NOT NULL,              -- Precio del producto
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP    -- Fecha de registro automática
);

-- =========================================
-- TABLA: detalle
-- Almacena el detalle de cada venta (productos vendidos)
-- =========================================
CREATE TABLE detalle (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, -- ID del detalle
    cod_pro VARCHAR (50) NOT NULL,              -- Código del producto
    cantidad INT NOT NULL,                      -- Cantidad vendida
    precio DECIMAL(10,2) NOT NULL,              -- Precio del producto
    id_venta INT NOT NULL                       -- ID de la venta relacionada
    estado TINYINT                              -- Estado del registro
);

-- =========================================
-- TABLA: ventas
-- Almacena las ventas realizadas
-- =========================================
CREATE TABLE ventas (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, -- ID de la venta
    vendedor VARCHAR(100) NOT NULL,             -- Nombre del vendedor
    total DECIMAL(10,2) NOT NULL,               -- Total de la venta
    fecha VARCHAR (50) NOT NULL                 -- Fecha de la venta
);

-- =========================================
-- TABLA: config
-- Almacena los datos de la empresa
-- =========================================
CREATE TABLE config (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, -- ID de configuración
    ruc int NOT NULL,                           -- RUC o identificador fiscal
    nombre VARCHAR (20) NOT NULL,               -- Nombre del negocio
    telefono INT  NOT NULL,                     -- Teléfono de la empresa
    direccion VARCHAR(200) NOT NULL             -- Dirección del negocio
);




