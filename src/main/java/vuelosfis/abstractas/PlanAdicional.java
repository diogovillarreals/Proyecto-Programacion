
package vuelosfis.abstractas;


public abstract class PlanAdicional {
   protected String nombrePlan;
   protected double costoAdicional;

    public PlanAdicional(String nombrePlan, double costoAdicional) {
        this.nombrePlan = nombrePlan;
        this.costoAdicional = costoAdicional;
    }

    public String getNombrePlan() {
        return nombrePlan;
    }

    public double getCostoAdicional() {
        return costoAdicional;
    }
   
   public abstract double calcularTotal(int numeroPasajeros);
}
