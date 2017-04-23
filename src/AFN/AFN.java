/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AFN;

import ExpresionRegular.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Clase que modela un Autómata Finito No Determinista. Un AFN consiste en 5 objetos:
 * 1) Un conjunto finito I de símbolos de entrada (el alfabeto).
 * 2) Un conjunto finito S de estados en los que puede estar el autómata.
 * 3) Un estado, denotado por s0, llamado el estado inicial.
 * 4) Un determinado conjunto de estados F llamado estados aceptables (donde F es un subconjunto de S, F c S).
 * 5) Una función de estado próximo N:SxI->S que asocia un 'estado siguiente' a cada par ordenado que consiste en un 'estado
 *    presente' y una 'entrada presente'.
 * @author Wilson Xicará
 */
public class AFN {
    private String[] alfabeto;
    private EstadoAFN[] estados;
    private EstadoAFN iterador;
    private int cantidadSimbolos, cantidadEstados;
    private String nombreAFN;
    
    /**
     * Constructor que inicializa un Autómata vacío (esto es, un alfabeto vacío y cero estados).
     */
    public AFN() {
        this.alfabeto = null;
        this.estados = null;
        this.iterador = null;
        this.nombreAFN = "";
        this.cantidadSimbolos = this.cantidadEstados = 0;
    }
    /**
     * Constructor que inicializa un AFN con un alfabeto y con n-estados (según los parámetros, para ambos). Los Estados
     * dentro del AFN se manejan como un arreglo de EstadoAFN, donde estados[0] es el estado inicial; cada estado dentro
     * del arreglo se inicializa con 'cantidadSimbolos+1' transiciones (incluido las transiciones para el caracter nulo), todos
     * apuntado a estados vacíos. En el constructor de cada EstadoAFN se inicializa para contener transiciones con el caracter nulo.
     * @param alfabeto arreglo de String que representa el alfabeto de este Autómata.
     * @param cantidadEstados entero que indica la cantidad de estados que tendrá el Autómata.
     */
    public AFN(String[] alfabeto, int cantidadEstados) {
        this.alfabeto = alfabeto;
        this.cantidadSimbolos = alfabeto.length;
        this.cantidadEstados = cantidadEstados;
        this.estados = new EstadoAFN[cantidadEstados];
        for(int i=0; i<cantidadEstados; i++)
            this.estados[i] = new EstadoAFN(this.cantidadSimbolos);
        this.iterador = null;
    }
    public AFN(String[] alfabeto) {
        this.alfabeto = alfabeto;
        this.cantidadSimbolos = alfabeto.length;
    }

    public int getCantidadSimbolos() {return cantidadSimbolos;}

    public String[] getAlfabeto() {return alfabeto;}

    public EstadoAFN[] getEstados() {
        return estados;
    }
    
    
    
