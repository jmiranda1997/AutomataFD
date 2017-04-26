/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import AFN.AFN;
import AFN.EstadoAFN;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Wilson Xicará
 */
public class ManejoArchivo {
    public static final String SEPARADOR = System.getProperty("file.separator");
    public static final String CARPETA_PRINCIPAL =  System.getProperty("user.home")+ SEPARADOR +"Automatas_Estado-Finito";
    public static final String CARPETA_AFD = CARPETA_PRINCIPAL + SEPARADOR + "AFD";
    public static final String CARPETA_AFN = CARPETA_PRINCIPAL + SEPARADOR + "AFN";
    public static final String CARPETA_IMAGENES = CARPETA_AFD + SEPARADOR + "IMAGENES";

    public ManejoArchivo() {

        // Verifico la existencia de la carpeta principal. De no existir, la creo con los archivos sin registros.
        verificarArchivos();    // Crea las rutas y archivos necesarios, si estos no existen
    }
    /**
     * Método que verifica la existencia de la ruta principal donde se almacenarán los datos de los Autómatas que se
     * crearán. Si la ruta no existen, lo crea y llama al método encargado de la creación de los archivos necesarios.
     */
    private void verificarArchivos() {
        File carpetaPrincipal = new File(CARPETA_PRINCIPAL);
        // Si la carpeta principal existe no existe, la creo con archivos sin registros
        if (!carpetaPrincipal.exists()) {
            System.out.println("No se encontró la carpeta principal: "+CARPETA_PRINCIPAL);
            carpetaPrincipal.mkdirs();  // Crea la carpeta principal
            crearArchivosVacios();  // Se crean los directorios y los archivos vacíos para los AFD y los AFN que se guardarán
        }
    }
    /**
     * Método que crea los archivos necesarios, con registros vacios.
     */
    private void crearArchivosVacios() {
        /* Se crearán los siguientes archivos (incluidos los subdirectorios):
            Automatas_Estado-Finito/AFD/Automatas.afd
            Automatas_Estado-Finito/AFD/Descripcion.afd
            Automatas_Estado-Finito/AFN/Automatas.afn
            Automatas_Estado-Finito/AFN/Descripcion.afn
        */
        RandomAccessFile archivo;
        File subdirectorio;
        try {
            String finArchivo = ""+(char)0xFF+(char)0xFF;
            /** AUTÓMATA FINITO DETERMINISTA **/
            /* Creación del archivo Automatas_Estado-Finito/AFD/Automatas.afd */
            subdirectorio = new File(CARPETA_AFD);
            subdirectorio.mkdirs();     // Inicialización del directorio
            archivo = new RandomAccessFile(subdirectorio + SEPARADOR + "Automatas.afd", "rw");
            archivo.writeBytes("AFD");  // Escritura de la Firma
            archivo.writeBytes("dat");  // Escritura del Indicador del archivo: 'dat' = Datos de los autómatas
            archivo.writeShort(8);      // Escritura del Puntero al Índice de registros (2 bytes)
            archivo.writeByte(0);       // Escritura de la Cantidad de Registros en el Índice (2 bytes)
            archivo.writeBytes(finArchivo);    // Escritura del Indicador de Fin de Archivo
            archivo.close();

            /* Creación del archivo Automatas_Estado-Finito/AFD/Descripcion.afd */
            archivo = new RandomAccessFile(subdirectorio + SEPARADOR + "Descripcion.afd", "rw");
            archivo.writeBytes("AFD");  // Escritura de la Firma
            archivo.writeBytes("inf");  // Escritura del Indicador del archivo: 'inf' = Información de los autómatas
            archivo.writeShort(8);      // Escritura del Puntero al Índice de registros (2 bytes)
            archivo.writeByte(0);       // Escritura de la Cantidad de Registros en el Índice (2 bytes)
            archivo.writeBytes(finArchivo);    // Escritura del Indicador de Fin de Archivo
            archivo.close();
            
            /* Creación del archivo IMGENES*/
            subdirectorio = new File(CARPETA_IMAGENES);
            subdirectorio.mkdirs();
            
            /** AUTÓMATA FINITO NO DETERMINISTA **/
            /* Creación del archivo Automatas_Estado-Finito/AFN/Automatas.afn */
            subdirectorio = new File(CARPETA_AFN);
            subdirectorio.mkdirs();     // Inicialización del directorio
            archivo = new RandomAccessFile(subdirectorio + SEPARADOR + "Automatas.afn", "rw");
            archivo.writeBytes("AFN");  // Escritura de la Firma
            archivo.writeBytes("dat");  // Escritura del Indicador del archivo: 'dat' = Datos de los autómatas
            archivo.writeShort(8);      // Escritura del Puntero al Índice de registros (2 bytes)
            archivo.writeByte(0);       // Escritura de la Cantidad de Registros en el Índice (2 bytes)
            archivo.writeBytes(finArchivo);    // Escritura del Indicador de Fin de Archivo
            archivo.close();

            /* Creación del archivo Automatas_Estado-Finito/AFN/Descripcion.afn */
            archivo = new RandomAccessFile(subdirectorio + SEPARADOR + "Descripcion.afn", "rw");
            archivo.writeBytes("AFN");  // Escritura de la Firma
            archivo.writeBytes("inf");  // Escritura del Indicador del archivo: 'inf' = Información de los autómatas
            archivo.writeShort(8);      // Escritura del Puntero al Índice de registros (2 bytes)
            archivo.writeByte(0);       // Escritura de la Cantidad de Registros en el Índice (2 bytes)
            archivo.writeBytes(finArchivo);    // Escritura del Indicador de Fin de Archivo
            archivo.close();
            
            // HASTA AQUÍ SE GARANTIZA LA CREACIÓN DE TODOS LOS ARCHIVOS Y DIRECTORIOS NECESARIOS
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManejoArchivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ManejoArchivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
             Logger.getLogger(ManejoArchivo.class.getName()).log(Level.SEVERE, null, ex);
              System.out.println(ex.getMessage())/*Se imprime el mesaje de error si es que sucede*/;
         }
        
    }
    
