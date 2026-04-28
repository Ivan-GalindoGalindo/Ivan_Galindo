package Vista;

import Modelo.Proveedor;
import Reportes.Excel;
import Reportes.Grafico;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.Config;
import modelo.Detalle;
import modelo.Eventos;
import modelo.Productos;
import modelo.ProductosDAO;
import modelo.ProveedorDAO;
import modelo.Venta;
import modelo.VentaDAO;
import modelo.login;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author Ivan G
 */
public class Sistema extends javax.swing.JFrame {
    /**
 * Cantidad de dinero que el cliente entrega para pagar la compra.
 * 
 * Se utiliza para calcular el cambio en una venta.
 */
private double pagoCliente = 0;

/**
 * Cantidad de dinero que se le devuelve al cliente como cambio.
 * 
 * Se calcula restando el total de la venta al pago realizado por el cliente.
 */
private double cambioCliente = 0;
    
   /**
 * Fecha actual del sistema utilizada para registrar ventas.
 */
Date fechaVenta = new Date();

/**
 * Fecha formateada en formato día/mes/año.
 * Ejemplo: 27/04/2026
 */
String fechaActual = new SimpleDateFormat("dd/MM/yyyy").format(fechaVenta);

/**
 * Objeto que representa un proveedor.
 */
Proveedor pr = new Proveedor();

/**
 * DAO encargado de las operaciones de proveedores en la base de datos.
 */
ProveedorDAO PrDAO = new ProveedorDAO();

/**
 * Objeto que representa un producto.
 */
Productos pro = new Productos();

/**
 * DAO encargado de las operaciones de productos.
 */
ProductosDAO prodao = new ProductosDAO();

/**
 * Objeto que representa una venta.
 */
Venta v = new Venta();

/**
 * DAO encargado de gestionar las ventas en la base de datos.
 */
VentaDAO Vdao = new VentaDAO();

/**
 * Objeto que representa el detalle de una venta (productos vendidos).
 */
Detalle Dv = new Detalle();

/**
 * Objeto de configuración del sistema (datos del negocio).
 */
Config conf = new Config();

/**
 * Clase auxiliar para validar eventos en campos de texto.
 */
Eventos event = new Eventos();

/**
 * Objeto que representa al usuario que inició sesión.
 */
login Login = new login();

/**
 * Modelo de tabla principal para mostrar datos (ej. productos, ventas).
 */
DefaultTableModel modelo = new DefaultTableModel();

/**
 * Modelo temporal de tabla (usado para filtros o búsquedas).
 */
DefaultTableModel tmp = new DefaultTableModel(); 
   

        /**
     * Índice o número de ítem dentro de una tabla o lista.
     * 
     * Se utiliza comúnmente para identificar la fila seleccionada
     * o recorrer elementos en una tabla (por ejemplo, productos en el POS).
     */
    int item;

    /**
     * Total acumulado a pagar en una venta.
     * 
     * Almacena la suma de todos los productos agregados al carrito.
     * Se utiliza para mostrar el monto final al cliente.
     */
    double Totalpagar = 0.00;
    
    
    /**
    * Constructor de la clase Sistema.
    * 
    * Inicializa los componentes gráficos de la interfaz
    * y carga la configuración del sistema al iniciar.
    */
    public Sistema() {
        initComponents();
            
    // Carga los datos de configuración (nombre del negocio, dirección, etc.)
        ListarConfig();
        
    }
    
    
            /**
         * Constructor de la clase Sistema con usuario autenticado.
         * 
         * Inicializa la interfaz del sistema, carga configuraciones,
         * proveedores y establece permisos según el rol del usuario.
         * 
         * @param priv Objeto login que contiene la información del usuario activo
         */
        public Sistema(login priv) {

            // Inicializa todos los componentes gráficos
            initComponents();

            // Centra la ventana en la pantalla
            this.setLocationRelativeTo(null);

            // Oculta campos internos (IDs) que no deben mostrarse al usuario
            txtIdVenta.setVisible(false);
            txtIdPro.setVisible(false);
            txtIdProveedor.setVisible(false);
            txtIdConfig.setVisible(false);

            // Activa autocompletado en el combo de proveedores
            AutoCompleteDecorator.decorate(cbxProveedorPro);

            // Carga proveedores en el comboBox
            prodao.ConsultarProveedor(cbxProveedorPro);

            // Carga la configuración del sistema (datos del negocio)
            ListarConfig();

            /**
             * Control de acceso según el rol del usuario
             */
            if (priv.getRol().equals("Asistente")) {

                // Desactiva opciones restringidas para el asistente
                // btnProductos.setEnabled(false);
                btnProveedor.setEnabled(false);
                // btnRegistrar.setEnabled(false);
                btnConfig.setEnabled(false);

                // Muestra el nombre del usuario en la interfaz
                LabelVendedor.setText(priv.getNombre());

            } else {

                // Si es administrador, solo muestra su nombre
                LabelVendedor.setText(priv.getNombre());
            }
        }
    

            /**
          * Lista todos los proveedores en la tabla TableProveedor.
          * 
          * Obtiene los datos desde la base de datos mediante el DAO,
          * los recorre y los agrega fila por fila al modelo de la tabla.
          */
         public void ListarProveedor() {

             // Obtiene la lista de proveedores desde la base de datos
             List<Proveedor> ListarPr = PrDAO.ListarProveedor();

             // Obtiene el modelo de la tabla
             modelo = (DefaultTableModel) TableProveedor.getModel();

             // Arreglo para almacenar los datos de cada fila
             Object[] ob = new Object[6];

             // Recorre la lista de proveedores
             for (int i = 0; i < ListarPr.size(); i++) {

                 ob[0] = ListarPr.get(i).getId();
                 ob[1] = ListarPr.get(i).getDni();
                 ob[2] = ListarPr.get(i).getNombre();
                 ob[3] = ListarPr.get(i).getTelefono();
                 ob[4] = ListarPr.get(i).getDireccion();
                 ob[5] = ListarPr.get(i).getFecha();

                 // Agrega la fila al modelo
                 modelo.addRow(ob);
             }

             // Asigna el modelo actualizado a la tabla
             TableProveedor.setModel(modelo);
         }

        /**
         * Lista todos los productos en la tabla TableProducto.
         * 
         * Consulta la base de datos mediante el DAO y carga los datos
         * en la tabla de productos dentro de la interfaz.
         */
        public void ListarProductos() {

            // Obtiene la lista de productos desde la base de datos
            List<Productos> ListarPro = prodao.ListarProductos();

            // Obtiene el modelo de la tabla
            modelo = (DefaultTableModel) TableProducto.getModel();

            // Arreglo para almacenar los datos de cada fila
            Object[] ob = new Object[6];

            // Recorre la lista de productos
            for (int i = 0; i < ListarPro.size(); i++) {

                ob[0] = ListarPro.get(i).getId();
                ob[1] = ListarPro.get(i).getCodigo();
                ob[2] = ListarPro.get(i).getNombre();
                ob[3] = ListarPro.get(i).getProveedor();
                ob[4] = ListarPro.get(i).getStock();
                ob[5] = ListarPro.get(i).getPrecio();

                // Agrega la fila al modelo
                modelo.addRow(ob);
            }

            // Asigna el modelo actualizado a la tabla
            TableProducto.setModel(modelo);
        }
    
    
  /**
 * Busca productos en la tabla según el texto ingresado.
 * 
 * Obtiene el valor escrito en el campo txtBuscar, limpia la tabla
 * y muestra únicamente los productos que coinciden con el código
 * o nombre buscado.
 */         
        private void BuscarProductoTabla() {

            // Obtiene el texto escrito en el campo de búsqueda
            String buscar = txtBuscar.getText().trim();

            // Obtiene el modelo de la tabla y limpia las filas actuales
            DefaultTableModel modelo = (DefaultTableModel) TableProducto.getModel();
            modelo.setRowCount(0);

            // Consulta los productos que coinciden con la búsqueda
            List<Productos> lista = prodao.BuscarProductos(buscar);

            // Arreglo para almacenar los datos de cada producto
            Object[] fila = new Object[6];

            // Recorre la lista de productos encontrados
            for (Productos p : lista) {
                fila[0] = p.getId();
                fila[1] = p.getCodigo();
                fila[2] = p.getNombre();
                fila[3] = p.getProveedor();
                fila[4] = p.getStock();
                fila[5] = p.getPrecio();

                // Agrega el producto a la tabla
                modelo.addRow(fila);
            }

            // Actualiza la tabla con los datos encontrados
            TableProducto.setModel(modelo);
        }

        
/**
 * Lista los datos de configuración del sistema.
 * 
 * Obtiene los datos guardados en la tabla config mediante el DAO
 * y los muestra en los campos correspondientes de la interfaz.
 */
public void ListarConfig() {

    // Consulta los datos de configuración del sistema
    conf = prodao.BuscarDatos();

    // Muestra los datos obtenidos en los campos de texto
    txtIdConfig.setText("" + conf.getId());
    txtRucConfig.setText("" + conf.getRuc());
    txtNombreConfig.setText("" + conf.getNombre());
    txtTelefonoConfig.setText("" + conf.getTelefono());
    txtDireccionConfig.setText("" + conf.getDireccion());
}
   

