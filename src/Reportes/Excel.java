package Reportes;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import modelo.Conexion;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Clase Excel
 * 
 * Se encarga de generar un reporte en formato Excel (.xlsx)
 * con la información de los productos registrados en la base de datos.
 * 
 * Utiliza la librería Apache POI para crear el archivo, aplicar estilos,
 * insertar el logo del sistema y mostrar los datos en una hoja de cálculo.
 */
public class Excel {

    /**
     * Genera un reporte de productos en Excel.
     * 
     * El archivo se crea en la carpeta Descargas del usuario
     * con el nombre productos.xlsx y posteriormente se abre automáticamente.
     */
    public static void reporte() {

        // Se crea un nuevo libro de Excel
        Workbook book = new XSSFWorkbook();

        // Se crea una hoja llamada Productos
        Sheet sheet = book.createSheet("Productos");

        try {
            // Se carga el logo desde la carpeta src/img
            InputStream is = new FileInputStream("src/img/logo.png");

            // Se convierte la imagen a bytes
            byte[] bytes = IOUtils.toByteArray(is);

            // Se agrega la imagen al libro de Excel
            int imgIndex = book.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            is.close();

            // Objetos necesarios para insertar la imagen en la hoja
            CreationHelper help = book.getCreationHelper();
            Drawing draw = sheet.createDrawingPatriarch();

            // Se define la posición del logo dentro de la hoja
            ClientAnchor anchor = help.createClientAnchor();
            anchor.setCol1(0);
            anchor.setRow1(1);

            // Se dibuja la imagen y se ajusta su tamaño
            Picture pict = draw.createPicture(anchor, imgIndex);
            pict.resize(1, 3);

            // Estilo para el título del reporte
            CellStyle tituloEstilo = book.createCellStyle();
            tituloEstilo.setAlignment(HorizontalAlignment.CENTER);
            tituloEstilo.setVerticalAlignment(VerticalAlignment.CENTER);

            // Fuente del título
            Font fuenteTitulo = book.createFont();
            fuenteTitulo.setFontName("Arial");
            fuenteTitulo.setBold(true);
            fuenteTitulo.setFontHeightInPoints((short) 14);
            tituloEstilo.setFont(fuenteTitulo);

            // Se crea la fila y celda del título
            Row filaTitulo = sheet.createRow(1);
            Cell celdaTitulo = filaTitulo.createCell(1);
            celdaTitulo.setCellStyle(tituloEstilo);
            celdaTitulo.setCellValue("Reporte de Productos");

            // Se combinan celdas para centrar el título
            sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 3));

            // Encabezados de la tabla
            String[] cabecera = new String[]{"Código", "Nombre", "Precio", "Existencia"};

            // Estilo para los encabezados
            CellStyle headerStyle = book.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            // Fuente de los encabezados
            Font font = book.createFont();
            font.setFontName("Arial");
            font.setBold(true);
            font.setColor(IndexedColors.WHITE.getIndex());
            font.setFontHeightInPoints((short) 12);
            headerStyle.setFont(font);

            // Se crea la fila de encabezados
            Row filaEncabezados = sheet.createRow(4);

            // Se agregan los encabezados a la hoja
            for (int i = 0; i < cabecera.length; i++) {
                Cell celdaEncabezado = filaEncabezados.createCell(i);
                celdaEncabezado.setCellStyle(headerStyle);
                celdaEncabezado.setCellValue(cabecera[i]);
            }

            // Se establece la conexión con la base de datos
            Conexion con = new Conexion();
            PreparedStatement ps;
            ResultSet rs;
            Connection conn = con.getConecction();

            // Fila donde comenzarán los datos
            int numFilaDatos = 5;

            // Estilo para las celdas de datos
            CellStyle datosEstilo = book.createCellStyle();
            datosEstilo.setBorderBottom(BorderStyle.THIN);
            datosEstilo.setBorderLeft(BorderStyle.THIN);
            datosEstilo.setBorderRight(BorderStyle.THIN);

            // Consulta SQL para obtener los productos
            ps = conn.prepareStatement("SELECT codigo, nombre, precio, stock FROM productos");
            rs = ps.executeQuery();

            // Número de columnas obtenidas de la consulta
            int numCol = rs.getMetaData().getColumnCount();

            // Se recorren los resultados de la consulta
            while (rs.next()) {
                Row filaDatos = sheet.createRow(numFilaDatos);

                // Se llenan las columnas de cada fila
                for (int a = 0; a < numCol; a++) {
                    Cell celdaDatos = filaDatos.createCell(a);
                    celdaDatos.setCellStyle(datosEstilo);
                    celdaDatos.setCellValue(rs.getString(a + 1));
                }

                numFilaDatos++;
            }

            // Se ajusta automáticamente el tamaño de las columnas
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);

            // Se establece el zoom de la hoja
            sheet.setZoom(150);

            // Nombre del archivo generado
            String fileName = "productos";

            // Ruta de la carpeta Descargas del usuario
            String home = System.getProperty("user.home");
            File file = new File(home + "/Downloads/" + fileName + ".xlsx");

            // Se escribe el archivo Excel
            FileOutputStream fileOut = new FileOutputStream(file);
            book.write(fileOut);
            fileOut.close();

            // Se abre automáticamente el archivo generado
            Desktop.getDesktop().open(file);

            // Mensaje de confirmación
            JOptionPane.showMessageDialog(null, "Reporte Generado");

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Excel.class.getName()).log(Level.SEVERE, null, ex);

        } catch (IOException | SQLException ex) {
            Logger.getLogger(Excel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}