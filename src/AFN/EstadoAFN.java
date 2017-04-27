/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AFN;

import java.util.ArrayList;

/**
 * Clase que modela un Estado perteneciente a un Autómata Finito No Determinista (AFN). Los estados de un AFN están conformados por:
 * - Un tipo (final o no final).
 * - Ninguna, una o varias transiciones para la cadena vacía.
 * - Ninguna, una o varias transiciones para cada símbolo del alfabeto.
 * También se define la 'Función de estado-eventual' N*: SxI*->S, donde S es el conjunto de estados e I es el alfabeto (incluido la cadena vacía).
 * @author Wilson Xicará
 */
public class EstadoAFN {
    private boolean aceptable;
    private ArrayList[] transiciones;
//    private ArrayList<EstadoAFN[]> transiciones;
    private String nombre;

    /**
     * Constructor que inicializa un estado vacío (con transiciones = null y aceptable = false).
     */
    public EstadoAFN() {
        this.aceptable = false;
        this.transiciones = null;
    }
    /**
     * Constructor que inicializa un estado con 'cantidadSimbolos + 1' de transiciones (que es igual a la cantidad de
     * símbolos en el alfabeto más la cadena nula) y como estado no aceptable. Cada transición apunta a un estado vacío.
     * @param cantidadSimbolos entero que indica la cantidad de símbolos que tiene el alfabeto (no incluiye el caracter nulo).
     */
    public EstadoAFN(int cantidadSimbolos) {
        this.aceptable = false;
        this.transiciones = new ArrayList[cantidadSimbolos + 1];
        for(int i=0; i<=cantidadSimbolos; i++)
            this.transiciones[i] = new ArrayList<EstadoAFN>(); // Agrego las 'cantidadSimbolos + 1' filas que tendrá el estado (no inicializo los arreglos)
        /* Las transiciones para el caracter nulo se encontrarán en la última posición del arreglo */
    }

    /**
     * Función que devuelve el tipo del EstadoAFN actual.
     * @return 'true' si this es un estado final; 'false' en caso contrario.
     */
    public boolean esAceptable() { return aceptable; }
    /**
     * Función que devuelve la transición cuando en el estado actual, this, sucede el símbolo 'alfabeto[indice]'. Debido a que
     * en el AFN puede haber cero o más transiciones para un símbolo del alfabeto, es necesario especificar dicha transición.
     * @param indexSimbolo entero que indica la posición dentro del array de estados en donde se encuentra la transición. Dicho
     * valor se obtiene de 'alfabeto[indice]' (que es de donde proviene el símbolo).
     * @param indexTransicion entero que indica a cual de todas las transiciones asociadas al caracter se hace referencia.
     * @return la 'indexTransicion' transición asociada al caracter 'alfabeto[indexSimbolo]' cuando se está en this (el estado actual).
     */
    public EstadoAFN getTransicion(int indexSimbolo, int indexTransicion) { return (EstadoAFN)transiciones[indexSimbolo].get(indexTransicion); }
    /**
     * Función que devuelve la index-ésima transición del estado actual con la cadena vacía.
     * @param index entero que identifica a cuál de todos los estados a los que se puede llegar, desde el estado actual, con
     * la cadena vacía se quiere acceder.
     * @return el estado específico al que se accede desde el estado actual con la cadena vacía.
     */
    public EstadoAFN getTransicionNula(int index) { return (EstadoAFN)this.transiciones[this.transiciones.length-1].get(index); }
    /**
     * Función que devuelve todas las transiciones asociadas al símbolo 'alfabeto[index]' pertenecientes a este EstadoAFN.
     * @param indexSimbolo entero que indica el índice del símbolo dentro del alfabeto.
     * @return el arreglo de transiciones (que son otros estados) asociados al símbolo especificado.
     */
    public ArrayList<EstadoAFN> getTransiciones(int indexSimbolo) { return this.transiciones[indexSimbolo]; }
    /**
     * Función que devuelve todas las transiciones asociadas a la cadena vacía pertenecientes a este EstadoAFN.
     * @return el arreglo de transiciones (que son otros estados) asociados a la cadena vacía.
     */
    public ArrayList<EstadoAFN> getTransicionesNulas(){return this.transiciones[this.transiciones.length-1];}
    public ArrayList<EstadoAFN> cerradura;
    public ArrayList<EstadoAFN> getCerradura() { 
         cerradura = new ArrayList<EstadoAFN>();
         
         ArrayList<EstadoAFN> aux = this.transiciones[this.transiciones.length-1];
         cerraduraKleen(aux);
         
         if(!cerradura.contains(this)) cerradura.add(this);
         
         return cerradura;
    }
  //  ArrayList<EstadoAFN> ya 
            private void cerraduraKleen(ArrayList<EstadoAFN> Array){
         if(!Array.isEmpty()) {
             
            for(EstadoAFN aux : Array) {
                if(!cerradura.contains(aux)){
               ArrayList<EstadoAFN> aux2 = aux.transiciones[aux.transiciones.length-1];
                for (EstadoAFN aux21: aux2) {
                    if(!cerradura.contains(aux21)){ cerradura.add(aux21);}
                }
                if(!cerradura.contains(aux)) cerradura.add(aux);
               if(!aux2.isEmpty()) cerraduraKleen(aux2);  }
            } 
         }
    }
    /*public boolean nulas(){
        boolean nul= false
    }*/
    /**
     * Función que devuelve el nombre de este EstadoAFN.
     * @return el nombre con el que se identifica este estado.
     */
    public String getNombre() { return nombre; }

