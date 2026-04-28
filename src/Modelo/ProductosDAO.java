package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;

/**
 * Clase ProductosDAO
 * 
 * Esta clase es un DAO (Data Access Object) encargado de gestionar
 * todas las operaciones relacionadas con la tabla productos y config
 * en la base de datos.
 * 
 * Permite realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * además de búsquedas y carga de datos en componentes gráficos.
 * 
 * Autor: (Ivan Galindo Galindo e George Ortega Leon)
 */
public class ProductosDAO {

    // Objetos de conexión a la base de datos
    Connection con;
    Conexion cn = new Conexion();
    PreparedStatement ps;
    ResultSet rs;

    /**
     * Registra un nuevo producto en la base de datos
     * 
     * @param pro Objeto Productos con los datos a guardar
     * @return true si se registró correctamente, false si hubo error
     */
    public boolean RegistrarProductos(Productos pro) {
        String sql = "INSERT INTO productos (codigo, nombre, proveedor, stock, precio) VALUES (?,?,?,?,?)";
        try {
            con = cn.getConecction();
            ps = con.prepareStatement(sql);
            ps.setString(1, pro.getCodigo());
            ps.setString(2, pro.getNombre());
            ps.setString(3, pro.getProveedor());
            ps.setInt(4, pro.getStock());
            ps.setDouble(5, pro.getPrecio());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al registrar producto: " + e.toString());
            return false;
        }
    }

    /**
     * Llena un JComboBox con los nombres de proveedores
     * 
     * @param proveedor JComboBox donde se cargarán los datos
     */
    public void ConsultarProveedor(JComboBox proveedor) {
        String sql = "SELECT nombre FROM proveedor";
        try {
            con = cn.getConecction();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                proveedor.addItem(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar proveedores: " + e.toString());
        }
    }

    /**
     * Lista todos los productos de la base de datos
     * 
     * @return Lista de productos
     */
    public List<Productos> ListarProductos() {
        List<Productos> listaPro = new ArrayList<>();
        String sql = "SELECT * FROM productos";

        try {
            con = cn.getConecction();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Productos pro = new Productos();
                pro.setId(rs.getInt("id"));
                pro.setCodigo(rs.getString("codigo"));
                pro.setNombre(rs.getString("nombre"));
                pro.setProveedor(rs.getString("proveedor"));
                pro.setStock(rs.getInt("stock"));
                pro.setPrecio(rs.getDouble("precio"));
                listaPro.add(pro);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar productos: " + e.toString());
        }
        return listaPro;
    }

    /**
     * Elimina un producto por su ID
     * 
     * @param id Identificador del producto
     * @return true si se eliminó correctamente
     */
    public boolean EliminarProductos(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";
        try {
            con = cn.getConecction();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar producto: " + e.toString());
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
    }

    /**
     * Modifica los datos de un producto existente
     * 
     * @param pro Objeto con los nuevos datos
     * @return true si se actualizó correctamente
     */
    public boolean ModificarProductos(Productos pro) {
        String sql = "UPDATE productos SET codigo=?, nombre=?, proveedor=?, stock=?, precio=? WHERE id=?";
        try {
            con = cn.getConecction();
            ps = con.prepareStatement(sql);
            ps.setString(1, pro.getCodigo());
            ps.setString(2, pro.getNombre());
            ps.setString(3, pro.getProveedor());
            ps.setInt(4, pro.getStock());
            ps.setDouble(5, pro.getPrecio());
            ps.setInt(6, pro.getId());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al modificar producto: " + e.toString());
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }

    /**
     * Busca un producto por su código
     * 
     * @param cod Código del producto
     * @return Objeto Productos con los datos encontrados
     */
    public Productos BuscarPro(String cod) {
        Productos producto = new Productos();
        String sql = "SELECT * FROM productos WHERE codigo = ?";

        try {
            con = cn.getConecction();
            ps = con.prepareStatement(sql);
            ps.setString(1, cod);
            rs = ps.executeQuery();

            if (rs.next()) {
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecio(rs.getDouble("precio"));
                producto.setStock(rs.getInt("stock"));
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar producto: " + e.toString());
        }
        return producto;
    }

    /**
     * Obtiene los datos de configuración del sistema
     * 
     * @return Objeto Config con los datos
     */
    public Config BuscarDatos() {
        Config conf = new Config();
        String sql = "SELECT * FROM config";

        try {
            con = cn.getConecction();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                conf.setId(rs.getInt("id"));
                conf.setRuc(rs.getInt("ruc"));
                conf.setNombre(rs.getString("nombre"));
                conf.setTelefono(rs.getString("telefono"));
                conf.setDireccion(rs.getString("direccion"));
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener configuración: " + e.toString());
        }
        return conf;
    }

    /**
     * Modifica los datos de configuración del sistema
     * 
     * @param conf Objeto Config con los nuevos datos
     * @return true si se actualizó correctamente
     */
    public boolean ModificarDatos(Config conf) {
        String sql = "UPDATE config SET ruc=?, nombre=?, telefono=?, direccion=? WHERE id=?";

        try {
            con = cn.getConecction();
            ps = con.prepareStatement(sql);
            ps.setInt(1, conf.getRuc());
            ps.setString(2, conf.getNombre());
            ps.setString(3, conf.getTelefono());
            ps.setString(4, conf.getDireccion());
            ps.setInt(5, conf.getId());
            ps.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al modificar configuración: " + e.toString());
            return false;

        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }

    /**
     * Busca productos por código o nombre (filtro dinámico)
     * 
     * @param valor Texto a buscar
     * @return Lista de productos encontrados
     */
    public List<Productos> BuscarProductos(String valor) {

        List<Productos> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE codigo LIKE ? OR nombre LIKE ?";

        try {
            con = cn.getConecction();
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + valor + "%");
            ps.setString(2, "%" + valor + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                Productos pro = new Productos();
                pro.setId(rs.getInt("id"));
                pro.setCodigo(rs.getString("codigo"));
                pro.setNombre(rs.getString("nombre"));
                pro.setProveedor(rs.getString("proveedor"));
                pro.setStock(rs.getInt("stock"));
                pro.setPrecio(rs.getDouble("precio"));
                lista.add(pro);
            }

        } catch (SQLException e) {
            System.out.println("Error en búsqueda: " + e.toString());
        }

        return lista;
    }
}
