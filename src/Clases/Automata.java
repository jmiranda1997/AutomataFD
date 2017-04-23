/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que modela un Autómata Finito Determinista. Un AFD consiste en 5 objetos:
 * 1) Un conjunto finito I de símbolos de entrada (el alfabeto).
 * 2) Un conjunto finito S de estados en los que puede estar el autómata.
 * 3) Un estado, denotado por s0, llamado el estado inicial.
 * 4) Un determinado conjunto de estados F llamado estados aceptables (donde F es un subconjunto de S, F c S).
 * 5) Una función de estado próximo N:SxI->S que asocia un 'estado siguiente' a cada par ordenado que consiste en un 'estado
 *    presente' y una 'entrada presente'.
 * @author Wilson Xicará
 */
public class Automata {
    private String nombre, descripcion;
    private String[] alfabeto, nombresEstados;
    private Estado[] estados;
    private int cantidadSimbolos, cantidadEstados;
    
    public Estado[] getEstados() { return this.estados; }
    public String getNombre() { return nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getDescripcion() { return descripcion; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getLongitudNombreEstados() {
        if (nombresEstados[0].equals("") == true)
            getNombresEstados();
        int longitud = nombresEstados[0].length();
        for(int i=1; i<cantidadEstados; i++) {
            if (nombresEstados[i].length() > longitud)
                longitud = nombresEstados[i].length();
        }
        return longitud;
    }
    public int getCantidadEstados() { return cantidadEstados; }
    public String[] getNombresEstados() {
        if (nombresEstados[0].equals("") == true)
            for(int i=0; i<cantidadEstados; i++)
                nombresEstados[i] = estados[i].getSimbolo();
        return nombresEstados;
    }
    public int getIndiceEstadoInicial() {
        Estado iterador;
        for(int indice=0; indice<cantidadEstados; indice++) {
            iterador = estados[indice];
            if (iterador == getEstadoInicial())
                return indice;
        }
        return 0;
    }
    public int[][] getMatrizTransiciones() {
        int[][] matriz = new int[cantidadEstados][cantidadSimbolos];
        Estado iterador, siguiente;
        for(int fil=0; fil<cantidadEstados; fil++) {
            iterador = estados[fil];
            for(int col=0; col<cantidadSimbolos; col++) {
                siguiente = iterador.siguienteEstado(col);
                for(int busc=0; busc<cantidadEstados; busc++) {
                    if (estados[busc] == siguiente) {
                        matriz[fil][col] = busc;
                        busc = cantidadEstados;
                    }
                }
                
            }
        }
        return matriz;
    }
    public void setEstados(Estado[] estados) { this.estados = estados; }
    public void setNombresEstados(String[] nombresEstados) {
        this.nombresEstados = nombresEstados;
        for(int i=0; i<cantidadEstados; i++)
            estados[i].setSimbolo(this.nombresEstados[i]);
    }
    
    
    
    /**
     * Constructor que inicializa un Autómata vacío (esto es, un alfabeto vacío y cero estados).
     */
    public Automata() {
        this.alfabeto = this.nombresEstados = null;
        this.estados = null;
        this.nombre = null;
        this.cantidadSimbolos = this.cantidadEstados = 0;
    }
    /**
     * Constructor que inicializa un Autómata con un alfabeto y con n-estados (según los parámetros, para ambos). Los Estados
     * dentro del Autómata se manejan como un arreglo de Estados, donde estados[0] es el estado inicial; cada estado dentro
     * del arreglo se inicializa con 'cantidadSimbolos' transiciones (todos apuntado a estados vacíos).
     * @param alfabeto arreglo de String que representa el alfabeto de este Autómata.
     * @param cantidadEstados entero que indica la cantidad de estados que tendrá el Autómata.
     */
    public Automata(String[] alfabeto, int cantidadEstados) {
        this.alfabeto = alfabeto;
        this.cantidadSimbolos = alfabeto.length;
        this.cantidadEstados = cantidadEstados;
        this.estados = new Estado[cantidadEstados];
        for(int i=0; i<cantidadEstados; i++)
            this.estados[i] = new Estado(this.cantidadSimbolos);
        this.nombresEstados = new String[cantidadEstados];
        for(int i=0; i<cantidadEstados; i++) this.nombresEstados[i] = "";
    }
    
    /**
     * Método que permite la construcción del Autómata; esto es la definición de cada transición (asociada a cada uno de los
     * estados) asociado a cada símbolo dentro del alfabeto, así como el tipo de estado (aceptable o no aceptable). Se define
     * el Estado Inicial como estados[0]. Lo anterior se realiza desde entrada del teclado.
     * @param teclado objeto que permite la captura de texto desde el teclado.
     */
    public void definirTransiciones(BufferedReader teclado) {
        int estado, tipo;
        Estado iterador;
        try {
            for(int contEstados=0; contEstados<cantidadEstados; contEstados++) {
                iterador = estados[contEstados];
                System.out.print("s"+contEstados+" es aceptable: ");  tipo = Integer.parseInt(teclado.readLine());
                iterador.setAceptable(tipo == 1);
                for(int cantSimb=0; cantSimb<cantidadSimbolos; cantSimb++) {
                    System.out.print("N(s"+contEstados+","+alfabeto[cantSimb]+") = s");  estado = Integer.parseInt(teclado.readLine());
                    iterador.getTransiciones()[cantSimb] = estados[estado];
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Automata.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Función que determina si 'cadena' pertenece al Lenguaje que implementa el Autómata. Para ello, prueba cada uno de los caracteres
     * que conforman 'cadena' y realiza la transición correspondiente al estado-símbolo. "Se dice que una cadena pertenece al
     * lenguaje definido por el autómata si y solo sí el autómata va a un estado aceptable cuando los símbolos de 'cadena' son
     * entrada para el autómata en suceción de izquierda a derecha, empezando cuando el autómata está en un estado inicial".
     * @param cadena suseción de símbolos (caracteres) que se prueban en el autómata.
     * @return 'true' si 'cadena' pertenece al lenguaje del autómata; 'false' en caso contrario.
     */
    public boolean probarCadena(String cadena) {
        Estado iterador = estados[0];   // iterador se incia como el Estado Inicial.
        int longitudCadena = cadena.length();
        
        for(int i=0; i<longitudCadena; i++) {
            String simbolo = ""+cadena.charAt(i);   // Prueba de 'simbolo' dentro del autómata.
            int transicion = 0;
            // Primero verifico que el símbolo exista en el alfabeto.
            for(int j=0; j<cantidadSimbolos; j++) {
                if (simbolo.equals(alfabeto[j]) == true)
                    j = cantidadSimbolos;   // El símbolo existe en el alfabeto. Finalizo el for
                else
                    transicion++;   // Verifico el siguiente símbolo
            }   // Al finalizar el for, 'transicion' contiene la posicion en el alfabeto que es igual a simbolo
            if (transicion < cantidadSimbolos)    // Esto quiere decir que 'simbolo' existe en 'alfabeto'
                iterador = iterador.getTransiciones()[transicion];
            else
                return false;
        }   // Al finalizar el for ya se ha analizado toda la cadena, e iterador está en un estado
        return iterador.esAceptable();
    }
    /**
     * Función que devuelve la cantidad de símbolos que tiene el alfabeto del autómata.
     * @return entero que indica la cantidad de símbolos del alfabeto.
     */
    public int getCantidadSimbolos() { return this.cantidadSimbolos; }
    /**
     * Función que devuelve el arreglo de símbolos que conforman el alfabeto.
     * @return el alfabeto del autómata.
     */
    public String[] getAlfabeto() { return this.alfabeto; }
    /**
     * Función que devuelve el Estado Inicial del autómata, esto es estados[0].
     * @return el estado inicial del autómata.
     */
    public Estado getEstadoInicial() { return this.estados[0]; }

    /**
     * Método que inserta un 'alfabeto' para el autómata.
     * @param alfabeto 
     */
    public void setAlfabeto(String[] alfabeto) {
        this.alfabeto = alfabeto;
        this.cantidadSimbolos = this.alfabeto.length;
    }
    /**
     * Método que inserta 'estado' en 'estados[indice]'.
     * @param estado Estado que se inserta en el autómata.
     * @param indice posición dentro del arreglo de estados en donde se inserta 'estado'.
     */
    public void setEstado(Estado estado, int indice) { this.estados[indice] = estado; }
    /**
     * Método que define la cantidad de estados del autómata. También crea el array de estados con 'cantidadEstados' posiciones.
     * Para ello, es importante que previo a este método se defina el alfabeto del autómata.
     * @param cantidadEstados entero que indica cuantos estados tendrá el autómata.
     */
    public void setCantidadEstados(int cantidadEstados) {
        this.cantidadEstados = cantidadEstados;
        estados = new Estado[cantidadEstados];
        for(int i=0; i<cantidadEstados; i++) estados[i] = new Estado(cantidadSimbolos);
    }
    public Automata minimizar(){
        ArrayList actuales = new ArrayList<ArrayList>();
        ArrayList no_finales = new ArrayList<Estado>();
        ArrayList Finales = new ArrayList<Estado>();
        for(Estado a : estados){
            if(a.esAceptable()==true)Finales.add(a);
            else no_finales.add(a);
        }
        actuales.add(no_finales);
        actuales.add(Finales);
        
        ArrayList nuevos_estados = Conjuntos_finales(actuales, actuales.size(),actuales);
        
        for(int i =0; i<nuevos_estados.size();i++){
            if(((ArrayList)nuevos_estados.get(i)).contains(estados[0])!=false && i!=0){
                ArrayList cambio  = (ArrayList)nuevos_estados.get(0);
                nuevos_estados.set(0,(ArrayList) nuevos_estados.get(i));
                nuevos_estados.set(i, cambio);
            }
        }
        
        Automata minimizado = new Automata(alfabeto, nuevos_estados.size());
     
        int c=1;
        for(int i=0; i<nuevos_estados.size();i++) {
            ArrayList<Estado> conjunto = (ArrayList)nuevos_estados.get(i);
            Estado nuevo = new Estado(cantidadSimbolos);
            if(conjunto.contains(estados[0])==true){
                nuevo = minimizado.getEstados(0);     
            }  
            else {
                nuevo = minimizado.getEstados(c);
                c++;
            }
            Estado viejo = (Estado)conjunto.get(0);
            for (int j = 0; j < cantidadSimbolos; j++) {
                Estado transicion = viejo.getTransicion(j);
                for (int k = 0; k < nuevos_estados.size(); k++) {
                     if(((ArrayList)nuevos_estados.get(k)).contains(transicion)==true){
                          nuevo.setTransicion(j,minimizado.getEstados(k));
                          nuevo.getTransicion(j).setSimbolo(transicion.getSimbolo());
                          k = nuevos_estados.size();
                          
                    }    
                }
                
            }
            boolean tipo = false;
            for(Estado a : conjunto){
                if(a.esAceptable()==true) tipo =true;
            }
            nuevo.setAceptable(tipo);
        }
       
        minimizado.setDescripcion(this.descripcion);
        return minimizado;
    }
    
    public ArrayList Conjuntos_finales(ArrayList conjuntos, int cantidad_subconjuntos, ArrayList sub_conjuntos) {
        ArrayList nuevo = new ArrayList();
        for (int i = 0; i < conjuntos.size(); i++) {
            Estado[] estados_actuales = new Estado[((ArrayList<Estado>)conjuntos.get(i)).size()];
            int v =0;
            for(Estado a :((ArrayList<Estado>)conjuntos.get(i))){
                estados_actuales[v]= a;
                v++;
            }
            
            if((((ArrayList)conjuntos.get(i)).size()) > 1) {
                int[][] opciones = opciones((ArrayList)conjuntos.get(i),sub_conjuntos);
                if(compatibilidad(opciones, ((ArrayList)conjuntos.get(i)).size(), cantidadSimbolos)==false){
                    ArrayList probar = Sub_conjuntos(opciones, ((ArrayList)conjuntos.get(i)).size(), cantidadSimbolos, estados_actuales);
                    ArrayList nuevos_sub_Conjuntos = new ArrayList();
                    for (int j = 0; j < probar.size(); j++) {
                        nuevos_sub_Conjuntos.add(probar.get(j));
                    }
                    for (int j = 0; j < sub_conjuntos.size(); j++) {
                        if(i!=j) nuevos_sub_Conjuntos.add(sub_conjuntos.get(j));
                    }
                    ArrayList agregar = Conjuntos_finales(probar, probar.size(),nuevos_sub_Conjuntos);
                    for (int j = 0; j < agregar.size(); j++) {
                        nuevo.add(agregar.get(j));
                    }
                }
                else {
                    ArrayList agregar = conjunto_final((ArrayList)conjuntos.get(i));
                    nuevo.add(agregar);
                }
            }
            else {
                ArrayList agregar = conjunto_final((ArrayList)conjuntos.get(i));
                nuevo.add(agregar);
            }
        }
        return nuevo;
    }
    
    public int[][] opciones(ArrayList conjunto,ArrayList principal) {
        int[][] opciones = new int[conjunto.size()][cantidadSimbolos];
        for (int i = 0; i < conjunto.size(); i++) {
            Estado probar = (Estado)conjunto.get(i);
            for (int j = 0; j < cantidadSimbolos; j++) {
                int a=-1;
                int b=0;
                Estado probar_S = probar.getTransicion(j);
                while(a==-1){
                    if(((ArrayList)principal.get(b)).contains(probar_S)==true){
                        a=b;
                    }
                    b++;
                }
                opciones[i][j]=a;
            }
        }
        return opciones;
    }
    
    public boolean compatibilidad(int[][] opciones,int filas, int columnas){
        boolean respuesta= true;
        for (int i = 0; i < columnas; i++) {
            int a = opciones[0][i];
            for (int j = 0; j < filas; j++) {
                if(opciones[j][i]!= a){
                    respuesta =false;
                }
            }
        }
        return respuesta;
    }
    
    public ArrayList Sub_conjuntos(int[][] opciones,int filas, int columnas,Estado[] estados_actuales){
        ArrayList nuevo = new ArrayList<ArrayList>();
        for (int i = 0; i < filas; i++) {
            ArrayList agregar = new ArrayList<Estado>();
            int a = opciones[i][0];
            if(a!=-1) {
                agregar.add(estados_actuales[i]);
                for (int c = 1; c < filas; c++) {
                    boolean diferente = false;
                    if(c!=i) {
                        for (int j = 0; j < columnas; j++) {
                            if(opciones[i][j]!= opciones[c][j]){
                                diferente = true;
                                j= columnas;
                            }
                        }
                        if(diferente == false){
                            opciones[c][0]= -1;
                            agregar.add(estados_actuales[c]);
                        }
                    }
                }
            }
            if(agregar.size()!=0)
                nuevo.add(agregar);
        }
        return nuevo;
    }
    
    public ArrayList conjunto_final(ArrayList conjunto){
        ArrayList<Estado> nuevo = new ArrayList<Estado>();
        for (int i = 0; i < conjunto.size(); i++) {
            Estado a = (Estado)conjunto.get(i);
            nuevo.add(a);
        }
        return nuevo;
    }

    public Estado getEstados(int i) {
        return estados[i];
    }
    
    public String Expresion_Regular(){
        Expresion_Regular expresion = new Expresion_Regular(estados, alfabeto);
        return expresion.Expresion();
    }
}
