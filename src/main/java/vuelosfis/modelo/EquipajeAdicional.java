
package vuelosfis.modelo;

import vuelosfis.abstractas.PlanAdicional;


public class EquipajeAdicional extends PlanAdicional{
    
    public EquipajeAdicional() {
        super("Maleta Extra (23kg)", 40.00); // $40 por maleta
    }
    
    @Override
    public double calcularTotal(int numeroPasajeros) {
        return costoAdicional * numeroPasajeros; // $40 * 3 personas = $120
    }
}
