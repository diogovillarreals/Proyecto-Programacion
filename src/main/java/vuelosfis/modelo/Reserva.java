
package vuelosfis.modelo;

import java.util.ArrayList;
import vuelosfis.abstractas.PlanAdicional;
import vuelosfis.enums.TipoViaje;

public class Reserva {
   private String codigoReserva;
   private TipoViaje tipoViaje; 
   //Una reserva puede tener muchos tickets y muchos planes extras
   private ArrayList<DetalleReserva> listaDetalles;
   private ArrayList<PlanAdicional> listaPlanes;
   
   public Reserva(String codigoReserva, TipoViaje tipoViaje) {
        this.codigoReserva = codigoReserva;
        this.tipoViaje = tipoViaje;
        this.listaDetalles = new ArrayList<>();
        this.listaPlanes = new ArrayList<>();
    }
   
    public String getCodigoReserva() { 
        return codigoReserva; 
    }
    
    public ArrayList<DetalleReserva> getListaDetalles() { 
        return listaDetalles; 
    }

    @Override
    public String toString() {
        return "Reserva " + codigoReserva + " [" + tipoViaje + "] - Total: $" + String.format("%.2f", calcularTotalReserva());
    }
   
    public void agregarDetalle(DetalleReserva detalle) {
        listaDetalles.add(detalle);
    }

    public void agregarPlan(PlanAdicional plan) {
        listaPlanes.add(plan);
    }
    
    public double calcularTotalReserva() {
        double totalTickets = 0.0;
        double totalPlanes = 0.0;
        
        // 1. Sumar los tickets individuales
        for (DetalleReserva detalle : listaDetalles) {
            totalTickets += detalle.calcularSubtotal();
        }
        
        // 2. Sumar los planes adicionales
        // El plan se cobra por ticket emitido).
        int cantidadTickets = listaDetalles.size();
        
        for (PlanAdicional plan : listaPlanes) {
            totalPlanes += plan.calcularTotal(cantidadTickets);
        }
        return totalTickets + totalPlanes;
    }
}
