
package vuelosfis.modelo;

import vuelosfis.interfaces.TipoVuelo;


public class DetalleReserva {
    private Vuelo vuelo;
    private Pasajero pasajero;
    private Asiento asiento;
    private TipoVuelo tipoVuelo;
    private boolean esSeleccionManual;

    public DetalleReserva(Vuelo vuelo, Pasajero pasajero, Asiento asiento, TipoVuelo tipoVuelo, boolean esSeleccionManual) {
        this.vuelo = vuelo;
        this.pasajero = pasajero;
        this.asiento = asiento;
        this.tipoVuelo = tipoVuelo;
        this.esSeleccionManual = esSeleccionManual;
        
        if (asiento != null) {
            asiento.ocuparAsiento();
        }
    }

    public Pasajero getPasajero() { 
        return pasajero; 
    }
    public Vuelo getVuelo() { 
        return vuelo; 
    }
    public Asiento getAsiento() { 
        return asiento; 
    }
    
    public double calcularSubtotal() {
        double precioBase = vuelo.getPrecioBase();
        double precioConClase = precioBase * tipoVuelo.getMultiplicadorPrecio();
        double descuento = precioConClase * pasajero.getTipoPasajero().getDescuento();
        double precioConDescuento = precioConClase - descuento;
        double costoAsiento = 0.0;
        if (asiento != null && esSeleccionManual) {
            costoAsiento = asiento.getPrecioExtra();
        }
        return precioConDescuento + costoAsiento;
    }
    
    // Este método lo usará la Interfaz Gráfica cuando el usuario haga clic en otro botón
    public void cambiarAsiento(Asiento nuevoAsiento) {
        // Validación asiento disponible
        if (!nuevoAsiento.isDisponible()) {
            System.out.println("Error: El asiento " + nuevoAsiento.getCodigo() + " ya está ocupado.");
            return; 
        }

        // 1. Soltar el asiento viejo
        if (this.asiento != null) {
            this.asiento.liberarAsiento();
            System.out.println("Asiento liberado: " + this.asiento.getCodigo());
        }

        // 2. Agarrar el asiento nuevo
        nuevoAsiento.ocuparAsiento();
        this.asiento = nuevoAsiento;

        // 3. Marcar que ahora ES MANUAL 
        this.esSeleccionManual = true; 
        
        System.out.println("Nuevo asiento asignado: " + this.asiento.getCodigo());
    }

    @Override
    public String toString() {
        return String.format("Ticket para %s en %s (%s) - Asiento %s: $%.2f",
                pasajero.getNombre(),
                vuelo.getRuta().toString(),
                tipoVuelo.getNombre(),
                (asiento != null ? asiento.getCodigo() : "Sin Asignar"),
                calcularSubtotal());
    }
    
}
