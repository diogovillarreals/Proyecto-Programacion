package vuelosfis.modelo;

public class Asiento {

    private String codigo;
    private int fila;
    private String columna;
    private double precioExtra;
    private boolean disponible;

    public Asiento(String codigo, int fila, String columna, double precioExtra) {
        this.codigo = codigo;
        this.fila = fila;
        this.columna = columna;
        this.precioExtra = precioExtra;
        this.disponible = true;
    }

    public void ocuparAsiento() {
        this.disponible = false;
    }

    public boolean isDisponible() {
        return disponible;
    }
    
    public void liberarAsiento() {
        this.disponible = true;
    }
    
    public String getCodigo() {
        return codigo;
    }

    public double getPrecioExtra() {
        return precioExtra;
    }
    
    public int getFila() { 
        return fila; 
    }
    
    public String getColumna() { 
        return columna; 
    }
    
}
