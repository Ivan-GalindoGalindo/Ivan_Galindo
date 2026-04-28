
package Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Clase VentanaCobro
 * 
 * Representa una ventana modal utilizada para realizar el cobro
 * de una venta. Permite ingresar el pago del cliente, calcular el
 * cambio en tiempo real y confirmar la operación.
 * 
 * Autor: Ivan Galindo Galindo e George Ortge leon 
 */
public class VentanaCobro extends JDialog {

    // Campo donde se ingresa el pago del cliente
    private JTextField txtPago;

    // Etiquetas para mostrar el total y el cambio
    private JLabel lblTotal, lblCambio;

    // Total de la venta
    private double total;

    // Indica si el cobro fue confirmado
    private boolean confirmado = false;

    // Cantidad pagada por el cliente
    private double pago = 0;

    /**
     * Constructor de la ventana de cobro.
     * 
     * @param parent Ventana padre
     * @param total Total de la venta a cobrar
     */
    public VentanaCobro(Frame parent, double total) {
        super(parent, "Cobro", true);
        this.total = total;

        setSize(300, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 5, 5));

        lblTotal = new JLabel("Total: $" + total, SwingConstants.CENTER);
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));

        txtPago = new JTextField();
        txtPago.setHorizontalAlignment(JTextField.CENTER);

        lblCambio = new JLabel("Cambio: $0", SwingConstants.CENTER);

        JButton btnCobrar = new JButton("Cobrar");

        add(lblTotal);
        add(new JLabel("Ingrese pago:", SwingConstants.CENTER));
        add(txtPago);
        add(lblCambio);
        add(btnCobrar);

        // Calcula el cambio mientras el usuario escribe
        txtPago.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                calcularCambio();
            }
        });

        // Valida el pago y confirma el cobro
        btnCobrar.addActionListener(e -> {
            try {
                pago = Double.parseDouble(txtPago.getText());

                if (pago < total) {
                    JOptionPane.showMessageDialog(null, "Pago insuficiente");
                } else {
                    confirmado = true;
                    dispose();
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Ingrese un número válido");
            }
        });
    }

    /**
     * Calcula el cambio en tiempo real.
     * 
     * Si el pago es menor al total, muestra cuánto falta.
     * Si el pago es suficiente, muestra el cambio correspondiente.
     */
    private void calcularCambio() {
        try {
            double p = Double.parseDouble(txtPago.getText());
            double cambio = p - total;

            if (p < total) {
                lblCambio.setText("Faltan: $" + (total - p));
            } else {
                lblCambio.setText("Cambio: $" + cambio);
            }

        } catch (Exception e) {
            lblCambio.setText("Cambio: $0");
        }
    }

    /**
     * Indica si el cobro fue confirmado.
     * 
     * @return true si el usuario confirmó el cobro
     */
    public boolean isConfirmado() {
        return confirmado;
    }

    /**
     * Obtiene la cantidad pagada por el cliente.
     * 
     * @return pago realizado
     */
    public double getPago() {
        return pago;
    }

    /**
     * Obtiene el cambio que debe entregarse al cliente.
     * 
     * @return cambio calculado
     */
    public double getCambio() {
        return pago - total;
    }
}