        /**
      * Carga y muestra las ventas en la tabla TableVentas.
      * 
      * Consulta la lista de ventas desde la base de datos,
      * limpia la tabla y agrega cada venta como una nueva fila.
      * 
      * Se muestra:
      * - ID de la venta
      * - Número de ticket
      * - Vendedor
      * - Total de la venta
      */
     private void TablaVentas() {

         // Obtiene el modelo de la tabla y limpia su contenido
         DefaultTableModel modelo = (DefaultTableModel) TableVentas.getModel();
         modelo.setRowCount(0);

         // Obtiene la lista de ventas desde el DAO
         List<Venta> lista = Vdao.Listarventas();

         // Arreglo para almacenar los datos de cada fila
         Object[] fila = new Object[4];

         // Recorre la lista de ventas
         for (Venta v : lista) {
             fila[0] = v.getId();
             fila[1] = "Ticket #" + v.getId(); // Formato visual del ticket
             fila[2] = v.getVendedor();
             fila[3] = v.getTotal();

             // Agrega la fila a la tabla
             modelo.addRow(fila);
         }

         // Actualiza la tabla con el modelo
         TableVentas.setModel(modelo);
     }

    /**
     * Limpia todas las filas de la tabla actual.
     * 
     * Recorre el modelo de la tabla y elimina cada fila una por una.
     */
    public void LimpiarTable() {

        for (int i = 0; i < modelo.getRowCount(); i++) {
            modelo.removeRow(i);
            i = i - 1; // Ajusta el índice al eliminar filas
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnNuevaVenta = new javax.swing.JButton();
        btnProveedor = new javax.swing.JButton();
        btnProductos = new javax.swing.JButton();
        btnVentas = new javax.swing.JButton();
        btnConfig = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        LabelVendedor = new javax.swing.JLabel();
        btnRegistrar = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnEliminarventa = new javax.swing.JButton();
        txtCodigoVenta = new javax.swing.JTextField();
        txtDescripcionVenta = new javax.swing.JTextField();
        txtCantidadVenta = new javax.swing.JTextField();
        txtPrecioVenta = new javax.swing.JTextField();
        txtStockDisponible = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableVenta = new javax.swing.JTable();
        btnGenerarVenta = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        LabelTotal = new javax.swing.JLabel();
        txtIdPro = new javax.swing.JTextField();
        btnGraficar = new javax.swing.JButton();
        Midate = new com.toedter.calendar.JDateChooser();
        jLabel11 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtCodigoPro = new javax.swing.JTextField();
        txtDesPro = new javax.swing.JTextField();
        txtCantPro = new javax.swing.JTextField();
        txtPrecioPro = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        TableProducto = new javax.swing.JTable();
        cbxProveedorPro = new javax.swing.JComboBox<>();
        btnGuardarpro = new javax.swing.JButton();
        btnEditarpro = new javax.swing.JButton();
        btnEliminarPro = new javax.swing.JButton();
        btnNuevopro = new javax.swing.JButton();
        btnExcelPro = new javax.swing.JButton();
        txtIdpro = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtBuscarPro = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        TableVentas = new javax.swing.JTable();
        btnPdfVentas = new javax.swing.JButton();
        txtIdVenta = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtRucProveedor = new javax.swing.JTextField();
        txtNombreProveedor = new javax.swing.JTextField();
        txtTelefonoProveedor = new javax.swing.JTextField();
        txtDireccionProveedor = new javax.swing.JTextField();
        txtFechaProveedor = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        TableProveedor = new javax.swing.JTable();
        btnguardarProveedor = new javax.swing.JButton();
        btnEliminarProveedor = new javax.swing.JButton();
        btnEditarProveedor = new javax.swing.JButton();
        btnNuevoProveedor = new javax.swing.JButton();
        txtIdProveedor = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        txtRucConfig = new javax.swing.JTextField();
        txtNombreConfig = new javax.swing.JTextField();
        txtTelefonoConfig = new javax.swing.JTextField();
        txtDireccionConfig = new javax.swing.JTextField();
        btnActualizarConfig = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        txtIdConfig = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));

