/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.ArrayList;

/**
 *
 * @author USUARIO
 */
public class Nodo_expresion {
    boolean completo;
    String Simbolo_Estado;
    String expresion;
    String[] alfabeto;
    String[] Estado;
    String[] Simbolo;
    int a;

    public Nodo_expresion(String estado, Estado[] transiciones, String[] alfabeto) {
        this.alfabeto = alfabeto;
        completo = false;
        Simbolo_Estado = estado;
        expresion = "";
        ArrayList<String> estados = new ArrayList<String>();
        ArrayList<String> simbolos = new ArrayList<String>(); 
        for (int i = 0; i < transiciones.length ; i++) {
            String simbolo_estado = transiciones[i].getSimbolo();
            if(estados.contains(simbolo_estado)){
                int posicion = estados.indexOf(simbolo_estado);
                String aux = simbolos.get(posicion)+ "+"+alfabeto[i];
                simbolos.set(posicion, aux);
            }else{
                estados.add(simbolo_estado);
                simbolos.add(alfabeto[i]);
            }
        }
        Estado = new String[estados.size()+1];
        Simbolo = new String[estados.size()+1];
        for (int i = 0; i < estados.size(); i++) {
            Estado[i] = estados.get(i);
            Simbolo[i] = simbolos.get(i);
        }
        Estado[estados.size()] = "";
        Simbolo[estados.size()] = "";
       Verificar_completo();
    }
    
    public void Verificar_completo(){
        int posicion =-1;
        for (int i = 0; i < Estado.length; i++) {
            if(Estado[i].equals(Simbolo_Estado)){
                posicion =i;
                i = Estado.length;
            }
        }
        if(posicion != -1){
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
        
        if(Estado.length == 1){
                completo = true;
                expresion = Simbolo[0];
        }
    }
    
    public void Operar(Nodo_expresion Combinar){
        if(completo != true){
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
                        Simbolo2[j] =    agregar + Simbolo2[j];    
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
            Verificar_completo();
        }
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
