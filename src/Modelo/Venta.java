package modelo;

/**
 * Clase Venta
 * 
 * Representa una venta realizada dentro del sistema.
 * Contiene información general de la transacción como el cliente,
 * el vendedor, el total de la compra y la fecha.
 * 
 * Esta clase funciona como un modelo de datos (POJO) que permite
 * almacenar y transportar información entre la base de datos
 * y la aplicación.
 * 
 * Autor: (Ivan Galindo Galindo e George Ortega Leon)
 */
public class Venta {

    // Identificador único de la venta
    private int id;

    // Nombre del cliente (puede ser genérico si no se registra cliente)
    private String cliente;

    // Nombre del usuario o empleado que realizó la venta
    private String vendedor;

    // Total de la venta
    private double total;

    // Fecha en la que se realizó la venta
    private String fecha;

    /**
     * Constructor vacío
     * Permite crear un objeto sin inicializar valores.
     */
    public Venta (){
        
    }

    /**
     * Constructor con parámetros
     * Permite inicializar todos los atributos de la venta.
     * 
     * @param id Identificador de la venta
     * @param cliente Nombre del cliente
     * @param vendedor Nombre del vendedor
     * @param total Total de la venta
     * @param fecha Fecha de la venta
     */
    public Venta(int id, String cliente, String vendedor, double total, String fecha) {
        this.id = id;
        this.cliente = cliente;
        this.vendedor = vendedor;
        this.total = total;
        this.fecha = fecha;
    }

    /**
     * Obtiene el ID de la venta
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID de la venta
     * @param id Identificador único
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del cliente
     * @return cliente
     */
    public String getCliente() {
        return cliente;
    }

    /**
     * Establece el nombre del cliente
     * @param cliente Nombre del cliente
     */
    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    /**
     * Obtiene el nombre del vendedor
     * @return vendedor
     */
    public String getVendedor() {
        return vendedor;
    }

    /**
     * Establece el nombre del vendedor
     * @param vendedor Usuario que realizó la venta
     */
    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    /**
     * Obtiene el total de la venta
     * @return total
     */
    public double getTotal() {
        return total;
    }

    /**
     * Establece el total de la venta
     * @param total Monto total
     */
    public void setTotal(double total) {
        this.total = total;
    }

    /**
     * Obtiene la fecha de la venta
     * @return fecha
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha de la venta
     * @param fecha Fecha (formato recomendado: YYYY-MM-DD)
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
