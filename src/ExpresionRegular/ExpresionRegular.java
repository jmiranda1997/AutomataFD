/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExpresionRegular;

import java.util.ArrayList;
import AFN.*;
import Excepciones.ExcepcionDatosIncorrectos;

/**
 * Clase que modela un validador para Expresiones Regulares. Una expresión regular es válida si:
 * - Su estructura sea correcta.
 * - Todos sus operandos pertenezcan al alfabeto.
 * Implementa un método (que a su vez implementa una excepción) evalúa la validez de una expresión regular. Además, implementa
 * la clase Grupo el cual es un conjunto estructurado de todos los operandos y operadores de la expresión regular que es
 * útil para construir un Autómata Finito No Determinista a partir de una Expresión Regular
 * @author Wilson Xicará
 */
public class ExpresionRegular {
    private String expresionRegular;
    private Grupo general;
    private String[] alfabeto;
    
    public ExpresionRegular() {
        this.expresionRegular = "";
        this.general = null;
        this.alfabeto = null;
    }
    public ExpresionRegular(String[] alfabeto) {
        this.expresionRegular = "";
        this.general = null;
        this.alfabeto = alfabeto;
    }
    
    public String[] getAlfabeto() { return alfabeto; }
    public Grupo getGrupo() { return general; }
    
    public void setAlfabeto(String[] alfabeto) { this.alfabeto = alfabeto; }
    public void setExpresionRegular(String expresionRegular) throws ExcepcionDatosIncorrectos {
        // Primero evalúo que la Expresión Regular tenga una estructura correcta
        validarEstructuraER(expresionRegular);
        // Seguidamente evalúo que todos los operandos sean símbolos del alfabeto
        validarOperandosER(expresionRegular);
        // Si todo es correcto, la Expresión Regular es correcta y la guardo
        this.expresionRegular = expresionRegular;
    }
    
