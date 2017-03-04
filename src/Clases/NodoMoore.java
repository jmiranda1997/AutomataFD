/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

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
