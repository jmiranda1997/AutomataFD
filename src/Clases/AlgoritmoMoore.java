/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.Arrays;

/**
 * Clase que modela el Algoritmo de Moore (para comprobar si dos Autómatas son equivalentes o no). Para ello, construye el
 * algoritmo en una lista doble enlazada, siempre que cada NodoMoore contenga un par de estados equivalentes. Se trabaja el
 * algoritmo de Moore de la siguiente manera:
 * 1. Se obtiene el par de estados iniciales de cada autómata (este será el primer nodo de la lista).
 * 2. Para cualquier nodo, se obtiene el par (siguienteA1,siguienteA2), en el que N*(estadoA1,r)=siguienteA1 y
 *    N*(estadoA2,r)=siguienteA2 (donde siguienteA1 y siguienteA2 corresponden a los estados pertenecientes a los Autómatas 1
 *    y 2, respectivamente, y 'r' representa el símbolo del alfabeto que genera la 'función de estado-eventual') y se busca en
 *    toda la lista doble; si el par (siguienteA1,siguienteA2) aún existe en la lista se agrega al final, de lo contrario ya hubo
 *    otro par que llegó hasta él.
 * 3. Se realiza el paso 2 para cada símbolo del alfabeto.
 * 4. Se avanza al siguiente nodo de la lista doble, repitiendo los pasos 2 y 3.
 * 5. Al llegar al final de la lista ya se habrá probado todos los símbolos del alfabeto asociado a cada par prosible de
 *    transiciones dentro de los autómatas.
 * 6. Si en ningún caso se obtiene un par de estados no equivalente, entonces los autómatas son equivalentes. Se dice que dos
 *    estados son equivalentes sí y sólo si ambos son estados aceptables o ambos son estados no aceptables.
 * @author Wilson Xicará
 */
public class AlgoritmoMoore {
    private NodoMoore inicio, fin;
    private int cantidadSimbolos;

    /**
     * Constructor que inicializa un nuevo objeto para comparar dos autómatas a través del algoritmo de Moore. Inicializa la
     * lista doble vacía y la cantidad de símbolos (que tiene el alfabeto de alguno de los autómatas) como 0.
     */
    public AlgoritmoMoore() {
        inicio = new NodoMoore();
        fin = new NodoMoore();
        inicio.setSiguiente(fin);
        fin.setAnterior(inicio);
        cantidadSimbolos = 0;
    }
    /**
     * Función que compara dos Autómatas, a través del Algoritmo de Moore, y devuelve si son equivalentes o no.
     * @param A1 objeto que representa al primer Autómata.
     * @param A2 objeto que representa al segundo Autómata.
     * @return 'false' si ambos autómatas tienen distinta cantidad de símbolos en su alfabeto, si sus alfabetos son diferentes o
     * si en el proceso alguno de los estados de un par no son equivalentes (lo que implica que los autómatas nos son
     * equivalentes). 'true' si ambos autómatas son equivalentes.
     */
    public boolean compararAutomatas(Automata A1, Automata A2) {
        // Si tienen diferente cantidad de símbolos o los alfabetos son distintos: Autómatas no equivalentes
        if (A1.getCantidadSimbolos() != A2.getCantidadSimbolos() || !Arrays.equals(A1.getAlfabeto(), A2.getAlfabeto()))
            return false;
        // Si sus estados iniciales no son equivalentes entonces los Autómatas no son equivalentes
        if (!A1.getEstadoInicial().esEquivalente(A2.getEstadoInicial()))
            return false;
        // Sino, puede que sean equivalentes
        boolean sonEquivalentes = true;
        cantidadSimbolos = A1.getCantidadSimbolos();
        /* No es necesario obtener el alfabeto ya que para cada transición N*(s,r)=t, donde 's' y 't' son estados y 'r' es un
           símbolo del alfabeto, no es necesario conocer 'r' (pues se sabe que tiene una posición definita dentro del alfabeto)
           sólo su posición 'i': por lo que N*(s,r)=t -> N*(s,[i])=t (donde '[i]' es la posición del símbolo 'r' en el alfabeto)*/
        // Enlazo el primer par a la lista doble enlazada
        NodoMoore primerPar = new NodoMoore(A1.getEstadoInicial(), A2.getEstadoInicial(), inicio, fin);
        inicio.setSiguiente(primerPar);
        fin.setAnterior(primerPar);
        
        NodoMoore iterador = inicio.getSiguiente(); // Al inicio, iterador es el primer par dentro de la lista doble
        boolean pasar = false;
        while (pasar == false) {
            for (int contSimbolos=0; contSimbolos<cantidadSimbolos; contSimbolos++) {
                // Se define el siguiente par de estados, que son los siguientes en ambos autómatas
                Estado siguienteA1 = iterador.getEstado1().siguienteEstado(contSimbolos);
                Estado siguienteA2 = iterador.getEstado2().siguienteEstado(contSimbolos);
                // Si los estados son equivalentes entonces siguo verificando
                if (siguienteA1.esEquivalente(siguienteA2) == true) {
                    // Busco si el par existe en la lista doble: se retorna el nodo que lo contiene, o 'fin' (si no existe)
                    NodoMoore buscador = buscarNodo(siguienteA1, siguienteA2);
                    // Si retorna 'fin':
                    if (buscador == fin) {
                        NodoMoore nuevoPar = new NodoMoore(siguienteA1, siguienteA2, fin.getAnterior(), fin);
                        fin.getAnterior().setSiguiente(nuevoPar);
                        fin.setAnterior(nuevoPar);
                    }
                    // Si retorna un par != 'fin': el par ya existe
                }
                else {
                    sonEquivalentes = false;
                    pasar = true;
                }
            }
            iterador = iterador.getSiguiente();
            if (iterador == fin)
                pasar = true;
        }
        return sonEquivalentes;
    }
    /**
     * Función que busca el nodo con el par (estadoA1, estadoA2) (perteneciente al Autómata 1 y al Autómata 2, respectivamente).
     * Si encuentra el par, lo retorna; en caso contrario retorna 'fin'.
     * @param estadoA1 estado que pertenece al Autómata 1.
     * @param estadoA2 estado que pertenece al Autómata 2.
     * @return el nodo que contiene al par (estadoA1,estadoA2), si lo encuentra en la lista doble; en caso contrario retorna 'fin'.
     */
    private NodoMoore buscarNodo(Estado estadoA1, Estado estadoA2) {
        NodoMoore iterador = inicio.getSiguiente();
        boolean pasar = false;
        while (pasar == false) {
            if (iterador.getEstado1() == estadoA1 && iterador.getEstado2() == estadoA2)
                pasar = true;
            else
                iterador = iterador.getSiguiente();
            if (iterador == fin)
                pasar = true;
        }
        return iterador;
    }
    /**
     * Clase que modela un nodo para contruir el Algoritmo de Moore. Dicho nodo está compuesto por un par (estadoA1, estadoA2) que
     * son estados pertenecientes al Autómata 1 y al Autómata 2, respectivamente. Además, tiene dos referencias a nodos del mismo
     * tipo, 'anterior' y 'siguiente' para construir el algoritmo como una lista doble enlazada.
     * @author Wilson Xicará
     */
    public class NodoMoore {
        private Estado estado1, estado2;
        private NodoMoore anterior, siguiente;

