package modelo;

/**
 * Clase Detalle
 * 
 * Representa el detalle de una venta dentro del sistema.
 * Cada objeto de esta clase corresponde a un producto incluido
 * en una venta específica.
 * 
 * Contiene información como el código del producto, cantidad,
 * precio y la relación con la venta (id_venta).
 * 
 * Autor: Ivan G
 */
public class Detalle {

    // Identificador único del detalle
    private int id;

    // Código del producto vendido
    private String cod_pro;

    // Cantidad de productos vendidos
    private int cantidad;

    // Precio unitario del producto
    private double precio;

    // Identificador de la venta a la que pertenece este detalle
    private int id_venta;

    /**
     * Constructor vacío
     * Permite crear un objeto sin inicializar atributos.
     */
    public Detalle(){
        
    }

    /**
     * Constructor con parámetros
     * Permite inicializar todos los atributos del detalle.
     * 
     * @param id Identificador del detalle
     * @param cod_pro Código del producto
     * @param cantidad Cantidad vendida
     * @param precio Precio unitario
     * @param id_venta ID de la venta asociada
     */
    public Detalle(int id, String cod_pro, int cantidad, double precio, int id_venta) {
        this.id = id;
        this.cod_pro = cod_pro;
        this.cantidad = cantidad;
        this.precio = precio;
        this.id_venta = id_venta;
    }

    /**
     * Obtiene el ID del detalle
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID del detalle
     * @param id Identificador único
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el código del producto
     * @return cod_pro
     */
    public String getCod_pro() {
        return cod_pro;
    }

    /**
     * Establece el código del producto
     * @param cod_pro Código del producto
     */
    public void setCod_pro(String cod_pro) {
        this.cod_pro = cod_pro;
    }

    /**
     * Obtiene la cantidad vendida
     * @return cantidad
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad vendida
     * @param cantidad Número de productos
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el precio unitario del producto
     * @return precio
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio unitario del producto
     * @param precio Precio del producto
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Obtiene el ID de la venta asociada
     * @return id_venta
     */
    public int getId_venta() {
        return id_venta;
    }

    /**
     * Establece el ID de la venta asociada
     * @param id_venta Identificador de la venta
     */
    public void setId_venta(int id_venta) {
        this.id_venta = id_venta;
    }
}
 
