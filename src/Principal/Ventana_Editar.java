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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author USUARIO
 */
public class Ventana_Editar {
    JDialog ventana;
    JPanel panel_etiquetas_transiciones;
    JLabel[] etiquetas_transiciones;
    Estado[] estados;
    String[] alfabeto;
    JButton guardar;
    JPanel panel_cajas_transiciones;
    JComboBox[] tipo_estado, cajas_transicion;
    JPanel panel_transiciones;
    Automata nuevo, principal;
    public Ventana_Editar(Automata automata){
         /* Inicialización de los paneles secundarios que se agregarán al 'panel_transiciones' */
        // Panel de etiquetas para los estados:
        principal = automata;
        ventana = new JDialog();
         ventana.setLayout(null);
         ventana.setModal(true);
         estados = automata.getEstados();
         guardar = new JButton("Guardar Cambios");
            guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                    /*Conecto las transiciones*/
                    int N_caja_transicion =0;
                    for (int i = 0; i < automata.getCantidadEstados(); i++) {

                        for (int j = 0; j < automata.getCantidadSimbolos(); j++) {
                            int N_estado = 0;
                            String transicion = (String)cajas_transicion[N_caja_transicion].getSelectedItem();
                            transicion.replaceAll(" ", "");
                            boolean encontrado = false;
                            while(encontrado!=true){
                                if(transicion.equals(estados[N_estado].getSimbolo())== true){
                                       estados[i].setTransicion(j, estados[N_estado]);
                                    encontrado = true;
                                }
                                else{
                                    N_estado++;
                                }
                            }
                            N_caja_transicion++;
                        }
                        String tipo = (String)tipo_estado[i].getSelectedItem();
                        if(tipo.equals("Si")==true){
                            estados[i].setAceptable(true);
                        }
                        else{
                            estados[i].setAceptable(false);
                        }
                    }

                    /*Si el inicial no esta en la posicion 0 lo reacomodo*/

                    nuevo = new Automata(alfabeto, estados.length);

                    nuevo.setEstados(principal.getEstados());
                    nuevo.setNombre(principal.getNombre());
                    nuevo.setDescripcion(principal.getDescripcion());
                    ventana.dispose();
            }
        });
         estados = automata.getEstados();
         alfabeto = automata.getAlfabeto();
        int cantidad_etiquetas = (automata.getCantidadSimbolos() + 1)*automata.getCantidadEstados();
        int contador = 0;
        panel_etiquetas_transiciones = new JPanel(new GridLayout(cantidad_etiquetas, 1, 0, 0));
        etiquetas_transiciones = new JLabel[cantidad_etiquetas];
        for(int i=0; i<automata.getCantidadEstados(); i++) {
            etiquetas_transiciones[contador] = new JLabel("El estado '"+estados[i].getSimbolo()+"' es final?");
            panel_etiquetas_transiciones.add(etiquetas_transiciones[contador]);
            contador++;
            for(int j=0; j<automata.getCantidadSimbolos(); j++) {
                etiquetas_transiciones[contador] = new JLabel("("+estados[i].getSimbolo()+","+alfabeto[j]+") =");
                panel_etiquetas_transiciones.add(etiquetas_transiciones[contador]);
                contador++;
            }
            
        }
        panel_etiquetas_transiciones.setVisible(true);
        // Panel de entradas de información de los estados (tipo y transiciones):
        panel_cajas_transiciones = new JPanel(new GridLayout(cantidad_etiquetas, 1, 0, 0));
        tipo_estado = new JComboBox[automata.getCantidadEstados()];
        cajas_transicion = new JComboBox[automata.getCantidadSimbolos()*automata.getCantidadEstados()];
        contador = 0;
        for(int i=0; i<automata.getCantidadEstados(); i++) {
            tipo_estado[i] = new JComboBox();
            tipo_estado[i].addItem("No");
            tipo_estado[i].addItem("Si");
            panel_cajas_transiciones.add(tipo_estado[i]);
            for(int j=0; j<automata.getCantidadSimbolos(); j++) {
                cajas_transicion[contador] = new JComboBox();
                for (int k = 0; k < automata.getCantidadEstados(); k++) {
                    cajas_transicion[contador].addItem(estados[k].getSimbolo());
                }
                panel_cajas_transiciones.add(cajas_transicion[contador]);
                cajas_transicion[contador].setSelectedItem(estados[i].getTransicion(j).getSimbolo());
                contador++;
            }
            String tipo;
            if(estados[i].esAceptable()==true){
                tipo = "Si";
            }
            else{
                tipo = "No";
            }
            tipo_estado[i].setSelectedItem(tipo);
        }
        panel_cajas_transiciones.setVisible(true);
        
        /* Inicialización del 'panel_transiciones' que contendrá a los anteriores */
        JLabel titulo_transiciones = new JLabel("DEFINICIÓN DE LAS TRANSICIONES:");
        titulo_transiciones.setBounds(500, 10, 200, 30);
        titulo_transiciones.setVisible(false);
        panel_transiciones = new JPanel(new GridLayout(1, 2, 10, 10));
        panel_transiciones.setBounds(500, titulo_transiciones.getY()+titulo_transiciones.getHeight(), 250, 500);
        panel_transiciones.add(panel_etiquetas_transiciones);
        panel_transiciones.add(panel_cajas_transiciones);
        panel_transiciones.setVisible(false);
        guardar.setBounds(350, panel_transiciones.getY()+panel_transiciones.getHeight(), 100, 30);
        
         ventana.add(panel_transiciones);
         ventana.add(guardar);
         ventana.setSize(1000, panel_transiciones.getHeight()+150);
                
        titulo_transiciones.setVisible(true);
        panel_transiciones.setVisible(true);
        ventana.setVisible(true);
        
     
        
    }

    public Automata getNuevo() {
        return nuevo;
    }
    
   
}
