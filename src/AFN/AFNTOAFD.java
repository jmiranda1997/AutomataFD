/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AFN;

import Clases.Automata;
import Clases.Estado;
import java.util.ArrayList;

/**
 * Esta clase se encargará de convertir un Automata Finito NO Determinista (AFN) a un Automata Finito Determinista (AFD) 
 * tomando en cuenta que el AFN es obtenido por el metodo de este mismo paquete y genera un AFD de la calse Automata
 * @author jonathan Miranda j.miranda1997@gmail.com
 * @see AFN
 * @version 1.0
 */
public final class AFNTOAFD {
    private AFN AFN;
    private Automata automata;
    private ArrayList<ConjuntosEstadosAFN> estados;
    private ArrayList[] tabla;
    private String[] alfabeto;
    private EstadoAFN[] estadosAFN;
    /**
     * Constructor General de la clase no recibe ningun atributo de igual manera se pueden usar los metodos SET y GET de la case para los atributos 
     * @see #AFNTOAFD(AFN.AFN)
     */
    public AFNTOAFD() {this.AFN = null; this.automata = null;}
    /**
     * Constructor que recibe el AFN y inicializa el metodo que convierte el automata AFN a AFD  
     * @param automataFN 
     */
    public AFNTOAFD(AFN automataFN) {this.AFN = automataFN; this.automata = null; toAFD(); toAutomata();}
    /**
     * 
     * @return 
     */
    public AFN getAFN() { return AFN;}
    /**
     * 
     * @return 
     */
    public Automata getAutomata() {return automata;}
    /**
     * 
     * @param AFN 
     */
    public void setAFN(AFN AFN) { this.AFN = AFN;}
    /**
     * Metodo para asignar valor al AFD 
     * @param Automata 
     * @deprecated 
     */
    public void setAutomata(Automata Automata) {this.automata = Automata;}
    
    /**
     * 
     */
    public void toAFD(){
        alfabeto = AFN.getAlfabeto();
        inicializarTabla();
        estadosAFN = AFN.getEstados();
        ArrayList<EstadoAFN> conjuntoTemp = null;
        /*Se crea el primer conjunto de estados apartir de el estado inicial (el cual tambien sera estado inicial) se guarda en el array list
        *y se almacena en la tabla de transiciones 
        */
        int contEstados = 0, contTE = 0;
        boolean Fin = true;
        conjuntoTemp = estadosAFN[0].getTransicionesNulas();
        estados.add(new ConjuntosEstadosAFN("E" + contEstados,  conjuntoTemp, esFinal(conjuntoTemp)));
        
        contTE++;

        while (Fin) {
            tabla[contEstados].add(estados.get(contTE).getNombre());
            conjuntoTemp = null;
            for (int i = 1; i < alfabeto.length +1 ; i++) {
                conjuntoTemp = transiciones(estados.get(contEstados), i - 1);
                if (!existe(conjuntoTemp)) {
                    estados.add(new ConjuntosEstadosAFN("E" + contTE, conjuntoTemp, esFinal(conjuntoTemp)));
                    contTE++;
                    tabla[i].add(estados.get(contTE).getNombre());
                }else{
                    tabla[i].add(busqueda(conjuntoTemp));
                }
            }
            contEstados++;
            
            
            if (contEstados > contTE) {
                Fin = false;
            }
        }
    }
    /**
     * 
     */   
    private void inicializarTabla(){
        int cantidadSimbolos = this.AFN.getCantidadSimbolos();
        this.tabla = new ArrayList[cantidadSimbolos+1];
        for (int i = 0; i <= cantidadSimbolos ; i++) {
            tabla[i] = new ArrayList<String>();
        }
    }
    private boolean esFinal(ArrayList<EstadoAFN> Estados){
        boolean Final = false;
        
        for(EstadoAFN Estados1 : Estados){
            if (Estados1.esAceptable()) {
                Final = true;
                return Final;
            }
        }
        
        return Final;
    } 
    private boolean existe(ArrayList<EstadoAFN> Estados){
      for(ConjuntosEstadosAFN Conjuto1 : estados){
        if(!Conjuto1.equivalencia(Estados)){
            return false;
        }
      }
      
      return true;
    }
    /*
    private ArrayList<EstadoAFN> transiciones(ConjuntosEstadosAFN conjunto){
        ArrayList<EstadoAFN> nuevoArray = new ArrayList<EstadoAFN>(), AUX;
        
        AUX = conjunto.getEstados();
        
        for (EstadoAFN Estados1 : AUX){
            if(!nuevoArray.contains(Estados1)) nuevoArray.add(Estados1);
            ArrayList<EstadoAFN> AUX2 = Estados1.getTransicionesNulas();
            if (AUX2 != null){
                for (EstadoAFN AUX21 : AUX2) {
                    if(!nuevoArray.contains(AUX21)) nuevoArray.add(AUX21);
                }
            }
        }
        
        return nuevoArray;
    }*/
    
    private ArrayList<EstadoAFN> transiciones(ConjuntosEstadosAFN conjunto, int index){
        ArrayList<EstadoAFN> nuevoArray = new ArrayList<EstadoAFN>(), AUX;
        
        AUX = conjunto.getEstados();
        
        for (EstadoAFN Estados1 : AUX){
            ArrayList<EstadoAFN> AUX2 = Estados1.getTransiciones(index);
            if (AUX2 != null){
                for (EstadoAFN AUX21 : AUX2) {
                    if(!nuevoArray.contains(AUX21)) nuevoArray.add(AUX21);
                }
            }
        }
        
        return nuevoArray;
    }
    
    private String busqueda(ArrayList<EstadoAFN> Estados){
        for (int cont = 0; cont < estados.size(); cont++){
            if (estados.get(cont).getEstados().equals(Estados)) {
                return estados.get(cont).getNombre();
            }
        }
        return "";
    }
    
    public void toAutomata(){
        int cantEstados = estados.size(), tamañoAlfabeto = alfabeto.length;
        
        automata = new Automata(alfabeto, cantEstados);
        
        Estado[] estadosFinales= new Estado[cantEstados];
        for (int i = 0; i < cantEstados; i++) {
            estadosFinales[i] = new Estado(tamañoAlfabeto);
        }
        for (int i = 0; i < cantEstados; i++) {
            estadosFinales[i].setAceptable(estados.get(i).isFinal());
            estadosFinales[i].setSimbolo(estados.get(i).getNombre());
        }
        for (int i = 0; i < cantEstados; i++) {
            for (int j = 1; j < tamañoAlfabeto + 1; j++) {
                estadosFinales[i].setTransicion(j - 1, estadosFinales[Integer.parseInt(tabla[i].get(j).toString().replace("E", ""))]);
            }
        }
        automata.setEstados(estadosFinales);
    }

}
