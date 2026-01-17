/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vuelosfis.modelo;

/**
 *
 * @author Diogo
 */
public class Ruta {
    private Ciudad origen;
    private Ciudad destino;
    private int duracionMinutos;

    public Ruta(Ciudad origen, Ciudad destino, int duracionMinutos) {
        this.origen = origen;
        this.destino = destino;
        this.duracionMinutos = duracionMinutos;
    }

    public Ciudad getOrigen() {
        return origen;
    }

    public Ciudad getDestino() {
        return destino;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    @Override
    public String toString() {
        return origen.getCodigo() + "-" + destino.getCodigo();
    }
        
}
