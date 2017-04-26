/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.ArrayList;

/**
 * Clase que representa las ecuaciones de argen de un estado de un automata.
 * @author Hugo Tzul
 */
public final class ecuaciones_argen {
    boolean completo; // nos dice si la ecuacion esta completa o en otras palabras que ya no contenga referencia de ninguna otra ecuacion.
    String Simbolo_Estado; //representa el simbolo del Estado
    String expresion; // Contiene la expresion regular del estado.
    String[] alfabeto; // contiene el alfabeto del automata.
    String[] Estado; // contiene las transiiones del estado
    String[] Simbolo; // contiene la expresion regular de cada transicion
    int a;
    /**
     * Constructor de la clase 
     * @param estado String que contiene el simbolo que representa el estado.
     * @param transiciones Arreglo de objetos de tipo Estado que contiene las transiciones de un estado.
     * @param alfabeto Arreglo de String que contiene el alfabeto del automata.
     * @param sumideros ArrayList de String que contiene los estados sumideros
     * @param aceptable boolean que nos dice si el estado es aceptable o no
     */
    public ecuaciones_argen(String estado, Estado[] transiciones, String[] alfabeto,ArrayList<String> sumideros,boolean aceptable) {
        //inicializa las variables globalse
        this.alfabeto = alfabeto;
        completo = false;
        Simbolo_Estado = estado;
        expresion = "";
        ArrayList<String> estados = new ArrayList<>();
        ArrayList<String> simbolos = new ArrayList<>();
        
        //construyo las transiciones del estado omitiendo los estados sumideros
        for (int i = 0; i < transiciones.length ; i++) {
            String simbolo_estado = transiciones[i].getSimbolo();
            if(!sumideros.contains(simbolo_estado)){
            if(estados.contains(simbolo_estado)){
                int posicion = estados.indexOf(simbolo_estado);
                String aux = simbolos.get(posicion)+ "+"+alfabeto[i];
                simbolos.set(posicion, aux);
            }else{
                estados.add(simbolo_estado);
                simbolos.add(alfabeto[i]);
            }
            }
        }
        //Si el estado es final se le debe agregar un estado vacio
        if(aceptable == true){
        Estado = new String[estados.size()+1];
        Simbolo = new String[estados.size()+1];
        for (int i = 0; i < estados.size(); i++) {
            Estado[i] = estados.get(i);
            Simbolo[i] = simbolos.get(i);
        }
        
            Estado[estados.size()] = "";
            Simbolo[estados.size()] = "";   
            Verificar_completo(true);
        }
        //Si no solamente se queda con las transiciones que tiene por defecto
        else{
            Estado = new String[estados.size()];
            Simbolo = new String[estados.size()];
            for (int i = 0; i < estados.size(); i++) {
            Estado[i] = estados.get(i);
            Simbolo[i] = simbolos.get(i);
            
           }
           Verificar_completo(false);
        }
         
    }
    
    /**
     * Metodo que verifica que la ecuacion no tenga referencias a otras ecuaciones
     */
    public void Verificar_completo(boolean aceptador){
        int posicion =-1;
        //averiguo si dentro de las transiciones del estado hay uno hacia si mismo
        for (int i = 0; i < Estado.length; i++) {
            if(Estado[i].equals(Simbolo_Estado)){
                posicion =i;
                i = Estado.length;
            }
        }
        //Si existe aplico la ley de argen
        if(posicion != -1){
            if(aceptador){
                String[] Nuevo_Estado  = new String[Estado.length];
                String[] Nuevo_Simbolo = new String[Estado.length];
                String agregar = Simbolo[posicion];
                int aux =0;
                for (int i = 0; i < Estado.length; i++) {
                    if(posicion != i){
                        Nuevo_Estado[aux] = Estado[i];
                        Nuevo_Simbolo[aux] = "("+agregar+")*"+Simbolo[i];
                        aux++;
                    }
                }
                Nuevo_Estado[Estado.length-1] = "";
                Nuevo_Simbolo[Estado.length-1] = "";
                Estado = new String[Nuevo_Estado.length];
                Simbolo = new String[Nuevo_Estado.length];
                Estado = Nuevo_Estado;
                Simbolo = Nuevo_Simbolo;
            }else{
                String[] Nuevo_Estado  = new String[Estado.length-1];
                String[] Nuevo_Simbolo = new String[Estado.length-1];
                String agregar = Simbolo[posicion];
                int aux =0;
                for (int i = 0; i < Estado.length; i++) {
                    if(posicion != i){
                        Nuevo_Estado[aux] = Estado[i];
                        Nuevo_Simbolo[aux] = "("+agregar+")*"+Simbolo[i];
                        aux++;
                    }
                }
                Estado = new String[Nuevo_Estado.length];
                Simbolo = new String[Nuevo_Estado.length];
                Estado = Nuevo_Estado;
                Simbolo = Nuevo_Simbolo;
            }
        }
        
        if(Estado.length == 1 && Estado[0].equals("")){
                completo = true;
                expresion = Simbolo[0];
        }
    }
    
    public void Operar(ecuaciones_argen Combinar){
        //if(completo != true){
            int posicion = -1;
            for (int i = 0; i < Estado.length; i++) {
                if(Combinar.getSimbolo_Estado().equals(Estado[i])){ 
                    posicion = i;
                    i = Estado.length;
                }
            }
            
            if(posicion != -1){
                
                String agregar = Simbolo[posicion];
                    String[] Simbolo2 = Combinar.getSimbolo();
                    String[] Estado2 = Combinar.getEstado();
                    int aux=0;
                    for (int j = 0; j < Simbolo2.length; j++) {
                        Simbolo2[j] =    agregar +"("+ Simbolo2[j]+")";    
                    }
                    ArrayList<String> Nuevo_Estado = new ArrayList<String>();
                    ArrayList<String> Nuevo_Simbolo = new ArrayList<String>();
                     for (int i = 0; i < Estado.length; i++) {
                         if(i!= posicion){
                             Nuevo_Estado.add(Estado[i]);
                             Nuevo_Simbolo.add(Simbolo[i]);
                         }
                         
                    }
                     
                    for (int i = 0; i < Estado2.length; i++) {
                        if(Nuevo_Estado.contains(Estado2[i])){
                             aux = Nuevo_Estado.indexOf(Estado2[i]);
                             System.out.println(a);
                            if(Nuevo_Simbolo.get(aux).length()!=0){
                            String nuevo = "("+Nuevo_Simbolo.get(aux)+"+"+Simbolo2[i]+")";
                            Nuevo_Simbolo.set(aux, nuevo);
                            }
                            else{
                                Nuevo_Simbolo.set(aux, Simbolo2[i]);
                            }
                        }
                        else{
                            Nuevo_Estado.add(Estado2[i]);
                            Nuevo_Simbolo.add(Simbolo2[i]);
                        }
                    }
                    Estado = new String[Nuevo_Estado.size()];
                    Simbolo = new String[Nuevo_Estado.size()];
                    for (int i = 0; i < Estado.length; i++) {
                         Estado[i] = Nuevo_Estado.get(i);
                         Simbolo[i] = Nuevo_Simbolo.get(i);
                }
                    
            }
            Verificar_completo(false);
        //}
    }

    public boolean isCompleto() {
        return completo;
    }

    public String getSimbolo_Estado() {
        return Simbolo_Estado;
    }

    public String getExpresion() {
        return expresion;
    }

    public String[] getEstado() {
        return Estado;
    }

    public String[] getSimbolo() {
        return Simbolo;
    }
    
    
    
    
}
