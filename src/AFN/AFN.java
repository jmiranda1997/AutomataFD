/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AFN;

import Excepciones.ExcepcionAutomataIncorrecto;
import ExpresionRegular.*;
import Excepciones.ExcepcionCadenaNoValida;
import java.util.ArrayList;

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
    private int cantidadSimbolos, cantidadEstados;
    private String nombreAFN, descripcion;
    private boolean estaEnEstadoAceptable;
    
    /**
     * Constructor que inicializa un Autómata vacío (esto es, un alfabeto vacío y cero estados).
     */
    public AFN() {
        this.alfabeto = null;
        this.estados = null;
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
    }
    
     public AFN(String[] alfabeto, int cantidadEstados, ArrayList<String> nombre_estados) {
        this.alfabeto = alfabeto;
        this.cantidadSimbolos = alfabeto.length;
        this.cantidadEstados = cantidadEstados;
        this.estados = new EstadoAFN[cantidadEstados];
        for(int i=0; i<cantidadEstados; i++){
            this.estados[i] = new EstadoAFN(this.cantidadSimbolos);
            this.estados[i].setNombre(nombre_estados.get(i));
        }
    }
    public AFN(String[] alfabeto) {
        this.alfabeto = alfabeto;
        this.cantidadSimbolos = alfabeto.length;
        this.cantidadEstados = 0;
        this.estados = null;
    }
    /**
     * Constructor que inicializa el AFN a partir de la expresión regular en grupoER. Este constructor llama al método
     * construir_automata(ExpresionRegular.Grupo grupoER).
     * @param grupoER objeto que contiene la expresión regular de la cual se creará el AFN.
     * @param alfabeto alfabeto del nuevo autómata (debe ser el mismo alfabeto sobre el que se define la expresión regular).
     */
    public AFN(ExpresionRegular.Grupo grupoER, String[] alfabeto) {
        this.alfabeto = alfabeto;
        this.cantidadSimbolos = alfabeto.length;
        construir_automata(grupoER);
    }

    public int getCantidadSimbolos() { return cantidadSimbolos; }
    public String[] getAlfabeto() { return alfabeto; }
    public EstadoAFN[] getEstados() { return estados; }
    public String getNombre() { return nombreAFN; }
    public String getDescripcion() { return descripcion; }
    
    public void setNombre(String nombre) { this.nombreAFN = nombre; }
    public void setAlfabeto(String[] alfabeto) {
        this.alfabeto = alfabeto;
        this.cantidadSimbolos = alfabeto.length;
    }
    public void setTransiciones(int indexEstado, int indexSimbolo, ArrayList<EstadoAFN> transiciones) {
        if (indexSimbolo == cantidadSimbolos)
            this.estados[indexEstado].setTransicionesNulas(transiciones);
        else
            this.estados[indexEstado].setTransiciones(indexSimbolo, transiciones);
    }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    /**
     * Método que inicializa y crea un AFN a partir de una Expresión Regular.
     * En 'grupo' se encuentra estructurada la expresión regular como un árbol: por niveles y por nodos internos.
     * Cada grupo contiene un pedazo de expresión regular.
     * @param grupo
     * @return 
     */
    private void construir_automata(ExpresionRegular.Grupo grupo) {
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
        this.estados = new EstadoAFN[cantidadEstados];
        this.estados[0] = listaEstados.remove(0);       // El estado inicial del Autómata
        this.estados[cantidadEstados-1] = listaEstados.remove(0);  // El estado final del autómata
        for (int i=1; i<(cantidadEstados-1); i++)
            this.estados[i] = listaEstados.remove(0);
        // HASTA AQUÍ SE GARANTIZA LA CREACIÓN DEL AUTÓMATA FINITO NO DETERMINISTA A PARTIR DE LA EXPRESIÓN REGULAR.
    }
    /**
     * Método que sirve para volver a crear el Autómata Finito No Determinista a partir de una Expresión Regular (estructurada
     * en grupoER).
     * @param grupoER 
     */
    public void crearAutomata(ExpresionRegular.Grupo grupoER) {
        construir_automata(grupoER);
    }
    /**
     * Método que crea las conexiones entre la Expresión Regular y los EstadoAFN del AFN. Se crean EstadoAFN's cuando se
     * necesiten, pero aún no se definen las transiciones sobre los mismos estados (se crean todos, pero sólo se puede acceder
     * a ellos mediante el objeto de tipo ExpresionRegular.Grupo), excepto para los grupos que tienen Cerradura de Kleene.
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
     * atributo Grupo.expresion un sólo caracter (esto es, por el alfabeto que tiene un caracter para cada símbolo).
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
    }
    
    /**FALTA IMPLEMENTAR!!!
     * Función que determina si 'cadena' pertenece al Lenguaje que implementa el Autómata. Para ello, prueba cada uno de los caracteres
     * que conforman 'cadena' y realiza la transición correspondiente al estado-símbolo. "Se dice que una cadena pertenece al
     * lenguaje definido por el autómata si y solo sí el autómata va a un estado aceptable cuando los símbolos de 'cadena' son
     * entrada para el autómata en suceción de izquierda a derecha, empezando cuando el autómata está en un estado inicial".
     * @param cadena suseción de símbolos (caracteres) que se prueban en el autómata.
     * @return 'true' si 'cadena' pertenece al lenguaje del autómata; 'false' en caso contrario.
     * @throws Excepciones.ExcepcionCadenaNoValida
     */
    public boolean probarCadena(String cadena) throws ExcepcionCadenaNoValida, ExcepcionAutomataIncorrecto {
        if (this.alfabeto == null)
            throw new ExcepcionAutomataIncorrecto("El Autómata no tiene alfabeto");
        estaEnEstadoAceptable = false;
        EstadoAFN iterador = estados[0];   // iterador se incia como el Estado Inicial.
        
        // Verifico si la cadena es vacía. Para ello, se verifica si el Autómata tiene una o varias transiciones con la cadena vacía.
        // De existir una transición con cadena vacía hacia un estado aceptable, la cadena es válida.
        if (cadena.length() == 0) {
            ArrayList<EstadoAFN> transicionesNulas = iterador.getTransicionesNulas();   // Obtengo las transiciones nulas del Estado Inicial
            int longitud = transicionesNulas.size();
            // Evalúo si alguna de las transiciones nulas lleva a un estado final
            for(int i=0; i<longitud; i++) {
                estaEnEstadoAceptable = (transicionesNulas.get(i).esAceptable());
                if (estaEnEstadoAceptable)
                    i = longitud;   // Para cerrar el ciclo
            }
        } else {    // Verificación de la cadena cuando no es vacía
            recorrerAutomata(iterador, cadena, 0);
        }
        return estaEnEstadoAceptable;
    }
    private void recorrerAutomata(EstadoAFN iterador, String cadena, int index) throws ExcepcionCadenaNoValida {
        if (index < cadena.length()) {
            String caracter = ""+cadena.charAt(index);
            // Busco si caracter es un símbolo del alfabeto
            int pos = 0;
            for(int i=0; i<cantidadSimbolos; i++) {
                if (caracter.equals(alfabeto[i]))
                    i = cantidadSimbolos;
                else
                    pos++;
            }
            if (pos == cantidadSimbolos)    // Si el caracter no está contenido en el alfabeto
                throw new ExcepcionCadenaNoValida("El caracter '"+caracter+"' no está en el alfabeto del Autómata.");
            // Obtengo todas las transiciones con el símbolo alfabeto[pos]
            ArrayList<EstadoAFN> transiciones = iterador.getTransiciones(pos);
            int tamaño = transiciones.size();
            for(int i=0; i<tamaño; i++) {
                if ((index+1) == cadena.length())
                    estaEnEstadoAceptable = (transiciones.get(i).esAceptable());
                if (!estaEnEstadoAceptable)
                    recorrerAutomata(transiciones.get(i), cadena, index+1);
                else
                    i = tamaño;
            }
            // Si no tiene transiciones con el símbolo alfabeto[pos] verifico si tiene transiciones con la cadena vacía
            if (tamaño == 0) {
                transiciones = iterador.getTransicionesNulas();
                tamaño = transiciones.size();
                for(int i=0; i<tamaño; i++) {
                    if ((index+1) == cadena.length())
                        estaEnEstadoAceptable = (transiciones.get(i).esAceptable());
                    if (!estaEnEstadoAceptable)
                        recorrerAutomata(transiciones.get(i), cadena, index);
                    else
                        i = tamaño;
                }
            }
        }
    }
    
    public int getLongitudNombreEstados() {
        int longitud = estados[0].getNombre().length();
        for(int i=1; i<cantidadEstados; i++) {
            if (estados[i].getNombre().length() > longitud)
                longitud = estados[i].getNombre().length();
        }
        return longitud;
    }
    public int getCantidadEstados() { return cantidadEstados; }
    public String[] getNombresEstados() {
        String[] nombres = new String[cantidadEstados];
        for(int i=0; i<cantidadEstados; i++)
            nombres[i] = estados[i].getNombre();
        return nombres;
    }
    public int[] indiceTransiciones(int indexEstado, int indexSimbolo) {
        EstadoAFN estado = estados[indexEstado];
        int contTransiciones = (indexSimbolo == alfabeto.length) ?
                estado.getTransicionesNulas().size() :
                estado.getTransiciones(indexSimbolo).size();
        if (contTransiciones == 0)
            return null;
        int[] indice = new int[contTransiciones];
        EstadoAFN iter;
        for(int i=0; i<contTransiciones; i++) {
            iter = (indexSimbolo == alfabeto.length) ?
                    estado.getTransicionNula(i) :
                    estado.getTransiciones(indexSimbolo).get(i);
            int encontrado = 0;
            for (int j=0; j<cantidadEstados; j++) {
                if (iter == estados[j])
                    j = cantidadEstados;
                else
                    encontrado++;
            }
            indice[i] = encontrado;
        }
        return indice;
    }
}
