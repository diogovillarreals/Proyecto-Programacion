package vuelosfis.main;

// Importamos el Cerebro (Fase 7)
import vuelosfis.controlador.ControladorVuelo;
// Importamos la Cara (Fase 8)
import vuelosfis.vista.VentanaPrincipal; 

public class Main {

    public static void main(String[] args) {
        // 1. Instanciamos el Controlador Principal
        // (Este a su vez crea el ControladorReserva)
        vuelosfis.controlador.ControladorVuelo controlador = new vuelosfis.controlador.ControladorVuelo();
        
        // 2. IMPORTANTE: ¡ORDENARLE QUE CARGUE LOS DATOS!
        // Esta línea va al archivo 'vuelos.txt' y 'reservas.csv' y restaura la memoria.
        // Si falta esta línea, el programa empieza con amnesia.
        controlador.getControladorReserva().cargarDatosIniciales(); 

        // 3. Crear e iniciar la ventana
        vuelosfis.vista.VentanaPrincipal ventana = new vuelosfis.vista.VentanaPrincipal();
        
        // 4. Conectar ventana con controlador
        ventana.setControlador(controlador);
        
        // 5. Mostrar
        ventana.setVisible(true);
    }
}