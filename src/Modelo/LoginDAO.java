package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase LoginDAO
 * 
 * Esta clase se encarga de gestionar las operaciones relacionadas
 * con los usuarios en la base de datos, específicamente:
 * 
 * - Validar el inicio de sesión (login)
 * - Registrar nuevos usuarios
 * 
 * Utiliza JDBC para la conexión con la base de datos.
 * 
 * Autores:( Ivan Galindo Galindo e George Ortega Leon)
 */
public class LoginDAO {

    // Objetos necesarios para la conexión y consultas SQL
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    // Instancia de la clase Conexion
    Conexion cn = new Conexion();

    /**
     * Método para validar el inicio de sesión de un usuario.
     * 
     * Busca en la base de datos un usuario que coincida con
     * el correo y contraseña proporcionados.
     * 
     * @param correo Correo del usuario
     * @param pass Contraseña del usuario
     * @return Objeto login con los datos del usuario si existe,
     *         o vacío si no se encontró coincidencia
     */
    public login log(String correo, String pass) {

        // Se crea un objeto login para almacenar los datos
        login l = new login();

        // Consulta SQL con parámetros
        String sql = "SELECT * FROM usuarios WHERE CORREO = ? AND pass = ? ";

        try {
            // Se obtiene la conexión
            con = cn.getConecction();

            // Se prepara la consulta
            ps = con.prepareStatement(sql);

            // Se asignan los parámetros
            ps.setString(1, correo);
            ps.setString(2, pass);

            // Se ejecuta la consulta
            rs = ps.executeQuery();

            // Si existe un resultado, se llenan los datos
            if (rs.next()) {
                l.setId(rs.getInt("Id"));
                l.setNombre(rs.getString("Nombre"));
                l.setCorreo(rs.getString("Correo"));
                l.setPass(rs.getString("Pass"));
                l.setTelefono(rs.getString("Telefono"));
                l.setRol(rs.getString("rol"));
            }

        } catch (SQLException e) {
            System.out.println("Error en login: " + e.toString());
        }

        // Retorna el objeto login (con datos o vacío)
        return l;
    }

    /**
     * Método para registrar un nuevo usuario en la base de datos.
     * 
     * Inserta un nuevo registro en la tabla usuarios.
     * 
     * @param reg Objeto login con los datos del usuario
     * @return true si se registró correctamente, false si ocurrió un error
     */
    public boolean Registrar(login reg) {

        // Consulta SQL para insertar usuario
        String sql = "INSERT INTO usuarios (nombre, correo, pass, telefono, rol) VALUES (?,?,?,?,?)";

        try {
            // Se obtiene la conexión
            con = cn.getConecction();

            // Se prepara la consulta
            ps = con.prepareStatement(sql);

            // Se asignan los valores desde el objeto
            ps.setString(1, reg.getNombre());
            ps.setString(2, reg.getCorreo());
            ps.setString(3, reg.getPass());
            ps.setString(4, reg.getTelefono());
            ps.setString(5, reg.getRol());

            // Se ejecuta la inserción
            ps.execute();

            return true;

        } catch (SQLException e) {
            System.out.println("Error al registrar: " + e.toString());
            return false;
        }
    }
}