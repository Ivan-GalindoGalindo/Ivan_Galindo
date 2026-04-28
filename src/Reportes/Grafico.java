package Reportes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelo.Conexion;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/**
 * Clase Grafico
 * 
 * Se encarga de generar un gráfico tipo pastel 
 * que representa el total de ventas realizadas en una fecha específica.
 * 
 * Utiliza la librería JFreeChart para la creación y visualización del gráfico.
 * 
 * Autor: Ivan G
 */
public class Grafico {

    /**
     * Genera un gráfico de ventas por día.
     * 
     * Consulta la base de datos para obtener los totales de ventas
     * en una fecha determinada y los muestra en un gráfico tipo pastel.
     * 
     * @param fecha Fecha de las ventas a consultar (formato: YYYY-MM-DD)
     */
    public static void Graficar(String fecha) {

        Connection con;
        Conexion cn = new Conexion();
        PreparedStatement ps;
        ResultSet rs;

        try {
            // Consulta SQL para obtener el total de ventas en una fecha específica
            String sql = "SELECT total FROM ventas WHERE fecha = ?";

            con = cn.getConecction();
            ps = con.prepareStatement(sql);
            ps.setString(1, fecha);

            rs = ps.executeQuery();

            // Dataset para el gráfico
            DefaultPieDataset dataset = new DefaultPieDataset();

            // Se agregan los datos al dataset
            while (rs.next()) {
                dataset.setValue(
                    rs.getString("total"), // etiqueta
                    rs.getDouble("total") // valor
                );
            }

            // Se crea el gráfico tipo pastel
            JFreeChart jf = ChartFactory.createPieChart(
                    "Reporte de Venta", // título
                    dataset
            );

            // Se muestra el gráfico en una ventana
            ChartFrame f = new ChartFrame("Total de Ventas por día", jf);
            f.setSize(1000, 500);
            f.setLocationRelativeTo(null);
            f.setVisible(true);

        } catch (SQLException e) {
            System.out.println("Error al generar gráfico: " + e.toString());
        }
    }
}