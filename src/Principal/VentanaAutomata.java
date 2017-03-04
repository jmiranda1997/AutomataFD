/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Principal;
import Clases.Automata;
import Clases.Estado;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
/**
 *
 * @author jonathanmiranda
 */
public class VentanaAutomata implements ActionListener {
    /* DefiniciÛn de variables */
    Automata nuevo;     // AutÛmata a crear
    JDialog ventana;    // Ventana principal
    
    /* DefiniciÛn de Paneles Primarios */
    JPanel panel_alfabeto, panel_estados, panel_transiciones;
    /* DefiniciÛn de Paneles Secundarios (Los que van dentro de un Primario) */
    JPanel panel_etiquetas_alfabeto, panel_cajas_alfabeto, panel_etiquetas_estados, panel_cajas_estados, panel_etiquetas_transiciones, panel_cajas_transiciones;
    /*Panel para comprobar estado inicial*/
//    JPanel panel_estado_inicial;
    /* DefiniciÛn de etiquetas */
    JLabel[] etiquetas_estados, etiquetas_transiciones;
    JLabel titulo_alfabeto, titulo_estados, titulo_transiciones, titulo_estado_inicial;
    /* DefiniciÛn de JTextFiel */
    public JTextField[] cajas_alfabeto, cajas_estados;
    /* DefiniciÛn de ComboBox */
    JComboBox[] tipo_estado,cajas_transicion;
    
    /* DefiniciÛn de los botones */
    JButton guardar_alfabeto_estados, guardar_transiciones;
    
    /* DefiniciÛn de otras variables */
    int cantidadEstados, cantidadSimbolos;
    String[] alfabeto, nombreEstados;
    private String mensaje_error;
    private int contador_cajas;
    
