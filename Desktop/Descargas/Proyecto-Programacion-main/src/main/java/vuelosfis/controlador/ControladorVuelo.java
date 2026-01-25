package vuelosfis.controlador;

// En la Fase 8, aquí importaremos la VentanaPrincipal
// import vuelosfis.vista.VentanaPrincipal;

public class ControladorVuelo {

    // Instancias de los "sub-controladores"
    private final ControladorReserva controladorReserva;
    
    // Instancia de la ventana principal (Aún no existe, la dejamos comentada)
    // private VentanaPrincipal ventana;

    public ControladorVuelo() {
        // Al crear el principal, inicializamos los controladores específicos
        this.controladorReserva = new ControladorReserva();
    }

    /**
     * MÉTODO DE ARRANQUE (BOOTSTRAP)
     * Este es el método que llamará el Main para despertar a la bestia.
     */
    public void iniciarSistema() {
        System.out.println("--- INICIANDO SISTEMA VUELOS-FIS ---");

        // PASO 1: Cargar datos en memoria (Delegamos esto al experto)
        controladorReserva.cargarDatosIniciales();

        // PASO 2: Iniciar la Interfaz Gráfica (Fase 8)
        // Aquí es donde haremos visible la ventana. Por ahora simulamos.
        
        /* CÓDIGO FUTURO FASE 8:
        this.ventana = new VentanaPrincipal();
        this.ventana.setControlador(this.controladorReserva); // Conectamos ventana con cerebro
        this.ventana.setVisible(true);
        */
        
        System.out.println(">> Sistema listo. Esperando interacción del usuario (Fase 8)...");
    }
    
    // Getter para que otros puedan acceder al controlador de reservas si hace falta
    public ControladorReserva getControladorReserva() {
        return controladorReserva;
    }
}
