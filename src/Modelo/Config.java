package modelo;

/**
 * Clase Config
 * 
 * Representa la configuración general del sistema o del negocio.
 * Contiene información básica como nombre, RUC, teléfono y dirección.
 * 
 * Esta clase funciona como un modelo de datos, utilizado para
 * transportar información entre la base de datos y la aplicación.
 * 
 * Autores: (Ivan Galindo Galindio e George Ortega Leon )
 */
public class Config {

    // Identificador único de la configuración
    private int id;

    // Registro Único de Contribuyentes (RUC) del negocio
    private int ruc;

    // Nombre del negocio o empresa
    private String nombre;

    // Número telefónico de contacto
    private String telefono;    

    // Dirección física del negocio
    private String direccion;

    /**
     * Constructor vacío
     * Permite crear un objeto sin inicializar valores.
     */
    public Config() {
    }

    /**
     * Constructor con parámetros
     * Permite inicializar todos los atributos de la clase.
     * 
     * @param id Identificador único
     * @param ruc Registro del negocio
     * @param nombre Nombre del negocio
     * @param telefono Número de teléfono
     * @param direccion Dirección del negocio
     */
    public Config(int id, int ruc, String nombre, String telefono, String direccion) {
        this.id = id;
        this.ruc = ruc;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    /**
     * Obtiene el ID de la configuración
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID de la configuración
     * @param id Identificador único
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el RUC del negocio
     * @return ruc
     */
    public int getRuc() {
        return ruc;
    }

    /**
     * Establece el RUC del negocio
     * @param ruc Registro del negocio
     */
    public void setRuc(int ruc) {
        this.ruc = ruc;
    }

    /**
     * Obtiene el nombre del negocio
     * @return nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del negocio
     * @param nombre Nombre de la empresa
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el número telefónico
     * @return telefono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el número telefónico
     * @param telefono Número de contacto
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene la dirección del negocio
     * @return direccion
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección del negocio
     * @param direccion Ubicación física
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}

