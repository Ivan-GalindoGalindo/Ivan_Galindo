package modelo;

/**
 * Clase login
 * 
 * Representa a los usuarios del sistema que pueden acceder
 * mediante un inicio de sesión (login).
 * 
 * Contiene información básica como nombre, correo, contraseña,
 * teléfono y rol (tipo de usuario).
 * 
 * Esta clase funciona como un modelo de datos (POJO) para
 * transportar información entre la base de datos y la aplicación.
 * 
 * Autor: ( Ivan Galindo Galindo e George Ortega Leon )
 */
public class login {

    // Identificador único del usuario
    private int id;

    // Nombre del usuario
    private String nombre;

    // Correo electrónico del usuario (usado para iniciar sesión)
    private String correo;

    // Contraseña del usuario
    private String pass;

    // Número telefónico del usuario
    private String Telefono;

    // Rol del usuario (ejemplo: admin, empleado)
    private String rol;

    /**
     * Constructor vacío
     * Permite crear un objeto sin inicializar atributos.
     */
    public login(){
       
    }

    /**
     * Constructor con parámetros
     * Permite inicializar todos los atributos del usuario.
     * 
     * @param id Identificador del usuario
     * @param nombre Nombre del usuario
     * @param correo Correo electrónico
     * @param pass Contraseña
     * @param telefono Número telefónico
     * @param rol Tipo de usuario
     */
    public login(int id, String nombre, String correo, String pass, String telefono, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.pass = pass;
        this.Telefono = telefono;
        this.rol = rol;
    }

    /**
     * Obtiene el ID del usuario
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID del usuario
     * @param id Identificador único
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del usuario
     * @return nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del usuario
     * @param nombre Nombre del usuario
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el correo del usuario
     * @return correo
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Establece el correo del usuario
     * @param correo Correo electrónico
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Obtiene la contraseña del usuario
     * @return pass
     */
    public String getPass() {
        return pass;
    }

    /**
     * Establece la contraseña del usuario
     * @param pass Contraseña
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    /**
     * Obtiene el teléfono del usuario
     * @return Telefono
     */
    public String getTelefono() {
        return Telefono;
    }

    /**
     * Establece el teléfono del usuario
     * @param Telefono Número telefónico
     */
    public void setTelefono(String Telefono) {
        this.Telefono = Telefono;
    }

    /**
     * Obtiene el rol del usuario
     * @return rol
     */
    public String getRol() {
        return rol;
    }

    /**
     * Establece el rol del usuario
     * @param rol Tipo de usuario (admin / empleado)
     */
    public void setRol(String rol) {
        this.rol = rol;
    }
}