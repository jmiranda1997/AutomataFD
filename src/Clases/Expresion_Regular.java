/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.ArrayList;

/**
 * Clase que crea la expresion regular de un automata finito determinista
 * @author Hugo Tzul
 */
public class Expresion_Regular {
    String Expresion;
    ecuaciones_argen[] ecuaciones;
    ArrayList<String> sumideros;
    /**
     * Constructor de la clase expresion Regular
     * @param estados arreglo de objetos de tipo Estado el cual contiene las transiciones del automata
     * @param alfabeto arreglo de strings que contiene el alfabeto del automata
     */
    public Expresion_Regular(Estado[] estados, String[] alfabeto) {
        Expresion = "";
        sumideros = new ArrayList<String>();
        ecuaciones = new ecuaciones_argen[estados.length];
        //Identifico que estados son sumideros
        for (int i = 0; i < ecuaciones.length; i++) {
            if(!estados[i].esAceptable()){
            int aux = 0;
            String simbolo  = estados[i].getSimbolo();
            Estado[] transiciones = estados[i].getTransiciones();
            for (int j = 0; j < transiciones.length; j++) {
                
                if(transiciones[j].getSimbolo().equals(simbolo)){aux++;}
            }
            if(aux == alfabeto.length) sumideros.add(simbolo);
                
            }
        }
        
        //Inicializo las ecuaciones de los estados que no son sumideros
        for (int t = 0; t < ecuaciones.length; t++) {
            System.out.println("entro 2 if");
            if(!sumideros.contains(estados[t].getSimbolo())){ 
                ecuaciones[t] = new ecuaciones_argen(estados[t].getSimbolo(), estados[t].getTransiciones(), alfabeto,sumideros,estados[t].esAceptable());
                }
            }
    }
    /**
     * Funcion que realiza las operaciones necesarias para construir la expresion regular
     * @return Retorna un String que contiene la expresion regular del automata
     */
    public String Expresion(){
        boolean listo = false;
        for (int i = 0; i < ecuaciones.length; i++) {
            for (int j = 0; j < ecuaciones.length; j++) {
                if(ecuaciones[i]!= null && ecuaciones[j]!= null){
                    if(i!=j){
                    if(ecuaciones[i].contiene(ecuaciones[j]) && ecuaciones[j].contiene(ecuaciones[i])){
                        ecuaciones[i].Operar(ecuaciones[j]);
                    }
                    }
                }   
            }   
        }
        
        while(listo != true){
            for (int i = 1; i < ecuaciones.length; i++) {
                    if(ecuaciones[0]!= null && ecuaciones[i]!= null)
                    ecuaciones[0].Operar(ecuaciones[i]);    
            }
            if(ecuaciones[0].isCompleto()) listo =true;
        }
        
        return ecuaciones[0].getExpresion();
    }
    
}