    /**FALTA IMPLEMENTAR!!!
     * Función que determina si 'cadena' pertenece al Lenguaje que implementa el Autómata. Para ello, prueba cada uno de los caracteres
     * que conforman 'cadena' y realiza la transición correspondiente al estado-símbolo. "Se dice que una cadena pertenece al
     * lenguaje definido por el autómata si y solo sí el autómata va a un estado aceptable cuando los símbolos de 'cadena' son
     * entrada para el autómata en suceción de izquierda a derecha, empezando cuando el autómata está en un estado inicial".
     * @param cadena suseción de símbolos (caracteres) que se prueban en el autómata.
     * @return 'true' si 'cadena' pertenece al lenguaje del autómata; 'false' en caso contrario.
     */
    public boolean probarCadena(String cadena) {
        EstadoAFN iterador = estados[0];   // iterador se incia como el Estado Inicial.
        int longitudCadena = cadena.length();
        
        // Verificación si la cadena es vacía. Para ello, se verifica si el Autómata tiene una transición
        if (longitudCadena == 0) {
            
        } else {
            for(int i=0; i<longitudCadena; i++) {
                String caracter = ""+cadena.charAt(i);   // Prueba de 'caracter' dentro del autómata.
                int transicion = 0;
                // Primero verifico que el símbolo exista en el alfabeto.
                for(int j=0; j<cantidadSimbolos; j++) {
                    if (caracter.equals(alfabeto[j]) == true)
                        j = cantidadSimbolos;   // El símbolo existe en el alfabeto. Finalizo el for
                    else
                        transicion++;   // Verifico el siguiente símbolo
                }   // Al finalizar el for, 'transicion' contiene la posicion en el alfabeto que es igual a 'caracter'
                if (transicion < cantidadSimbolos)    // Esto quiere decir que 'simbolo' existe en 'alfabeto'
                    iterador = iterador.getTransicion(transicion, 0);
                else
                    return false;
            }   // Al finalizar el for ya se ha analizado toda la cadena, e iterador está en un estado
        }
        return iterador.esAceptable();
    }
    /**
     * Método que devuelve un AFN a partir de una expresión regular.
     * En 'grupo' se encuentra estructurada la expresión regular como un árbol: por niveles y por nodos internos,
     * cada grupo contiene un pedazo de expresión regular.
     * El automata (this) ya ha sido inicializado
     * @param grupo
     * @return 
     */
    public AFN crearAutomata(ExpresionRegular.Grupo grupo) {
        // Primero obtengo el nodo del nivel más alto y el que está más a la izquierda
        // Se asume que grupo es un objeto que contiene varios grupos. Inicializo con las operaciones de grupo
        // Inicializo los estados 'inicial' y 'final'
        EstadoAFN primero = new EstadoAFN(cantidadSimbolos), ultimo = new EstadoAFN(cantidadSimbolos);
        primero.setNombre("I");
        ultimo.setNombre("F");
        ultimo.setAceptable(true);
        // 'grupo' apunta a los estados inicial y final, más no se definen aún las transiciones de estos
        grupo.setAnterior(primero);
        grupo.setSiguiente(ultimo);
        cantidadEstados = 2;    // Conteo de primero y último
        ArrayList<EstadoAFN> listaEstados = new ArrayList<>();
        listaEstados.add(primero);  // Agrego los pirmeros EstadoAFN a la lista de estados
        listaEstados.add(ultimo);
        conectar_expresion_regular(grupo, primero, ultimo, listaEstados);
        crear_transiciones(grupo, listaEstados);
        // Al salir de los métodos anteriores, listaEstados tiene todos los estados que se crearon. Lo paso a un arreglo
        System.out.println("cantidadEstados = "+cantidadEstados+", tamaño = "+listaEstados.size());
        this.estados = new EstadoAFN[cantidadEstados];
        this.estados[0] = listaEstados.remove(0);       // El estado inicial del Autómata
        this.estados[cantidadEstados-1] = listaEstados.remove(0);  // El estado final del autómata
        for (int i=1; i<(cantidadEstados-1); i++)
            this.estados[i] = listaEstados.remove(0);
        
        this.iterador = estados[0]; // iterador es el estado inicial
        
        return this;
        // HASTA AQUÍ SE GARANTIZA LA CREACIÓN DEL AUTÓMATA FINITO NO DETERMINISTA A PARTIR DE LA EXPRESIÓN REGULAR.
    }
    /**
     * Método que crea las conexiones entre la Expresión Regular y el Autómata Finito No Determinista. Se crean EstadoAFN's
     * cuando se necesiten, pero aún no se definen las transiciones sobre los mismos estados (se crean todos, pero sólo se
     * puede acceder a ellos mediante el objeto de tipo ExpresionRegular.Grupo). No se evalúa la Cerradura de Kleen para cada grupo.
     * @param grupoPadre
     * @param anterior
     * @param siguiente 
     */
    private void conectar_expresion_regular(ExpresionRegular.Grupo grupoPadre, EstadoAFN anterior, EstadoAFN siguiente, ArrayList<EstadoAFN> listaEstados) {
        ArrayList<ExpresionRegular.Grupo> grupo = grupoPadre.getGrupos();
        int index = 0, maximo = grupo.size();
        while (index < maximo) {
            /* Debido a la división de un grupo en varios grupos (si su expresión regular tiene varios símbolos
            concatenados) implica que la Cerradura de Kleene para un grupo padre no se "hereda" a los grupos hijos que
            se forman. Por ello, previo a crear las transiciones de los niveles más inferiores debo evaluar la Cerradura
            de Kleene de los grupos superiores. */
            if ("*".equals(grupoPadre.getRepeticion())) { // El grupo actual tiene Cerradura de Kleene
                // Se asume que este grupo tiene en .getExpresion() una cadena que coincide con un símbolo del alfabeto
                // Se crea un EstadoAFN intermedio entre iterador.getAnterior() e iterador.getSiguiente(), llegando y saliendo de él con la cadena vacía
                EstadoAFN intermedio = new EstadoAFN(cantidadSimbolos);
                cantidadEstados++;
                intermedio.setNombre(""+cantidadEstados);
                grupoPadre.getAnterior().setTransicionNula(intermedio);   // Defino la transición nula que LLEGA a intermedio
                intermedio.setTransicionNula(grupoPadre.getSiguiente());  // Defino la transición nula que SALE de intermedio

                grupoPadre.setAnterior(intermedio);
                grupoPadre.setSiguiente(intermedio);
                
                anterior = siguiente = intermedio;
                
                listaEstados.add(intermedio);
            }
            // Analizo el i-ésimo subgrupo y determino su operación con los demás subgrupos
            ExpresionRegular.Grupo grupito = grupo.get(index);    // Obtengo el primer subgrupo
            if (index == 0) {  // grupito es el primero de la lista. Su anterior es 'anterior'
                grupito.setAnterior(anterior);
                if ("+".equals(grupito.getPostOperador())) {    // El siguiente de grupito es 'siguiente'
                    grupito.setSiguiente(siguiente);
                } else  if (".".equals(grupito.getPostOperador())) {    // Su siguiente es un nuevo EstadoAFN
                    // Entra a esta condición siempre que haya como mínimo dos grupitos
                    EstadoAFN intermedio = new EstadoAFN(cantidadSimbolos);
                    cantidadEstados++;  // Aumento el contador de Estados
                    intermedio.setNombre(""+cantidadEstados);
                    grupito.setSiguiente(intermedio);
                    listaEstados.add(intermedio);
                } else {
                    // Puede entrar a esta condición si grupo.size() == 1
                    grupito.setSiguiente(siguiente);    // Tendrá como anterior y siguiente los que han sido pasados como parámetros
                }
            } else if ((index+1) == maximo) {   // grupito es el último de la lista
                if ("+".equals(grupito.getPreOperador())) { // Su anterior es 'anterior'
                    grupito.setAnterior(anterior);
                } else if (".".equals(grupito.getPreOperador())) {  // Su anterior es grupos.get(index-1).getSiguiente()
                    grupito.setAnterior(grupo.get(index-1).getSiguiente());
                }   // No hay caso contrario pues aquí grupito no puede ser el primero de la lista (de serlo, se evalúa en el if anterior)
                grupito.setSiguiente(siguiente);    // El siguiente de grupito es 'siguiente'
            }
            else {    // grupito es intermedio en la lista
                /* Definición del EstadoAFN anterior a grupito */
                if ("+".equals(grupito.getPreOperador())) { // Su anterior es 'anterior'
                    grupito.setAnterior(anterior);
                } else if (".".equals(grupito.getPreOperador())) {  // Su anterior es el siguiente a grupos.get(index-1)
                    // Entra a esta condición siempre que grupo.size() > 1
                    grupito.setAnterior(grupo.get(index-1).getSiguiente());
                }   // No hay caso contrario pues aquí grupito no puede ser el primero ni el último de la lista (grupo.size() > 2)
                
                /* Definición del Estado siguiente a grupito */
                if ("+".equals(grupito.getPostOperador())) {    // Su siguiente es 'siguiente'
                    grupito.setSiguiente(siguiente);
                } else if (".".equals(grupito.getPostOperador())) { // Su siguiente es un nuevo EstadoAFN
                    EstadoAFN intermedio = new EstadoAFN(cantidadSimbolos);
                    cantidadEstados++;  // Aumento el contador de Estados
                    intermedio.setNombre(""+cantidadEstados);
                    grupito.setSiguiente(intermedio);
                    listaEstados.add(intermedio);
                }   // No hay caso contrario pues aquí grupito no puede ser el primero ni el último de la lista (grupo.size() > 2)
            }
            index++;    // Analizo el siguiente grupito
        }   // Hasta aquí se garantiza la conexión de la ER con todos los EstadoAFN
        // Creo las conexiones de cada grupito, para todo el grupo
        for (int i=0; i<maximo; i++) {
            if (grupo.get(i).getGrupos() != null)
                conectar_expresion_regular(grupo.get(i), grupo.get(i).getAnterior(), grupo.get(i).getSiguiente(), listaEstados);
        }   // Hasta aquí se garantiza la conexión de forma recursiva de todos los grupos generados para la Expresión Regular
    }
    /**
     * Método que crea las transiciones de los EstadoAFN del AFN. Previo a esto, cada ExpresionRegular.Grupo tiene punteros
     * a los EstadoAFN a los que apunta, pero aún no se ha definito la Cerradura de Kleene de cada grupo.
     * Queda la evaluación de los caracteres. Para ello, los grupos que generan transiciones son aquellos que tienen en su
     * atributo Grupo.expresion un sólo caracter (esto es, por el alfabeto que tiene un caracter para cada símbolo). Otra
     * opción es evaluar si expresión pertenece al alfabeto del autómata (lo que hace no válido el conjunto).
     * @param grupoPadre
     * @param anterior
     * @param siguiente 
     */
    private void crear_transiciones(ExpresionRegular.Grupo grupoPadre, ArrayList<EstadoAFN> listaEstados) {
        // Accedo al Grupo más a la izquierda del nivel más bajo (siendo el principal de nivel 0)
        // Entra a este método sí y solo sí grupoPadre.getGrupos() != null
        ArrayList<ExpresionRegular.Grupo> grupo = grupoPadre.getGrupos();   // Obtengo los grupos
        int maximo = grupo.size();
        for (int i=0; i<maximo; i++) {
            ExpresionRegular.Grupo iterador = grupo.get(i);
            if (iterador.getGrupos() != null) { // Llamo en busca del Grupo de nivel más bajo
                crear_transiciones(iterador, listaEstados);
            } else {    // Este es un grupo de último nivel (ya no tiene más grupos)
                // Se asume que este grupo tiene en .getExpresion() una cadena que coincide con un símbolo del alfabeto
                // Busco la expresion en el alfabeto
                int pos = 0;
                for (int j=0; j<cantidadSimbolos; j++) {
                    if (alfabeto[j].equals(iterador.getExpresion()))
                        j = cantidadSimbolos;   // La expresión está en el alfabeto. Finalizo el ciclo
                    else
                        pos++;  // Evalúo el siguiente símbolo del alfabeto
                }   // 'pos' tiene la posición dentro del alfabeto de la transición del grupo evaluado
                // Primero evalúo si este grupo tiene Cerradura de Kleene
                if ("*".equals(iterador.getRepeticion())) {
                    // Se crea un EstadoAFN intermedio entre iterador.getAnterior() e iterador.getSiguiente()
                    EstadoAFN intermedio = new EstadoAFN(cantidadSimbolos);
                    cantidadEstados++;
                    intermedio.setNombre(""+cantidadEstados);
                    iterador.getAnterior().setTransicionNula(intermedio);   // Defino la transición nula que LLEGA a intermedio
                    intermedio.setTransicionNula(iterador.getSiguiente());  // Defino la transición nula que SALE de intermedio
                    intermedio.setTransicion(pos, intermedio);  // Defino la transición de intermedio con el símbolo alfabeto[pos]
                    listaEstados.add(intermedio);
                } else {
                    // iterador.getAnterior() tiene una transición hacia iterador.getSiguiente() con el símbolo alfabeto[pos]
                    if (pos < cantidadSimbolos) {   // Esto es, si la expresión está en el alfabeto
                        iterador.getAnterior().setTransicion(pos, iterador.getSiguiente());
                    }   // Hasta aquí ya se ha generado la transición en el autómata
                }
            }
            iterador.setAnterior(null);     // Desconecto el grupo actual del EstadoAFN dentro del Autómata
            iterador.setSiguiente(null);
        }   // Hasta aquí se garantiza la creación de todas las transiciones correspondientes al listado de Grupos.
        // Necesito eliminar los grupos que ya generaron una conexión entre EstadoAFN
//        for(int i=0; i<maximo; i++)
//            grupo.remove(0);    // Elimino el i-ésimo grupo del ArrayList
    }
    
    public void iniciarAutomata() { this.iterador = this.estados[0]; }
    public EstadoAFN getEstadoAutomata(String caracter) {
        // Busco caracter dentro del alfabeto. De no existir retorna null
        int pos = 0;
        for(int i=0; i<cantidadSimbolos; i++) {
            if (alfabeto[i].equals(caracter))
                i = cantidadSimbolos;
            else
                pos++;
        }
        if (!(pos < cantidadSimbolos))
            return null;
        this.iterador = this.iterador.getTransicion(pos, 0);
        return this.iterador;
    }
}
