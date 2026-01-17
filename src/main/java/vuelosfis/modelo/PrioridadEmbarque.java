
package vuelosfis.modelo;

import vuelosfis.abstractas.PlanAdicional;


public class PrioridadEmbarque extends PlanAdicional{
    public PrioridadEmbarque() {
        super("Prioridad de Embarque", 15.00); // $15 por persona
    }

    @Override
    public double calcularTotal(int numeroPasajeros) {
        return costoAdicional * numeroPasajeros;
    }
}
