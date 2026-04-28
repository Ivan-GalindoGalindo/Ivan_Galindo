package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase encargada de establecer la conexión con la base de datos.
 * Utiliza el driver JDBC para conectarse a MariaDB.
 * 
 * * Autores: (Ivan Galindo Galindio e George Ortega Leon )
 */
public class Conexion {    

    // Variable de tipo Connection que almacenará la conexión activa
    Connection con;

    /**
     * Método que establece y retorna una conexión a la base de datos.
     * 
     * @return Connection   Objeto de conexión si se realiza correctamente,
     *                      null en caso de error.
     */
    public Connection getConecction(){
        try{
            // URL de conexión:
            // jdbc:mariadb://host:puerto/nombre_base_datos
            String myBD = "jdbc:mariadb://localhost:3307/sistemaventas";

            // Se establece la conexión con usuario y contraseña
            con = DriverManager.getConnection(myBD, "root", "Galindo26");

            // Retorna la conexión si fue exitosa
            return con;

        } catch (SQLException e){
            // En caso de error, se imprime el mensaje en consola
            System.out.println("Error de conexión: " + e.toString());
        }

        // Si falla la conexión, retorna null
        return null;
    }
}





