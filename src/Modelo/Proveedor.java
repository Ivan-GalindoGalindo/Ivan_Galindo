package Modelo;

/**
 * Clase Proveedor
 * 
 * Representa a los proveedores del sistema, es decir, las personas o empresas
 * que suministran productos al negocio.
 * 
 * Esta clase funciona como un modelo de datos (POJO) para almacenar y
 * transportar información entre la base de datos y la aplicación.
 * 
 * Autor:( Ivan Galindo Galindo e George Ortega Leon)
 */
public class Proveedor {

    // Identificador único del proveedor
    private int id;

    // Documento de identificación (DNI o RFC según el caso)
    private int dni;

    // Nombre del proveedor o empresa
    private String nombre;

    // Número telefónico de contacto
    private String telefono;

    // Dirección del proveedor
    private String direccion;

    // Fecha de registro del proveedor
    private String fecha;

    /**
     * Constructor vacío
     * Permite crear un objeto sin inicializar atributos.
     */
    public Proveedor(){
        
    }

    /**
     * Constructor con parámetros
     * Permite inicializar todos los atributos del proveedor.
     * 
     * @param id Identificador único
     * @param dni Documento del proveedor
     * @param nombre Nombre del proveedor
     * @param telefono Teléfono de contacto
     * @param direccion Dirección
     * @param fecha Fecha de registro
     */
    public Proveedor(int id, int dni, String nombre, String telefono, String direccion, String fecha) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.fecha = fecha;
    }

    /**
     * Obtiene el ID del proveedor
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID del proveedor
     * @param id Identificador único
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el DNI del proveedor
     * @return dni
     */
    public int getDni() {
        return dni;
    }

    /**
     * Establece el DNI del proveedor
     * @param dni Documento de identificación
     */
    public void setDni(int dni) {
        this.dni = dni;
    }

    /**
     * Obtiene el nombre del proveedor
     * @return nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del proveedor
     * @param nombre Nombre o razón social
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el teléfono del proveedor
     * @return telefono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el teléfono del proveedor
     * @param telefono Número de contacto
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene la dirección del proveedor
     * @return direccion
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección del proveedor
     * @param direccion Ubicación física
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Obtiene la fecha de registro del proveedor
     * @return fecha
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha de registro del proveedor
     * @param fecha Fecha (formato recomendado: YYYY-MM-DD)
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