        btnNuevaVenta.setBackground(new java.awt.Color(255, 255, 255));
        btnNuevaVenta.setForeground(new java.awt.Color(0, 0, 0));
        btnNuevaVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/Nventa.png"))); // NOI18N
        btnNuevaVenta.setText("Nueva Venta");
        btnNuevaVenta.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnNuevaVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevaVentaActionPerformed(evt);
            }
        });

        btnProveedor.setBackground(new java.awt.Color(255, 255, 255));
        btnProveedor.setForeground(new java.awt.Color(0, 0, 0));
        btnProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/proveedor.png"))); // NOI18N
        btnProveedor.setText("Proveedor");
        btnProveedor.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProveedorActionPerformed(evt);
            }
        });

        btnProductos.setBackground(new java.awt.Color(255, 255, 255));
        btnProductos.setForeground(new java.awt.Color(0, 0, 0));
        btnProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/producto.png"))); // NOI18N
        btnProductos.setText("Productos");
        btnProductos.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductosActionPerformed(evt);
            }
        });

        btnVentas.setBackground(new java.awt.Color(255, 255, 255));
        btnVentas.setForeground(new java.awt.Color(0, 0, 0));
        btnVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/compras.png"))); // NOI18N
        btnVentas.setText("Ventas");
        btnVentas.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentasActionPerformed(evt);
            }
        });

        btnConfig.setBackground(new java.awt.Color(255, 255, 255));
        btnConfig.setForeground(new java.awt.Color(0, 0, 0));
        btnConfig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/config.png"))); // NOI18N
        btnConfig.setText("Config");
        btnConfig.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfigActionPerformed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/20251212_190316.png"))); // NOI18N

        LabelVendedor.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        LabelVendedor.setText("Inge en Sistema");

        btnRegistrar.setBackground(new java.awt.Color(255, 255, 255));
        btnRegistrar.setForeground(new java.awt.Color(0, 0, 0));
        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/Clientes.png"))); // NOI18N
        btnRegistrar.setText("Usuarios");
        btnRegistrar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnNuevaVenta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnProductos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnVentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnConfig, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
            .addComponent(btnRegistrar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(LabelVendedor)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(btnProveedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LabelVendedor)
                .addGap(18, 18, 18)
                .addComponent(btnNuevaVenta)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnConfig, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(93, 93, 93))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 190, 630));

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Codigo");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("nombre");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Cantidad");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Precio");

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Stock Disponible");

        btnEliminarventa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/eliminar.png"))); // NOI18N
        btnEliminarventa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarventaActionPerformed(evt);
            }
        });

        txtCodigoVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoVentaActionPerformed(evt);
            }
        });
        txtCodigoVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodigoVentaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodigoVentaKeyTyped(evt);
            }
        });

        txtDescripcionVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDescripcionVentaKeyTyped(evt);
            }
        });

        txtCantidadVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCantidadVentaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantidadVentaKeyTyped(evt);
            }
        });

        txtPrecioVenta.setEditable(false);

        txtStockDisponible.setEditable(false);

        TableVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CODIGO", "DESCRIPCION", "CANTIDAD", "PRECIO", "TOTAL"
            }
        ));
        TableVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableVentaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TableVenta);
        if (TableVenta.getColumnModel().getColumnCount() > 0) {
            TableVenta.getColumnModel().getColumn(0).setPreferredWidth(30);
            TableVenta.getColumnModel().getColumn(1).setPreferredWidth(100);
            TableVenta.getColumnModel().getColumn(2).setPreferredWidth(30);
            TableVenta.getColumnModel().getColumn(3).setPreferredWidth(30);
            TableVenta.getColumnModel().getColumn(4).setPreferredWidth(40);
        }

        btnGenerarVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/print.png"))); // NOI18N
        btnGenerarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarVentaActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/money.png"))); // NOI18N
        jLabel10.setText("TOTAL A PAGAR");

        LabelTotal.setText("-----");

        btnGraficar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/torta.png"))); // NOI18N
        btnGraficar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGraficarActionPerformed(evt);
            }
        });

        jLabel11.setText("Seleccionar:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel3))
                            .addComponent(txtCodigoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(txtDescripcionVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(36, 36, 36)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtCantidadVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnEliminarventa, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(16, 16, 16))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(100, 100, 100)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(txtPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(txtStockDisponible, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtIdPro, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(btnGraficar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(Midate, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel11))))
                                .addContainerGap())))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btnGenerarVenta)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel10)
                                .addGap(30, 30, 30)
                                .addComponent(LabelTotal)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(LabelTotal)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnGraficar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Midate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnEliminarventa, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7))
                                .addGap(12, 12, 12)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtCodigoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtDescripcionVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtCantidadVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtStockDisponible, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtIdPro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                        .addComponent(btnGenerarVenta)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Nueva_Venta", jPanel2);

        jLabel22.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 0, 0));
        jLabel22.setText("CODIGO:");

        jLabel23.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 0, 0));
        jLabel23.setText("DESCRIPCION:");

        jLabel24.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(0, 0, 0));
        jLabel24.setText("CANTIDAD:");

        jLabel25.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 0, 0));
        jLabel25.setText("PRECIO:");

        jLabel26.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(0, 0, 0));
        jLabel26.setText("PROVEEDOR:");

        txtCodigoPro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodigoProKeyTyped(evt);
            }
        });

        txtDesPro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDesProKeyTyped(evt);
            }
        });

        txtCantPro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantProKeyTyped(evt);
            }
        });

        txtPrecioPro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioProKeyTyped(evt);
            }
        });

        TableProducto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "CODIGO", "NOMBRE", "PROVEEDOR", "STOCK", "PRECIO "
            }
        ));
        TableProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableProductoMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(TableProducto);
        if (TableProducto.getColumnModel().getColumnCount() > 0) {
            TableProducto.getColumnModel().getColumn(0).setPreferredWidth(20);
            TableProducto.getColumnModel().getColumn(1).setPreferredWidth(50);
            TableProducto.getColumnModel().getColumn(2).setPreferredWidth(100);
            TableProducto.getColumnModel().getColumn(3).setPreferredWidth(60);
            TableProducto.getColumnModel().getColumn(4).setPreferredWidth(40);
            TableProducto.getColumnModel().getColumn(5).setPreferredWidth(50);
        }

        cbxProveedorPro.setEditable(true);
        cbxProveedorPro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxProveedorProActionPerformed(evt);
            }
        });

        btnGuardarpro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/GuardarTodo.png"))); // NOI18N
        btnGuardarpro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarproActionPerformed(evt);
            }
        });

        btnEditarpro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/Actualizar (2).png"))); // NOI18N
        btnEditarpro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarproActionPerformed(evt);
            }
        });

        btnEliminarPro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/eliminar.png"))); // NOI18N
        btnEliminarPro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProActionPerformed(evt);
            }
        });

        btnNuevopro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/nuevo.png"))); // NOI18N
        btnNuevopro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoproActionPerformed(evt);
            }
        });

        btnExcelPro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/excel.png"))); // NOI18N
        btnExcelPro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelProActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("INVENTARIO");

        txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("INGRESA EL NOMBRE  0 CODIGO");

        txtBuscarPro.setText("BUSCAR");
        txtBuscarPro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarProActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23)
                            .addComponent(jLabel22)
                            .addComponent(jLabel24)
                            .addComponent(jLabel25)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btnNuevopro)
                                .addComponent(jLabel26)))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbxProveedorPro, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtPrecioPro, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtCantPro, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(txtCodigoPro, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtDesPro, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtIdpro, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(73, 73, 73)
                                .addComponent(btnExcelPro)))
                        .addGap(27, 27, 27))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(btnGuardarpro)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEditarpro)
                        .addGap(67, 67, 67)
                        .addComponent(btnEliminarPro)
                        .addGap(59, 59, 59)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel12)
                        .addGap(100, 100, 100)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtBuscarPro)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(txtCodigoPro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIdpro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(txtDesPro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(txtCantPro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(txtPrecioPro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(cbxProveedorPro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEliminarPro, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnEditarpro, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGuardarpro)))
                .addGap(32, 32, 32)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNuevopro)
                    .addComponent(btnExcelPro))
                .addGap(24, 24, 24))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBuscarPro))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Productos", jPanel5);

        TableVentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "TICKET", "VENDEDOR", "TOTAL"
            }
        ));
        TableVentas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableVentasMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                TableVentasMousePressed(evt);
            }
        });
        jScrollPane5.setViewportView(TableVentas);
        if (TableVentas.getColumnModel().getColumnCount() > 0) {
            TableVentas.getColumnModel().getColumn(0).setPreferredWidth(20);
            TableVentas.getColumnModel().getColumn(1).setPreferredWidth(60);
            TableVentas.getColumnModel().getColumn(2).setPreferredWidth(60);
            TableVentas.getColumnModel().getColumn(3).setPreferredWidth(60);
        }

        btnPdfVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/pdf.png"))); // NOI18N
        btnPdfVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPdfVentasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(0, 75, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(btnPdfVentas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtIdVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(757, 757, 757))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 839, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(78, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnPdfVentas)
                    .addComponent(txtIdVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Ventas", jPanel6);

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 0, 0));
        jLabel17.setText("CLAVE");

        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 0, 0));
        jLabel18.setText("NOMBRE:");

        jLabel19.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 0, 0));
        jLabel19.setText("TELEFONO:");

        jLabel20.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 0, 0));
        jLabel20.setText("DIRECCION:");

        jLabel21.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 0, 0));
        jLabel21.setText("FECHA");

        txtRucProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRucProveedorActionPerformed(evt);
            }
        });
        txtRucProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRucProveedorKeyTyped(evt);
            }
        });

        txtNombreProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreProveedorKeyTyped(evt);
            }
        });

        txtTelefonoProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelefonoProveedorKeyTyped(evt);
            }
        });

        txtDireccionProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDireccionProveedorActionPerformed(evt);
            }
        });
        txtDireccionProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDireccionProveedorKeyTyped(evt);
            }
        });

        txtFechaProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFechaProveedorKeyTyped(evt);
            }
        });

        TableProveedor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "RUC", "NOMBRE", "TELEFONO", "DIRECCION", "FECHA"
            }
        ));
        TableProveedor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableProveedorMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(TableProveedor);
        if (TableProveedor.getColumnModel().getColumnCount() > 0) {
            TableProveedor.getColumnModel().getColumn(0).setPreferredWidth(20);
            TableProveedor.getColumnModel().getColumn(1).setPreferredWidth(40);
            TableProveedor.getColumnModel().getColumn(2).setPreferredWidth(100);
            TableProveedor.getColumnModel().getColumn(3).setPreferredWidth(50);
            TableProveedor.getColumnModel().getColumn(4).setPreferredWidth(80);
            TableProveedor.getColumnModel().getColumn(5).setPreferredWidth(70);
        }

        btnguardarProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/GuardarTodo.png"))); // NOI18N
        btnguardarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnguardarProveedorActionPerformed(evt);
            }
        });

        btnEliminarProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/eliminar.png"))); // NOI18N
        btnEliminarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProveedorActionPerformed(evt);
            }
        });

        btnEditarProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/Actualizar (2).png"))); // NOI18N
        btnEditarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarProveedorActionPerformed(evt);
            }
        });

        btnNuevoProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/nuevo.png"))); // NOI18N
        btnNuevoProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoProveedorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel20))
                                .addGap(168, 168, 168))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addGap(18, 45, Short.MAX_VALUE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtTelefonoProveedor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                                        .addComponent(txtFechaProveedor, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(txtDireccionProveedor, javax.swing.GroupLayout.Alignment.TRAILING))
                                    .addComponent(txtNombreProveedor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtRucProveedor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(btnEliminarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnEditarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(btnguardarProveedor)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnNuevoProveedor)))
                        .addGap(68, 68, 68)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 618, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtIdProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txtRucProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIdProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(txtNombreProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(txtTelefonoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(23, 23, 23)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(txtDireccionProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(txtFechaProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnguardarProveedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnNuevoProveedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(36, 36, 36)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEditarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEliminarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(45, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Proveedor", jPanel4);

        jLabel27.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(0, 0, 0));
        jLabel27.setText("CLAVE");

        jLabel28.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(0, 0, 0));
        jLabel28.setText("NOMBRE");

        jLabel29.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(0, 0, 0));
        jLabel29.setText("TELEFONO");

        jLabel30.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(0, 0, 0));
        jLabel30.setText("DIRECCION");

        txtRucConfig.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRucConfigKeyTyped(evt);
            }
        });

        txtNombreConfig.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreConfigKeyTyped(evt);
            }
        });

        txtTelefonoConfig.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelefonoConfigKeyTyped(evt);
            }
        });

        txtDireccionConfig.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDireccionConfigKeyTyped(evt);
            }
        });

        btnActualizarConfig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/Actualizar (2).png"))); // NOI18N
        btnActualizarConfig.setText("ACTUALIZAR");
        btnActualizarConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarConfigActionPerformed(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(0, 0, 0));
        jLabel32.setText("DATOS DE LA EMPRESA");

        txtIdConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdConfigActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtRucConfig, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel30)
                                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txtIdConfig, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                        .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGap(92, 92, 92)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtNombreConfig, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel28))
                                .addGap(95, 95, 95)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTelefonoConfig, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel29)))
                            .addComponent(txtDireccionConfig, javax.swing.GroupLayout.PREFERRED_SIZE, 715, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(350, 350, 350)
                        .addComponent(btnActualizarConfig)))
                .addGap(0, 162, Short.MAX_VALUE))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(258, 258, 258)
                .addComponent(jLabel32)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(87, Short.MAX_VALUE)
                .addComponent(jLabel32)
                .addGap(1, 1, 1)
                .addComponent(txtIdConfig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28)
                    .addComponent(jLabel29))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRucConfig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreConfig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTelefonoConfig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel30)
                .addGap(14, 14, 14)
                .addComponent(txtDireccionConfig, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(btnActualizarConfig)
                .addGap(68, 68, 68))
        );

        jTabbedPane1.addTab("Config", jPanel7);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 150, 920, 480));

        jPanel8.setForeground(new java.awt.Color(102, 102, 102));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Algerian", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("REFACCIONARIA AUTOMOTRIZ ");
        jPanel8.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 610, 60));

        jLabel33.setFont(new java.awt.Font("Algerian", 1, 48)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(0, 0, 0));
        jLabel33.setText("\"EL AGUILA\"");
        jPanel8.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 70, -1, -1));

        getContentPane().add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 920, 150));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
 * Evento del botón Configuración.
 * 
 * Cambia la vista al panel de configuración del sistema.
 */
    private void btnConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfigActionPerformed
        jTabbedPane1.setSelectedIndex(4);
    }//GEN-LAST:event_btnConfigActionPerformed