        /**
         * Constructor vacío que inicializa un nuevo nodo con sus estados y referencias nulos.
         */
        public NodoMoore() {
            estado1 = estado2 = null;
            anterior = siguiente = null;
        }
        /**
         * Constructor que inicializa un nuevo nodo con los parámetros especificados.
         * @param estado1 estado perteneciente al Autómata 1.
         * @param estado2 estado perteneciente al Autómata 2.
         * @param anterior nodo que será el anterior a this.
         * @param siguiente nodo que será el siguiente a this.
         */
        public NodoMoore(Estado estado1, Estado estado2, NodoMoore anterior, NodoMoore siguiente) {
            this.estado1 = estado1;
            this.estado2 = estado2;
            this.anterior = anterior;
            this.siguiente = siguiente;
        }

        /**
         * Función que devuelve del par el Estado perteneciente al Autómata 1.
         * @return el estado contenido en el Autómata 1.
         */
        public Estado getEstado1() { return estado1; }
        /**
         * Función que devuelve del par el Estado perteneciente al Autómata 2.
         * @return el estado contenido en el Autómata 2.
         */
        public Estado getEstado2() { return estado2; }
        /**
         * Función que devuelve el nodo anterior a this.
         * @return el nodo anterior a this.
         */
        public NodoMoore getAnterior() { return anterior; }
        /**
         * Función que devuelve el nodo siguiente a this.
         * @return el nodo siguiente a this.
         */
        public NodoMoore getSiguiente() { return siguiente; }

        /**
         * Método que asigna al par de estados el estado perteneciente al Autómata 1.
         * @param estado1 el estado que pertenece al Autómata 1.
         */
        public void setEstado1(Estado estado1) { this.estado1 = estado1; }
        /**
         * Método que asigna al par de estados el estado perteneciente al Autómata 2.
         * @param estado2 el estado que pertenece al Autómata 2.
         */
        public void setEstado2(Estado estado2) { this.estado2 = estado2; }
        /**
         * Método que asigna el nodo que será el anterior a this (en la lista doble).
         * @param anterior nodo que irá antes de this.
         */
        public void setAnterior(NodoMoore anterior) { this.anterior = anterior; }
        /**
         * Método que asigna el nodo que será el siguiente a this (en la lista doble).
         * @param siguiente nodo que irá después de this.
         */
        public void setSiguiente(NodoMoore siguiente) { this.siguiente = siguiente; }
    }
}
