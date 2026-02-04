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
        System.out.println("INICIANDO CONTROLADORES");
        
        // 1. Cargar datos y restaurar memoria
        controladorReserva.cargarDatosIniciales();
        
        System.out.println("Datos cargados. Abriendo Ventana Principal");
        
        // 1. Creamos la ventana visual
        vuelosfis.vista.VentanaPrincipal ventana = new vuelosfis.vista.VentanaPrincipal();
        
        // 2. Le pasamos 'this' (este controlador) para que la ventana pueda buscar vuelos
        ventana.setControlador(this); 
        
        // 3. La mostramos en el centro de la pantalla
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }
    
    public ControladorReserva getControladorReserva() {
        return controladorReserva;
    }
}
