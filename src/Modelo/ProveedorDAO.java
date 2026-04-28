package modelo;

import Modelo.Proveedor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase ProveedorDAO
 * 
 * Se encarga de realizar las operaciones de acceso a datos
 * relacionadas con los proveedores del sistema.
 * 
 * Permite registrar, listar, modificar y eliminar proveedores
 * dentro de la base de datos.
 * 
 * Autor: Ivan G
 */
public class ProveedorDAO {

    // Instancia de la clase conexión
    Conexion cn = new Conexion();

    // Objetos necesarios para manejar la conexión y consultas SQL
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    /**
     * Registra un nuevo proveedor en la base de datos.
     * 
     * @param pr Objeto Proveedor con los datos a guardar
     * @return true si el registro fue exitoso, false si ocurrió un error
     */
    public boolean RegistrarProveedor(Proveedor pr) {
        String sql = "INSERT INTO proveedor(dni, nombre, telefono, direccion, fecha) VALUES (?,?,?,?,?)";

        try {
            con = cn.getConecction();
            ps = con.prepareStatement(sql);

            ps.setInt(1, pr.getDni());
            ps.setString(2, pr.getNombre());
            ps.setString(3, pr.getTelefono());
            ps.setString(4, pr.getDireccion());
            ps.setString(5, pr.getFecha());

            ps.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al registrar proveedor: " + e.toString());
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
     * Obtiene todos los proveedores registrados.
     * 
     * @return Lista de proveedores
     */
    public List<Proveedor> ListarProveedor() {
        List<Proveedor> listaPr = new ArrayList<>();
        String sql = "SELECT * FROM proveedor";

        try {
            con = cn.getConecction();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Proveedor pr = new Proveedor();

                pr.setId(rs.getInt("id"));
                pr.setDni(rs.getInt("dni"));
                pr.setNombre(rs.getString("nombre"));
                pr.setTelefono(rs.getString("telefono"));
                pr.setDireccion(rs.getString("direccion"));
                pr.setFecha(rs.getString("fecha"));

                listaPr.add(pr);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar proveedores: " + e.toString());
        }

        return listaPr;
    }

    /**
     * Elimina un proveedor de la base de datos mediante su ID.
     * 
     * @param id Identificador del proveedor
     * @return true si se eliminó correctamente, false si ocurrió un error
     */
    public boolean EliminarProveedor(int id) {
        String sql = "DELETE FROM proveedor WHERE id = ?";

        try {
            con = cn.getConecction();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            ps.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al eliminar proveedor: " + e.toString());
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
     * Modifica los datos de un proveedor existente.
     * 
     * @param pr Objeto Proveedor con los nuevos datos
     * @return true si se modificó correctamente, false si ocurrió un error
     */
    public boolean ModificarProveedor(Proveedor pr) {
        String sql = "UPDATE proveedor SET dni=?, nombre=?, telefono=?, direccion=?, fecha=? WHERE id=?";

        try {
            con = cn.getConecction();
            ps = con.prepareStatement(sql);

            ps.setInt(1, pr.getDni());
            ps.setString(2, pr.getNombre());
            ps.setString(3, pr.getTelefono());
            ps.setString(4, pr.getDireccion());
            ps.setString(5, pr.getFecha());
            ps.setInt(6, pr.getId());

            ps.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al modificar proveedor: " + e.toString());
            return false;

        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }
}
