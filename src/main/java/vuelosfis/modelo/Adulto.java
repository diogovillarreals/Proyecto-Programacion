/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vuelosfis.modelo;

import vuelosfis.interfaces.TipoPasajero;

/**
 *
 * @author Diogo
 */
public class Adulto implements TipoPasajero{
    @Override
    public String getDescripcion() {
        return "Adulto";
    }

    @Override
    public double getDescuento() {
        return 0.0; 
    }
}
