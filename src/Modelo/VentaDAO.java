package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase VentaDAO
 * 
 * Se encarga de realizar las operaciones de acceso a datos relacionadas
 * con las ventas del sistema.
 * 
 * Permite registrar ventas, guardar detalles de venta, consultar el último
 * ID de venta, actualizar stock y listar ventas activas.
 * 
 * Autor:( Ivan Galindo Galindo e George Ortega Leon) 
 */
public class VentaDAO {

    // Objetos necesarios para la conexión y consultas SQL
    Connection con;
    Conexion cn = new Conexion();
    PreparedStatement ps;
    ResultSet rs;

    // Variable auxiliar para retornar resultados numéricos
    int r;

    /**
     * Obtiene el último ID registrado en la tabla ventas.
     * 
     * @return Último ID de venta registrado
     */
    public int IdVenta() {
        int id = 0;
        String sql = "SELECT MAX(id) FROM ventas";

        try {
            con = cn.getConecction();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                id = rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener ID de venta: " + e.toString());
        }

        return id;
    }

    /**
     * Registra una nueva venta en la base de datos.
     * 
     * @param v Objeto Venta con los datos de la venta
     * @return Valor entero auxiliar de resultado
     */
    public int RegistrarVenta(Venta v) {
        String sql = "INSERT INTO ventas (vendedor, total, fecha) VALUES (?,?,?)";

        try {
            con = cn.getConecction();
            ps = con.prepareStatement(sql);

            ps.setString(1, v.getVendedor());
            ps.setDouble(2, v.getTotal());
            ps.setString(3, v.getFecha());

            ps.execute();

        } catch (SQLException e) {
            System.out.println("Error al registrar venta: " + e.toString());

        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }

        return r;
    }

    /**
     * Registra el detalle de una venta.
     * 
     * Cada detalle representa un producto incluido dentro de una venta.
     * 
     * @param Dv Objeto Detalle con la información del producto vendido
     * @return Valor entero auxiliar de resultado
     */
    public int RegistrarDetalle(Detalle Dv) {
        String sql = "INSERT INTO detalle (cod_pro, cantidad, precio, id_venta) VALUES (?,?,?,?)";

        try {
            con = cn.getConecction();
            ps = con.prepareStatement(sql);

            ps.setString(1, Dv.getCod_pro());
            ps.setInt(2, Dv.getCantidad());
            ps.setDouble(3, Dv.getPrecio());

            // ID de la venta a la que pertenece el detalle
            ps.setInt(4, Dv.getId());

            ps.execute();

        } catch (SQLException e) {
            System.out.println("Error al registrar detalle: " + e.toString());

        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }

        return r;
    }

    /**
     * Actualiza el stock de un producto después de realizar una venta.
     * 
     * @param cant Nueva cantidad disponible en inventario
     * @param cod Código del producto
     * @return true si se actualizó correctamente, false si ocurrió un error
     */
    public boolean ActualizarStock(int cant, String cod) {
        String sql = "UPDATE productos SET stock = ? WHERE codigo = ?";

        try {
            con = cn.getConecction();
            ps = con.prepareStatement(sql);

            ps.setInt(1, cant);
            ps.setString(2, cod);

            ps.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar stock: " + e.toString());
            return false;
        }
    }

    /**
     * Lista todas las ventas activas registradas en el sistema.
     * 
     * @return Lista de ventas activas
     */
    public List<Venta> Listarventas() {
        List<Venta> ListaVenta = new ArrayList<>();
        String sql = "SELECT * FROM ventas WHERE estado = 1";

        try {
            con = cn.getConecction();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Venta vent = new Venta();

                vent.setId(rs.getInt("id"));
                vent.setVendedor(rs.getString("vendedor"));
                vent.setTotal(rs.getDouble("total"));
                vent.setFecha(rs.getString("fecha"));

                ListaVenta.add(vent);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar ventas: " + e.toString());
        }

        return ListaVenta;
    }
}