/**
 * Evento del botón Proveedor.
 * 
 * Limpia la tabla, carga la lista de proveedores
 * y muestra la pestaña correspondiente.
 */
    private void btnProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProveedorActionPerformed
        
        LimpiarTable();
        ListarProveedor();
       jTabbedPane1.setSelectedIndex(3);
    }//GEN-LAST:event_btnProveedorActionPerformed
/**
 * Evento del botón Productos.
 * 
 * Limpia la tabla, carga la lista de productos
 * y cambia a la pestaña de productos.
 */
    private void btnProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductosActionPerformed
        // TODO add your handling code here:
        LimpiarTable();
        ListarProductos();
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_btnProductosActionPerformed
/**
 * Evento del botón Nueva Venta.
 * 
 * Cambia la vista al panel principal de ventas
 * para registrar una nueva venta.
 */
    private void btnNuevaVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevaVentaActionPerformed
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_btnNuevaVentaActionPerformed
/**
 * Evento del botón Ventas.
 * 
 * Muestra el historial de ventas, limpia la tabla
 * y carga los registros existentes.
 */
    private void btnVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVentasActionPerformed
        jTabbedPane1.setSelectedIndex(2);
        LimpiarTable();
        TablaVentas();
    }//GEN-LAST:event_btnVentasActionPerformed
/**
 * Evento del botón Registrar Usuario.
 * 
 * Abre la ventana de registro para crear un nuevo usuario.
 */
    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        Registro reg = new Registro();
        reg.setVisible(true);
    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void txtIdConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdConfigActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdConfigActionPerformed

    private void btnActualizarConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarConfigActionPerformed
            /**
 * Valida y actualiza los datos de configuración de la empresa.
 * 
 * Verifica que todos los campos estén llenos antes de proceder.
 * Si la validación es correcta, actualiza los datos en la base
 * de datos y recarga la información en la interfaz.
 */
if (!txtRucConfig.getText().trim().isEmpty()
        && !txtNombreConfig.getText().trim().isEmpty()
        && !txtTelefonoConfig.getText().trim().isEmpty()
        && !txtDireccionConfig.getText().trim().isEmpty()
        && !txtIdConfig.getText().trim().isEmpty()) {

    // Asigna los valores de los campos al objeto configuración
    conf.setRuc(Integer.parseInt(txtRucConfig.getText()));
    conf.setNombre(txtNombreConfig.getText());
    conf.setTelefono(txtTelefonoConfig.getText());
    conf.setDireccion(txtDireccionConfig.getText());
    conf.setId(Integer.parseInt(txtIdConfig.getText()));

    // Actualiza los datos en la base de datos
    prodao.ModificarDatos(conf);

    // Mensaje de confirmación
    JOptionPane.showMessageDialog(null, "Datos de la empresa modificado");

    // Recarga los datos actualizados en la interfaz
    ListarConfig();

} else {
    // Mensaje de error si hay campos vacíos
    JOptionPane.showMessageDialog(null, "Los campos estan vacios");
}
    }//GEN-LAST:event_btnActualizarConfigActionPerformed
/**
 * Evento al escribir en el campo Dirección.
 * 
 * Permite únicamente letras, espacios y retroceso.
 */
    private void txtDireccionConfigKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionConfigKeyTyped
        event.textKeyPress(evt);
    }//GEN-LAST:event_txtDireccionConfigKeyTyped
/**
 * Evento al escribir en el campo Teléfono.
 * 
 * Permite únicamente números (0-9).
 */
    private void txtTelefonoConfigKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoConfigKeyTyped
        event.numberKeyPress(evt);
    }//GEN-LAST:event_txtTelefonoConfigKeyTyped
/**
 * Evento al escribir en el campo Nombre.
 * 
 * Permite únicamente letras, espacios y retroceso.
 */
    private void txtNombreConfigKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreConfigKeyTyped
        event.textKeyPress(evt);
    }//GEN-LAST:event_txtNombreConfigKeyTyped
/**
 * Evento al escribir en el campo RUC.
 * 
 * Permite únicamente números (0-9).
 */
    private void txtRucConfigKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRucConfigKeyTyped
        event.numberKeyPress(evt);
    }//GEN-LAST:event_txtRucConfigKeyTyped

    private void btnPdfVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPdfVentasActionPerformed
                                             
    /**
 * Busca y abre el archivo PDF del ticket correspondiente a una venta.
 * 
 * Obtiene el ID de la venta desde el campo txtIdVenta, busca el archivo
 * en la carpeta "src/pdf/" y lo abre si existe.
 * 
 * Si no encuentra el archivo o hay un error, muestra un mensaje al usuario.
 */
