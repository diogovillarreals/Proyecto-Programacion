package vuelosfis.main;

// Importamos el Cerebro 
import vuelosfis.controlador.ControladorVuelo;
// Importamos la Cara 
import vuelosfis.vista.VentanaPrincipal; 

public class Main {

    public static void main(String[] args) {
        // 1. Instanciamos el Controlador Principal
        // (Este a su vez crea el ControladorReserva)
        vuelosfis.controlador.ControladorVuelo controlador = new vuelosfis.controlador.ControladorVuelo();
        
        // 2. Iniciamos el sistema a través del método dedicado
        // Esto cargará los datos y abrirá la Ventana Principal automáticamente
        controlador.iniciarSistema();
    }
    
}