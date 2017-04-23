/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AFN;

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
    private String nombreAFN;
    
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
}