try {
    // Obtiene el ID de la venta desde el campo de texto
    int id = Integer.parseInt(txtIdVenta.getText().trim());

    // Define la carpeta donde se guardan los tickets
    File carpeta = new File("src/pdf/");
    File[] archivos = carpeta.listFiles();

    // Verifica si la carpeta existe o contiene archivos
    if (archivos == null) {
        JOptionPane.showMessageDialog(null, "No existe la carpeta de tickets");
        return;
    }

    // Busca el archivo que coincida con el formato del ticket
    for (File f : archivos) {
        if (f.getName().startsWith("ticket_" + id + "_") && f.getName().endsWith(".pdf")) {

            // Abre el archivo encontrado
            Desktop.getDesktop().open(f);
            return;
        }
    }

    // Mensaje si no se encuentra el ticket
    JOptionPane.showMessageDialog(null, "No se encontró el ticket de la venta " + id);

} catch (Exception e) {
    // Error si no hay ID válido o no se seleccionó una venta
    JOptionPane.showMessageDialog(null, "Selecciona una venta primero");
}
    }//GEN-LAST:event_btnPdfVentasActionPerformed

    /**
 * Evento al hacer clic en la tabla de ventas.
 * 
 * Obtiene la fila seleccionada y asigna el ID de la venta
 * al campo txtIdVenta para poder usarlo posteriormente
 * (por ejemplo, abrir ticket o eliminar).
 */
    private void TableVentasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableVentasMouseClicked
        int fila = TableVentas.rowAtPoint(evt.getPoint());
        txtIdVenta.setText(TableVentas.getValueAt(fila, 0).toString());
    }//GEN-LAST:event_TableVentasMouseClicked
/**
 * Evento del botón Exportar a Excel.
 * 
 * Genera un reporte en formato Excel con los productos
 * registrados en el sistema.
 */
    private void btnExcelProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelProActionPerformed
        // TODO add your handling code here:
        Excel.reporte();
    }//GEN-LAST:event_btnExcelProActionPerformed
/**
 * Evento del botón Nuevo Producto.
 * 
 * Limpia todos los campos del formulario de productos
 * para ingresar un nuevo registro.
 */
    private void btnNuevoproActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoproActionPerformed
        LimpiarProducto();
    }//GEN-LAST:event_btnNuevoproActionPerformed
/**
 * Evento del botón Eliminar Producto.
 * 
 * Verifica que exista un ID seleccionado, solicita confirmación
 * al usuario y elimina el producto de la base de datos.
 * Posteriormente actualiza la tabla y limpia el formulario.
 */
    private void btnEliminarProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProActionPerformed
         // Verifica que haya un ID seleccionado
        if (!"".equals(txtIdPro.getText())) {

        // Muestra confirmación al usuario
        int pregunta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar");

        if (pregunta == 0) {

            // Obtiene el ID del producto
            int id = Integer.parseInt(txtIdPro.getText());

            // Elimina el producto
            prodao.EliminarProductos(id);

            // Actualiza la interfaz
            LimpiarTable();
            LimpiarProducto();
            ListarProductos();
        }
    }
    }//GEN-LAST:event_btnEliminarProActionPerformed

 /**
 * Evento del botón Editar Producto.
 * 
 * Verifica que exista un producto seleccionado y que los campos
 * no estén vacíos. Posteriormente actualiza los datos del producto
 * en la base de datos y refresca la tabla.
 */
    private void btnEditarproActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarproActionPerformed
            // Verifica si hay un producto seleccionado
    if ("".equals(txtIdPro.getText())) {
        JOptionPane.showMessageDialog(null, "Seleccione una fila");

    } else {

        // Valida que los campos no estén vacíos
        if (!"".equals(txtCodigoPro.getText()) &&
            !"".equals(txtDesPro.getText()) &&
            !"".equals(txtCantPro.getText()) &&
            !"".equals(txtPrecioPro.getText())) {

            // Asigna los nuevos valores al objeto producto
            pro.setCodigo(txtCodigoPro.getText());
            pro.setNombre(txtDesPro.getText());
            pro.setProveedor(cbxProveedorPro.getSelectedItem().toString());
            pro.setStock(Integer.parseInt(txtCantPro.getText()));
            pro.setPrecio(Double.parseDouble(txtPrecioPro.getText()));
            pro.setId(Integer.parseInt(txtIdPro.getText()));

            // Actualiza el producto en la base de datos
            prodao.ModificarProductos(pro);

            // Mensaje de confirmación
            JOptionPane.showMessageDialog(null, "Producto Modificado");

            // Actualiza la interfaz
            LimpiarTable();
            ListarProductos();
            LimpiarProducto();

        } else {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
        }
    }
    }//GEN-LAST:event_btnEditarproActionPerformed

    
    /**
 * Evento del botón Guardar Producto.
 * 
 * Valida que todos los campos estén completos y registra un nuevo
 * producto en la base de datos. También maneja errores de formato
 * en cantidad y precio.
 */
    private void btnGuardarproActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarproActionPerformed
    try {
            // Validación de campos obligatorios
            if (!txtCodigoPro.getText().trim().isEmpty()
                    && !txtDesPro.getText().trim().isEmpty()
                    && cbxProveedorPro.getSelectedItem() != null
                    && !txtCantPro.getText().trim().isEmpty()
                    && !txtPrecioPro.getText().trim().isEmpty()) {

                // Asignación de valores al objeto producto
                pro.setCodigo(txtCodigoPro.getText().trim());
                pro.setNombre(txtDesPro.getText().trim());
                pro.setProveedor(cbxProveedorPro.getSelectedItem().toString());
                pro.setStock(Integer.parseInt(txtCantPro.getText().trim()));
                pro.setPrecio(Double.parseDouble(txtPrecioPro.getText().trim()));

                // Registro en base de datos
                prodao.RegistrarProductos(pro);

                // Mensaje de confirmación
                JOptionPane.showMessageDialog(null, "Producto registrado");

                // Actualización de la interfaz
                LimpiarTable();
                ListarProductos();
                LimpiarProducto();

            } else {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            }

        } catch (NumberFormatException e) {
            // Manejo de error si cantidad o precio no son números válidos
            JOptionPane.showMessageDialog(null, "Cantidad y precio deben ser números válidos");
        }
      
    }//GEN-LAST:event_btnGuardarproActionPerformed

    
    /**
    * Evento al hacer clic en la tabla de productos.
    * 
    * Obtiene la fila seleccionada y carga los datos del producto
    * en los campos del formulario para su edición o eliminación.
    */
    private void TableProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableProductoMouseClicked
        int fila = TableProducto.rowAtPoint(evt.getPoint());

        
        // Asigna los valores de la fila seleccionada a los campos
        if (fila >= 0) {
            txtIdPro.setText(TableProducto.getValueAt(fila, 0).toString());
            txtCodigoPro.setText(TableProducto.getValueAt(fila, 1).toString());
            txtDesPro.setText(TableProducto.getValueAt(fila, 2).toString());
            cbxProveedorPro.setSelectedItem(TableProducto.getValueAt(fila, 3).toString());
            txtCantPro.setText(TableProducto.getValueAt(fila, 4).toString());
            txtPrecioPro.setText(TableProducto.getValueAt(fila, 5).toString());
        }
        
    }//GEN-LAST:event_TableProductoMouseClicked
/**
 * Evento al escribir en el campo Precio del producto.
 * 
 * Permite únicamente números y un solo punto decimal.
 */
    private void txtPrecioProKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioProKeyTyped
        event.numberDecimalKeyPress(evt,txtPrecioPro);
    }//GEN-LAST:event_txtPrecioProKeyTyped

    /**
 * Evento al escribir en el campo Cantidad del producto.
 * 
 * Permite únicamente números enteros (0-9).
 */
    private void txtCantProKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantProKeyTyped
        event.numberKeyPress(evt);
    }//GEN-LAST:event_txtCantProKeyTyped

    /**
 * Evento al escribir en el campo Descripción del producto.
 * 
 * Permite únicamente letras, espacios y retroceso.
 */
    private void txtDesProKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDesProKeyTyped
        event.textKeyPress(evt);
    }//GEN-LAST:event_txtDesProKeyTyped

    /**
    * Evento al escribir en el campo Código del producto.
    * 
    * Permite únicamente números.
    */
    private void txtCodigoProKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoProKeyTyped
        event.numberKeyPress(evt);
    }//GEN-LAST:event_txtCodigoProKeyTyped

    /**
    * Evento del botón Nuevo Proveedor.
    * 
    * Limpia todos los campos del formulario de proveedor
    * para registrar un nuevo proveedor.
    */
    private void btnNuevoProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoProveedorActionPerformed
        LimpiarProveedor();
    }//GEN-LAST:event_btnNuevoProveedorActionPerformed

    private void btnEditarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarProveedorActionPerformed
       /**
 * Valida y modifica los datos de un proveedor seleccionado.
 * 
 * Primero verifica que exista un proveedor seleccionado mediante su ID.
 * Después valida que todos los campos estén completos, asigna los datos
 * al objeto Proveedor y actualiza el registro en la base de datos.
 */
