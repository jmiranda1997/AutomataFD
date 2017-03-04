/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

/**
 * Clase que modela un Estado perteneciente a un Autómata Finito Determinista. Un Estado está conformado por su  tipo (Aceptable
 * o No aceptable), así como una transición asociada a cada símbolo dentro del alfabeto. Sobre un Estado, además, se define la
 * 'Función de estado-eventual' N*: SxI*->S, donde S es el conjunto de estados e I es el alfabeto.
 * @author Wilson Xicará
 */
public class Estado {
    private boolean aceptable;
    private Estado[] transiciones;
    private String simbolo;

    /**
     * Constructor que inicializa un estado vacío (con transiciones = null y aceptable = false).
     */
    public Estado() {
        this.aceptable = false;
        this.transiciones = null;
    }
    /**
     * Constructor que inicializa un estado con 'cantidadTransiciones' de transiciones (que es igual a la cantidad de símbolos
     * en el alfabeto) y como estado no aceptable. Cada trnsición apunta a un estado vacío.
     * @param cantidadTransiciones 
     */
    public Estado(int cantidadTransiciones) {
        this.aceptable = false;
        transiciones = new Estado[cantidadTransiciones];
        for(int i=0; i<cantidadTransiciones; i++) transiciones[i] = new Estado();
    }

    /**
     * Función que devuelve el tipo del Estado this.
     * @return 'true' si this es un estado aceptable; 'false' en caso contrario.
     */
    public boolean esAceptable() { return aceptable; }
    /**
     * Función que devuelve la transición cuando en el estado actual, this, sucede el símbolo 'alfabeto[indice]'.
     * @param indice entero que indica la posición dentro del array de estados en donde se encuentra la posición. Dicho
     * valor se obtiene de alfabeto[indice] (que es de donde proviene el símbolo).
     * @return la transición asociada al caracter 'alfabeto[indice]' cuando se está en this (el estado actual).
     */
    public Estado getTransicion(int indice) { return transiciones[indice]; }
    /**
     * Función que devuelve el arreglo de transiciones pertenecientes al Estado this.
     * @return el arreglo de transiciones (que son otros estados).
     */
    public Estado[] getTransiciones() { return transiciones; }
    public String getSimbolo() { return simbolo; }

    /**
     * Método que inserta el tipo de estado que es this ('true' si es aceptable, 'false' si es no aceptable).
     * @param aceptable booleano que indica el tipo de estado que es this.
     */
    public void setAceptable(boolean aceptable) { this.aceptable = aceptable; }
    /**
     * Método que inserta una transición que se produce en este estado cuando sucede el símbolo 'alfabeto[indice]'.
     * @param indice entero asociado a 'alfabeto[indice]', que será el estado destino en 'transiciones[indice]'.
     * @param transicion estado al que se dirige el autómata desde this cuando sucede el par N*(this,alfabeto[indice]).
     */
    public void setTransicion(int indice, Estado transicion) { this.transiciones[indice] = transicion; }
    public void setSimbolo(String simbolo) { this.simbolo = simbolo; }
    
    /**
     * Función que determina si dos estados, this y est, son equivalentes. "Dos estados son equivalentes si y sólo si ambos son
     * estados aceptables o ambos son estados no aceptables".
     * @param est objeto de tipo Estado que se compara con this.
     * @return 'true' si los estados son equivalentes; 'false' en caso contrario.
     */
    public boolean esEquivalente(Estado est) {
        return (aceptable == est.esAceptable());
    }
    /**
     * Función que representa la 'Función de estado-eventual'. Dicha función es: N*(s,r)=t, donde s y t pertenecen al conjunto
     * de estados, y r es un símbolo del alfabeto. Representa la trasición cuando en un estado sucede un caracter.
     * @param posSimbolo índice del símbolo en el alfabeto, que es el mísmo índice de la transición, a la cual se mueve el
     * autómata cuanto sucede un caracter del alfabeto, siendo this el estado actual.
     * @return el siguiente estado a this cuando sucede un caracter (que está en alfabeto[posSimbolo]).
     */
    public Estado siguienteEstado(int posSimbolo) { return transiciones[posSimbolo]; }
}