    public VentanaAutomata(int cantidadEstados, int cantidadSimbolos){
        ventana = new JDialog();
        ventana.setModal(true);
        ventana.setLayout(null);
        this.cantidadEstados = cantidadEstados;
        this.cantidadSimbolos = cantidadSimbolos;
        this.contador_cajas = 0;
        nuevo = null;
        
        creacion_espacio_alfabeto_estados();
        
/*        panel_etiquetas_transiciones.setBounds(posX, posY, ancho, alto); */
        /* InicializaciÛn de los botones */
        // BotÛn para comprobar la validez del alfabeto
        guardar_alfabeto_estados = new JButton("Verificar");
        guardar_alfabeto_estados.addActionListener(this);
        guardar_alfabeto_estados.setBounds(75, panel_alfabeto.getY()+panel_alfabeto.getHeight(), 100, 30);
        ventana.add(guardar_alfabeto_estados);
        ventana.addWindowListener(new WindowAdapter() {
             @Override
             public void windowClosing(WindowEvent e) {
                nuevo = null;
                ventana.dispose();
             }
        });
        
        ventana.setLocation(100,50);
        //vent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        ventana.setSize(ancho, alto);
        ventana.setSize(700, panel_estados.getHeight()+100);
        ventana.setResizable(false);
        ventana.setTitle("Crear Automata");
        ventana.setVisible(true);
        
    }
    /**
     * Este mÈtodo crea e inserta las etiquetas del Alfabeto y los Estados, asÌ como las cajas de texto desde los cuales
     * se le asigna los valores a dichos objetos.
     */
    private void creacion_espacio_alfabeto_estados() {
        /* InicializaciÛn de los paneles secundarios que se agregar·n al 'panel_alfabeto' */
        // Panel de las etiquetas "Simbolo {i} = ":
        panel_etiquetas_alfabeto = new JPanel(new GridLayout(cantidadSimbolos, 1, 0, 0));
        panel_etiquetas_alfabeto.setBounds(10, 50, 85, 20*cantidadSimbolos);
        JLabel[] etiquetas_alfabeto = new JLabel[cantidadSimbolos];
        for(int i=0; i<cantidadSimbolos; i++) {
            etiquetas_alfabeto[i] = new JLabel("SÌmbolo "+(i+1)+" = ");
            panel_etiquetas_alfabeto.add(etiquetas_alfabeto[i]);
        }
        panel_etiquetas_alfabeto.setVisible(true);
        // Panel de las entradas de texto para los SÌmbolos
        panel_cajas_alfabeto = new JPanel(new GridLayout(cantidadSimbolos, 1, 0, 0));
        panel_cajas_alfabeto.setBounds(100, 50, 50, 30*cantidadSimbolos);
        cajas_alfabeto = new JTextField[cantidadSimbolos];
        
        for(int i=0; i<cantidadSimbolos; i++) {
            contador_cajas = i;
            cajas_alfabeto[i] = new JTextField();
            panel_cajas_alfabeto.add(cajas_alfabeto[i]);
        }
        panel_cajas_alfabeto.setVisible(true);
        
        /* InicializaciÛn del 'panel_alfabeto' que contendr· a los anteriores */
        titulo_alfabeto = new JLabel("DEFINICI”N DEL ALFABETO:");
        titulo_alfabeto.setBounds(50, 10, 200, 30);
        panel_alfabeto = new JPanel(new GridLayout(1, 2, 10, 10));
        panel_alfabeto.setBounds(50, titulo_alfabeto.getY()+titulo_alfabeto.getHeight(), 170, 30*cantidadSimbolos);
        panel_alfabeto.add(panel_etiquetas_alfabeto);
        panel_alfabeto.add(panel_cajas_alfabeto);
        panel_alfabeto.setVisible(true);
        
        ventana.add(titulo_alfabeto);
        ventana.add(panel_alfabeto);
        
        /* InicializaciÛn de los paneles secundarios que se agregar·n al 'panel_estados' */
        // Panel de las etiquetas "Simbolo {i} = ":
        panel_etiquetas_estados = new JPanel(new GridLayout(cantidadEstados, 1, 0, 0));
        panel_etiquetas_estados.setBounds(10, 50, 85, 20*cantidadEstados);
        JLabel[] etiqueta_estado = new JLabel[cantidadEstados];
        for(int i=0; i<cantidadEstados; i++) {
            etiqueta_estado[i] = new JLabel("Estado "+(i+1)+" = ");
            panel_etiquetas_estados.add(etiqueta_estado[i]);
        }
        panel_etiquetas_estados.setVisible(true);
        // Panel de las entradas de texto para los Estados
        panel_cajas_estados = new JPanel(new GridLayout(cantidadEstados, 1, 0, 0));
        panel_cajas_estados.setBounds(100, 50, 50, 30*cantidadEstados);
        cajas_estados = new JTextField[cantidadEstados];
        for(int i=0; i<cantidadEstados; i++) {
            cajas_estados[i] = new JTextField(3);
            panel_cajas_estados.add(cajas_estados[i]);
        }
        panel_cajas_estados.setVisible(true);
        
        /* InicializaciÛn del 'panel_estados' que contendr· a los anteriores */
        titulo_estados = new JLabel("DEFINICI”N DE LOS ESTADOS:");
        titulo_estados.setBounds(300, 10, 200, 30);
        titulo_estado_inicial = new JLabel("(Este es el Estado Inicial)");
        titulo_estado_inicial.setBounds(525, 45, 150, 25);
        panel_estados = new JPanel(new GridLayout(1, 3, 10, 10));
        panel_estados.setBounds(300, titulo_estados.getY()+titulo_estados.getHeight(), 220, 30*cantidadEstados);
        panel_estados.add(panel_etiquetas_estados);
        panel_estados.add(panel_cajas_estados);
        panel_estados.setVisible(true);
        
        ventana.add(titulo_estado_inicial);
        ventana.add(titulo_estados);
        ventana.add(panel_estados);
    }
    private void creacion_espacio_transiciones() {
        /* InicializaciÛn de los paneles secundarios que se agregar·n al 'panel_transiciones' */
        // Panel de etiquetas para los nombreEstados:
        int cantidad_etiquetas = (cantidadSimbolos + 1)*cantidadEstados;
        int contador = 0;
        panel_etiquetas_transiciones = new JPanel(new GridLayout(cantidad_etiquetas, 1, 0, 0));
        etiquetas_transiciones = new JLabel[cantidad_etiquetas];
        for(int i=0; i<cantidadEstados; i++) {
            etiquetas_transiciones[contador] = new JLabel("El Estado '"+nombreEstados[i]+"' es final?");
            panel_etiquetas_transiciones.add(etiquetas_transiciones[contador]);
            contador++;
            for(int j=0; j<cantidadSimbolos; j++) {
                etiquetas_transiciones[contador] = new JLabel("("+nombreEstados[i]+","+alfabeto[j]+") =");
                panel_etiquetas_transiciones.add(etiquetas_transiciones[contador]);
                contador++;
            }
        }
        panel_etiquetas_transiciones.setVisible(true);
        // Panel de entradas de informaciÛn de los nombreEstados (tipo y transiciones):
        panel_cajas_transiciones = new JPanel(new GridLayout(cantidad_etiquetas, 1, 0, 0));
        tipo_estado = new JComboBox[cantidadEstados];
        cajas_transicion = new JComboBox[cantidadSimbolos*cantidadEstados];
        contador = 0;
        for(int i=0; i<cantidadEstados; i++) {
            tipo_estado[i] = new JComboBox();
            tipo_estado[i].addItem("No");
            tipo_estado[i].addItem("Si");
            panel_cajas_transiciones.add(tipo_estado[i]);
            for(int j=0; j<cantidadSimbolos; j++) {
                cajas_transicion[contador] = new JComboBox();
                for (int k = 0; k < cantidadEstados; k++) {
                    cajas_transicion[contador].addItem(cajas_estados[k].getText());
                }
                panel_cajas_transiciones.add(cajas_transicion[contador]);
                contador++;
            }
        }
        panel_cajas_transiciones.setVisible(true);
        
        /* InicializaciÛn del 'panel_transiciones' que contendr· a los anteriores */
        titulo_transiciones = new JLabel("DEFINICI”N DE LAS TRANSICIONES:");
        titulo_transiciones.setBounds(100, 10, 250, 30);
        titulo_transiciones.setVisible(false);
        panel_transiciones = new JPanel(new GridLayout(1, 2, 10, 10));
        panel_transiciones.setBounds(10, 10, 250, 550);
        panel_transiciones.add(panel_etiquetas_transiciones);
        panel_transiciones.add(panel_cajas_transiciones);
        panel_transiciones.setVisible(false);
        
        ventana.add(titulo_transiciones);
    }
    /**
     * FunciÛn que controla los caracteres insertados como SÌmbolos del Alfabeto. Los mensajes de error se generan si un
     * sÌmbolo es nulo o si dos sÌmbolos son el mismo caracter.
     * @return 'true' si no hay caracteres repetidos o nulos; 'false' si los hay.
     */
    private boolean esAlfabetoValido() {
        for(int cont=0; cont<cantidadSimbolos; cont++) {
            String simbolo = cajas_alfabeto[cont].getText();   // Obtengo el i-esimo simbolo
            if (simbolo.length() == 0) {
                mensaje_error+="\nNo puede definir el SÌmbolo "+(cont+1)+" como nulo";
                return false;
            }
            if (simbolo.length() > 1) {
                mensaje_error+="\nNo puede definir el SÌmbolo "+(cont+1)+" con m·s de un caracter";
                return false;
            }
            for(int cont2=cont+1; cont2<cantidadSimbolos; cont2++) {
                String simbolo2 = cajas_alfabeto[cont2].getText();
                if (simbolo2.equals(simbolo) == true) {
                    mensaje_error+="\nLos SÌmbolos "+(cont+1)+" y "+(cont2+1)+" no deben ser iguales";
                    return false;
                }
            }
        }
        return true;
    }
    /**
     * FunciÛn que controla los caracteres insertados como Nombre de los Estados. Los mensajes de error se generan si un
     * estado es nulo o si dos estados tienen el mismo nombre.
     * @return 'true' si no hay nombres nulos o repetidos; 'false' si los hay.
     */
    private boolean sonEstadosValidos() {
        for(int cont=0; cont<cantidadEstados; cont++) {
            String estado = cajas_estados[cont].getText();
            if (estado.length() == 0) {
                mensaje_error+="\nNo se puede definir el Estado "+(cont+1)+" como nulo.";
                return false;
            }
            for(int cont2=cont+1; cont2<cantidadEstados; cont2++) {
                String estado2 = cajas_estados[cont2].getText();
                if (estado2.equals(estado) == true) {
                    mensaje_error+="\nLos Estados "+(cont+1)+" y "+(cont2+1)+" no deben ser iguales";
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // Cuando se quiere guardar el alfabeto: inicio la commprobaciÛn del mismo
        if (e.getSource() == guardar_alfabeto_estados) {
            mensaje_error = "Error.\n";
            boolean alfabetoValido = esAlfabetoValido();
            boolean estadosValidos = sonEstadosValidos();
            if (alfabetoValido == false || estadosValidos == false) {
                JOptionPane.showMessageDialog(ventana, mensaje_error, "Error en Alfabeto o Estados", JOptionPane.ERROR_MESSAGE, null);
            } else {    // Los SÌmbolos y Estados son correctos
                // Inicio el guardado de los SÌmbolos del Alfabeto:
                alfabeto = new String[cantidadSimbolos];
                for(int cont=0; cont<cantidadSimbolos; cont++)
                    alfabeto[cont] = cajas_alfabeto[cont].getText();
                // Inicio el guardado de los Nombres de los Estados:
                nombreEstados = new String[cantidadEstados];
                for(int cont=0; cont<cantidadEstados; cont++)
                    nombreEstados[cont] = cajas_estados[cont].getText();
                
                creacion_espacio_transiciones();
                // BotÛn para comprobar la validez del AutÛmata
                guardar_transiciones = new JButton("Guardar");
                guardar_transiciones.addActionListener(this);
                guardar_transiciones.setBounds(150, panel_transiciones.getY()+panel_transiciones.getHeight()+35, 100, 30);
                guardar_transiciones.setVisible(true);
                ventana.add(guardar_transiciones);
                JScrollPane scroll = new JScrollPane(panel_transiciones, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                scroll.setBounds(50, titulo_transiciones.getY()+titulo_transiciones.getHeight(), 350, 550);
                ventana.add(scroll);
                
                ventana.setSize(450, panel_transiciones.getHeight()+125);
                
                // OcultaciÛn de los componentes
                titulo_estado_inicial.setVisible(false);
                titulo_alfabeto.setVisible(false);
                panel_alfabeto.setVisible(false);
                titulo_estados.setVisible(false);
                panel_estados.setVisible(false);
                titulo_transiciones.setVisible(true);
                panel_transiciones.setVisible(true);
                guardar_alfabeto_estados.setVisible(false);
                
                guardar_transiciones.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                          crearAutomata();
                    }
                });
            }
        }
        
    }
    private void crearAutomata() {
        // CreaciÛn del Alfabeto
        String[] alfabeto = new String[cantidadSimbolos];
        for(int i=0; i<cantidadSimbolos; i++)
            alfabeto[i] = cajas_alfabeto[i].getText();
        // CreaciÛn del nuevo AutÛmata
        nuevo = new Automata(alfabeto, cantidadEstados);
        // CreaciÛn de los Estados y definiciÛn de sus nombres
        Estado[] estados = new Estado[cantidadEstados];
        for(int i=0; i<cantidadEstados; i++) {
            estados[i] = new Estado(cantidadSimbolos);
            estados[i].setSimbolo(cajas_estados[i].getText());
        }
        // DefiniciÛn de los Tipos de Estados (finales o no finales)
        for(int i=0; i<cantidadEstados; i++) {
            String tipo = (String)tipo_estado[i].getItemAt(i);  // Obtengo "Si" o "No"
            if ("Si".equals(tipo) == true)
                estados[i].setAceptable(true);
            else
                estados[i].setAceptable(false);
        }
        // DefiniciÛn de las Transiciones
        int contadorTransicion = 0;
        for(int contEstado=0; contEstado<cantidadEstados; contEstado++) {
            for(int contSimbolo=0; contSimbolo<cantidadSimbolos; contSimbolo++) {
                String siguienteEstado = (String)cajas_transicion[contadorTransicion].getSelectedItem();    // Obtengo la TransiciÛn
                // Inicio la B˙squeda para determinar cu·l es
                for(int cont=0; cont<cantidadEstados; cont++) {
                    if (estados[cont].getSimbolo().equals(siguienteEstado) == true) {   // Si encuentra el Estado Destino
                        estados[contEstado].setTransicion(contSimbolo, estados[cont]);
                        cont = cantidadEstados;
                    }
                }
                contadorTransicion++;
            }
        }   // Hasta aquÌ se garantiza la construcciÛn de todas las Transiciones
        nuevo.setEstados(estados);  // Inserto los Estados, con las transiciones ya definidas
        ventana.dispose();      // Cierro la ventana
    }
    public Automata getNuevo() { return nuevo; }
    public void setNuevo(Automata nuevo) { this.nuevo = nuevo; }
}

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package Principal;
//import Clases.Automata;
//import Clases.Estado;
//import java.awt.GridLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//import javax.swing.*;
///**
// *
// * @author jonathanmiranda
// */
//public class VentanaAutomata implements ActionListener {
//    /*Los utilizo para poder verificar que estado es inicial*/
//    JRadioButton[] etiqueta_estado_inicial;
//    int estado_inicial;
//    
//    private ActionListener identificar_estado_inicial = new ActionListener() {
//        @Override
//        public void actionPerformed(ActionEvent ae) {
//            int a =0;
//            boolean encontrado = false;
//            while(encontrado !=true){
//                if(ae.getSource() == etiqueta_estado_inicial[a]){
//                    encontrado =true;
//                }
//                else a++;
//            }
//            etiqueta_estado_inicial[estado_inicial].setSelected(false);
//          estado_inicial = a;   
//       }
//    };
//    
//    /*Automata que se crea*/
//    Automata nuevo;
//    /* Definicón de la ventana principal */
//    JDialog ventana;
//    
//    /* Definición de Paneles Primarios */
//    JPanel panel_alfabeto, panel_estados, panel_transiciones;
//    /* Definición de Paneles Secundarios (Los que van dentro de un Primario) */
//    JPanel panel_etiquetas_alfabeto, panel_cajas_alfabeto, panel_etiquetas_estados, panel_cajas_estados, panel_etiquetas_transiciones, panel_cajas_transiciones;
//    /*Panel para comprobar estado inicial*/
//    JPanel panel_estado_inicial;
//    /* Definición de etiquetas */
//    JLabel[] etiquetas_estados, etiquetas_transiciones;
//    JLabel titulo_alfabeto, titulo_estados, titulo_transiciones;
//    /* Definición de JTextFiel */
//    JTextField[] cajas_alfabeto, cajas_estados;
//    /* Definición de ComboBox */
//    JComboBox[] tipo_estado,cajas_transicion;
//    
//    /* Definición de los botones */
//    JButton guardar_alfabeto_estados, guardar_transiciones;
//    
//    /* Definición de otras variables */
//    int cantidadEstados, cantidadSimbolos;
//    String[] alfabeto, estados;
//    
//    public VentanaAutomata(int cantidadEstados, int cantidadSimbolos){
//        ventana = new JDialog();
//        ventana.setModal(true);
//        ventana.setLayout(null);
//        estado_inicial =0;   
//        this.cantidadEstados = cantidadEstados;
//        this.cantidadSimbolos = cantidadSimbolos;
//        nuevo = null;
//        
//        creacion_espacio_alfabeto_estados();
//        
///*        panel_etiquetas_transiciones.setBounds(posX, posY, ancho, alto); */
//        /* Inicialización de los botones */
//        // Botón para comprobar la validez del alfabeto
//        guardar_alfabeto_estados = new JButton("Verificar");
//        guardar_alfabeto_estados.addActionListener(this);
//        guardar_alfabeto_estados.setBounds(75, panel_alfabeto.getY()+panel_alfabeto.getHeight(), 100, 30);
//        ventana.add(guardar_alfabeto_estados);
//        ventana.addWindowListener(new WindowAdapter() {
//            
//             public void windowClosing(WindowEvent e){
//                nuevo = null;
//                //ventana.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
//                ventana.dispose();
//             }
//        });
//        
//        ventana.setLocation(100,50);
//        //vent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
////        ventana.setSize(ancho, alto);
//        ventana.setSize(1000, panel_alfabeto.getHeight()+200);
//        ventana.setResizable(false);
//        ventana.setTitle("Crear Automata");
//        ventana.setVisible(true);
//        
//    }
//    private void creacion_espacio_alfabeto_estados() {
//        /* Inicialización de los paneles secundarios que se agregarán al 'panel_alfabeto' */
//        // Panel de las etiquetas "Simbolo {i} = ":
//        panel_etiquetas_alfabeto = new JPanel(new GridLayout(cantidadSimbolos, 1, 0, 0));
//        panel_etiquetas_alfabeto.setBounds(10, 50, 85, 20*cantidadSimbolos);
//        JLabel[] etiquetas_alfabeto = new JLabel[cantidadSimbolos];
//        for(int i=0; i<cantidadSimbolos; i++) {
//            etiquetas_alfabeto[i] = new JLabel("Símbolo "+(i+1)+" = ");
//            panel_etiquetas_alfabeto.add(etiquetas_alfabeto[i]);
//        }
//        panel_etiquetas_alfabeto.setVisible(true);
//        // Panel de las entradas de texto para los Símbolos
//        panel_cajas_alfabeto = new JPanel(new GridLayout(cantidadSimbolos, 1, 0, 0));
//        panel_cajas_alfabeto.setBounds(100, 50, 50, 30*cantidadSimbolos);
//        cajas_alfabeto = new JTextField[cantidadSimbolos];
//        for(int i=0; i<cantidadSimbolos; i++) {
//            cajas_alfabeto[i] = new JTextField();
//            panel_cajas_alfabeto.add(cajas_alfabeto[i]);
//        }
//        panel_cajas_alfabeto.setVisible(true);
//        
//        /* Inicialización del 'panel_alfabeto' que contendrá a los anteriores */
//        titulo_alfabeto = new JLabel("DEFINICIÓN DEL ALFABETO:");
//        titulo_alfabeto.setBounds(50, 50, 200, 30);
//        panel_alfabeto = new JPanel(new GridLayout(1, 2, 10, 10));
//        panel_alfabeto.setBounds(50, titulo_alfabeto.getY()+titulo_alfabeto.getHeight(), 170, 30*cantidadSimbolos);
//        panel_alfabeto.add(panel_etiquetas_alfabeto);
//        panel_alfabeto.add(panel_cajas_alfabeto);
//        panel_alfabeto.setVisible(true);
//        
//        ventana.add(titulo_alfabeto);
//        ventana.add(panel_alfabeto);
//        
//        /* Inicialización de los paneles secundarios que se agregarán al 'panel_estados' */
//        // Panel de las etiquetas "Simbolo {i} = ":
//        panel_estado_inicial = new JPanel(new GridLayout(cantidadEstados,1));
//        etiqueta_estado_inicial = new JRadioButton[cantidadEstados];
//        panel_etiquetas_estados = new JPanel(new GridLayout(cantidadEstados, 1, 0, 0));
//        panel_etiquetas_estados.setBounds(10, 50, 85, 20*cantidadEstados);
//        JLabel[] etiqueta_estado = new JLabel[cantidadEstados];
//        for(int i=0; i<cantidadEstados; i++) {
//            etiqueta_estado[i] = new JLabel("Estado "+(i+1)+" = ");
//            panel_etiquetas_estados.add(etiqueta_estado[i]);
//            if(i == 0) etiqueta_estado_inicial[i] = new JRadioButton("EI", true);
//            else  etiqueta_estado_inicial[i] = new JRadioButton("EI", false);
//            
//            etiqueta_estado_inicial[i].addActionListener(identificar_estado_inicial);
//            
//            panel_estado_inicial.add(etiqueta_estado_inicial[i]);
//        }
//        panel_etiquetas_estados.setVisible(true);
//        // Panel de las entradas de texto para los Estados
//        panel_cajas_estados = new JPanel(new GridLayout(cantidadEstados, 1, 0, 0));
//        panel_cajas_estados.setBounds(100, 50, 50, 30*cantidadEstados);
//        cajas_estados = new JTextField[cantidadEstados];
//        for(int i=0; i<cantidadEstados; i++) {
//            cajas_estados[i] = new JTextField(3);
//            panel_cajas_estados.add(cajas_estados[i]);
//        }
//        panel_cajas_estados.setVisible(true);
//        
//        /* Inicialización del 'panel_estados' que contendrá a los anteriores */
//        titulo_estados = new JLabel("DEFINICIÓN DE LOS ESTADOS:");
//        titulo_estados.setBounds(300, 10, 200, 30);
//        panel_estados = new JPanel(new GridLayout(1, 3, 10, 10));
//        panel_estados.setBounds(300, titulo_estados.getY()+titulo_estados.getHeight(), 220, 30*cantidadEstados);
//        panel_estados.add(panel_etiquetas_estados);
//        panel_estados.add(panel_cajas_estados);
//        panel_estados.add(panel_estado_inicial);
//        panel_estados.setVisible(true);
//        
//        ventana.add(titulo_estados);
//        ventana.add(panel_estados);
//    }
//    private void creacion_espacio_transiciones() {
//        /* Inicialización de los paneles secundarios que se agregarán al 'panel_transiciones' */
//        // Panel de etiquetas para los estados:
//        int cantidad_etiquetas = (cantidadSimbolos + 1)*cantidadEstados;
//        int contador = 0;
//        panel_etiquetas_transiciones = new JPanel(new GridLayout(cantidad_etiquetas, 1, 0, 0));
//        etiquetas_transiciones = new JLabel[cantidad_etiquetas];
//        for(int i=0; i<cantidadEstados; i++) {
//            etiquetas_transiciones[contador] = new JLabel("El estado '"+estados[i]+"' es final?");
//            panel_etiquetas_transiciones.add(etiquetas_transiciones[contador]);
//            contador++;
//            for(int j=0; j<cantidadSimbolos; j++) {
//                etiquetas_transiciones[contador] = new JLabel("("+estados[i]+","+alfabeto[j]+") =");
//                panel_etiquetas_transiciones.add(etiquetas_transiciones[contador]);
//                contador++;
//            }
//        }
//        panel_etiquetas_transiciones.setVisible(true);
//        // Panel de entradas de información de los estados (tipo y transiciones):
//        panel_cajas_transiciones = new JPanel(new GridLayout(cantidad_etiquetas, 1, 0, 0));
//        tipo_estado = new JComboBox[cantidadEstados];
//        cajas_transicion = new JComboBox[cantidadSimbolos*cantidadEstados];
//        contador = 0;
//        for(int i=0; i<cantidadEstados; i++) {
//            tipo_estado[i] = new JComboBox();
//            tipo_estado[i].addItem("No");
//            tipo_estado[i].addItem("Si");
//            panel_cajas_transiciones.add(tipo_estado[i]);
//            for(int j=0; j<cantidadSimbolos; j++) {
//                cajas_transicion[contador] = new JComboBox();
//                for (int k = 0; k < cantidadEstados; k++) {
//                    cajas_transicion[contador].addItem(cajas_estados[k].getText());
//                }
//                panel_cajas_transiciones.add(cajas_transicion[contador]);
//                contador++;
//            }
//        }
//        panel_cajas_transiciones.setVisible(true);
//        
//        /* Inicialización del 'panel_transiciones' que contendrá a los anteriores */
//        titulo_transiciones = new JLabel("DEFINICIÓN DE LAS TRANSICIONES:");
//        titulo_transiciones.setBounds(500, 10, 200, 30);
//        titulo_transiciones.setVisible(false);
//        panel_transiciones = new JPanel(new GridLayout(1, 2, 10, 10));
//        panel_transiciones.setBounds(500, titulo_transiciones.getY()+titulo_transiciones.getHeight(), 250, 500);
//        panel_transiciones.add(panel_etiquetas_transiciones);
//        panel_transiciones.add(panel_cajas_transiciones);
//        panel_transiciones.setVisible(false);
//        
//        ventana.add(titulo_transiciones);
//    }
//    private boolean esAlfabetoValido(String mensaje) {
//        for(int cont=0; cont<cantidadSimbolos; cont++) {
//            String simbolo = cajas_alfabeto[cont].getText();   // Obtengo el i-esimo simbolo
//            if (simbolo.length() == 0) {
//                mensaje+="\nNo puede definir el Símbolo "+(cont+1)+" como nulo";
//                return false;
//            }
//            else if (simbolo.length() > 1) {
//                mensaje+="\nNo puede definir el Símbolo "+(cont+1)+" con más de un caracter";
//                return false;
//            }
//            for(int cont2=cont+1; cont2<cantidadSimbolos; cont2++) {
//                String simbolo2 = cajas_alfabeto[cont2].getText();
//                if (simbolo2.equals(simbolo) == true) {
//                    mensaje+="\nLos Símbolos "+(cont+1)+" y "+(cont2+1)+" son iguales";
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//    private boolean sonEstadosValidos(String mensaje) {
//        for(int cont=0; cont<cantidadEstados; cont++) {
//            String estado = cajas_estados[cont].getText();
//            if (estado.length() == 0) {
//                mensaje+="\n";
//                return false;
//            }
//            for(int cont2=cont+1; cont2<cantidadEstados; cont2++) {
//                String estado2 = cajas_estados[cont2].getText();
//                if (estado2.equals(estado) == true)
//                    return false;
//            }
//        }
//        return true;
//    }
//    
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        // Cuando se quiere guardar el alfabeto: inicio la commprobación del mismo
//        if (e.getSource() == guardar_alfabeto_estados) {
//            String mensaje = "Error.";
//            boolean alfabetoValido = esAlfabetoValido(mensaje);
//            boolean estadosValidos = sonEstadosValidos(mensaje);
//            if (alfabetoValido == false || estadosValidos == false) {
//                JOptionPane.showMessageDialog(ventana, mensaje, "Error en Alfabeto o Estadeos", JOptionPane.ERROR_MESSAGE, null);
//            }
//            else {
//                JOptionPane.showMessageDialog(ventana, "Alfabeto y Estados correctos", "Alfabeto y Estados guardado", JOptionPane.INFORMATION_MESSAGE, null);
//                // Inicio el guardado de los Símbolos del Alfabeto:
//                alfabeto = new String[cantidadSimbolos];
//                for(int cont=0; cont<cantidadSimbolos; cont++)
//                    alfabeto[cont] = cajas_alfabeto[cont].getText();
//                // Inicio el guardado de los Nombres de los Estados:
//                estados = new String[cantidadEstados];
//                for(int cont=0; cont<cantidadEstados; cont++)
//                    estados[cont] = cajas_estados[cont].getText();
//                
//                creacion_espacio_transiciones();
//                // Botón para comprobar la validez del Autómata
//                guardar_transiciones = new JButton("Guardar");
//                guardar_transiciones.addActionListener(this);
//                guardar_transiciones.setBounds(350, panel_transiciones.getY()+panel_transiciones.getHeight(), 100, 30);
//                guardar_transiciones.setVisible(true);
//                ventana.add(guardar_transiciones);
//                ventana.add(panel_transiciones);
//                ventana.setSize(1000, panel_transiciones.getHeight()+150);
//                
//                // Ocultación de los componentes
//                titulo_alfabeto.setVisible(false);
//                panel_alfabeto.setVisible(false);
//                titulo_estados.setVisible(false);
//                panel_estados.setVisible(false);
//                titulo_transiciones.setVisible(true);
//                panel_transiciones.setVisible(true);
//                guardar_alfabeto_estados.setVisible(false);
//                
//                guardar_transiciones.addActionListener(new ActionListener() {
//                    @Override
//                    public void actionPerformed(ActionEvent ae) {
//                          Guardar_Automata();
//                    }
//                });
//            }
//        }
//        
//    }
//    
//    private void Guardar_Automata(){
//        /*Construir el alfabeto*/
//        String[] Alfabeto = new String[cajas_alfabeto.length];
//        for (int i = 0; i < alfabeto.length; i++) {
//            Alfabeto[i] = cajas_alfabeto[i].getText();
//        }
//        
//        /*Creo el arreglo de Estados*/
//        Estado[] Estados = new Estado[cajas_estados.length];
//        int c=1;
//        int b=0;
//        for (int i = 0; i < Estados.length; i++) {
//            Estado nuevo = new Estado(Alfabeto.length);
//            String tipo = (String)(tipo_estado[i].getSelectedItem());
//            nuevo.setSimbolo(cajas_estados[i].getText());
//                if(tipo.equals("Si")==true){
//                    nuevo.setAceptable(true);
//                }
//                else nuevo.setAceptable(false);
//            if(i != estado_inicial){
//               Estados[i] = nuevo;                
//               c++;
//            }
//            else{
//                Estados[0]= nuevo;
//                b=i;
//            }
//        }
//        
//        /*Conecto las transiciones*/
//        int N_caja_transicion =0;
//        for (int i = 0; i < Estados.length; i++) {
//           
//            for (int j = 0; j < Alfabeto.length; j++) {
//                int N_estado = 0;
//                String transicion = (String)cajas_transicion[N_caja_transicion].getSelectedItem();
//                transicion.replaceAll(" ", "");
//                boolean encontrado = false;
//                while(encontrado!=true){
//                    if(transicion.equals(Estados[N_estado].getSimbolo())== true){
//                        if(b!=0){
//                            if(i==0){
//                                Estados[b].setTransicion(j, Estados[N_estado]);
//                            }
//                            else{
//                                Estados[i].setTransicion(j, Estados[N_estado]);
//                            }
//                        
//                        }
//                        else Estados[i].setTransicion(j, Estados[N_estado]);
//                        
//                        encontrado = true;
//                    }
//                    else{
//                        N_estado++;
//                    }
//                }
//                N_caja_transicion++;
//            }
//        }
//        
//        /*Si el inicial no esta en la posicion 0 lo reacomodo*/
//        
//        nuevo = new Automata(Alfabeto, Estados.length);
//        
//        nuevo.setEstados(Estados);
//        ventana.dispose();
//    } 
//
//    public Automata getNuevo() {
//        return nuevo;
//    }
//
//    public void setNuevo(Automata nuevo) {
//        this.nuevo = nuevo;
//    }
//    
//}