    /**
     * Método que escribe los datos del Autómata 'nuevo' en el archivo. Para ello, primero verifica que no exista otro
     * Autómata con el mismo nombre que nuevo.getNombreAutomata(). Si nuevo.getNombreAutomata() ya existe o hay 255
     * autómatas guardados en el archivo, no se guardarán los datos de 'nuevo'; de lo contrario, se escriben todos los
     * datos de 'nuevo' y se actualizan todos los registros necesarios en el archivo.
     * @param nuevo objeto de tipo Automata que contiene los datos del nuevo autómata que se quiere guardar.
     */
    public void guardarAutomata(Automata nuevo) {
        RandomAccessFile archivo;
        try {
            int numeroRegistro = 0;
            archivo = new RandomAccessFile(CARPETA_AFD + SEPARADOR + "Automatas.afd", "rw");
            String firma = "", tipo = "";
            for(int i=0; i<3; i++) firma+= (char)Byte.toUnsignedInt(archivo.readByte());
            for(int i=0; i<3; i++) tipo+= (char)Byte.toUnsignedInt(archivo.readByte());
            // Compruebo si es el archivo Automatas_Estado-Finito/AFD/Automatas.afd
            if ("AFD".equals(firma) == true && "dat".equals(tipo) == true) {
                String descripcionNuevo = nuevo.getDescripcion();
                int punteroAlIndice = Short.toUnsignedInt(archivo.readShort()); // Obtengo el Puntero al Índice
                archivo.seek(punteroAlIndice);      // Salto hasta el Índice
                int cantidadRegistros = Byte.toUnsignedInt(archivo.readByte()); // Obtengo la Cantidad de Registros
                
                /* La escritura del nuevo Autómata consiste en cuatro pasos: */
                /** Paso 1: Verificación de su existencia (en el Índice de Referencias a Registros). **/
                boolean yaExiste = false;
                int comparacion = -1, punteroADatos = 8;
                long posicionInicioReferencia = archivo.getFilePointer();   // Obtengo la posición del primer registro en Índice
                for(int cont=0; cont<cantidadRegistros; cont++) {
                    numeroRegistro = cont;
                    posicionInicioReferencia = archivo.getFilePointer(); // Obtengo la posición de la i-ésima referencia en Índice
                    int longitudNombre = Byte.toUnsignedInt(archivo.readByte()); // Obtengo la longitud del i-ésimo nombre
                    String nombre = "";
                    for(int i=0; i<longitudNombre; i++) nombre+= (char)Byte.toUnsignedInt(archivo.readByte());  // Obtengo el i-ésimo nombre
                    punteroADatos = Short.toUnsignedInt(archivo.readShort());   // Obtengo el puntero del i-ésimo registro
                    
                    comparacion = compararCadenaPorOrden(nombre, nuevo.getNombre());
                    if (comparacion == 0) { // Si nuevo.getNombreAutomata() ya existe, finalizo el for y no se escribirá
                        yaExiste = true;
                        cont = cantidadRegistros;
                    }
                    if (comparacion == 1)   // Si nuevo.getNombreAutomta() < nombre, nuevo se insertará antes de 'nombre'
                        cont = cantidadRegistros;
                }   // Al salir, 'posicionInicioReferencia' y 'punteroADatos' definirán las posiciones de la nueva referencia
                // Debido a que debo actualizar (cantidadRegistros - numeroRegistro) referencias, verifico si 'nuevo' no se
                // ingresará al final del archivo ya que de así ser debería actualizar(cantidadRegistros - numeroRegistro = 1
                // referencias cuando en realidad debo actualizar cero referencias. Por eso, le sumo 1 de ser el caso
                if (numeroRegistro == (cantidadRegistros - 1) && comparacion == -1 && cantidadRegistros != 0) {
                    numeroRegistro++;
                    // La nueva referencia irá casi al final del archivo (en el penúltimo byte, antes de 'F')
                    posicionInicioReferencia = archivo.length() - 2;
                    // El nuevo registro empezará en donde está actualmente el Índice
                    punteroADatos = punteroAlIndice;
                }
                
                /** Paso 2: Escritura de la nueva referencia en el Índice (si yaExiste == false). **/
                if (yaExiste == false && cantidadRegistros < 255) {
                    // 'posicionInicioReferencia' contiene el puntero en el archivo donde se debe ecribir la referencia de nuevo
                    // 'punteroADatos' contiene el puntero en el archivo donde se debe escribir el registro de nuevo
                    archivo.seek(posicionInicioReferencia); // Me muevo a donde se ecribirá la nueva referencia
                    byte[] bloque = new byte[(int)(archivo.length() - posicionInicioReferencia)];
                    archivo.read(bloque);   // Copio todo lo que irá después de la nueva referencia
                    archivo.seek(posicionInicioReferencia); // Regreso nuevamente a donde se escribirá la nueva referencia
                    
                    // Escribo la nueva referencia
                    archivo.writeByte(nuevo.getNombre().length());  // Escritura de la Longitud del nombre del nuevo Autómata
                    archivo.writeBytes(nuevo.getNombre());  // Escritura del Nombre del nuevo Autómata
                    archivo.writeShort(punteroADatos);      // Escritura del Puntero al registro del nuevo Autómata
                    long posicionFinReferencia = archivo.getFilePointer();  // Obtengo la posición final en el archivo de la nueva referencia
                    archivo.write(bloque);      // Escribo todo el bloque que irá después de la nueva referencia
                    archivo.seek(posicionFinReferencia);    // Regreso nuevamenta al final de la nueva referencia
                    
                    // Modifico las referencias que están después de la referencia de 'nuevo'
                    int longitudBloqueNuevo = longitudBloqueDatosAutomata(nuevo);
                    for(int i=numeroRegistro; i<cantidadRegistros; i++) {
                        int longitudN = Byte.toUnsignedInt(archivo.readByte()); // Obtengo la longitud del i-ésimo nombre
                        archivo.skipBytes(longitudN);   // Salto el i-ésimo nombre
                        int punteroActual = Short.toUnsignedInt(archivo.readShort());   // Obtengo en puntero del i-ésimo nombre
                        archivo.seek(archivo.getFilePointer() - 2); // Regreso nuevamente al inicio del puntero del i-ésimo nombre
                        archivo.writeShort(punteroActual + longitudBloqueNuevo);    // Escribo la nueva posición al que apunta el i-ésimo nombre
                    }   // Hasta aquí se garantiza la actualización de todas referencias que lo requieren.
                    
                    // Modifico la Cantidad de Registros en el Índice
                    archivo.seek(punteroAlIndice);
                    archivo.writeByte(cantidadRegistros + 1);
                    
                    /** Paso 3: Escritura del nuevo registro en el archivo y actualización del Puntero al Índice. **/
                    // 'posicionInicioReferencia' contiene el puntero en el archivo donde se debe ecribir la referencia de nuevo
                    // 'punteroADatos' contiene el puntero en el archivo donde se debe escribir el registro de nuevo
                    archivo.seek(punteroADatos);    // Me muevo a donde se escribirá el registro de 'nuevo'
                    bloque = new byte[(int)(archivo.length() - punteroADatos)];
                    archivo.read(bloque);   // Copio todo lo que irá después del registro de 'nuevo'
                    archivo.seek(punteroADatos);    // Regreso nuevamente a donde se escribirá el registro de 'nuevo'
                    
                    // Escribo el Registro de 'nuevo'
                    archivo.writeByte(nuevo.getCantidadSimbolos()); // Escritura de la Cantidad de Símbolos de 'nuevo'
                    String[] auxArrayString = nuevo.getAlfabeto();    // Obtengo el Alfabeto de 'nuevo'
                    int cantidadSimb = nuevo.getCantidadSimbolos();   // Obtengo la Cantidad de Símbolos de 'nuevo'
                    for(int i=0; i<cantidadSimb; i++) archivo.writeBytes(auxArrayString[i]);  // Escritura de los Símbolos del alfabeto de 'nuevo'
                    
                    archivo.writeByte(nuevo.getCantidadEstados());  // Escritura de la Cantidad de Estados de 'nuevo'
                    int longitudNombres = nuevo.getLongitudNombreEstados();
                    archivo.writeByte(longitudNombres);    // Escritura de la cantidad de bytes que tienen el nombre de cada estado
                    auxArrayString = nuevo.getNombresEstados();   // Obtengo los Nombres de los Estados de 'nuevo'
                    int cantidadEstados = nuevo.getCantidadEstados();    // Obtengo la Cantidad de Estados de 'nuevo'
                    for(int i=0; i<cantidadEstados; i++) {   // Escritura de los Nombres de los Estados de 'nuevo'
                        int longitud = auxArrayString[i].length();
                        // Si el nombre tiene menos caracteres del que debería, le concateno (char)0 al inicio los que necesite.
                        for(int j=0; j<(longitudNombres-longitud); j++)
                            auxArrayString[i] = (char)0 + auxArrayString[i];
                        archivo.writeBytes(auxArrayString[i]);  // Escritura del Nombre del i-ésimo estado
                    }
                    archivo.writeByte(nuevo.getIndiceEstadoInicial());  // Escribo el índice (dentro del arreglo de estados) del Estado inicial
                    int auxArrayInt[][] = nuevo.getMatrizTransiciones();    // Obtengo la matriz de transiciones dentro de 'nuevo'
                    // Escritura del tipo de estado y los Índices (del array estados) a los que apunta cada estado-caracter de 'nuevo'
                    for(int est=0; est<cantidadEstados; est++) {
                        archivo.writeBoolean(nuevo.getEstados(est).esAceptable()); // Escribo el tipo del i-ésimo estado (aceptable o no)
                        for(int simb=0; simb<cantidadSimb; simb++)
                            archivo.writeByte(auxArrayInt[est][simb]);  // Escritura de la transicion del par estado-caracter
                    }   // Hasta aquí se garantiza la escritura del Registro de 'nuevo'
                    archivo.write(bloque);  // Escritura del bloque que va despues del Registro de 'nuevo'
                    
                    // Actualización del Puntero al Índice
                    archivo.seek(6);    // Me muevo hasta el inicio del Puntero al Índice
                    archivo.writeShort(punteroAlIndice + longitudBloqueDatosAutomata(nuevo));
                    
                    archivo.close();    // Cierro el archivo
                    
                    /** Paso 4: Escritura de la descripcion del Autómata 'nuevo'. **/
                    /* Si 'nuevo' se guardará, entonces aún hay espacio en Descripcion.afd; El Registro de nuevo irá después
                       del 'numeroRegistro'-ésimo registro del archivo Automatas.afd y será el mismo en este archivo */
                    archivo = new RandomAccessFile(CARPETA_AFD + SEPARADOR + "Descripcion.afd", "rw");
                    firma = tipo = "";
                    for(int i=0; i<3; i++) firma+= (char)Byte.toUnsignedInt(archivo.readByte());
                    for(int i=0; i<3; i++) tipo+= (char)Byte.toUnsignedInt(archivo.readByte());
                    // Compruebo si es el archivo Automatas_Estado-Finito/AFD/Descripcion.afd
                    if ("AFD".equals(firma) == true && "inf".equals(tipo) == true) {
                        punteroAlIndice = Short.toUnsignedInt(archivo.readShort()); // Obtengo el Puntero al Índice
                        archivo.seek(punteroAlIndice);  // Me muevo hasta el Índice
                        cantidadRegistros = Byte.toUnsignedInt(archivo.readByte()); // Obtengo la Cantidad de Registros
                        archivo.seek(archivo.getFilePointer() - 1); // Regreso nuevamente al inicio del Índice
                        archivo.writeByte(cantidadRegistros + 1);   // Aumento la Cantidad de Registros
                        
                        // Me desplazo hasta la referencia del 'numeroRegistro'-ésimo registro en el Índice
                        if (comparacion == -1) {    // Si 'nuevo' irá de último
                            // La nueva referencia irá casi al final del archivo (en el penúltimo byte, antes de 'F')
                            posicionInicioReferencia = archivo.length() - 2;
                            // El nuevo registro empezará en donde está actualmente el Índice
                            punteroADatos = punteroAlIndice;
                        } else {
                            archivo.skipBytes(2*numeroRegistro);    // Si 'nuevo' no irá de último
                            posicionInicioReferencia = archivo.getFilePointer();
                            punteroADatos = Short.toUnsignedInt(archivo.readShort());
                        }
                        archivo.seek(posicionInicioReferencia);
                        bloque = new byte[(int)(archivo.length() - archivo.getFilePointer())];
                        archivo.read(bloque);   // Copio todo el bloque que irá después de la nueva referencia
                        archivo.seek(posicionInicioReferencia);   // Regreso nuevamente al inicio de la nueva referencia
                        
                        // Escribo la nueva referencia
                        archivo.writeShort(punteroADatos);
                        // Escribo y actualizo las referencias siguientes a la de 'nuevo'
                        posicionFinReferencia = archivo.getFilePointer();
                        archivo.write(bloque);  // Escribo todo el bloque
                        archivo.seek(posicionFinReferencia);    // Regreso al final de la referencia de 'nuevo'
                        for(int i=numeroRegistro; i<cantidadRegistros; i++) {   // Actualizo las demás referencias
                            int punteroActual = Short.toUnsignedInt(archivo.readShort());
                            archivo.seek(archivo.getFilePointer() - 2);
                            // El nuevo registro tendrá: 1 byte de la longitud y descripcion.length() bytes de la descripcion
                            archivo.writeShort(punteroActual + 1 + descripcionNuevo.length());
                        }   // Hasta aquí se garantiza la modificación de las referencias que van después de la de 'nuevo'
                        
                        // Escritura del Registro correspondiente a 'nuevo'
                        archivo.seek(punteroADatos);    // Me muevo hasta donde se insertará el Registro de 'nuevo'
                        bloque = new byte[(int)(archivo.length() - archivo.getFilePointer())];
                        archivo.read(bloque);   // Copio todo el bloque
                        archivo.seek(punteroADatos);    // Regreso nuevamente a donde se insertará el Registro de 'nuevo'
                        // Escribo el Registro de 'nuevo'
                        if (descripcionNuevo.length() > 255)    // Acorto la 'descripcionNuevo' si tiene más de 255 caracteres
                            descripcionNuevo = descripcionNuevo.substring(0, 255);
                        archivo.writeByte(descripcionNuevo.length());
                        archivo.writeBytes(descripcionNuevo);   // Escribo el Registro de 'nuevo'
                        archivo.write(bloque);  // Escribo todo el bloque que va después del Registro de 'nuevo'
                        
                        // Actualizo el Puntero al Índice (al inicio del archivo)
                        archivo.seek(6);
                        archivo.writeShort(punteroAlIndice + 1 + descripcionNuevo.length());
                        
                        archivo.close();    // Cierro el archivo.
                    } else {
                        String mensajeError = "El guardará el autómata '"+nuevo.getNombre()+"', pero no su descripción (No se encontró el archivo).";
                        JOptionPane.showMessageDialog(null, mensajeError, "Error!", JOptionPane.ERROR_MESSAGE, null);
                    }
                } else {
                    String mensajeError = "El autómata '"+nuevo.getNombre()+"' ya existe.";
                    if (cantidadRegistros == 255)
                        mensajeError = "Se ha llegado al número máximo de autómatas que puede guardar.";
                    JOptionPane.showMessageDialog(null, mensajeError, "Error!", JOptionPane.ERROR_MESSAGE, null);
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el archivo", "Error!", JOptionPane.ERROR_MESSAGE, null);
            }   // Hasta aquí se garantiza la inserción (o no inserción) al archivo del Autómata 'nuevo'
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManejoArchivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ManejoArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Método que escribe los datos del Autómata Finito No Determinista 'nuevo' en el archivo. Para ello, primero verifica
     * que no exista otro AFN con el mismo nombre que nuevo.getNombre(). Si nuevo.getNombre() ya existe o hay 255
     * autómatas guardados en el archivo, no se guardarán los datos de 'nuevo'; de lo contrario, se escriben todos los
     * datos de 'nuevo' y se actualizan todos los registros necesarios en el archivo.
     * @param nuevo objeto de tipo AFN que contiene los datos del nuevo autómata que se quiere guardar.
     */
    public void guardarAFN(AFN nuevo) {
        RandomAccessFile archivo;
        try {
            int numeroRegistro = 0;
            archivo = new RandomAccessFile(CARPETA_AFN + SEPARADOR + "Automatas.afn", "rw");
            String firma = "", tipo = "";
            for(int i=0; i<3; i++) firma+= (char)Byte.toUnsignedInt(archivo.readByte());
            for(int i=0; i<3; i++) tipo+= (char)Byte.toUnsignedInt(archivo.readByte());
            // Compruebo si es el archivo Automatas_Estado-Finito/AFD/Automatas.afd
            if ("AFN".equals(firma) == true && "dat".equals(tipo) == true) {
                String descripcionNuevo = nuevo.getDescripcion();
                int punteroAlIndice = Short.toUnsignedInt(archivo.readShort()); // Obtengo el Puntero al Índice
                archivo.seek(punteroAlIndice);      // Salto hasta el Índice
                int cantidadRegistros = Byte.toUnsignedInt(archivo.readByte()); // Obtengo la Cantidad de Registros
                
                /* La escritura del nuevo Autómata consiste en cuatro pasos: */
                /** Paso 1: Verificación de su existencia (en el Índice de Referencias a Registros). **/
                boolean yaExiste = false;
                int comparacion = -1, punteroADatos = 8;
                long posicionInicioReferencia = archivo.getFilePointer();   // Obtengo la posición del primer registro en Índice
                for(int cont=0; cont<cantidadRegistros; cont++) {
                    numeroRegistro = cont;
                    posicionInicioReferencia = archivo.getFilePointer(); // Obtengo la posición de la i-ésima referencia en Índice
                    int longitudNombre = Byte.toUnsignedInt(archivo.readByte()); // Obtengo la longitud del i-ésimo nombre
                    String nombre = "";
                    for(int i=0; i<longitudNombre; i++) nombre+= (char)Byte.toUnsignedInt(archivo.readByte());  // Obtengo el i-ésimo nombre
                    punteroADatos = Short.toUnsignedInt(archivo.readShort());   // Obtengo el puntero del i-ésimo registro
                    
                    comparacion = compararCadenaPorOrden(nombre, nuevo.getNombre());
                    if (comparacion == 0) { // Si nuevo.getNombreAutomata() ya existe, finalizo el for y no se escribirá
                        yaExiste = true;
                        cont = cantidadRegistros;
                    }
                    if (comparacion == 1)   // Si nuevo.getNombreAutomta() < nombre, nuevo se insertará antes de 'nombre'
                        cont = cantidadRegistros;
                }   // Al salir, 'posicionInicioReferencia' y 'punteroADatos' definirán las posiciones de la nueva referencia
                // Debido a que debo actualizar (cantidadRegistros - numeroRegistro) referencias, verifico si 'nuevo' no se
                // ingresará al final del archivo ya que de así ser debería actualizar(cantidadRegistros - numeroRegistro = 1
                // referencias cuando en realidad debo actualizar cero referencias. Por eso, le sumo 1 de ser el caso
                if (numeroRegistro == (cantidadRegistros - 1) && comparacion == -1 && cantidadRegistros != 0) {
                    numeroRegistro++;
                    // La nueva referencia irá casi al final del archivo (en el penúltimo byte, antes de 'F')
                    posicionInicioReferencia = archivo.length() - 2;
                    // El nuevo registro empezará en donde está actualmente el Índice
                    punteroADatos = punteroAlIndice;
                }
                
                /** Paso 2: Escritura de la nueva referencia en el Índice (si yaExiste == false). **/
                if (!yaExiste && cantidadRegistros < 255) {
                    // 'posicionInicioReferencia' contiene el puntero en el archivo donde se debe ecribir la referencia de nuevo
                    // 'punteroADatos' contiene el puntero en el archivo donde se debe escribir el registro de nuevo
                    archivo.seek(posicionInicioReferencia); // Me muevo a donde se ecribirá la nueva referencia
                    byte[] bloque = new byte[(int)(archivo.length() - posicionInicioReferencia)];
                    archivo.read(bloque);   // Copio todo lo que irá después de la nueva referencia
                    archivo.seek(posicionInicioReferencia); // Regreso nuevamente a donde se escribirá la nueva referencia
                    
                    // Escribo la nueva referencia
                    archivo.writeByte(nuevo.getNombre().length());  // Escritura de la Longitud del nombre del nuevo Autómata
                    archivo.writeBytes(nuevo.getNombre());  // Escritura del Nombre del nuevo Autómata
                    archivo.writeShort(punteroADatos);      // Escritura del Puntero al registro del nuevo Autómata
                    long posicionFinReferencia = archivo.getFilePointer();  // Obtengo la posición final en el archivo de la nueva referencia
                    archivo.write(bloque);      // Escribo todo el bloque que irá después de la nueva referencia
                    archivo.seek(posicionFinReferencia);    // Regreso nuevamenta al final de la nueva referencia
                    
                    // Modifico las referencias que están después de la referencia de 'nuevo'
                    int longitudBloqueNuevo = longitudBloqueDatosAFN(nuevo);
                    for(int i=numeroRegistro; i<cantidadRegistros; i++) {
                        int longitudN = Byte.toUnsignedInt(archivo.readByte()); // Obtengo la longitud del i-ésimo nombre
                        archivo.skipBytes(longitudN);   // Salto el i-ésimo nombre
                        int punteroActual = Short.toUnsignedInt(archivo.readShort());   // Obtengo en puntero del i-ésimo nombre
                        archivo.seek(archivo.getFilePointer() - 2); // Regreso nuevamente al inicio del puntero del i-ésimo nombre
                        archivo.writeShort(punteroActual + longitudBloqueNuevo);    // Escribo la nueva posición al que apunta el i-ésimo nombre
                    }   // Hasta aquí se garantiza la actualización de todas referencias que lo requieren.
                    
                    // Modifico la Cantidad de Registros en el Índice
                    archivo.seek(punteroAlIndice);
                    archivo.writeByte(cantidadRegistros + 1);
                    
                    /** Paso 3: Escritura del nuevo registro en el archivo y actualización del Puntero al Índice. **/
                    // 'posicionInicioReferencia' contiene el puntero en el archivo donde se debe ecribir la referencia de nuevo
                    // 'punteroADatos' contiene el puntero en el archivo donde se debe escribir el registro de nuevo
                    archivo.seek(punteroADatos);    // Me muevo a donde se escribirá el registro de 'nuevo'
                    bloque = new byte[(int)(archivo.length() - punteroADatos)];
                    archivo.read(bloque);   // Copio todo lo que irá después del registro de 'nuevo'
                    archivo.seek(punteroADatos);    // Regreso nuevamente a donde se escribirá el registro de 'nuevo'
                    
                    // Escribo el Registro de 'nuevo'
                    archivo.writeByte(nuevo.getCantidadSimbolos()); // Escritura de la Cantidad de Símbolos de 'nuevo'
                    String[] auxArrayString = nuevo.getAlfabeto();    // Obtengo el Alfabeto de 'nuevo'
                    int cantidadSimb = nuevo.getCantidadSimbolos();   // Obtengo la Cantidad de Símbolos de 'nuevo'
                    for(int i=0; i<cantidadSimb; i++) archivo.writeBytes(auxArrayString[i]);  // Escritura de los Símbolos del alfabeto de 'nuevo'
                    
                    archivo.writeByte(nuevo.getCantidadEstados());  // Escritura de la Cantidad de Estados de 'nuevo'
                    int longitudNombres = nuevo.getLongitudNombreEstados();
                    archivo.writeByte(longitudNombres);    // Escritura de la cantidad de bytes que tienen el nombre de cada estado
                    auxArrayString = nuevo.getNombresEstados();   // Obtengo los Nombres de los Estados de 'nuevo'
                    int cantidadEstados = nuevo.getCantidadEstados();    // Obtengo la Cantidad de Estados de 'nuevo'
                    for(int i=0; i<cantidadEstados; i++) {   // Escritura de los Nombres de los Estados de 'nuevo'
                        int longitud = auxArrayString[i].length();
                        // Si el nombre tiene menos caracteres del que debería, le concateno (char)0 al inicio los que necesite.
                        for(int j=0; j<(longitudNombres-longitud); j++)
                            auxArrayString[i] = (char)0 + auxArrayString[i];
                        archivo.writeBytes(auxArrayString[i]);  // Escritura del Nombre del i-ésimo estado
                    }
                    archivo.writeByte(0);  // Escribo el índice (dentro del arreglo de estados) del Estado inicial (siempre es el primero)
                    
                    // Ahora escribo las transiciones de todos los estados del AFN. Pueden haber estados con cero o más transiciones para
                    // un símbolo del alfabeto, incluido la cadena vacía.
                    for(int contEst=0; contEst<cantidadEstados; contEst++) {
                        archivo.writeBoolean(nuevo.getEstados()[contEst].esAceptable());  // Escribo el tipo del i-ésimo estado (aceptable o no)
                        int contSimb;
                        for(contSimb=0; contSimb<cantidadSimb; contSimb++) {
                            // Obtengo las transiciones del i-ésimo estado con el j-ésimo símbolo del alfabeto
                            int[] arrayTransiciones = nuevo.indiceTransiciones(contEst, contSimb);
                            int tamaño = (arrayTransiciones != null) ? arrayTransiciones.length : 0;
                            archivo.writeByte(tamaño);  // Escritura de la Cantidad de transiciones con el contSimb-ésimo símbolo
                            for(int k=0; k<tamaño; k++)
                                archivo.writeByte(arrayTransiciones[k]);    // Escritura de la transición dle par estado-caracter
                        }
                        // Obtengo las transiciones del i-ésimo estado con la cadena vacía
                        int[] arrayTransiciones = nuevo.indiceTransiciones(contEst, contSimb);
                        int tamaño = (arrayTransiciones != null) ? arrayTransiciones.length : 0;
                        archivo.writeByte(tamaño);  // Escritura de la Cantidad de transiciones con la cadena vacía
                        for(int k=0; k<tamaño; k++)
                            archivo.writeByte(arrayTransiciones[k]);    // Escritura de la transición dle par estado-caracter
                    }   // Hasta aquí se garantiza la escritura del Registro de 'nuevo'
                    archivo.write(bloque);  // Escritura del bloque que va despues del Registro de 'nuevo'
                    
                    // Actualización del Puntero al Índice
                    archivo.seek(6);    // Me muevo hasta el inicio del Puntero al Índice
                    archivo.writeShort(punteroAlIndice + longitudBloqueDatosAFN(nuevo));
                    archivo.close();    // Cierro el archivo
                    
                    /** Paso 4: Escritura de la descripcion del Autómata 'nuevo'. **/
                    /* Si 'nuevo' se guardará, entonces aún hay espacio en Descripcion.afd; El Registro de nuevo irá después
                       del 'numeroRegistro'-ésimo registro del archivo Automatas.afd y será el mismo en este archivo */
                    archivo = new RandomAccessFile(CARPETA_AFN + SEPARADOR + "Descripcion.afn", "rw");
                    firma = tipo = "";
                    for(int i=0; i<3; i++) firma+= (char)Byte.toUnsignedInt(archivo.readByte());
                    for(int i=0; i<3; i++) tipo+= (char)Byte.toUnsignedInt(archivo.readByte());
                    // Compruebo si es el archivo Automatas_Estado-Finito/AFD/Descripcion.afd
                    if ("AFN".equals(firma) == true && "inf".equals(tipo) == true) {
                        punteroAlIndice = Short.toUnsignedInt(archivo.readShort()); // Obtengo el Puntero al Índice
                        archivo.seek(punteroAlIndice);  // Me muevo hasta el Índice
                        cantidadRegistros = Byte.toUnsignedInt(archivo.readByte()); // Obtengo la Cantidad de Registros
                        archivo.seek(archivo.getFilePointer() - 1); // Regreso nuevamente al inicio del Índice
                        archivo.writeByte(cantidadRegistros + 1);   // Aumento la Cantidad de Registros
                        
                        // Me desplazo hasta la referencia del 'numeroRegistro'-ésimo registro en el Índice
                        if (comparacion == -1) {    // Si 'nuevo' irá de último
                            // La nueva referencia irá casi al final del archivo (en el penúltimo byte, antes de 'F')
                            posicionInicioReferencia = archivo.length() - 2;
                            // El nuevo registro empezará en donde está actualmente el Índice
                            punteroADatos = punteroAlIndice;
                        } else {
                            archivo.skipBytes(2*numeroRegistro);    // Si 'nuevo' no irá de último
                            posicionInicioReferencia = archivo.getFilePointer();
                            punteroADatos = Short.toUnsignedInt(archivo.readShort());
                        }
                        archivo.seek(posicionInicioReferencia);
                        bloque = new byte[(int)(archivo.length() - archivo.getFilePointer())];
                        archivo.read(bloque);   // Copio todo el bloque que irá después de la nueva referencia
                        archivo.seek(posicionInicioReferencia);   // Regreso nuevamente al inicio de la nueva referencia
                        
                        // Escribo la nueva referencia
                        archivo.writeShort(punteroADatos);
                        // Escribo y actualizo las referencias siguientes a la de 'nuevo'
                        posicionFinReferencia = archivo.getFilePointer();
                        archivo.write(bloque);  // Escribo todo el bloque
                        archivo.seek(posicionFinReferencia);    // Regreso al final de la referencia de 'nuevo'
                        for(int i=numeroRegistro; i<cantidadRegistros; i++) {   // Actualizo las demás referencias
                            int punteroActual = Short.toUnsignedInt(archivo.readShort());
                            archivo.seek(archivo.getFilePointer() - 2);
                            // El nuevo registro tendrá: 1 byte de la longitud y descripcion.length() bytes de la descripcion
                            archivo.writeShort(punteroActual + 1 + descripcionNuevo.length());
                        }   // Hasta aquí se garantiza la modificación de las referencias que van después de la de 'nuevo'
                        
                        // Escritura del Registro correspondiente a 'nuevo'
                        archivo.seek(punteroADatos);    // Me muevo hasta donde se insertará el Registro de 'nuevo'
                        bloque = new byte[(int)(archivo.length() - archivo.getFilePointer())];
                        archivo.read(bloque);   // Copio todo el bloque
                        archivo.seek(punteroADatos);    // Regreso nuevamente a donde se insertará el Registro de 'nuevo'
                        // Escribo el Registro de 'nuevo'
                        if (descripcionNuevo.length() > 255)    // Acorto la 'descripcionNuevo' si tiene más de 255 caracteres
                            descripcionNuevo = descripcionNuevo.substring(0, 255);
                        archivo.writeByte(descripcionNuevo.length());
                        archivo.writeBytes(descripcionNuevo);   // Escribo el Registro de 'nuevo'
                        archivo.write(bloque);  // Escribo todo el bloque que va después del Registro de 'nuevo'
                        
                        // Actualizo el Puntero al Índice (al inicio del archivo)
                        archivo.seek(6);
                        archivo.writeShort(punteroAlIndice + 1 + descripcionNuevo.length());
                        
                        archivo.close();    // Cierro el archivo.
                    } else {
                        String mensajeError = "El guardará el autómata '"+nuevo.getNombre()+"', pero no su descripción (No se encontró el archivo).";
                        JOptionPane.showMessageDialog(null, mensajeError, "Error!", JOptionPane.ERROR_MESSAGE, null);
                    }
                } else {
                    String mensajeError = "El autómata '"+nuevo.getNombre()+"' ya existe.";
                    if (cantidadRegistros == 255)
                        mensajeError = "Se ha llegado al número máximo de autómatas que puede guardar.";
                    JOptionPane.showMessageDialog(null, mensajeError, "Error!", JOptionPane.ERROR_MESSAGE, null);
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el archivo", "Error!", JOptionPane.ERROR_MESSAGE, null);
            }   // Hasta aquí se garantiza la inserción (o no inserción) al archivo del Autómata 'nuevo'
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManejoArchivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ManejoArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void exportarAutomata(File directorio, Automata automata) {
        RandomAccessFile exportador;
        try {
            String finArchivo = ""+(char)0xFF+(char)0xFF;   // Indicador de Fin del Archivo
            /* Archivo que se generar· con los datos exportados */
            exportador = new RandomAccessFile(directorio+".afd", "rw"); // Para el archivo a exportar
            exportador.writeBytes("AFD");  // Escritura de la Firma
            exportador.writeBytes("exp");  // Escritura del Indicador del archivo: 'exp' = Datos exportados
            
            /* Inicio la escritura del bloque de datos del AutÛmata a exportar. Obtengo cada dato de 'autÛmata'. */
            exportador.writeByte(automata.getCantidadSimbolos());   // Escritura de la Cantidad de SÌmbolos
            String[] auxArrayString = automata.getAlfabeto();       // Obtengo el Alfabeto de 'automata'
            int cantidadSimb = automata.getCantidadSimbolos();      // Obtengo la Cantidad de SÌmbolos de 'automata'
            for(int i=0; i<cantidadSimb; i++) exportador.writeBytes(auxArrayString[i]);  // Escritura de los SÌmbolos del alfabeto de 'automata'

            exportador.writeByte(automata.getCantidadEstados());  // Escritura de la Cantidad de Estados de 'automata'
            int longitudNombres = automata.getLongitudNombreEstados();
            exportador.writeByte(longitudNombres);    // Escritura de la cantidad de bytes que tienen el nombre de cada estado
            auxArrayString = automata.getNombresEstados();   // Obtengo los Nombres de los Estados de 'automata'
            int cantidadEstados = automata.getCantidadEstados();    // Obtengo la Cantidad de Estados de 'automata'
            for(int i=0; i<cantidadEstados; i++) {   // Escritura de los Nombres de los Estados de 'automata'
                int longitud = auxArrayString[i].length();
                // Si el nombre tiene menos caracteres del que deberÌa, le concateno (char)0 al inicio los que necesite.
                for(int j=0; j<(longitudNombres-longitud); j++)
                    auxArrayString[i] = (char)0 + auxArrayString[i];
                exportador.writeBytes(auxArrayString[i]);  // Escritura del Nombre del i-Èsimo estado
            }
            exportador.writeByte(automata.getIndiceEstadoInicial());  // Escribo el Ìndice (dentro del arreglo de estados) del Estado inicial
            int auxArrayInt[][] = automata.getMatrizTransiciones();    // Obtengo la matriz de transiciones dentro de 'automata'
            // Escritura del tipo de estado y los Õndices (del array estados) a los que apunta cada estado-caracter de 'automata'
            for(int est=0; est<cantidadEstados; est++) {
                exportador.writeBoolean(automata.getEstados(est).esAceptable()); // Escribo el tipo del i-Èsimo estado (aceptable o no)
                for(int simb=0; simb<cantidadSimb; simb++)
                    exportador.writeByte(auxArrayInt[est][simb]);  // Escritura de la transicion del par estado-caracter
            }   // Hasta aquÌ se garantiza la escritura del Registro de 'automata'
            
            /* Inicio la escritura de la DescripciÛn del AutÛmata */
            exportador.writeByte(automata.getDescripcion().length());   // Escritura de la longitud de la DescripciÛn
            exportador.writeBytes(automata.getDescripcion());
            
            exportador.writeBytes(finArchivo);    // Escritura del Indicador de Fin de Archivo
            exportador.close();
            // Hasta aquÌ se garantiza la exportaciÛn de los Datos de 'automata'
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManejoArchivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ManejoArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void exportarAFN(File directorio, AFN automata) {
        RandomAccessFile exportador;
        try {
            String finArchivo = ""+(char)0xFF+(char)0xFF;   // Indicador de Fin del Archivo
            /* Archivo que se generar· con los datos exportados */
            exportador = new RandomAccessFile(directorio+".afn", "rw"); // Para el archivo a exportar
            exportador.writeBytes("AFN");  // Escritura de la Firma
            exportador.writeBytes("exp");  // Escritura del Indicador del archivo: 'exp' = Datos exportados
            
            /* Inicio la escritura del bloque de datos del AutómataFN a exportar. Obtengo cada dato de 'autómata'. */
            exportador.writeByte(automata.getCantidadSimbolos());   // Escritura de la Cantidad de SÌmbolos
            String[] auxArrayString = automata.getAlfabeto();       // Obtengo el Alfabeto de 'automata'
            int cantidadSimb = automata.getCantidadSimbolos();      // Obtengo la Cantidad de SÌmbolos de 'automata'
            for(int i=0; i<cantidadSimb; i++) exportador.writeBytes(auxArrayString[i]);  // Escritura de los SÌmbolos del alfabeto de 'automata'
            
            exportador.writeByte(automata.getCantidadEstados());  // Escritura de la Cantidad de Estados de 'automata'
            int longitudNombres = automata.getLongitudNombreEstados();
            exportador.writeByte(longitudNombres);    // Escritura de la cantidad de bytes que tienen el nombre de cada estado
            auxArrayString = automata.getNombresEstados();   // Obtengo los Nombres de los Estados de 'automata'
            int cantidadEstados = automata.getCantidadEstados();    // Obtengo la Cantidad de Estados de 'automata'
            for(int i=0; i<cantidadEstados; i++) {   // Escritura de los Nombres de los Estados de 'automata'
                int longitud = auxArrayString[i].length();
                // Si el nombre tiene menos caracteres del que deberÌa, le concateno (char)0 al inicio los que necesite.
                for(int j=0; j<(longitudNombres-longitud); j++)
                    auxArrayString[i] = (char)0 + auxArrayString[i];
                exportador.writeBytes(auxArrayString[i]);  // Escritura del Nombre del i-Èsimo estado
            }
            exportador.writeByte(0);  // Escribo el índice (dentro del arreglo de estados) del Estado inicial (siempre es el primero)

            // Ahora escribo las transiciones de todos los estados del AFN. Pueden haber estados con cero o más transiciones para
            // un símbolo del alfabeto, incluido la cadena vacía.
            for(int contEst=0; contEst<cantidadEstados; contEst++) {
                exportador.writeBoolean(automata.getEstados()[contEst].esAceptable());  // Escribo el tipo del i-ésimo estado (aceptable o no)
                int contSimb;
                for(contSimb=0; contSimb<cantidadSimb; contSimb++) {
                    // Obtengo las transiciones del i-ésimo estado con el j-ésimo símbolo del alfabeto
                    int[] arrayTransiciones = automata.indiceTransiciones(contEst, contSimb);
                    int tamaño = (arrayTransiciones != null) ? arrayTransiciones.length : 0;
                    exportador.writeByte(tamaño);  // Escritura de la Cantidad de transiciones con el contSimb-ésimo símbolo
                    for(int k=0; k<tamaño; k++)
                        exportador.writeByte(arrayTransiciones[k]);    // Escritura de la transición dle par estado-caracter
                }
                // Obtengo las transiciones del i-ésimo estado con la cadena vacía
                int[] arrayTransiciones = automata.indiceTransiciones(contEst, contSimb);
                int tamaño = (arrayTransiciones != null) ? arrayTransiciones.length : 0;
                exportador.writeByte(tamaño);  // Escritura de la Cantidad de transiciones con la cadena vacía
                for(int k=0; k<tamaño; k++)
                    exportador.writeByte(arrayTransiciones[k]);    // Escritura de la transición dle par estado-caracter
            }   // Hasta aquí se garantiza la escritura del Registro de 'nuevo'
            
            /* Inicio la escritura de la DescripciÛn del Autómata */
            exportador.writeByte(automata.getDescripcion().length());   // Escritura de la longitud de la DescripciÛn
            exportador.writeBytes(automata.getDescripcion());
            
            exportador.writeBytes(finArchivo);    // Escritura del Indicador de Fin de Archivo
            exportador.close();
            // Hasta aquÌ se garantiza la exportación de los Datos de 'automata'
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManejoArchivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ManejoArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public Automata importarAutomata(File rutaArchivo) {
        Automata nuevo = null;
        RandomAccessFile archivo;
        try {
            archivo = new RandomAccessFile(rutaArchivo, "rw");
            String firma = "", tipo = "";
            for(int i=0; i<3; i++) firma+= (char)Byte.toUnsignedInt(archivo.readByte());
            for(int i=0; i<3; i++) tipo+= (char)Byte.toUnsignedInt(archivo.readByte());
            if ("AFD".equals(firma) == true && "exp".equals(tipo) == true) {
                nuevo = new Automata();
                // ExtracciÛn de datos
                int cantidadSimbolos = Byte.toUnsignedInt(archivo.readByte());  // Obtengo la Cantidad de SÌmbolos
                String[] alfabeto = new String[cantidadSimbolos];
                for(int i=0; i<cantidadSimbolos; i++)   // Obtengo el Alfabeto
                    alfabeto[i] = ""+(char)Byte.toUnsignedInt(archivo.readByte());
                int cantidadEstados = Byte.toUnsignedInt(archivo.readByte());   // Obtengo la Cantidad de Estados
                nuevo.setAlfabeto(alfabeto);    // Inserto el Alfabeto en el AutÛmata
                nuevo.setCantidadEstados(cantidadEstados);  // Inserto la Cantidad de Estados en el AutÛmata
                int longNombreEstados = Byte.toUnsignedInt(archivo.readByte()); // Obtengo la cantidad de caracteres del nombre de los estados
                String[] nombresEstados = new String[cantidadEstados];
                for(int i=0; i<cantidadEstados; i++) {  // Inicio la extracciÛn de los Nombres de los Estados
                    nombresEstados[i] = "";
                    for(int j=0; j<longNombreEstados; j++) {
                        char caracter = (char)Byte.toUnsignedInt(archivo.readByte());   // Obtengo un caracter
                        if ((int)caracter != 0)
                            nombresEstados[i]+= caracter;
                    }
                }
                nuevo.setNombresEstados(nombresEstados);
                archivo.readByte();     // Lectura del Estado Inicial
                for(int i=0; i<cantidadEstados; i++) {  // ExtracciÛn del Tipo de Estado y sus Transiciones
                    nuevo.getEstados(i).setAceptable(archivo.readBoolean());    // Inserto el Tipo del i-Èsimo Estado
                    for(int j=0; j<cantidadSimbolos; j++) {
                        nuevo.getEstados(i).setTransicion(j, nuevo.getEstados()[Byte.toUnsignedInt(archivo.readByte())]);
                    }
                }
                int longitudDescripcion = Byte.toUnsignedInt(archivo.readByte());   // Obtengo la longitud de la DescripciÛn
                String descripcion = "";
                for(int i=0; i<longitudDescripcion; i++) descripcion+= (char)Byte.toUnsignedInt(archivo.readByte());
                nuevo.setDescripcion(descripcion);
                nuevo.setNombre(rutaArchivo.getName().substring(0, rutaArchivo.getName().length()-4));
                archivo.close();
                // Hasta aquÌ se garantiza la extracciÛn de los datos del AutÛmata en el archivo.
            } else {
                JOptionPane.showMessageDialog(null, "El archivo "+rutaArchivo.getName()+" no es un archivo válido!", "Error!", JOptionPane.ERROR_MESSAGE, null);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManejoArchivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ManejoArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nuevo;
    }
    public AFN importarAFN(File rutaArchivo) {
        AFN nuevo = null;
        RandomAccessFile archivo;
        try {
            archivo = new RandomAccessFile(rutaArchivo, "rw");
            String firma = "", tipo = "";
            for(int i=0; i<3; i++) firma+= (char)Byte.toUnsignedInt(archivo.readByte());
            for(int i=0; i<3; i++) tipo+= (char)Byte.toUnsignedInt(archivo.readByte());
            if ("AFN".equals(firma) == true && "exp".equals(tipo) == true) {
                // Extracción de datos
                int contSimb = Byte.toUnsignedInt(archivo.readByte());  // Obtengo la Cantidad de SÌmbolos
                String[] alfabeto = new String[contSimb];
                for(int i=0; i<contSimb; i++)   // Obtengo el Alfabeto
                    alfabeto[i] = ""+(char)Byte.toUnsignedInt(archivo.readByte());
                int contEst = Byte.toUnsignedInt(archivo.readByte());   // Obtengo la Cantidad de Estados
                nuevo = new AFN(alfabeto, contEst); // Inicializo el Autóma con su alfabeto y con los estados que tendrá
                int longNombreEstados = Byte.toUnsignedInt(archivo.readByte()); // Obtengo la cantidad de caracteres del nombre de los estados
                for(int i=0; i<contEst; i++) {  // Inicio la extracción de los Nombres de los Estados
                    String nombreEstado = "";
                    for(int j=0; j<longNombreEstados; j++) {
                        nombreEstado+= (char)Byte.toUnsignedInt(archivo.readByte());
                        /*char caracter = (char)Byte.toUnsignedInt(archivo.readByte());   // Obtengo un caracter
                        if ((int)caracter != 0)
                            nombreEstado+= caracter;*/
                        System.out.println("Nombre del Estado "+i+" = '"+nombreEstado+"'");
                    }
                    nuevo.getEstados()[i].setNombre(nombreEstado);  // Inserto el nombre
                }
                archivo.readByte();     // Lectura del Índice del Estado Inicial
                for(int i=0; i<contEst; i++) {  // Extracción del Tipo de Estado y sus Transiciones
                    nuevo.getEstados()[i].setAceptable(archivo.readBoolean());  // Inserto el Tipo del i-ésimo Estado
                    for(int j=0; j<=contSimb; j++) {    // Extrae las transiciones hasta la(s) transiciones de la cadena vacía
                        int contTrans = Byte.toUnsignedInt(archivo.readByte()); // Cantidad de Transiciones que el i-ésimo estado tiene con el símbolo alfabeto[j]
                        ArrayList<EstadoAFN> transiciones = new ArrayList<>();
                        for(int k=0; k<contTrans; k++) {
                            int index = Byte.toUnsignedInt(archivo.readByte()); // Obtención del index de la Transición
                            EstadoAFN est = nuevo.getEstados()[index];
                            transiciones.add(est);  // Agrego el estado al que llega con el símbolo alfabeto[k]
                        }
                        nuevo.setTransiciones(i, j, transiciones); // Inserto las transiciones del i-ésimo estado con el j-ésimo símbolo
                    }
                }   // Hasta aquí se garantiza la carga y creación de las Transiciones del Autómata
                int longitudDescripcion = Byte.toUnsignedInt(archivo.readByte());   // Obtengo la longitud de la Descripción
                String descripcion = "";
                for(int i=0; i<longitudDescripcion; i++) descripcion+= (char)Byte.toUnsignedInt(archivo.readByte());
                nuevo.setDescripcion(descripcion);
                nuevo.setNombre(rutaArchivo.getName().substring(0, rutaArchivo.getName().length()-4));
                archivo.close();
                // Hasta aquÌ se garantiza la extracción de datos y la construcción del AFN desde el archivo
            } else {
                JOptionPane.showMessageDialog(null, "El archivo "+rutaArchivo.getName()+" no es un archivo v·lido!", "Error!", JOptionPane.ERROR_MESSAGE, null);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManejoArchivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ManejoArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nuevo;
    }
    public void eliminarAutomata(IndiceAutomatas eliminar) {
        RandomAccessFile archivo;
        try {
            boolean eliminado = false;
            /** Eliminación de los registros de 'eliminar' en Automatas_Estado-Finito/AFD/Automatas.afd **/
            archivo = new RandomAccessFile(CARPETA_AFD + SEPARADOR + "Automatas.afd", "rw");
            String firma = "", tipo = "";
            for(int i=0; i<3; i++) firma+= (char)Byte.toUnsignedInt(archivo.readByte());
            for(int i=0; i<3; i++) tipo+= (char)Byte.toUnsignedInt(archivo.readByte());
            if ("AFD".equals(firma) == true && "dat".equals(tipo) == true) {
                // Una vez en el archivo, los punteros de los datos a borrar están en 'eliminar'
                // No es necesario realizar búsquedas ya que 'eliminar' tiene la información requerida para borrar el registro
                // 'eliminar' != null siempre que cantidadRegistros > 0
                int punteroAlIndice = Short.toUnsignedInt(archivo.readShort()); // Obtengo el Puntero al Índice
                archivo.seek(punteroAlIndice);  // Me muevo hasta el Índice
                int cantidadRegistros = Byte.toUnsignedInt(archivo.readByte()); // Obtengo la Cantidad de Registros
                archivo.seek(archivo.getFilePointer() - 1); // Regreso nuevamente al inico de la Cantidad de Registros
                archivo.writeByte(cantidadRegistros - 1);   // Disminuyo la cantidad de Registros
                
                /* Eliminación de la Referencia en el Índice */
                archivo.seek(eliminar.getPunteroARefDatos());     // Me muevo hasta la Referencia en el Índice
                int longReferencia = 1 + eliminar.getNombreAutomata().length() + 2; // Obtengo la longitud del Bloque Referencia
                archivo.skipBytes(longReferencia);  // Me desplazo hasta el final de la referencia
                int longRegistro = longitudRegistroAutomata(archivo, eliminar.getPunteroADatos());  // Obtengo la longitud del Bloque de Registro
                byte[] bloque = new byte[(int)(archivo.length() - archivo.getFilePointer())];
                archivo.read(bloque);   // Copio todo el bloque que va después de la Referencia a borrar
                archivo.seek(eliminar.getPunteroARefDatos());    // Regreso al inicio del bloque de la Referencia
                archivo.write(bloque);  // Escribo todo el bloque (sobreescribiendo el bloque de referencia de 'elinar')
                archivo.seek(eliminar.getPunteroARefDatos());     // Regreso al inicio del bloque escrito
                archivo.setLength(archivo.length() - longReferencia);   // Acorto el tamaño del archivo
                // Inicio la actualización de los Punteros de las demas Referencias en el Índice
                int longNombre = Byte.toUnsignedInt(archivo.readByte());
                while (longNombre != 255) {   // Mientras no se llegue al final del archivo
                    archivo.skipBytes(longNombre);  // Salto los bytes del Nombre
                    int punteroActual = Short.toUnsignedInt(archivo.readShort());   // Obtengo el Puntero actual de la Referencia
                    archivo.seek(archivo.getFilePointer() - 2);     // Regreso al inicio del Puntero Actual
                    archivo.writeShort(punteroActual - longRegistro);   // Modifico el Puntero a Datos
                    longNombre = Byte.toUnsignedInt(archivo.readByte());    // Leo la longitud del siguiente nombre
                }   // Hasta aquí se garantiza la actualización de todas las referencias que lo requieran
                
                /* Eliminación del Registro de datos de 'eliminar' */
                archivo.seek(eliminar.getPunteroADatos() + longRegistro);   // Me muevo hasta el final del Registro de Datos
                bloque = new byte[(int)(archivo.length() - archivo.getFilePointer())];
                archivo.read(bloque);   // Copio todos los bytes que están después del Registro a borrar
                archivo.seek(eliminar.getPunteroADatos());  // Regreso al inicio del bloque de Registro de Datos
                archivo.write(bloque);  // Borro el Registro de 'eliminar' (sobreescribiéndolo)
                archivo.setLength(archivo.length() - longRegistro); // Acorto el tamaño del archivo (eliminando los bytes inservibles)
                // Modificación del Puntero al Índice
                archivo.seek(6);    // Me desplazo hasta el inicio del Puntero al Índice
                archivo.writeShort(punteroAlIndice - longRegistro); // Modifico el Puntero al Índice
                eliminado = true;
                
                archivo.close();
                /* Hasta aquí se garantiza la eliminación de Datos correspondiente a 'eliminar' */
                /*Elimina la imagen del automata*/
                File imagen = new File(CARPETA_AFD + SEPARADOR + "IMAGENES" + SEPARADOR + eliminar.getNombreAutomata() + ".png");
                while (imagen.exists() == true)
                    imagen.delete();
            } else {
                JOptionPane.showMessageDialog(null, "No se puede encontrar el archivo que contiene la informaicón de '"+eliminar.getNombreAutomata()+"'.", "Error!", JOptionPane.ERROR_MESSAGE, null);
            }
            
            /** Eliminación de los registros de 'eliminar' en Automatas_Estado-Finito/AFD/Descripcion.afd **/
            archivo = new RandomAccessFile(CARPETA_AFD + SEPARADOR + "Descripcion.afd", "rw");
            firma = tipo = "";
            for(int i=0; i<3; i++) firma+= (char)Byte.toUnsignedInt(archivo.readByte());
            for(int i=0; i<3; i++) tipo+= (char)Byte.toUnsignedInt(archivo.readByte());
            if ("AFD".equals(firma) == true && "inf".equals(tipo) == true && eliminado == true) {
                int punteroAlIndice = Short.toUnsignedInt(archivo.readShort());   // Obtengo el Puntero al Índice
                archivo.seek(punteroAlIndice);  // Me muevo hasta el puntero al Índice
                int cantidadRegistros = Byte.toUnsignedInt(archivo.readByte()); // Obtengo la Cantidad de Registros
                archivo.seek(archivo.getFilePointer() - 1);
                archivo.writeByte(cantidadRegistros - 1);   // Modifico la Cantidad de Registros
                
                /* Eliminación de la Referencia en el Índice */
                archivo.seek(eliminar.getPunteroARefDescripcion() + 2); // Me muevo hasta el final del puntero el en Índice
                byte[] bloque = new byte[(int)(archivo.length() - archivo.getFilePointer())];
                archivo.read(bloque);   // Copio todo el bloque
                archivo.seek(eliminar.getPunteroARefDescripcion()); // Regreso al inico del Puntero en el Índice
                archivo.write(bloque);  // Escribo todo el bloque
                archivo.seek(eliminar.getPunteroARefDescripcion()); // Regreso al inico del Puntero en el Índice
                archivo.setLength(archivo.length() - 2);    // Acorto el tamaño del archivo (que borra la referencia)
                // Actualización de las Referencias siguientes al de 'eliminado'
                int punteroActual = Short.toUnsignedInt(archivo.readShort());
                while (punteroActual != 65535) {
                    archivo.seek(archivo.getFilePointer() - 2);
                    archivo.writeShort(punteroActual - 1 - eliminar.getDescripcion().length()); // Modifico el i-ésimo puntero
                    punteroActual = Short.toUnsignedInt(archivo.readShort());   // Obtengo el (i+1)-ésimo puntero
                }   // Hasta aquí se garantiza la actualización de todas las referencias que lo requieran
                
                /* Eliminación del Registro de Descripción */
                archivo.seek(eliminar.getPunteroADescripcion() + 1 + eliminar.getDescripcion().length());   // Me muevo hasta el final del bloque de Descripción
                bloque = new byte[(int)(archivo.length() - archivo.getFilePointer())];
                archivo.read(bloque);   // Copio todo el bloque
                archivo.seek(eliminar.getPunteroADescripcion());    // Regreso al inicio del bloque de Descripción
                archivo.write(bloque);  // Escribo todo el bloque que va después (para sobreescribir el de 'eliminar')
                archivo.seek(eliminar.getPunteroADescripcion());    // Regreso al inicio del bloque de Descripción
                archivo.setLength(archivo.length() - 1 - eliminar.getDescripcion().length());   // Elimino el bloque inservible
                // Actualización del Puntero al Índice
                archivo.seek(6);
                archivo.writeShort(punteroAlIndice - 1 - eliminar.getDescripcion().length());   // Modifico el puntero al Índice
                
                archivo.close();
                /* Hasta aquí se garantiza la eliminación de la Descripción correspondiente a 'eliminar' */
            } else {
                JOptionPane.showMessageDialog(null, "No se puede encontró la informaicón de '"+eliminar.getNombreAutomata()+"'.", "Error!", JOptionPane.ERROR_MESSAGE, null);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManejoArchivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ManejoArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void modificarAutomata(IndiceAutomatas original, Automata modificado) {
        eliminarAutomata(original);
        guardarAutomata(modificado);
    }
    public IndiceAutomatas[] obtenerReferencias() {
        IndiceAutomatas[] referencias = null;
        RandomAccessFile archivo;
        try {
            /* Extracción de las referencias necesarias en Automatas_Estado-Finito/AFD/Automatas.afd */
            archivo = new RandomAccessFile(CARPETA_AFD + SEPARADOR + "Automatas.afd", "rw");
            String firma = "", tipo = "";
            for(int i=0; i<3; i++) firma+= (char)Byte.toUnsignedInt(archivo.readByte());
            for(int i=0; i<3; i++) tipo+= (char)Byte.toUnsignedInt(archivo.readByte());
            if ("AFD".equals(firma) == true && "dat".equals(tipo) == true) {
                int punteroAlIndice = Short.toUnsignedInt(archivo.readShort()); // Obtengo el Puntero al Índice
                archivo.seek(punteroAlIndice);  // Me muevo hasta el Índice
                int cantidadaRegistros = Byte.toUnsignedInt(archivo.readByte());    // Obtengo la Cantidad de Registros
                if (cantidadaRegistros!=0) {
                    referencias = new IndiceAutomatas[cantidadaRegistros];  // Inicializo el arreglo de referenecias
                    for(int i=0; i<cantidadaRegistros; i++) referencias[i] = new IndiceAutomatas();
                }

                // Inicio la extracción de las referencias
                for(int cont=0; cont<cantidadaRegistros; cont++) {
                    referencias[cont].setPunteroARefDatos((int)archivo.getFilePointer()); // Obtengo el Puntero en Índice
                    int longitudNombre = Byte.toUnsignedInt(archivo.readByte());    // Obtengo la longitud del i-ésimo nombre
                    String nombre = "";     // Obtengo el i-ésimo nombre
                    for(int i=0; i<longitudNombre; i++) nombre+= (char)Byte.toUnsignedInt(archivo.readByte());
                    referencias[cont].setNombreAutomata(nombre);
                    referencias[cont].setPunteroADatos(Short.toUnsignedInt(archivo.readShort()));   // Obtengo el Puntero a Datos
                }   // Hasta aquí se garantiza la extracción de las referencias de los datos de los autómatas
                archivo.close();    // Cierro el archivo
            } else {
                JOptionPane.showMessageDialog(null, "No se cargarán datos pues no se encontró el archivo!", "Error!", JOptionPane.ERROR_MESSAGE, null);
            }
            
            /* Extracción de las referencias necesarias en Automatas_Estado-Finito/AFD/Descripcion.afd */
            archivo = new RandomAccessFile(CARPETA_AFD + SEPARADOR + "Descripcion.afd", "rw");
            firma = tipo = "";
            for(int i=0; i<3; i++) firma+= (char)Byte.toUnsignedInt(archivo.readByte());
            for(int i=0; i<3; i++) tipo+= (char)Byte.toUnsignedInt(archivo.readByte());
            if ("AFD".equals(firma) == true && "inf".equals(tipo) == true) {
                int punteroAlIndice = Short.toUnsignedInt(archivo.readShort()); // Obtengo el Puntero al Índice
                archivo.seek(punteroAlIndice);  // Me muevo hasta el Índice
                int cantidadaRegistros = Byte.toUnsignedInt(archivo.readByte());    // Obtengo la Cantidad de Registros
                
                // Inicio la extracción de las descripciones y su posición en el archivo
                for(int cont=0; cont<cantidadaRegistros; cont++) {
                    long ultimaPocicionEnIndice = archivo.getFilePointer();
                    referencias[cont].setPunteroARefDescripcion((int)ultimaPocicionEnIndice); // Obtengo el puntero de esta i-ésima referencia a descripción
                    referencias[cont].setPunteroADescripcion(Short.toUnsignedInt(archivo.readShort())); // Obtengo el puntero a la i-ésima referencia
                    archivo.seek(referencias[cont].getPunteroADescripcion());   // Me muevo hasta la i-ésima Descripción
                    // Extraigo la i-ésima Descripción
                    String descripcion = "";
                    int longitud = Byte.toUnsignedInt(archivo.readByte());  // Obtengo la longitud de la i-ésima Descripción
                    for(int i=0; i<longitud; i++) descripcion+= (char)Byte.toUnsignedInt(archivo.readByte());
                    referencias[cont].setDescripcion(descripcion);  // Inserto la i-ésima Descripción
                    
                    archivo.seek(ultimaPocicionEnIndice+2);   // Regreso a la siguiente Referencia en el Índice
                }   // Hasta quí se garantiza la extracción de las descripciones de los autómatas
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el archivo con las descripcinoes", "Error!", JOptionPane.ERROR_MESSAGE, null);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManejoArchivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ManejoArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return referencias;
    }
    public Automata cargarAutomata(IndiceAutomatas referencia) {
        // Una parte de la información del Autómata que se cargará está en 'referencia'
        Automata nuevo = null;
        RandomAccessFile archivo;
        try {
            /** Extracción de los datos del Autómata en Automatas_Estado-Finito/AFD/Automatas.afd **/
            archivo = new RandomAccessFile(CARPETA_AFD + SEPARADOR + "Automatas.afd", "rw");
            String firma = "", tipo = "";
            for(int i=0; i<3; i++) firma+= (char)Byte.toUnsignedInt(archivo.readByte());
            for(int i=0; i<3; i++) tipo+= (char)Byte.toUnsignedInt(archivo.readByte());
            if ("AFD".equals(firma) == true && "dat".equals(tipo) == true) {
                /* Ya no es necesario realizar búsqueda pues los datos de referencia al Autómata que se quiere cargar están
                   en 'referencia'. Así que, voy directamente a los datos. La Descripción del Autómata está en 'referencia' */
                nuevo = new Automata();
                // Extracción de datos
                archivo.seek(referencia.getPunteroADatos());    // Salto al inicio de los datos
                int cantidadSimbolos = Byte.toUnsignedInt(archivo.readByte());  // Obtengo la Cantidad de Símbolos
                String[] alfabeto = new String[cantidadSimbolos];
                for(int i=0; i<cantidadSimbolos; i++)   // Obtengo el Alfabeto
                    alfabeto[i] = ""+(char)Byte.toUnsignedInt(archivo.readByte());
                int cantidadEstados = Byte.toUnsignedInt(archivo.readByte());   // Obtengo la Cantidad de Estados
                nuevo.setAlfabeto(alfabeto);    // Inserto el Alfabeto en el Autómata
                nuevo.setCantidadEstados(cantidadEstados);  // Inserto la Cantidad de Estados en el Autómata
                int longNombreEstados = Byte.toUnsignedInt(archivo.readByte()); // Obtengo la cantidad de caracteres del nombre de los estados
                String[] nombresEstados = new String[cantidadEstados];
                for(int i=0; i<cantidadEstados; i++) {  // Inicio la extracción de los Nombres de los Estados
                    nombresEstados[i] = "";
                    for(int j=0; j<longNombreEstados; j++) {
                        char caracter = (char)Byte.toUnsignedInt(archivo.readByte());   // Obtengo un caracter
                        if ((int)caracter != 0)
                            nombresEstados[i]+= caracter;
                    }
                }
                nuevo.setNombresEstados(nombresEstados);
                archivo.readByte();     // Lectura del Estado Inicial
                for(int i=0; i<cantidadEstados; i++) {  // Extracción del Tipo de Estado y sus Transiciones
                    nuevo.getEstados(i).setAceptable(archivo.readBoolean());    // Inserto el Tipo del i-ésimo Estado
                    for(int j=0; j<cantidadSimbolos; j++) {
                        nuevo.getEstados(i).setTransicion(j, nuevo.getEstados()[Byte.toUnsignedInt(archivo.readByte())]);
                    }
                }
                // Hasta aquí se garantiza la extracción de los datos del Autómata en el archivo.
                nuevo.setNombre(referencia.getNombreAutomata());
                nuevo.setDescripcion(referencia.getDescripcion());
                archivo.close();
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el archivo.", "Error!", JOptionPane.ERROR_MESSAGE, null);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManejoArchivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ManejoArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nuevo;
    }
    
    /**
     * Método que compara dos cadenas de tipo String y devuelve la relación que existe entre ellos; dicha relación es de
     * menor, igual o mayor. La comparación se realiza tomando caracter a caracter, de cada cadena, y comparando el valor
     * numérico de los caracteres ASCCI.
     * @param primeraCadena primer conjunto de caracteres a comparar
     * @param segundaCadena segundo conjunto de caracteres a comparar
     * @return La relación existente entre las cadenas de texto: '-1' si primeraCadena ES MENOR A segundaCadena,
     * '0' si primeraCadena ES IGUAL A segundaCadena, o '1' si primeraCadena ES MAYOR A segundaCadena.
     */
    private int compararCadenaPorOrden(String primeraCadena, String segundaCadena) {
        if (primeraCadena.equals(segundaCadena) == true)
            return 0;
        int cont = 0, resultado = 0;  // Inicialmente asumo que las cadenas son iguales
        boolean pasar = false;
        int long1 = primeraCadena.length(), long2 = segundaCadena.length();
        while (pasar == false) {
            if (cont < long1 && cont < long2) {
                if (primeraCadena.charAt(cont) < segundaCadena.charAt(cont)) {
                    resultado = -1;
                    pasar = true;
                }
                else {
                    resultado = 1;
                    pasar = true;
                }
                cont++;
            }
            else
                pasar = true;
        }
        return resultado;
    }
    private int longitudRegistroAutomata(RandomAccessFile archivo, int punteroADatos) throws IOException {
        // en 'punteroADatos' está la posición de inicio del registro del autómata, pero 'archivo' aún no está ahí
        int posOriginal = (int)archivo.getFilePointer(); // Obtengo la posición actual del puntero
        
        int longitud, cantSimbolos, cantEstados, cantCaracteres; // Variables auxiliares para obtener valores enterosn dentro del Bloque de Registro
        archivo.seek(punteroADatos);    // Me desplazo hasta el inicio del bloque del Registro
        cantSimbolos = Byte.toUnsignedInt(archivo.readByte());  // Obtengo la Cantidad de Símbolos
        archivo.skipBytes(cantSimbolos);    // Salto los símbolos
        longitud = 1 + cantSimbolos;    // Sumo el byte de la Cantidad de Simbolos y los N bytes de los Símbolos
        cantEstados = Byte.toUnsignedInt(archivo.readByte());  // Obtengo la Cantidad de Estados
        cantCaracteres = Byte.toUnsignedInt(archivo.readByte());  // Obtengo la cantidad de Caracteres del Nombre de los Estados
        longitud+= 2;   // Sumo el byte de Cantidad de Estados y el byte de Cantidad de Caracteres
        archivo.skipBytes(cantEstados*cantCaracteres);   // Salto los nombres de los N Estados
        longitud+= cantEstados*cantCaracteres;   // Sumo los N bytes de los Nombres de los Estados
        archivo.skipBytes(1);   // Salto el byte que indica quién es el Estado Inicial
        longitud++;     // Sumo el byte que indica quién es el Estado Inicial
        longitud+= cantEstados*(1+cantSimbolos);   // Sumo el Tipo y las Transiciones de todos los estados
        // No es necesario saltarlos ya que hay 'cantEstados' bloques con (1+cantSimbolos) bytes
        
        archivo.seek(posOriginal);   // Regreso nuevamente a la posición original
        return longitud;
    }
    private int longitudBloqueDatosAutomata(Automata A) {
        int longitud = 1;   // Para el byte de la cantidad de símbolos
        longitud+= A.getCantidadSimbolos(); // Para los bytes que conforman el Alfabeto
        longitud+=2;     // Para el byte de la cantidad de estados y la longitud de los nombres de los estados
        longitud+=(A.getCantidadEstados()*A.getLongitudNombreEstados());   // Para los bytes que conforman los nombres de los estados
        longitud++;     // Para el byte que indica cuál es el estado inicial
        longitud+=A.getCantidadEstados()*(1+A.getCantidadSimbolos());   // Para el byte del tipo de estado y las transiciones, para cada estado
        return longitud;
    }
    private int longitudBloqueDatosAFN(AFN A) {
        int longitud = 1;   // Para el byte de la cantidad de símbolos
        int contSimbolos = A.getCantidadSimbolos(), contEstados = A.getCantidadEstados();
        longitud+= contSimbolos; // Para los bytes que conforman el Alfabeto
        longitud+=2;     // Para el byte de la cantidad de estados y la longitud de los nombres de los estados
        longitud+=(contEstados*A.getLongitudNombreEstados());   // Para los bytes que conforman los nombres de los estados
        longitud++;     // Para el byte que indica cuál es el estado inicial
        // Ahora sumo los bytes que indicarán el tipo de estado y todas sus transiciones (incluidos los de la cadena vacía)
        for(int i=0; i<contEstados; i++) {
            longitud++; // Para el byte del tipo de estado (final o no final)
            for(int j=0; j<contSimbolos; j++) {
                longitud++; // Para el byte que indica cuántas transiciones tiene el estado[i] con el símbolo alfabeto[j]
                longitud+= A.getEstados()[i].getTransiciones(j).size();
            }
            longitud++; // Para el byte que indica cuántas transiciones tiene el estado[i] con la cadena vacía
            longitud+= A.getEstados()[i].getTransicionesNulas().size();
        }   // Hasta aquí ya tomé en cuenta todos los bytes que ocuparía un registro para el AFN
        return longitud;
    }
}