if ("".equals(txtIdProveedor.getText())) {
    JOptionPane.showMessageDialog(null, "Seleccione una fila");

} else {
    // Valida que todos los campos estén llenos
    if (!txtRucProveedor.getText().trim().isEmpty()
            && !txtNombreProveedor.getText().trim().isEmpty()
            && !txtTelefonoProveedor.getText().trim().isEmpty()
            && !txtDireccionProveedor.getText().trim().isEmpty()
            && !txtFechaProveedor.getText().trim().isEmpty()) {

        // Asigna los datos capturados al objeto proveedor
        pr.setDni(Integer.parseInt(txtRucProveedor.getText()));
        pr.setNombre(txtNombreProveedor.getText());
        pr.setTelefono(txtTelefonoProveedor.getText());
        pr.setDireccion(txtDireccionProveedor.getText());
        pr.setFecha(txtFechaProveedor.getText());
        pr.setId(Integer.parseInt(txtIdProveedor.getText()));

        // Modifica el proveedor en la base de datos
        PrDAO.ModificarProveedor(pr);

        // Mensaje de confirmación
        JOptionPane.showMessageDialog(null, "Proveedor Modificado");

        // Actualiza la interfaz
        LimpiarTable();
        ListarProveedor();
        LimpiarProveedor();

        /*
         * Opcional:
         * Estos botones se pueden activar o desactivar según el flujo del formulario.
         */
        /*
        btnEditarProveedor.setEnabled(false);
        btnEliminarProveedor.setEnabled(false);
        btnguardarProveedor.setEnabled(true);
        */
    } else {
        JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
    }
}
    }//GEN-LAST:event_btnEditarProveedorActionPerformed

    private void btnEliminarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProveedorActionPerformed
        /**
        * Elimina un proveedor seleccionado.
        * 
        * Verifica que exista un ID seleccionado, solicita confirmación al usuario
        * y elimina el proveedor de la base de datos. Posteriormente actualiza la
        * tabla y limpia los campos del formulario.
        */
       if (!"".equals(txtIdProveedor.getText())) {

           // Confirmación antes de eliminar
           int pregunta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar?");

           if (pregunta == JOptionPane.YES_OPTION) {

               // Obtiene el ID del proveedor
               int id = Integer.parseInt(txtIdProveedor.getText());

               // Elimina el proveedor de la base de datos
               PrDAO.EliminarProveedor(id);

               // Actualiza la interfaz
               LimpiarTable();
               ListarProveedor();
               LimpiarProveedor();
           }

       } else {
           JOptionPane.showMessageDialog(null, "Seleccione una fila");
       }
    }//GEN-LAST:event_btnEliminarProveedorActionPerformed

    private void btnguardarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnguardarProveedorActionPerformed
                /**
         * Registra un nuevo proveedor.
         * 
         * Valida que todos los campos estén completos antes de registrar
         * el proveedor en la base de datos. Posteriormente actualiza la
         * tabla y limpia los campos del formulario.
         */
        if (!txtRucProveedor.getText().trim().isEmpty()
                && !txtNombreProveedor.getText().trim().isEmpty()
                && !txtTelefonoProveedor.getText().trim().isEmpty()
                && !txtDireccionProveedor.getText().trim().isEmpty()
                && !txtFechaProveedor.getText().trim().isEmpty()) {

            // Asigna los datos al objeto proveedor
            pr.setDni(Integer.parseInt(txtRucProveedor.getText()));
            pr.setNombre(txtNombreProveedor.getText());
            pr.setTelefono(txtTelefonoProveedor.getText());
            pr.setDireccion(txtDireccionProveedor.getText());
            pr.setFecha(txtFechaProveedor.getText());

            // Registra el proveedor en la base de datos
            PrDAO.RegistrarProveedor(pr);

            // Mensaje de confirmación
            JOptionPane.showMessageDialog(null, "Proveedor Registrado");

            // Actualiza la interfaz
            LimpiarTable();
            ListarProveedor();
            LimpiarProveedor();

            /*
             * Opcional: control de botones según flujo
             */
            /*
            btnEditarProveedor.setEnabled(false);
            btnEliminarProveedor.setEnabled(false);
            btnguardarProveedor.setEnabled(true);
            */

        } else {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
        }
    }//GEN-LAST:event_btnguardarProveedorActionPerformed

    private void TableProveedorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableProveedorMouseClicked
         /**
         * Evento al hacer clic en la tabla de proveedores.
         * 
         * Obtiene la fila seleccionada y carga los datos del proveedor
         * en los campos del formulario para su edición o eliminación.
         */
        int fila = TableProveedor.rowAtPoint(evt.getPoint());

        // Verifica que se haya seleccionado una fila válida
        if (fila >= 0) {

            txtIdProveedor.setText(TableProveedor.getValueAt(fila, 0).toString());
            txtRucProveedor.setText(TableProveedor.getValueAt(fila, 1).toString());
            txtNombreProveedor.setText(TableProveedor.getValueAt(fila, 2).toString());
            txtTelefonoProveedor.setText(TableProveedor.getValueAt(fila, 3).toString());
            txtDireccionProveedor.setText(TableProveedor.getValueAt(fila, 4).toString());
            txtFechaProveedor.setText(TableProveedor.getValueAt(fila, 5).toString());
        }
    }//GEN-LAST:event_TableProveedorMouseClicked

    private void txtFechaProveedorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFechaProveedorKeyTyped
        
    }//GEN-LAST:event_txtFechaProveedorKeyTyped
