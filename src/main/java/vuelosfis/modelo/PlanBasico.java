/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vuelosfis.modelo;

import vuelosfis.abstractas.PlanAdicional;


public class PlanBasico extends PlanAdicional{

    public PlanBasico() {
        super("Plan Basico (Mochila)", 0.0);
    }
    
    @Override
    public double calcularTotal(int numeroPasajeros) {
        return 0.0; 
    }
}