    /**
     * Método que evalúa si una cadena cumple con ser una expresión regular. Para que sea una expresión regular, debe tener
     * una estructura correcta (que no tenga bloques no válidos), y todos sus operandos deben pertenecer a un alfabeto definido
     * previamente en este objeto. Para la comprobación, se engloba toda la 'expresionRegular' entre paréntesis (al inicio y
     * al final) y se analiza de esa forma la estructura de la expresión regular.
     * Si la cadena es una expresión regular, se agrega en this.expresionRegula.
     * @param expresionRegular String que se evaluará para determinar si es o no una expresión regular escrita correctamente.
     * @throws ExcepcionDatosIncorrectos se lanza al detectar una condición que invalida la cadena, indicando el motivo de dicha acción.
     */
    public static void validarEstructuraER(String expresionRegular) throws ExcepcionDatosIncorrectos {
        if (expresionRegular.length() == 0)
            throw new ExcepcionDatosIncorrectos("La Expresión regular debe contener al menos un símbolo");
        
        String cpER = "(" + expresionRegular + ")";    // Englobo todo en un sólo operando
        int nivel = 0, maximo = cpER.length(), index = 0;
        String caracter = "";
        while (index < maximo) {
            caracter = "" + cpER.charAt(index);
            if ("(".equals(caracter)) {
                nivel++;
                index++;
            } else if (")".equals(caracter)) {
                // Si se tienen los bloques +) o ()
                if ("+".equals(""+cpER.charAt(index-1)) || "(".equals(""+cpER.charAt(index-1)))
                    throw new ExcepcionDatosIncorrectos("El bloque "+cpER.charAt(index-1)+caracter+" no es válido");
                else {
                    nivel--;
                    if (nivel < 0)  // En caso de haber cerrado más paréntesis de los abiertos, sin agotar la cadena
                        throw new ExcepcionDatosIncorrectos("El bloque en la posición "+cpER.charAt(index)+cpER.charAt(index-1)+" de la cadena cierra forzadamente la expresión regular");
                    else
                        index++;
                }
            } else if ("*".equals(caracter)) {
                // Evalúo que no haya dos operadores consecutivos (el caso *+ si es válido {a*+b})
                if ("(".equals(""+cpER.charAt(index-1)) || "+".equals(""+cpER.charAt(index-1)) || "*".equals(""+cpER.charAt(index-1)))
                    throw new ExcepcionDatosIncorrectos("El bloque "+cpER.charAt(index-1)+caracter+" no es válido");    // Si se tienen los bloques no válidos (*, +*, o **
                else
                    index++;
            } else if ("+".equals(caracter)) {
                // Si se tienen los bloques no válidos (+ o ++ (el bloque *+ es válido)
                if ("(".equals(""+cpER.charAt(index-1)) || "+".equals(""+cpER.charAt(index-1)))
                    throw new ExcepcionDatosIncorrectos("El bloque "+cpER.charAt(index-1)+caracter+" no es válido");
                else
                    index++;
            } else
                index++;    // Si no encuentra algún sub bloque incorrecto, se sigue evaluando la cadena
        }   // Hasta aquí se garantiza que la expresión regular tiene una estructura correcta
    }
    private void validarOperandosER(String cadena)  throws ExcepcionDatosIncorrectos {
        if (alfabeto == null)
            throw new ExcepcionDatosIncorrectos("Aún no se ha definido el Alfabeto para validar la Expresión Regular");
        // En caso de ser válida, evalúo que todos los operandos de la expresión regular estén definidos en el alfabeto
        int index = 0, maximo = cadena.length();
        String cpER = cadena, caracter;
        int contSimbolos = alfabeto.length;
        while (index < maximo) {    // Mientras no se agote toda la cadena
            caracter = ""+cpER.charAt(index);   // Obtengo un caracter
            // Evaluo si caracter pertenece al alfabeto si no es un operando o un signo de agrupación
            if (!("(".equals(caracter) || ")".equals(caracter) || "+".equals(caracter) || "*".equals(caracter))) {
                int posEncontrado = 0;
                for (int i=0; i<contSimbolos; i++) {    // Busco caracter en el alfabeto
                    if (caracter.equals(alfabeto[i]))
                        i = contSimbolos;
                    else
                        posEncontrado++;
                }
                if (posEncontrado == contSimbolos)    // En caso de no pertenencer. La expresión regular no es válida
                    throw new ExcepcionDatosIncorrectos("El símbolo '"+caracter+"' no está definido en el alfabeto");
            }
            index++;
        }
    }
    /**
     * Método que convierte unaa Expresión Regular válida en un objeto de tipo ExpresionRegular.Grupo el cual sirve para
     * construir un Autómata Finito No Determinista desde dicha expresión.
     * Este método debe utilizarse en conjunto a validarEstructuraER(String expresionRegular) ya que si la cadena no es una
 expresión regular válida puede generar problemas al crear los grupos y al constrir el Autómata.
     */
    public void generarGrupos() {
        // Englobo la expresión regular en paréntesis ya que servirán para identificar el inicio y fin de toda la expresión.
        // Al llamar a esta función, se asume que this.expresionRegular es válida.
        String cpER = "("+expresionRegular+")";
        int fila = 0;
        int  maximo = cpER.length();
        int index = 0;  // La expresión regular ya no está encerrada en paréntesis que la engloban
        this.general = new Grupo();
        String caracter = "";
        // Este ArrayList puede interpretarse como una matriz cuadrada (aunque no necesariamente de mxn).
        ArrayList<ArrayList<Grupo>> pilaGrupos = new ArrayList<>();
        pilaGrupos.add(new ArrayList<>());    // Para poder utilizar la pilaGrupos
        
        while (index < maximo) {    // Mientras no se agote la cadena
            /* Se reinicia este ciclo siempre que se crea un nuevo grupo en el nivel en el que indica 'fila' */
            Grupo nuevo = new Grupo();
            caracter = "" + cpER.charAt(index);
            if ("(".equals(caracter)) { // Implica que se creará un nuevo grupo. Agrego un preOperador del grupo
                pilaGrupos.get(fila).add(nuevo);    // Agrego el nuevo grupo en la 'fila' que le corresponde
                pilaGrupos.add(new ArrayList<>()); // Agrego lo que será los grupos de nuevo
                // Defino el preOperador de nuevo
                int conteo = pilaGrupos.get(fila).size() - 1; // Existe por lo menos un elemento
                nuevo.setPreOperador((conteo > 0) ? pilaGrupos.get(fila).get(conteo-1).getPostOperador(): "");
                fila++;
                index++;
            } else if (")".equals(caracter)) {  // Implica que se cerrará un grupo. Agrego un postOperador del grupo
                // Extraigo de pilaGrupos todos los subgrupos existentes en pilaGrupos.get(fila) y los inserto en el Grupo
                // que está en el último del ArrayList en pilaGrupos.get(fila-1)
                Grupo padre = pilaGrupos.get(fila-1).get(pilaGrupos.get(fila-1).size()-1);
                padre.addGrupos(pilaGrupos.remove(fila));
                fila--; // pilaGrupos.get(fila+1) ya no contiene un ArrayList pues ya fue extraido 
                // Puede darse el caso de que 'padre' tenga Cerradura de Kleene. Evalúo el caso
                if ((index+1) < maximo) {
                    if ("*".equals(""+cpER.charAt(index+1))) {  // 'padre' tiene Cerradura de Kleene
                        padre.setRepeticion("*");
                        index++;
                    }
                }
                // Puede o no seguir otro grupo, el cual puede estar sumado o concatenado.
                if ((index+1) < maximo) {   // Siguen más grupos los cuales pueden estar sumados o concatenados
                    if ("+".equals(""+cpER.charAt(index+1))) {  // El siguiente grupo está sumandose
                        padre.setPostOperador("+");
                        index++;
                    } else if ("(".equals(""+cpER.charAt(index+1))) {   // El siguiente grupo está concatenándose (inicia un nuevo grupo)
                        padre.setPostOperador(".");
                    } else if (")".equals(""+cpER.charAt(index+1))) {   // Se tiene el cierre del grupo actual
                        padre.setPostOperador("");
                    } else {    // Lo que sigue es un símbolo, que está concatenándose
                        padre.setPostOperador(".");
                    }
                }   // En caso de agotar la cadena, su postOperador será nulo
                index++;
            } else {    // Implica que lo que viene son operandos y operadores
                boolean continuar = false;
                while (!continuar) {    // En esta parte concateno todo un operando (separados por '+')
                    nuevo.setPedazoExpresion(caracter); // Agrego esta parte a nuevo
                    index++;
                    caracter = ""+cpER.charAt(index);   // Obtengo el siguiente caracter
                    if ("+".equals(caracter) || "(".equals(caracter) || ")".equals(caracter)) { // Se puede finalizar el nuevo grupo por tres sucesos
                        continuar = true;   // Para cerrar el ciclo
                    }
                }   // Hasta aquí, ya se a agregado la expresión regular de nuevo
                // El ciclo anterior se cierra por finalización de grupo, ahora puede o no seguir otro (si sigue otro, puede estar sumado o concatenado)
                pilaGrupos.get(fila).add(nuevo);
                /* Defino el preOperador y el postOperador de nuevo */
                int conteo = pilaGrupos.get(fila).size() - 1;   // Obtengo la posición ocupada hasta el momento
                nuevo.setPreOperador((conteo > 0) ? pilaGrupos.get(fila).get(conteo-1).getPostOperador(): "");
                // Para el posOperador, 'caracter' tiene '+', '(' o ')'. Evalúo el caso
                if ("+".equals(caracter)) { // Su postOperador es '+'
                    nuevo.setPostOperador("+");
                    index++;    // Obtengo el índice del siguiente caracter (se leerá al repetir el ciclo)
                } else if ("(".equals(caracter)) {  // Su posOperador es '.' (concatenación)
                    nuevo.setPostOperador(".");
                } else {    // Se cierra el grupo, por lo que no tiene postOperador
                    nuevo.setPostOperador("");// Sin postOperador
                }
            }   // Hasta aquí, ya se ha creado y agregado un nuevo grupo, pero se puede seguir en el mismo nivel
            // Previo a reiniciar el grupo, index ya está sobre un caracter que no sea '+'
        }
        this.general = pilaGrupos.get(0).get(0);
        // Hasta aquí se garantiza la creación de tantos Grupos como sean necesarios (aunque no simplificados).
        simplificarGrupos(general); // Simplifico a lo más mínimo todos los grupos creados.
    }
    /**
     * Método recursivo que divide un Grupo en varios Grupos en caso de que la porción de la expresión regular que tenga
     * incluye varios operandos. En generarGrupos() se crearon varios Grupos delimitados por paréntesis o por el operador '+'
     * pero varios operandos (con cerradura de Kleene inclusive) concatenados pertenecen a un mismo Grupo. Aún está pendiente
     * subdividir dichos Grupos hasta que su expresión sólo contenga un caracter del alfabeto.
     * Los Grupos de los niveles más inferiores (los que tengan su ArrayList == null) son los que aún pueden ser
     * divididos en más Grupos. Evalúo dichos Grupos y genero la simplificación de la expresión de cada uno.
     * @param padre objeto ExpresionRegular.Grupo general que contiene todos los grupos creados (este es this.general).
     */
    private void simplificarGrupos(Grupo padre) {
        // La división se hace en el mismo nivel, por lo que se aumentará la cantidad de elementos en el subgrupo y se
        // deberán correr todas las siguientes a la que se evalúa.
        ArrayList<Grupo> gruposHijo = padre.getGrupos();
        int cantidad = gruposHijo.size();
        for(int i=0; i<cantidad; i++) {
            if (gruposHijo.get(i).getGrupos() != null)  // Si el grupo padre tiene más hijos, evalúo cada uno de sus hijos
                simplificarGrupos(gruposHijo.get(i));
            else {  // Si padre es un grupo final (sin grupos hijo). Simplifico dicho grupo
                // Las únicas operaciones que puede tener es concatenación o Cerrradura de Kleene de 'padre'.
                String cpER = gruposHijo.get(i).getExpresion(); // Obtengo la expresión
                int maximo = cpER.length();
                int index = 0;
                ArrayList<Grupo> subgrupo = new ArrayList<>();
                String caracter;
                while (index < maximo) {    // Siempre que no agote toda la cadena
                    Grupo nuevo = new Grupo();
                    caracter = ""+cpER.charAt(index);   // Obtengo el caracter actual
                    nuevo.setPedazoExpresion(caracter); // Inserto el símbolo existente en el alfabeto
                    subgrupo.add(nuevo);    // Agrego nuevo a la lista de Grupos que se generarán
                    int conteo = subgrupo.size() - 1;   // Por lo menos ya existe un elemento en el ArrayList
                    // Defino el preOperador de nuevo
                    nuevo.setPreOperador((conteo > 0) ? subgrupo.get(conteo-1).getPostOperador(): "");
                    if ((index+1) < maximo) {
                        if ("*".equals(""+cpER.charAt(index+1))) {  // El nuevo grupo tiene Cerradura de Kleene
                            nuevo.setRepeticion("*");
                            index++;
                        }
                    }
                    // Defino el postOperador de nuevo (se sabe que no puede ser '+')
                    nuevo.setPostOperador(((index+1) < maximo) ? ".": "");  // Si no se ha agotado la cadena, lo que le sigue está concatenado
                    index++;    // Para evaluar el siguiente caracter
                }   // Hasta aquí se garantiza la división de la expresión regular de 'padre' en subgrupos finales (de un sólo símbolo)
                gruposHijo.get(i).addGrupos(subgrupo);  // Agrego todo el nuevo grupo a 'padre'
            }
        }
    }
    @Override
    public String toString() {
        String cadena = "<"+general.toString()+">";
        return cadena;
    }
    