/**
 * Evento al escribir en el campo Dirección del proveedor.
 * 
 * Permite únicamente letras, espacios y retroceso.
 */
    private void txtDireccionProveedorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionProveedorKeyTyped
        event.textKeyPress(evt);
    }//GEN-LAST:event_txtDireccionProveedorKeyTyped

    
    private void txtDireccionProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDireccionProveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionProveedorActionPerformed
    
    /**
     * Evento al escribir en el campo Teléfono del proveedor.
     * 
     * Permite únicamente números (0-9).
     */
    private void txtTelefonoProveedorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoProveedorKeyTyped
        event.numberKeyPress(evt);
    }//GEN-LAST:event_txtTelefonoProveedorKeyTyped
    
    /**
     * Evento al escribir en el campo Nombre del proveedor.
     * 
     * Permite únicamente letras, espacios y retroceso.
     */
    private void txtNombreProveedorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreProveedorKeyTyped
        event.textKeyPress(evt);
    }//GEN-LAST:event_txtNombreProveedorKeyTyped

    /**
 * Evento al escribir en el campo RUC del proveedor.
 * 
 * Permite únicamente números.
 */
    private void txtRucProveedorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRucProveedorKeyTyped
        event.numberKeyPress(evt);
    }//GEN-LAST:event_txtRucProveedorKeyTyped

    private void txtRucProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRucProveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRucProveedorActionPerformed
    
    /**
     * Evento del botón Graficar.
     * 
     * Obtiene la fecha seleccionada en el componente Midate,
     * la formatea y genera una gráfica de ventas mediante la clase Grafico.
     */
    private void btnGraficarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGraficarActionPerformed
        String fechaReporte = new SimpleDateFormat("dd/MM/yyyy").format(Midate.getDate());
        Grafico.Graficar(fechaReporte);
    }//GEN-LAST:event_btnGraficarActionPerformed

    /**
    * Evento del botón Generar Venta.
    * 
    * Verifica que existan productos en la tabla de venta. Después abre
    * la ventana de cobro para capturar el pago del cliente y calcular
    * el cambio.
    * 
    * Si el cobro es confirmado, registra la venta, guarda el detalle,
    * actualiza el stock, genera el ticket PDF, actualiza la tabla de ventas
    * y limpia la tabla de productos vendidos.
    */
    private void btnGenerarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarVentaActionPerformed
        // Verifica que existan productos agregados a la venta
    if (TableVenta.getRowCount() > 0) {

        // Obtiene el total actual de la venta
        double total = Totalpagar;

        // Abre la ventana de cobro
        VentanaCobro vc = new VentanaCobro(null, total);
        vc.setVisible(true);

        // Verifica si el cobro fue confirmado
        if (vc.isConfirmado()) {

            // Obtiene el pago y cambio calculados
            pagoCliente = vc.getPago();
            cambioCliente = vc.getCambio();

            // Muestra el resumen del cobro
            JOptionPane.showMessageDialog(null,
                    "Pago: $" + String.format("%.2f", pagoCliente)
                    + "\nCambio: $" + String.format("%.2f", cambioCliente));

            // Registra la venta en la base de datos
            RegistrarVenta();

            // Registra los productos vendidos
            RegistrarDetalle();

            // Actualiza el stock de productos
            ActualizarStock();

            // Genera el ticket PDF
            ticket80mm();

            // Actualiza la tabla de ventas
            TablaVentas();

            // Limpia la tabla de venta actual
            LimpiarTableVenta();
        }

    } else {
        JOptionPane.showMessageDialog(null, "No hay productos en la venta");
    }    
    }//GEN-LAST:event_btnGenerarVentaActionPerformed

    private void TableVentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableVentaMouseClicked

    }//GEN-LAST:event_TableVentaMouseClicked

    /**
    * Evento al escribir en el campo Cantidad de venta.
    * 
    * Permite únicamente la entrada de números enteros (0-9),
    * evitando caracteres no válidos para cantidades.
    */
    private void txtCantidadVentaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadVentaKeyTyped
        event.numberKeyPress(evt);
    }//GEN-LAST:event_txtCantidadVentaKeyTyped

    /**
    * Evento al presionar una tecla en el campo Cantidad de venta.
    * 
    * Si se presiona la tecla ENTER:
    * - Valida que exista una cantidad ingresada
    * - Calcula el total del producto
    * - Verifica el stock disponible
    * - Evita productos duplicados en la tabla
    * - Agrega el producto a la tabla de venta
    * - Actualiza el total a pagar
    * - Limpia los campos y regresa el foco al código
    */
    private void txtCantidadVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadVentaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

        // Validar que se haya ingresado una cantidad
        if (!"".equals(txtCantidadVenta.getText())) {

            String cod = txtCodigoVenta.getText();
            String descripcion = txtDescripcionVenta.getText();
            int cant = Integer.parseInt(txtCantidadVenta.getText());
            double precio = Double.parseDouble(txtPrecioVenta.getText());
            double total = cant * precio;
            int stock = Integer.parseInt(txtStockDisponible.getText());

            // Verificar stock disponible
            if (stock >= cant) {

                item = item + 1;
                tmp = (DefaultTableModel) TableVenta.getModel();

                // Validar que el producto no esté duplicado
                for (int i = 0; i < TableVenta.getRowCount(); i++) {
                    if (TableVenta.getValueAt(i, 1).equals(descripcion)) {
                        JOptionPane.showMessageDialog(null, "El producto ya está registrado");
                        return;
                    }
                }

                // Crear fila para la tabla
                Object[] fila = new Object[5];
                fila[0] = cod;
                fila[1] = descripcion;
                fila[2] = cant;
                fila[3] = precio;
                fila[4] = total;

                // Agregar fila a la tabla
                tmp.addRow(fila);
                TableVenta.setModel(tmp);

                // Actualizar total de la venta
                TotalPagar();

                // Limpiar campos
                LimparVenta();

                // Regresar el foco al campo código
                txtCodigoVenta.requestFocus();

            } else {
                JOptionPane.showMessageDialog(null, "Stock no disponible");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Ingrese Cantidad");
        }
    }
    }//GEN-LAST:event_txtCantidadVentaKeyPressed
   
    /**
 * Evento al escribir en el campo Descripción de venta.
 * 
 * Permite únicamente letras, espacios y retroceso.
 * Se usa para evitar caracteres inválidos en la descripción del producto.
 */
    private void txtDescripcionVentaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescripcionVentaKeyTyped
        event.textKeyPress(evt);
    }//GEN-LAST:event_txtDescripcionVentaKeyTyped

    /**
    * Evento al escribir en el campo Código de venta.
    * 
    * Permite únicamente números (0-9).
    * Se utiliza para validar que el código ingresado sea numérico.
    */
    private void txtCodigoVentaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoVentaKeyTyped
        event.numberKeyPress(evt);
    }//GEN-LAST:event_txtCodigoVentaKeyTyped

    /**
    * Evento al presionar una tecla en el campo Código de venta.
    * 
    * Si se presiona ENTER:
    * - Valida que se haya ingresado un código
    * - Busca el producto en la base de datos
    * - Si existe, carga sus datos en los campos (descripción, precio, stock)
    * - Si no existe, limpia los campos y solicita nuevamente el código
    */
    private void txtCodigoVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoVentaKeyPressed
       if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

    // Verifica que el campo código no esté vacío
    if (!txtCodigoVenta.getText().trim().isEmpty()) {

        String cod = txtCodigoVenta.getText();

        // Busca el producto en la base de datos
        pro = prodao.BuscarPro(cod);

        // Verifica si el producto existe
        if (pro != null && pro.getNombre() != null) {

            // Carga los datos del producto
            txtDescripcionVenta.setText(pro.getNombre());
            txtPrecioVenta.setText(String.valueOf(pro.getPrecio()));
            txtStockDisponible.setText(String.valueOf(pro.getStock()));

            // Enfoca el campo cantidad y selecciona el contenido
            txtCantidadVenta.requestFocus();
            txtCantidadVenta.selectAll();

        } else {
            // Producto no encontrado
            JOptionPane.showMessageDialog(null, "Producto no encontrado");
            LimparVenta();
            txtCodigoVenta.requestFocus();
        }

    } else {
        // Campo vacío
        JOptionPane.showMessageDialog(null, "Ingrese el código del producto");
        txtCodigoVenta.requestFocus();
    }
}
    }//GEN-LAST:event_txtCodigoVentaKeyPressed

    private void txtCodigoVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoVentaActionPerformed

    /**
    * Evento del botón Eliminar producto de la venta.
    * 
    * Elimina la fila seleccionada de la tabla de venta,
    * actualiza el total a pagar y regresa el foco al campo
    * de código para continuar con la venta.
    */
    private void btnEliminarventaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarventaActionPerformed
          // Obtiene el modelo de la tabla
        modelo = (DefaultTableModel) TableVenta.getModel();

        // Verifica que haya una fila seleccionada
        int fila = TableVenta.getSelectedRow();

        if (fila >= 0) {

            // Elimina la fila seleccionada
            modelo.removeRow(fila);

            // Recalcula el total
            TotalPagar();

            // Regresa el foco al campo código
            txtCodigoVenta.requestFocus();

        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un producto para eliminar");
        }
    }//GEN-LAST:event_btnEliminarventaActionPerformed

    private void TableVentasMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableVentasMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TableVentasMousePressed

    private void cbxProveedorProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxProveedorProActionPerformed

    }//GEN-LAST:event_cbxProveedorProActionPerformed

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarActionPerformed

 /**
 * Evento al interactuar con el campo de búsqueda de productos.
 * 
 * Ejecuta la búsqueda de productos y agrega un listener para
 * actualizar los resultados en tiempo real mientras el usuario escribe.
 */
    private void txtBuscarProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarProActionPerformed
         // Ejecuta búsqueda inicial
    BuscarProductoTabla();

    // Agrega listener para búsqueda en tiempo real
    txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            BuscarProductoTabla();
        }
    });
    }//GEN-LAST:event_txtBuscarProActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Sistema().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabelTotal;
    private javax.swing.JLabel LabelVendedor;
    private com.toedter.calendar.JDateChooser Midate;
    private javax.swing.JTable TableProducto;
    private javax.swing.JTable TableProveedor;
    private javax.swing.JTable TableVenta;
    private javax.swing.JTable TableVentas;
    private javax.swing.JButton btnActualizarConfig;
    private javax.swing.JButton btnConfig;
    private javax.swing.JButton btnEditarProveedor;
    private javax.swing.JButton btnEditarpro;
    private javax.swing.JButton btnEliminarPro;
    private javax.swing.JButton btnEliminarProveedor;
    private javax.swing.JButton btnEliminarventa;
    private javax.swing.JButton btnExcelPro;
    private javax.swing.JButton btnGenerarVenta;
    private javax.swing.JButton btnGraficar;
    private javax.swing.JButton btnGuardarpro;
    private javax.swing.JButton btnNuevaVenta;
    private javax.swing.JButton btnNuevoProveedor;
    private javax.swing.JButton btnNuevopro;
    private javax.swing.JButton btnPdfVentas;
    private javax.swing.JButton btnProductos;
    private javax.swing.JButton btnProveedor;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnVentas;
    private javax.swing.JButton btnguardarProveedor;
    private javax.swing.JComboBox<String> cbxProveedorPro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JButton txtBuscarPro;
    private javax.swing.JTextField txtCantPro;
    private javax.swing.JTextField txtCantidadVenta;
    private javax.swing.JTextField txtCodigoPro;
    private javax.swing.JTextField txtCodigoVenta;
    private javax.swing.JTextField txtDesPro;
    private javax.swing.JTextField txtDescripcionVenta;
    private javax.swing.JTextField txtDireccionConfig;
    private javax.swing.JTextField txtDireccionProveedor;
    private javax.swing.JTextField txtFechaProveedor;
    private javax.swing.JTextField txtIdConfig;
    private javax.swing.JTextField txtIdPro;
    private javax.swing.JTextField txtIdProveedor;
    private javax.swing.JTextField txtIdVenta;
    private javax.swing.JTextField txtIdpro;
    private javax.swing.JTextField txtNombreConfig;
    private javax.swing.JTextField txtNombreProveedor;
    private javax.swing.JTextField txtPrecioPro;
    private javax.swing.JTextField txtPrecioVenta;
    private javax.swing.JTextField txtRucConfig;
    private javax.swing.JTextField txtRucProveedor;
    private javax.swing.JTextField txtStockDisponible;
    private javax.swing.JTextField txtTelefonoConfig;
    private javax.swing.JTextField txtTelefonoProveedor;
    // End of variables declaration//GEN-END:variables
    
    
   /**
 * Limpia los campos del formulario de proveedor.
 */
