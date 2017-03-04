/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

/**
 *
 * @author Wilson Xicará
 */
public class IndiceAutomatas {
    private String nombreAutomata, descripcion;
    private int punteroARefDatos, punteroADatos, punteroARefDescripcion, punteroADescripcion;
    
    @Override
    public String toString() {
        return "punteroARefDatos = "+punteroARefDatos+", punteroADatos = "+punteroADatos+", punteroARefDescripcion = "+punteroARefDescripcion+", punteroADescripcion = "+punteroADescripcion;
    }
    public IndiceAutomatas() {
        nombreAutomata = "";
        descripcion = "<Sin descripción>";
        punteroARefDatos = punteroADatos = punteroARefDescripcion = punteroADescripcion = 0;
    }
    public IndiceAutomatas(String nombreAutomata, int punteroAReferencia, int punteroADatos) {
        this.nombreAutomata = nombreAutomata;
        this.punteroADatos = punteroADatos;
        this.punteroARefDatos = punteroAReferencia;
    }

    public String getNombreAutomata() { return nombreAutomata; }
    public String getDescripcion() { return descripcion; }
    public int getPunteroARefDatos() { return punteroARefDatos; }
    public int getPunteroADatos() { return punteroADatos; }
    public int getPunteroADescripcion() { return punteroADescripcion; }
    public int getPunteroARefDescripcion() { return punteroARefDescripcion; }

    public void setNombreAutomata(String nombreAutomata) { this.nombreAutomata = nombreAutomata; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setPunteroARefDatos(int punteroARefDatos) { this.punteroARefDatos = punteroARefDatos; }
    public void setPunteroADatos(int punteroADatos) { this.punteroADatos = punteroADatos; }
    public void setPunteroADescripcion(int punteroADescripcion) { this.punteroADescripcion = punteroADescripcion; }
    public void setPunteroARefDescripcion(int punteroARefDescripcion) { this.punteroARefDescripcion = punteroARefDescripcion; }
    
}