    /**
     * Clase que modela una porción de una Expresión Regular.
     * En esta clase, una expresión regular se modela parecida a un árbol ya que un grupo Padre puede tener cero o más
     * grupos Hijo. Para ello, cada grupo tiene un atributo para la porción de la expresión regular (que debe ser un sólo
     * operando), las operaciones que dicho grupo está realizando con los grupos anterior y siguiente a él, un atributo que
     * indica si todo el grupo tiene Cerradura de Kleene, un ArrayList<Grupo> de más Grupos (que en conjunto forma el Grupo
     * this), y punteros al EstadoAFN anterior y siguiente al Grupo actual (utilizado a la hora de construir el Autómata Finito
     * No Determinista).
     */
    public class Grupo {
        private String expresion;
        private String preOperador, postOperador;
        private String repeticion;
        private ArrayList<Grupo> grupos;
        private EstadoAFN anterior, siguiente;
        
        public Grupo() {
            expresion = preOperador = postOperador = repeticion = "";
            grupos = null;
            anterior = siguiente = null;
        }
        
        public String getExpresion() { return expresion; }
        public String getPreOperador() { return preOperador; }
        public String getPostOperador() { return postOperador; }
        public String getRepeticion() { return repeticion; }
        public ArrayList<Grupo> getGrupos() { return grupos; }
        public EstadoAFN getAnterior() { return anterior; }
        public EstadoAFN getSiguiente() { return siguiente; }
        @Override
        public String toString() {
            String texto = "{";
            Grupo iterador;
            for(int i=0; i<grupos.size(); i++) {
                iterador = grupos.get(i);
                if (iterador.getGrupos() != null)
                    texto+= iterador.toString();
                else {
                    texto+= "'"+iterador.getExpresion()+"'["+iterador.getRepeticion()+","+iterador.getPreOperador()+","+iterador.getPostOperador()+"]";
                }
            }
            texto+= "}["+repeticion+","+preOperador+","+postOperador+"]";
            return texto;
        }
        
        public void setExpresion(String expresion) { this.expresion = expresion; }
        public void setPedazoExpresion(String pedazoExpresion) { expresion+= pedazoExpresion; }
        public void setPreOperador(String preOperador) { this.preOperador = (".".equals(preOperador) || "+".equals(preOperador)) ? preOperador : ""; }
        public void setPostOperador(String postOperador) { this.postOperador = (".".equals(postOperador) || "+".equals(postOperador)) ? postOperador : ""; }
        public void setRepeticion(String repeticion) { this.repeticion = ("*".equals(repeticion)) ? repeticion : ""; }
        public void addGrupo(Grupo grupo) { this.grupos.add(grupo); }
        public void addGrupos(ArrayList<Grupo> grupos) { this.grupos = grupos; }
        public void setAnterior(EstadoAFN anterior) { this.anterior = anterior; }
        public void setSiguiente(EstadoAFN siguiente) { this.siguiente = siguiente; }
    }
}
