package modelo;

/**
 * Clase Productos
 * 
 * Representa los productos disponibles dentro del sistema.
 * Contiene la información necesaria para la gestión de inventario
 * y ventas, como código, nombre, proveedor, stock y precio.
 * 
 * Esta clase funciona como un modelo de datos, que permite
 * transportar información entre la base de datos y la aplicación.
 * 
 * Autores: (Ivan Galindo Galindo e George Ortega Leon)
 */
public class Productos {

    // Identificador único del producto
    private int id;

    // Código único del producto
    private String codigo;

    // Nombre del producto
    private String nombre;

    // Nombre del proveedor del producto
    private String provvedor;

    // Cantidad disponible en inventario
    private int stock;

    // Precio del producto
    private double precio;

    /**
     * Constructor vacío
     * Permite crear un objeto sin inicializar valores.
     */
    public Productos(){
        
    }

    /**
     * Constructor con parámetros
     * Permite inicializar todos los atributos del producto.
     * 
     * @param id Identificador del producto
     * @param codigo Código del producto
     * @param nombre Nombre del producto
     * @param proveedor Nombre del proveedor
     * @param stock Cantidad en inventario
     * @param precio Precio del producto
     */
    public Productos(int id, String codigo, String nombre, String proveedor, int stock, double precio) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.provvedor = proveedor;
        this.stock = stock;
        this.precio = precio;
    }

    /**
     * Obtiene el ID del producto
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID del producto
     * @param id Identificador único
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el código del producto
     * @return codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Establece el código del producto
     * @param codigo Código único
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Obtiene el nombre del producto
     * @return nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del producto
     * @param nombre Nombre del producto
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el proveedor del producto
     * @return provvedor
     */
    public String getProveedor() {
        return provvedor;
    }

    /**
     * Establece el proveedor del producto
     * @param provvedor Nombre del proveedor
     */
    public void setProveedor(String provvedor) {
        this.provvedor = provvedor;
    }

    /**
     * Obtiene el stock disponible
     * @return stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * Establece el stock disponible
     * @param stock Cantidad en inventario
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Obtiene el precio del producto
     * @return precio
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio del producto
     * @param precio Valor del producto
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
