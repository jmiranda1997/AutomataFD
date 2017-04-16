/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

/**
 *
 * @author USUARIO
 */
public class Expresion_Regular {
    String Expresion;
    Nodo_expresion[] ecuaciones;

    public Expresion_Regular(Estado[] estados, String[] alfabeto) {
        Expresion = "";
        ecuaciones = new Nodo_expresion[estados.length];
        for (int i = 0; i < ecuaciones.length; i++) {
            ecuaciones[i] = new Nodo_expresion(estados[i].getSimbolo(), estados[i].getTransiciones(), alfabeto);
        }
    }
    
    public String Expresion(){
        boolean listo = false;
        while(listo != true){
            for (int i = 1; i < ecuaciones.length; i++) {
                for (int j = 0; j < ecuaciones.length; j++) {
                    ecuaciones[j].Operar(ecuaciones[i]);
                }
            }
            if(ecuaciones[0].isCompleto()) listo =true;
        }
        
        return ecuaciones[0].getExpresion();
    }
    
}
