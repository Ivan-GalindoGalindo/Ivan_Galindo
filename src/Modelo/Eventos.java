package modelo;

import java.awt.event.KeyEvent;
import javax.swing.JTextField;

/**
 * Clase Eventos
 * 
 * Esta clase contiene métodos para validar la entrada de datos
 * en campos de texto mediante eventos de teclado (KeyEvent).
 * 
 * Permite restringir lo que el usuario puede escribir en los
 * JTextField, como solo letras, solo números o números decimales.
 * 
 * Autores: (Ivan Galindo Galinddo e George Ortega Leon)
 */
public class Eventos {

    /**
     * Método que permite solo letras (mayúsculas y minúsculas),
     * espacio y tecla de retroceso.
     * 
     * @param evt Evento de teclado generado al presionar una tecla
     */
    public void textKeyPress(KeyEvent evt) {

        // Se obtiene el carácter presionado
        char car = evt.getKeyChar();

        // Validación: solo letras, espacio y backspace
        if ((car < 'a' || car > 'z') && (car < 'A' || car > 'Z')
                && (car != (char) KeyEvent.VK_BACK_SPACE)
                && (car != (char) KeyEvent.VK_SPACE)) {

            // Si no cumple, se cancela la entrada
            evt.consume();
        }
    }

    /**
     * Método que permite solo números (0-9) y tecla de retroceso.
     * 
     * @param evt Evento de teclado
     */
    public void numberKeyPress(KeyEvent evt) {

        // Se obtiene el carácter presionado
        char car = evt.getKeyChar();

        // Validación: solo números y backspace
        if ((car < '0' || car > '9')
                && (car != (char) KeyEvent.VK_BACK_SPACE)) {

            // Se cancela la entrada si no es válido
            evt.consume();
        }
    }

    /**
     * Método que permite solo números decimales.
     * Acepta números, un punto decimal (.) y backspace.
     * 
     * @param evt Evento de teclado
     * @param textField Campo de texto donde se escribe
     */
    public void numberDecimalKeyPress(KeyEvent evt, JTextField textField) {

        // Se obtiene el carácter presionado
        char car = evt.getKeyChar();

        // Si ya existe un punto decimal, no permite otro
        if ((car < '0' || car > '9')
                && textField.getText().contains(".")
                && (car != (char) KeyEvent.VK_BACK_SPACE)) {

            evt.consume();

        // Permite números, un punto y backspace
        } else if ((car < '0' || car > '9')
                && (car != '.')
                && (car != (char) KeyEvent.VK_BACK_SPACE)) {

            evt.consume();
        }
    }
}