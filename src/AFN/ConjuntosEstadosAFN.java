/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AFN;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author jonathan Miranda
 */
public class ConjuntosEstadosAFN {
    private String Nombre;
    private ArrayList<EstadoAFN> Estados;
    private boolean Final;

    public ConjuntosEstadosAFN(String Nombre, ArrayList<EstadoAFN> Estados, boolean Final) {
        this.Nombre = Nombre;
        this.Final = Final;
        this.Estados = Estados;
    }
    public ConjuntosEstadosAFN() {
        this.Nombre = "";
        this.Estados = null;
        this.Final = false;
    }

    public String getNombre() {
        return Nombre;
    }

    public ArrayList<EstadoAFN> getEstados() {
        return Estados;
    }

    public boolean isFinal() {
        return Final;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public void setEstados(ArrayList<EstadoAFN> Estados) {
        this.Estados = Estados;
    }

    public void setFinal(boolean Final) {
        this.Final = Final;
    }
    public boolean equivalencia(ArrayList<EstadoAFN> arrayDeBusqueda){
        boolean equivalente = true;
        for (EstadoAFN arrayDeBusqueda1 : arrayDeBusqueda) {
            if (!this.Estados.contains(arrayDeBusqueda1)) {
                equivalente = false;
                return equivalente;
            }
        }
        
        return equivalente;
    }
    
  
}
