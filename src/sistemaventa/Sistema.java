
package sistemaventa;

import Vista.Login;
import Vista.Registro;

/**
 * Clase principal del sistema.
 * 
 * Contiene el método main que inicia la ejecución de la aplicación.
 * Desde aquí se define qué ventana se mostrará al iniciar el sistema
 * (Registro o Login).
 * 
 * Autor: Ivan G
 */
public class Sistema {

    /**
     * Método principal (punto de entrada del sistema).
     * 
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {

        // Inicia la ventana de registro
        Registro rg = new Registro();
        rg.setVisible(true);

        /*
         * Alternativa:
         * Si deseas iniciar directamente con el login,
         * puedes usar este código en lugar del registro.
         */
        /*
        Login lg = new Login();
        lg.setVisible(true);
        */
    }
}
