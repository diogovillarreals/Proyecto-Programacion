package vuelosfis.controlador;


public class ControladorVuelo {
    private final ControladorReserva controladorReserva;
    // private VentanaPrincipal ventana;

    public ControladorVuelo() {
        this.controladorReserva = new ControladorReserva();
    }

    /**
     * Este método se llama desde el MAIN para arrancar todo.
     */
    public void iniciarSistema() {
        System.out.println("--- INICIANDO CONTROLADORES (FASE 7) ---");
        
        // 1. Cargar datos y restaurar memoria
        controladorReserva.cargarDatosIniciales();
        
        System.out.println(">> Backend listo. Esperando Ventana (Fase 8)...");
        
        // AQUÍ IRÁ EL CÓDIGO DE LA VENTANA EN EL FUTURO:
        /*
        this.ventana = new VentanaPrincipal();
        this.ventana.setControlador(controladorReserva);
        this.ventana.setVisible(true);
        */
    }
    
    public ControladorReserva getControladorReserva() {
        return controladorReserva;
    }
}