    /**
     * Método que inserta el tipo de estado que es this ('true' si es aceptable, 'false' si es no aceptable).
     * @param aceptable booleano que indica el tipo de estado que es this.
     */
    public void setAceptable(boolean aceptable) { this.aceptable = aceptable; }
    /**
     * Método que inserta una transición que se produce en este estado cuando sucede el símbolo 'alfabeto[indexSimbolo]'.
     * @param indexSimbolo entero asociado a 'alfabeto[indexSimbolo]', que será el estado destino en 'transiciones[indexSimbolo]'.
     * @param estado estado al que se dirige el autómata desde this cuando sucede el par N*(this,alfabeto[indexSimbolo]).
     */
    public void setTransicion(int indexSimbolo, EstadoAFN estado) { this.transiciones[indexSimbolo].add(estado); }
    /**
     * Método que inserta una nueva transición asociada al caracter nulo para este estado.
     * @param estado estado al que se puede acceder desde el estado actual con el caracter nulo.
     */
    public void setTransicionNula(EstadoAFN estado) { this.transiciones[this.transiciones.length-1].add(estado); }
    /**
     * Método que inserta un ArrayList con todos los estados a los que el estado actual puede dirigirse cuando sucede el
     * símbolo 'alfabeto[indexSimbolo]'.
     * @param indexSimbolo referencia al símbolo dentro de 'alfabeto[indexSimbolo]'.
     * @param transiciones todas las posibles transiciones que el estado actual puede ir con el símbolo 'alfabeto[indexSimbolo]'.
     */
    public void setTransiciones(int indexSimbolo, ArrayList<EstadoAFN> transiciones) { this.transiciones[indexSimbolo] = transiciones; }
    /**
     * Método que inserta un ArrayList con todos los estados a los que se puede acceder, desde el estado actual, con el caracter nulo.
     * @param estados estados a los que se puede llegar con la cadena vacía.
     */
    public void setTransicionesNulas(ArrayList<EstadoAFN> estados) { this.transiciones[this.transiciones.length-1] = estados; }
    /**
     * Método que inserta una cadena para identificar al estado actual.
     * @param nombre String que será el nombre del estado actual.
     */
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    /**
     * Función que determina si dos estados,this y est, son equivalentes. "Dos estados son equivalentes si y sólo si ambos
     * son estados finales o ambos son estados no finales".
     * @param est objeto de tipo EstadoAFN que se compara con this.
     * @return 'true' si los estados son equivalentes; 'false' en caso contrario.
     */
    public boolean esEquivalente(EstadoAFN est) { return (aceptable == est.esAceptable()); }
    /**
     * Función que representa la 'Función de estado-eventual'. Dicha función es: N*(s,r)=t, donde s y t pertenecen al conjunto
     * de estados, y r es un símbolo del alfabeto. Representa la trasición cuando en un estado sucede un caracter. Debido a que
     * en el AFN hay cero o varias transiciones con un símbolo, es necesario especificar a cuál de todos se quiere llegar.
     * @param indexSimbolo índice del símbolo en el alfabeto, que es el mísmo índice de la transición, a la cual se mueve el
     * autómata cuanto sucede un caracter del alfabeto, siendo this el estado actual.
     * @param indexEstado índice del estado al que se quiere llegar con el símbolo 'alfabeto[indexSimbolo]'.
     * @return el siguiente estado a this cuando sucede un caracter (que está en alfabeto[indexSimbolo]).
     */
    public EstadoAFN siguienteEstado(int indexSimbolo, int indexEstado) { return (EstadoAFN)this.transiciones[indexSimbolo].get(indexEstado); }
    /**
     * Función que representa la 'Función de estado-eventual' para la cadena vacía. Dicha función es: N*(s,e)=t, donde s y t
     * pertenecen al conjunto de estados, y e es el símbolo nulo o la cadena vacía. Debido a que en el AFN hay cero o varias
     * transiciones con con la cadena vacía, es necesario especificar a cuál de todos se quiere llegar.
     * @param indexEstado índice del estado al que se quiere llegar con el símbolo nulo (o la cadena vacía).
     * @return el siguiente estado a this cuando sucede un caracter (que está en alfabeto[indexSimbolo]).
     */
    public EstadoAFN siguienteEstadoNulo(int indexEstado) { return (EstadoAFN)this.transiciones[this.transiciones.length-1].get(indexEstado); }
}