private void LimpiarProveedor() {
    txtIdProveedor.setText("");
    txtRucProveedor.setText("");
    txtNombreProveedor.setText("");
    txtTelefonoProveedor.setText("");
    txtDireccionProveedor.setText("");
    txtFechaProveedor.setText("");
}

/**
 * Limpia los campos del formulario de producto.
 */
private void LimpiarProducto() {
    txtIdPro.setText("");
    txtCodigoPro.setText("");
    cbxProveedorPro.setSelectedItem(null);
    txtDesPro.setText("");
    txtCantPro.setText("");
    txtPrecioPro.setText("");
}

/**
 * Calcula el total a pagar sumando los importes de la tabla de venta.
 */
private void TotalPagar() {
    Totalpagar = 0.00;
    int numFila = TableVenta.getRowCount();

    for (int i = 0; i < numFila; i++) {
        double cal = Double.parseDouble(String.valueOf(TableVenta.getModel().getValueAt(i, 4)));
        Totalpagar = Totalpagar + cal;
    }

    LabelTotal.setText(String.format("%.2f", Totalpagar));
}

/**
 * Limpia los campos usados para capturar productos en la venta.
 */
private void LimparVenta() {
    txtCodigoVenta.setText("");
    txtDescripcionVenta.setText("");
    txtCantidadVenta.setText("");
    txtStockDisponible.setText("");
    txtPrecioVenta.setText("");
    txtIdVenta.setText("");
}

/**
 * Registra la venta principal en la base de datos.
 */
private void RegistrarVenta() {
    String vendedor = LabelVendedor.getText();
    Double monto = Totalpagar;

    v.setVendedor(vendedor);
    v.setTotal(monto);
    v.setFecha(fechaActual);

    Vdao.RegistrarVenta(v);
}

/**
 * Registra el detalle de la venta.
 * 
 * Recorre la tabla de venta y guarda cada producto vendido
 * relacionándolo con el ID de la venta registrada.
 */
private void RegistrarDetalle() {
    int id = Vdao.IdVenta();

    for (int i = 0; i < TableVenta.getRowCount(); i++) {
        String cod = TableVenta.getValueAt(i, 0).toString();
        int cant = Integer.parseInt(TableVenta.getValueAt(i, 2).toString());
        double precio = Double.parseDouble(TableVenta.getValueAt(i, 3).toString());

        Dv.setCod_pro(cod);
        Dv.setCantidad(cant);
        Dv.setPrecio(precio);
        Dv.setId_venta(id);

        Vdao.RegistrarDetalle(Dv);
    }
}

/**
 * Actualiza el stock de los productos vendidos.
 */
private void ActualizarStock() {
    for (int i = 0; i < TableVenta.getRowCount(); i++) {
        String cod = TableVenta.getValueAt(i, 0).toString();
        int cant = Integer.parseInt(TableVenta.getValueAt(i, 2).toString());

        pro = prodao.BuscarPro(cod);

        int StockActual = pro.getStock() - cant;

        Vdao.ActualizarStock(StockActual, cod);
    }
}

/**
 * Limpia la tabla donde se muestran los productos agregados a la venta.
 */
private void LimpiarTableVenta() {
    tmp = (DefaultTableModel) TableVenta.getModel();
    int fila = TableVenta.getRowCount();

    for (int i = 0; i < fila; i++) {
        tmp.removeRow(0);
    }
}

/**
 * Genera un ticket en formato PDF con tamaño aproximado de 80mm.
 * 
 * El ticket incluye datos del negocio, número de ticket, fecha,
 * productos vendidos, total, pago del cliente, cambio y mensaje final.
 */
private void ticket80mm() {
    try {
        int id = Vdao.IdVenta();

        File file = new File("src/pdf/ticket_" + id + "_" + System.currentTimeMillis() + ".pdf");
        FileOutputStream archivo = new FileOutputStream(file);

        // Tamaño 80mm: ancho fijo y alto amplio
        Document doc = new Document(new Rectangle(226, 1000), 5, 5, 5, 5);
        PdfWriter.getInstance(doc, archivo);
        doc.open();

        // Fuentes tipo ticket
        Font font = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL);
        Font bold = new Font(Font.FontFamily.COURIER, 9, Font.BOLD);

        // Encabezado del ticket
        Paragraph header = new Paragraph();
        header.setAlignment(Element.ALIGN_CENTER);

        header.add(new Phrase("refaccionaria automotriz: " + txtNombreConfig.getText() + "\n", bold));
        header.add(new Phrase("dirección: " + txtDireccionConfig.getText() + "\n", font));
        header.add(new Phrase("Tel: " + txtTelefonoConfig.getText() + "\n", font));
        header.add(new Phrase("RFC: " + txtRucConfig.getText() + "\n", font));
        header.add(new Phrase("-------------------------------\n", font));

        Date date = new Date();
        header.add(new Phrase("Ticket: " + id + "\n", bold));
        header.add(new Phrase(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(date) + "\n", font));
        header.add(new Phrase("-------------------------------\n", font));

        doc.add(header);

        // Productos vendidos
        Paragraph productos = new Paragraph();
        productos.setAlignment(Element.ALIGN_LEFT);

        for (int i = 0; i < TableVenta.getRowCount(); i++) {
            String producto = TableVenta.getValueAt(i, 1).toString();
            String cantidad = TableVenta.getValueAt(i, 2).toString();
            String precio = TableVenta.getValueAt(i, 3).toString();
            String total = TableVenta.getValueAt(i, 4).toString();

            productos.add(new Phrase(producto + "\n", bold));
            productos.add(new Phrase(cantidad + " x $" + precio + " = $" + total + "\n", font));
        }

        productos.add(new Phrase("-------------------------------\n", font));
        doc.add(productos);

        // Totales de la venta
        Paragraph total = new Paragraph();
        total.setAlignment(Element.ALIGN_RIGHT);
        total.add(new Phrase("TOTAL: $" + Totalpagar + "\n", bold));
        total.add(new Phrase("PAGO:   $" + String.format("%.2f", pagoCliente) + "\n", font));
        total.add(new Phrase("CAMBIO: $" + String.format("%.2f", cambioCliente) + "\n", font));
        total.add(new Phrase("-------------------------------\n", font));
        doc.add(total);

        // Mensaje final
        Paragraph mensaje = new Paragraph();
        mensaje.setAlignment(Element.ALIGN_CENTER);
        mensaje.add(new Phrase("GRACIAS POR SU COMPRA\n", bold));
        mensaje.add(new Phrase("\n"));
        mensaje.add(new Phrase("Vuelva pronto\n", font));
        doc.add(mensaje);

        doc.close();
        archivo.close();

        Desktop.getDesktop().open(file);

    } catch (DocumentException | IOException e) {
        System.out.println(e.toString());
    }
}
    
   
}
