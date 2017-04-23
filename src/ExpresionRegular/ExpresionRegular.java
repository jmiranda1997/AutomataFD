/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExpresionRegular;

import java.util.ArrayList;
import AFN.*;

/**
 *
 * @author Wilson Xicará
 */
public class ExpresionRegular {
    private String expresionRegular;
    private boolean cadenaValida;
    private Grupo general;
    
    public ExpresionRegular(String cadenaER) {
        this.expresionRegular = cadenaER;
        this.cadenaValida = false;
        this.general = null;
    }
    
    public Grupo getGrupo() { return general; }
    
    public boolean validarER(String expresionRegular) {
        this.expresionRegular = expresionRegular;
        String cpER = "(" + this.expresionRegular + ")";    // Englobo todo en un sólo operando
        int nivel = 0, limite = cpER.length(), index = 0;
        String caracter = "";
        boolean forzado = false;
        while (index < limite) {
            caracter = "" + cpER.charAt(index);
            if ("(".equals(caracter)) {
                nivel++;
                index++;
            } else if (")".equals(caracter)) {
                // Si se tienen los bloques +) o ()
                forzado = ("+".equals(""+cpER.charAt(index-1)) || "(".equals(""+cpER.charAt(index-1)));
                nivel--;
                index++;
            }
            // Evalúo que no haya dos operadores consecutivos (el caso *+ si es válido {a*+b})
            else if ("*".equals(caracter)) {
                if ("(".equals(""+cpER.charAt(index-1)) || "+".equals(""+cpER.charAt(index-1)) || "*".equals(""+cpER.charAt(index-1))) {
                    // Si se tienen los bloques no válidos (*, +*, o **
                    forzado = true;
                    index = limite;
                } else
                    index++;
            } else if ("+".equals(caracter)) {
                if ("(".equals(""+cpER.charAt(index-1)) || "+".equals(""+cpER.charAt(index-1))) {
                    // Si se tienen los bloques no válidos (+ o ++ (el bloque *+ es válido)
                    forzado = true;
                    index = limite;
                } else
                    index++;
            } else {
                index++;
            }
            if (nivel < 0 || forzado) {
                forzado = true;
                index = limite;
            }
        }
        return !(forzado || nivel != 0);
    }
    public void generarGrupos() {
        /* Previo a iniciar, la expresión regular tiene el formato (expresionRegular) con paréntesis al inicio y al final.
           Dichos paréntesis me servirán para identificar el inicio y fin de toda la expresión.
           Al llamar a esta función, se asume que this.expresionRegular es válida */
        String cpER = "("+expresionRegular+")";
        int fila = 0;
        int  maximo = cpER.length();
        int index = 0;  // La expresión regular ya no está encerrada en paréntesis que la engloban
        this.general = new Grupo();
        String caracter = "";
        
        /* Este ArrayList puede interpretarse como una matriz cuadrada (aunque no necesariamente de mxn).
           En ella, 'nivel' lleva el contador de las filas y 'conteo' lleva el contador de las columnas */
        ArrayList<ArrayList<Grupo>> pilaGrupos = new ArrayList<>();
//        ArrayList<Grupo> primerArray = new ArrayList<>();
        pilaGrupos.add(new ArrayList<Grupo>());    // Para poder utilizar la pilaGrupos
        
        while (index < maximo) {    // Mientras no se agote la cadena
            /* Se reinicia este ciclo siempre que se crea un nuevo grupo en el nivel en el que indica 'fila' */
            Grupo nuevo = new Grupo();
            caracter = "" + cpER.charAt(index);
            if ("(".equals(caracter)) { // Implica que se creará un nuevo grupo. Agrego un preOperador del grupo
                pilaGrupos.get(fila).add(nuevo);    // Agrego el nuevo grupo en la 'fila' que le corresponde
                pilaGrupos.add(new ArrayList<Grupo>()); // Agrego lo que será los grupos de nuevo
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
        /* Hasta este punto, ya se han creado tantos grupos como sean necesarios. Dichos grupos fueron delimitados por
           paréntesis o por '+' o concatenación, pero de momento cada grupo puede tener más de un símbolo como expresión.
           Queda pendiente subdividir aún más dichos estados hasta que su expresión sólo contenga un caracter.
           Los Grupos de los niveles más inferiores (los que tengan su ArrayList == null) son los que aún pueden ser
           divididos en más estados. Evalúo dichos Grupos y genero la simplificación de la expresión de cada uno. */
        System.out.println("Resultado previo = "+this.toString());
        simplificarGrupos(general);
    }
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
        public void iniciarGrupo() {
            grupos = new ArrayList<Grupo>();
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
        
        public int generarGrupos(int posActual, int maximo, String cadena) {
            grupos = new ArrayList<>(); // Inicializo el ArrayList
            String simbolo;
            int contGrupos = 0;
            boolean regresar = false;
            while (!regresar) { // Si entra a este método, por lo menos se agregará un grupo a 'grupos'
                simbolo = ""+cadena.charAt(posActual);
                Grupo nuevo = null;  // Inicializo un nuevo grupo que contendrá la expresión que se leerá
                if ("(".equals(simbolo)) {
                    nuevo = new Grupo();
                    // SE CREARÁ UN NUEVO GRUPO, lo que implica una nueva llamada a este método recursivo
                    posActual = nuevo.generarGrupos(posActual+1, maximo, cadena);
                    // Al regresar, posActual está hasta donde terminó el grupo anterior, y le sigue un operador o un operando
                } else if (")".equals(simbolo)) {
                    // Se cerrará el grupo actual.
                    regresar = true;
                } else {    // Pueden suceder serie de caracteres concatenados, o separados por +
                    nuevo = new Grupo();
                    boolean pasar = false;
                    while (!pasar) {    // Se leerá un bloque de la ER que contenga la misma operación (de concatenación)
                        simbolo = ""+cadena.charAt(posActual);
                        if ("+".equals(simbolo) || ")".equals(simbolo)) {
                            pasar = true;
                            if (")".equals(simbolo)) {
                                if ((posActual+1) < maximo) {
                                    posActual++;
                                    simbolo = "" + cadena.charAt(posActual);
                                    regresar = true;
                                }
                            }
                        } else {    // Hay operación de concatenación
                            nuevo.setPedazoExpresion(simbolo);    // Agrego el operando a nuevo
                            posActual++;
                            simbolo = "" + cadena.charAt(posActual);
                        }
                    }   // Sale de este ciclo sí y sólo sí se ha encontrado un + (ya se ha cargado un grupo a nuevo)
                    
                    // Inserto el preOperador de nuevo
                    nuevo.setPreOperador((contGrupos > 0) ? grupos.get(contGrupos-1).getPostOperador() : "");
                    // Inserto el postOperador de nuevo. Si sale del ciclo por encontrar ')', no tienen postOperador
                    if ("+".equals(simbolo)) {
                        nuevo.setPostOperador("+");
                        posActual++;
                    }   // En caso contrario, se queda sin postOperador
                }
                if (nuevo != null)
                    grupos.add(nuevo);
                contGrupos = grupos.size();
            }   // Al salir de este while, ya se puede hacer el cierre del grupo
            // Evalúo la repetición de este grupo
            if ((posActual+1) < maximo) {   // Aún hay más caracteres
                if ("*".equals(""+cadena.charAt(posActual+1))) {
                    this.setRepeticion("*");    // Si es que el grupo actual tiene cerradura de Kleen
                    posActual++;
                }
            }
            return posActual;
        }
    }
}
